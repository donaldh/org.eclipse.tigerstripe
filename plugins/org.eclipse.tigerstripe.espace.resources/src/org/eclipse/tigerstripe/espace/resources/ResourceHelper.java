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

import java.io.IOException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.tigerstripe.espace.resources.core.IIndexer;

/**
 * @author Yuri Strot
 *
 */
public class ResourceHelper {
	
	private static final int NO_INDEX = 0;
	private static final int ADD_INDEX = 1;
	private static final int REMOVE_INDEX = 2;
	
	private IIndexer indexer;
	
	public ResourceHelper(IIndexer indexer) {
		this.indexer = indexer;
	}
	
	public void addAndSave(Resource resource, EObject object) {
		addAndSave(resource, object, false);
	}
	
	public void addAndSave(Resource resource, EObject object, boolean indexing) {
		indexer.clear();
		resource.getContents().add(object);
		readReferences(object, indexing ? ADD_INDEX : NO_INDEX);
		indexer.applyChanges();
		save(resource);
		indexer.save();
	}
	
	public void removeAndSave(Resource resource, EObject object) {
		indexer.clear();
		readReferences(object, REMOVE_INDEX);
		indexer.resolve();
		resource.getContents().remove(object);
		save(resource);
		indexer.applyChanges();
		indexer.save();
	}
	
	public static void save(Resource resource) {
    	try {
            resource.save(null);
        }
        catch (IOException e) {
        	ResourcesPlugin.log(e);
        }
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

}
