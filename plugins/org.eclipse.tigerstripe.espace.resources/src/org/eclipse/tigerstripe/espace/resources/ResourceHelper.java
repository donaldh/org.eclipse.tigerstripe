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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.tigerstripe.espace.core.tree.RBTree;
import org.eclipse.tigerstripe.espace.core.tree.TreeFactory;
import org.eclipse.tigerstripe.espace.resources.core.IndexPair;

/**
 * @author Yuri Strot
 *
 */
public class ResourceHelper {
	
	private static final String ANNOTATION_MARKER = "org.eclipse.tigerstripe.annotation";
	private static final String ANNOTATION_INDEX = "index";
	
	public static final String INDEX_DIRECTORY = "INDEX/";
	
	private ResourceSet resourceSet;
	private Resource listResource;
	private IndexList list;
	
	private Map<EStructuralFeature, Resource> map;
	
	public ResourceHelper(ResourceSet resourceSet) {
		this.resourceSet = resourceSet;
		map = new HashMap<EStructuralFeature, Resource>();
	}
	
	public void addAndSave(Resource resource, EObject object) {
		addAndSave(resource, object, false);
	}
	
	public void addAndSave(Resource resource, EObject object, boolean indexing) {
		HashMap<EStructuralFeature, List<EObject>> indexes =
			new HashMap<EStructuralFeature, List<EObject>>();
		readReferences(object, new HashSet<EObject>(), resource.getContents(), indexes);
		List<Resource> resourceToSave = new ArrayList<Resource>();
		resourceToSave.add(resource);
		if (indexing) updateIndexes(indexes, false, resourceToSave);
		saveAll(resourceToSave);
	}
	
	protected void saveAll(List<Resource> resourceToSave) {
		Iterator<Resource> res = resourceToSave.iterator();
		while (res.hasNext()) {
	        Resource resource = (Resource) res.next();
	        save(resource);
        }
	}
	
	public EObject[] readFromIndex(EStructuralFeature feature, Object value) {
		Resource res = getResource(feature);
		RBTree tree = getTree(res, feature);
		return tree.find(value);
	}
	
	protected RBTree getTree(Resource resource, EStructuralFeature feature) {
		RBTree tree = null;
		if (resource.getContents().size() == 1 && resource.getContents().get(0) instanceof RBTree) {
			tree = (RBTree)resource.getContents().get(0);
		}
		else {
			tree = TreeFactory.eINSTANCE.createRBTree();
			tree.setFeature(feature);
			resource.getContents().add(tree);
		}
		return tree;
	}
	
	protected Resource getResource(EStructuralFeature feature) {
		Resource res = map.get(feature);
		if (res == null) {
			String featurePath = ((EClass)feature.eContainer()).getInstanceClassName() + "." + feature.getName() + ".xml";
			File file = new File(ResourcesPlugin.getDefault().getStateLocation().toFile(), 
				INDEX_DIRECTORY + featurePath);
			res = getResource(file);
			map.put(feature, res);
		}
		return res;
	}
	
	protected void updateIndexes(HashMap<EStructuralFeature, List<EObject>> indexes,
			boolean remove, List<Resource> resourceToSave) {
		if (indexes.size() == 0)
			return;
		
		Iterator<EStructuralFeature> it = indexes.keySet().iterator();
		while (it.hasNext()) {
			EStructuralFeature feature = it.next();
			Resource res = getResource(feature);
			RBTree tree = getTree(res, feature);
			Iterator<EObject> iter = indexes.get(feature).iterator();
			while (iter.hasNext()) {
				EObject object = iter.next();
				if (remove) tree.remove(object);
				else tree.insert(object);
				if (tree.isEmpty())
					res.getContents().remove(tree);
				else if (!res.getContents().contains(tree))
					res.getContents().add(tree);
            }
			resourceToSave.add(res);
        }
	}
	
	protected ResourceSet getResourceSet() {
		return resourceSet;
	}
	
	protected Resource getResource(File file) {
		URI uri = URI.createFileURI(file.getAbsolutePath());
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
		}
		return resource;
	}
	
	protected IndexList getList() {
		if (list == null) {
			if (listResource == null) {
				File file = new File(ResourcesPlugin.getDefault().getStateLocation(
					).toFile(), INDEX_DIRECTORY + "list.xml");
				listResource = getResource(file);
			}
			EList<EObject> content = listResource.getContents();
			if (content.size() == 1 && content.get(0) instanceof IndexList) {
				list = (IndexList)content.get(0);
			}
			else {
				list = ResourcesFactory.eINSTANCE.createIndexList();
			}
		}
		return list;
	}
	
	protected void saveList() {
		listResource.getContents().clear();
		addAndSave(listResource, list);
	}
	
	protected static IndexKey getKey(IndexPair pair) {
		IndexKey key = ResourcesFactory.eINSTANCE.createIndexKey();
		key.setClassName(pair.getObject().getClass().getName());
		key.setFeatureName(pair.getFeatureName());
		return key;
	}
	
	public void removeAndSave(Resource resource, EObject object) {
		ArrayList<EObject> list = new ArrayList<EObject>();
		HashMap<EStructuralFeature, List<EObject>> indexes =
			new HashMap<EStructuralFeature, List<EObject>>();
		readReferences(object, new HashSet<EObject>(), list, indexes);
		List<Resource> resourceToSave = new ArrayList<Resource>();
		resource.getContents().removeAll(list);
		resourceToSave.add(resource);
		updateIndexes(indexes, true, resourceToSave);
		saveAll(resourceToSave);
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
    private static void readReferences(EObject eobject, HashSet<EObject> preventCycles,
    		List<EObject> rootList, Map<EStructuralFeature, List<EObject>> indexes) {
        if(preventCycles.contains(eobject)){ // been here get away
            return;
        }
        preventCycles.add(eobject);
        collectIndexes(eobject, indexes);
        if(eobject.eContainer() != null){
            readReferences(eobject.eContainer(), preventCycles, rootList, indexes);
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
                    readReferences((EObject)obj, preventCycles, rootList, indexes);
                }
            }else{ // an eobject
                readReferences((EObject)value, preventCycles, rootList, indexes);
            }
        }
    }
	
	protected static void collectIndexes(EObject object, Map<EStructuralFeature, List<EObject>> indexes) {
		Iterator<EStructuralFeature> it = object.eClass().getEStructuralFeatures().iterator();
		while (it.hasNext()) {
	        EStructuralFeature feature = it.next();
	        collectIndex(object, feature, indexes);
        }
	}

	public static boolean isFeatureIndexed(EStructuralFeature feature) {
    	EAnnotation annotation = feature.getEAnnotation(ANNOTATION_MARKER);
    	if (annotation != null) {
			String value = annotation.getDetails().get(ANNOTATION_INDEX);
			if (value != null && Boolean.valueOf(value))
				return true;
    	}
		return false;
	}
	
	protected static void collectIndex(EObject object, EStructuralFeature feature, 
			Map<EStructuralFeature, List<EObject>> indexes) {
		if (indexes != null && isFeatureIndexed(feature)) {
			List<EObject> list = indexes.get(feature);
			if (list == null) {
				list = new ArrayList<EObject>();
				indexes.put(feature, list);
			}
			list.add(object);
		}
	}

}
