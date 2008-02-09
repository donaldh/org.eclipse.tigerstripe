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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.plugins.renderer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.plugins.IPluggablePluginPropertyListener;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.ITablePluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.ITablePluginProperty.ColumnDef;
import org.eclipse.tigerstripe.workbench.plugins.ITablePluginProperty.TablePropertyRow;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class TablePropertyRenderer extends BasePropertyRenderer {

	private List<TablePropertyRow> rows = new ArrayList<TablePropertyRow>();

	private TableViewer tableViewer;

	private Button addEntry;

	private Button removeEntry;

	private Button upEntry;

	private Button downEntry;

	private class EntryListener implements SelectionListener,
			ISelectionChangedListener {
		public void widgetDefaultSelected(SelectionEvent e) {
		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}

		public void selectionChanged(SelectionChangedEvent event) {
			handleViewerSelectionChanged(event);
		}

	}

	public TablePropertyRenderer(Composite parent, FormToolkit toolkit,
			ITigerstripeModelProject project, IPluginProperty property,
			IPluggablePluginPropertyListener persister) {
		super(parent, toolkit, project, property, persister);
	}

	@Override
	public void setEnabled(boolean enabled) {
		tableViewer.getTable().setEnabled(enabled);
	}

	private void handleViewerSelectionChanged(SelectionChangedEvent e) {
		updateButtonState();
	}

	protected void updateButtonState() {
		removeEntry.setEnabled(tableViewer.getTable().getSelectionCount() != 0);
		upEntry.setEnabled(tableViewer.getTable().getSelectionCount() == 1
				&& tableViewer.getTable().getSelectionIndex() != 0);
		downEntry
				.setEnabled(tableViewer.getTable().getSelectionCount() == 1
						&& tableViewer.getTable().getSelectionIndex() != rows
								.size() - 1);
	}

	@Override
	public void applyDefault() {
		// try {
		// IBooleanPPluginProperty sProp = (IBooleanPPluginProperty)
		// getProperty();
		// propButton.setSelection(sProp.getDefaultBoolean());
		// getPersister().storeProperty(sProp,
		// Boolean.valueOf(propButton.getSelection()));
		// } catch (TigerstripeException e) {
		// EclipsePlugin.log(e);
		// }
	}

	protected void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == addEntry) {
			addEntrySelected();
		} else if (e.getSource() == upEntry) {
			upEntrySelected();
		} else if (e.getSource() == downEntry) {
			downEntrySelected();
		} else if (e.getSource() == removeEntry) {
			removeEntrySelected();
		}
	}

	private void removeEntrySelected() {
		TableItem[] selectedItems = tableViewer.getTable().getSelection();
		TablePropertyRow[] selectedFields = new TablePropertyRow[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (TablePropertyRow) selectedItems[i].getData();
		}

		String message = "Do you really want to remove ";
		if (selectedFields.length > 1) {
			message = message + "these " + selectedFields.length + " rows?";
		} else {
			message = message + "this row?";
		}

		MessageDialog msgDialog = new MessageDialog(getParent().getShell(),
				"Remove column definition", null, message,
				MessageDialog.QUESTION, new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == Window.OK) {
			for (TablePropertyRow ent : selectedFields) {
				rows.remove(ent);
			}
			tableViewer.remove(selectedFields);
			ITablePluginProperty sProp = (ITablePluginProperty) getProperty();
			try {
				getPersister().storeProperty(sProp, rows);
			} catch (TigerstripeException ee) {
				EclipsePlugin.log(ee);
			}
			updateForm();
		}
	}

	private void addEntrySelected() {
		ITablePluginProperty sProp = (ITablePluginProperty) getProperty();

		TablePropertyRow row = sProp.makeRow();
		rows.add(row);
		try {
			getPersister().storeProperty(sProp, rows);
		} catch (TigerstripeException ee) {
			EclipsePlugin.log(ee);
		}
		updateForm();
	}

	private void upEntrySelected() {
		int index = tableViewer.getTable().getSelectionIndex();
		TablePropertyRow current = rows.remove(index);
		rows.add(index - 1, current);
		ITablePluginProperty sProp = (ITablePluginProperty) getProperty();
		try {
			getPersister().storeProperty(sProp, rows);
		} catch (TigerstripeException ee) {
			EclipsePlugin.log(ee);
		}
		updateForm();
	}

	private void downEntrySelected() {
		int index = tableViewer.getTable().getSelectionIndex();
		TablePropertyRow current = rows.remove(index);
		rows.add(index + 1, current);
		ITablePluginProperty sProp = (ITablePluginProperty) getProperty();
		try {
			getPersister().storeProperty(sProp, rows);
		} catch (TigerstripeException ee) {
			EclipsePlugin.log(ee);
		}
		updateForm();
	}

	@Override
	public void render() throws TigerstripeException {
		ITablePluginProperty sProp = (ITablePluginProperty) getProperty();

		Composite parent = getParent();
		FormToolkit toolkit = getToolkit();
		EntryListener listener = new EntryListener();

		Label l = toolkit.createLabel(parent, sProp.getName());
		GridData gdt = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.VERTICAL_ALIGN_BEGINNING);
		gdt.heightHint = 150;
		gdt.verticalSpan = 4;
		l.setLayoutData(gdt);

		Table t = new Table(parent, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		t.setLinesVisible(true);
		t.setHeaderVisible(true);
		String[] columnNames = new String[sProp.getColumnDefs().size()];
		int i = 0;
		for (ColumnDef def : sProp.getColumnDefs()) {
			TableColumn column = new TableColumn(t, SWT.NULL);
			column.setText(def.columnName);
			column.pack();
			columnNames[i++] = def.columnName;
		}
		t.pack();

		gdt = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL
				| GridData.GRAB_VERTICAL);
		gdt.heightHint = 150;
		gdt.verticalSpan = 4;
		t.setLayoutData(gdt);

		tableViewer = new TableViewer(t);
		tableViewer.setLabelProvider(new ITableLabelProvider() {

			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			public String getColumnText(Object element, int columnIndex) {
				TablePropertyRow row = (TablePropertyRow) element;
				return row.getValues()[columnIndex];
			}

			public void addListener(ILabelProviderListener listener) {
			}

			public void dispose() {
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void removeListener(ILabelProviderListener listener) {
			}
		});
		tableViewer.addSelectionChangedListener(listener);
		tableViewer.setContentProvider(new ArrayContentProvider() {
			@Override
			public Object[] getElements(Object inputElement) {
				List<TablePropertyRow> tableRows = (List<TablePropertyRow>) inputElement;
				return tableRows.toArray();
			}
		});
		tableViewer.setColumnProperties(columnNames);
		final TextCellEditor entryListCellEditor = new TextCellEditor(
				tableViewer.getTable());

		CellEditor[] cellEditors = new CellEditor[columnNames.length];
		for (int index = 0; index < columnNames.length; index++) {
			cellEditors[index] = entryListCellEditor;
		}
		tableViewer.setCellEditors(cellEditors);
		tableViewer.setCellModifier(new RowCellModifier(tableViewer));

		addEntry = toolkit.createButton(parent, "Add", SWT.PUSH);
		GridData gdb = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.VERTICAL_ALIGN_BEGINNING);
		addEntry.setLayoutData(gdb);
		addEntry.addSelectionListener(listener);

		upEntry = toolkit.createButton(parent, "Up", SWT.PUSH);
		gdb = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.VERTICAL_ALIGN_BEGINNING);
		upEntry.setLayoutData(gdb);
		upEntry.addSelectionListener(listener);

		downEntry = toolkit.createButton(parent, "Down", SWT.PUSH);
		gdb = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.VERTICAL_ALIGN_BEGINNING);
		downEntry.setLayoutData(gdb);
		downEntry.addSelectionListener(listener);

		removeEntry = toolkit.createButton(parent, "Remove", SWT.PUSH);
		gdb = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.VERTICAL_ALIGN_BEGINNING);
		removeEntry.setLayoutData(gdb);
		removeEntry.addSelectionListener(listener);
	}

	private void updateForm() {
		tableViewer.setInput(rows);
		tableViewer.refresh();
		updateButtonState();
	}

	@Override
	public void update(String serializedValue) {
		setSilentUpdate(true);
		rows = null;
		rows = (List<TablePropertyRow>) getProperty().deSerialize(
				serializedValue);
		updateForm();
		setSilentUpdate(false);
	}

	private class RowCellModifier implements ICellModifier {

		private TableViewer viewer;

		public RowCellModifier(TableViewer viewer) {
			this.viewer = viewer;
		}

		public boolean canModify(Object element, String property) {
			return true;
		}

		public Object getValue(Object element, String property) {
			TablePropertyRow row = (TablePropertyRow) element;
			return row.getValue(property);
		}

		public void modify(Object item, String property, Object value) {

			// null indicates that the validator rejected the values
			if (value == null)
				return;

			int index = viewer.getTable().getSelectionIndex();
			TablePropertyRow row = rows.get(index);
			row.setValue(property, (String) value);
			try {
				getPersister().storeProperty(getProperty(), rows);
				viewer.refresh(true);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

}
