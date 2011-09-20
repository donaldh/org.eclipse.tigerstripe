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

import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.annotation.ui.core.ITargetProcessor;
import org.eclipse.tigerstripe.annotation.ui.internal.core.TargetProcessorSource;
import org.eclipse.tigerstripe.annotation.ui.util.WorkbenchUtil;
import org.eclipse.tigerstripe.annotation.ui.wizard.CreateAnnotationWizard;

/**
 * @author Yuri Strot
 * 
 */
public class OpenAnnotationWizardAction extends Action {

	private Object object;

	public OpenAnnotationWizardAction() {
	}

	public OpenAnnotationWizardAction(Object object, String name) {
		this(object, name, null);
	}

	public OpenAnnotationWizardAction(Object object, String name,
			ImageDescriptor image) {
		super(name, image);
		this.object = object;
	}

	public void run() {

		Shell shell = WorkbenchUtil.getShell();
		if (shell == null) {
			return;
		}
		
		Collection<ITargetProcessor> processors = TargetProcessorSource.getProcessors();
		
		for (ITargetProcessor processor : processors) {
			if (processor.isDirty(object)) {
				String name = processor.getName(object);
				String message = processor.getDirtyViolationMessage(object);
				MessageDialog.openInformation(shell, name + " is dirty", message);
				return;
			}
		}
		
		CreateAnnotationWizard wizard = new CreateAnnotationWizard(object);
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.getShell().setSize(500, 500);
		dialog.open();
	}

}
