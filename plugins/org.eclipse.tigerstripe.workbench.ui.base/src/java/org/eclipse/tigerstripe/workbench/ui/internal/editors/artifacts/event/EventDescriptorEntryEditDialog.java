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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.event;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ComboDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventDescriptorEntry;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.TSMessageDialog;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class EventDescriptorEntryEditDialog extends TSMessageDialog {

	private final static String[] primitiveTypes = { "String", "int", "long",
			"float", "double", "boolean", "short", "byte", "char" };

	private EventDescriptorEntry entry;

	private StringDialogField labelField;

	private ComboDialogField typeComboField;

	private class Adapter implements IDialogFieldListener {

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
			attributeFieldChanged(field);
		}
	}

	private List existingEntries;

	public EventDescriptorEntryEditDialog(Shell parentShell,
			EventDescriptorEntry entry, List existingEntries) {
		super(parentShell);

		this.entry = entry;
		this.existingEntries = existingEntries;

		Adapter adapter = new Adapter();

		labelField = new StringDialogField();
		labelField.setDialogFieldListener(adapter);
		labelField.setLabelText("Label:"); //$NON-NLS-1$

		typeComboField = new ComboDialogField(SWT.DROP_DOWN);
		typeComboField.setLabelText("Type:");
		typeComboField.setItems(primitiveTypes);
		typeComboField.setDialogFieldListener(adapter);
	}

	protected void setDefaultMessage() {
		setMessage("Fill Event Selector Details");
	}

	protected boolean validateSelector() {
		Button okButton = this.getButton(IDialogConstants.OK_ID);
		boolean okEnabled = true;

		// Check for valid name
		if (!JavaConventions.validateFieldName(labelField.getText().trim())
				.isOK()) {
			okEnabled = false;
			setMessage("Invalid label");
		}

		// Check for duplicates
		for (Iterator iter = this.existingEntries.iterator(); iter.hasNext();) {
			EventDescriptorEntry ent = (EventDescriptorEntry) iter.next();
			if (ent.getLabel().equals(entry.getLabel())) {
				okEnabled = false;
				setMessage("Duplicate Selector Label.");
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

	@Override
	public Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FormLayout());

		initializeDialogUnits(parent);
		Composite composite = new Composite(area, SWT.NONE);
		int nColumns = 3;
		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		getShell().setText("Package-XSD Mapping Details");

		createMessageArea(composite, nColumns);
		createMappingControls(composite, nColumns);
		initDialog();
		return area;
	}

	protected void initDialog() {

		labelField.setText(entry.getLabel());

		String primitiveType = entry.getPrimitiveType();

		// Since 1.1 removed refs to java.lang.String. This is for backwards
		// compatibility
		if ("java.lang.String".equals(primitiveType)) {
			primitiveType = "String";
		}
		for (int i = 0; i < primitiveTypes.length; i++) {
			if (primitiveType.equalsIgnoreCase(primitiveTypes[i])) {
				typeComboField.selectItem(i);
			}
		}

		validateSelector();
	}

	/**
	 * Returns the recommended maximum width for text fields (in pixels). This
	 * method requires that createContent has been called before this method is
	 * call. Subclasses may override to change the maximum width for text
	 * fields.
	 * 
	 * @return the recommended maximum width for text fields.
	 */
	protected int getMaxFieldWidth() {
		return convertWidthInCharsToPixels(40);
	}

	private void createMappingControls(Composite composite, int nColumns) {
		labelField.doFillIntoGrid(composite, nColumns);
		typeComboField.doFillIntoGrid(composite, nColumns);

		LayoutUtil.setWidthHint(labelField.getTextControl(null),
				getMaxFieldWidth());

	}

	protected void attributeFieldChanged(DialogField field) {
		if (field == labelField) {
			entry.setLabel(labelField.getText().trim());
		} else if (field == typeComboField) {
			entry.setPrimitiveType(primitiveTypes[typeComboField
					.getSelectionIndex()]);
		}
		validateSelector();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		validateSelector();
	}

	@Override
	protected void okPressed() {
		super.okPressed();
	}
}
