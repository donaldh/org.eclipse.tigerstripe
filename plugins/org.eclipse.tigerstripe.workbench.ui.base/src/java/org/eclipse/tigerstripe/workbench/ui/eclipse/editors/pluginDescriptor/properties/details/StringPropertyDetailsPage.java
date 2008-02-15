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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.properties.details;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.PluginDescriptorEditor;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class StringPropertyDetailsPage extends BasePropertyDetailsPage {

	private Text defaultString;

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class StringPropertyDetailsPageListener implements ModifyListener {
		public void modifyText(ModifyEvent e) {
			handleStringPropertyModifyText(e);
		}
	}

	protected void handleStringPropertyModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			if (e.getSource() == defaultString) {
				IPluginProperty property = getIPluggablePluginProperty();
				property.setDefaultValue(defaultString.getText().trim());
				pageModified();
			}
		}
	}

	@Override
	protected void updateForm() {
		super.updateForm();
		setSilentUpdate(true);
		defaultString.setText((String) getIPluggablePluginProperty()
				.getDefaultValue());
		setSilentUpdate(false);

	}

	public void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);
		TableWrapData td = new TableWrapData(TableWrapData.FILL);
		td.heightHint = 200;
		parent.setLayoutData(td);

		Composite sectionClient = createPropertyInfo(parent);

		StringPropertyDetailsPageListener adapter = new StringPropertyDetailsPageListener();

		Label label = form.getToolkit().createLabel(sectionClient,
				"Default Value:");

		defaultString = form.getToolkit().createText(sectionClient, "");
		defaultString.setEnabled(PluginDescriptorEditor.isEditable());
		defaultString.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		defaultString.addModifyListener(adapter);
		defaultString
				.setToolTipText("This value will be used by default for all projects using this plugin.");

		form.getToolkit().createLabel(sectionClient, "");

		label = form
				.getToolkit()
				.createLabel(
						sectionClient,
						"The value of this String property will be available within the context of the plugin.");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);

		form.getToolkit().paintBordersFor(parent);
	}
}
