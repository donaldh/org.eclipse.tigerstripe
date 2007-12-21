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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.plugins;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.internal.ui.refactoring.nls.PackageSelectionDialogButtonField;
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
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.plugin.PackageToSchemaMapper.PckXSDMapping;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.TSMessageDialog;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PackageMappingEditDialog extends TSMessageDialog {

	private PckXSDMapping mapping;

	private StringDialogField namespaceDialogField;

	private StringDialogField locationDialogField;

	private StringDialogField schemaNameDialogField;

	private StringDialogField userPrefixDialogField;

	private PackageSelectionDialogButtonField packageDialogField;

	private class AttributesFieldsAdapter implements IDialogFieldListener {

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
			attributeFieldChanged(field);
		}
	}

	private List existingMappings;

	public PackageMappingEditDialog(Shell parentShell, PckXSDMapping mapping,
			List existingMappings, ITigerstripeProject tsProject) {
		super(parentShell);

		this.mapping = mapping;
		this.existingMappings = existingMappings;

		AttributesFieldsAdapter adapter = new AttributesFieldsAdapter();

		IJavaProject prj = EclipsePlugin.getIJavaProject(tsProject);
		packageDialogField = new PackageSelectionDialogButtonField("Package:",
				"Browse", new TSPackageBrowseAdapter(prj), prj);
		packageDialogField.setDialogFieldListener(adapter);
		packageDialogField.setLabelText("Package:"); //$NON-NLS-1$

		schemaNameDialogField = new StringDialogField();
		schemaNameDialogField.setDialogFieldListener(adapter);
		schemaNameDialogField.setLabelText("Schema Name:"); //$NON-NLS-1$

		namespaceDialogField = new StringDialogField();
		namespaceDialogField.setDialogFieldListener(adapter);
		namespaceDialogField.setLabelText("Namespace:"); //$NON-NLS-1$

		locationDialogField = new StringDialogField();
		locationDialogField.setDialogFieldListener(adapter);
		locationDialogField.setLabelText("Location:"); //$NON-NLS-1$

		userPrefixDialogField = new StringDialogField();
		userPrefixDialogField.setDialogFieldListener(adapter);
		userPrefixDialogField.setLabelText("Prefix:"); //$NON-NLS-1$

	}

	protected void setDefaultMessage() {
		setMessage("Specify Package Mapping Details");
	}

	protected boolean validateMapping() {
		Button okButton = this.getButton(IDialogConstants.OK_ID);
		boolean okEnabled = true;

		// Check for valid name
		if (JavaConventions.validatePackageName(mapping.getPackage())
				.getSeverity() == IStatus.ERROR) {
			okEnabled = false;
			setErrorMessage("Invalid package definition");
			// In case this is called before the button is created
			if (okButton != null) {
				okButton.setEnabled(okEnabled);
			}
			return okEnabled;
		}

		// Check for duplicates
		for (Iterator iter = this.existingMappings.iterator(); iter.hasNext();) {
			PckXSDMapping map = (PckXSDMapping) iter.next();
			if (map.getPackage().equals(mapping.getPackage())) {
				okEnabled = false;
				setErrorMessage("Duplicate package mapping.");
				// In case this is called before the button is created
				if (okButton != null) {
					okButton.setEnabled(okEnabled);
				}
				return okEnabled;
			}
		}

		if (this.schemaNameDialogField.getText().trim().length() == 0) {
			okEnabled = false;
			setErrorMessage("Invalid schema name.");
			// In case this is called before the button is created
			if (okButton != null) {
				okButton.setEnabled(okEnabled);
			}
			return okEnabled;
		}

		if (this.namespaceDialogField.getText().trim().length() == 0) {
			okEnabled = false;
			setErrorMessage("Invalid namespace.");
			// In case this is called before the button is created
			if (okButton != null) {
				okButton.setEnabled(okEnabled);
			}
			return okEnabled;
		}

		if (this.locationDialogField.getText().trim().length() == 0) {
			okEnabled = false;
			setErrorMessage("Invalid location.");
			// In case this is called before the button is created
			if (okButton != null) {
				okButton.setEnabled(okEnabled);
			}
			return okEnabled;
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

		packageDialogField.setText(mapping.getPackage());
		namespaceDialogField.setText(mapping.getTargetNamespace());
		schemaNameDialogField.setText(mapping.getXsdName());
		locationDialogField.setText(mapping.getXsdLocation());
		userPrefixDialogField.setText(mapping.getUserPrefix());

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
		packageDialogField.doFillIntoGrid(composite, nColumns);

		DialogField.createEmptySpace(composite, nColumns);
		schemaNameDialogField.doFillIntoGrid(composite, nColumns);
		namespaceDialogField.doFillIntoGrid(composite, nColumns);
		locationDialogField.doFillIntoGrid(composite, nColumns);
		userPrefixDialogField.doFillIntoGrid(composite, nColumns);

		LayoutUtil.setWidthHint(packageDialogField.getTextControl(null),
				getMaxFieldWidth());
		LayoutUtil.setWidthHint(namespaceDialogField.getTextControl(null),
				getMaxFieldWidth());
		LayoutUtil.setWidthHint(locationDialogField.getTextControl(null),
				getMaxFieldWidth());
		LayoutUtil.setWidthHint(schemaNameDialogField.getTextControl(null),
				getMaxFieldWidth());
		LayoutUtil.setWidthHint(userPrefixDialogField.getTextControl(null),
				getMaxFieldWidth());
	}

	protected void attributeFieldChanged(DialogField field) {
		if (field == namespaceDialogField) {
			mapping.setTargetNamespace(namespaceDialogField.getText().trim());
		} else if (field == packageDialogField) {
			mapping.setPackage(packageDialogField.getText().trim());
		} else if (field == schemaNameDialogField) {
			mapping.setXsdName(schemaNameDialogField.getText().trim());
		} else if (field == locationDialogField) {
			mapping.setXsdLocation(locationDialogField.getText().trim());
		} else if (field == userPrefixDialogField) {
			mapping.setuserPrefix(userPrefixDialogField.getText().trim());
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
