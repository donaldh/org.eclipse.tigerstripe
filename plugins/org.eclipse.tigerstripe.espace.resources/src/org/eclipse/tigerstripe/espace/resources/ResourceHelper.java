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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
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
		readReferences(object, new HashSet<EObject>(), resource.getContents(),
				indexing ? ADD_INDEX : NO_INDEX);
		save(resource);
		indexer.save();
	}
	
	public void removeAndSave(Resource resource, EObject object) {
		indexer.clear();
		ArrayList<EObject> list = new ArrayList<EObject>();
		readReferences(object, new HashSet<EObject>(), list, REMOVE_INDEX);
		resource.getContents().removeAll(list);
		indexer.save();
		save(resource);
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
    private void readReferences(EObject eobject, HashSet<EObject> preventCycles, List<EObject> rootList,
    		int indexStyle) {
        if(preventCycles.contains(eobject)){ // been here get away
            return;
        }
        preventCycles.add(eobject);
        
        if (indexStyle == ADD_INDEX)
        	indexer.addToIndex(eobject);
        else if (indexStyle == REMOVE_INDEX)
        	indexer.removeFromIndex(eobject);
        
        if(eobject.eContainer() != null){
            readReferences(eobject.eContainer(), preventCycles, rootList, indexStyle);
        }else{ // a root object
        	if (!rootList.contains(eobject))
        		rootList.add(eobject);
        }
        for(Object erefObj : eobject.eClass().getEAllReferences()){
            EReference eref = (EReference)erefObj;
            final Object value = eobject.eGet(eref);
            if (value == null) {
                continue;
            }
            if(value instanceof List){
                for(Object obj : (List<?>)value){
                    readReferences((EObject)obj, preventCycles, rootList, indexStyle);
                }
            }else{ // an eobject
                readReferences((EObject)value, preventCycles, rootList, indexStyle);
            }
        }
    }

}
