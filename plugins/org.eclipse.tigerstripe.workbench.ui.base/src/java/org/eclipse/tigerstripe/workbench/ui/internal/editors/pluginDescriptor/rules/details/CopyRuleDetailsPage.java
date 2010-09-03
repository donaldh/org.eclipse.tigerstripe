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
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.CopyRule;
import org.eclipse.tigerstripe.workbench.plugins.ICopyRule;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.rules.GlobalRulesSection;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class CopyRuleDetailsPage extends BaseRuleDetailsPage {

	private Button fromPluginButton;

	private Button fromProjectButton;

	private Text filematchText;

	private Text toDirectoryText;

	public CopyRuleDetailsPage(GlobalRulesSection master,
			FormToolkit formToolkit, Composite parent) {
		super(master, formToolkit, parent);
	}

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class SimpleRuleDetailsPageListener implements ModifyListener,
			SelectionListener {
		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void widgetDefaultSelected(SelectionEvent e) {
		}

		public void widgetSelected(SelectionEvent e) {
			handleSimpleWidgetSelected(e);
		}
	}

	protected void handleSimpleWidgetSelected(SelectionEvent e) {
		if (!isSilentUpdate()) {
			if (e.getSource() == fromProjectButton) {
				CopyRule rule = (CopyRule) getIRunRule();
				rule.setCopyFrom(ICopyRule.FROM_PROJECT);
				pageModified();
			} else if (e.getSource() == fromPluginButton) {
				CopyRule rule = (CopyRule) getIRunRule();
				rule.setCopyFrom(ICopyRule.FROM_PLUGIN);
				pageModified();
			}
		}
	}

	@Override
	protected void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);

		createRuleInfo(parent);

		getToolkit().paintBordersFor(parent);
	}

	protected Composite createRuleInfo(Composite parent) {

		FormToolkit toolkit = getToolkit();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section);

		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		layout.bottomMargin = layout.topMargin = 0;
		sectionClient.setLayout(layout);
		sectionClient.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		// Add the common details for a rule
		createRuleCommonInfo(sectionClient, toolkit);
		createCopyRuleSpecifics(sectionClient, toolkit);

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);

		return sectionClient;
	}

	protected void createCopyRuleSpecifics(Composite sectionClient,
			FormToolkit toolkit) {

		SimpleRuleDetailsPageListener adapter = new SimpleRuleDetailsPageListener();

		toolkit.createLabel(sectionClient, "Source");
		filematchText = toolkit.createText(sectionClient, "", SWT.BORDER);
		filematchText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		filematchText.addModifyListener(adapter);

		toolkit.createLabel(sectionClient, "To Directory");
		toDirectoryText = toolkit.createText(sectionClient, "", SWT.BORDER);
		toDirectoryText
				.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		toDirectoryText.addModifyListener(adapter);

		toolkit.createLabel(sectionClient, "");
		Composite fromComposite = toolkit.createComposite(sectionClient);
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 2;
		fromComposite.setLayout(twLayout);

		fromPluginButton = toolkit.createButton(fromComposite, "From Plugin",
				SWT.RADIO);
		fromPluginButton.addSelectionListener(adapter);

		fromProjectButton = toolkit.createButton(fromComposite, "From Project",
				SWT.RADIO);
		fromProjectButton.addSelectionListener(adapter);
	}

	@Override
	protected void updateForm() {
		super.updateForm();
		setSilentUpdate(true);
		ICopyRule rule = (ICopyRule) getIRunRule();
		fromPluginButton
				.setSelection(rule.getCopyFrom() == ICopyRule.FROM_PLUGIN);
		fromProjectButton
				.setSelection(rule.getCopyFrom() == ICopyRule.FROM_PROJECT);
		setSilentUpdate(false);
	}

	@Override
	public void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			if (e.getSource() == filematchText) {
				ICopyRule rule = (ICopyRule) getIRunRule();
				rule.setFilesetMatch(filematchText.getText().trim());
				pageModified();
			} else if (e.getSource() == toDirectoryText) {
				ICopyRule rule = (ICopyRule) getIRunRule();
				rule.setToDirectory(toDirectoryText.getText().trim());
				pageModified();
			} else
				super.handleModifyText(e);
		}
	}

	@Override
	public void switchTarget(Object target) {
		super.switchTarget(target);
		setSilentUpdate(true);
		ICopyRule rule = (ICopyRule) getIRunRule();
		filematchText.setText(rule.getFilesetMatch());
		toDirectoryText.setText(rule.getToDirectory());
		setSilentUpdate(false);
	}
}
