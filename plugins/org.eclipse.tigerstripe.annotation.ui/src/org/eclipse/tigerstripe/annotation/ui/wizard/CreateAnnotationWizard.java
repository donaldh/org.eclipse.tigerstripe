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
package org.eclipse.tigerstripe.annotation.ui.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.CreateSpecificTypeAnnotationAction;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * @author Yuri Strot
 *
 */
public class CreateAnnotationWizard extends Wizard implements INewWizard {
	
	private static final String TITLE = "Create Annotation Wizard";
	
	protected CreateAnnotationWizardPage page;
	protected Object object;
	
	public CreateAnnotationWizard() {
		setWindowTitle(TITLE);
	}
	
	public CreateAnnotationWizard(Object object) {
		this();
		this.object = object;
	}
	
	@Override
	public void addPages() {
		page = new CreateAnnotationWizardPage(object);
		addPage(page);
	}

	@Override
    public boolean performFinish() {
		AnnotationType type = page.getType();
		addContent(type);
		return true;
    }
	
	protected void addContent(AnnotationType type) {
		if (object != null) {
			new CreateSpecificTypeAnnotationAction(object, type).run();
		}
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
    }

}
