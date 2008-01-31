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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.runtime;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.pluggable.IPluggablePluginProject;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.PluginDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.PluginDescriptorSectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class PluginLogSection extends PluginDescriptorSectionPart {

	private boolean silentUpdate;

	private Text logPathText;

	private Button logEnabledButton;

	private CCombo defaultLevelButton;

	public PluginLogSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR
				| ExpandableComposite.COMPACT);
		setTitle("Plugin Logging Details");
		createContent();
	}

	@Override
	protected void createContent() {
		FormToolkit toolkit = getToolkit();

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		getSection().setLayoutData(td);

		Composite body = getToolkit().createComposite(getSection());
		GridLayout gLayout = new GridLayout();
		gLayout.marginWidth = 5;
		gLayout.marginHeight = 5;
		gLayout.numColumns = 2;
		body.setLayout(gLayout);

		logEnabledButton = toolkit.createButton(body,
				"Enable logging for this plugin", SWT.CHECK);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		logEnabledButton.setLayoutData(gd);
		logEnabledButton.setEnabled(PluginDescriptorEditor.isEditable());
		logEnabledButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (!isSilentUpdate()) {
					// when updating the form, the changes to all fields should
					// be
					// ignored so that the form is not marked as dirty.
					IPluggablePluginProject handle = getIPluggablePluginProject();
					try {
						handle.setLogEnabled(logEnabledButton.getSelection());
						markPageModified();
						updateForm();
					} catch (TigerstripeException ee) {
						EclipsePlugin.log(ee);
					}
				}
			}

		});

		toolkit.createLabel(body, "Log file:");
		logPathText = toolkit.createText(body, "truc");
		logPathText.setEnabled(PluginDescriptorEditor.isEditable());
		logPathText
				.setToolTipText("Pathname of the logfile relative to generation directory.");
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 2;
		logPathText.setLayoutData(gd);
		logPathText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				handleModifyText(e);
			}
		});

		int nValues = PluginLog.LogLevel.values().length;
		String[] selections = new String[nValues];
		int i = 0;
		for (PluginLog.LogLevel level : PluginLog.LogLevel.values()) {
			selections[i] = level.name();
			i++;
		}

		toolkit.createLabel(body, "Default Debug Level:");
		defaultLevelButton = new CCombo(body, SWT.READ_ONLY | SWT.BORDER);
		defaultLevelButton.setEnabled(PluginDescriptorEditor.isEditable());
		toolkit.adapt(defaultLevelButton, true, true);
		defaultLevelButton.setItems(selections);
		defaultLevelButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (!isSilentUpdate()) {
					// when updating the form, the changes to all fields should
					// be
					// ignored so that the form is not marked as dirty.
					IPluggablePluginProject handle = getIPluggablePluginProject();
					try {
						handle
								.setDefaultLogLevel(PluginLog.LogLevel
										.fromInt(defaultLevelButton
												.getSelectionIndex()));
						markPageModified();
						updateForm();
					} catch (TigerstripeException ee) {
						EclipsePlugin.log(ee);
					}
				}
			}

		});

		getSection().setClient(body);
		getToolkit().paintBordersFor(body);
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
			IPluggablePluginProject handle = getIPluggablePluginProject();
			try {
				if (e.getSource() == logPathText) {
					handle.setLogPath(logPathText.getText().trim());
				}
				markPageModified();
			} catch (TigerstripeException ee) {
				EclipsePlugin.log(ee);
			}
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

			logEnabledButton.setSelection(handle.isLogEnabled());
			logPathText.setText(handle.getLogPath());
			logPathText.setEnabled(logEnabledButton.isEnabled());
			defaultLevelButton.select(handle.getDefaultLogLevel().toInt());
			defaultLevelButton.setEnabled(logEnabledButton.isEnabled());

			setSilentUpdate(false);
		} catch (TigerstripeException e) {
			Status status = new Status(IStatus.ERROR,
					TigerstripePluginConstants.PLUGIN_ID, 222,
					"Error refreshing Logger details on plugin descriptor", e);
			EclipsePlugin.log(status);
		}
	}
}
