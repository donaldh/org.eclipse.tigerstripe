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
package org.eclipse.tigerstripe.espace.resources.internal.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.tigerstripe.espace.core.Mode;
import org.eclipse.tigerstripe.espace.resources.DeferredResourceSaver;
import org.eclipse.tigerstripe.espace.resources.ResourceHelper;
import org.eclipse.tigerstripe.espace.resources.ResourceList;
import org.eclipse.tigerstripe.espace.resources.ResourceLocation;
import org.eclipse.tigerstripe.espace.resources.ResourcesFactory;
import org.eclipse.tigerstripe.espace.resources.ResourcesPlugin;
import org.eclipse.tigerstripe.espace.resources.core.DefaultObjectRouter;

/**
 * @author Yuri Strot
 *
 */
public class ResourceStorage {
	
	private DefaultObjectRouter resourcesStorage;
	private ResourceList resourceList;
	private DefaultObjectRouter defaultRouter;
	private ResourceHelper helper;
	
	public ResourceStorage(ResourceHelper helper) {
		this.helper = helper;
		defaultRouter = new DefaultObjectRouter(FileResourceUtils.getDefaultStorage());
	}
	
	private boolean isDefaultUri(URI uri) {
		return defaultRouter.getUri().equals(uri);
	}
	
	public URI defaultRoute(EObject object) {
		return defaultRouter.route(object);
	}
	
	public boolean needUpdate() {
		boolean needUpdate = false;
		for (ResourceLocation location : getResourceList().getLocations()) {
			Resource resource = helper.getResource(location.getUri());
			if (resource != null && ResourceHelper.getLastModification(
					resource) != location.getTimeStamp()) {
				needUpdate = true;
				resource.unload();
			}
		}
		return needUpdate;
	}
	
	public void updateTimes() {
		for (ResourceLocation location : getResourceList().getLocations()) {
			Resource resource = helper.getResource(location.getUri());
			if (resource != null)
				location.setTimeStamp(ResourceHelper.getLastModification(resource));
		}
		ResourceHelper.save(helper.getResource(resourcesStorage.getUri()));
	}
	
	public ResourceList getResourceList() {
		if (resourceList == null) {
			resourcesStorage = new DefaultObjectRouter(FileResourceUtils.getResourceMetaFile());
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
            	try {
                	helper.addAndSave(resource, resourceList);
            	}
            	catch (IOException e) {
					ResourcesPlugin.log(e);
				}
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
	
	public void addResource(Resource resource, EObject object) throws IOException {
		helper.addAndSave(resource, object, true);
		if (isDefaultUri(resource.getURI()))
			return;
		addResource(resource, Mode.READ_WRITE);
	}
	
	public boolean addResource(Resource resource, Mode option) {
		boolean modified = false;
		boolean newElement = false;
		ResourceLocation location = getLocation(resource);
		if (location == null) {
			modified = true;
			location = ResourcesFactory.eINSTANCE.createResourceLocation();
			location.setUri(resource.getURI());
			location.setOption(option);
			newElement = getResourceList().getLocations().add(location);
		}
		long newStamp = ResourceHelper.getLastModification(resource);
		if (newStamp != location.getTimeStamp()) {
			location.setTimeStamp(newStamp);
			modified = true;
		}
		if (modified)
			ResourceHelper.save(helper.getResource(resourcesStorage.getUri()));
		return newElement;
	}
	
	public boolean removeResource(Resource resource, boolean removeFromSavingList) {
		boolean haveElement = false;
		ResourceLocation location = getLocation(resource);
		if (location != null) {
			haveElement = getResourceList().getLocations().remove(location);
			if (removeFromSavingList)
				DeferredResourceSaver.getInstance().removeResource(resource, false);
			ResourceHelper.save(helper.getResource(resourcesStorage.getUri()));
		}
		return haveElement;
	}
	
	public void removeResourceIfEmpty(Resource resource) {
		if (isDefaultUri(resource.getURI()))
			return;
		if (resource.getContents().size() == 0) {
			removeResource(resource, false);
		}
		else
			updateResource(resource);
	}
	
	public Resource getResource(IResource resource) {
		URI uri = URI.createPlatformResourceURI(
				resource.getFullPath().toString(), false);
		return helper.getResource(uri);
	}
	
	public ResourceLocation getLocation(Resource resource) {
		Iterator<ResourceLocation> it = getResourceList().getLocations().iterator();
		while (it.hasNext()) {
			ResourceLocation location = (ResourceLocation) it.next();
			if (location.getUri().equals(resource.getURI())) {
				return location;
			}
		}
		return null;
	}
	
	protected void updateResource(Resource resource) {
		ResourceLocation location = getLocation(resource);
		if (location != null) {
			location.setTimeStamp(ResourceHelper.getLastModification(resource));
			ResourceHelper.save(helper.getResource(resourcesStorage.getUri()));
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
	
	public void removeAndSave(EObject object, Resource resource) throws IOException {
		helper.removeAndSave(resource, object);
		removeResourceIfEmpty(resource);
	}

}
