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
package org.eclipse.tigerstripe.annotation.ui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;

/**
 * @author Yuri Strot
 *
 */
public class AdaptableUtil {
	
	public static Object[] getAdaptables(Object object) {
		List<Object> adaptables = new ArrayList<Object>();
		String[] targets = AnnotationPlugin.getManager().getAnnotatedObjectTypes();
		for (int i = 0; i < targets.length; i++) {
			Object adapted = getAdapted(object, targets[i]);
			if (adapted != null) {
				adaptables.add(adapted);
			}
		}
		return adaptables.toArray(new Object[adaptables.size()]);
	}
	
	public static Annotation[] getAllAnnotations(Object object) {
		List<Annotation> annotations = new ArrayList<Annotation>();
		Object[] adaptables = getAdaptables(object);
		for (int i = 0; i < adaptables.length; i++) {
			Annotation[] array = AnnotationPlugin.getManager().getAnnotations(adaptables[i], false);
			annotations.addAll(Arrays.asList(array));
		}
		return annotations.toArray(new Annotation[annotations.size()]);
	}
	
	public static AnnotationType[] getTypes(Object object) {
		List<AnnotationType> resultTypes = new ArrayList<AnnotationType>();
		AnnotationType[] types = AnnotationPlugin.getManager().getTypes();
		for (int i = 0; i < types.length; i++) {
			if (getTargets(object, types[i]).length > 0)
				resultTypes.add(types[i]);
		}
		return resultTypes.toArray(new AnnotationType[resultTypes.size()]);
	}
	
	public static String[] getTargets(Object object, AnnotationType type) {
		List<String> resultTypes = new ArrayList<String>();
		String[] targets = type.getTargets();
		if (targets.length == 0)
			targets = AnnotationPlugin.getManager().getAnnotatedObjectTypes();
		for (int i = 0; i < targets.length; i++) {
			if (getAdapted(object, targets[i]) != null) {
				resultTypes.add(targets[i]);
			}
		}
		return resultTypes.toArray(new String[resultTypes.size()]);
	}
	
	public static Object getAdapted(Object object, String className) {
		try {
			Class<?> clazz = Class.forName(className, true, object.getClass().getClassLoader());
			return Platform.getAdapterManager().getAdapter(object, clazz);
		} catch (ClassNotFoundException e) {
		}
		return null;
	}

}
