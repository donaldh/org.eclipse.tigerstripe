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

import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.example.person.PersonPackage;
import org.eclipse.tigerstripe.annotation.ui.core.properties.AnnotationFilter;

/**
 * @author Yuri Strot
 *
 */
public class CustomAnnotationFilter extends AnnotationFilter {

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.AnnotationFilter#select(org.eclipse.tigerstripe.annotation.core.Annotation)
	 */
	@Override
	public boolean select(Annotation annotation) {
		AnnotationType type = AnnotationPlugin.getManager().getType(annotation);
		if (type.getClazz().equals(PersonPackage.eINSTANCE.getPerson()))
			return true;
		return false;
	}

}
