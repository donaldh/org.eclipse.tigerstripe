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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.StereotypeAttributeFactory;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IEntryListStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.TSMessageDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ColorUtils;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class StereotypeAttributeEditDialog extends TSMessageDialog {

	private boolean isEdit = false;

	private List<Entry> entries = new ArrayList<Entry>();

	public void setEdit(boolean edit) {
		this.isEdit = edit;
	}

	private class Entry {
		public String label;

		public boolean isDefault;

		public String getText() {
			String result = label;
			if (isDefault) {
				result += "(default)";
			}
			return result;
		}

		@Override
		public boolean equals(Object other) {
			if (other instanceof Entry) {
				Entry ent = (Entry) other;
				return label.equals(ent.label);
			}
			return false;
		}
	}

	private IStereotypeAttribute attribute;

	private int currentKind;

	private Text nameText;

	private Text descriptionText;

	private Button isArray;

	// String Entry related fields
	private Button stringEntryTypeButton;

	private Label stringEntryDefaultLabel;

	private Text stringEntryDefaultText;

	// Checkable entry related fields
	private Button checkableTypeButton;

	private Label checkableTypeDefaultLabel;

	private Combo checkableTypeDefaultCombo;

	// Entry list entry related fields
	private Label entryListChoicesLabel;

	private Button entryListTypeButton;

	private TableViewer entryListViewer;

	private Button addEntryButton;

	private Button setDefaultEntryButton;

	private Button removeEntryButton;

	private List<IStereotypeAttribute> existingStereotypeAttributes;

	private class AttributeWidgetListener implements SelectionListener,
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

	protected void handleViewerSelectionChanged(SelectionChangedEvent e) {
		int count = entryListViewer.getTable().getSelectionCount();
		if (count == 0) {
			removeEntryButton.setEnabled(false);
			setDefaultEntryButton.setEnabled(false);
		} else {
			removeEntryButton.setEnabled(true);
			setDefaultEntryButton.setEnabled(true);
		}
	}

	public StereotypeAttributeEditDialog(Shell parentShell,
			IStereotypeAttribute attribute,
			List<IStereotypeAttribute> existingStereotypeAttributes,
			IStereotype stereotype) {
		super(parentShell);

		this.attribute = attribute;
		this.existingStereotypeAttributes = existingStereotypeAttributes;
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);

	}

	protected void setDefaultMessage() {
		setMessage("Edit Annotation Attribute");
	}

	protected boolean validateParam() {
		Button okButton = this.getButton(IDialogConstants.OK_ID);
		boolean okEnabled = true;

		// Check for duplicates
		if (!isEdit) {
			for (IStereotypeAttribute attr : existingStereotypeAttributes) {
				if (nameText != null) {
					if (attr.getName().equals(nameText.getText().trim())) {
						okEnabled = false;
						setErrorMessage("Duplicate attribute name");
					}
				}
			}
		}

		if (nameText != null) {
			if (nameText.getText().trim().length() == 0) {
				okEnabled = false;
				setErrorMessage("Please enter valid attribute name");
			}
		}

		// In case this is called before the button is created
		if (okButton != null) {
			okButton.setEnabled(okEnabled);
		}

		if (okEnabled) {
			setDefaultMessage();
		}
		return okEnabled;
	}

	protected void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == stringEntryTypeButton) {
			setKind(IStereotypeAttribute.STRING_ENTRY_KIND);
		} else if (e.getSource() == checkableTypeButton) {
			setKind(IStereotypeAttribute.CHECKABLE_KIND);
		} else if (e.getSource() == entryListTypeButton) {
			setKind(IStereotypeAttribute.ENTRY_LIST_KIND);
		} else if (e.getSource() == addEntryButton) {
			addEntrySelected();
		} else if (e.getSource() == setDefaultEntryButton) {
			setDefaultEntrySelected();
		} else if (e.getSource() == removeEntryButton) {
			removeEntrySelected();
		} else if (e.getSource() == isArray) {
			isArraySelected();
		}
		validateParam();
	}

	protected void isArraySelected() {
		attribute.setArray(isArray.getSelection());

		if (isArray.getSelection()) {
			// Attribute of type boolean and selection list are not allowed for
			// arrays
			setKind(IStereotypeAttribute.STRING_ENTRY_KIND);

			checkableTypeButton.setEnabled(false);
			entryListTypeButton.setEnabled(false);
		} else {
			checkableTypeButton.setEnabled(true);
			entryListTypeButton.setEnabled(true);
		}

	}

	private int newFieldCount;

	/**
	 * Finds a new field name
	 */
	protected String findNewEntryName() {
		String result = "anEntry" + newFieldCount++;

		// make sure we're not creating a duplicate
		TableItem[] items = entryListViewer.getTable().getItems();
		for (int i = 0; i < items.length; i++) {
			String name = ((Entry) items[i].getData()).label;
			if (result.equals(name))
				return findNewEntryName();
		}
		return result;
	}

	private void addEntrySelected() {
		Entry newEntry = new Entry();
		newEntry.label = findNewEntryName();
		if (entryListViewer.getTable().getItems().length == 0) {
			newEntry.isDefault = true;
		}
		entries.add(newEntry);
		entryListViewer.add(newEntry);
		entryListViewer.setInput(entries);
		entryListViewer.refresh(true);
	}

	protected void removeEntrySelected() {
		TableItem[] selectedItems = entryListViewer.getTable().getSelection();
		Entry[] selectedFields = new Entry[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (Entry) selectedItems[i].getData();
		}

		String message = "Do you really want to remove ";
		if (selectedFields.length > 1) {
			message = message + "these " + selectedFields.length + " entries?";
		} else {
			message = message + "this entry?";
		}

		MessageDialog msgDialog = new MessageDialog(getShell(), "Remove Entry",
				null, message, MessageDialog.QUESTION, new String[] { "Yes",
						"No" }, 1);

		if (msgDialog.open() == Window.OK) {
			for (Entry ent : selectedFields) {
				entries.remove(ent);
			}
			entryListViewer.remove(selectedFields);
			entryListViewer.setInput(entries);
			entryListViewer.refresh(true);
		}
	}

	private void setDefaultEntrySelected() {
		TableItem[] allItems = entryListViewer.getTable().getItems();
		for (TableItem item : allItems) {
			Entry ent = (Entry) item.getData();
			ent.isDefault = false;
		}

		TableItem[] selectedItems = entryListViewer.getTable().getSelection();
		if (selectedItems.length > 0) {
			Entry ent = (Entry) selectedItems[0].getData();
			ent.isDefault = true;
		}

		entryListViewer.refresh(true);
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
		createStereotypeAttributeControl(composite, nColumns);

		initDialog();

		return area;
	}

	protected void resetControls() {
		stringEntryDefaultLabel.setEnabled(false);
		stringEntryDefaultText.setEnabled(false);

		checkableTypeDefaultCombo.setEnabled(false);
		checkableTypeDefaultLabel.setEnabled(false);

		entryListViewer.getTable().setForeground(ColorUtils.LIGHT_GREY);
		entryListChoicesLabel.setEnabled(false);
		addEntryButton.setEnabled(false);
		setDefaultEntryButton.setEnabled(false);
		removeEntryButton.setEnabled(false);
	}

	protected void setKind(int kind) {
		resetControls();

		currentKind = kind;

		switch (kind) {
		case IStereotypeAttribute.STRING_ENTRY_KIND:
			stringEntryDefaultLabel.setEnabled(true);
			stringEntryDefaultText.setEnabled(true);
			stringEntryTypeButton.setSelection(true);
			checkableTypeButton.setSelection(false);
			entryListTypeButton.setSelection(false);
			break;
		case IStereotypeAttribute.CHECKABLE_KIND:
			checkableTypeDefaultCombo.setEnabled(true);
			checkableTypeDefaultLabel.setEnabled(true);
			stringEntryTypeButton.setSelection(false);
			checkableTypeButton.setSelection(true);
			entryListTypeButton.setSelection(false);
			break;

		case IStereotypeAttribute.ENTRY_LIST_KIND:
			entryListViewer.getTable().setForeground(ColorUtils.BLACK);
			entryListChoicesLabel.setEnabled(true);
			addEntryButton.setEnabled(true);
			// setDefaultEntryButton.setEnabled(true); //enabled by selection in
			// the viewer
			// removeEntryButton.setEnabled(true); //enabled by selection in the
			// viewer

			stringEntryTypeButton.setSelection(false);
			checkableTypeButton.setSelection(false);
			entryListTypeButton.setSelection(true);
			break;
		}
	}

	protected void initDialog() {
		currentKind = attribute.getKind();
		setKind(currentKind);

		if (attribute.getName() != null) {
			nameText.setText(attribute.getName());
		}

		if (attribute.getDescription() != null) {
			descriptionText.setText(attribute.getDescription());
		}

		isArray.setSelection(attribute.isArray());
		isArraySelected(); // update form properly Bug 467

		if (currentKind == IStereotypeAttribute.STRING_ENTRY_KIND) {
			if (attribute.getDefaultValue() != null) {
				stringEntryDefaultText.setText(attribute.getDefaultValue());
			}
		} else if (currentKind == IStereotypeAttribute.CHECKABLE_KIND) {
			int index = "true".equals(attribute.getDefaultValue()) ? 0 : 1;
			checkableTypeDefaultCombo.select(index);
		} else if (currentKind == IStereotypeAttribute.ENTRY_LIST_KIND) {
			IEntryListStereotypeAttribute attr = (IEntryListStereotypeAttribute) attribute;
			String[] values = attr.getSelectableValues();
			entries.clear();
			for (String value : values) {
				Entry ent = new Entry();
				ent.label = value;
				if (value.equals(attribute.getDefaultValue())) {
					ent.isDefault = true;
				} else {
					ent.isDefault = false;
				}
				entries.add(ent);
			}
			entryListViewer.setInput(entries);
		}

		getShell().setText("Annotation Attribute Edit");
		validateParam();
	}

	/**
	 * Returns the recommended maximum width for text fields (in pixels). This
	 * method requires that createContent has been called before this method is
	 * call. Subclasses may override to change the maximum width for text
	 * fields.
	 * 
	 * @return the recommended maximum width for text fields.
	 */
	// protected int getMaxFieldWidth() {
	// return convertWidthInCharsToPixels(40);
	// }
	private void createStereotypeAttributeControl(Composite composite,
			int nColumns) {

		AttributeWidgetListener listener = new AttributeWidgetListener();

		Label l = new Label(composite, SWT.BOLD);
		l.setText("Name:");
		nameText = new Text(composite, SWT.BORDER);
		GridData g = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		nameText.setLayoutData(g);
		nameText
				.setToolTipText("The name of the attribute as presented to the end-user.");
		nameText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validateParam();
			}

		});

		l = new Label(composite, SWT.BOLD);
		l.setText("Short Description");
		descriptionText = new Text(composite, SWT.BORDER | SWT.V_SCROLL);
		g = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		g.heightHint = 40;
		descriptionText.setLayoutData(g);
		descriptionText
				.setToolTipText("This short description will be presented as a tooltip to the end-user when hovering over this attribute.");

		new Label(composite, SWT.NULL);
		isArray = new Button(composite, SWT.CHECK);
		isArray.addSelectionListener(listener);
		isArray.setText("is array?");

		Label separator = new Label(composite, SWT.LINE_SOLID);
		g = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		g.horizontalSpan = 2;
		g.heightHint = 10;
		separator.setLayoutData(g);

		Group box = new Group(composite, SWT.NULL);
		box.setText("Attribute type");
		GridData bgd = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		bgd.horizontalSpan = 2;
		box.setLayoutData(bgd);
		GridLayout bLayout = new GridLayout();
		bLayout.numColumns = 2;
		box.setLayout(bLayout);

		// String entry box
		stringEntryTypeButton = new Button(box, SWT.RADIO);
		stringEntryTypeButton.setText("String Entry Attribute");
		g = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		g.horizontalSpan = 2;
		stringEntryTypeButton.setLayoutData(g);
		stringEntryTypeButton.addSelectionListener(listener);
		stringEntryTypeButton
				.setToolTipText("A String Entry Attribute is presented as a free-text field to the end-user.");

		Composite stringEntryComposite = new Composite(box, SWT.NULL);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.horizontalIndent = 15;
		stringEntryComposite.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		stringEntryComposite.setLayout(layout);
		stringEntryDefaultLabel = new Label(stringEntryComposite, SWT.NULL);
		stringEntryDefaultLabel.setText("Default value:");
		stringEntryDefaultText = new Text(stringEntryComposite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		stringEntryDefaultText.setLayoutData(gd);

		// Checkable box
		checkableTypeButton = new Button(box, SWT.RADIO);
		checkableTypeButton.setText("'Checkable' Attribute");
		g = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		g.horizontalSpan = 2;
		checkableTypeButton.setLayoutData(g);
		checkableTypeButton.addSelectionListener(listener);
		checkableTypeButton
				.setToolTipText("A checkable attribute is presented as a checkbox to the end-user");

		Composite checkableComposite = new Composite(box, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		g = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		g.horizontalSpan = 2;
		g.horizontalIndent = 15;
		checkableComposite.setLayoutData(g);
		layout = new GridLayout();
		layout.numColumns = 2;
		checkableComposite.setLayout(layout);
		checkableTypeDefaultLabel = new Label(checkableComposite, SWT.NULL);
		checkableTypeDefaultLabel.setText("Default Value:");
		checkableTypeDefaultCombo = new Combo(checkableComposite, SWT.READ_ONLY);
		checkableTypeDefaultCombo.setItems(new String[] { "Checked",
				"Not Checked" });

		// EntryList box
		entryListTypeButton = new Button(box, SWT.RADIO);
		entryListTypeButton.setText("Selection List Attribute");
		g = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		g.horizontalSpan = 2;
		entryListTypeButton.setLayoutData(g);
		entryListTypeButton.addSelectionListener(listener);
		entryListTypeButton
				.setToolTipText("A Selection List Attribute is presented as a fixed drop-down list of values to the end-user.");

		Composite entryListComposite = new Composite(box, SWT.NULL);
		g = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		g.horizontalSpan = 2;
		g.horizontalIndent = 15;
		entryListComposite.setLayoutData(g);
		TableWrapLayout tlayout = new TableWrapLayout();
		tlayout.numColumns = 2;
		entryListComposite.setLayout(tlayout);

		entryListChoicesLabel = new Label(entryListComposite, SWT.NULL);
		entryListChoicesLabel.setText("List of valid choices");
		TableWrapData twd = new TableWrapData();
		twd.colspan = 2;
		entryListChoicesLabel.setLayoutData(twd);

		Table t = new Table(entryListComposite, SWT.BORDER | SWT.MULTI
				| SWT.FULL_SELECTION);
		TableWrapData gdt = new TableWrapData(TableWrapData.FILL_GRAB);
		gdt.grabHorizontal = true;
		gdt.grabVertical = true;
		gdt.heightHint = 150;
		gdt.rowspan = 3;
		t.setLayoutData(gdt);
		entryListViewer = new TableViewer(t);
		t
				.setToolTipText("This is the list of valid choices that will be presented to the end-user");
		entryListViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object obj) {
				Entry en = (Entry) obj;
				return en.getText();
			}
		});
		entryListViewer.addSelectionChangedListener(listener);
		entryListViewer.setContentProvider(new ArrayContentProvider() {
			@Override
			public Object[] getElements(Object inputElement) {
				return entries.toArray();
			}
		});
		entryListViewer.setSorter(new ViewerSorter());
		entryListViewer.setInput(entries);

		entryListViewer.setColumnProperties(new String[] { "LABEL" });
		final TextCellEditor entryListCellEditor = new TextCellEditor(
				entryListViewer.getTable());
		entryListViewer
				.setCellEditors(new CellEditor[] { entryListCellEditor });
		entryListViewer.setCellModifier(new EntryListCellModifier(
				entryListViewer));

		entryListViewer.refresh(true);

		addEntryButton = new Button(entryListComposite, SWT.PUSH);
		addEntryButton.setText("Add");
		TableWrapData gdb = new TableWrapData(TableWrapData.FILL);
		addEntryButton.setLayoutData(gdb);
		addEntryButton.addSelectionListener(listener);

		setDefaultEntryButton = new Button(entryListComposite, SWT.PUSH);
		setDefaultEntryButton.setText("Set Default");
		setDefaultEntryButton.addSelectionListener(listener);

		removeEntryButton = new Button(entryListComposite, SWT.PUSH);
		removeEntryButton.setText("Remove");
		gdb = new TableWrapData(TableWrapData.FILL);
		removeEntryButton.setLayoutData(gdb);
		removeEntryButton.addSelectionListener(listener);
	}

	private class EntryListCellModifier implements ICellModifier {

		private TableViewer viewer;

		public EntryListCellModifier(TableViewer viewer) {
			this.viewer = viewer;
		}

		public boolean canModify(Object element, String property) {
			if ("LABEL".equals(property))
				return true;

			return false;
		}

		public Object getValue(Object element, String property) {
			if ("LABEL".equals(property)) {
				Entry entry = (Entry) element;
				return entry.label;
			}

			return null;
		}

		public void modify(Object item, String property, Object value) {

			// null indicates that the validator rejected the values
			if (value == null)
				return;

			Entry theEntry = (Entry) viewer.getTable().getSelection()[0].getData();
			if (theEntry != null) {
				theEntry.label = (String) value;
				viewer.refresh(true);
			}
		}
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		validateParam();
	}

	@Override
	protected void okPressed() {
		// At this point we create a new attribute if the type of attr has
		// changed or
		// update the existing one otherwise
		if (currentKind != attribute.getKind()) {
			// need a new type
			attribute = StereotypeAttributeFactory.makeAttribute(currentKind);
		}

		attribute.setName(nameText.getText().trim());
		attribute.setDescription(descriptionText.getText().trim());
		if (currentKind == IStereotypeAttribute.STRING_ENTRY_KIND) {
			attribute.setDefaultValue(stringEntryDefaultText.getText().trim());
		} else if (currentKind == IStereotypeAttribute.CHECKABLE_KIND) {
			int index = checkableTypeDefaultCombo.getSelectionIndex();
			attribute.setDefaultValue(index == 0 ? "true" : "false");
		} else if (currentKind == IStereotypeAttribute.ENTRY_LIST_KIND) {
			IEntryListStereotypeAttribute attr = (IEntryListStereotypeAttribute) attribute;
			String[] values = new String[entries.size()];
			int i = 0;
			for (Entry ent : entries) {
				values[i++] = ent.label;
				if (ent.isDefault) {
					attr.setDefaultValue(ent.label);
				}
			}
			try {
				attr.setSelectableValues(values);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
		super.okPressed();
	}

	public IStereotypeAttribute getStereotypeAttribute() {
		return attribute;
	}

}
