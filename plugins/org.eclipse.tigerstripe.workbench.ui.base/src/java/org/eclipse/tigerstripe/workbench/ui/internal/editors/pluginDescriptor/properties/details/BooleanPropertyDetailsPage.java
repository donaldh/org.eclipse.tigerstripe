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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.properties.details;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.PluginDescriptorEditor;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class BooleanPropertyDetailsPage extends BasePropertyDetailsPage {

	private CCombo defaultBooleanCombo;

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class BooleanPropertyDetailsPageListener implements
			SelectionListener {
		public void widgetDefaultSelected(SelectionEvent e) {
		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}
	}

	protected void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == defaultBooleanCombo) {
			IPluginProperty property = getIPluggablePluginProperty();
			property
					.setDefaultValue(defaultBooleanCombo.getSelectionIndex() == 0);
			pageModified();
		}
	}

	@Override
	protected void updateForm() {
		super.updateForm();
		if ((Boolean) getIPluggablePluginProperty().getDefaultValue())
			defaultBooleanCombo.select(0);
		else
			defaultBooleanCombo.select(1);
	}

	public void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);
		TableWrapData td = new TableWrapData(TableWrapData.FILL);
		td.heightHint = 200;
		parent.setLayoutData(td);

		Composite sectionClient = createPropertyInfo(parent);

		BooleanPropertyDetailsPageListener adapter = new BooleanPropertyDetailsPageListener();

		Label label = form.getToolkit().createLabel(sectionClient,
				"Default Selection:");

		defaultBooleanCombo = new CCombo(sectionClient, SWT.SINGLE
				| SWT.READ_ONLY | SWT.FLAT);
		defaultBooleanCombo.setEnabled(PluginDescriptorEditor.isEditable());
		form.getToolkit().adapt(this.defaultBooleanCombo, true, true);
		defaultBooleanCombo.setItems(new String[] { "TRUE", "FALSE" });
		defaultBooleanCombo.addSelectionListener(adapter);
		defaultBooleanCombo
				.setToolTipText("This value will be used by default for all projects using this plugin.");

		form.getToolkit().createLabel(sectionClient, "");

		label = form
				.getToolkit()
				.createLabel(
						sectionClient,
						"The value of this Boolean property will be available as a 'checkbox' within the context of the plugin.");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);

		form.getToolkit().paintBordersFor(parent);
	}
}
