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
package org.eclipse.tigerstripe.annotation.ui.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;

/**
 * @author Yuri Strot
 *
 */
public class DisplayAnnotationUtil {
	
	public static String getText(Annotation annotation) {
		EObject content = annotation.getContent();
		if (content != null) {
			AnnotationType type = AnnotationPlugin.getManager().getType(annotation);
			if (type != null) {
				ILabelProvider provider = AnnotationUIPlugin.getManager().getLabelProvider(type);
				if (provider != null)
					return provider.getText(content);
			}
			return content.eClass().getName();
		}
		return "<no content>";
	}
	
	public static Image getImage(Annotation annotation) {
		AnnotationType type = AnnotationPlugin.getManager().getType(annotation);
		ILabelProvider provider = AnnotationUIPlugin.getManager().getLabelProvider(type);
		if (provider != null)
			return provider.getImage(annotation.getContent());
		return null;
	}

}
