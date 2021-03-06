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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.segment.scope;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.segment.SegmentEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class ScopePage extends TigerstripeFormPage {

	public static final String PAGE_ID = "contractSegment.scope"; //$NON-NLS-1$
	private IManagedForm managedForm;

	public ScopePage(FormEditor editor) {
		super(editor, PAGE_ID, "Scope");
	}

	public ScopePage() {
		super(PAGE_ID, "Scope");
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		this.managedForm = managedForm;

		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Facet Scope");
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

		managedForm.addPart(new IncludeExcludeSection(this, body, toolkit));
		managedForm.addPart(new AnnotationIncludeExcludeSection(this, body,
				toolkit));
		// managedForm.addPart(new AnnotationContextIncludeExcludeSection(this,
		//		body, toolkit));

	}

	public ISegmentScope getScope() throws TigerstripeException {
		return ((SegmentEditor) getEditor()).getSegment().getISegmentScope();
	}
}
