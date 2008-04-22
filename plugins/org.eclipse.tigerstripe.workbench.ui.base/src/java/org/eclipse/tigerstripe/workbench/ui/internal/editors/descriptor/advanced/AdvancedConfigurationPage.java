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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.advanced;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class AdvancedConfigurationPage extends TigerstripeFormPage {

	private boolean shownDescriptorUpgrade = false;
	private GenerationPrefSection generationPrefSection;
	public static final String PAGE_ID = "projectDescriptor.advanced"; //$NON-NLS-1$

	public boolean getShownDescriptorUpgrade() {
		return shownDescriptorUpgrade;
	}

	public void setShownDescriptorUpgrade(boolean setShownDescriptorUpgrade) {
		this.shownDescriptorUpgrade = setShownDescriptorUpgrade;
	}

	public AdvancedConfigurationPage(FormEditor editor) {
		super(editor, PAGE_ID, "Advanced Settings");
		// TODO Auto-generated constructor stub
	}

	public AdvancedConfigurationPage() {
		super(PAGE_ID, "Advanced Settings");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Advanced Settings");
		fillBody(managedForm, toolkit);
		managedForm.refresh();
	}

	@Override
	public void refresh() {
		// TODO
	}

	public void openGenerationPrefSection() {
		generationPrefSection.getSection().setExpanded(true);
	}

	private void fillBody(IManagedForm managedForm, FormToolkit toolkit) {
		Composite body = managedForm.getForm().getBody();
		TableWrapLayout layout = new TableWrapLayout();
		body.setLayout(layout);

		// sections
		generationPrefSection = new GenerationPrefSection(this, body, toolkit);
		managedForm.addPart(generationPrefSection);
		// managedForm.addPart( new AdvancedGeneralSection(this, body, toolkit
		// ));
		// managedForm.addPart( new ModelImportSection(this, body, toolkit ));
	}
}
