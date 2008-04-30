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
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.TigerstripeDescriptorSectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

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

	private Text idText;

	private Label idLabel;

	private Text nameText;

	private Label nameLabel;

	private Text versionText;

	private Label versionLabel;

	private Text descriptionText;

	private Label descriptionLabel;

	public GeneralInfoSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR);
		setTitle("General Information");
		createContent();
	}

	@Override
	protected void createContent() {
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		getSection().setLayout(layout);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		getSection().setLayoutData(td);

		createID(getBody(), getToolkit());
		createName(getBody(), getToolkit());
		createVersion(getBody(), getToolkit());
		createDescription(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createID(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		idLabel = toolkit.createLabel(parent, "ID: ", SWT.WRAP);
		String projectLabel = "";
		projectLabel = getTSProject().getProjectLabel();
		// on a ReadOnly descriptor (ie. from a module, this will be null)
		if (projectLabel == null) {
			projectLabel = "<MODULE>";
		}

		idText = toolkit.createText(parent, projectLabel);
		idText.setEnabled(false);
		idText.setEditable(false);
		idLabel.setEnabled(false);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		idText.setLayoutData(td);
	}

	private void createName(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		nameLabel = toolkit.createLabel(parent, "Name: ", SWT.WRAP);
		nameText = toolkit.createText(parent, "");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		nameText.setLayoutData(td);
		nameText.setEnabled(!this.isReadonly());
		nameLabel.setEnabled(!this.isReadonly());
		nameText.addModifyListener(new GeneralInfoPageListener());
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
				if (e.getSource() == nameText) {
					handle.getProjectDetails().setName(
							nameText.getText().trim());
				} else if (e.getSource() == versionText) {
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

			nameText.setText(handle.getProjectDetails().getName());
			versionText.setText(handle.getProjectDetails().getVersion());
			descriptionText
					.setText(handle.getProjectDetails().getDescription());

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
