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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.rules.details;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.GlobalRunnableRule;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.PluginDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.rules.RulesSectionPart;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class GlobalRunnableRuleDetailsPage extends BaseRunnableRuleDetailsPage {

	private Button suppressEmptyFilesButton;

	private Button overwriteFilesButton;

	public GlobalRunnableRuleDetailsPage(RulesSectionPart master) {
		super(master);
	}

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class GlobalRunnableRuleDetailsPageListener implements
			ModifyListener, SelectionListener {
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
				GlobalRunnableRule rule = (GlobalRunnableRule) getIRunnableRule();
				rule.setSuppressEmptyFiles(suppressEmptyFilesButton
						.getSelection());
				pageModified();
			} else if (e.getSource() == overwriteFilesButton) {
				GlobalRunnableRule rule = (GlobalRunnableRule) getIRunnableRule();
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
		parent.setLayoutData(td);

		Composite sectionClient = createRuleInfo(parent);

		Label label = form.getToolkit().createLabel(sectionClient,
				"This rule will be run once per generation with this plugin.");
		TableWrapData twData = new TableWrapData(TableWrapData.FILL_GRAB);
		twData.colspan = 2;
		label.setLayoutData(twData);
		createOptionButtons(sectionClient);

		int height = sectionClient.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		master.setMinimumHeight(height);

		form.getToolkit().paintBordersFor(parent);
	}

	protected void createOptionButtons(Composite parent) {
		// Put an empty label first to "Centre" the control

		form.getToolkit().createLabel(parent, "");
		GlobalRunnableRuleDetailsPageListener adapter = new GlobalRunnableRuleDetailsPageListener();
		suppressEmptyFilesButton = form.getToolkit().createButton(parent,
				"Suppress Empty Files", SWT.CHECK);
		suppressEmptyFilesButton.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		suppressEmptyFilesButton
				.setEnabled(PluginDescriptorEditor.isEditable());
		if (PluginDescriptorEditor.isEditable())
			suppressEmptyFilesButton.addSelectionListener(adapter);

		// Put an empty label first to "Centre" the control
		form.getToolkit().createLabel(parent, "");
		overwriteFilesButton = form.getToolkit().createButton(parent,
				"Overwrite Files", SWT.CHECK);
		overwriteFilesButton.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		overwriteFilesButton.setEnabled(PluginDescriptorEditor.isEditable());
		if (PluginDescriptorEditor.isEditable())
			overwriteFilesButton.addSelectionListener(adapter);

	}

	@Override
	protected void updateForm() {
		super.updateForm();
		setSilentUpdate(true);
		GlobalRunnableRule rule = (GlobalRunnableRule) getIRunnableRule();
		suppressEmptyFilesButton.setSelection(rule.isSuppressEmptyFiles());
		overwriteFilesButton.setSelection(rule.isOverwriteFiles());
		runnableClassText.setText(rule.getRunnableClassName());

		setSilentUpdate(false);
	}
}
