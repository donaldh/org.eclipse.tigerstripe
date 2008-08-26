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
package org.eclipse.tigerstripe.annotation.core.util;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IProviderTarget;


/**
 * @author Yuri Strot
 *
 */
public class AnnotationUtils {
	
	public static EClass getClass(String epackageUri, String eclass) {
		EPackage pkg = getPackage(epackageUri);
		return (EClass) pkg.getEClassifier(eclass);
	}

	public static EPackage getPackage(String uri) {
		return EPackage.Registry.INSTANCE.getEPackage(uri);
	}
	
	public static ClassName getInstanceClassName(EClass eclass) {
		String name = eclass.getInstanceClassName();
		if (name == null)
			return new ClassName(eclass.getEPackage().getNsPrefix(),
					eclass.getName());
		else
			return new ClassName(name);
	}

	/**
	 * Return false if object can't be adapted to defined annotation types
	 * and true otherwise
	 * 
	 * @param object
	 * @param annotations
	 * @return
	 */
	public static boolean getAllAnnotations(Object object, List<Annotation> annotations) {
//		if (!AnnotationPlugin.getManager().isAnnotable(object))
//			return false;
		boolean haveAdapted = false;
		IProviderTarget[] targets = AnnotationPlugin.getManager().getProviderTargets();
		if (targets.length == 0)
			return false;
		for (int i = 0; i < targets.length; i++) {
			Object adapted = targets[i].adapt(object);
			if (adapted != null) {
				Annotation[] array = AnnotationPlugin.getManager().getAnnotations(adapted, false);
				if (array.length != 0 || AnnotationPlugin.getManager().isAnnotable(adapted))
					haveAdapted = true;
				annotations.addAll(Arrays.asList(array));
			}
		}
		return haveAdapted;
	}
}
