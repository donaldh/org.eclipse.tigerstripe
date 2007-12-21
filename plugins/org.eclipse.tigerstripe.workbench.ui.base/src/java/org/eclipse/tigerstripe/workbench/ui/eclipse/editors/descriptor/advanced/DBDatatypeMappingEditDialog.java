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

import java.util.Iterator;
import java.util.List;

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
import org.eclipse.tigerstripe.core.model.importing.db.mapper.DBDatatypeMapping;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.TSMessageDialog;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DBDatatypeMappingEditDialog extends TSMessageDialog {

	private DBDatatypeMapping mapping;

	private StringDialogField dbDatatypeField;
	private StringDialogField mappedDatatypeField;

	private class AttributesFieldsAdapter implements IDialogFieldListener {

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
			attributeFieldChanged(field);
		}
	}

	private List existingMappings;

	public DBDatatypeMappingEditDialog(Shell parentShell,
			DBDatatypeMapping mapping, List existingMappings) {
		super(parentShell);

		this.mapping = mapping;
		this.existingMappings = existingMappings;

		AttributesFieldsAdapter adapter = new AttributesFieldsAdapter();

		dbDatatypeField = new StringDialogField();
		dbDatatypeField.setDialogFieldListener(adapter);
		dbDatatypeField.setLabelText("DB Datatype:"); //$NON-NLS-1$

		mappedDatatypeField = new StringDialogField();
		mappedDatatypeField.setDialogFieldListener(adapter);
		mappedDatatypeField.setLabelText("Mapped Datatype:"); //$NON-NLS-1$

	}

	protected void setDefaultMessage() {
		setMessage("Specify DB Datatype Mapping Details");
	}

	protected boolean validateMapping() {
		Button okButton = this.getButton(IDialogConstants.OK_ID);
		boolean okEnabled = true;

		if ("".equals(mapping.getDbDatatype())) {
			okEnabled = false;
			setErrorMessage("Invalid DB datatype");
			return okEnabled;
		}

		// Check for valid name
		if (!lightValidate(mapping.getMappedDatatype())) {
			okEnabled = false;
			setErrorMessage("Invalid target datatype");
		}
		// if
		// (JavaConventions.validateJavaTypeName(mapping.getMappedDatatype()).getSeverity()
		// == IStatus.ERROR ) {
		// if ( !validateJavaScalarTypeName(mapping.getMappedDatatype())) {
		// okEnabled = false;
		// setErrorMessage("Invalid target datatype");
		// }
		// }

		// Check for duplicates
		for (Iterator iter = this.existingMappings.iterator(); iter.hasNext();) {
			DBDatatypeMapping map = (DBDatatypeMapping) iter.next();
			if (map.getDbDatatype().equals(mapping.getDbDatatype())) {
				okEnabled = false;
				setErrorMessage("Duplicate DB Datatype mapping.");
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

	private String[] scalars = { "int", "float", "long", "short", "double",
			"char", "boolean", "byte" };

	protected boolean validateJavaScalarTypeName(String name) {
		for (String sc : scalars) {
			if (name.startsWith(sc))
				return true;
		}
		return false;
	}

	private final static String[] illegalStrings = { " ", ",", ";", ":", "'",
			"\"", "(", ")", "{", "}", "*", "&", "^", "%", "$", "#", "@", "!",
			"?", "<", ">", "`", "~", "|", "/", "\\", "+", "=", "-" };

	protected boolean lightValidate(String name) {

		if ("".equals(name))
			return false;

		for (String il : illegalStrings) {
			if (name.indexOf(il) != -1)
				return false;
		}
		return true;
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

		getShell().setText("DB Datatype Mapping Details");

		createMessageArea(composite, nColumns);
		createMappingControls(composite, nColumns);
		initDialog();
		return area;
	}

	protected void initDialog() {

		dbDatatypeField.setText(mapping.getDbDatatype());
		mappedDatatypeField.setText(mapping.getMappedDatatype());

		validateMapping();
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
		dbDatatypeField.doFillIntoGrid(composite, nColumns);

		DialogField.createEmptySpace(composite, nColumns);
		mappedDatatypeField.doFillIntoGrid(composite, nColumns);

		LayoutUtil.setWidthHint(dbDatatypeField.getTextControl(null),
				getMaxFieldWidth());
		LayoutUtil.setWidthHint(mappedDatatypeField.getTextControl(null),
				getMaxFieldWidth());
	}

	protected void attributeFieldChanged(DialogField field) {
		if (field == dbDatatypeField) {
			mapping.setDbDatatype(dbDatatypeField.getText().trim());
		} else if (field == mappedDatatypeField) {
			mapping.setMappedDatatype(mappedDatatypeField.getText().trim());
		}
		validateMapping();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		validateMapping();
	}

	@Override
	protected void okPressed() {
		super.okPressed();
	}
}
