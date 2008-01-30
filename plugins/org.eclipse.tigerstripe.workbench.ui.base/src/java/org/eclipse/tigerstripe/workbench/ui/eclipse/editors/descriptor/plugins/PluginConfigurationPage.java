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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.plugins;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggableHousing;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class PluginConfigurationPage extends TigerstripeFormPage {

	public static final String PAGE_ID = "descriptor.pluginInformation"; //$NON-NLS-1$

	public PluginConfigurationPage(FormEditor editor) {
		super(editor, PAGE_ID, "Plugin Settings");
		// TODO Auto-generated constructor stub
	}

	public PluginConfigurationPage() {
		super(PAGE_ID, "Plugin Settings");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Plugin Settings");
		fillBody(managedForm, toolkit);
		managedForm.refresh();
	}

	@Override
	public void refresh() {
		// TODO
	}

	private void fillBody(IManagedForm managedForm, FormToolkit toolkit) {
		Composite body = managedForm.getForm().getBody();
		TableWrapLayout layout = new TableWrapLayout();
		body.setLayout(layout);

		managedForm.addPart(new GenerationPrefSection(this, body, toolkit));

		// sections
		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);

		if (prop.getPropertyValue(IOssjLegacySettigsProperty.ENABLE_JVT_PLUGIN)) {
			managedForm.addPart(new JVTProfileSection(this, body, toolkit));
		}

		if (prop.getPropertyValue(IOssjLegacySettigsProperty.ENABLE_XML_PLUGIN)) {
			managedForm.addPart(new XMLProfileSection(this, body, toolkit));
		}

		if (prop
				.getPropertyValue(IOssjLegacySettigsProperty.ENABLE_WSDL_PLUGIN)) {
			managedForm.addPart(new WSDLProfileSection(this, body, toolkit));
		}

		addUserPluginsParts(managedForm, toolkit);
	}

	private void addUserPluginsParts(IManagedForm managedForm,
			FormToolkit toolkit) {
		Composite body = managedForm.getForm().getBody();
		List<PluggableHousing> housings = PluginManager.getManager()
				.getRegisteredPluggableHousings();
		for (PluggableHousing housing : housings) {
			managedForm.addPart(new PluggablePluginSection(this, body, toolkit,
					housing));
		}
	}
}
