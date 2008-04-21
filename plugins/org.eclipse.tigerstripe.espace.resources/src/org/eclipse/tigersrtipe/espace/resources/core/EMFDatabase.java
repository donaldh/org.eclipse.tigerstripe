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
package org.eclipse.tigersrtipe.espace.resources.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.tigersrtipe.espace.core.IEMFDatabase;
import org.eclipse.tigersrtipe.espace.resources.ResourceList;
import org.eclipse.tigersrtipe.espace.resources.ResourceUtil;
import org.eclipse.tigersrtipe.espace.resources.ResourcesFactory;
import org.eclipse.tigersrtipe.espace.resources.ResourcesPlugin;

/**
 * @author Yuri Strot
 *
 */
public class EMFDatabase implements IEMFDatabase {
	
	protected static final String DEFAULT_STORAGE = "defaultStorage.xml";
	protected static final String RESOURCES_STORAGE = "resources.xml";
	
	private static final String ROUTER_EXTPT = "org.eclipse.tigerstripe.annotation.core.router";
	private static final String ROUTER_ATTR_CLASS = "class";
	
	private DefaultObjectRouter resourcesStorage;
	private DefaultObjectRouter defaultRouter;
	private EObjectRouter[] routers;
	private ResourceSet resourceSet;
	private ResourceList resourceList;
	
	public EMFDatabase() {
		IPath path = ResourcesPlugin.getDefault().getStateLocation();
		defaultRouter = new DefaultObjectRouter(new File(path.toFile(), DEFAULT_STORAGE));
		resourceSet = new ResourceSetImpl();
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

	public void write(EObject object) {
		Resource resource = getResource(object);
		ResourceUtil.addAndSave(resource, object);
		
		if (addToUris(resource.getURI())) {
			getResourceList().getResourceUris().add(resource.getURI());
			ResourceUtil.save(getResource(resourcesStorage.getUri()));
		}
    }
	
	public void remove(EObject object) {
		Resource resource = getResource(object);
		ResourceUtil.removeAndSave(resource, object);
		
		if (resource.getContents().size() == 0 && !isDefaultUri(resource.getURI())) {
			getResourceList().getResourceUris().remove(resource.getURI());
			removeResource(resource);
			ResourceUtil.save(getResource(resourcesStorage.getUri()));
		}
	}
	
	protected void removeResource(Resource resource) {
		try {
			Path path = new Path(resource.getURI().toPlatformString(false));
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
		Resource resource = resourceSet.getResource(uri, false);
		if (resource == null) {
			resource = resourceSet.createResource(uri);
		}
		if (resource != null) {
			try {
	            resource.load(null);
            }
            catch (IOException e) {
            	//ignore exception
            }
		}
		return resource;
	}
	
	protected URI getUri(EObject object) {
		EObjectRouter[] routers = getRouters();
		for (int i = 0; i < routers.length; i++) {
	        EObjectRouter elem = routers[i];
	        URI uri = elem.route(object);
	        if (uri != null)
	        	return uri;
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
            	ResourceUtil.addAndSave(resource, resourceList);
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
