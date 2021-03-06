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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.rules;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class PluginDescriptorRulesPage extends TigerstripeFormPage {

	public static final String PAGE_ID = "pluginDescriptor.rules"; //$NON-NLS-1$

	public PluginDescriptorRulesPage(FormEditor editor) {
		super(editor, PAGE_ID, "Rules");
	}

	public PluginDescriptorRulesPage() {
		super(PAGE_ID, "Rules");
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Generator Rules");
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
		managedForm.addPart(new GlobalRulesSection(this, body, toolkit));
		managedForm.addPart(new ModelRulesSection(this, body, toolkit));
		managedForm.addPart(new ArtifactRulesSection(this, body, toolkit));
	}
}
