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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
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
import org.eclipse.tigerstripe.espace.resources.ResourcesPlugin;
import org.eclipse.tigerstripe.espace.resources.internal.core.ClassifierIndexer;
import org.eclipse.tigerstripe.espace.resources.internal.core.CompositeIndexer;
import org.eclipse.tigerstripe.espace.resources.internal.core.FeatureIndexer;
import org.eclipse.tigerstripe.espace.resources.internal.core.IndexStorage;
import org.eclipse.tigerstripe.espace.resources.internal.core.ResourceStorage;

/**
 * @author Yuri Strot
 *
 */
public class EMFDatabase implements IEMFDatabase {
	
	private static final String ROUTER_EXTPT = "org.eclipse.tigerstripe.annotation.core.router";
	private static final String ROUTER_ATTR_CLASS = "class";
	
	private static final String ANNOTATION_MARKER = "org.eclipse.tigerstripe.annotation";
	private static final String ANNOTATION_ID = "id";
	
	private EObjectRouter[] routers;
	private ResourceSet resourceSet;
	private ResourceHelper resourceHelper;
	
	private IndexStorage indexStorage;
	private FeatureIndexer fIndexer;
	private ClassifierIndexer cIndexer;
	private CompositeIndexer indexer;
	
	private int ignoreChanges;
	
	private ResourceStorage resourceStorage;
	
	public EMFDatabase() {
		addResourceDeltaListener();
	}
	
	protected ResourceSet getResourceSet() {
		if (resourceSet == null) {
			resourceSet = new ResourceSetImpl();
		}
		return resourceSet;
	}
	
	protected ResourceStorage getResourceStorage() {
		if (resourceStorage == null) {
			resourceStorage = new ResourceStorage(getResourceHelper());
		}
		return resourceStorage;
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
			resourceHelper = new ResourceHelper(getIndexer(), getResourceSet());
		return resourceHelper;
	}
	
	private void lockChanges() {
		ignoreChanges++;
	}
	
	private void unlockChanges() {
		ignoreChanges--;
	}
	
	private boolean isLocked() {
		return ignoreChanges > 0;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.core.IEMFDatabase#update(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.change.ChangeDescription)
	 */
	public void update(EObject object, ChangeDescription changes) {
		lockChanges();
		try {
			update();
			changes.applyAndReverse();
			doRemove(object);
			changes.apply();
			doWrite(object);
		}
		finally {
			unlockChanges();
		}
	}
	
	public EObject[] query(EClassifier classifier) {
		lockChanges();
		try {
			update();
			return copy(getClassifierIndexer().read(classifier));
		}
		finally {
			unlockChanges();
		}
	}

	public void write(EObject object) {
		lockChanges();
		try {
			update();
			doWrite(object);
		}
		finally {
			unlockChanges();
		}
    }
	
	public void remove(EObject object) {
		lockChanges();
		try {
			update();
			doRemove(object);
		}
		finally {
			unlockChanges();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.core.IEMFDatabase#get(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object)
	 */
	public EObject[] get(EStructuralFeature feature, Object value) {
		lockChanges();
		try {
			update();
			return copy(doGet(feature, value, false));
		}
		finally {
			unlockChanges();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.core.IEMFDatabase#getPostfixes(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object)
	 */
	public EObject[] getPostfixes(EStructuralFeature feature, Object value) {
		lockChanges();
		try {
			update();
			return copy(doGet(feature, value, true));
		}
		finally {
			unlockChanges();
		}
	}
	
	public EObject[] read() {
		lockChanges();
		try {
			update();
			return doRead();
		}
		finally {
			unlockChanges();
		}
	}
	
	public void rebuildIndex() {
		lockChanges();
		try {
			doRebuildIndex();
		}
		finally {
			unlockChanges();
		}
	}
	
	protected void doRebuildIndex() {
		getIndexStorage().removeIndex();
		clear();
		EObject[] objects = doRead();
		getResourceHelper().rebuildIndex(objects);
		getResourceStorage().updateTimes();
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
	
	protected EObject[] copy(EObject[] objects) {
		return copy(Arrays.asList(objects));
	}
	
	protected EObject[] copy(Collection<EObject> collection) {
		Collection<EObject> copy = EcoreUtil.copyAll(collection);
		return copy.toArray(new EObject[copy.size()]);
	}
	
	protected void update() {
		if (getResourceStorage().needUpdate())
			doRebuildIndex();
	}
	
	protected void doWrite(EObject object) {
		object = EcoreUtil.copy(object);
		Resource resource = getResource(object);
		getResourceStorage().addResource(resource, object);
	}
	
	protected void clear() {
		resourceSet = null;
		resourceHelper = null;
		indexStorage = null;
		fIndexer = null;
		cIndexer = null;
		indexer = null;
		resourceStorage = null;
	}
	
	protected void doRemove(EObject object) {
		EStructuralFeature feature = getIDFeature(object);
		if (feature != null) {
			EObject[] objects = doGet(feature, object.eGet(feature), false);
			for (int i = 0; i < objects.length; i++) {
				EObject candidate = objects[i];
				if (EcoreUtil.equals(candidate, object)) {
					Resource resource = candidate.eResource();
					if (resource == null)
						resource = getResource(object);
					getResourceStorage().removeAndSave(candidate, resource);
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
	
	protected Resource getResource(EObject object) {
		return getResourceHelper().getResource(getUri(object));
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
		return getResourceStorage().defaultRoute(object);
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
	
	protected void addResourceDeltaListener() {
		org.eclipse.core.resources.ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener() {
			
			public void resourceChanged(IResourceChangeEvent event) {
				try {
					if (event.getDelta() != null) {
						event.getDelta().accept(new IResourceDeltaVisitor() {
							
							public boolean visit(IResourceDelta delta) throws CoreException {
								IResource resource = delta.getResource();
								switch (delta.getKind()) {
									case IResourceDelta.ADDED:
										updateResource(resource, true);
										break;
									case IResourceDelta.CHANGED:
										break;
									case IResourceDelta.REMOVED:
										updateResource(resource, false);
										break;
								}
								return true;
							}
						});
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		
		});
	}
	
	protected void updateResource(IResource resource, boolean added) {
		if (resource instanceof IFile) {
			IFile file = (IFile)resource;
			if (!isLocked() && file.getFileExtension().toLowerCase().equals(
					EObjectRouter.ANNOTATION_FILE_EXTENSION)) {
				getResourceStorage().updateResource(resource, added);
				doRebuildIndex();
			}
		}
	}
	
	protected EObject[] doGet(EStructuralFeature feature, Object value, boolean postfixes) {
		if (getFeatureIndexer().isFeatureIndexed(feature)) {
		    return postfixes ? getFeatureIndexer().readPostfixes(feature, value) : 
		    	getFeatureIndexer().read(feature, value);
		}
		else {
			List<EObject> list = new ArrayList<EObject>();
			EClass clazz = (EClass)feature.eContainer();
			EObject[] objects = doRead();
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
	
	protected EObject[] doRead() {
		ArrayList<EObject> contents = new ArrayList<EObject>();
		Resource[] resources = getResourceStorage().loadResources();
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
