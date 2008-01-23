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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.useCase.header;

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
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.useCase.IUseCase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.useCase.UseCaseEditor;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.FileEditorInput;

public class GeneralInfoSection extends TigerstripeSectionPart {

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

	private Text nameText;

	private Text versionText;

	private Text descriptionText;

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

		Label label = toolkit.createLabel(parent, "ID: ", SWT.WRAP);
		FileEditorInput input = (FileEditorInput) getPage().getEditorInput();
		idText = toolkit.createText(parent, input.getFile().getName());
		idText.setEnabled(false);
		idText.setEditable(false);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		idText.setLayoutData(td);
	}

	private void createName(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		Label label = toolkit.createLabel(parent, "Name: ", SWT.WRAP);
		nameText = toolkit.createText(parent, "");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		nameText.setLayoutData(td);
		nameText.addModifyListener(new GeneralInfoPageListener());
		nameText.setEnabled(UseCaseEditor.isEditable());
	}

	private void createVersion(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		Label label = toolkit.createLabel(parent, "Version: ", SWT.WRAP);
		versionText = toolkit.createText(parent, "");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		versionText.setLayoutData(td);
		versionText.addModifyListener(new GeneralInfoPageListener());
		versionText.setEnabled(UseCaseEditor.isEditable());
	}

	private void createDescription(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		Label label = toolkit.createLabel(parent, "Description: ", SWT.WRAP);
		descriptionText = toolkit.createText(parent, "", SWT.WRAP | SWT.MULTI
				| SWT.V_SCROLL);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.grabVertical = true;
		td.heightHint = 70;
		descriptionText.setLayoutData(td);
		if (UseCaseEditor.isEditable())
			descriptionText.addModifyListener(new GeneralInfoPageListener());
		descriptionText.setEnabled(UseCaseEditor.isEditable());
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

			try {
				IUseCase handle = getUseCase();
				if (e.getSource() == nameText) {
					handle.setName(nameText.getText().trim());
				} else if (e.getSource() == versionText) {
					handle.setVersion(versionText.getText().trim());
				} else if (e.getSource() == descriptionText) {
					handle.setDescription(descriptionText.getText().trim());
				}
			} catch (TigerstripeException ee) {
				Status status = new Status(
						IStatus.ERROR,
						TigerstripePluginConstants.PLUGIN_ID,
						222,
						"Error refreshing form GeneralInfoSection on Use Case Editor",
						ee);
				EclipsePlugin.log(status);
			}
			markPageModified();
		}
	}

	protected void markPageModified() {
		UseCaseEditor editor = (UseCaseEditor) getPage().getEditor();
		editor.pageModified();
	}

	@Override
	public void refresh() {
		updateForm();
	}

	private IUseCase getUseCase() throws TigerstripeException {
		return ((UseCaseEditor) getPage().getEditor()).getUseCase();
	}

	protected void updateForm() {

		try {
			IUseCase handle = getUseCase();

			setSilentUpdate(true);

			nameText.setText(handle.getName());
			versionText.setText(handle.getVersion());
			descriptionText.setText(handle.getDescription());

			setSilentUpdate(false);
		} catch (TigerstripeException e) {
			Status status = new Status(
					IStatus.ERROR,
					TigerstripePluginConstants.PLUGIN_ID,
					222,
					"Error refreshing form OssjDefaultForm on Tigerstripe descriptor",
					e);
			EclipsePlugin.log(status);
		}
	}
}
