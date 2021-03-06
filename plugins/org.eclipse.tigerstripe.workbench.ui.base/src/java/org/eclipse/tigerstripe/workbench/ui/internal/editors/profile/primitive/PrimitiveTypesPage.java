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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.primitive;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.metamodel.impl.IPrimitiveTypeImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class PrimitiveTypesPage extends TigerstripeFormPage {

	public static final String PAGE_ID = "workbenchProfile.primitiveTypes"; //$NON-NLS-1$
	private IManagedForm managedForm;

	public PrimitiveTypesPage(FormEditor editor) {
		super(editor, PAGE_ID, "Primitive Types");
	}

	public PrimitiveTypesPage() {
		super(PAGE_ID, "Primitive Types");
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		this.managedForm = managedForm;

		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText(ArtifactMetadataFactory.INSTANCE.getMetadata(
				IPrimitiveTypeImpl.class.getName()).getLabel(null)
				+ " Definitions");
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

		managedForm.addPart(new PrimitiveTypeDefsSection(this, body, toolkit));
	}

}
