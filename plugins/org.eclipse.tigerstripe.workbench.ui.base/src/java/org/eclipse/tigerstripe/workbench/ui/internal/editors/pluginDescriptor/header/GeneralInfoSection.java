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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.header;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.generator.GeneratorDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.generator.GeneratorDescriptorSectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.FileEditorInput;

public class GeneralInfoSection extends GeneratorDescriptorSectionPart {

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

	private Text providerText;

	private CCombo pluginNature;

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

		// createID(getBody(), getToolkit());
		createName(getBody(), getToolkit());
		createVersion(getBody(), getToolkit());
		createDescription(getBody(), getToolkit());
		createProvider(getBody(), getToolkit());
		createNature(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createID(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		toolkit.createLabel(parent, "ID: ", SWT.WRAP);
		FileEditorInput input = (FileEditorInput) getPage().getEditorInput();
		idText = toolkit.createText(parent, input.getFile().getProject()
				.getName());
		idText.setEnabled(false);
		idText.setEditable(false);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		idText.setLayoutData(td);
	}

	private void createName(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		toolkit.createLabel(parent, "Name: ", SWT.WRAP);
		nameText = toolkit.createText(parent, "");
		nameText
				.setToolTipText("The name of the plugin as presented to the user once deployed.");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		nameText.setLayoutData(td);
		nameText.setEnabled(false);
	}

	private void createVersion(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		toolkit.createLabel(parent, "Version: ", SWT.WRAP);
		versionText = toolkit.createText(parent, "");
		versionText.setToolTipText("The version of this plugin");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		versionText.setLayoutData(td);
		versionText.addModifyListener(new GeneralInfoPageListener());
		versionText.setEnabled(GeneratorDescriptorEditor.isEditable());
	}

	private void createDescription(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		toolkit.createLabel(parent, "Description: ");
		descriptionText = toolkit.createText(parent, "", SWT.WRAP | SWT.MULTI
				| SWT.V_SCROLL);
		descriptionText
				.setToolTipText("The description of this plugin, as presented to the user once deployed.");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.grabVertical = true;
		td.heightHint = 70;
		descriptionText.setLayoutData(td);
		if (GeneratorDescriptorEditor.isEditable())
			descriptionText.addModifyListener(new GeneralInfoPageListener());
		descriptionText.setEnabled(GeneratorDescriptorEditor.isEditable());
	}

	private void createProvider(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		toolkit.createLabel(parent, "Provider: ", SWT.WRAP);
		providerText = toolkit.createText(parent, "");
		providerText
				.setToolTipText("The name of the provider or owner of this plugin.");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		providerText.setLayoutData(td);
		providerText.addModifyListener(new GeneralInfoPageListener());
		providerText.setEnabled(GeneratorDescriptorEditor.isEditable());
	}

	protected String[] getSupportedNature() {
		return new String[] { EPluggablePluginNature.Generic.name(),
				EPluggablePluginNature.Validation.name(),
				EPluggablePluginNature.M0.name() };
	}

	private void createNature(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		toolkit.createLabel(parent, "Nature: ", SWT.WRAP);

		pluginNature = new CCombo(parent, SWT.READ_ONLY | SWT.BORDER);
		pluginNature.setEnabled(GeneratorDescriptorEditor.isEditable());
		toolkit.adapt(pluginNature, true, true);
		pluginNature.setItems(getSupportedNature());
		pluginNature
				.setToolTipText("Choose a 'generic' plugin for simple generation,\n or 'validation' to allow this plugin to cancel generation if validation fails");
		if (GeneratorDescriptorEditor.isEditable()) {
			pluginNature.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					if (!isSilentUpdate()) {
						int natIdx = pluginNature.getSelectionIndex();
						try {
							getIPluggablePluginProject()
									.setPluginNature(
											EPluggablePluginNature
													.valueOf(getSupportedNature()[natIdx]));
							markPageModified();
						} catch (TigerstripeException ee) {
							EclipsePlugin.log(ee);
						}
					}
				}
			});
		}

		td = new TableWrapData(TableWrapData.LEFT);
		pluginNature.setLayoutData(td);
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

	protected void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.

			ITigerstripeGeneratorProject handle = getIPluggablePluginProject();

			try {
				IProjectDetails details = handle.getProjectDetails();
				if (e.getSource() == versionText) {

					// TODO OSGI-ify
					// Validate the input..

					details.setVersion(versionText.getText().trim());
					handle.setProjectDetails(details);
				} else if (e.getSource() == descriptionText) {
					details.setDescription(descriptionText.getText().trim());
					handle.setProjectDetails(details);
				} else if (e.getSource() == providerText) {
					details.setProvider(providerText.getText().trim());
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
		GeneratorDescriptorEditor editor = (GeneratorDescriptorEditor) getPage()
				.getEditor();
		editor.pageModified();
	}

	@Override
	public void refresh() {
		updateForm();
	}

	protected void updateForm() {
		ITigerstripeGeneratorProject handle = getIPluggablePluginProject();

		try {
			setSilentUpdate(true);

			nameText.setText(handle.getName());
			versionText.setText(handle.getProjectDetails().getVersion());
			descriptionText
					.setText(handle.getProjectDetails().getDescription());
			providerText.setText(handle.getProjectDetails().getProvider());

			int idx = 0;
			for (String s : getSupportedNature()) {
				if (s.equals(handle.getPluginNature().name())) {
					break;
				}
				idx++;
			}
			pluginNature.select(idx);
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
