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
package org.eclipse.tigersrtipe.espace.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * @author Yuri Strot
 *
 */
public class ResourceUtil {
	
	public static void addAndSave(Resource resource, EObject object) {
		collectAll(object, resource.getContents(), new ArrayList<EObject>());
		save(resource);
	}
	
	public static void removeAndSave(Resource resource, EObject object) {
		ArrayList<EObject> list = new ArrayList<EObject>();
		collectAll(object, list, new ArrayList<EObject>());
		resource.getContents().removeAll(list);
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
	
	protected static void collectAll(EObject object, List<EObject> collection, List<EObject> toSkip) {
		if (toSkip.contains(object))
			return;
		if (!collection.contains(object))
			collection.add(object);
		toSkip.add(object);
		
		Iterator<EStructuralFeature> it = object.eClass().getEStructuralFeatures().iterator();
		while (it.hasNext()) {
			EStructuralFeature feature = it.next();
			Object value = object.eGet(feature);
			if (value instanceof Iterable<?>) {
				Iterator<?> iter = ((Iterable<?>)value).iterator();
				while (iter.hasNext()) {
	                Object elem = (Object) iter.next();
	                if (elem instanceof EObject) {
	    				collectAll((EObject)value, collection, toSkip);
	                }
                }
			}
			if (value instanceof EObject) {
				collectAll((EObject)value, collection, toSkip);
			}
        }
	}

}
