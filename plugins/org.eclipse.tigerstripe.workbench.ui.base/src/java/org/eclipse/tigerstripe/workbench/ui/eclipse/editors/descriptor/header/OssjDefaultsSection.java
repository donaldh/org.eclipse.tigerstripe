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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.header;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.IProjectDetails;
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

public class OssjDefaultsSection extends TigerstripeDescriptorSectionPart {

	private Text idText;

	private Label idLabel;

	private Text idText2;

	private Label idLabel2;

	private boolean silentUpdate;

	// === The Oss/J Common Defaults
	private Button applyOssjCommonDefault;

	private Text ossjCommonSchemaLocation;

	private Label ossjCommonSchemaLocationLabel;

	private Text ossjCommonTargetNamespace;

	private Label ossjCommonTargetNamespaceLabel;

	private Text ossjCommonNamespacePrefix;

	private Label ossjCommonNamespacePrefixLabel;

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class DefaultPageListener implements ModifyListener, KeyListener,
			SelectionListener {

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

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}

	}

	public OssjDefaultsSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR
				| ExpandableComposite.TWISTIE | ExpandableComposite.COMPACT);
		setTitle("Project Defaults");
		createContent();
	}

	@Override
	protected void createContent() {
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		getSection().setLayout(layout);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		getSection().setLayoutData(td);

		try {
			createID(getBody(), getToolkit());
		} catch (TigerstripeException ee) {
			Status status = new Status(IStatus.WARNING,
					TigerstripePluginConstants.PLUGIN_ID, 111,
					"Unexpected Exception", ee);
			EclipsePlugin.log(status);
		}

		if (!this.isReadonly())
			createPreferenceMsg(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createID(Composite parent, FormToolkit toolkit)
			throws TigerstripeException {
		TableWrapData td = null;

		ITigerstripeProject handle = getTSProject();

		DefaultPageListener listener = new DefaultPageListener();

		idLabel = toolkit.createLabel(parent, "Default Artifact Package: ",
				SWT.WRAP);
		idText = toolkit.createText(parent, handle.getProjectDetails()
				.getProperty(IProjectDetails.DEFAULTARTIFACTPACKAGE_PROP, ""));
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		idText.setLayoutData(td);
		idText.addModifyListener(listener);
		idText.setEnabled(!this.isReadonly());
		idLabel.setEnabled(!this.isReadonly());

		idLabel2 = toolkit.createLabel(parent,
				"Copyright Notice for all files: ", SWT.WRAP);
		idText2 = toolkit.createText(parent, handle.getProjectDetails()
				.getProperty("copyrightNotice", ""), SWT.WRAP | SWT.MULTI
				| SWT.V_SCROLL);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.grabVertical = true;
		td.heightHint = 70;
		idText2.setLayoutData(td);
		idText2.addModifyListener(listener);
		idText2.setEnabled(!this.isReadonly());
		idLabel2.setEnabled(!this.isReadonly());

		ExpandableComposite exComposite = toolkit.createExpandableComposite(
				parent, ExpandableComposite.TREE_NODE);
		exComposite.setText("Implementation Defaults");
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 1;
		exComposite.setLayout(layout);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
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

		applyOssjCommonDefault = toolkit.createButton(innerComposite,
				"Apply Defaults", SWT.PUSH);
		td = new TableWrapData(TableWrapData.LEFT);
		td.colspan = 2;
		applyOssjCommonDefault.setLayoutData(td);
		applyOssjCommonDefault.addSelectionListener(listener);
		applyOssjCommonDefault.setEnabled(!this.isReadonly());

		ossjCommonTargetNamespaceLabel = toolkit.createLabel(innerComposite,
				"OSS/J Common Target Namespace: ");
		ossjCommonTargetNamespace = toolkit.createText(innerComposite, "");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		ossjCommonTargetNamespace.setLayoutData(td);
		ossjCommonTargetNamespace.addModifyListener(listener);
		ossjCommonTargetNamespace.setEnabled(!this.isReadonly());
		ossjCommonTargetNamespaceLabel.setEnabled(!this.isReadonly());

		ossjCommonSchemaLocationLabel = toolkit.createLabel(innerComposite,
				"OSS/J Common Schema Location: ");
		ossjCommonSchemaLocation = toolkit.createText(innerComposite, "");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		ossjCommonSchemaLocation.setLayoutData(td);
		ossjCommonSchemaLocation.addModifyListener(listener);
		ossjCommonSchemaLocation.setEnabled(!this.isReadonly());
		ossjCommonSchemaLocationLabel.setEnabled(!this.isReadonly());

		ossjCommonNamespacePrefixLabel = toolkit.createLabel(innerComposite,
				"OSS/J Common Namespace Prefix: ");
		ossjCommonNamespacePrefix = toolkit.createText(innerComposite, "");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		ossjCommonNamespacePrefix.setLayoutData(td);
		ossjCommonNamespacePrefix.addModifyListener(listener);
		ossjCommonNamespacePrefix.setEnabled(!this.isReadonly());
		ossjCommonNamespacePrefixLabel.setEnabled(!this.isReadonly());
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

			try {
				if (e.getSource() == idText) {
					handle.getProjectDetails().getProperties().setProperty(
							IProjectDetails.DEFAULTARTIFACTPACKAGE_PROP,
							idText.getText().trim());
				} else if (e.getSource() == idText2) {
					handle.getProjectDetails().getProperties().setProperty(
							IProjectDetails.COPYRIGHT_NOTICE,
							idText2.getText().trim());
				} else if (e.getSource() == ossjCommonNamespacePrefix) {
					handle.getProjectDetails().getProperties().setProperty(
							IProjectDetails.OSSJ_COMMON_NAMESPACE_PREFIX,
							ossjCommonNamespacePrefix.getText().trim());
				} else if (e.getSource() == ossjCommonTargetNamespace) {
					handle.getProjectDetails().getProperties().setProperty(
							IProjectDetails.OSSJ_COMMON_TARGET_NAMESPACE,
							ossjCommonTargetNamespace.getText().trim());
				} else if (e.getSource() == ossjCommonSchemaLocation) {
					handle.getProjectDetails().getProperties().setProperty(
							IProjectDetails.OSSJ_COMMON_SCHEMA_LOCATION,
							ossjCommonSchemaLocation.getText().trim());
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
		DescriptorEditor editor = (DescriptorEditor) getPage().getEditor();
		editor.pageModified();
	}

	@Override
	public void refresh() {
		updateForm();
	}

	protected void updateForm() {
		ITigerstripeProject handle = getTSProject();

		try {
			setSilentUpdate(true);
			idText.setText(handle.getProjectDetails().getProperty(
					IProjectDetails.DEFAULTARTIFACTPACKAGE_PROP, ""));
			idText2.setText(handle.getProjectDetails().getProperty(
					IProjectDetails.COPYRIGHT_NOTICE, ""));

			// For compatibility reasons, since 1.0 moved the following
			// properties from the context of the XML plugin to the project
			// details
			// we check if they're set and offer to set defaults.

			String ossjCommonTargetnamespaceStr = handle.getProjectDetails()
					.getProperty(IProjectDetails.OSSJ_COMMON_TARGET_NAMESPACE,
							"");
			ossjCommonTargetNamespace.setText(ossjCommonTargetnamespaceStr);

			String ossjCommonSchemaLocationStr = handle.getProjectDetails()
					.getProperty(IProjectDetails.OSSJ_COMMON_SCHEMA_LOCATION,
							"");
			ossjCommonSchemaLocation.setText(ossjCommonSchemaLocationStr);

			String ossjCommonNamespacePrefixStr = handle.getProjectDetails()
					.getProperty(IProjectDetails.OSSJ_COMMON_NAMESPACE_PREFIX,
							"");
			ossjCommonNamespacePrefix.setText(ossjCommonNamespacePrefixStr);
			setSilentUpdate(false);

			if ("".equals(ossjCommonTargetnamespaceStr)
					|| "".equals(ossjCommonSchemaLocationStr)
					|| "".equals(ossjCommonNamespacePrefixStr)) {
				MessageBox dialog = new MessageBox(getSection().getShell(),
						SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				dialog.setText("Set OSS/J Common Schema Defaults");
				dialog
						.setMessage("References to OSS/J Common Schema are not properly set (in project '"
								+ handle.getProjectDetails().getName()
								+ "').\nDo you want to automatically apply default values?.");
				if (dialog.open() == SWT.YES) {
					applyOssjCommonDefaults();
					markPageModified();
				}
			}
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

	private void applyOssjCommonDefaults() {
		ITigerstripeProject handle = getTSProject();

		try {
			handle.getProjectDetails().getProperties().setProperty(
					IProjectDetails.OSSJ_COMMON_TARGET_NAMESPACE,
					"http://ossj.org/xml/Common/v1-3");
			markPageModified();
			handle.getProjectDetails().getProperties().setProperty(
					IProjectDetails.OSSJ_COMMON_NAMESPACE_PREFIX, "co-v1-3");
			handle.getProjectDetails().getProperties().setProperty(
					IProjectDetails.OSSJ_COMMON_SCHEMA_LOCATION,
					"http://ossj.org/xml/Common/v1-3/OSSJ-Common-v1-3.xsd");
		} catch (TigerstripeException e) {
			Status status = new Status(
					IStatus.ERROR,
					TigerstripePluginConstants.PLUGIN_ID,
					222,
					"Error while trying to apply OSS/J Common Schema Defaults.",
					e);
			EclipsePlugin.log(status);
		}
		updateForm();
	}

	private void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == applyOssjCommonDefault) {
			MessageBox dialog = new MessageBox(getSection().getShell(),
					SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			dialog.setText("Apply Default Values");
			dialog
					.setMessage("Do you really want to apply default OSSJ Common Schma values?\nAll current values will be lost.");
			if (dialog.open() == SWT.YES) {
				applyOssjCommonDefaults();
				markPageModified();
			}

		}
	}
}
