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
package org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IEntryListStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.TSMessageDialog;

/**
 * Edit dialog that renders all attributes of Stereotype instance, and allows to
 * set there values
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class StereotypeInstanceEditDialog extends TSMessageDialog {

	private IStereotypeInstance instance;

	public StereotypeInstanceEditDialog(Shell parentShell,
			IStereotypeInstance instance) {
		super(parentShell);

		this.instance = instance;

		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
	}

	protected void setDefaultMessage() {
		setMessage("Edit the details of this annotation");
	}

	@Override
	public Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FillLayout());

		initializeDialogUnits(parent);
		Composite composite = new Composite(area, SWT.NONE);
		int nColumns = 2;
		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createMessageArea(composite, nColumns);
		createAttributesControls(composite, nColumns);
		getShell().setText("Annotation Details");

		setDefaultMessage();
		return area;
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
		gd.widthHint = 150;
		border.setLayoutData(gd);

		// we need to go thru the list of defined attributes for the stereotype
		// and render them
		// There are 3 types: String, checkbox or drop-downs
		// We need to keep track of the attribute/widget to handle changes and
		// updates
		// appropriately.
		IStereotype stereotype = instance.getCharacterizingIStereotype();
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
				renderEntryListAttribute(border, attr);
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
				return attrValue;
			default:
				return attrValue;
			}
		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}
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

		Table m = new Table(subComposite, SWT.SINGLE | SWT.FULL_SELECTION);
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

		Button attributeArrayAddButton = new Button(subComposite, SWT.PUSH);
		attributeArrayAddButton.setText("Add");
		attributeArrayAddButton.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		attributeArrayAddButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				attributeArrayAddButtonSelected(event, attributeArrayViewer,
						attribute);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		Button attributeArrayRemoveButton = new Button(subComposite, SWT.PUSH);
		attributeArrayRemoveButton.setText("Remove");
		attributeArrayRemoveButton
				.addSelectionListener(new SelectionListener() {
					public void widgetSelected(SelectionEvent event) {
						attributeArrayRemoveButtonSelected(event,
								attributeArrayViewer, attribute);
					}

					public void widgetDefaultSelected(SelectionEvent event) {
						// empty
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

		private TableViewer viewer;

		private IStereotypeAttribute attribute;

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
			ArrayList<String> result = new ArrayList<String>(Arrays
					.asList(values));
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
					|| "0".equalsIgnoreCase(value)); // "0" used to represent
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
						instance.setAttributeValue(attribute, attr
								.getSelectableValues()[index]);
					} catch (TigerstripeException ee) {
						// ignore here
					}
				}
			}
		});
	}

}
