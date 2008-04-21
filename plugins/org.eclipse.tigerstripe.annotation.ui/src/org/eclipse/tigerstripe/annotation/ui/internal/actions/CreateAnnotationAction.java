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
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.annotation.ui.internal.util.AnnotationUtils;
import org.eclipse.tigerstripe.annotation.ui.util.WorkbenchUtil;
import org.eclipse.tigerstripe.annotation.ui.wizard.CreateAnnotationWizard;


/**
 * @author Yuri Strot
 *
 */
public class CreateAnnotationAction extends DelegateAction {
	
	private Object object;

	public void run() {
		Shell shell = WorkbenchUtil.getShell();
		if (shell == null)
			return;
		CreateAnnotationWizard wizard = new CreateAnnotationWizard(object);
		WizardDialog dialog= new WizardDialog(shell, wizard);
		dialog.create();
		dialog.getShell().setSize(500, 500);
		dialog.open();
    }
	
	@Override
	protected void adaptSelection(ISelection selection) {
		object = AnnotationUtils.getAnnotableElement(selection);
		setEnabled(object != null);
	}

}
