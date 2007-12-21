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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.impl.AbstractTigerstripeProjectHandle;
import org.eclipse.tigerstripe.api.project.IAdvancedProperties;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.model.importing.db.mapper.DBDatatypeMapping;
import org.eclipse.tigerstripe.core.model.importing.db.mapper.DBTypeMapper;
import org.eclipse.tigerstripe.core.model.importing.mapper.UmlDatatypeMapper;
import org.eclipse.tigerstripe.core.model.importing.mapper.UmlDatatypeMapping;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.TigerstripeDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.preferences.modelImport.ImportPreferencePage;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class ModelImportSection extends TigerstripeDescriptorSectionPart
		implements IAdvancedProperties {
	private boolean silentUpdate;

	// ================================================
	// UML Mapping table
	private UmlDatatypeMapper umlDatatypeMapper;

	private TableViewer umlMappingTableViewer;

	private Button addUMLMappingButton;

	private Button editUMLMappingButton;

	private Button removeUMLMappingButton;

	private Button restoreDefaultUMLMappingsButton;

	private Button exportUMLMap;

	private Button importUMLMap;

	// ================================================
	// DB Mapping table
	private DBTypeMapper dbDatatypeMapper;

	private TableViewer dbMappingTableViewer;

	private Button addDBMappingButton;

	private Button editDBMappingButton;

	private Button removeDBMappingButton;

	private Button restoreDefaultDBMappingsButton;

	private Button exportDBMap;

	private Button importDBMap;

	// Should a report be generated upon run
	// (PROP_IMPORT_USETARGETPROJECTASGUIDE)
	private Button useTargetProjectToGuide;

	// DB Import specific buttons
	private Button importTables;

	private Button importViews;

	private Button applyDefaultPreferences;

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class DefaultPageListener implements ModifyListener,
			SelectionListener, IDoubleClickListener {

		public void doubleClick(DoubleClickEvent event) {
			if (event.getSource() == umlMappingTableViewer)
				editMappingButtonPressed();
			else if (event.getSource() == dbMappingTableViewer)
				editDBMappingButtonPressed();
		}

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

	public ModelImportSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR
				| ExpandableComposite.TWISTIE | ExpandableComposite.COMPACT);
		setTitle("Model Import");
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

		DefaultPageListener listener = new DefaultPageListener();

		createApplyDefaultsButton(parent, toolkit, listener);
		createGeneralExpandable(parent, toolkit, listener);
		createUmlImportExpandable(parent, toolkit, listener);
		createDBImportExpandable(parent, toolkit, listener);
		// createMiscExpandable(parent, toolkit, listener);
	}

	private void createApplyDefaultsButton(Composite composite,
			FormToolkit toolkit, SelectionListener listener) {
		applyDefaultPreferences = toolkit.createButton(composite,
				"Apply default preferences", SWT.PUSH);
		TableWrapData td = new TableWrapData(TableWrapData.LEFT);
		td.colspan = 2;
		applyDefaultPreferences.setLayoutData(td);
		applyDefaultPreferences.addSelectionListener(listener);
	}

	private void createGeneralExpandable(Composite composite,
			FormToolkit toolkit, SelectionListener listener) {

		ExpandableComposite exComposite = toolkit.createExpandableComposite(
				composite, ExpandableComposite.TREE_NODE);
		exComposite.setText("General");
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

		useTargetProjectToGuide = toolkit.createButton(innerComposite,
				"Use target project to guide annotation", SWT.CHECK);
		td = new TableWrapData(TableWrapData.LEFT);
		td.colspan = 2;
		useTargetProjectToGuide.setLayoutData(td);
		useTargetProjectToGuide.addSelectionListener(listener);

		getToolkit().paintBordersFor(innerComposite);

	}

	private void createUmlImportExpandable(Composite composite,
			FormToolkit toolkit, SelectionListener listener) {

		ExpandableComposite exComposite = toolkit.createExpandableComposite(
				composite, ExpandableComposite.TREE_NODE);
		exComposite.setText("UML Model Import");
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
		GridLayout glayout = new GridLayout();
		glayout.numColumns = 3;
		innerComposite.setLayout(glayout);
		exComposite.setClient(innerComposite);

		createUmlDatatypeMap(innerComposite, toolkit);

		getToolkit().paintBordersFor(innerComposite);
	}

	private void createUmlDatatypeMap(Composite composite, FormToolkit toolkit) {

		DefaultPageListener listener = new DefaultPageListener();

		Label l = toolkit.createLabel(composite, "UML Datatype Map");
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 3;
		l.setLayoutData(gd);

		umlMappingTableViewer = new TableViewer(composite, SWT.SINGLE
				| SWT.FULL_SELECTION);
		final Table table = umlMappingTableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setTopIndex(0);
		table.addSelectionListener(listener);
		GridData tableGD = new GridData(GridData.FILL_HORIZONTAL);
		tableGD.horizontalSpan = 2;
		tableGD.verticalSpan = 3;
		tableGD.heightHint = 110;
		table.setLayoutData(tableGD);
		String[] columnNames = new String[] { "UML Datatype", "Mapped Datatype" };
		int[] columnWidths = new int[] { 150, 150 };
		int[] columnAlignments = new int[] { SWT.LEFT, SWT.LEFT };

		for (int i = 0; i < columnAlignments.length; i++) {
			TableColumn tableColumn = new TableColumn(table,
					columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidths[i]);
		}

		umlMappingTableViewer.addDoubleClickListener(listener);
		umlMappingTableViewer
				.setLabelProvider(new UmlDatatypeMappingLabelProvider());
		umlMappingTableViewer
				.setContentProvider(new UmlDatatypeMappingContentProvider());
		ITigerstripeProject handle = getTSProject();

		try {
			umlDatatypeMapper = new UmlDatatypeMapper(handle);
			umlMappingTableViewer.setInput(umlDatatypeMapper);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		addUMLMappingButton = toolkit.createButton(composite, "Add", SWT.PUSH);
		GridData gdb = new GridData();
		addUMLMappingButton.setLayoutData(gdb);
		addUMLMappingButton.addSelectionListener(listener);

		editUMLMappingButton = toolkit
				.createButton(composite, "Edit", SWT.PUSH);
		editUMLMappingButton.setLayoutData(gdb);
		editUMLMappingButton.addSelectionListener(listener);

		removeUMLMappingButton = toolkit.createButton(composite, "Remove",
				SWT.PUSH);
		removeUMLMappingButton.setLayoutData(gdb);
		removeUMLMappingButton.addSelectionListener(listener);

		restoreDefaultUMLMappingsButton = toolkit.createButton(composite,
				"Restore defaults", SWT.PUSH);
		gdb = new GridData();
		gdb.horizontalSpan = 3;
		restoreDefaultUMLMappingsButton.setLayoutData(gdb);
		restoreDefaultUMLMappingsButton.addSelectionListener(listener);

		gdb = new GridData();
		importUMLMap = toolkit.createButton(composite, "Import map", SWT.PUSH);
		importUMLMap.setLayoutData(gdb);
		importUMLMap.addSelectionListener(listener);

		exportUMLMap = toolkit.createButton(composite, "Export map", SWT.PUSH);
		exportUMLMap.setLayoutData(gdb);
		exportUMLMap.addSelectionListener(listener);

		getToolkit().paintBordersFor(composite);

	}

	private void createDBImportExpandable(Composite composite,
			FormToolkit toolkit, SelectionListener listener) {

		ExpandableComposite exComposite = toolkit.createExpandableComposite(
				composite, ExpandableComposite.TREE_NODE);
		exComposite.setText("DB Import");
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
		GridLayout glayout = new GridLayout();
		glayout.numColumns = 3;
		innerComposite.setLayout(glayout);
		exComposite.setClient(innerComposite);

		importTables = toolkit.createButton(innerComposite, "Import Tables",
				SWT.CHECK);
		GridData gd = new GridData(GridData.BEGINNING);
		importTables.setLayoutData(gd);
		importTables.addSelectionListener(listener);

		importViews = toolkit.createButton(innerComposite, "Import Views",
				SWT.CHECK);
		gd = new GridData(GridData.BEGINNING);
		gd.horizontalSpan = 2;
		importViews.setLayoutData(gd);
		importViews.addSelectionListener(listener);

		createDBDatatypeMap(innerComposite, toolkit);
		getToolkit().paintBordersFor(innerComposite);

	}

	private void createDBDatatypeMap(Composite composite, FormToolkit toolkit) {

		DefaultPageListener listener = new DefaultPageListener();

		Label l = toolkit.createLabel(composite, "DB Datatype Map");
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 3;
		l.setLayoutData(gd);

		dbMappingTableViewer = new TableViewer(composite, SWT.SINGLE
				| SWT.FULL_SELECTION);
		final Table table = dbMappingTableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setTopIndex(0);
		table.addSelectionListener(listener);
		GridData tableGD = new GridData(GridData.FILL_HORIZONTAL);
		tableGD.horizontalSpan = 2;
		tableGD.verticalSpan = 3;
		tableGD.heightHint = 110;
		table.setLayoutData(tableGD);
		String[] columnNames = new String[] { "DB Datatype", "Mapped Datatype" };
		int[] columnWidths = new int[] { 150, 150 };
		int[] columnAlignments = new int[] { SWT.LEFT, SWT.LEFT };

		for (int i = 0; i < columnAlignments.length; i++) {
			TableColumn tableColumn = new TableColumn(table,
					columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidths[i]);
		}

		dbMappingTableViewer.addDoubleClickListener(listener);
		dbMappingTableViewer
				.setLabelProvider(new DBDatatypeMappingLabelProvider());
		dbMappingTableViewer
				.setContentProvider(new DBDatatypeMappingContentProvider());
		ITigerstripeProject handle = getTSProject();

		try {
			dbDatatypeMapper = new DBTypeMapper(null, handle, null);
			dbMappingTableViewer.setInput(dbDatatypeMapper);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		addDBMappingButton = toolkit.createButton(composite, "Add", SWT.PUSH);
		GridData gdb = new GridData();
		addDBMappingButton.setLayoutData(gdb);
		addDBMappingButton.addSelectionListener(listener);

		editDBMappingButton = toolkit.createButton(composite, "Edit", SWT.PUSH);
		editDBMappingButton.setLayoutData(gdb);
		editDBMappingButton.addSelectionListener(listener);

		removeDBMappingButton = toolkit.createButton(composite, "Remove",
				SWT.PUSH);
		removeDBMappingButton.setLayoutData(gdb);
		removeDBMappingButton.addSelectionListener(listener);

		restoreDefaultDBMappingsButton = toolkit.createButton(composite,
				"Restore defaults", SWT.PUSH);
		gdb = new GridData();
		gdb.horizontalSpan = 3;
		restoreDefaultDBMappingsButton.setLayoutData(gdb);
		restoreDefaultDBMappingsButton.addSelectionListener(listener);

		gdb = new GridData();
		importDBMap = toolkit.createButton(composite, "Import map", SWT.PUSH);
		importDBMap.setLayoutData(gdb);
		importDBMap.addSelectionListener(listener);

		exportDBMap = toolkit.createButton(composite, "Export map", SWT.PUSH);
		exportDBMap.setLayoutData(gdb);
		exportDBMap.addSelectionListener(listener);

		getToolkit().paintBordersFor(composite);

	}

	private void createPreferenceMsg(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		String data = "<form><p></p><p>To set default values to be reused across multiple Tigerstripe Projects, please use the <a href=\"preferences\">Tigerstripe Model Import Preferences</a> page.</p></form>";
		FormText rtext = toolkit.createFormText(parent, true);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		rtext.setLayoutData(td);
		rtext.setText(data, true, false);
		rtext.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				PreferenceDialog dialog = new PreferenceDialog(getBody()
						.getShell(), EclipsePlugin.getDefault().getWorkbench()
						.getPreferenceManager());
				dialog.setSelectedNode(ImportPreferencePage.PAGE_ID);
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

	public void commit(boolean onSave) {
		super.commit(onSave);
	}

	protected void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.

			AbstractTigerstripeProjectHandle handle = (AbstractTigerstripeProjectHandle) getTSProject();

			markPageModified();
		}
	}

	protected void markPageModified() {
		DescriptorEditor editor = (DescriptorEditor) getPage().getEditor();
		editor.pageModified();
	}

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

			useTargetProjectToGuide
					.setSelection("true"
							.equalsIgnoreCase(handle
									.getAdvancedProperty(IAdvancedProperties.PROP_IMPORT_USETARGETPROJECTASGUIDE)));

			importTables
					.setSelection("true"
							.equalsIgnoreCase(handle
									.getAdvancedProperty(IAdvancedProperties.PROP_IMPORT_DB_TABLES)));
			importViews
					.setSelection("true"
							.equalsIgnoreCase(handle
									.getAdvancedProperty(IAdvancedProperties.PROP_IMPORT_DB_VIEWS)));

			setSilentUpdate(false);

		} catch (TigerstripeException e) {
			Status status = new Status(
					IStatus.ERROR,
					EclipsePlugin.PLUGIN_ID,
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
							EclipsePlugin.PLUGIN_ID,
							222,
							"Error while applying default Advanced Preferences on Project",
							ee);
					EclipsePlugin.log(status);
				}
			}
		} else if (e.getSource() == useTargetProjectToGuide) {
			try {
				handle
						.setAdvancedProperty(
								IAdvancedProperties.PROP_IMPORT_USETARGETPROJECTASGUIDE,
								String.valueOf(useTargetProjectToGuide
										.getSelection()));
				markPageModified();
			} catch (TigerstripeException ee) {
				Status status = new Status(
						IStatus.ERROR,
						EclipsePlugin.PLUGIN_ID,
						222,
						"Error while setting "
								+ IAdvancedProperties.PROP_GENERATION_GenerateReport
								+ " advanced property on Project "
								+ handle.getProjectLabel(), ee);
				EclipsePlugin.log(status);
			}
		} else if (e.getSource() == addUMLMappingButton) {
			addMappingButtonPressed();
		} else if (e.getSource() == removeUMLMappingButton) {
			removeMappingButtonPressed();
		} else if (e.getSource() == editUMLMappingButton) {
			editMappingButtonPressed();
		} else if (e.getSource() == restoreDefaultUMLMappingsButton) {
			restoreDefaultMappingsButtonPressed();
		} else if (e.getSource() == importUMLMap) {
			importMapButtonPressed();
		} else if (e.getSource() == exportUMLMap) {
			exportMapButtonPressed();
		} else if (e.getSource() == addDBMappingButton) {
			addDBMappingButtonPressed();
		} else if (e.getSource() == removeDBMappingButton) {
			removeDBMappingButtonPressed();
		} else if (e.getSource() == editDBMappingButton) {
			editDBMappingButtonPressed();
		} else if (e.getSource() == restoreDefaultDBMappingsButton) {
			restoreDefaultDBMappingsButtonPressed();
		} else if (e.getSource() == importDBMap) {
			importDBMapButtonPressed();
		} else if (e.getSource() == exportDBMap) {
			exportDBMapButtonPressed();
		} else if (e.getSource() == importTables) {
			try {
				handle.setAdvancedProperty(
						IAdvancedProperties.PROP_IMPORT_DB_TABLES, String
								.valueOf(importTables.getSelection()));
				markPageModified();
			} catch (TigerstripeException ee) {
				Status status = new Status(IStatus.ERROR,
						EclipsePlugin.PLUGIN_ID, 222, "Error while setting "
								+ IAdvancedProperties.PROP_IMPORT_DB_TABLES
								+ " advanced property on Project "
								+ handle.getProjectLabel(), ee);
				EclipsePlugin.log(status);
			}
		} else if (e.getSource() == importViews) {
			try {
				handle.setAdvancedProperty(
						IAdvancedProperties.PROP_IMPORT_DB_VIEWS, String
								.valueOf(importViews.getSelection()));
				markPageModified();
			} catch (TigerstripeException ee) {
				Status status = new Status(IStatus.ERROR,
						EclipsePlugin.PLUGIN_ID, 222, "Error while setting "
								+ IAdvancedProperties.PROP_IMPORT_DB_VIEWS
								+ " advanced property on Project "
								+ handle.getProjectLabel(), ee);
				EclipsePlugin.log(status);
			}
		}
	}

	protected void importMapButtonPressed() {
		FileDialog dialog = new FileDialog(getBody().getShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.xml" });
		String path = dialog.open();
		if (path != null) {
			try {
				FileReader fReader = new FileReader(path);
				SAXReader sReader = new SAXReader();
				Document doc = sReader.read(fReader);
				umlDatatypeMapper.extractMap(doc.asXML());
				umlMappingTableViewer.refresh(true);
				markPageModified();
			} catch (FileNotFoundException e) {
				MessageDialog.openError(getBody().getShell(),
						"UML Datatype map import Error",
						"Could not find UML Datatype map to import (" + path
								+ ")");
				EclipsePlugin.log(e);
			} catch (DocumentException e) {
				MessageDialog.openError(getBody().getShell(),
						"UML Datatype map import Error",
						"Invalid UML Datatype map (" + path + ")");
				EclipsePlugin.log(e);
			} catch (TigerstripeException e) {
				MessageDialog.openError(getBody().getShell(),
						"UML Datatype map import Error",
						"Invalid UML Datatype map (" + path + ")");
				EclipsePlugin.log(e);
			}
		}
	}

	protected void exportMapButtonPressed() {
		FileDialog dialog = new FileDialog(getBody().getShell(), SWT.SAVE);
		dialog.setFilterExtensions(new String[] { "*.xml" });
		String path = dialog.open();
		if (path != null) {
			if (!path.endsWith(".xml")) {
				path.concat(".xml");
			}
			try {

				File file = new File(path);
				if (file.exists()) {
					if (!MessageDialog.openConfirm(getBody().getShell(),
							"Overwrite existing file",
							"Do you want to overwrite existing file?"))
						return;
				}
				FileWriter fWriter = new FileWriter(path);
				fWriter.append(umlDatatypeMapper.xmlizeMap());
				fWriter.flush();
				fWriter.close();
			} catch (IOException e) {
				MessageDialog.openError(getBody().getShell(),
						"UML Datatype map export Error",
						"Could not access file (" + path + ")");
				EclipsePlugin.log(e);
			} catch (TigerstripeException e) {
				MessageDialog.openError(getBody().getShell(),
						"UML Datatype map export Error",
						"Could not export UML Datatype map (" + path + ")");
				EclipsePlugin.log(e);
			}
		}
	}

	protected void restoreDefaultMappingsButtonPressed() {
		try {
			umlDatatypeMapper.extractMap(UmlDatatypeMapper.DEFAULTMAPPINGS);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		umlMappingTableViewer.refresh(true);
		markPageModified();
	}

	protected void addMappingButtonPressed() {
		UmlDatatypeMapping newMapping = new UmlDatatypeMapping();
		UmlDatatypeMapping[] existingMappings = umlDatatypeMapper.getMappings();
		UmlDatatypeMappingEditDialog dialog = new UmlDatatypeMappingEditDialog(
				getBody().getShell(), newMapping, Arrays
						.asList(existingMappings));
		if (dialog.open() == Window.OK) {
			UmlDatatypeMapping[] newMappings = new UmlDatatypeMapping[existingMappings.length + 1];
			for (int i = 0; i < existingMappings.length; i++) {
				newMappings[i] = existingMappings[i];
			}
			newMappings[existingMappings.length] = newMapping;

			try {
				umlDatatypeMapper.setMappings(newMappings);
				umlMappingTableViewer.refresh(true);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			markPageModified();
		}
	}

	protected void removeMappingButtonPressed() {
		int index = umlMappingTableViewer.getTable().getSelectionIndex();
		UmlDatatypeMapping mapping = umlDatatypeMapper.getMappings()[index];

		MessageBox dialog = new MessageBox(getSection().getShell(),
				SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		dialog.setText("Remove UMLDatatype Mapping");
		dialog.setMessage("Do you really want to remove the '"
				+ mapping.getUmlDatatype() + "' UMLDatatype mapping?");
		if (dialog.open() == SWT.YES) {
			UmlDatatypeMapping[] mappings = umlDatatypeMapper.getMappings();
			UmlDatatypeMapping[] newMappings = new UmlDatatypeMapping[mappings.length - 1];
			int newIndex = 0;
			for (int i = 0; i < mappings.length; i++) {
				if (i != index) {
					newMappings[newIndex++] = mappings[i];
				}
			}
			try {
				umlDatatypeMapper.setMappings(newMappings);
				umlMappingTableViewer.refresh(true);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			markPageModified();
			umlMappingTableViewer.getTable().redraw();
		}
	}

	protected void editMappingButtonPressed() {
		int index = umlMappingTableViewer.getTable().getSelectionIndex();
		UmlDatatypeMapping mapping = umlDatatypeMapper.getMappings()[index];
		UmlDatatypeMapping clone = new UmlDatatypeMapping(mapping);

		UmlDatatypeMapping[] existingMappings = umlDatatypeMapper.getMappings();
		List existingList = new ArrayList();
		existingList.addAll(Arrays.asList(existingMappings));
		existingList.remove(mapping);
		UmlDatatypeMappingEditDialog dialog = new UmlDatatypeMappingEditDialog(
				getBody().getShell(), clone, existingList);
		if (dialog.open() == Window.OK) {
			existingMappings[index] = clone;
			try {
				umlDatatypeMapper.setMappings(existingMappings);
				umlMappingTableViewer.refresh(true);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			umlMappingTableViewer.getTable().redraw();
			markPageModified();
		}
	}

	protected void importDBMapButtonPressed() {
		FileDialog dialog = new FileDialog(getBody().getShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.xml" });
		String path = dialog.open();
		if (path != null) {
			try {
				FileReader fReader = new FileReader(path);
				SAXReader sReader = new SAXReader();
				Document doc = sReader.read(fReader);
				dbDatatypeMapper.extractMap(doc.asXML());
				dbMappingTableViewer.refresh(true);
				markPageModified();
			} catch (FileNotFoundException e) {
				MessageDialog.openError(getBody().getShell(),
						"DB Datatype map import Error",
						"Could not find DB Datatype map to import (" + path
								+ ")");
				EclipsePlugin.log(e);
			} catch (DocumentException e) {
				MessageDialog.openError(getBody().getShell(),
						"DB Datatype map import Error",
						"Invalid DB Datatype map (" + path + ")");
				EclipsePlugin.log(e);
			} catch (TigerstripeException e) {
				MessageDialog.openError(getBody().getShell(),
						"DB Datatype map import Error",
						"Invalid DB Datatype map (" + path + ")");
				EclipsePlugin.log(e);
			}
		}
	}

	protected void exportDBMapButtonPressed() {
		FileDialog dialog = new FileDialog(getBody().getShell(), SWT.SAVE);
		dialog.setFilterExtensions(new String[] { "*.xml" });
		String path = dialog.open();
		if (path != null) {
			if (!path.endsWith(".xml")) {
				path.concat(".xml");
			}
			try {

				File file = new File(path);
				if (file.exists()) {
					if (!MessageDialog.openConfirm(getBody().getShell(),
							"Overwrite existing file",
							"Do you want to overwrite existing file?"))
						return;
				}
				FileWriter fWriter = new FileWriter(path);
				fWriter.append(dbDatatypeMapper.xmlizeMap());
				fWriter.flush();
				fWriter.close();
			} catch (IOException e) {
				MessageDialog.openError(getBody().getShell(),
						"DB Datatype map export Error",
						"Could not access file (" + path + ")");
				EclipsePlugin.log(e);
			} catch (TigerstripeException e) {
				MessageDialog.openError(getBody().getShell(),
						"DB Datatype map export Error",
						"Could not export DB Datatype map (" + path + ")");
				EclipsePlugin.log(e);
			}
		}
	}

	protected void restoreDefaultDBMappingsButtonPressed() {
		try {
			dbDatatypeMapper.extractMap(DBTypeMapper.DEFAULTMAPPINGS);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		dbMappingTableViewer.refresh(true);
		markPageModified();
	}

	protected void addDBMappingButtonPressed() {
		DBDatatypeMapping newMapping = new DBDatatypeMapping();
		DBDatatypeMapping[] existingMappings = dbDatatypeMapper.getMappings();
		DBDatatypeMappingEditDialog dialog = new DBDatatypeMappingEditDialog(
				getBody().getShell(), newMapping, Arrays
						.asList(existingMappings));
		if (dialog.open() == Window.OK) {
			DBDatatypeMapping[] newMappings = new DBDatatypeMapping[existingMappings.length + 1];
			for (int i = 0; i < existingMappings.length; i++) {
				newMappings[i] = existingMappings[i];
			}
			newMappings[existingMappings.length] = newMapping;

			try {
				dbDatatypeMapper.setMappings(newMappings);
				dbMappingTableViewer.refresh(true);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			markPageModified();
		}
	}

	protected void removeDBMappingButtonPressed() {
		int index = dbMappingTableViewer.getTable().getSelectionIndex();
		DBDatatypeMapping mapping = dbDatatypeMapper.getMappings()[index];

		MessageBox dialog = new MessageBox(getSection().getShell(),
				SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		dialog.setText("Remove DB Datatype Mapping");
		dialog.setMessage("Do you really want to remove the '"
				+ mapping.getDbDatatype() + "' DB Datatype mapping?");
		if (dialog.open() == SWT.YES) {
			DBDatatypeMapping[] mappings = dbDatatypeMapper.getMappings();
			DBDatatypeMapping[] newMappings = new DBDatatypeMapping[mappings.length - 1];
			int newIndex = 0;
			for (int i = 0; i < mappings.length; i++) {
				if (i != index) {
					newMappings[newIndex++] = mappings[i];
				}
			}
			try {
				dbDatatypeMapper.setMappings(newMappings);
				dbMappingTableViewer.refresh(true);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			markPageModified();
			dbMappingTableViewer.getTable().redraw();
		}
	}

	protected void editDBMappingButtonPressed() {
		int index = dbMappingTableViewer.getTable().getSelectionIndex();
		DBDatatypeMapping mapping = dbDatatypeMapper.getMappings()[index];
		DBDatatypeMapping clone = new DBDatatypeMapping(mapping);

		DBDatatypeMapping[] existingMappings = dbDatatypeMapper.getMappings();
		List existingList = new ArrayList();
		existingList.addAll(Arrays.asList(existingMappings));
		existingList.remove(mapping);
		DBDatatypeMappingEditDialog dialog = new DBDatatypeMappingEditDialog(
				getBody().getShell(), clone, existingList);
		if (dialog.open() == Window.OK) {
			existingMappings[index] = clone;
			try {
				dbDatatypeMapper.setMappings(existingMappings);
				dbMappingTableViewer.refresh(true);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			dbMappingTableViewer.getTable().redraw();
			markPageModified();
		}
	}

	private void applyAdvancedPropertiesDefaults() throws TigerstripeException {
		ITigerstripeProject handle = getTSProject();

		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();

		handle
				.setAdvancedProperty(
						IAdvancedProperties.PROP_IMPORT_USETARGETPROJECTASGUIDE,
						store
								.getString(IAdvancedProperties.PROP_IMPORT_USETARGETPROJECTASGUIDE));
		markPageModified();
	}
}
