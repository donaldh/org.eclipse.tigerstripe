/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.stereotypes;

import static org.eclipse.jface.dialogs.IMessageProvider.ERROR;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class StereotypesPage extends TigerstripeFormPage implements Validatable {

	public static final String PAGE_ID = "workbenchProfile.stereotypes"; //$NON-NLS-1$
	private IManagedForm managedForm;
	private StereotypesSection section;

	public StereotypesPage(FormEditor editor) {
		super(editor, PAGE_ID, "Stereotypes");
	}

	public StereotypesPage() {
		super(PAGE_ID, "Stereotypes");
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		this.managedForm = managedForm;

		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Stereotype Definitions");
		fillBody(managedForm, toolkit);
		managedForm.refresh();
	}

	@Override
	public void refresh() {
		if (managedForm != null) {
			managedForm.refresh();
		}
	}

	private void fillBody(IManagedForm managedForm, FormToolkit toolkit) {
		Composite body = managedForm.getForm().getBody();
		TableWrapLayout layout = TigerstripeLayoutFactory
				.createPageTableWrapLayout(2, false);
		body.setLayout(layout);

		section = new StereotypesSection(this, body, toolkit);
		managedForm.addPart(section);
	}

	public boolean hasErrors() {
		if (managedForm != null) {
			ScrolledForm form = managedForm.getForm();
			if (form != null) {
				return form.getMessageType() == ERROR;
			}
		}
		return false;
	}

	public String getErrorMessage() {
		return managedForm.getForm().getMessage();
	}

	public void validate() {
		if (section != null) {
			section.validate();
		}
	}
}
