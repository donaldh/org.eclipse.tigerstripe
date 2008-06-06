/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.espace.resources.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.tigerstripe.espace.core.IEMFDatabase;
import org.eclipse.tigerstripe.espace.resources.ResourceHelper;
import org.eclipse.tigerstripe.espace.resources.ResourceList;
import org.eclipse.tigerstripe.espace.resources.ResourcesFactory;
import org.eclipse.tigerstripe.espace.resources.ResourcesPlugin;

/**
 * @author Yuri Strot
 *
 */
public class EMFDatabase implements IEMFDatabase {
	
	protected static final String DEFAULT_STORAGE = "defaultStorage.xml";
	protected static final String RESOURCES_STORAGE = "resources.xml";
	
	private static final String ROUTER_EXTPT = "org.eclipse.tigerstripe.annotation.core.router";
	private static final String ROUTER_ATTR_CLASS = "class";
	
	private static final String ANNOTATION_MARKER = "org.eclipse.tigerstripe.annotation";
	private static final String ANNOTATION_ID = "id";
	
	private DefaultObjectRouter resourcesStorage;
	private DefaultObjectRouter defaultRouter;
	private EObjectRouter[] routers;
	private ResourceSet resourceSet;
	private ResourceList resourceList;
	private ResourceHelper resourceHelper;
	
	private IndexStorage indexStorage;
	private FeatureIndexer fIndexer;
	private ClassifierIndexer cIndexer;
	private CompositeIndexer indexer;
	
	public EMFDatabase() {
		IPath path = ResourcesPlugin.getDefault().getStateLocation();
		defaultRouter = new DefaultObjectRouter(new File(path.toFile(), DEFAULT_STORAGE));
	}
	
	protected ResourceSet getResourceSet() {
		if (resourceSet == null) {
			resourceSet = new ResourceSetImpl();
		}
		return resourceSet;
	}
	
	protected IndexStorage getIndexStorage() {
		if (indexStorage == null)
			indexStorage = new IndexStorage(getResourceSet());
		return indexStorage;
	}
	
	protected FeatureIndexer getFeatureIndexer() {
		if (fIndexer == null) {
			fIndexer = new FeatureIndexer(getIndexStorage());
		}
		return fIndexer;
	}
	
	protected ClassifierIndexer getClassifierIndexer() {
		if (cIndexer == null) {
			cIndexer = new ClassifierIndexer(getIndexStorage());
		}
		return cIndexer;
	}
	
	protected CompositeIndexer getIndexer() {
		if (indexer == null) {
			indexer = new CompositeIndexer();
			indexer.addIndexer(getFeatureIndexer());
			indexer.addIndexer(getClassifierIndexer());
		}
		return indexer;
		
	}
	
