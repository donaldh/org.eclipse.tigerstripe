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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.csvCreate;

import java.util.Properties;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.api.IPluginReference;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.plugin.csv.CSVPlugin;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.TigerstripeDescriptorSectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class AbstractCSVCreateSection extends
		TigerstripeDescriptorSectionPart {

	public AbstractCSVCreateSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit, int details) {
		super(page, parent, toolkit, details);
	}

	/**
	 * Get the properties for the XML Plugin. Returns an empty set of properties
	 * if the plugin is not referenced in the Tigerstripe.xml descriptor.
	 * 
	 */
	protected Properties getCSVCreatePluginProperties() {
		IPluginReference ref = getCSVCreatePluginReference();
		if (ref != null)
			return ref.getProperties();

		return null;
	}

	/**
	 * Returns the XML plugin ref from the descriptor if it exists, null
	 * otherwise.
	 * 
	 * @return
	 */
	protected IPluginReference getCSVCreatePluginReference() {
		try {

			ITigerstripeProject handle = getTSProject();
			IPluginReference[] plugins = handle.getPluginReferences();

			for (int i = 0; i < plugins.length; i++) {
				if (CSVPlugin.PLUGIN_ID.equals(plugins[i].getPluginId()))
					return plugins[i];
			}

		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		return null;
	}

	protected void markPageModified() {
		DescriptorEditor editor = (DescriptorEditor) getPage().getEditor();
		editor.pageModified();
	}

	@Override
	public void commit(boolean onSave) {
		super.commit(onSave);
	}

}
