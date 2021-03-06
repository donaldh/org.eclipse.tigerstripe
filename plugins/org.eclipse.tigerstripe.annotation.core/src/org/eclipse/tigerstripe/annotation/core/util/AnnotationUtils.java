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

import static org.eclipse.emf.ecore.util.EcoreUtil.copy;

import java.util.Collection;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationException;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.ProviderContext;

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
		if (name == null) {
			return new ClassName(eclass.getEPackage().getNsPrefix(), eclass
					.getName());
		} else {
			return new ClassName(name);
		}
	}

	/**
	 * Copy annotations from one annotable object to another. If some of the
	 * source annotations can't be copy to target object, this annotations will
	 * be ignored
	 * 
	 * @param from
	 *            source annotable object
	 * @param to
	 *            target annotable object
	 * @return true, if some source annotations have been copied and false
	 *         otherwise
	 * @throws AnnotationException
	 */
	public static boolean copyAnnotations(Object from, Object to)
			throws AnnotationException {
		Annotation[] annotations = AnnotationPlugin.getManager()
				.getAnnotations(from, false);
		boolean haveCopied = false;
		for (Annotation annotation : annotations) {
			AnnotationPlugin.getManager().addAnnotation(to,
					copy(annotation.getContent()));
			
			haveCopied = true;
		}
		return haveCopied;
	}

	/**
	 * Collect all annotations for given object
	 * 
	 * @param object
	 * @param annotations
	 *            list of the result annotations
	 * @return false if object can't be adapted to defined annotation types (so,
	 *         it's not annotable object) and true otherwise
	 */
	public static boolean collectAllAnnotations(Object object,
			Set<Annotation> annotations) {
		boolean haveAdapted = false;
		IAnnotationManager manager = AnnotationPlugin.getManager();
		Collection<ProviderContext> pctxs = manager.getProviderManager()
				.getProviders();

		for (ProviderContext ctx : pctxs) {
			Object adapted = ctx.getTarget().adapt(object);
			if (adapted != null) {
				Annotation[] array = manager.doGetAnnotations(adapted, false);
				if (array.length != 0 || manager.isAnnotable(adapted)) {
					haveAdapted = true;
				}
				for (Annotation annotation : array) {
					annotations.add(annotation);
				}
			}
		}
		return haveAdapted;
	}
}
