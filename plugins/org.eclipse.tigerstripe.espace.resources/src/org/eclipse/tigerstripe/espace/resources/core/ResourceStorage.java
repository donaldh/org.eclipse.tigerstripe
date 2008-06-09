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
import java.util.Iterator;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.tigerstripe.espace.resources.ResourceHelper;
import org.eclipse.tigerstripe.espace.resources.ResourceList;
import org.eclipse.tigerstripe.espace.resources.ResourceLocation;
import org.eclipse.tigerstripe.espace.resources.ResourcesFactory;
import org.eclipse.tigerstripe.espace.resources.ResourcesPlugin;

/**
 * @author Yuri Strot
 *
 */
public class ResourceStorage {
	
	private DefaultObjectRouter resourcesStorage;
	private ResourceList resourceList;
	protected static final String RESOURCES_STORAGE = "resources.xml";
	private DefaultObjectRouter defaultRouter;
	private ResourceHelper helper;
	
	protected static final String DEFAULT_STORAGE = "defaultStorage.xml";
	
	public ResourceStorage(ResourceHelper helper) {
		this.helper = helper;
		IPath path = ResourcesPlugin.getDefault().getStateLocation();
		defaultRouter = new DefaultObjectRouter(new File(path.toFile(), DEFAULT_STORAGE));
	}
	
	private boolean isDefaultUri(URI uri) {
		return defaultRouter.getUri().equals(uri);
	}
	
	public URI defaultRoute(EObject object) {
		return defaultRouter.route(object);
	}
	
	protected ResourceList getResourceList() {
		if (resourceList == null) {
			IPath path = ResourcesPlugin.getDefault().getStateLocation();
			resourcesStorage = new DefaultObjectRouter(new File(path.toFile(), RESOURCES_STORAGE));
			Resource resource = helper.getResource(resourcesStorage.getUri());
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
            	helper.addAndSave(resource, resourceList);
            }
		}
		return resourceList;
	}
	
	protected boolean addToUris(URI uri) {
		for (ResourceLocation location : getResourceList().getLocations()) {
			if (location.getUri().equals(uri))
				return false;
		}
		return true;
	}
	
	public void addResource(Resource resource, EObject object) {
		helper.addAndSave(resource, object, true);
		if (isDefaultUri(resource.getURI()))
			return;
		ResourceLocation location = getLocation(resource);
		if (location == null) {
			location = ResourcesFactory.eINSTANCE.createResourceLocation();
			location.setUri(resource.getURI());
			getResourceList().getLocations().add(location);
		}
		File file = ResourceHelper.getFile(resource);
		if (file != null)
			location.setTimeStamp(file.lastModified());
		ResourceHelper.save(helper.getResource(resourcesStorage.getUri()));
	}
	
	public void removeResourceIfEmpty(Resource resource) {
		if (isDefaultUri(resource.getURI()))
			return;
		if (resource.getContents().size() == 0)
			removeResource(resource);
		else
			updateResource(resource);
		ResourceHelper.save(helper.getResource(resourcesStorage.getUri()));
	}
	
	protected ResourceLocation getLocation(Resource resource) {
		Iterator<ResourceLocation> it = getResourceList().getLocations().iterator();
		while (it.hasNext()) {
			ResourceLocation location = (ResourceLocation) it.next();
			if (location.getUri().equals(resource.getURI())) {
				return location;
			}
		}
		return null;
	}
	
	protected void removeResource(Resource resource) {
		ResourceLocation location = getLocation(resource);
		if (location != null)
			getResourceList().getLocations().remove(location);
		ResourceHelper.delete(resource);
	}
	
	protected void updateResource(Resource resource) {
		ResourceLocation location = getLocation(resource);
		if (location != null) {
			File file = ResourceHelper.getFile(resource);
			if (file != null) {
				location.setTimeStamp(file.lastModified());
			}
		}
	}
	
	public Resource[] loadResources() {
		ArrayList<Resource> resources = new ArrayList<Resource>();
    	Iterator<ResourceLocation> iter = getResourceList().getLocations().iterator();
    	while (iter.hasNext()) {
            Resource newResource = helper.getResource(iter.next().getUri());
            if (newResource != null)
            	resources.add(newResource);
        }
        resources.add(helper.getResource(defaultRouter.getUri()));
		return resources.toArray(new Resource[resources.size()]);
	}
	
	public void removeAndSave(EObject object, Resource resource) {
		helper.removeAndSave(resource, object);
		removeResourceIfEmpty(resource);
	}

}
