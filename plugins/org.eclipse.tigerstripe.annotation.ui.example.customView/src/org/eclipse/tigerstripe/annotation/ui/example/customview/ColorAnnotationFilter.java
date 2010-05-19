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
package org.eclipse.tigerstripe.annotation.ui.example.customview;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.ui.core.properties.AnnotationFilter;
import org.eclipse.tigerstripe.annotation.ui.example.customview.styles.StylesPackage;

/**
 * @author Yuri Strot
 * 
 */
public class ColorAnnotationFilter extends AnnotationFilter {

	private static final EClass COLOR_CLASS = StylesPackage.eINSTANCE
			.getColor();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.ui.core.properties.AnnotationFilter
	 * #select(org.eclipse.tigerstripe.annotation.core.Annotation)
	 */
	@Override
	protected boolean select(Annotation annotation) {
		EObject content = annotation.getContent();
		if (content != null && content.eClass().equals(COLOR_CLASS))
			return true;
		return false;
	}

}
