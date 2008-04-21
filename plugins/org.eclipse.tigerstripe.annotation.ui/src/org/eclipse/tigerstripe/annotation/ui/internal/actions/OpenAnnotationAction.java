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
package org.eclipse.tigerstripe.annotation.ui.internal.actions;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.tigerstripe.annotation.ui.internal.util.AnnotationUtils;

/**
 * @author Yuri Strot
 *
 */
public class OpenAnnotationAction extends DelegateAction {
	
	private Object object;
	
	public OpenAnnotationAction() {
		super("Open");
    }
	
	@Override
	public void run() {
		AnnotationUIPlugin.getManager().open(object);
	}
	
	@Override
	protected void adaptSelection(ISelection selection) {
		object = null;
		Annotation annotation = AnnotationUtils.getAnnotation(selection);
		if (annotation != null) {
			object = AnnotationPlugin.getManager().getObject(annotation.getUri());
		}
		setEnabled(object != null);
	}

}
