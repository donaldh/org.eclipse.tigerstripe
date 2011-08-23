/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Anton Salnik) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.internal.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.util.AnnotationUtils;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class RemoveAllAnnotationsHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		Annotation[] annotations = getAnnotations(event);
		if (annotations != null && annotations.length > 0) {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell();
			MessageBox message = new MessageBox(shell, SWT.YES | SWT.NO);
			message.setText("Confirm Delete");
			message.setMessage("Are you sure you want to delete all annotations from this object?");
			if (message.open() == SWT.YES) {
				for (int i = 0; i < annotations.length; i++) {
					AnnotationPlugin.getManager().removeAnnotation(
							annotations[i]);
				}
			}
		}
		return null;
	}

	private Annotation[] getAnnotations(ExecutionEvent event) {
		Object object = getSelection(event);
		if (object != null) {
			List<Annotation> annotations = new ArrayList<Annotation>();
			AnnotationUtils.getAllAnnotations(object, annotations);
			return annotations.toArray(new Annotation[annotations.size()]);
		}
		return null;
	}

	private Object getSelection(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event)
				.getActivePage().getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection) selection;
			return sel.getFirstElement();
		}
		return null;
	}
}
