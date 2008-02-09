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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.plugins.IBooleanPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.IPluggablePluginPropertyListener;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class BooleanPropertyRenderer extends BasePropertyRenderer {

	private Button propButton;

	private class BooleanListener implements SelectionListener {
		public void widgetDefaultSelected(SelectionEvent e) {
		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}
	}

	public BooleanPropertyRenderer(Composite parent, FormToolkit toolkit,
			ITigerstripeModelProject project, IPluginProperty property,
			IPluggablePluginPropertyListener persister) {
		super(parent, toolkit, project, property, persister);
	}

	@Override
	public void setEnabled(boolean enabled) {
		propButton.setEnabled(enabled);
	}

	@Override
	public void applyDefault() {
		try {
			IBooleanPluginProperty sProp = (IBooleanPluginProperty) getProperty();
			propButton.setSelection(sProp.getDefaultBoolean());
			getPersister().storeProperty(sProp,
					Boolean.valueOf(propButton.getSelection()));
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	protected void handleWidgetSelected(SelectionEvent e) {
		if (!isSilentUpdate()) {
			if (e.getSource() == propButton) {
				try {
					IBooleanPluginProperty sProp = (IBooleanPluginProperty) getProperty();
					getPersister().storeProperty(sProp,
							Boolean.valueOf(propButton.getSelection()));
				} catch (TigerstripeException ee) {
					EclipsePlugin.log(ee);
				}
			}
		}
	}

	@Override
	public void render() throws TigerstripeException {
		IBooleanPluginProperty sProp = (IBooleanPluginProperty) getProperty();

		propButton = getToolkit().createButton(getParent(), sProp.getName(),
				SWT.CHECK);
		propButton.setText(sProp.getName());
		propButton.setToolTipText(sProp.getTipToolText());
		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		propButton.setLayoutData(gd);
		propButton.addSelectionListener(new BooleanListener());
	}

	@Override
	public void update(String serializedValue) {
		setSilentUpdate(true);
		boolean bool = Boolean.valueOf(serializedValue);
		propButton.setSelection(bool);
		setSilentUpdate(false);
	}
}
