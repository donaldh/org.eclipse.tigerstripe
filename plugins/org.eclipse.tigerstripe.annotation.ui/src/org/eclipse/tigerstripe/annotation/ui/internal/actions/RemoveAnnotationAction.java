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

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.ui.PlatformUI;

/**
 * @author Yuri Strot
 *
 */
public class RemoveAnnotationAction extends Action {
	
	private Annotation annotation;
	
	public RemoveAnnotationAction(Annotation annotation) {
		this.annotation = annotation;
	}

	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		MessageBox message = new MessageBox(shell, SWT.YES | SWT.NO);
		message.setText("Confirm Delete");
		message.setMessage("Are you sure you want to delete annotation '" + 
			annotation.getUri().toString() + "'?");
		if (message.open() == SWT.YES) {
			AnnotationPlugin.getManager().removeAnnotation(annotation);
		}
    }

}
