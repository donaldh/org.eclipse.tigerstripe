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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.csvCreate;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.api.IPluginReference;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.core.plugin.CSVCreatePluginRef;
import org.eclipse.tigerstripe.core.plugin.PluginRef;
import org.eclipse.tigerstripe.core.plugin.PluginRefFactory;
import org.eclipse.tigerstripe.core.plugin.UnknownPluginException;
import org.eclipse.tigerstripe.core.plugin.csv.CSVPlugin;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.core.util.Util;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.internal.ide.dialogs.FileFolderSelectionDialog;

public class CSVCreateOverviewSection extends AbstractCSVCreateSection {

	private Button generate;

	private Button applyDefaultButton;

	private Text csvDirectoryText;

	private Label csvDirectoryLabel;

	private Button browseCVSDirButton;

	// Fix bug 108 - No value in "publishing" a project that has not been
	// "generated"
	// private Button generateBeforePublish;

	private Button includeInheritedInformation;

	private CCombo detailCombo;

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

	public CSVCreateOverviewSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR);
		setTitle("CSV Creation General Information");
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

		updateForm();
		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createDetailCombo(Composite parent, FormToolkit toolkit) {

		Label label = toolkit
				.createLabel(parent, "Level of detail: ", SWT.WRAP);
		detailCombo = new CCombo(parent, SWT.SINGLE | SWT.READ_ONLY | SWT.FLAT);
		toolkit.adapt(this.detailCombo, true, true);

		for (int i = 0; i < CSVPlugin.DETAIL_OPTIONS.length; i++) {
			detailCombo.add(CSVPlugin.DETAIL_OPTIONS[i]);
		}
		detailCombo.addSelectionListener(new DefaultPageListener());
		toolkit.createLabel(parent, "");
	}

	private void addCSVCreatePluginToDescriptor(IPluginReference ref) {
		try {
			TigerstripeProjectHandle handle = (TigerstripeProjectHandle) getTSProject();
			handle.addPluginReference(ref);
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
	private IPluginReference createDefaultCSVCreatePluginReference() {
		try {
			PluginRef ref = PluginRefFactory.getInstance().createPluginRef(
					CSVCreatePluginRef.MODEL, getTigerstripeProject());
			applyDefault(ref);
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
		ref.getProperties().setProperty(CSVPlugin.CSV_DIRECTORY, "csv");
		ref.getProperties().setProperty(CSVPlugin.INCLUDE_INHERITED, "true");
		ref.getProperties().setProperty(CSVPlugin.LEVEL_OF_DETAIL, "max");
		// Fix bug 108 - No value in "publishing" a project that has not been
		// "generated"
		// ref.getProperties().setProperty(PublisherPlugin.GENERATE_BEFOREPUBLISH,
		// "true");
		// ref.getProperties().setProperty(CSVPlugin.CLEAR_BEFOREPUBLISH,
		// "true");
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
		td = new GridData(GridData.FILL);
		generate.setLayoutData(td);

		applyDefaultButton = toolkit.createButton(parent, "Default", SWT.PUSH);
		applyDefaultButton.addSelectionListener(listener);
		td = new GridData(GridData.FILL);
		td.horizontalSpan = 2;
		applyDefaultButton.setLayoutData(td);

		csvDirectoryLabel = toolkit.createLabel(parent, "CSV Directory: ",
				SWT.NULL);
		csvDirectoryText = toolkit.createText(parent, "");
		td = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		csvDirectoryText.setLayoutData(td);
		csvDirectoryText.addModifyListener(listener);
		csvDirectoryText
				.setToolTipText("Project-relative directory where generated .csv file is to be saved.");

		browseCVSDirButton = toolkit.createButton(parent, "Browse", SWT.PUSH);
		browseCVSDirButton.addSelectionListener(listener);

		createDetailCombo(parent, toolkit);
		// Fix bug 108 - No value in "publishing" a project that has not been
		// "generated"
		// Label empty = toolkit.createLabel(parent, "" );
		// generateBeforePublish = toolkit.createButton(parent,
		// "Generate Project before publish", SWT.CHECK);
		// generateBeforePublish.addSelectionListener(listener);
		// td = new GridData(GridData.FILL_HORIZONTAL |
		// GridData.GRAB_HORIZONTAL);
		// generateBeforePublish.setLayoutData(td);
		//
		toolkit.createLabel(parent, "");
		includeInheritedInformation = toolkit.createButton(parent,
				"Include Inherited Information in CSV output", SWT.CHECK);
		includeInheritedInformation.addSelectionListener(listener);
		td = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		td.horizontalSpan = 2;
		includeInheritedInformation.setLayoutData(td);

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
			Properties pluginProperties = getCSVCreatePluginProperties();
			if (pluginProperties != null) {
				if (e.getSource() == csvDirectoryText) {
					pluginProperties.setProperty(CSVPlugin.CSV_DIRECTORY,
							csvDirectoryText.getText().trim());
					markPageModified();
				}
			}
		}
	}

	protected void handleWidgetSelected(SelectionEvent e) {
		if (!isSilentUpdate()) {
			Properties pluginProperties = getCSVCreatePluginProperties();
			if (e.getSource() == generate) {
				if (getCSVCreatePluginReference() == null) {
					addCSVCreatePluginToDescriptor(createDefaultCSVCreatePluginReference());
				}
				getCSVCreatePluginReference().setEnabled(
						generate.getSelection());
				markPageModified();
			} else if (e.getSource() == applyDefaultButton) {
				MessageBox dialog = new MessageBox(getSection().getShell(),
						SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				dialog.setText("Apply Default Values");
				dialog
						.setMessage("Do you really want to apply default values?\nAll current values will be lost.");
				if (dialog.open() == SWT.YES) {
					applyDefault(getCSVCreatePluginReference());
					markPageModified();
				}
				// Fix bug 108 - No value in "publishing" a project that has not
				// been "generated"
				// } else if ( e.getSource() == generateBeforePublish ) {
				// pluginProperties.setProperty(
				// PublisherPlugin.GENERATE_BEFOREPUBLISH,
				// String.valueOf(generateBeforePublish.getSelection()));
			} else if (e.getSource() == includeInheritedInformation) {
				pluginProperties.setProperty(CSVPlugin.INCLUDE_INHERITED,
						String.valueOf(includeInheritedInformation
								.getSelection()));

				markPageModified();
			} else if (e.getSource() == detailCombo) {
				pluginProperties.setProperty(CSVPlugin.LEVEL_OF_DETAIL, String
						.valueOf(CSVPlugin.DETAIL_OPTIONS[detailCombo
								.getSelectionIndex()]));
				markPageModified();
			} else if (e.getSource() == browseCVSDirButton) {
				if (getPage().getEditorInput() instanceof IFileEditorInput) {
					IFileEditorInput input = (IFileEditorInput) getPage()
							.getEditorInput();
					FileFolderSelectionDialog dialog = new FileFolderSelectionDialog(
							getSection().getShell(), false, IResource.FOLDER);
					dialog.setInput(input.getFile().getProject().getLocation()
							.toFile());
					dialog.setDoubleClickSelects(true);
					dialog.setTitle("Select csv target directory");

					if (dialog.open() == Window.OK) {
						File file = ((File) dialog.getResult()[0]);
						try {
							String relative = Util.getRelativePath(file, input
									.getFile().getProject().getLocation()
									.toFile());
							this.csvDirectoryText.setText(relative);
							pluginProperties.setProperty(
									CSVPlugin.CSV_DIRECTORY, csvDirectoryText
											.getText().trim());
							markPageModified();
						} catch (IOException ee) {
							EclipsePlugin.log(ee);
						}
					}
				}
			}
		}
		updateForm();
	}

	@Override
	public void refresh() {
		updateForm();
	}

	protected void updateForm() {
		setSilentUpdate(true);

		if (getCSVCreatePluginReference() == null
				|| !getCSVCreatePluginReference().isEnabled()) {
			generate.setSelection(false);
		} else {
			Properties pluginProperties = getCSVCreatePluginProperties();
			generate.setSelection(true);
			csvDirectoryText.setText(pluginProperties.getProperty(
					CSVPlugin.CSV_DIRECTORY, ""));
			// Fix bug 108 - No value in "publishing" a project that has not
			// been "generated"
			// generateBeforePublish.setSelection("true"
			// .equalsIgnoreCase(pluginProperties.getProperty(
			// PublisherPlugin.GENERATE_BEFOREPUBLISH, "")));
			includeInheritedInformation.setSelection("true"
					.equalsIgnoreCase(pluginProperties.getProperty(
							CSVPlugin.INCLUDE_INHERITED, "")));
			String level = pluginProperties.getProperty(
					CSVPlugin.LEVEL_OF_DETAIL, "max");
			for (int i = 0; i < CSVPlugin.DETAIL_OPTIONS.length; i++) {
				if (CSVPlugin.DETAIL_OPTIONS[i].equals(level)) {
					detailCombo.select(i);
				}
			}
		}

		boolean isEnabled = generate.getSelection();
		applyDefaultButton.setEnabled(isEnabled);
		csvDirectoryText.setEnabled(isEnabled);
		csvDirectoryLabel.setEnabled(isEnabled);
		browseCVSDirButton.setEnabled(isEnabled);
		// Fix bug 108 - No value in "publishing" a project that has not been
		// "generated"
		// generateBeforePublish.setEnabled(isEnabled);
		includeInheritedInformation.setEnabled(isEnabled);
		detailCombo.setEnabled(isEnabled);

		setSilentUpdate(false);
	}
}
