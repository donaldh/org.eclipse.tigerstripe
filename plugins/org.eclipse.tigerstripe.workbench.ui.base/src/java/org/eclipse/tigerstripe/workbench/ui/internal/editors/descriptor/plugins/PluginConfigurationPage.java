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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.plugins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IGlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggableHousing;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.GlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.DescriptorEditor;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class PluginConfigurationPage extends TigerstripeFormPage {

	public static final String PAGE_ID = "descriptor.pluginInformation"; //$NON-NLS-1$

	private IManagedForm managedForm;
	private FormEditor editor;
		
	
	public PluginConfigurationPage(FormEditor editor) {
		super(editor, PAGE_ID, "Plugin Settings");
		// TODO Auto-generated constructor stub
		this.editor = editor;
		
	}

	public PluginConfigurationPage() {
		super(PAGE_ID, "Plugin Settings");
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		this.managedForm = managedForm;
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Plugin Settings");
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
		body.setLayout(layout);

		managedForm.addPart(new GenerationPrefSection(this, body, toolkit));

		// sections
		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);

		
		addUserPluginsParts(managedForm, toolkit);
	}

	protected ITigerstripeModelProject getTSProject() {
		DescriptorEditor editor = (DescriptorEditor) getEditor();
		return editor.getTSProject();
	}
	
	private void addUserPluginsParts(IManagedForm managedForm,
			FormToolkit toolkit) {
		Composite body = managedForm.getForm().getBody();
		List<PluggableHousing> housings = PluginManager.getManager()
				.getRegisteredPluggableHousings();
		
		ITigerstripeModelProject handle = getTSProject();
		
		
		
		if (! PluginManager.getManager().isOsgiVersioning()){
			for (PluggableHousing housing : housings) {
				managedForm.addPart(new PluggablePluginSection(this, body, toolkit,
					housing));
			}
		} else {
			
			Map<String, Collection<PluggableHousing>> map = new HashMap<String, Collection<PluggableHousing>>();
			for (PluggableHousing housing : housings) {
				// Just build ONE entry per "name" - the resolution takes place within the section
				String name = housing.getPluginName();
				if (map.containsKey(name)){
					map.get(name).add(housing);
				} else {
					Collection<PluggableHousing> phs = new ArrayList<PluggableHousing>();
					phs.add(housing);
					map.put(name, phs);
				}
			}
			for (String name : map.keySet()){
				managedForm.addPart(new OsgiPluggablePluginSection(this, body, toolkit,
						map.get(name)));
			}
		}
	}
}
