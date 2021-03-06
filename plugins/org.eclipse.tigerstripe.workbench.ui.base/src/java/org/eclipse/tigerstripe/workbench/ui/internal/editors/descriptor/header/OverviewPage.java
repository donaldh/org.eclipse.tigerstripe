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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.header;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class OverviewPage extends TigerstripeFormPage {

	public static final String PAGE_ID = "descriptor.repositories"; //$NON-NLS-1$
	private IManagedForm managedForm;

	public OverviewPage(FormEditor editor) {
		super(editor, PAGE_ID, "Overview");
	}

	public OverviewPage() {
		super(PAGE_ID, "Overview");
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		this.managedForm = managedForm;

		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Tigerstripe Project");
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
		body.setLayout(TigerstripeLayoutFactory.createPageTableWrapLayout(2,
				true));

		managedForm.addPart(new WelcomeSection(this, body, toolkit));
		managedForm.addPart(new GeneralInfoSection(this, body, toolkit));
		managedForm.addPart(new ProjectContentSection(this, body, toolkit));
		managedForm.addPart(new ProjectDefaultsSection(this, body, toolkit));
		toolkit.createComposite(body);
	}
}
