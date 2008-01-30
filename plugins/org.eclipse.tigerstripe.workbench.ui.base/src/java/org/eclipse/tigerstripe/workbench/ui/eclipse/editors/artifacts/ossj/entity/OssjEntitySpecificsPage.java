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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.entity;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.IArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.EntityMethodFlavorSection;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.IOssjArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.IOssjArtifactFormLabelProvider;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class OssjEntitySpecificsPage extends TigerstripeFormPage {

	private IOssjArtifactFormLabelProvider labelProvider;

	private IOssjArtifactFormContentProvider contentProvider;

	private IManagedForm managedForm;

	public static final String PAGE_ID = "ossj.entity.ossj"; //$NON-NLS-1$

	public OssjEntitySpecificsPage(FormEditor editor,
			IOssjArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider) {
		super(editor, PAGE_ID, "Details");
		this.labelProvider = labelProvider;
		this.contentProvider = contentProvider;
	}

	public OssjEntitySpecificsPage() {
		super(PAGE_ID, "Details");
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
		layout.numColumns = 1;
		layout.verticalSpacing = 30;
		layout.horizontalSpacing = 10;
		body.setLayout(layout);

		TigerstripeSectionPart part = new OssjEntitySpecificsSection(this,
				body, toolkit, labelProvider, contentProvider);
		managedForm.addPart(part);

		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);

		if (prop
				.getPropertyValue(IOssjLegacySettigsProperty.DISPLAY_OSSJSPECIFICS)) {
			part = new EntityMethodFlavorSection(this, body, toolkit,
					labelProvider, contentProvider);
			managedForm.addPart(part);
		}
	}
}
