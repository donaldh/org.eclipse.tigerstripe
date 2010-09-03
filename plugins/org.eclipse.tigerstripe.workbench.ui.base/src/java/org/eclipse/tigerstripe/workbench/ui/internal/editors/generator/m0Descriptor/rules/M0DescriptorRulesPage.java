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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.generator.m0Descriptor.rules;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class M0DescriptorRulesPage extends TigerstripeFormPage {

	public static final String PAGE_ID = "m0Descriptor.rules"; //$NON-NLS-1$

	public M0DescriptorRulesPage(FormEditor editor) {
		super(editor, PAGE_ID, "Rules");
	}

	public M0DescriptorRulesPage() {
		super(PAGE_ID, "Rules");
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("M0 Generator Rules");
		fillBody(managedForm, toolkit);
		managedForm.refresh();
	}

	@Override
	public void refresh() {
		// TODO
	}

	private void fillBody(IManagedForm managedForm, FormToolkit toolkit) {
		Composite body = managedForm.getForm().getBody();
		GridLayoutFactory.fillDefaults().margins(3, 3).applyTo(body);
		// sections
		M0GlobalRulesSection rulesSection = new M0GlobalRulesSection(this,
				body, toolkit);
		rulesSection.getSection().setExpanded(true);
		managedForm.addPart(rulesSection);
		// managedForm.addPart(new ArtifactRulesSection(this, body, toolkit));
	}
}
