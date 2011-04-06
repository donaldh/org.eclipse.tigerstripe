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
package org.eclipse.tigerstripe.workbench.ui.internal.dialogs;

import static org.eclipse.jface.layout.GridDataFactory.fillDefaults;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IEntryListStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.help.StereotypeHelpListener;

/**
 * Edit dialog that renders all attributes of Stereotype instance, and allows to
 * set there values
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class StereotypeInstanceEditDialog extends StatusDialog {

	private final IStereotypeInstance instance;
	private HelpListener helpListener;

	private Composite area;
	private Composite parentComposite;

	public StereotypeInstanceEditDialog(Shell parentShell,
			IStereotypeInstance instance) {
		super(parentShell);

		this.instance = instance;

		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
	}

	protected void setDefaultMessage() {
		updateStatus(new Status(IStatus.OK, EclipsePlugin.PLUGIN_ID,
				"Edit the details of this stereotype"));
	}

	@Override
	public Control createDialogArea(Composite parent) {
		parentComposite = parent;
		helpListener = new StereotypeHelpListener(
				instance.getCharacterizingStereotype());
		parentComposite.addHelpListener(helpListener);

		area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FillLayout());

		initializeDialogUnits(parent);
		Composite composite = new Composite(area, SWT.NONE);
		int nColumns = 2;
		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createAttributesControls(composite, nColumns);
		getShell().setText("Stereotype Details");

		setDefaultMessage();
		return area;
	}

	@Override
	public boolean close() {
		if (parentComposite != null) {
			parentComposite.removeHelpListener(helpListener);
		}
		return super.close();
	}

	protected void createAttributesControls(Composite composite, int nColumns) {

		Label l = new Label(composite, SWT.BOLD);
		l.setFont(new Font(null, "Courrier", 10, SWT.BOLD));
		l.setText(instance.getName());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.heightHint = 30;
		l.setLayoutData(gd);

		Composite border = new Composite(composite, SWT.BORDER);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		border.setLayout(layout);
		gd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL
				| GridData.GRAB_VERTICAL);
		gd.widthHint = 400;
		border.setLayoutData(gd);

		// We need to go thru the list of defined attributes for the stereotype
		// and render them. There are 4 types: String, checkbox or drop-downs,
		// or tables. We need to keep track of the attribute/widget to handle
		// changes and
		// updates appropriately.
		IStereotype stereotype = instance.getCharacterizingStereotype();
		for (IStereotypeAttribute attr : stereotype.getAttributes()) {
			int attrKind = attr.getKind();
			switch (attrKind) {
			case IStereotypeAttribute.STRING_ENTRY_KIND:
				if (attr.isArray()) {
					renderStringArrayEntryAttribute(border, attr);
				} else {
					renderStringEntryAttribute(border, attr);
				}
				break;
			case IStereotypeAttribute.CHECKABLE_KIND:
				renderCheckableAttribute(border, attr);
				break;
			case IStereotypeAttribute.ENTRY_LIST_KIND:
				if (!attr.isArray()) {
					renderEntryListAttribute(border, attr);
				} else {
					renderEntryArrayListAttribute(border, attr);
				}
				break;
			}
		}
	}

	class AttributeArrayContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IStereotypeAttribute) {
				IStereotypeAttribute attribute = (IStereotypeAttribute) inputElement;
				try {
					return instance.getAttributeValues(attribute);
				} catch (TigerstripeException e) {
					return new Object[0];
				}
			}
			return new Object[0];
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}

	class AttributeArrayLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			String attrValue = (String) obj;
			switch (index) {
			case 1:
				return emptySafe(attrValue);
			default:
				return emptySafe(attrValue);
			}
		}

		private String emptySafe(String attrValue) {
			return attrValue == null || attrValue.length() == 0 ? "<empty>"
					: attrValue;
		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}
	}

	@Override
	protected void okPressed() {
		if (area != null) {
			area.forceFocus();
		}
		super.okPressed();
	}

	protected void renderStringArrayEntryAttribute(Composite composite,
			final IStereotypeAttribute attribute) {

		Label l = new Label(composite, SWT.NULL);
		l.setText(attribute.getName());

		Composite subComposite = new Composite(composite, SWT.NULL);
		GridLayout gdLayout = new GridLayout();
		gdLayout.numColumns = 2;
		subComposite.setLayout(gdLayout);
		GridData gd = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		subComposite.setLayoutData(gd);

		Table m = new Table(subComposite, SWT.SINGLE | SWT.FULL_SELECTION
				| SWT.BORDER);
		m.setToolTipText(attribute.getDescription());
		GridData tdm = new GridData(GridData.FILL_BOTH);
		tdm.verticalSpan = 2;
		tdm.heightHint = 100;
		m.setLayoutData(tdm);

		TableColumn attributeValueColumn = new TableColumn(m, SWT.NULL);
		attributeValueColumn.setText("Value");
		attributeValueColumn.setWidth(150);

		m.setHeaderVisible(true);
		m.setLinesVisible(true);

		final TableViewer attributeArrayViewer = new TableViewer(m);
		attributeArrayViewer
				.setContentProvider(new AttributeArrayContentProvider());
		attributeArrayViewer
				.setLabelProvider(new AttributeArrayLabelProvider());

		attributeArrayViewer.setInput(attribute);

		m.pack();

		Composite buttonPanel = new Composite(subComposite, SWT.NONE);
		FillLayout layout = new FillLayout(SWT.VERTICAL);
		layout.spacing = 5;
		buttonPanel.setLayout(layout);
		fillDefaults().applyTo(buttonPanel);

		Button attributeArrayAddButton = new Button(buttonPanel, SWT.PUSH);
		attributeArrayAddButton.setText("Add");
		attributeArrayAddButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				attributeArrayAddButtonSelected(event, attributeArrayViewer,
						attribute);
			}
		});

		Button attributeArrayRemoveButton = new Button(buttonPanel, SWT.PUSH);
		attributeArrayRemoveButton.setText("Remove");
		attributeArrayRemoveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				attributeArrayRemoveButtonSelected(event, attributeArrayViewer,
						attribute);
			}
		});

		// Create the corresponding cell editors
		attributeArrayViewer.setColumnProperties(new String[] { "STEREOTYPE" });
		final TextCellEditor stereotypeEditor = new TextCellEditor(
				attributeArrayViewer.getTable());
		attributeArrayViewer
				.setCellEditors(new CellEditor[] { stereotypeEditor });
		attributeArrayViewer.setCellModifier(new StereotypeEditorCellModifier(
				attributeArrayViewer, attribute));

		attributeArrayViewer.refresh(true);
	}

	private class StereotypeEditorCellModifier implements ICellModifier {

		private final TableViewer viewer;

		private final IStereotypeAttribute attribute;

		public StereotypeEditorCellModifier(TableViewer viewer,
				IStereotypeAttribute attribute) {
			this.viewer = viewer;
			this.attribute = attribute;
		}

		public boolean canModify(Object element, String property) {
			if ("STEREOTYPE".equals(property))
				return true;

			return false;
		}

		public Object getValue(Object element, String property) {
			if ("STEREOTYPE".equals(property))
				return element;

			return null;
		}

		public void modify(Object item, String property, Object value) {

			// null indicates that the validator rejected the values
			if (value == null)
				return;

			int index = viewer.getTable().getSelectionIndex();
			try {
				String[] values = instance.getAttributeValues(attribute);
				values[index] = ((String) value).trim();
				instance.setAttributeValues(attribute, values);
				viewer.refresh(true);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	protected void attributeArrayRemoveButtonSelected(SelectionEvent event,
			TableViewer attributeArrayViewer, IStereotypeAttribute attribute) {
		TableItem[] selectedItems = attributeArrayViewer.getTable()
				.getSelection();
		String[] selectedLabels = new String[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedLabels[i] = (String) selectedItems[i].getData();
		}

		String message = "Do you really want to remove ";
		if (selectedLabels.length > 1) {
			message = message + "these " + selectedLabels.length + " values?";
		} else {
			message = message + "this value?";
		}

		MessageDialog msgDialog = new MessageDialog(getShell(),
				"Remove Attribute values", null, message,
				MessageDialog.QUESTION, new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {
			attributeArrayViewer.remove(selectedLabels);

			TableItem[] items = attributeArrayViewer.getTable().getItems();
			String[] newValues = new String[items.length];

			for (int i = 0; i < items.length; i++) {
				newValues[i] = (String) items[i].getData();
			}

			try {
				instance.setAttributeValues(attribute, newValues);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	protected void attributeArrayAddButtonSelected(SelectionEvent event,
			TableViewer attributeArrayViewer, IStereotypeAttribute attribute) {

		try {
			String[] values = instance.getAttributeValues(attribute);
			ArrayList<String> result = new ArrayList<String>(
					Arrays.asList(values));
			String defaultValue = attribute.getDefaultValue();
			if (defaultValue == null) {
				defaultValue = "";
			}
			result.add(defaultValue);
			String[] newValues = result.toArray(new String[result.size()]);
			instance.setAttributeValues(attribute, newValues);
			attributeArrayViewer.refresh(true);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	protected void renderStringEntryAttribute(Composite composite,
			final IStereotypeAttribute attribute) {
		Label l = new Label(composite, SWT.NULL);
		l.setText(attribute.getName());

		final Text text = new Text(composite, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		text.setLayoutData(gd);

		try {
			text.setText(instance.getAttributeValue(attribute));
			text.setToolTipText(attribute.getDescription());
		} catch (TigerstripeException e) {
			text.setText("");
		}
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				try {
					instance.setAttributeValue(attribute, text.getText());
				} catch (TigerstripeException ee) {
					// ignore here
				}
			}
		});
	}

	protected void renderCheckableAttribute(Composite composite,
			final IStereotypeAttribute attribute) {

		Label l = new Label(composite, SWT.NULL);
		final Button button = new Button(composite, SWT.CHECK);
		button.setText(attribute.getName());
		button.setToolTipText(attribute.getDescription());

		try {
			String value = instance.getAttributeValue(attribute);
			button.setSelection("true".equalsIgnoreCase(value)
					|| "0".equalsIgnoreCase(value)); // "0"
			// used
			// to
			// represent
			// 'checked'
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
		button.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				boolean sel = button.getSelection();
				try {
					instance.setAttributeValue(attribute, (sel ? "true"
							: "false"));
				} catch (TigerstripeException ee) {
					// ignore here
				}
			}
		});
	}

	protected void renderEntryListAttribute(Composite composite,
			final IStereotypeAttribute attribute) {

		Label l = new Label(composite, SWT.NULL);
		l.setText(attribute.getName());

		final Combo combo = new Combo(composite, SWT.READ_ONLY);
		IEntryListStereotypeAttribute attr = (IEntryListStereotypeAttribute) attribute;
		combo.setItems(attr.getSelectableValues());
		combo.setToolTipText(attribute.getDescription());

		try {
			String val = instance.getAttributeValue(attribute);
			int i = 0;
			int index = -1;
			for (String item : attr.getSelectableValues()) {
				if (item.equals(val)) {
					index = i;
				}
				i++;
			}
			if (index != -1) {
				combo.select(index);
			}
		} catch (TigerstripeException e) {

		}

		combo.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				int index = combo.getSelectionIndex();
				if (index != -1) {
					try {
						IEntryListStereotypeAttribute attr = (IEntryListStereotypeAttribute) attribute;
						instance.setAttributeValue(attribute,
								attr.getSelectableValues()[index]);
					} catch (TigerstripeException te) {
						EclipsePlugin.log(te);
					}
				}
			}
		});
	}

	protected void renderEntryArrayListAttribute(Composite composite,
			final IStereotypeAttribute attribute) {

		Label l = new Label(composite, SWT.NULL);
		l.setText(attribute.getName());

		IEntryListStereotypeAttribute attr = (IEntryListStereotypeAttribute) attribute;

		int style = SWT.CHECK | SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL;
		Table table = new Table(composite, style);
		table.setToolTipText(attribute.getDescription());

		table.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {

				if (event.detail == SWT.CHECK) {

					if (event.item instanceof TableItem) {
						try {

							TableItem tableItem = (TableItem) event.item;
							String[] attrValues = instance
									.getAttributeValues(attribute);
							ArrayList<String> attrValueList = new ArrayList<String>(
									Arrays.asList(attrValues));
							if (tableItem.getChecked() == true) {
								attrValueList.add(tableItem.getText());
							} else {
								attrValueList.remove(tableItem.getText());
							}
							instance.setAttributeValues(attribute,
									attrValueList.toArray(new String[0]));
						} catch (TigerstripeException e) {
							EclipsePlugin.log(e);
						}
					}
				}
			}
		});

		table.setLinesVisible(false);
		table.setHeaderVisible(false);

		TableColumn column = new TableColumn(table, SWT.NONE, 0);
		column.setWidth(300);

		try {

			String[] selectedValues = instance.getAttributeValues(attribute);
			for (String selectableValue : attr.getSelectableValues()) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(selectableValue);
				for (String selectedValue : selectedValues) {
					if (selectableValue.equals(selectedValue)) {
						item.setChecked(true);
						break;
					}
				}
			}

		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

	}

}
