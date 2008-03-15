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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
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
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggableHousing;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.GeneratorProjectDescriptor;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.plugins.IPluggablePluginPropertyListener;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.FacetSelectionDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.TigerstripeDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.plugins.renderer.BasePropertyRenderer;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.plugins.renderer.PropertyRendererFactory;
import org.eclipse.ui.IFileEditorInput;
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

	private Label facetReferenceLabel = null;
	private Text facetReferenceText = null;
	private Button browseForFacetReferenceButton = null;
	private Label facetOutputDirLabel = null;
	private Text facetOutputDirText = null;

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
	 * @return the pluginConfig from the tigerstripe.xml descriptor if it
	 *         exists, null otherwise.
	 */
	private IPluginConfig getPluginConfig() {
		try {
			ITigerstripeModelProject handle = getTSProject();
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

	private ITigerstripeModelProject getITigerstripeProject() {

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

		buildFacetDetails(parent, toolkit);

		// Build dynamically the content now based on the global properties
		// found in the metadata
		buildGlobalProperties(parent, toolkit);

		initGlobalProperties();

		toolkit.paintBordersFor(parent);
	}

	protected void buildFacetDetails(Composite parent, FormToolkit toolkit) {
		facetReferenceLabel = toolkit.createLabel(parent, "Use Facet:");
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalIndent = 5;
		facetReferenceLabel.setLayoutData(gd);

		facetReferenceText = toolkit.createText(parent, "");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		facetReferenceText.setLayoutData(gd);
		facetReferenceText
				.setToolTipText("The facet to use as scope for this plugin.");
		facetReferenceText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!isSilentUpdate()) {
					String relativePath = facetReferenceText.getText().trim();
					IPluginConfig config = getPluginConfig();
					if ("".equals(relativePath)) {
						config.setFacetReference(null);
					} else {
						try {
							ITigerstripeModelProject handle = getTSProject();
							IFacetReference dep = handle
									.makeFacetReference(relativePath);
							dep.setGenerationDir(facetOutputDirText.getText()
									.trim());
							config.setFacetReference(dep);
						} catch (TigerstripeException ee) {
							EclipsePlugin.log(ee);
						}
					}
					try {
						getTSProject().addPluginConfig(config);
						markPageModified();
					} catch (TigerstripeException ee) {
						EclipsePlugin.log(ee);
					}

					markPageModified();
				}
			}
		});

		browseForFacetReferenceButton = toolkit.createButton(parent, "Browse",
				SWT.PUSH);
		browseForFacetReferenceButton
				.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						widgetSelected(e);
					}

					@Override
					public void widgetSelected(SelectionEvent e) {
						IFacetReference ref = browseForFacetButtonSelected(e);
						if (ref != null) {
							String dir = facetOutputDirText.getText().trim();
							facetReferenceText.setText(ref
									.getProjectRelativePath());
							if (!"".equals(dir)) {
								ref.setGenerationDir(dir);
							}

							IPluginConfig config = getPluginConfig();
							config.setFacetReference(ref);
							try {
								getTSProject().addPluginConfig(config);
								markPageModified();
							} catch (TigerstripeException ee) {
								EclipsePlugin.log(ee);
							}

							markPageModified();
							updateForm();
						}
					}
				});

		facetOutputDirLabel = toolkit.createLabel(parent, "Output dir");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalIndent = 10;
		facetOutputDirLabel.setLayoutData(gd);

		facetOutputDirText = toolkit.createText(parent, "");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		facetOutputDirText.setLayoutData(gd);
		facetOutputDirText
				.setToolTipText("The facet to use as scope for this plugin.");
		facetOutputDirText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (!isSilentUpdate()) {
					String dir = facetOutputDirText.getText().trim();
					if ("".equals(dir)) {
						dir = null;
					}
					IPluginConfig config = getPluginConfig();
					IFacetReference ref = config.getFacetReference();
					ref.setGenerationDir(dir);
					config.setFacetReference(ref);
					try {
						getTSProject().addPluginConfig(config);
						markPageModified();
					} catch (TigerstripeException ee) {
						EclipsePlugin.log(ee);
					}
				}
			}

		});
		toolkit.createLabel(parent, "");

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

					IPluginConfig config = getPluginConfig();
					try {
						getTSProject().addPluginConfig(config);
						markPageModified();
					} catch (TigerstripeException ee) {
						EclipsePlugin.log(ee);
					}
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
		GeneratorProjectDescriptor pProject = housing.getBody().getPProject();
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
	 * PluginConfig exists. In this case we set default values for all fields
	 * and leave the "generate" disabled. Upon enablement of Generate, a
	 * PluginConfig is created and pushed into the tigerstripe.xml - a
	 * PluginConfig corresponding to this housing is found in the
	 * Tigerstripe.xml descriptor. In this case we use it. - However - The list
	 * of properties may have changed due to plugin re-deployment.
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
			GeneratorProjectDescriptor pProject = housing.getBody()
					.getPProject();
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

				IPluginConfig config = getPluginConfig();
				config.setEnabled(generate.getSelection());
				try {
					getTSProject().addPluginConfig(config);
					markPageModified();
				} catch (TigerstripeException ee) {
					EclipsePlugin.log(ee);
				}
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
					IPluginConfig config = getPluginConfig();
					try {
						getTSProject().addPluginConfig(config);
						markPageModified();
					} catch (TigerstripeException ee) {
						EclipsePlugin.log(ee);
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

		facetOutputDirText.setEnabled(isEnabled
				&& getPluginConfig().getFacetReference() != null);
		facetOutputDirLabel.setEnabled(isEnabled
				&& getPluginConfig().getFacetReference() != null);

		facetReferenceLabel.setEnabled(isEnabled);
		facetReferenceText.setEnabled(isEnabled);
		browseForFacetReferenceButton.setEnabled(isEnabled);

		if (ref != null) {
			if (ref.getFacetReference() != null) {
				IFacetReference fRef = ref.getFacetReference();
				facetReferenceText.setText(fRef.getProjectRelativePath());
				if (!"".equals(fRef.getGenerationDir())) {
					facetOutputDirText.setText(fRef.getGenerationDir());
				}
			}
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
				String serializedValue = ((PluginConfig) ref).getProperties()
						.getProperty(property.getName());
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
			getTSProject().addPluginConfig(ref);
			markPageModified();
		}
	}

	protected IFacetReference browseForFacetButtonSelected(SelectionEvent event) {
		if (getPage().getEditorInput() instanceof IFileEditorInput) {
			IFileEditorInput input = (IFileEditorInput) getPage()
					.getEditorInput();
			FacetSelectionDialog dialog = new FacetSelectionDialog(getSection()
					.getShell(), false, false);
			dialog
					.setInput(input.getFile().getProject().getLocation()
							.toFile());
			dialog.setDoubleClickSelects(true);
			dialog.setTitle("Select Facet");

			if (dialog.open() == Window.OK) {
				Object[] toAdd = dialog.getResult();
				for (int i = 0; i < toAdd.length; i++) {
					File file = (File) toAdd[i];

					try {
						String relative = Util.getRelativePath(file, input
								.getFile().getProject().getLocation().toFile());
						ITigerstripeModelProject handle = getTSProject();
						IFacetReference dep = handle
								.makeFacetReference(relative);
						return dep;
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					} catch (IOException e) {
						EclipsePlugin.log(e);
					}
				}
			}
		}
		return null;
	}

}
