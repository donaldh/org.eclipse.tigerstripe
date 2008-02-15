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

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.TSMessageDialog;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class RenameArtifactDialog extends TSMessageDialog {

	private IAbstractArtifact artifact;

	private IFile file;

	private StringDialogField artifactNameDialogField;

	private class ArtifactRenameAdapter implements IStringButtonAdapter,
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
			attributeFieldChanged(field);
		}

		public void doubleClicked(ListDialogField field) {
		}
	}

	public RenameArtifactDialog(Shell parentShell, IFile file,
			IAbstractArtifact artifact) {
		super(parentShell);

		this.artifact = artifact;
		this.file = file;

		ArtifactRenameAdapter adapter = new ArtifactRenameAdapter();

		artifactNameDialogField = new StringDialogField();
		artifactNameDialogField.setDialogFieldListener(adapter);
		artifactNameDialogField.setLabelText("Artifact Name:"); //$NON-NLS-1$
	}

	protected void setDefaultMessage() {
		setMessage("Specify new Artifact name");
	}

	protected boolean validateParam() {
		Button okButton = this.getButton(IDialogConstants.OK_ID);
		boolean okEnabled = true;

		// Check for valid name
		String parameterName = this.artifactNameDialogField.getText().trim();
		if (parameterName.charAt(0) >= '0' && parameterName.charAt(0) <= '9') {
			okEnabled = false;
			setMessage("Invalid Artifact Name.");
		}

		char[] invalidChars = { ' ', ';', ',', '-', '{', '}', '[', ']', '(',
				')', '+', '=', '|', '\\', '/', '?', '<', '>', '.', '`', '~',
				'!', '@', '#', '$', '%', '^', '&', '*' };

		for (int i = 0; i < invalidChars.length; i++) {
			if (parameterName.indexOf(invalidChars[i]) != -1) {
				okEnabled = false;
				setMessage("Invalid Artifact Name.");
			}
		}

		// Check for duplicates
		// TODO

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
		createArtifactNameControl(composite, nColumns);

		initDialog();

		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
		return area;
	}

	protected void initDialog() {
		if (artifact.getName() != null) {
			artifactNameDialogField.setText(artifact.getName());
		} else {
			artifactNameDialogField.setText("anArtifact");
		}

		getShell().setText("Rename Artifact");
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

	private void createArtifactNameControl(Composite composite, int nColumns) {
		artifactNameDialogField.doFillIntoGrid(composite, nColumns - 1);
		DialogField.createEmptySpace(composite);

		LayoutUtil.setWidthHint(artifactNameDialogField.getTextControl(null),
				getMaxFieldWidth());
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

		artifact.setName(artifactNameDialogField.getText().trim());

		IPath origPath = file.getProjectRelativePath();
		IPath destPath = new Path(artifact.getName() + ".java");

		try {
			file.move(destPath, true, null);

			IPath newPath = (IPath) origPath.clone();
			newPath = newPath.removeLastSegments(1);
			newPath = newPath.append(destPath);

			IFile destFile = file.getProject().getFile(newPath);
			if (destFile.exists()) {
				byte[] bytes = null;
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(buffer));

				artifact.write(writer);

				bytes = buffer.toByteArray();
				destFile.setContents(new ByteArrayInputStream(bytes), true,
						true, null);
			}
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}
}
