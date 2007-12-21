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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.IArtifactFormContentProvider;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class OssjArtifactOverviewPage extends TigerstripeFormPage {

	private IOssjArtifactFormLabelProvider labelProvider;

	private IOssjArtifactFormContentProvider contentProvider;

	private IManagedForm managedForm;

	public static final String PAGE_ID = "ossj.entity.overview"; //$NON-NLS-1$

	public OssjArtifactOverviewPage(FormEditor editor,
			IOssjArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider) {
		super(editor, PAGE_ID, "Overview");
		this.labelProvider = labelProvider;
		this.contentProvider = contentProvider;
	}

	public OssjArtifactOverviewPage() {
		super(PAGE_ID, "Overview");
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		this.managedForm = managedForm;
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();

		form.setText(contentProvider
				.getText(IArtifactFormContentProvider.ARTIFACT_OVERVIEW_TITLE));
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

		// Annoyance 14 - removed welcome section (js)

		TigerstripeSectionPart part = new OssjArtifactGeneralInfoSection(this,
				body, toolkit, labelProvider, contentProvider);
		managedForm.addPart(part);

		part = new OssjArtifactContentSection(this, body, toolkit,
				labelProvider, contentProvider);
		managedForm.addPart(part);

		if (contentProvider.hasSpecifics()) {
			part = contentProvider.getSpecifics(this, body, toolkit);
			managedForm.addPart(part);
		}

		if (contentProvider.hasAttributes()) {
			part = new OssjArtifactAttributesSection(this, body, toolkit,
					labelProvider, contentProvider);
			managedForm.addPart(part);
		}

		if (contentProvider.hasConstants()) {
			part = new OssjArtifactConstantsSection(this, body, toolkit,
					labelProvider, contentProvider);

			// see bug #77
			// See #90 no need to set up setForcedType anymore. So #77 is kinda
			// undone.

			managedForm.addPart(part);
		}

		if (contentProvider.hasMethods()) {
			part = new OssjArtifactMethodsSection(this, body, toolkit,
					labelProvider, contentProvider);
			managedForm.addPart(part);
		}
	}
}
