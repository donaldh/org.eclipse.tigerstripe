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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.generator.GeneratorDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.PluginDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.rules.RulesSectionPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class BaseRuleDetailsPage implements IDetailsPage {

	protected IManagedForm form;

	protected RulesSectionPart master;

	private IRule rule;

	private boolean silentUpdate = false;

	private Text nameText;

	private Text descriptionText;

	protected Button enabledButton;

	private class DetailsPageListener implements ModifyListener, KeyListener,
			SelectionListener {

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}

		public void keyPressed(KeyEvent e) {
			// empty
		}

		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void keyReleased(KeyEvent e) {
		}

	}

	protected RulesSectionPart getMaster() {
		return master;
	}

	protected ITigerstripeGeneratorProject getPPProject() {
		return ((GeneratorDescriptorEditor) getMaster().getPage().getEditor())
				.getProjectHandle();
	}

	public BaseRuleDetailsPage(RulesSectionPart master) {
		this.master = master;
	}

	protected void createRuleCommonInfo(Composite sectionClient,
			FormToolkit toolkit) {

		DetailsPageListener adapter = new DetailsPageListener();

		// Put an empty label first to "Centre" the control
		toolkit.createLabel(sectionClient, "");
		enabledButton = toolkit.createButton(sectionClient, "Enabled",
				SWT.CHECK);
		enabledButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL));
		enabledButton.setEnabled(PluginDescriptorEditor.isEditable());
		if (PluginDescriptorEditor.isEditable())
			enabledButton.addSelectionListener(adapter);

		// Pad out the section
		toolkit.createLabel(sectionClient, "");

		Label label = toolkit.createLabel(sectionClient, "Name: ");
		nameText = toolkit.createText(sectionClient, "");
		nameText.setEnabled(PluginDescriptorEditor.isEditable());
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameText.addModifyListener(adapter);
		nameText.setToolTipText("Name of the rule");

		label = toolkit.createLabel(sectionClient, "");
		label.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));

		label = toolkit.createLabel(sectionClient, "Description: ");
		descriptionText = toolkit.createText(sectionClient, "", SWT.WRAP
				| SWT.MULTI | SWT.V_SCROLL);
		descriptionText.setEnabled(PluginDescriptorEditor.isEditable());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 60;
		descriptionText.setLayoutData(gd);
		descriptionText.addModifyListener(adapter);
		descriptionText.setToolTipText("Document this rule.");

		label = toolkit.createLabel(sectionClient, "");
		label.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));
	}

	public void initialize(IManagedForm form) {
		this.form = form;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void commit(boolean onSave) {
	}

	public boolean setFormInput(Object input) {
		return false;
	}

	public void setFocus() {
		nameText.setFocus();
	}

	public boolean isStale() {
		return false;
	}

	public void refresh() {
		updateForm();
	}

	// ============================================================
	private void setIRunRule(IRule rule) {
		this.rule = rule;
	}

	protected IRule getIRunRule() {
		return rule;
	}

	protected void updateForm() {
		setSilentUpdate(true);
		IRule rule = getIRunRule();
		nameText.setText(rule.getName());
		descriptionText.setText(rule.getDescription());
		enabledButton.setSelection(getIRunRule().isEnabled());
		setSilentUpdate(false);
	}

	/**
	 * If silent Update is set, the form should not consider the updates to
	 * fields.
	 * 
	 * @return
	 */
	protected boolean isSilentUpdate() {
		return this.silentUpdate;
	}

	/**
	 * Set the silent update flag
	 * 
	 * @param silentUpdate
	 */
	protected void setSilentUpdate(boolean silentUpdate) {
		this.silentUpdate = silentUpdate;
	}

	protected void pageModified() {
		master.markPageModified();
	}

	protected Shell getShell() {
		return master.getSection().getShell();
	}

	public boolean isDirty() {
		return master.isDirty();
	}

	protected void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == enabledButton) {
			this.rule.setEnabled(enabledButton.getSelection());
			pageModified();
		}
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		if (part instanceof RulesSectionPart) {
			master = (RulesSectionPart) part;
			Table fieldsTable = master.getViewer().getTable();

			IRule selected = (IRule) fieldsTable.getSelection()[0]
					.getData();
			setIRunRule(selected);
			updateForm();
		}
	}

	public void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			IRule rule = getIRunRule();
			if (e.getSource() == nameText) {
				rule.setName(nameText.getText().trim());
				if (master != null) {
					pageModified();
					TableViewer viewer = master.getViewer();
					viewer.refresh(getIRunRule());
				}
			} else if (e.getSource() == descriptionText) {
				getIRunRule().setDescription(descriptionText.getText().trim());
				pageModified();
			}
		}
	}
}
