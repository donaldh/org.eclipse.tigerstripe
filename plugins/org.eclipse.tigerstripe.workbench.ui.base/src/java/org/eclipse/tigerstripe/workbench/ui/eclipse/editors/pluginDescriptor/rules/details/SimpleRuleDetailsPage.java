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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.rules.details;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.core.project.pluggable.rules.SimplePPluginRule;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.PluginDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.rules.GlobalRulesSection;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class SimpleRuleDetailsPage extends BaseTemplateRuleDetailsPage {

	private Button suppressEmptyFilesButton;

	private Button overwriteFilesButton;

	public SimpleRuleDetailsPage(GlobalRulesSection master) {
		super(master);
	}

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class SimpleRuleDetailsPageListener implements ModifyListener,
			SelectionListener {
		public void modifyText(ModifyEvent e) {

		}

		public void widgetDefaultSelected(SelectionEvent e) {
		}

		public void widgetSelected(SelectionEvent e) {
			handleSimpleWidgetSelected(e);
		}
	}

	protected void handleSimpleWidgetSelected(SelectionEvent e) {
		if (!isSilentUpdate()) {
			if (e.getSource() == suppressEmptyFilesButton) {
				SimplePPluginRule rule = (SimplePPluginRule) getITemplateRunRule();
				rule.setSuppressEmptyFiles(suppressEmptyFilesButton
						.getSelection());
				pageModified();
			} else if (e.getSource() == overwriteFilesButton) {
				SimplePPluginRule rule = (SimplePPluginRule) getITemplateRunRule();
				rule.setOverwriteFiles(overwriteFilesButton.getSelection());
				pageModified();
			}
		}
	}

	@Override
	public void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);
		TableWrapData td = new TableWrapData(TableWrapData.FILL);
		td.heightHint = 200;
		parent.setLayoutData(td);

		Composite sectionClient = createRuleInfo(parent);

		Label label = form
				.getToolkit()
				.createLabel(sectionClient,
						"This template will be run once per generation with this plugin.");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);
		createOptionButtons(sectionClient);
		createContextDefinitions(parent);
		createMacros(parent);
		form.getToolkit().paintBordersFor(parent);
	}

	protected void createOptionButtons(Composite parent) {
		// Put an empty label first to "Centre" the control

		form.getToolkit().createLabel(parent, "");
		SimpleRuleDetailsPageListener adapter = new SimpleRuleDetailsPageListener();
		suppressEmptyFilesButton = form.getToolkit().createButton(parent,
				"Suppress Empty Files", SWT.CHECK);
		suppressEmptyFilesButton.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
		suppressEmptyFilesButton
				.setEnabled(PluginDescriptorEditor.isEditable());
		if (PluginDescriptorEditor.isEditable())
			suppressEmptyFilesButton.addSelectionListener(adapter);

		// Pad out the section
		form.getToolkit().createLabel(parent, "");

		// Put an empty label first to "Centre" the control
		form.getToolkit().createLabel(parent, "");
		overwriteFilesButton = form.getToolkit().createButton(parent,
				"Overwrite Files", SWT.CHECK);
		overwriteFilesButton.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
		overwriteFilesButton.setEnabled(PluginDescriptorEditor.isEditable());
		if (PluginDescriptorEditor.isEditable())
			overwriteFilesButton.addSelectionListener(adapter);

		// Pad out the section
		form.getToolkit().createLabel(parent, "");

	}

	@Override
	protected void updateForm() {
		super.updateForm();
		setSilentUpdate(true);
		SimplePPluginRule rule = (SimplePPluginRule) getITemplateRunRule();
		suppressEmptyFilesButton.setSelection(rule.isSuppressEmptyFiles());
		overwriteFilesButton.setSelection(rule.isOverwriteFiles());
		setSilentUpdate(false);
	}
}
