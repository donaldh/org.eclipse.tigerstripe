/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/

package org.eclipse.tigerstripe.annotation.ui.core.properties;

import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.ui.core.view.AnnotationNote;
import org.eclipse.tigerstripe.annotation.ui.core.view.INote;

/**
 * Class determines if annotation section should be displayed
 * 
 * @author Yuri Strot
 * @see EPropertiesSection
 */
public class AnnotationFilter extends NoteFilter {

	/**
	 * Determines if the given annotation passes this filter
	 * 
	 * @param annotation specified annotation
	 * @return true if annotation section should be displayed for the specified annotation
	 * and false otherwise
	 */
	protected boolean select(Annotation annotation) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.ui.core.properties.NoteFilter#select
	 * (org.eclipse.tigerstripe.annotation.ui.core.view.INote)
	 */
	@Override
	public boolean select(INote note) {
		if (note instanceof AnnotationNote) {
			AnnotationNote aNote = (AnnotationNote) note;
			Annotation annotation = aNote.getAnnotation();
			if (annotation != null) {
				return select(annotation);
			}
		}
		return false;
	}

}
