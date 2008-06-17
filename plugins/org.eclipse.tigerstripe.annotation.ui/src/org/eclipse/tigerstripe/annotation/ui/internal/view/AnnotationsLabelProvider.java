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
package org.eclipse.tigerstripe.annotation.ui.internal.view;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.ui.util.DisplayAnnotationUtil;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationsLabelProvider extends LabelProvider implements ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		if (element instanceof Annotation && columnIndex == 0)
			return DisplayAnnotationUtil.getImage((Annotation)element);
	    return null;
    }

	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof Annotation) {
			Annotation annotation = (Annotation)element;
			switch (columnIndex) {
    			case 0:
					AnnotationType type = AnnotationPlugin.getManager().getType(annotation);
					return type == null ? null : type.getName();
				case 1:
					URI uri = annotation.getUri();
					return uri == null ? null : uri.toString();
			}
		}
	    return null;
    }

}
