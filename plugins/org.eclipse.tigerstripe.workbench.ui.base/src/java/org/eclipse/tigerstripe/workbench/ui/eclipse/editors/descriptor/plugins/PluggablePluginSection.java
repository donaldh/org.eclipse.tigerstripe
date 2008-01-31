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
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.pluggable.IPluggablePluginPropertyListener;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggableHousing;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.PluggablePluginProject;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.TigerstripeDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.plugins.renderer.BasePropertyRenderer;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.plugins.renderer.PropertyRendererFactory;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

/**
 * Builds a section dynamically based on the metadata contained in the housing
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class PluggablePluginSection extends TigerstripeDescriptorSectionPart
		implements IPluggablePluginPropertyListener {

	private PluggableHousing housing;

	private List<BasePropertyRenderer> renderers = new ArrayList<BasePropertyRenderer>();

	private Button applyDefaultButton;

	private CCombo loggingLevelCombo;

	private Label loggingLevelLabel;

	private Button generate;

	private boolean silentUpdate;

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class DefaultPageListener implements SelectionListener {
		public void widgetDefaultSelected(SelectionEvent e) {
		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}
	}

	public PluggablePluginSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit, PluggableHousing housing) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR
				| ExpandableComposite.TWISTIE);

		this.housing = housing;

		setTitle(housing.getPluginId() + getNatureMark());
		createContent();
	}

	protected String getNatureMark() {
		String validationLabel = " [" + housing.getPluginNature().name() + "]";
		return validationLabel;
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

		updateForm();
		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	/**
	 * Returns the reference for this plugin in the local project based on the
	 * details found in the housing.
	 * 
	 * @return the pluginConfig from the tigerstripe.xml descriptor if it exists,
	 *         null otherwise.
	 */
	private IPluginConfig getPluginConfig() {
		try {
			ITigerstripeProject handle = getTSProject();
			IPluginConfig[] plugins = handle.getPluginConfigs();

			for (int i = 0; i < plugins.length; i++) {
				if (housing.getPluginId().equals(plugins[i].getPluginId()))
					return plugins[i];
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return null;
	}

	private ITigerstripeProject getITigerstripeProject() {

		TigerstripeProjectHandle handle = (TigerstripeProjectHandle) getTSProject();
		return handle;
	}

	private void createID(Composite parent, FormToolkit toolkit)
			throws TigerstripeException {
		GridData td = null;

		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		parent.setLayout(layout);

		DefaultPageListener listener = new DefaultPageListener();

		generate = toolkit.createButton(parent, "Enable", SWT.CHECK);
		generate.addSelectionListener(listener);
		generate.setEnabled(!this.isReadonly());
		td = new GridData(GridData.FILL);
		generate.setLayoutData(td);

		applyDefaultButton = toolkit.createButton(parent, "Default", SWT.PUSH);
		applyDefaultButton.addSelectionListener(listener);
		td = new GridData(GridData.FILL);
		applyDefaultButton.setLayoutData(td);
		applyDefaultButton.setEnabled(!this.isReadonly());

		toolkit.createLabel(parent, "");

		buildLoggingDetails(parent, toolkit);

		// Build dynamically the content now based on the global properties
		// found in the metadata
		buildGlobalProperties(parent, toolkit);

		initGlobalProperties();

		toolkit.paintBordersFor(parent);
	}

	protected void buildLoggingDetails(Composite parent, FormToolkit toolkit) {

		loggingLevelLabel = toolkit.createLabel(parent, "Log Level:");
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalIndent = 5;
		loggingLevelLabel.setLayoutData(gd);

		int nValues = PluginLog.LogLevel.values().length;
		String[] selections = new String[nValues + 1];
		int i = 0;
		for (PluginLog.LogLevel level : PluginLog.LogLevel.values()) {
			selections[i] = level.name();
			i++;
		}
		selections[i] = "No Logging";

		loggingLevelCombo = new CCombo(parent, SWT.READ_ONLY | SWT.BORDER);

		loggingLevelCombo.setEnabled(!this.isReadonly());
		toolkit.adapt(loggingLevelCombo, true, true);
		loggingLevelCombo.setItems(selections);
		loggingLevelCombo.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				IPluginConfig ref = getPluginConfig();
				int index = loggingLevelCombo.getSelectionIndex();
				if (ref != null) {
					if (index < PluginLog.LogLevel.values().length) {
						PluginLog.LogLevel level = PluginLog.LogLevel
								.fromInt(index);
						ref.setLogLevel(level);
						ref.setDisableLogging(false);
					} else
						ref.setDisableLogging(true);
					markPageModified();

				}
			}
		});

		toolkit.createLabel(parent, "");
	}

	/**
	 * Builds all the controls for the GlobalProperties defined in this plugin
	 * 
	 * @param parent
	 * @param toolkit
	 */
	protected void buildGlobalProperties(Composite parent, FormToolkit toolkit) {
		PluggablePluginProject pProject = housing.getBody().getPProject();
		PropertyRendererFactory factory = new PropertyRendererFactory(parent,
				toolkit, getITigerstripeProject(), this);

		for (IPluginProperty property : pProject.getGlobalProperties()) {
			try {
				BasePropertyRenderer renderer = factory.getRenderer(property);
				renderer.render();
				renderers.add(renderer);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	/**
	 * Initialize the global properties. One of 2 cases may happen: - No
	 * PluginConfig exists. In this case we set default values for all fields and
	 * leave the "generate" disabled. Upon enablement of Generate, a PluginConfig
	 * is created and pushed into the tigerstripe.xml - a PluginConfig
	 * corresponding to this housing is found in the Tigerstripe.xml descriptor.
	 * In this case we use it. - However - The list of properties may have
	 * changed due to plugin re-deployment.
	 */
	private void initGlobalProperties() {
		IPluginConfig ref = getPluginConfig();
		if (ref == null) {
			generate.setSelection(false);
			applyDefaultButton.setEnabled(false);
			for (BasePropertyRenderer renderer : renderers) {
				renderer.setEnabled(false);
			}
		} else {
			PluggablePluginConfig pRef = (PluggablePluginConfig) ref;
			boolean enabled = pRef.isEnabled();
			generate.setSelection(enabled);
			applyDefaultButton.setEnabled(enabled);
			for (BasePropertyRenderer renderer : renderers) {
				renderer.setEnabled(enabled);
			}
			// Make sure the ref has the correct properties
			Properties properties = ((PluginConfig) ref).getProperties();
			String[] definedProps = ref.getDefinedProperties();
			Properties usableProps = new Properties();
			PluggablePluginProject pProject = housing.getBody().getPProject();
			// TigerstripeRuntime.logInfoMessage(definedProps.length);
			for (int i = 0; i < definedProps.length; i++) {
				// TigerstripeRuntime.logInfoMessage(definedProps[i]);
				if (properties.getProperty(definedProps[i]) == null
						|| properties.getProperty(definedProps[i]).length() == 0) {

					for (IPluginProperty property : pProject
							.getGlobalProperties()) {
						if (property.getName().equals(definedProps[i])) {
							usableProps.setProperty(definedProps[i], property
									.getDefaultValue().toString());
							// If we are here, we've updated descriptor based on
							// newly deployed plugin data.
							markPageModified();
						}
					}
				} else {
					usableProps.setProperty(definedProps[i], properties
							.getProperty(definedProps[i]));
				}
			}
			pRef.setProperties(usableProps);
		}
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

	private PluggablePluginConfig createInitialPluginConfig() {
		try {
			TigerstripeProjectHandle handle = (TigerstripeProjectHandle) getITigerstripeProject();
			PluggablePluginConfig ref = housing.makeDefaultPluginConfig(handle);
			handle.addPluginConfig(ref);
			return ref;
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return null;
	}

	protected void handleWidgetSelected(SelectionEvent e) {
		if (!isSilentUpdate()) {
			if (e.getSource() == generate) {
				// Upon first enablement, we need to check that a PluginConfig
				// exists for this housing in the tigerstripe.xml descriptor
				if (generate.getSelection() && getPluginConfig() == null) {
					// first time. Let's create a pluginConfig in the descriptor
					PluggablePluginConfig ref = createInitialPluginConfig();
					if (ref == null) {
						// it didn't work
						generate.setSelection(false);
					} else {
						markPageModified();
					}

				}

				getPluginConfig().setEnabled(generate.getSelection());
				markPageModified();
			} else if (e.getSource() == applyDefaultButton) {
				MessageBox dialog = new MessageBox(getSection().getShell(),
						SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				dialog.setText("Apply Default Values");
				dialog
						.setMessage("Do you really want to apply default values?\nAll current values will be lost.");
				if (dialog.open() == SWT.YES) {

					for (BasePropertyRenderer renderer : renderers) {
						renderer.applyDefault();
					}
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

		IPluginConfig ref = getPluginConfig();

		boolean isEnabled = generate.getSelection();
		applyDefaultButton.setEnabled(isEnabled && !this.isReadonly());

		if (ref != null) {
			if (ref.isLoggingDisabled()) {
				loggingLevelCombo.setEnabled(isEnabled && !this.isReadonly());
				loggingLevelLabel.setEnabled(isEnabled && !this.isReadonly());
				loggingLevelCombo.select(PluginLog.LogLevel.values().length); // corresponds
				// to
				// "no
				// logging"
				// label
			} else if (ref.isLogEnabled()) {
				if (loggingLevelCombo.getSelectionIndex() == -1) {
					loggingLevelCombo.select(ref.getCurrentLogLevel().toInt());
				}
				loggingLevelCombo.setEnabled(isEnabled && !this.isReadonly()
						&& ref.isLogEnabled());
				loggingLevelLabel.setEnabled(isEnabled && !this.isReadonly()
						&& ref.isLogEnabled());
			} else {
				loggingLevelCombo.setEnabled(false);
				loggingLevelLabel.setEnabled(false);
			}
		} else {
			loggingLevelCombo.setEnabled(false);
			loggingLevelLabel.setEnabled(false);
		}

		for (BasePropertyRenderer renderer : renderers) {
			renderer.setEnabled(isEnabled);

			if (ref != null) {
				IPluginProperty property = renderer.getProperty();
				String serializedValue = ((PluginConfig) ref).getProperties().getProperty(
						property.getName());
				renderer.update(serializedValue);
			}

		}
		String enabledString = (isEnabled ? "enabled" : "disabled");
		setTitle(housing.getPluginId() + " (" + enabledString + ")"
				+ getNatureMark());
		setSilentUpdate(false);
	}

	/**
	 * This method is called when the property is changed from a renderer. It
	 * will propagate the value down to the PluginConfig for persistence
	 * 
	 * @param property
	 * @param value
	 * @throws TigerstripeException
	 */
	public void storeProperty(IPluginProperty property, Object value)
			throws TigerstripeException {
		PluggablePluginConfig ref = (PluggablePluginConfig) getPluginConfig();
		if (ref != null) {
			Properties props = ref.getProperties();
			props.setProperty(property.getName(), property.serialize(value));
			markPageModified();
		}
	}
}
