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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.plugins;

import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.builtin.IOssjJVTProfilePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.JvtPluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfigFactory;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.UnknownPluginException;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.TigerstripeDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.preferences.TopLevelPreferencePage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class JVTProfileSection extends TigerstripeDescriptorSectionPart {

	private Label activeVersionLabel;

	private CCombo activeVersionCombo;

	private Button generate;

	private Button applyDefaultButton;

	private Text defaultInterfacePackageText;

	private Label defaultInterfacePackageLabel;

	private boolean silentUpdate;

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class DefaultPageListener implements ModifyListener, KeyListener,
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
			if (e.character == '\r') {
				commit(false);
			}
		}

	}

	public JVTProfileSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR
				| ExpandableComposite.TWISTIE);
		setTitle("J2EE Integration Profile");
		createContent();
	}

	@Override
	protected void createContent() {
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		getSection().setLayoutData(td);

		try {
			createID(getBody(), getToolkit());
		} catch (TigerstripeException ee) {
			Status status = new Status(IStatus.WARNING,
					TigerstripePluginConstants.PLUGIN_ID, 111,
					"Unexpected Exception", ee);
			EclipsePlugin.log(status);
		}

		createPreferenceMsg(getBody(), getToolkit());

		updateForm();
		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	/**
	 * Get the properties for the XML Plugin. Returns an empty set of properties
	 * if the plugin is not referenced in the Tigerstripe.xml descriptor.
	 * 
	 */
	private Properties getXMLPluginProperties() {
		IPluginConfig ref = getJvtPluginConfig();
		if (ref != null)
			return ((PluginConfig) ref).getProperties();

		return null;
	}

	/**
	 * Returns the XML plugin ref from the descriptor if it exists, null
	 * otherwise.
	 * 
	 * @return
	 */
	private IPluginConfig getJvtPluginConfig() {
		try {
			ITigerstripeProject handle = getTSProject();
			IPluginConfig[] plugins = handle.getPluginConfigs();

			for (int i = 0; i < plugins.length; i++) {
				if (JvtPluginConfig.MODEL.getPluginId().equals(
						plugins[i].getPluginId()))
					return plugins[i];
			}

		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		return null;
	}

	private TigerstripeProject getTigerstripeProject() {
		try {
			TigerstripeProjectHandle handle = (TigerstripeProjectHandle) getTSProject();
			return handle.getTSProject();
		} catch (TigerstripeException e) {
			return null;
		}
	}

	private void addXMLPluginToDescriptor(IPluginConfig ref) {
		try {
			TigerstripeProjectHandle handle = (TigerstripeProjectHandle) getTSProject();
			handle.addPluginConfig(ref);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	/**
	 * If an XML plugin ref exists in the descriptor, return it. If not create
	 * one with default values.
	 * 
	 * @return
	 */
	private IPluginConfig createDefaultJvtPluginConfig() {
		try {
			PluginConfig ref = PluginConfigFactory.getInstance().createPluginConfig(
					JvtPluginConfig.MODEL, getTigerstripeProject());
			applyDefault(ref);
			return ref;
		} catch (UnknownPluginException e) {
			EclipsePlugin.log(e);
		}
		return null;
	}

	/**
	 * Applies the default values
	 * 
	 * @param ref
	 */
	private void applyDefault(IPluginConfig ref) {
		((PluginConfig) ref).getProperties().setProperty("defaultInterfacePackage",
				"com.mycompany");
		((PluginConfig) ref).getProperties().setProperty("activeVersion",
				IOssjJVTProfilePlugin.defaultVersion);
	}

	private void createID(Composite parent, FormToolkit toolkit)
			throws TigerstripeException {
		GridData td = null;

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);

		DefaultPageListener listener = new DefaultPageListener();

		generate = toolkit.createButton(parent, "Enable", SWT.CHECK);
		generate.addSelectionListener(listener);
		td = new GridData(GridData.FILL);
		generate.setLayoutData(td);
		generate.setEnabled(!this.isReadonly());

		applyDefaultButton = toolkit.createButton(parent, "Default", SWT.PUSH);
		applyDefaultButton.addSelectionListener(listener);
		td = new GridData(GridData.FILL);
		applyDefaultButton.setLayoutData(td);
		applyDefaultButton.setEnabled(!this.isReadonly());

		activeVersionLabel = toolkit.createLabel(parent, "Plugin Version");

		activeVersionCombo = new CCombo(parent, SWT.SINGLE | SWT.READ_ONLY
				| SWT.FLAT);
		toolkit.adapt(this.activeVersionCombo, true, true);

		for (int i = 0; i < IOssjJVTProfilePlugin.supportedVersions.length; i++) {
			activeVersionCombo.add(IOssjJVTProfilePlugin.supportedVersions[i]);
		}
		activeVersionCombo.addSelectionListener(listener);
		activeVersionCombo.setEnabled(!this.isReadonly());

		defaultInterfacePackageLabel = toolkit.createLabel(parent,
				"Default Interface Package: ", SWT.NULL);
		defaultInterfacePackageText = toolkit.createText(parent, "");
		td = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		defaultInterfacePackageText.setLayoutData(td);
		defaultInterfacePackageText.addModifyListener(listener);
		defaultInterfacePackageText.setEnabled(!this.isReadonly());
		toolkit.paintBordersFor(parent);

	}

	private void createPreferenceMsg(Composite parent, FormToolkit toolkit) {
		GridData td = null;

		String data = "<form><p></p><p>To set default values to be reused across multiple Tigerstripe Projects, please use the Tigerstripe <a href=\"http://www.tigerstripedev.net/	\">preferences</a> page.</p></form>";
		FormText rtext = toolkit.createFormText(parent, true);
		td = new GridData(GridData.FILL);
		td.horizontalSpan = 2;
		rtext.setLayoutData(td);
		rtext.setText(data, true, false);
		rtext.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				PreferenceDialog dialog = new PreferenceDialog(getBody()
						.getShell(), EclipsePlugin.getDefault().getWorkbench()
						.getPreferenceManager());
				dialog.setSelectedNode(TopLevelPreferencePage.PAGE_ID);
				dialog.open();
			}
		});
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
			Properties pluginProperties = getXMLPluginProperties();
			if (pluginProperties != null) {
				pluginProperties.setProperty("defaultInterfacePackage",
						defaultInterfacePackageText.getText().trim());
				markPageModified();
			}
		}
	}

	protected void handleWidgetSelected(SelectionEvent e) {
		if (!isSilentUpdate()) {
			if (e.getSource() == generate) {
				if (getJvtPluginConfig() == null) {
					addXMLPluginToDescriptor(createDefaultJvtPluginConfig());
				}
				getJvtPluginConfig().setEnabled(generate.getSelection());
				markPageModified();
			} else if (e.getSource() == activeVersionCombo) {
				getXMLPluginProperties()
						.setProperty(
								"activeVersion",
								IOssjJVTProfilePlugin.supportedVersions[activeVersionCombo
										.getSelectionIndex()]);
				markPageModified();
			} else if (e.getSource() == applyDefaultButton) {
				MessageBox dialog = new MessageBox(getSection().getShell(),
						SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				dialog.setText("Apply Default Values");
				dialog
						.setMessage("Do you really want to apply default values?\nAll current values will be lost.");
				if (dialog.open() == SWT.YES) {
					applyDefault(getJvtPluginConfig());
					markPageModified();
				}
			}
		}
		updateForm();
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
		setSilentUpdate(true);

		if (getJvtPluginConfig() == null
				|| !getJvtPluginConfig().isEnabled()) {
			generate.setSelection(false);
		} else {
			Properties pluginProperties = getXMLPluginProperties();
			generate.setSelection(true);
			defaultInterfacePackageText.setText(pluginProperties.getProperty(
					"defaultInterfacePackage", ""));

			int versionIndex = -1;
			String activeVersion = pluginProperties.getProperty(
					"activeVersion", IOssjJVTProfilePlugin.defaultVersion);
			for (int i = 0; i < IOssjJVTProfilePlugin.supportedVersions.length; i++) {
				if (activeVersion
						.equals(IOssjJVTProfilePlugin.supportedVersions[i])) {
					versionIndex = i;
				}
			}

			activeVersionCombo.select(versionIndex);
		}

		boolean isEnabled = generate.getSelection();
		applyDefaultButton.setEnabled(isEnabled && !this.isReadonly());
		defaultInterfacePackageText.setEnabled(isEnabled && !this.isReadonly());
		defaultInterfacePackageLabel
				.setEnabled(isEnabled && !this.isReadonly());
		activeVersionCombo.setEnabled(isEnabled && !this.isReadonly());
		activeVersionLabel.setEnabled(isEnabled && !this.isReadonly());
		String enabledString = (isEnabled ? "enabled" : "disabled");
		setTitle("J2EE Integration Profile (" + enabledString + ")");
		setSilentUpdate(false);
	}
}
