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
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;

/**
 * @author Yuri Strot
 *
 */
public class DelegateAction extends Action implements IActionDelegate {
	
	public DelegateAction() {
		super();
	}
	
	public DelegateAction(String text) {
		super(text);
	}
	
	public DelegateAction(String text, ImageDescriptor des) {
		super(text, des);
	}
	
	public DelegateAction(String text, int style) {
		super(text, style);
	}

	public void run(IAction action) {
		run();
    }
	
	protected static Object getSelected(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection)selection;
			return sel.getFirstElement();
		}
		return null;
	}

	public void selectionChanged(IAction action, ISelection selection) {
		adaptSelection(selection);
		action.setEnabled(isEnabled());
    }
	
	protected void adaptSelection(ISelection selection) {
		//user should implement this method
	}

}
