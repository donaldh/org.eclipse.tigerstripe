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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.segment.useCases;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.segment.SegmentEditor;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class UseCasesPage extends TigerstripeFormPage {

	public static final String PAGE_ID = "contractFacet.useCases"; //$NON-NLS-1$
	private IManagedForm managedForm;

	public UseCasesPage(FormEditor editor) {
		super(editor, PAGE_ID, "Use Cases");
	}

	public UseCasesPage() {
		super(PAGE_ID, "Use Cases");
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		this.managedForm = managedForm;

		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Use Cases");
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
		TableWrapLayout layout = new TableWrapLayout();
		layout.bottomMargin = 10;
		layout.topMargin = 5;
		layout.leftMargin = 10;
		layout.rightMargin = 10;
		layout.numColumns = 2;
		layout.verticalSpacing = 30;
		layout.horizontalSpacing = 10;
		body.setLayout(layout);

		managedForm.addPart(new UseCaseSection(this, body, toolkit));
	}

	public IContractSegment getFacet() throws TigerstripeException {
		return ((SegmentEditor) getEditor()).getSegment();
	}
}
