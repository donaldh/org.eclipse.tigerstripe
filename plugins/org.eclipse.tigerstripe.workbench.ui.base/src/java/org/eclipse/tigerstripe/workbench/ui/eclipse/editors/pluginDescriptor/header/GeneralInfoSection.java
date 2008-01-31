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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.header;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.pluggable.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.pluggable.IPluggablePluginProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.PluginDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.PluginDescriptorSectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.FileEditorInput;

public class GeneralInfoSection extends PluginDescriptorSectionPart {

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

	private String[] supportedNatures;
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

		createID(getBody(), getToolkit());
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

		Label label = toolkit.createLabel(parent, "ID: ", SWT.WRAP);
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

		Label label = toolkit.createLabel(parent, "Name: ", SWT.WRAP);
		nameText = toolkit.createText(parent, "");
		nameText
				.setToolTipText("The name of the plugin as presented to the user once deployed.");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		nameText.setLayoutData(td);
		nameText.addModifyListener(new GeneralInfoPageListener());
		nameText.setEnabled(PluginDescriptorEditor.isEditable());
	}

	private void createVersion(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		Label label = toolkit.createLabel(parent, "Version: ", SWT.WRAP);
		versionText = toolkit.createText(parent, "");
		versionText.setToolTipText("The version of this plugin");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		versionText.setLayoutData(td);
		versionText.addModifyListener(new GeneralInfoPageListener());
		versionText.setEnabled(PluginDescriptorEditor.isEditable());
	}

	private void createDescription(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		Label label = toolkit.createLabel(parent, "Description: ", SWT.WRAP);
		descriptionText = toolkit.createText(parent, "", SWT.WRAP | SWT.MULTI
				| SWT.V_SCROLL);
		descriptionText
				.setToolTipText("The description of this plugin, as presented to the user once deployed.");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.grabVertical = true;
		td.heightHint = 70;
		descriptionText.setLayoutData(td);
		if (PluginDescriptorEditor.isEditable())
			descriptionText.addModifyListener(new GeneralInfoPageListener());
		descriptionText.setEnabled(PluginDescriptorEditor.isEditable());
	}

	private void createProvider(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		Label label = toolkit.createLabel(parent, "Provider: ", SWT.WRAP);
		providerText = toolkit.createText(parent, "");
		providerText
				.setToolTipText("The name of the provider or owner of this plugin.");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		providerText.setLayoutData(td);
		providerText.addModifyListener(new GeneralInfoPageListener());
		providerText.setEnabled(PluginDescriptorEditor.isEditable());
	}

	private void createNature(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		Label label = toolkit.createLabel(parent, "Nature: ", SWT.WRAP);

		supportedNatures = new String[EPluggablePluginNature.values().length];
		int i = 0;
		for (EPluggablePluginNature nature : EPluggablePluginNature.values()) {
			supportedNatures[i++] = nature.name();
		}
		pluginNature = new CCombo(parent, SWT.READ_ONLY | SWT.BORDER);
		pluginNature.setEnabled(PluginDescriptorEditor.isEditable());
		toolkit.adapt(pluginNature, true, true);
		pluginNature.setItems(supportedNatures);
		pluginNature
				.setToolTipText("Choose a 'generic' plugin for simple generation,\n or 'validation' to allow this plugin to cancel generation if validation fails");
		if (PluginDescriptorEditor.isEditable()) {
			pluginNature.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					if (!isSilentUpdate()) {
						int natIdx = pluginNature.getSelectionIndex();
						try {
							getIPluggablePluginProject().setPluginNature(
									EPluggablePluginNature
											.valueOf(supportedNatures[natIdx]));
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

			IPluggablePluginProject handle = getIPluggablePluginProject();

			try {
				if (e.getSource() == nameText) {
					handle.getProjectDetails().setName(
							nameText.getText().trim());
				} else if (e.getSource() == versionText) {
					handle.getProjectDetails().setVersion(
							versionText.getText().trim());
				} else if (e.getSource() == descriptionText) {
					handle.getProjectDetails().setDescription(
							descriptionText.getText().trim());
				} else if (e.getSource() == providerText) {
					handle.getProjectDetails().setProvider(
							providerText.getText().trim());
				}
			} catch (TigerstripeException ee) {
				Status status = new Status(
						IStatus.ERROR,
						TigerstripePluginConstants.PLUGIN_ID,
						222,
						"Error refreshing form OssjDefaultForm on Tigerstripe descriptor",
						ee);
				EclipsePlugin.log(status);
			}
			markPageModified();
		}
	}

	protected void markPageModified() {
		PluginDescriptorEditor editor = (PluginDescriptorEditor) getPage()
				.getEditor();
		editor.pageModified();
	}

	@Override
	public void refresh() {
		updateForm();
	}

	protected void updateForm() {
		IPluggablePluginProject handle = getIPluggablePluginProject();

		try {
			setSilentUpdate(true);

			nameText.setText(handle.getProjectDetails().getName());
			versionText.setText(handle.getProjectDetails().getVersion());
			descriptionText
					.setText(handle.getProjectDetails().getDescription());
			providerText.setText(handle.getProjectDetails().getProvider());

			int idx = 0;
			for (String s : supportedNatures) {
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
					TigerstripePluginConstants.PLUGIN_ID,
					222,
					"Error refreshing form OssjDefaultForm on Tigerstripe descriptor",
					e);
			EclipsePlugin.log(status);
		}
	}
}
