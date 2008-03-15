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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.generator;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.header.PluginDebugSection;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.header.PluginPackageSection;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public abstract class GeneratorOverviewPage extends TigerstripeFormPage {

	private IManagedForm managedForm;

	public GeneratorOverviewPage(FormEditor editor, String pageId) {
		super(editor, pageId, "Overview");
	}

	public GeneratorOverviewPage(String pageId) {
		super(pageId, "Overview");
	}

	protected abstract String getFormTitle();

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		this.managedForm = managedForm;

		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText(getFormTitle());
		fillBody(managedForm, toolkit);
		managedForm.refresh();
	}

	@Override
	public void refresh() {
		if (managedForm != null) {
			managedForm.refresh();
		}
	}

	protected abstract TigerstripeSectionPart getWelcomeSection(
			TigerstripeFormPage page, Composite body, FormToolkit toolkit);

	protected abstract TigerstripeSectionPart getContentSection(
			TigerstripeFormPage page, Composite body, FormToolkit toolkit);

	protected abstract TigerstripeSectionPart getGeneralInfoSection(
			TigerstripeFormPage page, Composite body, FormToolkit toolkit);

	private void fillBody(IManagedForm managedForm, FormToolkit toolkit) {
		Composite body = managedForm.getForm().getBody();
		TableWrapLayout layout = new TableWrapLayout();
		layout.bottomMargin = 10;
		layout.topMargin = 5;
		layout.leftMargin = 10;
		layout.rightMargin = 10;
		layout.numColumns = 2;
		layout.verticalSpacing = 30;
		layout.horizontalSpacing = 10;
		body.setLayout(layout);

		managedForm.addPart(getWelcomeSection(this, body, toolkit));
		managedForm.addPart(getGeneralInfoSection(this, body, toolkit));
		managedForm.addPart(getContentSection(this, body, toolkit));
		managedForm.addPart(new PluginDebugSection(this, body, toolkit));
		managedForm.addPart(new PluginPackageSection(this, body, toolkit));
	}
}
