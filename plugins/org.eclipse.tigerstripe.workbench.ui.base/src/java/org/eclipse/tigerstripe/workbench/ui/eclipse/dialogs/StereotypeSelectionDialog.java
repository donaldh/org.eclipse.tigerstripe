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

import java.util.List;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.TSMessageDialog;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class StereotypeSelectionDialog extends TSMessageDialog {

	private String initialStereotypeName;

	private StringButtonDialogField defNameDialogField;

	// private JavaTypeCompletionProcessor attributeClassCompletionProcessor;

	private class AttributesFieldsAdapter implements IStringButtonAdapter,
			IDialogFieldListener, IListAdapter {

		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
			attributeChangeControlPressed(field);
		}

		// -------- IListAdapter
		public void customButtonPressed(ListDialogField field, int index) {
		}

		public void selectionChanged(ListDialogField field) {
		}

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
			attributeFieldChanged(field);
		}

		public void doubleClicked(ListDialogField field) {
		}
	}

	private List<String> existingStereotypeNames;

	public StereotypeSelectionDialog(Shell parentShell,
			String initialStereotypeName, List<String> existingStereotypeNames,
			IWorkbenchProfile profile) {
		super(parentShell);

		this.initialStereotypeName = initialStereotypeName;
		this.existingStereotypeNames = existingStereotypeNames;

		AttributesFieldsAdapter adapter = new AttributesFieldsAdapter();

		defNameDialogField = new StringButtonDialogField(adapter);
		defNameDialogField.setDialogFieldListener(adapter);
		defNameDialogField.setButtonLabel("Browse");
		defNameDialogField.setLabelText("Annotation Name:"); //$NON-NLS-1$
	}

	protected void setDefaultMessage() {
		setMessage("Select Annotation Name");
	}

	protected boolean validateParam() {
		Button okButton = this.getButton(IDialogConstants.OK_ID);
		boolean okEnabled = true;

		// Check for duplicates
		for (String existing : this.existingStereotypeNames) {
			if (existing.equals(defNameDialogField.getText())) {
				okEnabled = false;
				setMessage("Duplicate Annotation Selection.");
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

		createMessageArea(composite, nColumns);
		createStereotypeNameControl(composite, nColumns);

		initDialog();

		return area;
	}

	protected void initDialog() {
		if (initialStereotypeName != null
				&& initialStereotypeName.length() != 0) {
			defNameDialogField.setText(initialStereotypeName);
		} else {
			defNameDialogField.setText("anAnnotation");
		}

		getShell().setText("Annotation Name Selection");
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

	private void createStereotypeNameControl(Composite composite, int nColumns) {
		defNameDialogField.doFillIntoGrid(composite, nColumns);
		DialogField.createEmptySpace(composite);

		LayoutUtil.setWidthHint(defNameDialogField.getTextControl(null),
				getMaxFieldWidth());
	}

	protected void attributeChangeControlPressed(DialogField field) {
		if (field == defNameDialogField) {
			defNameDialogField.setText(chooseStereotype());
		}
		validateParam();
	}

	protected void attributeFieldChanged(DialogField field) {
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

		// populate the attribute
		initialStereotypeName = defNameDialogField.getText();
	}

	private String chooseStereotype() {

		// try {
		// IPackageFragmentRoot[] roots = javaProject
		// .getAllPackageFragmentRoots();
		// IJavaElement[] elements = roots;
		//			
		// //TODO restrict the elements to those implementing
		// ITigerstripeContextEntry
		//			
		// IJavaSearchScope scope = SearchEngine
		// .createJavaSearchScope(elements);
		//
		// TypeSelectionDialog2 dialog = new TypeSelectionDialog2(getShell(),
		// false, null, scope, IJavaSearchConstants.TYPE);
		// dialog
		// .setTitle(NewWizardMessages
		// .getString("NewArtifactWizardPage.attributes.SuperClassDialog.title"));
		// //$NON-NLS-1$
		// dialog
		// .setMessage(NewWizardMessages
		// .getString("NewArtifactWizardPage.attributes.SuperClassDialog.message"));
		// //$NON-NLS-1$
		// dialog.setFilter(getAttributeClass());
		//
		// if (dialog.open() == Window.OK) {
		// return ((IType) dialog.getFirstResult())
		// .getFullyQualifiedName();
		// }
		// } catch (JavaModelException e) {
		// TigerstripeRuntime.logErrorMessage("JavaModelException detected", e);
		// EclipsePlugin.log(e);
		// }
		return "";
	}

	public String getStereotypeName() {
		return initialStereotypeName;
	}
}
