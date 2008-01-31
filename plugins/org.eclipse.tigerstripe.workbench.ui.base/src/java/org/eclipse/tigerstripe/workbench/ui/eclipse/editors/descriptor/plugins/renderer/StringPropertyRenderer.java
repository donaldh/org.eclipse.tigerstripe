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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.plugins.renderer;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.pluggable.IPluggablePluginPropertyListener;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.IStringPluginProperty;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class StringPropertyRenderer extends BasePropertyRenderer {

	private Label label;

	private Text text;

	private class StringListener implements ModifyListener {
		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}
	}

	public StringPropertyRenderer(Composite parent, FormToolkit toolkit,
			ITigerstripeProject project, IPluginProperty property,
			IPluggablePluginPropertyListener persister) {
		super(parent, toolkit, project, property, persister);
	}

	@Override
	public void setEnabled(boolean enabled) {
		label.setEnabled(enabled);
		text.setEnabled(enabled);
	}

	@Override
	public void applyDefault() {
		try {
			IStringPluginProperty sProp = (IStringPluginProperty) getProperty();
			getPersister().storeProperty(sProp, sProp.getDefaultString());
			text.setText(sProp.getDefaultString());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	@Override
	public void render() throws TigerstripeException {
		IStringPluginProperty sProp = (IStringPluginProperty) getProperty();

		label = getToolkit().createLabel(getParent(), sProp.getName());

		text = getToolkit().createText(getParent(), sProp.getDefaultString());
		text.setToolTipText(sProp.getTipToolText());
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL);
		text.setLayoutData(gd);
		text.addModifyListener(new StringListener());

		getToolkit().createLabel(getParent(), "");
	}

	private void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			if (e.getSource() == text) {
				try {
					IStringPluginProperty sProp = (IStringPluginProperty) getProperty();
					getPersister().storeProperty(sProp, text.getText().trim());
				} catch (TigerstripeException ee) {
					EclipsePlugin.log(ee);
				}
			}
		}
	}

	@Override
	public void update(String serializedValue) {
		setSilentUpdate(true);
		IStringPluginProperty sProp = (IStringPluginProperty) getProperty();
		String s = (String) sProp.deSerialize(serializedValue);

		if (s == null)
			s = sProp.getDefaultString();
		text.setText(s);
		setSilentUpdate(false);
	}
}
