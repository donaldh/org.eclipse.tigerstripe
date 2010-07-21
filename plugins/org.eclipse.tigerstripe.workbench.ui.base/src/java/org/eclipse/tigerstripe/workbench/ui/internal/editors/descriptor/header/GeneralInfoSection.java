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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.header;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.ChangeModelIDDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.TigerstripeDescriptorSectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class GeneralInfoSection extends TigerstripeDescriptorSectionPart {

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class GeneralInfoPageListener implements ModifyListener,
			KeyListener {

		public void keyPressed(KeyEvent e) {
			// empty
		}

		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void keyReleased(KeyEvent e) {
			if (e.character == '\r') {
				commit(false);
			}
		}

	}

	private boolean silentUpdate;

	private Text nameText;

	private Label nameLabel;

	private Text versionText;

	private Label versionLabel;

	private Text descriptionText;

	private Label descriptionLabel;

	private Label modelIdLabel;

	private Text modelIdText;

	public GeneralInfoSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR);
		setTitle("General Information");
		createContent();
	}

	@Override
	protected void createContent() {
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		getSection().setLayoutData(td);

		createName(getBody(), getToolkit());
		createId(getBody(), getToolkit());
		createVersion(getBody(), getToolkit());
		createDescription(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createName(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		nameLabel = toolkit.createLabel(parent, "Name: ", SWT.WRAP);
		nameText = toolkit.createText(parent, "");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		nameText.setLayoutData(td);
		nameText.setEnabled(false);
		nameLabel.setEnabled(!this.isReadonly());
		nameText.addModifyListener(new GeneralInfoPageListener());
	}

	private void createId(Composite parent, FormToolkit toolkit) {
		modelIdLabel = toolkit.createLabel(parent, "Id: ", SWT.WRAP);
		modelIdLabel.setEnabled(!this.isReadonly());

		boolean changeAbility = !this.isReadonly();
		Composite panel = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 5;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = changeAbility ? 2 : 1;
		panel.setLayout(layout);
		panel.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		modelIdText = toolkit.createText(panel, "");
		modelIdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		modelIdText.setEnabled(false);

		if (changeAbility) {
			final Button button = toolkit.createButton(panel, "Change...",
					SWT.PUSH);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					Shell shell = button.getShell();
					if (getPage().getEditor().isDirty()) {
						MessageBox box = new MessageBox(shell,
								SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
						box.setText("Save Modified Project");
						box
								.setMessage("tigerstripe.xml must be saved before this operation");
						if (box.open() != SWT.OK) {
							return;
						} else {
							getPage().getEditor().doSave(
									new NullProgressMonitor());
						}
					}
					ChangeModelIDDialog dialog = new ChangeModelIDDialog(shell,
							getTSProject());
					dialog.open();
					refresh();
				}
			});
		}
	}

	private void createVersion(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		versionLabel = toolkit.createLabel(parent, "Version: ", SWT.WRAP);
		versionText = toolkit.createText(parent, "");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		versionText.setLayoutData(td);
		versionText.addModifyListener(new GeneralInfoPageListener());
		versionText.setEnabled(!this.isReadonly());
		versionLabel.setEnabled(!this.isReadonly());
	}

	private void createDescription(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		descriptionLabel = toolkit.createLabel(parent, "Description: ",
				SWT.WRAP);
		descriptionText = toolkit.createText(parent, "", SWT.WRAP | SWT.MULTI
				| SWT.V_SCROLL);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.grabVertical = true;
		td.heightHint = 70;
		descriptionText.setLayoutData(td);
		descriptionText.addModifyListener(new GeneralInfoPageListener());
		descriptionText.setEnabled(!this.isReadonly());
		descriptionLabel.setEnabled(!this.isReadonly());
	}

	/**
	 * Set the silent update flag
	 * 
	 * @param silentUpdate
	 */
	private void setSilentUpdate(boolean silentUpdate) {
		this.silentUpdate = silentUpdate;
	}

	/**
	 * If silent Update is set, the form should not consider the updates to
	 * fields.
	 * 
	 * @return
	 */
	private boolean isSilentUpdate() {
		return this.silentUpdate;
	}

	@Override
	public void commit(boolean onSave) {
		super.commit(onSave);
	}

	protected void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.
			ITigerstripeModelProject handle = getTSProject();

			try {
				if (e.getSource() == versionText) {
					IProjectDetails details = handle.getProjectDetails();
					details.setVersion(versionText.getText().trim());
					handle.setProjectDetails(details);
				} else if (e.getSource() == descriptionText) {
					IProjectDetails details = handle.getProjectDetails();
					details.setDescription(descriptionText.getText().trim());
					handle.setProjectDetails(details);
				}
			} catch (TigerstripeException ee) {
				Status status = new Status(
						IStatus.ERROR,
						EclipsePlugin.getPluginId(),
						222,
						"Error refreshing form OssjDefaultForm on Tigerstripe descriptor",
						ee);
				EclipsePlugin.log(status);
			}
			markPageModified();
		}
	}

	protected void markPageModified() {
		DescriptorEditor editor = (DescriptorEditor) getPage().getEditor();
		editor.pageModified();
	}

	@Override
	public void refresh() {
		updateForm();
	}

	protected void updateForm() {
		ITigerstripeModelProject handle = getTSProject();

		try {
			setSilentUpdate(true);
			if ( handle != null){
				nameText.setText(handle.getName());
				modelIdText.setText(handle.getProjectDetails().getModelId());
				versionText.setText(handle.getProjectDetails().getVersion());
				descriptionText
				.setText(handle.getProjectDetails().getDescription());
			}
			setSilentUpdate(false);
		} catch (TigerstripeException e) {
			Status status = new Status(
					IStatus.ERROR,
					EclipsePlugin.getPluginId(),
					222,
					"Error refreshing form OssjDefaultForm on Tigerstripe descriptor",
					e);
			EclipsePlugin.log(status);
		}
	}
}
