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
package org.eclipse.tigerstripe.annotation.ui.core.properties;

import org.eclipse.jface.viewers.IFilter;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.ui.internal.view.property.TabDescriptionManipulator;

/**
 * Class determines if annotation section should be displayed
 * 
 * @author Yuri Strot
 * @see AnnotationPropertiesSection
 */
public abstract class AnnotationFilter implements IFilter {
	
	/**
	 * Determines if the given annotation passes this filter
	 * 
	 * @param annotation specified annotation
	 * @return true if annotation section should be displayed for the specified annotation
	 * and false otherwise
	 */
	public abstract boolean select(Annotation annotation);

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IFilter#select(java.lang.Object)
	 */
	public final boolean select(Object toTest) {
		if (toTest instanceof Annotation) {
			return TabDescriptionManipulator.getInstance().isEnabled(this);
		}
		return false;
	}

}
