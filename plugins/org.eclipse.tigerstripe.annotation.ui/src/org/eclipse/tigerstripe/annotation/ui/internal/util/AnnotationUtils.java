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

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotable;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationUtils {
	
	public static Object getAnnotableElement(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection)selection;
			Iterator<?> it = sel.iterator();
			while (it.hasNext()) {
	            Object element = it.next();
	            if (element instanceof Annotation) {
	            	return AnnotationPlugin.getManager().getObject(
	            		((Annotation)element).getUri());
	            }
	            IAnnotable adaptable = (IAnnotable)Platform.getAdapterManager(
            		).getAdapter(element, IAnnotable.class);
	            if (adaptable != null)
	            	return element;
            }
		}
		return null;
	}
	
	public static Annotation getAnnotation(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection)selection;
			Iterator<?> it = sel.iterator();
			while (it.hasNext()) {
	            Object element = it.next();
	            if (element instanceof Annotation) {
	            	return (Annotation)element;
	            }
            }
		}
		return null;
	}

}
