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
package org.eclipse.tigerstripe.workbench.ui.eclipse.elements;

import java.util.List;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.LabelsSelectionDialog;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.NewArtifactWizardPage;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.enums.LabelRef;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.enums.LabelRefsListLabelProvider;

public class LabelListDialog extends TSMessageDialog {

	private ListDialogField labelListField;
	private NewArtifactWizardPage parentWizardPage;

	private class LabelsListAdapter implements IStringButtonAdapter,
			IDialogFieldListener, IListAdapter {

		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
		}

		// -------- IListAdapter
		public void customButtonPressed(ListDialogField field, int index) {
			labelListDialogCustomButtonPressed(field, index);
		}

		public void selectionChanged(ListDialogField field) {
		}

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
		}

		public void doubleClicked(ListDialogField field) {
		}
	}

	public LabelListDialog(Shell parentShell, List labelRefsList,
			NewArtifactWizardPage parentWizardPage) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);

		this.parentWizardPage = parentWizardPage;
		LabelsListAdapter adapter = new LabelsListAdapter();
		// The attribute list selection
		String[] addButtons = new String[] { /* 0 */"add", //$NON-NLS-1$
				/* 1 */"edit", /* 2 */"remove" //$NON-NLS-1$
		};

		labelListField = new ListDialogField(adapter, addButtons,
				new LabelRefsListLabelProvider());
		labelListField.setDialogFieldListener(adapter);
		String pluginsLabel = "Labels:";

		labelListField.setLabelText(pluginsLabel);
		labelListField.setRemoveButtonIndex(2);
		this.labelListField.setElements(labelRefsList);

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
		createLabelListControls(composite, nColumns);

		setMessage("Define constants for this Artifact...");
		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
		return area;
	}

	private void createLabelListControls(Composite composite, int nColumns) {
		labelListField.doFillIntoGrid(composite, nColumns);
	}

	private void labelListDialogCustomButtonPressed(DialogField field, int index) {
		if (field == labelListField) {
			if (index == 0) {
				LabelRef newLabel = new LabelRef();
				newLabel.setLabelClass("String");
				LabelsSelectionDialog dialog = new LabelsSelectionDialog(
						getShell(), newLabel, this.parentWizardPage, this
								.getLabelRefsList(), false);
				dialog.setEnableCustomType(true);

				if (dialog.open() == Window.OK) {
					LabelRef ref = dialog.getLabelRef();
					labelListField.addElement(ref);
				}
			} else if (index == 1) {
				List selectedElem = labelListField.getSelectedElements();
				if (selectedElem.size() == 0)
					return;
				LabelRef ref = (LabelRef) selectedElem.get(0);

				LabelsSelectionDialog dialog = new LabelsSelectionDialog(
						getShell(), (LabelRef) ref.clone(),
						this.parentWizardPage, this.getLabelRefsList(), true);
				dialog.setEnableCustomType(true);

				if (dialog.open() == Window.OK) {
					LabelRef returnedRef = dialog.getLabelRef();
					ref.applyValues(returnedRef);
					labelListField.refresh();
				}
			}
		}
	}

	public List getLabelRefsList() {
		return this.labelListField.getElements();
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Artifact Constants");
	}
}
