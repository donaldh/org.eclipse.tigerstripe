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
package org.eclipse.tigerstripe.annotation.internal.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.tigerstripe.annotation.core.ResourceSetFactory;

/**
 * This class provide general methods for saving EMF objects
 * 
 * @author Yuri Strot
 */
public class EMFStorage {
	
	protected static Resource getResource(File file) throws IOException {
		if (!file.exists())
			file.createNewFile();
		URI uri = URI.createFileURI(file.getAbsolutePath());
		ResourceSet resourceSet = ResourceSetFactory.getResourceSet();
		Resource resource = resourceSet.getResource(uri, false);
		if (resource == null) {
			resource = resourceSet.createResource(uri);
		}
		return resource;
	}
	
	public static void save(EObject object) throws IOException {
		Resource resource = object.eResource();
		if (resource != null) {
			EList<EObject> list = resource.getContents();
			list.clear();
			collectAll(object, list);
			final Map<String, String> saveOptions = new HashMap<String, String>();
			saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
			resource.save(saveOptions);
		}
	}
	
	public static void save(EObject object, File file) throws IOException {
		Resource resource = getResource(file);
		if (resource != null) {
			EList<EObject> list = resource.getContents();
			list.clear();
			collectAll(object, list);
			final Map<String, String> saveOptions = new HashMap<String, String>();
			saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
			resource.save(saveOptions);
		}
	}
	
	protected static void collectAll(EObject object, List<EObject> collection) {
		collection.add(object);
		Iterator<EStructuralFeature> it = object.eClass().getEStructuralFeatures().iterator();
		while (it.hasNext()) {
			EStructuralFeature feature = it.next();
			Object value = object.eGet(feature);
			if (value instanceof Iterable<?>) {
				//TODO how to save full model?
			}
			if (value instanceof EObject) {
				collectAll((EObject)value, collection);
			}
        }
	}
	
	public static EObject load(File file) throws IOException {
		Resource resource = getResource(file);
		if (resource != null)
			resource.load(new HashMap<String, String>());
		EList<EObject> list = resource.getContents();
		if (list.size() > 0)
			return (EObject)list.get(0);
		return null;
	}

}
