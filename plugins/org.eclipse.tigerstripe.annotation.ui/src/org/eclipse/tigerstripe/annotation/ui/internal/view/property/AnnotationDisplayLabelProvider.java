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
package org.eclipse.tigerstripe.annotation.ui.internal.view.property;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.ui.util.DisplayAnnotationUtil;

/**
 * @author Yuri Strot
 * 
 */
public class AnnotationDisplayLabelProvider extends LabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof Annotation) {
			Annotation annotation = (Annotation) element;
			StringBuilder builder = new StringBuilder();
			if (isDirty(annotation)) {
				builder.append('*');
			}
			builder.append(DisplayAnnotationUtil.getText(annotation));
			return builder.toString();
		}
		return super.getText(element);
	}

	private boolean isDirty(Annotation annotation) {
		EObject eObject = annotation.getContent();
		if (eObject != null) {
			for (Adapter adapter : eObject.eAdapters()) {
				if (adapter instanceof DirtyAdapter) {
					return ((DirtyAdapter) adapter).isDirty();
				}
			}
		}
		return false;
	}

}
