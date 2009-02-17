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

package org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeSectionPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class ConfigOverviewPage extends TigerstripeFormPage {

	private IExtensionFormContentProvider contentProvider;

	private IManagedForm managedForm;

	public static final String PAGE_ID = "ossj.entity.overview"; //$NON-NLS-1$

	public ConfigOverviewPage(FormEditor editor) {
		super(editor, PAGE_ID, "Overview");
	}

	public ConfigOverviewPage() {
		super(PAGE_ID, "Overview");
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		this.managedForm = managedForm;
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();

		form.setText("My Editor");
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

		TigerstripeSectionPart part = new ConfigGeneralInfoSection(this,
				body, toolkit, contentProvider);
		managedForm.addPart(part);

//		part = new ArtifactContentSection(this, body, toolkit,
//				labelProvider, contentProvider);
//		managedForm.addPart(part);
//
//		if (contentProvider.hasSpecifics()) {
//			part = contentProvider.getSpecifics(this, body, toolkit);
//			managedForm.addPart(part);
//		}
//
//		if (contentProvider.hasAttributes()) {
//			part = new ArtifactAttributesSection(this, body, toolkit,
//					labelProvider, contentProvider, getAttributesStyle());
//			managedForm.addPart(part);
//		}
//
//		if (contentProvider.hasConstants()) {
//			part = new ArtifactConstantsSection(this, body, toolkit,
//					labelProvider, contentProvider, getConstantsStyle());
//
//			// see bug #77
//			// See #90 no need to set up setForcedType anymore. So #77 is kinda
//			// undone.
//
//			managedForm.addPart(part);
//		}
//
//		if (contentProvider.hasMethods()) {
//			part = new ArtifactMethodsSection(this, body, toolkit,
//					labelProvider, contentProvider, getMethodsStyle());
//			managedForm.addPart(part);
//		}
	}

	private int getAttributesStyle() {


		return ExpandableComposite.COMPACT;
	}
	
	private int getMethodsStyle() {



		return ExpandableComposite.COMPACT;
	}
	
	private int getConstantsStyle() {


		return ExpandableComposite.COMPACT;
	}
}
