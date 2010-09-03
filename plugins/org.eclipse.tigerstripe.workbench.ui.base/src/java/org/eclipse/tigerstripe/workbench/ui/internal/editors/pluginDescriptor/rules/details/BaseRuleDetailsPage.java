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

import static org.eclipse.jface.layout.GridDataFactory.createFrom;

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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.ui.components.md.IDetails;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.generator.GeneratorDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.rules.RulesSectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public abstract class BaseRuleDetailsPage implements IDetails {

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

	protected final RulesSectionPart master;
	private final FormToolkit formToolkit;
	private final Composite parent;

	public BaseRuleDetailsPage(RulesSectionPart master,
			FormToolkit formToolkit, Composite parent) {
		this.master = master;
		this.formToolkit = formToolkit;
		this.parent = parent;
	}

	protected void createRuleCommonInfo(Composite sectionClient,
			FormToolkit toolkit) {

		DetailsPageListener adapter = new DetailsPageListener();

		// Put an empty label first to "Centre" the control
		toolkit.createLabel(sectionClient, "");
		enabledButton = toolkit.createButton(sectionClient, "Enabled",
				SWT.CHECK);
		enabledButton.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		enabledButton.setEnabled(GeneratorDescriptorEditor.isEditable());
		if (GeneratorDescriptorEditor.isEditable())
			enabledButton.addSelectionListener(adapter);

		toolkit.createLabel(sectionClient, "Name: ");
		nameText = toolkit.createText(sectionClient, "");
		nameText.setEnabled(GeneratorDescriptorEditor.isEditable());
		nameText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		nameText.addModifyListener(adapter);
		nameText.setToolTipText("Name of the rule");

		toolkit.createLabel(sectionClient, "Description: ");

		descriptionText = toolkit.createText(sectionClient, "", SWT.WRAP
				| SWT.MULTI | SWT.V_SCROLL);
		descriptionText.setEnabled(GeneratorDescriptorEditor.isEditable());
		TableWrapData wrapData = new TableWrapData(TableWrapData.FILL_GRAB);
		wrapData.heightHint = 60;
		descriptionText.setLayoutData(wrapData);
		descriptionText.addModifyListener(adapter);
		descriptionText.setToolTipText("Document this rule.");
	}

	protected FormToolkit getToolkit() {
		return formToolkit;
	}

	public boolean setFormInput(Object input) {
		return false;
	}

	public void setFocus() {
		nameText.setFocus();
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

	protected void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == enabledButton) {
			this.rule.setEnabled(enabledButton.getSelection());
			pageModified();
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

	private Composite content;

	protected abstract void createContents(Composite parent);

	public void init() {
		content = getToolkit().createComposite(parent);
		GridData gd = new GridData(GridData.FILL_BOTH);
		content.setLayoutData(gd);
		createContents(content);
	}

	public void show() {
		setVisibleForContent(true);
		createFrom((GridData) content.getLayoutData()).exclude(false).applyTo(
				content);
		parent.layout();
		master.getManagedForm().reflow(true);
	}

	public void hide() {
		setVisibleForContent(false);
		createFrom((GridData) content.getLayoutData()).exclude(true).applyTo(
				content);
	}

	public void switchTarget(Object target) {
		setIRunRule((IRule) target);
		updateForm();
	}

	private void setVisibleForContent(boolean value) {
		if (content == null) {
			throw new IllegalStateException("Page not initialiazed");
		}
		content.setVisible(value);
	}
}
