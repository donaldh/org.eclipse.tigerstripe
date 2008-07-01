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
package org.eclipse.tigerstripe.annotation.ui.example.editpartProvider;

import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.ui.diagrams.parts.AnnotationEditPart;
import org.eclipse.tigerstripe.annotation.ui.diagrams.parts.IAnnotationEditPartProvider;
import org.eclipse.tigerstripe.annotation.ui.example.entityNote.EntityNote;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationEditPartProvider implements IAnnotationEditPartProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.parts.IAnnotationEditPartProvider#getAnnotationEditPartClass(org.eclipse.tigerstripe.annotation.core.Annotation)
	 */
	public Class<? extends AnnotationEditPart> getAnnotationEditPartClass(
			Annotation annotation) {
		if (annotation.getContent() instanceof EntityNote)
			return AnnotationImageEditPart.class;
		return null;
	}

}
