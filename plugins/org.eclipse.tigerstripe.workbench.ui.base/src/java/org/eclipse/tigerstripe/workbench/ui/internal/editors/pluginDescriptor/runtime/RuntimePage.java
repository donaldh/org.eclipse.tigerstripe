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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.runtime;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class RuntimePage extends TigerstripeFormPage {

	public static final String PAGE_ID = "pluginDescriptor.runtime"; //$NON-NLS-1$
	private IManagedForm managedForm;

	public RuntimePage(FormEditor editor) {
		super(editor, PAGE_ID, "Runtime");
	}

	public RuntimePage() {
		super(PAGE_ID, "Runtime");
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		this.managedForm = managedForm;

		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Tigerstripe Generator Project");
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

		managedForm.addPart(new PluginLogSection(this, body, toolkit));
		managedForm.addPart(new ClasspathEntrySection(this, body, toolkit));
		managedForm.addPart(new IncludedFilesSection(this, body, toolkit));
		managedForm.addPart(new AnnotationPluginsSection(this, body, toolkit));
	}
}
