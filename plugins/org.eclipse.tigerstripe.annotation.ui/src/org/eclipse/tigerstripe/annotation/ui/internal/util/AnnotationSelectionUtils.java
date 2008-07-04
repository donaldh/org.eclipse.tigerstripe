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
package org.eclipse.tigerstripe.annotation.ui.internal.util;

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.annotation.core.Annotation;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationSelectionUtils {
	
	public static Object getAnnotableElement(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection)selection;
			return sel.getFirstElement();
		}
		return null;
	}
	
	public static Annotation getAnnotation(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection)selection;
			Iterator<?> it = sel.iterator();
			while (it.hasNext()) {
	            Object element = it.next();
	            Annotation annotation = getAnnotation(element);
	            if (annotation != null)
	            	return annotation;
            }
		}
		return null;
	}
	
	public static Annotation getAnnotation(Object element) {
		Annotation annotation = null;
        if (element instanceof Annotation) {
        	annotation = (Annotation)element;
        }
        else {
        	annotation = (Annotation)Platform.getAdapterManager(
        			).getAdapter(element, Annotation.class);
        	if (annotation == null && element instanceof IAdaptable) {
        		annotation = (Annotation)((IAdaptable)element).getAdapter(Annotation.class);
        	}
        }
        return annotation;
	}

}
