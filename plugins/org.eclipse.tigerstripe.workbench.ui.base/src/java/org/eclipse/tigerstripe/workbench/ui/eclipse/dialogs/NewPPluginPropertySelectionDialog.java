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

import org.eclipse.jdt.internal.ui.wizards.dialogfields.ComboDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.pluggable.GeneratorProjectHandle;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.TSMessageDialog;

/**
 * Presents a dialog to select a PPlugin property and forces a name
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class NewPPluginPropertySelectionDialog extends TSMessageDialog {

	private StringDialogField propertyNameField;

	private ComboDialogField propertyTypeCombo;

	private ITigerstripeGeneratorProject ppProject;

	private IPluginProperty result;

	private class PropertySelectionAdapter implements IStringButtonAdapter,
			IDialogFieldListener, IListAdapter {

		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
		}

		// -------- IListAdapter
		public void customButtonPressed(ListDialogField field, int index) {
		}

		public void selectionChanged(ListDialogField field) {
		}

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
			propertyFieldChanged(field);
		}

		public void doubleClicked(ListDialogField field) {
		}
	}

	public NewPPluginPropertySelectionDialog(Shell parentShell,
			String initialPropertyName, ITigerstripeGeneratorProject ppProject) {
		super(parentShell);

		this.ppProject = ppProject;

		PropertySelectionAdapter adapter = new PropertySelectionAdapter();

		propertyNameField = new StringDialogField();
		propertyNameField.setDialogFieldListener(adapter);
		propertyNameField.setLabelText("Property Name:"); //$NON-NLS-1$
		propertyNameField.setText(initialPropertyName);

		propertyTypeCombo = new ComboDialogField(SWT.READ_ONLY);
		String[] labels = ((GeneratorProjectHandle) ppProject)
				.getSupportedPluginPropertyLabels();
		propertyTypeCombo.setItems(labels);
		propertyTypeCombo.setLabelText("Property Type:");
		propertyTypeCombo.setDialogFieldListener(adapter);
		propertyTypeCombo.selectItem(0);
	}

	protected void setDefaultMessage() {
		setMessage("Define new property details");
	}

	protected boolean validateParam() {
		Button okButton = this.getButton(IDialogConstants.OK_ID);
		boolean okEnabled = true;

		// Check for valid name
		String parameterName = this.propertyNameField.getText().trim();
		if (parameterName.charAt(0) >= '0' && parameterName.charAt(0) <= '9') {
			okEnabled = false;
			setMessage("Invalid property Name.");
		}

		char[] invalidChars = { ' ', ';', ',', '-', '{', '}', '[', ']', '(',
				')', '+', '=', '|', '\\', '/', '?', '<', '>', '.', '`', '~',
				'!', '@', '#', '$', '%', '^', '&', '*' };

		for (int i = 0; i < invalidChars.length; i++) {
			if (parameterName.indexOf(invalidChars[i]) != -1) {
				okEnabled = false;
				setMessage("Invalid property Name.");
			}
		}

		// Check for duplicates
		try {
			IPluginProperty[] existingProperties = ppProject
					.getGlobalProperties();
			for (IPluginProperty prop : existingProperties) {
				if (prop.getName().equals(parameterName)) {
					okEnabled = false;
					setMessage("Duplicate property");
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
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

		createMessageArea(composite, nColumns);
		createPropertyNameControl(composite, nColumns);
		createPropertyTypeControl(composite, nColumns);

		initDialog();

		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
		return area;
	}

	protected void initDialog() {
		getShell().setText("New Plugin Property");
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
	protected int getMaxFieldWidth() {
		return convertWidthInCharsToPixels(40);
	}

	private void createPropertyNameControl(Composite composite, int nColumns) {
		propertyNameField.doFillIntoGrid(composite, nColumns - 1);
		DialogField.createEmptySpace(composite);

		LayoutUtil.setWidthHint(propertyNameField.getTextControl(null),
				getMaxFieldWidth());
	}

	private void createPropertyTypeControl(Composite composite, int nColumns) {
		propertyTypeCombo.doFillIntoGrid(composite, nColumns - 1);
		DialogField.createEmptySpace(composite);
	}

	protected void propertyFieldChanged(DialogField field) {
		validateParam();
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
		super.okPressed();

		Class<IPluginProperty>[] supportedTypes = ppProject
				.getSupportedProperties();
		try {
			result = ppProject.makeProperty(supportedTypes[propertyTypeCombo
					.getSelectionIndex()]);

			result.setName(propertyNameField.getText().trim());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	public IPluginProperty getNewPPluginProperty() {
		return result;
	}
}
