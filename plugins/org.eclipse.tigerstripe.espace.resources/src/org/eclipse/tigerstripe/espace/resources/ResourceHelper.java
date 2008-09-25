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
package org.eclipse.tigerstripe.espace.resources;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.tigerstripe.espace.resources.internal.core.IIndexer;
import org.eclipse.tigerstripe.espace.resources.internal.core.FileResourceUtils;

/**
 * @author Yuri Strot
 *
 */
public class ResourceHelper {
	
	private static final int NO_INDEX = 0;
	private static final int ADD_INDEX = 1;
	private static final int REMOVE_INDEX = 2;
	
	private IIndexer indexer;
	private ResourceSet resourceSet;
	
	public ResourceHelper(IIndexer indexer, ResourceSet resourceSet) {
		this.indexer = indexer;
		this.resourceSet = resourceSet;
	}
	
	public void addAndSave(Resource resource, EObject object) throws IOException {
		addAndSave(resource, object, false);
	}
	
	public void addAndSave(Resource resource, EObject object, boolean indexing) throws IOException {
		indexer.clear();
		resource.getContents().add(object);
		readReferences(object, indexing ? ADD_INDEX : NO_INDEX);
		indexer.applyChanges();
		save(resource);
		indexer.save();
	}
	
	public void rebuildIndex(EObject[] objects) throws IOException {
		indexer.removeIndex();
		for (int i = 0; i < objects.length; i++)
			readReferences(objects[i], ADD_INDEX);
		indexer.applyChanges();
		indexer.save();
	}
	
	public void removeAndSave(Resource resource, EObject object) throws IOException {
		indexer.clear();
		readReferences(object, REMOVE_INDEX);
		indexer.resolve();
		resource.getContents().remove(object);
		save(resource);
		indexer.applyChanges();
		indexer.save();
	}
	
	public static void save(Resource resource) {
		DeferredResourceSaver.getInstance().resourceDirty(
				resource, !FileResourceUtils.isSystemResource(resource));
	}
	
	/**
	* Recursively read all objects which are referenced from the passed eobject.
	* The objects which do not have an econtainer are added to the rootList.
	* The resulting rootList can be added to the contents of a resource.
	*/
    private void readReferences(EObject eobject, int indexStyle) {
        if (indexStyle == ADD_INDEX)
        	indexer.addToIndex(eobject);
        else if (indexStyle == REMOVE_INDEX)
        	indexer.removeFromIndex(eobject);
        
        for(EObject child : eobject.eContents()){
        	readReferences(child, indexStyle);
        }
    }
	
	public static long getLastModification(Resource resource) {
		URI uri = resource.getURI();
		String pString = uri.toPlatformString(false);
		if (pString != null) {
			Path path = new Path(pString);
			IFile file = org.eclipse.core.resources.ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if (file != null) {
				return file.getLocalTimeStamp();
			}
		}
		String fString = uri.toFileString();
		if (fString != null) {
			File file = new File(fString);
			return file.lastModified();
		}
		return IResource.NULL_STAMP;
	}
	
	/**
	 * @return the resourceSet
	 */
	public ResourceSet getResourceSet() {
		return resourceSet;
	}
	
	public Resource getResource(URI uri) {
		return getResource(uri, true);
	}
	
	public Resource getResource(URI uri, boolean load) {
		Resource resource = resourceSet.getResource(uri, false);
		if (!load) return resource;
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
    		EcoreUtil.resolveAll(resource);
		}
		return resource;
	}

}
