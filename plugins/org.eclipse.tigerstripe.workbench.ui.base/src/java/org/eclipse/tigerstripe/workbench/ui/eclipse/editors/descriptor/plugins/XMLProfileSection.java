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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.builtin.IOssjWSDLProfilePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.builtin.IOssjXMLProfilePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PackageToSchemaMapper;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfigFactory;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.UnknownPluginException;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.XmlExamplePluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.XmlPluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PackageToSchemaMapper.PckXSDMapping;
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

public class XMLProfileSection extends TigerstripeDescriptorSectionPart {

	private Label activeVersionLabel;

	private CCombo activeVersionCombo;

	private Button generate;

	private Button generateExamples;

	private Button useEnumValues;

	private Button applyDefaultButton;

	private Button addMappingButton;

	private Button editMappingButton;

	private Button removeMappingButton;

	private boolean silentUpdate;

	private Button useDefaultMappingButton;

	private Text defaultUserPrefixText;

	private Label defaultUserPrefixLabel;

	private Text defaultSchemaNameText;

	private Label defaultScemaNameLabel;

	private Text defaultSchemaLocationText;

	private Label defaultScemaLocationLabel;

	private Text defaultNamespaceText;

	private Label defaultNamespaceLabel;

	private TableViewer mappingTableViewer;

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class DefaultPageListener implements ModifyListener, KeyListener,
			SelectionListener, IDoubleClickListener {

		public void doubleClick(DoubleClickEvent event) {
			editMappingButtonPressed();
		}

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

	public XMLProfileSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR
				| ExpandableComposite.TWISTIE);
		setTitle("JMS/XML Integration Profile");
		createContent();
	}

	@Override
	protected void createContent() {
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		getSection().setLayoutData(td);

		try {
			createID(getBody(), getToolkit());
			createPackageMappingContent(getBody(), getToolkit());
			createExample(getBody(), getToolkit());
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
		IPluginConfig ref = getXMLPluginConfig();
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
	private IPluginConfig getXMLPluginConfig() {
		try {
			ITigerstripeProject handle = getTSProject();
			IPluginConfig[] plugins = handle.getPluginConfigs();

			for (int i = 0; i < plugins.length; i++) {
				if (XmlPluginConfig.MODEL.getPluginId().equals(
						plugins[i].getPluginId()))
					return plugins[i];
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		return null;
	}

	private void addPluginToDescriptor(IPluginConfig ref) {
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
	private IPluginConfig createDefaultXMLPluginConfig() {
		try {
			PluginConfig ref = PluginConfigFactory.getInstance().createPluginConfig(
					XmlPluginConfig.MODEL, getTigerstripeProject());
			applyDefault(ref);
			return ref;
		} catch (UnknownPluginException e) {
			EclipsePlugin.log(e);
		}
		return null;
	}

	private TigerstripeProject getTigerstripeProject() {
		try {
			return ((TigerstripeProjectHandle) getITigerstripeProject())
					.getTSProject();
		} catch (TigerstripeException e) {
			return null;
		}
	}

	private ITigerstripeProject getITigerstripeProject() {
		ITigerstripeProject handle = getTSProject();
		return handle;
	}

	/**
	 * Applies the default values
	 * 
	 * @param ref
	 */
	private void applyDefault(IPluginConfig ref) {

		XmlPluginConfig xmlRef = (XmlPluginConfig) ref;
		PackageToSchemaMapper mapper = xmlRef.getMapper();

		mapper.setUseDefaultMapping(true);
		mapper
				.setDefaultSchemaLocation("http://ossj.org/xml/${name}/v${ver}/OSSJ-${name}-v${ver}.xsd");
		mapper
				.setDefaultSchemaName("xml/${name}/v${ver}/OSSJ-${name}-v${ver}.xsd");
		mapper.setTargetNamespace("http://ossj.org/xml/${name}/v${ver}");
		mapper.setDefaultUserPrefix("ossj-${name}-v${ver}");
		((PluginConfig) ref).getProperties().setProperty("activeVersion",
				IOssjXMLProfilePlugin.defaultVersion);

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

		for (int i = 0; i < IOssjXMLProfilePlugin.supportedVersions.length; i++) {
			activeVersionCombo.add(IOssjXMLProfilePlugin.supportedVersions[i]);
		}
		activeVersionCombo.addSelectionListener(listener);
		activeVersionCombo.setEnabled(!this.isReadonly());

	}

	private void createExample(Composite parent, FormToolkit toolkit)
			throws TigerstripeException {
		GridData td = null;

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);

		DefaultPageListener listener = new DefaultPageListener();

		useEnumValues = toolkit.createButton(parent,
				"Use Enumeration values in schema", SWT.CHECK);
		useEnumValues.addSelectionListener(listener);
		useEnumValues.setEnabled(!this.isReadonly());
		td = new GridData(GridData.FILL);
		useEnumValues.setLayoutData(td);

		generateExamples = toolkit.createButton(parent, "Generate Examples",
				SWT.CHECK);
		generateExamples.addSelectionListener(listener);
		generateExamples.setEnabled(!this.isReadonly());
		td = new GridData(GridData.FILL);
		generateExamples.setLayoutData(td);

	}

	private void createPackageMappingContent(Composite parent,
			FormToolkit toolkit) {
		DefaultPageListener listener = new DefaultPageListener();

		Composite mappingComposite = toolkit.createComposite(parent);
		GridLayout gdl = new GridLayout();
		gdl.numColumns = 3;
		mappingComposite.setLayout(gdl);
		GridData ggd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		ggd.horizontalSpan = 3;
		mappingComposite.setLayoutData(ggd);

		useDefaultMappingButton = toolkit.createButton(mappingComposite,
				"Use default Schema", SWT.CHECK);
		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		useDefaultMappingButton.setLayoutData(gd);
		useDefaultMappingButton.addSelectionListener(listener);
		useDefaultMappingButton.setEnabled(!this.isReadonly());

		defaultScemaNameLabel = toolkit.createLabel(mappingComposite,
				"Default Schema Name:");
		defaultSchemaNameText = toolkit.createText(mappingComposite, "");
		defaultSchemaNameText.addModifyListener(listener);
		GridData gd2 = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		defaultSchemaNameText.setLayoutData(gd2);
		defaultSchemaNameText.setEnabled(!this.isReadonly());
		defaultScemaNameLabel.setEnabled(!this.isReadonly());
		toolkit.createLabel(mappingComposite, "");

		defaultUserPrefixLabel = toolkit.createLabel(mappingComposite,
				"Default Schema Prefix:");
		defaultUserPrefixText = toolkit.createText(mappingComposite, "");
		defaultUserPrefixText.addModifyListener(listener);
		defaultUserPrefixText.setLayoutData(gd2);
		defaultUserPrefixText.setEnabled(!this.isReadonly());
		defaultUserPrefixLabel.setEnabled(!this.isReadonly());
		toolkit.createLabel(mappingComposite, "");

		defaultNamespaceLabel = toolkit.createLabel(mappingComposite,
				"Default Namespace:");
		defaultNamespaceText = toolkit.createText(mappingComposite, "");
		defaultNamespaceText.addModifyListener(listener);
		defaultNamespaceText.setLayoutData(gd2);
		defaultNamespaceText.setEnabled(!this.isReadonly());
		defaultNamespaceLabel.setEnabled(!this.isReadonly());
		toolkit.createLabel(mappingComposite, "");

		defaultScemaLocationLabel = toolkit.createLabel(mappingComposite,
				"Default Schema Location:");
		defaultSchemaLocationText = toolkit.createText(mappingComposite, "");
		defaultSchemaLocationText.addModifyListener(listener);
		defaultSchemaLocationText.setLayoutData(gd2);
		defaultSchemaLocationText.setEnabled(!this.isReadonly());
		defaultScemaNameLabel.setEnabled(!this.isReadonly());
		toolkit.createLabel(mappingComposite, "");

		mappingTableViewer = new TableViewer(mappingComposite, SWT.SINGLE
				| SWT.FULL_SELECTION);
		final Table table = mappingTableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setTopIndex(0);
		table.addSelectionListener(listener);
		GridData tableGD = new GridData(GridData.FILL_HORIZONTAL);
		tableGD.horizontalSpan = 2;
		tableGD.verticalSpan = 3;
		tableGD.heightHint = 70;
		table.setLayoutData(tableGD);
		String[] columnNames = new String[] { "Package", "Schema Name",
				"Target Namespace", "Schema Location", "Prefix" };
		int[] columnWidths = new int[] { 150, 150, 150, 150, 120 };
		int[] columnAlignments = new int[] { SWT.LEFT, SWT.LEFT, SWT.LEFT,
				SWT.LEFT, SWT.LEFT };

		for (int i = 0; i < columnAlignments.length; i++) {
			TableColumn tableColumn = new TableColumn(table,
					columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidths[i]);
		}

		mappingTableViewer.setLabelProvider(new PackageMappingLabelProvider());
		mappingTableViewer
				.setContentProvider(new PackageMappingContentProvider());
		mappingTableViewer.setInput(getPackageToSchemaMapper());
		mappingTableViewer.addDoubleClickListener(listener);
		table.setEnabled(!this.isReadonly());

		addMappingButton = toolkit.createButton(mappingComposite, "Add",
				SWT.PUSH);
		GridData gdb = new GridData();
		addMappingButton.setLayoutData(gdb);
		addMappingButton.addSelectionListener(listener);
		addMappingButton.setEnabled(!this.isReadonly());

		editMappingButton = toolkit.createButton(mappingComposite, "Edit",
				SWT.PUSH);
		editMappingButton.setLayoutData(gdb);
		editMappingButton.addSelectionListener(listener);
		editMappingButton.setEnabled(!this.isReadonly());

		removeMappingButton = toolkit.createButton(mappingComposite, "Remove",
				SWT.PUSH);
		removeMappingButton.setLayoutData(gdb);
		removeMappingButton.addSelectionListener(listener);
		removeMappingButton.setEnabled(!this.isReadonly());

		getToolkit().paintBordersFor(mappingComposite);
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
	 * Returns the XML EXAMPLE plugin ref from the descriptor if it exists, null
	 * otherwise.
	 * 
	 * @return
	 */
	private IPluginConfig getXmlExamplePluginConfig() {
		try {
			ITigerstripeProject handle = getTSProject();
			IPluginConfig[] plugins = handle.getPluginConfigs();

			for (int i = 0; i < plugins.length; i++) {
				if ("ossj-xml-example-spec".equals(plugins[i].getPluginId()))
					return plugins[i];
			}

		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		return null;
	}

	/**
	 * If a XML Example plugin ref exists in the descriptor, return it. If not
	 * create one with default values.
	 * 
	 * @return
	 */
	private IPluginConfig createDefaultXmlExamplePluginReference() {
		try {
			PluginConfig ref = PluginConfigFactory.getInstance().createPluginConfig(
					XmlExamplePluginConfig.MODEL, getTigerstripeProject());
			applyExampleDefault(ref);
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
	private void applyExampleDefault(IPluginConfig ref) {
		// ref.getProperties().setProperty("targetNamespace", "tns");

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

			if (e.getSource() == defaultSchemaLocationText) {
				getPackageToSchemaMapper().setDefaultSchemaLocation(
						defaultSchemaLocationText.getText().trim());
				markPageModified();
			} else if (e.getSource() == defaultSchemaNameText) {
				getPackageToSchemaMapper().setDefaultSchemaName(
						defaultSchemaNameText.getText().trim());
				markPageModified();
			} else if (e.getSource() == defaultNamespaceText) {
				getPackageToSchemaMapper().setTargetNamespace(
						defaultNamespaceText.getText().trim());
				markPageModified();
			} else if (e.getSource() == defaultUserPrefixText) {
				getPackageToSchemaMapper().setDefaultUserPrefix(
						defaultUserPrefixText.getText().trim());
				markPageModified();
			}
		}
	}

	protected void handleWidgetSelected(SelectionEvent e) {
		if (!isSilentUpdate()) {
			if (e.getSource() == generate) {
				PluginConfig ref = (PluginConfig) getXMLPluginConfig();
				if (ref == null) {
					addPluginToDescriptor(createDefaultXMLPluginConfig());
				}
				getXMLPluginConfig().setEnabled(
						generate.getSelection() && !this.isReadonly());
				markPageModified();
			} else if (e.getSource() == applyDefaultButton) {
				MessageBox dialog = new MessageBox(getSection().getShell(),
						SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				dialog.setText("Apply Default Values");
				dialog
						.setMessage("Do you really want to apply default values?\nAll current values will be lost.");
				if (dialog.open() == SWT.YES) {
					applyDefault(getXMLPluginConfig());
					markPageModified();
				}
			} else if (e.getSource() == activeVersionCombo) {
				getXMLPluginProperties()
						.setProperty(
								"activeVersion",
								IOssjXMLProfilePlugin.supportedVersions[activeVersionCombo
										.getSelectionIndex()]);
				markPageModified();
			} else if (e.getSource() == addMappingButton) {
				addMappingButtonPressed();
			} else if (e.getSource() == editMappingButton) {
				editMappingButtonPressed();
			} else if (e.getSource() == removeMappingButton) {
				removeMappingButtonPressed();
			} else if (e.getSource() == useDefaultMappingButton) {
				getPackageToSchemaMapper().setUseDefaultMapping(
						useDefaultMappingButton.getSelection());
				markPageModified();
			} else if (e.getSource() == generateExamples) {
				if (getXmlExamplePluginConfig() == null) {
					addPluginToDescriptor(createDefaultXmlExamplePluginReference());
				}
				getXmlExamplePluginConfig().setEnabled(
						generateExamples.getSelection() && !this.isReadonly());
				markPageModified();
			} else if (e.getSource() == useEnumValues) {
				getXMLPluginProperties().setProperty("useEnumValues",
						Boolean.toString(useEnumValues.getSelection()));
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

	protected void addMappingButtonPressed() {
		PckXSDMapping newMapping = getPackageToSchemaMapper().new PckXSDMapping();
		PckXSDMapping[] existingMappings = getPackageToSchemaMapper()
				.getMappings();
		PackageMappingEditDialog dialog = new PackageMappingEditDialog(
				getBody().getShell(), newMapping, Arrays
						.asList(existingMappings), getITigerstripeProject());
		if (dialog.open() == Window.OK) {
			PckXSDMapping[] newMappings = new PckXSDMapping[existingMappings.length + 1];
			for (int i = 0; i < existingMappings.length; i++) {
				newMappings[i] = existingMappings[i];
			}
			newMappings[existingMappings.length] = newMapping;
			getPackageToSchemaMapper().setMappings(newMappings);
			mappingTableViewer.refresh(true);
			markPageModified();
		}
	}

	protected void removeMappingButtonPressed() {
		int index = mappingTableViewer.getTable().getSelectionIndex();
		PckXSDMapping mapping = getPackageToSchemaMapper().getMappings()[index];

		MessageBox dialog = new MessageBox(getSection().getShell(),
				SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		dialog.setText("Remove Package Mapping");
		dialog.setMessage("Do you really want to remove the '"
				+ mapping.getPackage() + "' package mapping?");
		if (dialog.open() == SWT.YES) {
			PckXSDMapping[] mappings = getPackageToSchemaMapper().getMappings();
			PckXSDMapping[] newMappings = new PckXSDMapping[mappings.length - 1];
			int newIndex = 0;
			for (int i = 0; i < mappings.length; i++) {
				if (i != index) {
					newMappings[newIndex++] = mappings[i];
				}
			}
			getPackageToSchemaMapper().setMappings(newMappings);
			markPageModified();
			mappingTableViewer.refresh(true);
			mappingTableViewer.getTable().redraw();
		}
	}

	protected void editMappingButtonPressed() {
		int index = mappingTableViewer.getTable().getSelectionIndex();
		PckXSDMapping mapping = getPackageToSchemaMapper().getMappings()[index];
		PckXSDMapping clone = getPackageToSchemaMapper().new PckXSDMapping(
				mapping);

		PckXSDMapping[] existingMappings = getPackageToSchemaMapper()
				.getMappings();
		List existingList = new ArrayList();
		existingList.addAll(Arrays.asList(existingMappings));
		existingList.remove(mapping);
		PackageMappingEditDialog dialog = new PackageMappingEditDialog(
				getBody().getShell(), clone, existingList,
				getITigerstripeProject());
		if (dialog.open() == Window.OK) {
			existingMappings[index] = clone;
			getPackageToSchemaMapper().setMappings(existingMappings);
			mappingTableViewer.refresh(true);
			mappingTableViewer.getTable().redraw();
			markPageModified();
		}
	}

	protected void updateForm() {
		setSilentUpdate(true);

		if (getXMLPluginConfig() == null
				|| !getXMLPluginConfig().isEnabled()) {
			generate.setSelection(false);
		} else {
			Properties pluginProperties = getXMLPluginProperties();
			generate.setSelection(true);
			if (getXmlExamplePluginConfig() == null) {
				addPluginToDescriptor(createDefaultXmlExamplePluginReference());
			}
			generateExamples.setSelection(getXmlExamplePluginConfig()
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
			String useEnums = pluginProperties.getProperty("useEnumValues",
					"false");
			useEnumValues.setSelection(Boolean.parseBoolean(useEnums));

		}
		updateSchemaMappingControls();

		boolean isEnabled = generate.getSelection();
		applyDefaultButton.setEnabled(isEnabled && !this.isReadonly());
		generateExamples.setEnabled(isEnabled && !this.isReadonly());
		useEnumValues.setEnabled(isEnabled && !this.isReadonly());
		String enabledString = (isEnabled ? "enabled" : "disabled");
		setTitle("JMS/XML Integration Profile (" + enabledString + ")");
		setSilentUpdate(false);
	}

	private void updateSchemaMappingControls() {
		useDefaultMappingButton.setSelection(getPackageToSchemaMapper()
				.useDefaultMapping());
		useDefaultMappingButton.setEnabled(generate.getEnabled()
				&& !this.isReadonly());

		activeVersionLabel.setEnabled(generate.getEnabled()
				&& !this.isReadonly());
		activeVersionCombo.setEnabled(generate.getEnabled()
				&& !this.isReadonly());

		defaultSchemaNameText.setEnabled(useDefaultMappingButton.getSelection()
				&& !this.isReadonly());
		defaultScemaNameLabel.setEnabled(useDefaultMappingButton.getSelection()
				&& !this.isReadonly());
		defaultSchemaNameText.setText(getPackageToSchemaMapper()
				.getDefaultSchemaName());

		defaultSchemaLocationText.setEnabled(useDefaultMappingButton
				.getSelection()
				&& !this.isReadonly());
		defaultScemaLocationLabel.setEnabled(useDefaultMappingButton
				.getSelection()
				&& !this.isReadonly());
		defaultSchemaLocationText.setText(getPackageToSchemaMapper()
				.getDefaultSchemaLocation());

		defaultUserPrefixText.setEnabled(useDefaultMappingButton.getSelection()
				&& !this.isReadonly());
		defaultUserPrefixLabel.setEnabled(useDefaultMappingButton
				.getSelection()
				&& !this.isReadonly());
		defaultUserPrefixText.setText(getPackageToSchemaMapper()
				.getDefaultUserPrefix());

		defaultNamespaceText.setEnabled(useDefaultMappingButton.getSelection()
				&& !this.isReadonly());
		defaultNamespaceLabel.setEnabled(useDefaultMappingButton.getSelection()
				&& !this.isReadonly());
		defaultNamespaceText.setText(getPackageToSchemaMapper()
				.getTargetNamespace());

		if (mappingTableViewer.getTable().getSelectionIndex() != -1) {
			editMappingButton.setEnabled(!useDefaultMappingButton
					.getSelection()
					&& !this.isReadonly());
			removeMappingButton.setEnabled(!useDefaultMappingButton
					.getSelection()
					&& !this.isReadonly());
		} else {
			editMappingButton.setEnabled(false);
			removeMappingButton.setEnabled(false);
		}
		addMappingButton.setEnabled(!useDefaultMappingButton.getSelection()
				&& !this.isReadonly());

		mappingTableViewer.refresh(true);
		mappingTableViewer.getTable().setEnabled(
				!useDefaultMappingButton.getSelection() && !this.isReadonly());

		// In case the pluginConfig is not enabled altogether...
		if (!generate.getSelection()) {
			useDefaultMappingButton.setEnabled(false);
			defaultSchemaNameText.setEnabled(false);
			defaultScemaNameLabel.setEnabled(false);
			defaultSchemaLocationText.setEnabled(false);
			defaultScemaLocationLabel.setEnabled(false);
			defaultNamespaceText.setEnabled(false);
			defaultNamespaceLabel.setEnabled(false);
			defaultUserPrefixText.setEnabled(false);
			defaultUserPrefixLabel.setEnabled(false);
			mappingTableViewer.getTable().setEnabled(false);
			addMappingButton.setEnabled(false);
			editMappingButton.setEnabled(false);
			removeMappingButton.setEnabled(false);
			activeVersionLabel.setEnabled(false);
			activeVersionCombo.setEnabled(false);
		}
	}

	// Handling of the Package<->XSD mapping
	private PackageToSchemaMapper getPackageToSchemaMapper() {
		XmlPluginConfig ref = (XmlPluginConfig) getXMLPluginConfig();
		if (ref == null) {
			ref = (XmlPluginConfig) createDefaultXMLPluginConfig();
			addPluginToDescriptor(ref);
		}

		PackageToSchemaMapper result = ref.getMapper();
		return result;
	}
}
