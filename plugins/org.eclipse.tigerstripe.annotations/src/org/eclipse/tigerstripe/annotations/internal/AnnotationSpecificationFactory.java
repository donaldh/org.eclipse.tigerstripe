/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    erdillon (Cisco Systems, Inc.) - Initial version
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations.internal;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.IAnnotationForm;
import org.eclipse.tigerstripe.annotations.IAnnotationScheme;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;

/**
 * A Factory for all AnnotationSpecs
 * 
 * @author erdillon
 * 
 */
public class AnnotationSpecificationFactory {

	private static String[] annotationSpecKeys = {
			"stringAnnotationSpecification", "booleanAnnotationSpecification" };

	private static Class[] annotationSpecImpls = {
			StringAnnotationSpecification.class,
			BooleanAnnotationSpecification.class };

	/**
	 * 
	 * @param element
	 * @param parentScheme
	 * @return
	 * @throws AnnotationCoreException
	 */
	public static IAnnotationSpecification parseAnnotationSpecification(
			IConfigurationElement element, IAnnotationForm parentForm)
			throws AnnotationCoreException {
		BaseAnnotationSpecification result = null;
		String elementName = element.getName();
		for (int i = 0; i < annotationSpecKeys.length; i++) {
			if (elementName.equals(annotationSpecKeys[i])) {
				Class<BaseAnnotationSpecification> impl = annotationSpecImpls[i];
				try {
					result = impl.newInstance();
					result.populate(element, parentForm);
				} catch (IllegalAccessException e) {
					AnnotationCoreException ee = new AnnotationCoreException(e);
					throw ee;
				} catch (InstantiationException e) {
					AnnotationCoreException ee = new AnnotationCoreException(e);
					throw ee;
				}
			}
		}
		return result;
	}
}
