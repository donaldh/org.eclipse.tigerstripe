/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.annotation;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeSectionPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class AnnotationPage extends TigerstripeFormPage {

	private IManagedForm managedForm;

	public static final String PAGE_ID = "org.eclipse.tigerstripe.sdk.extension.annotation"; 

	public AnnotationPage(FormEditor editor) {
		super(editor, PAGE_ID, "Annotations");
	}

	public AnnotationPage() {
		super(PAGE_ID, "Annotations");
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		this.managedForm = managedForm;
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();

		form.setText("Contributions to various Annotation Extensions");
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

		TigerstripeSectionPart part = new AnnotationSection(this,
				body, toolkit, ExpandableComposite.EXPANDED);
		managedForm.addPart(part);
		part = new AnnotationPackageLabelSection(this,
				body, toolkit, ExpandableComposite.EXPANDED);
		managedForm.addPart(part);
		part = new AnnotationExplicitFileRouterSection(this,
				body, toolkit, ExpandableComposite.EXPANDED);
		managedForm.addPart(part);
		part = new AnnotationPropertyProviderSection(this,
				body, toolkit, ExpandableComposite.EXPANDED);
		managedForm.addPart(part);
		
	}

}
