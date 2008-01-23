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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.advanced;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.impl.AbstractTigerstripeProjectHandle;
import org.eclipse.tigerstripe.api.project.IAdvancedProperties;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.TigerstripeDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.preferences.TopLevelPreferencePage;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class AdvancedGeneralSection extends TigerstripeDescriptorSectionPart
		implements IAdvancedProperties {

	private boolean silentUpdate;

	// Should a report be generated upon run (PROP_GENERATION_GenerateReport)
	private Button generateReportButton;

	// Should messages from code generation be logged during run
	// (PROP_GENERATION_LogMessages)
	private Button logMessagesButton;

	// should XML message payload samples be generated
	// (PROP_GENERATION_GenerateXmlMessagePayloadSample)
	// Moved to the wsdl page for consistency
	// private Button generateWSDLExamplesButton;

	// should examples use the network for schemas
	// (PROP_GENERATION_MessagePayloadSampleAllowNetwork)
	private Button allowNetworkButton;

	private Text defaultSchemaLocationText;

	// Artifact elements without tags should be ignored
	// (PROP_MISC_IgnoreArtifactElementsWithoutTag)
	private Button ignoreElementsWithoutTagsButton;

	private Button applyDefaultPreferences;

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class DefaultPageListener implements ModifyListener,
			SelectionListener {

		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}

	}

	public AdvancedGeneralSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR
				| ExpandableComposite.TWISTIE | ExpandableComposite.COMPACT);
		setTitle("General");
		createContent();
	}

	@Override
	protected void createContent() {
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		getSection().setLayout(layout);

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

	private void createID(Composite parent, FormToolkit toolkit)
			throws TigerstripeException {
		AbstractTigerstripeProjectHandle handle = (AbstractTigerstripeProjectHandle) getTSProject();

		DefaultPageListener listener = new DefaultPageListener();

		createApplyDefaultsButton(parent, toolkit, listener);
		createGenerationExpandable(parent, toolkit, listener);
		// createMiscExpandable(parent, toolkit, listener);
	}

	private void createApplyDefaultsButton(Composite composite,
			FormToolkit toolkit, DefaultPageListener listener) {
		applyDefaultPreferences = toolkit.createButton(composite,
				"Apply default preferences", SWT.PUSH);
		TableWrapData td = new TableWrapData(TableWrapData.LEFT);
		td.colspan = 2;
		applyDefaultPreferences.setLayoutData(td);
		applyDefaultPreferences.addSelectionListener(listener);
	}

	private void createGenerationExpandable(Composite composite,
			FormToolkit toolkit, DefaultPageListener listener) {

		ExpandableComposite exComposite = toolkit.createExpandableComposite(
				composite, ExpandableComposite.TREE_NODE);
		exComposite.setText("Generation");
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 1;
		exComposite.setLayout(layout);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		exComposite.setLayoutData(td);
		exComposite.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				getManagedForm().reflow(true);
			}
		});
		Composite innerComposite = toolkit.createComposite(exComposite);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		innerComposite.setLayoutData(td);
		layout = new TableWrapLayout();
		layout.numColumns = 2;
		innerComposite.setLayout(layout);
		exComposite.setClient(innerComposite);

		generateReportButton = toolkit.createButton(innerComposite,
				"Generate report", SWT.CHECK);
		generateReportButton
				.setToolTipText("Creates a report on the generated files.");
		td = new TableWrapData(TableWrapData.LEFT);
		td.colspan = 2;
		generateReportButton.setLayoutData(td);
		generateReportButton.addSelectionListener(listener);

		logMessagesButton = toolkit.createButton(innerComposite,
				"Log messages", SWT.CHECK);
		td = new TableWrapData(TableWrapData.LEFT);
		td.colspan = 2;
		logMessagesButton.setLayoutData(td);
		logMessagesButton.addSelectionListener(listener);

		allowNetworkButton = toolkit.createButton(innerComposite,
				"Allow Internet Access for message payload samples", SWT.CHECK);

		td = new TableWrapData(TableWrapData.LEFT);
		td.colspan = 2;
		allowNetworkButton.setLayoutData(td);
		allowNetworkButton.addSelectionListener(listener);

		Label label = toolkit
				.createLabel(
						innerComposite,
						"Default Location for schemas (relative to Project Base Dir): ",
						SWT.NULL);
		defaultSchemaLocationText = toolkit.createText(innerComposite, "");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		defaultSchemaLocationText.setLayoutData(td);
		defaultSchemaLocationText.addModifyListener(listener);
		getToolkit().paintBordersFor(innerComposite);

	}

	private void createPreferenceMsg(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		String data = "<form><p></p><p>To set default values to be reused across multiple Tigerstripe Projects, please use the Tigerstripe <a href=\"http://www.tigerstripedev.net/	\">preferences</a> page.</p></form>";
		FormText rtext = toolkit.createFormText(parent, true);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
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
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.
			ITigerstripeProject handle = getTSProject();

			if (e.getSource() == defaultSchemaLocationText) {
				try {
					handle
							.setAdvancedProperty(
									IAdvancedProperties.PROP_GENERATION_MessagePayloadSampleDefaultlocation,
									String.valueOf(defaultSchemaLocationText
											.getText()));
				} catch (TigerstripeException ee) {
					Status status = new Status(
							IStatus.ERROR,
							TigerstripePluginConstants.PLUGIN_ID,
							222,
							"Error while setting "
									+ IAdvancedProperties.PROP_GENERATION_MessagePayloadSampleDefaultlocation
									+ " advanced property on Project "
									+ handle.getProjectLabel(), ee);
					EclipsePlugin.log(status);
				}
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

	protected boolean getShownDescriptorUpgrade() {
		AdvancedConfigurationPage page = (AdvancedConfigurationPage) getPage();
		return page.getShownDescriptorUpgrade();
	}

	protected void setShownDescriptorUpgrade(boolean setShownDescriptorUpgrade) {
		AdvancedConfigurationPage page = (AdvancedConfigurationPage) getPage();
		page.setShownDescriptorUpgrade(setShownDescriptorUpgrade);
	}

	protected void updateForm() {
		ITigerstripeProject handle = getTSProject();

		try {
			setSilentUpdate(true);

			if (handle.requiresDescriptorUpgrade()
					&& !getShownDescriptorUpgrade()) {
				setShownDescriptorUpgrade(true);
				MessageBox dialog = new MessageBox(getSection().getShell(),
						SWT.ICON_INFORMATION | SWT.OK);
				dialog.setText("Upgrade project descriptor");
				dialog
						.setMessage("Advanced properties are not properly set (in project '"
								+ handle.getProjectDetails().getName()
								+ "').\nDefault preferences will be applied.");
				dialog.open();
				applyAdvancedPropertiesDefaults();
			}

			generateReportButton
					.setSelection("true"
							.equalsIgnoreCase(handle
									.getAdvancedProperty(IAdvancedProperties.PROP_GENERATION_GenerateReport)));

			logMessagesButton
					.setSelection("true"
							.equalsIgnoreCase(handle
									.getAdvancedProperty(IAdvancedProperties.PROP_GENERATION_LogMessages)));
			/*
			 * generateWSDLExamplesButton.setSelection("true".equalsIgnoreCase(
			 * handle.getAdvancedProperty(
			 * IAdvancedProperties.PROP_GENERATION_GenerateXmlMessagePayloadSample)));
			 */

			allowNetworkButton
					.setSelection("true"
							.equalsIgnoreCase(handle
									.getAdvancedProperty(IAdvancedProperties.PROP_GENERATION_MessagePayloadSampleAllowNetwork)));

			// ignoreElementsWithoutTagsButton.setSelection("true".equalsIgnoreCase(
			// handle.getAdvancedProperty(
			// PROP_MISC_IgnoreArtifactElementsWithoutTag)));

			defaultSchemaLocationText
					.setText(handle
							.getAdvancedProperty(
									IAdvancedProperties.PROP_GENERATION_MessagePayloadSampleDefaultlocation,
									""));

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

	private void handleWidgetSelected(SelectionEvent e) {
		ITigerstripeProject handle = getTSProject();

		if (e.getSource() == applyDefaultPreferences) {
			MessageBox dialog = new MessageBox(getSection().getShell(),
					SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			dialog.setText("Apply default preferences");
			dialog
					.setMessage("Do you really want to apply default preferences for Advanced Project Configuration?\nAll current values will be lost.");
			if (dialog.open() == SWT.YES) {
				try {
					applyAdvancedPropertiesDefaults();
				} catch (TigerstripeException ee) {
					Status status = new Status(
							IStatus.ERROR,
							TigerstripePluginConstants.PLUGIN_ID,
							222,
							"Error while applying default Advanced Preferences on Project",
							ee);
					EclipsePlugin.log(status);
				}
			}
		} else if (e.getSource() == generateReportButton) {
			try {
				handle.setAdvancedProperty(
						IAdvancedProperties.PROP_GENERATION_GenerateReport,
						String.valueOf(generateReportButton.getSelection()));
				markPageModified();
			} catch (TigerstripeException ee) {
				Status status = new Status(
						IStatus.ERROR,
						TigerstripePluginConstants.PLUGIN_ID,
						222,
						"Error while setting "
								+ IAdvancedProperties.PROP_GENERATION_GenerateReport
								+ " advanced property on Project "
								+ handle.getProjectLabel(), ee);
				EclipsePlugin.log(status);
			}

		} else if (e.getSource() == logMessagesButton) {
			try {
				handle.setAdvancedProperty(
						IAdvancedProperties.PROP_GENERATION_LogMessages, String
								.valueOf(logMessagesButton.getSelection()));
				markPageModified();
			} catch (TigerstripeException ee) {
				Status status = new Status(
						IStatus.ERROR,
						TigerstripePluginConstants.PLUGIN_ID,
						222,
						"Error while setting "
								+ IAdvancedProperties.PROP_GENERATION_LogMessages
								+ " advanced property on Project "
								+ handle.getProjectLabel(), ee);
				EclipsePlugin.log(status);
			}

		} else if (e.getSource() == allowNetworkButton) {

			try {
				handle
						.setAdvancedProperty(
								IAdvancedProperties.PROP_GENERATION_MessagePayloadSampleAllowNetwork,
								String.valueOf(allowNetworkButton
										.getSelection()));
				markPageModified();
			} catch (TigerstripeException ee) {
				Status status = new Status(
						IStatus.ERROR,
						TigerstripePluginConstants.PLUGIN_ID,
						222,
						"Error while setting "
								+ IAdvancedProperties.PROP_GENERATION_MessagePayloadSampleAllowNetwork
								+ " advanced property on Project "
								+ handle.getProjectLabel(), ee);
				EclipsePlugin.log(status);
			}
		} else if (e.getSource() == ignoreElementsWithoutTagsButton) {
			try {
				handle
						.setAdvancedProperty(
								IAdvancedProperties.PROP_MISC_IgnoreArtifactElementsWithoutTag,
								String.valueOf(ignoreElementsWithoutTagsButton
										.getSelection()));
				markPageModified();
			} catch (TigerstripeException ee) {
				Status status = new Status(
						IStatus.ERROR,
						TigerstripePluginConstants.PLUGIN_ID,
						222,
						"Error while setting "
								+ IAdvancedProperties.PROP_MISC_IgnoreArtifactElementsWithoutTag
								+ " advanced property on Project "
								+ handle.getProjectLabel(), ee);
				EclipsePlugin.log(status);
			}
		}
	}

	private void applyAdvancedPropertiesDefaults() throws TigerstripeException {
		ITigerstripeProject handle = getTSProject();

		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();

		handle
				.setAdvancedProperty(
						IAdvancedProperties.PROP_GENERATION_GenerateReport,
						store
								.getString(IAdvancedProperties.PROP_GENERATION_GenerateReport));
		handle
				.setAdvancedProperty(
						IAdvancedProperties.PROP_GENERATION_LogMessages,
						store
								.getString(IAdvancedProperties.PROP_GENERATION_LogMessages));
		// handle.setAdvancedProperty(IAdvancedProperties.PROP_GENERATION_GenerateXmlMessagePayloadSample,
		// store.getString(IAdvancedProperties.PROP_GENERATION_GenerateXmlMessagePayloadSample));
		handle
				.setAdvancedProperty(
						IAdvancedProperties.PROP_GENERATION_MessagePayloadSampleAllowNetwork,
						store
								.getString(IAdvancedProperties.PROP_GENERATION_MessagePayloadSampleAllowNetwork));
		handle
				.setAdvancedProperty(
						IAdvancedProperties.PROP_GENERATION_MessagePayloadSampleDefaultlocation,
						store
								.getString(IAdvancedProperties.PROP_GENERATION_MessagePayloadSampleDefaultlocation));
		// handle.setAdvancedProperty(IAdvancedProperties.PROP_MISC_IgnoreArtifactElementsWithoutTag,
		// "true");

		refresh();
		markPageModified();
	}
}
