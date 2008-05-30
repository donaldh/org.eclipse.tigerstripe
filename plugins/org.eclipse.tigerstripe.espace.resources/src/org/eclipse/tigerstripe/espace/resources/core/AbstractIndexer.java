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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.tigerstripe.espace.resources.ResourcesPlugin;

/**
 * @author Yuri Strot
 *
 */
public abstract class AbstractIndexer implements IIndexer {
	
	private Map<Object, List<EObject>> indexes;
	private Map<EObject, Boolean> addedMap;
	
	private List<Resource> resources;
	private IndexStorage storage;
	
	protected static EObject[] EMPTY = new EObject[0];
	
	public AbstractIndexer(IndexStorage storage) {
		resources = new ArrayList<Resource>();
		this.storage = storage;
		clear();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIndexer#removeFromIndex(org.eclipse.emf.ecore.EObject)
	 */
	public void removeFromIndex(EObject object) {
		addedMap.put(object, Boolean.FALSE);
		index(object);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIndexer#addToIndex(org.eclipse.emf.ecore.EObject)
	 */
	public void addToIndex(EObject object) {
		addedMap.put(object, Boolean.TRUE);
		index(object);
	}
	
	protected void index(EObject object) {
		Object[] targets = getTargets(object);
		for (int i = 0; i < targets.length; i++) {
			List<EObject> list = indexes.get(targets[i]);
			if (list == null) {
				list = new ArrayList<EObject>();
				indexes.put(targets[i], list);
			}
			list.add(object);
		}
	}
	
	protected abstract Object[] getTargets(EObject object);
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIndexer#resolve()
	 */
	public void resolve() {
		if (indexes.size() == 0)
			return;
		
		Iterator<Object> it = indexes.keySet().iterator();
		while (it.hasNext()) {
			Object object = it.next();
			Resource res = getResource(object, true);
			EObject container = getContainer(res, object);
			Iterator<EObject> iter = indexes.get(object).iterator();
			while (iter.hasNext()) {
				EObject eobject = iter.next();
				resolve(container, eobject);
            }
        }
	}
	
	public void applyChanges() {
		if (indexes.size() == 0)
			return;
		
		Iterator<Object> it = indexes.keySet().iterator();
		while (it.hasNext()) {
			Object object = it.next();
			Resource res = getResource(object, true);
			EObject container = getContainer(res, object);
			Iterator<EObject> iter = indexes.get(object).iterator();
			while (iter.hasNext()) {
				EObject eobject = iter.next();
				Boolean add = (Boolean) addedMap.get(eobject);
				if (add != null && add.booleanValue())
					insert(container, eobject);
				else
					remove(container, eobject);
				if (isEmpty(container))
					res.getContents().remove(container);
				else if (!res.getContents().contains(container))
					res.getContents().add(container);
            }
			resources.add(res);
        }
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIndexer#save()
	 */
	public void save() {
		for (Resource resource : resources)
			save(resource);
		resources = new ArrayList<Resource>();
	}
	
	public static void save(Resource resource) {
    	try {
            resource.save(null);
        }
        catch (IOException e) {
        	ResourcesPlugin.log(e);
        }
	}
	
	protected Resource getResource(Object object, boolean create) {
		String featureName = getFeatureName(object);
		return storage.getResource(featureName, create);
	}
	
	protected abstract String getFeatureName(Object object);

	protected abstract EObject getContainer(Resource resource, Object object);

	protected abstract void insert(EObject container, Object object);

	protected abstract void resolve(EObject container, Object object);

	protected abstract void remove(EObject container, Object object);

	protected abstract boolean isEmpty(EObject container);

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIndexer#clear()
	 */
	public void clear() {
		indexes = new HashMap<Object, List<EObject>>();
		addedMap = new HashMap<EObject, Boolean>();
	}

}