	protected ResourceHelper getResourceHelper() {
		if (resourceHelper == null)
			resourceHelper = new ResourceHelper(getIndexer());
		return resourceHelper;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.core.IEMFDatabase#update(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.change.ChangeDescription)
	 */
	public void update(EObject object, ChangeDescription changes) {
		changes.applyAndReverse();
		remove(object);
		changes.apply();
		write(object);
	}
	
	protected EObjectRouter[] getRouters() {
		if (this.routers == null) {
			IConfigurationElement[] configs = Platform.getExtensionRegistry(
				).getConfigurationElementsFor(ROUTER_EXTPT);
        	List<EObjectRouter> routers = new ArrayList<EObjectRouter>();
            for (IConfigurationElement config : configs) {
            	try {
            		EObjectRouter provider = 
                    	(EObjectRouter)config.createExecutableExtension(ROUTER_ATTR_CLASS);
                    routers.add(provider);
                }
                catch (CoreException e) {
                    e.printStackTrace();
                }
            }
            this.routers = routers.toArray(new EObjectRouter[routers.size()]);
		}
		return this.routers;
	}
	
	protected boolean addToUris(URI uri) {
		if (isDefaultUri(uri))
			return false;
		if (getResourceList().getResourceUris().contains(uri))
			return false;
		return true;
	}
	
	private boolean isDefaultUri(URI uri) {
		return defaultRouter.getUri().equals(uri);
	}
	
	public EObject[] query(EClassifier classifier) {
		return copy(getClassifierIndexer().read(classifier));
	}
	
	protected EObject[] copy(EObject[] objects) {
		return copy(Arrays.asList(objects));
	}
	
	protected EObject[] copy(Collection<EObject> collection) {
		Collection<EObject> copy = EcoreUtil.copyAll(collection);
		return copy.toArray(new EObject[copy.size()]);
	}

	public void write(EObject object) {
		object = EcoreUtil.copy(object);
		Resource resource = getResource(object);
		getResourceHelper().addAndSave(resource, object, true);
		
		if (addToUris(resource.getURI())) {
			getResourceList().getResourceUris().add(resource.getURI());
			ResourceHelper.save(getResource(resourcesStorage.getUri()));
		}
    }
	
	public void remove(EObject object) {
		EStructuralFeature feature = getIDFeature(object);
		if (feature != null) {
			EObject[] objects = doGet(feature, object.eGet(feature), false);
			for (int i = 0; i < objects.length; i++) {
				EObject candidate = objects[i];
				if (EcoreUtil.equals(candidate, object)) {
					Resource resource = candidate.eResource();
					if (resource == null)
						resource = getResource(candidate);
					getResourceHelper().removeAndSave(resource, candidate);
					if (resource.getContents().size() == 0 && !isDefaultUri(resource.getURI())) {
						getResourceList().getResourceUris().remove(resource.getURI());
						removeResource(resource);
						ResourceHelper.save(getResource(resourcesStorage.getUri()));
					}
					return;
				}
			}
		}
	}
	
	protected EStructuralFeature getIDFeature(EObject object) {
		for (EStructuralFeature feature : object.eClass().getEStructuralFeatures())
			if (isIDFeature(feature) && object.eIsSet(feature))
				return feature;
		return null;
	}
	
	protected void removeResource(Resource resource) {
		try {
			Path path = new Path(resource.getURI().toString());
			IResource res = org.eclipse.core.resources.ResourcesPlugin.getWorkspace().getRoot().findMember(path);
			if (res != null)
				res.delete(true, new NullProgressMonitor());
        }
        catch (Exception e) {
	        e.printStackTrace();
        }
		
	}
	
	protected Resource getResource(EObject object) {
		return getResource(getUri(object));
	}
	
	protected Resource getResource(URI uri) {
		Resource resource = getResourceSet().getResource(uri, false);
		if (resource == null) {
			resource = getResourceSet().createResource(uri);
		}
		if (resource != null) {
			try {
	            resource.load(null);
            }
            catch (IOException e) {
            	//ignore exception
            }
    		EcoreUtil.resolveAll(resource);
		}
		return resource;
	}
	
	protected URI getUri(EObject object) {
		EObjectRouter[] routers = getRouters();
		for (int i = 0; i < routers.length; i++) {
	        EObjectRouter elem = routers[i];
	        try {
		        URI uri = elem.route(object);
		        if (uri != null)
		        	return uri;
	        }
	        catch (Exception e) {
	        	ResourcesPlugin.log(e);
			}
        }
		return defaultRouter.route(object);
	}
	
	protected ResourceList getResourceList() {
		if (resourceList == null) {
			IPath path = ResourcesPlugin.getDefault().getStateLocation();
			resourcesStorage = new DefaultObjectRouter(new File(path.toFile(), RESOURCES_STORAGE));
			Resource resource = getResource(resourcesStorage.getUri());
			try {
				resource.load(null);
            }
            catch (IOException e) {
            	//ignore
            }
            if (resource.getContents().size() > 0) {
            	resourceList = (ResourceList)resource.getContents().get(0);
            }
            else {
            	resourceList = ResourcesFactory.eINSTANCE.createResourceList();
            	resource.getContents().add(resourceList);
            	getResourceHelper().addAndSave(resource, resourceList);
            }
		}
		return resourceList;
	}
	
	protected Resource[] loadResources() {
		ArrayList<Resource> resources = new ArrayList<Resource>();
    	Iterator<URI> iter = getResourceList().getResourceUris().iterator();
    	while (iter.hasNext()) {
            Resource newResource = getResource(iter.next());
            if (newResource != null)
            	resources.add(newResource);
        }
        resources.add(getResource(defaultRouter.getUri()));
		return resources.toArray(new Resource[resources.size()]);
	}

	public boolean isIDFeature(EStructuralFeature feature) {
    	EAnnotation annotation = feature.getEAnnotation(ANNOTATION_MARKER);
    	if (annotation != null) {
			String value = annotation.getDetails().get(ANNOTATION_ID);
			if (value != null && Boolean.valueOf(value))
				return true;
    	}
		return false;
	}
	
	protected EObject[] doGet(EStructuralFeature feature, Object value, boolean postfixes) {
		if (getFeatureIndexer().isFeatureIndexed(feature)) {
		    return postfixes ? getFeatureIndexer().readPostfixes(feature, value) : 
		    	getFeatureIndexer().read(feature, value);
		}
		else {
			List<EObject> list = new ArrayList<EObject>();
			EClass clazz = (EClass)feature.eContainer();
			EObject[] objects = read();
			for (int i = 0; i < objects.length; i++) {
	            if (objects[i].eClass().equals(clazz)) {
	            	try {
		            	Object oValue = objects[i].eGet(feature);
		            	if (value == null) {
		            		if (oValue == null && !postfixes)
		            			list.add(objects[i]);
		            	}
		            	else {
		            		if (!postfixes && value.equals(oValue))
			            		list.add(objects[i]);
		            		if (postfixes && oValue.toString().startsWith(value.toString()))
			            		list.add(objects[i]);
		            	}
	            	}
	            	catch (Exception e) {
					}
	            }
            }
			return list.toArray(new EObject[list.size()]);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.core.IEMFDatabase#get(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object)
	 */
	public EObject[] get(EStructuralFeature feature, Object value) {
		return copy(doGet(feature, value, false));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.core.IEMFDatabase#getPostfixes(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object)
	 */
	public EObject[] getPostfixes(EStructuralFeature feature, Object value) {
		return copy(doGet(feature, value, true));
	}
	
	public EObject[] read() {
		ArrayList<EObject> contents = new ArrayList<EObject>();
		Resource[] resources = loadResources();
		for (int i = 0; i < resources.length; i++) {
			try {
	            resources[i].load(null);
            }
            catch (IOException e) {
            	//ignore
            }
			contents.addAll(resources[i].getContents());
        }
	    return contents.toArray(new EObject[contents.size()]);
	}

}
