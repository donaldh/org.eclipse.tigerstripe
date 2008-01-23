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
import org.eclipse.tigerstripe.api.IPluginReference;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.api.plugins.builtin.IOssjWSDLProfilePlugin;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.plugin.PluginRef;
import org.eclipse.tigerstripe.core.plugin.PluginRefFactory;
import org.eclipse.tigerstripe.core.plugin.UnknownPluginException;
import org.eclipse.tigerstripe.core.plugin.WsdlExamplePluginRef;
import org.eclipse.tigerstripe.core.plugin.WsdlPluginRef;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
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

public class WSDLProfileSection extends TigerstripeDescriptorSectionPart {

	private Label activeVersionLabel;

	private CCombo activeVersionCombo;

	private Button generate;

	private Button generateExamples;

	private Button applyDefaultButton;

	private Text targetNamespaceText;

	private Label targetNamespaceLabel;

	private Text targetPrefixText;

	private Label targetPrefixLabel;

	private Button includeWSNotificationsButton;

	private Text wsNotificationsLocationText;

	private Label wsNotificationsLocationLabel;

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

	public WSDLProfileSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR
				| ExpandableComposite.TWISTIE);
		setTitle("Web Service Integration Profile");
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
	 * Get the properties for the WSDL Plugin. Returns an empty set of
	 * properties if the plugin is not referenced in the Tigerstripe.xml
	 * descriptor.
	 * 
	 */
	private Properties getWSDLPluginProperties() {
		IPluginReference ref = getWSDLPluginReference();
		if (ref != null)
			return ref.getProperties();

		return null;
	}

	/**
	 * Get the properties for the WSDL Example Plugin. Returns an empty set of
	 * properties if the plugin is not referenced in the Tigerstripe.xml
	 * descriptor.
	 * 
	 */
	private Properties getWSDLExamplePluginProperties() {
		IPluginReference ref = getWSDLExamplePluginReference();
		if (ref != null)
			return ref.getProperties();

		return null;
	}

	/**
	 * Returns the WSDL plugin ref from the descriptor if it exists, null
	 * otherwise.
	 * 
	 * @return
	 */
	private IPluginReference getWSDLPluginReference() {
		try {
			ITigerstripeProject handle = getTSProject();
			IPluginReference[] plugins = handle.getPluginReferences();

			for (int i = 0; i < plugins.length; i++) {
				if ("ossj-wsdl-spec".equals(plugins[i].getPluginId()))
					return plugins[i];
			}

		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		return null;
	}

	/**
	 * Returns the WSDL EXAMPLE plugin ref from the descriptor if it exists,
	 * null otherwise.
	 * 
	 * @return
	 */
	private IPluginReference getWSDLExamplePluginReference() {
		try {
			ITigerstripeProject handle = getTSProject();
			IPluginReference[] plugins = handle.getPluginReferences();

			for (int i = 0; i < plugins.length; i++) {
				if ("ossj-wsdl-example-spec".equals(plugins[i].getPluginId()))
					return plugins[i];
			}

		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		return null;
	}

	private void addPluginToDescriptor(IPluginReference ref) {
		try {
			TigerstripeProjectHandle handle = (TigerstripeProjectHandle) getTSProject();
			handle.addPluginReference(ref);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	/**
	 * If an WSDL plugin ref exists in the descriptor, return it. If not create
	 * one with default values.
	 * 
	 * @return
	 */
	private IPluginReference createDefaultWsdlPluginReference() {
		try {
			PluginRef ref = PluginRefFactory.getInstance().createPluginRef(
					WsdlPluginRef.MODEL, getTigerstripeProject());
			applyDefault(ref);
			return ref;
		} catch (UnknownPluginException e) {
			EclipsePlugin.log(e);
		}
		return null;
	}

	/**
	 * If a WSDL Example plugin ref exists in the descriptor, return it. If not
	 * create one with default values.
	 * 
	 * @return
	 */
	private IPluginReference createDefaultWsdlExamplePluginReference() {
		try {
			PluginRef ref = PluginRefFactory.getInstance().createPluginRef(
					WsdlExamplePluginRef.MODEL, getTigerstripeProject());
			applyExampleDefault(ref);
			return ref;
		} catch (UnknownPluginException e) {
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

	/**
	 * Applies the default values
	 * 
	 * @param ref
	 */
	private void applyDefault(IPluginReference ref) {
		ref.getProperties().setProperty("targetNamespace", "tns");
		ref.getProperties().setProperty("targetPrefix", "tns");
		ref
				.getProperties()
				.setProperty(
						"WSNotificationsLocation",
						"http://www-128.ibm.com/developerworks/library/specification/ws-notification/WS-BaseN.wsdl");
		ref.getProperties().setProperty("includeWSNotifications", "true");
		ref.getProperties().setProperty("activeVersion",
				IOssjWSDLProfilePlugin.defaultVersion);
	}

	/**
	 * Applies the default values
	 * 
	 * @param ref
	 */
	private void applyExampleDefault(IPluginReference ref) {
		// ref.getProperties().setProperty("targetNamespace", "tns");

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

		for (int i = 0; i < IOssjWSDLProfilePlugin.supportedVersions.length; i++) {
			activeVersionCombo.add(IOssjWSDLProfilePlugin.supportedVersions[i]);
		}
		activeVersionCombo.addSelectionListener(listener);
		activeVersionCombo.setEnabled(!this.isReadonly());

		targetNamespaceLabel = toolkit.createLabel(parent,
				"Target Namespace: ", SWT.NULL);
		targetNamespaceText = toolkit.createText(parent, "");
		td = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		targetNamespaceText.setLayoutData(td);
		targetNamespaceText.addModifyListener(listener);
		targetNamespaceText.setEnabled(!this.isReadonly());

		targetPrefixLabel = toolkit.createLabel(parent, "Target Prefix: ",
				SWT.NULL);
		targetPrefixText = toolkit.createText(parent, "");
		td = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		targetPrefixText.setLayoutData(td);
		targetPrefixText.addModifyListener(listener);
		targetPrefixText.setEnabled(!this.isReadonly());

		includeWSNotificationsButton = toolkit.createButton(parent,
				"Include WS-Notifications", SWT.CHECK);
		includeWSNotificationsButton.addSelectionListener(listener);
		includeWSNotificationsButton.setEnabled(!this.isReadonly());
		td = new GridData(GridData.FILL);
		td.horizontalSpan = 2;
		includeWSNotificationsButton.setLayoutData(td);

		wsNotificationsLocationLabel = toolkit.createLabel(parent,
				"WS-Notifications Location: ", SWT.NULL);
		wsNotificationsLocationText = toolkit.createText(parent, "");
		td = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		wsNotificationsLocationText.setLayoutData(td);
		wsNotificationsLocationText.addModifyListener(listener);
		wsNotificationsLocationText.setEnabled(!this.isReadonly());

		generateExamples = toolkit.createButton(parent, "Generate Examples",
				SWT.CHECK);
		generateExamples.addSelectionListener(listener);
		generateExamples.setEnabled(!this.isReadonly());
		td = new GridData(GridData.FILL);
		generateExamples.setLayoutData(td);

		getToolkit().paintBordersFor(parent);
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
			Properties pluginProperties = getWSDLPluginProperties();
			if (pluginProperties != null) {
				pluginProperties.setProperty("targetNamespace",
						targetNamespaceText.getText().trim());
				pluginProperties.setProperty("targetPrefix", targetPrefixText
						.getText().trim());
				pluginProperties.setProperty("WSNotificationsLocation",
						wsNotificationsLocationText.getText().trim());
				markPageModified();
			}
		}
	}

	protected void handleWidgetSelected(SelectionEvent e) {
		if (!isSilentUpdate()) {
			if (e.getSource() == generate) {
				if (getWSDLPluginReference() == null) {
					addPluginToDescriptor(createDefaultWsdlPluginReference());
				}
				getWSDLPluginReference().setEnabled(
						generate.getSelection() && !this.isReadonly());
				markPageModified();
			} else if (e.getSource() == includeWSNotificationsButton) {
				Properties pluginProperties = getWSDLPluginProperties();
				pluginProperties.setProperty("includeWSNotifications", String
						.valueOf(includeWSNotificationsButton.getSelection()));
				markPageModified();
			} else if (e.getSource() == activeVersionCombo) {
				getWSDLPluginProperties()
						.setProperty(
								"activeVersion",
								IOssjWSDLProfilePlugin.supportedVersions[activeVersionCombo
										.getSelectionIndex()]);
				markPageModified();
			} else if (e.getSource() == applyDefaultButton) {
				MessageBox dialog = new MessageBox(getSection().getShell(),
						SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				dialog.setText("Apply Default Values");
				dialog
						.setMessage("Do you really want to apply default values?\nAll current values will be lost.");
				if (dialog.open() == SWT.YES) {
					applyDefault(getWSDLPluginReference());
					markPageModified();
				}
			} else if (e.getSource() == generateExamples) {
				if (getWSDLExamplePluginReference() == null) {
					addPluginToDescriptor(createDefaultWsdlExamplePluginReference());
				}
				getWSDLExamplePluginReference().setEnabled(
						generateExamples.getSelection() && !this.isReadonly());
				markPageModified();
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

		if (getWSDLPluginReference() == null
				|| !getWSDLPluginReference().isEnabled()) {
			generate.setSelection(false);
		} else {
			Properties pluginProperties = getWSDLPluginProperties();
			generate.setSelection(true);
			targetNamespaceText.setText(pluginProperties.getProperty(
					"targetNamespace", ""));
			targetPrefixText.setText(pluginProperties.getProperty(
					"targetPrefix", ""));
			includeWSNotificationsButton.setSelection("true"
					.equalsIgnoreCase(pluginProperties.getProperty(
							"includeWSNotifications", "false")));
			wsNotificationsLocationText.setText(pluginProperties.getProperty(
					"WSNotificationsLocation", ""));

			Properties examplePluginProperties = getWSDLExamplePluginProperties();
			if (getWSDLExamplePluginReference() == null) {
				addPluginToDescriptor(createDefaultWsdlExamplePluginReference());
			}
			generateExamples.setSelection(getWSDLExamplePluginReference()
					.isEnabled());

			int versionIndex = -1;
			String activeVersion = pluginProperties.getProperty(
					"activeVersion", IOssjWSDLProfilePlugin.defaultVersion);
			for (int i = 0; i < IOssjWSDLProfilePlugin.supportedVersions.length; i++) {
				if (activeVersion
						.equals(IOssjWSDLProfilePlugin.supportedVersions[i])) {
					versionIndex = i;
				}
			}

			activeVersionCombo.select(versionIndex);
		}

		boolean isEnabled = generate.getSelection();
		applyDefaultButton.setEnabled(isEnabled && !this.isReadonly());
		targetNamespaceText.setEnabled(isEnabled && !this.isReadonly());
		targetNamespaceLabel.setEnabled(isEnabled && !this.isReadonly());
		targetPrefixText.setEnabled(isEnabled && !this.isReadonly());
		targetPrefixLabel.setEnabled(isEnabled && !this.isReadonly());
		includeWSNotificationsButton
				.setEnabled(isEnabled && !this.isReadonly());
		wsNotificationsLocationText.setEnabled(isEnabled
				& includeWSNotificationsButton.getSelection()
				&& !this.isReadonly());
		wsNotificationsLocationLabel.setEnabled(isEnabled
				& includeWSNotificationsButton.getSelection()
				&& !this.isReadonly());

		generateExamples.setEnabled(isEnabled && !this.isReadonly());
		activeVersionLabel.setEnabled(isEnabled && !this.isReadonly());
		activeVersionCombo.setEnabled(isEnabled && !this.isReadonly());
		String enabledString = (isEnabled ? "enabled" : "disabled");
		setTitle("Web Service Integration Profile (" + enabledString + ")");
		setSilentUpdate(false);
	}
}
