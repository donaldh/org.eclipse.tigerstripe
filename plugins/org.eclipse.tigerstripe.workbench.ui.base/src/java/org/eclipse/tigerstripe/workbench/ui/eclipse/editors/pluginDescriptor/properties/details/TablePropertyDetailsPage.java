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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.properties.details;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.api.external.plugins.IextTablePPluginProperty.ColumnDef;
import org.eclipse.tigerstripe.api.plugins.pluggable.ITablePPluginProperty;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class TablePropertyDetailsPage extends BasePropertyDetailsPage {

	private TableViewer columnDefListViewer;

	private Button addColumnDef;

	private Button removeColumnDef;

	private Button upColumnDef;

	private Button downColumnDef;

	private boolean contentCreated = false;

	private class ColumnDefListener implements SelectionListener,
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

	private List<ColumnDef> getColumnDefs() {
		return ((ITablePPluginProperty) getIPluggablePluginProperty())
				.getColumnDefs();
	}

	protected void updateButtonState() {
		removeColumnDef.setEnabled(columnDefListViewer.getTable()
				.getSelectionCount() != 0);
		upColumnDef.setEnabled(columnDefListViewer.getTable()
				.getSelectionCount() == 1
				&& columnDefListViewer.getTable().getSelectionIndex() != 0);
		downColumnDef
				.setEnabled(columnDefListViewer.getTable().getSelectionCount() == 1
						&& columnDefListViewer.getTable().getSelectionIndex() != getColumnDefs()
								.size() - 1);
	}

	@Override
	protected void updateForm() {
		super.updateForm();

		if (contentCreated) {
			columnDefListViewer.setInput(getColumnDefs());
			updateButtonState();
		}
	}

	protected void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == addColumnDef) {
			addColumnDefSelected();
			pageModified();
		} else if (e.getSource() == removeColumnDef) {
			removeColumnDefSelected();
			pageModified();
		} else if (e.getSource() == upColumnDef) {
			upColumnDefSelected();
			pageModified();
		} else if (e.getSource() == downColumnDef) {
			downColumnDefSelected();
			pageModified();
		}
	}

	protected void handleViewerSelectionChanged(SelectionChangedEvent e) {
		updateButtonState();
	}

	private void upColumnDefSelected() {
		int index = columnDefListViewer.getTable().getSelectionIndex();
		ColumnDef current = getColumnDefs().remove(index);
		getColumnDefs().add(index - 1, current);
		updateForm();
	}

	private void downColumnDefSelected() {
		int index = columnDefListViewer.getTable().getSelectionIndex();
		ColumnDef current = getColumnDefs().remove(index);
		getColumnDefs().add(index + 1, current);
		updateForm();
	}

	private void addColumnDefSelected() {
		ColumnDef newEntry = new ColumnDef();
		newEntry.columnName = findNewEntryName();
		getColumnDefs().add(newEntry);
		columnDefListViewer.add(newEntry);
		columnDefListViewer.setInput(getColumnDefs());
		columnDefListViewer.refresh(true);
	}

	private void removeColumnDefSelected() {
		TableItem[] selectedItems = columnDefListViewer.getTable()
				.getSelection();
		ColumnDef[] selectedFields = new ColumnDef[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (ColumnDef) selectedItems[i].getData();
		}

		String message = "Do you really want to remove ";
		if (selectedFields.length > 1) {
			message = message + "these " + selectedFields.length
					+ " column definitions?";
		} else {
			message = message + "this entry?";
		}

		MessageDialog msgDialog = new MessageDialog(form.getForm().getShell(),
				"Remove column definition", null, message,
				MessageDialog.QUESTION, new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == Window.OK) {
			for (ColumnDef ent : selectedFields) {
				getColumnDefs().remove(ent);
			}
			columnDefListViewer.remove(selectedFields);
			columnDefListViewer.setInput(getColumnDefs());
			columnDefListViewer.refresh(true);
		}
	}

	private int newFieldCount;

	/**
	 * Finds a new field name
	 */
	protected String findNewEntryName() {
		String result = "column" + newFieldCount++;

		// make sure we're not creating a duplicate
		TableItem[] items = columnDefListViewer.getTable().getItems();
		for (int i = 0; i < items.length; i++) {
			String name = ((ColumnDef) items[i].getData()).columnName;
			if (result.equals(name))
				return findNewEntryName();
		}
		return result;
	}

	public void createContents(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);
		TableWrapData td = new TableWrapData(TableWrapData.FILL);
		td.heightHint = 200;
		parent.setLayoutData(td);

		ColumnDefListener listener = new ColumnDefListener();
		Composite sectionClient = createPropertyInfo(parent);

		Label l = toolkit.createLabel(sectionClient, "Column Names:");
		GridData gdt = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gdt.verticalSpan = 4;
		l.setLayoutData(gdt);

		Table t = new Table(sectionClient, SWT.BORDER | SWT.MULTI
				| SWT.FULL_SELECTION);
		gdt = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL
				| GridData.GRAB_VERTICAL);
		gdt.heightHint = 150;
		gdt.verticalSpan = 4;
		t.setLayoutData(gdt);

		columnDefListViewer = new TableViewer(t);
		t.setToolTipText("This is the list of columns for the table property.");
		columnDefListViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object obj) {
				ColumnDef en = (ColumnDef) obj;
				return en.columnName;
			}
		});
		columnDefListViewer.addSelectionChangedListener(listener);
		columnDefListViewer.setContentProvider(new ArrayContentProvider() {
			@Override
			public Object[] getElements(Object inputElement) {
				return getColumnDefs().toArray();
			}
		});

		columnDefListViewer.setColumnProperties(new String[] { "LABEL" });
		final TextCellEditor entryListCellEditor = new TextCellEditor(
				columnDefListViewer.getTable());
		columnDefListViewer
				.setCellEditors(new CellEditor[] { entryListCellEditor });
		columnDefListViewer.setCellModifier(new ColumnDefListCellModifier(
				columnDefListViewer));

		columnDefListViewer.refresh(true);

		addColumnDef = toolkit.createButton(sectionClient, "Add", SWT.PUSH);
		GridData gdb = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.VERTICAL_ALIGN_BEGINNING);
		addColumnDef.setLayoutData(gdb);
		addColumnDef.addSelectionListener(listener);

		upColumnDef = toolkit.createButton(sectionClient, "Up", SWT.PUSH);
		gdb = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.VERTICAL_ALIGN_BEGINNING);
		upColumnDef.setLayoutData(gdb);
		upColumnDef.addSelectionListener(listener);

		downColumnDef = toolkit.createButton(sectionClient, "Down", SWT.PUSH);
		gdb = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.VERTICAL_ALIGN_BEGINNING);
		downColumnDef.setLayoutData(gdb);
		downColumnDef.addSelectionListener(listener);

		removeColumnDef = toolkit.createButton(sectionClient, "Remove",
				SWT.PUSH);
		gdb = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.VERTICAL_ALIGN_BEGINNING);
		removeColumnDef.setLayoutData(gdb);
		removeColumnDef.addSelectionListener(listener);

		contentCreated = true;
		form.getToolkit().paintBordersFor(parent);
	}

	private class ColumnDefListCellModifier implements ICellModifier {

		private TableViewer viewer;

		public ColumnDefListCellModifier(TableViewer viewer) {
			this.viewer = viewer;
		}

		public boolean canModify(Object element, String property) {
			if ("LABEL".equals(property))
				return true;

			return false;
		}

		public Object getValue(Object element, String property) {
			if ("LABEL".equals(property)) {
				ColumnDef entry = (ColumnDef) element;
				return entry.columnName;
			}

			return null;
		}

		public void modify(Object item, String property, Object value) {

			// null indicates that the validator rejected the values
			if (value == null)
				return;

			int index = viewer.getTable().getSelectionIndex();
			ColumnDef ent = getColumnDefs().get(index);
			ent.columnName = (String) value;
			viewer.refresh(true);
		}
	}

}
