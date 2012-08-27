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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.dialog;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.NewTSMessageDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractLogicalExplorerNode;

public class LogicalNodePromptForNameDialog extends NewTSMessageDialog {

	private AbstractLogicalExplorerNode node;

	protected Text newNameText;

	protected String newName;

	protected String initialName;
	
	protected IContainer targetContainer;

	public LogicalNodePromptForNameDialog(Shell parent,
			AbstractLogicalExplorerNode node, String title, String caption, IContainer targetContainer) {
		super(parent, title, caption);
		this.node = node;
		this.targetContainer = targetContainer;
		initialName = node.getText();
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
	}

	public void setInitialName(String initialName) {
		this.initialName = initialName;
	}

	public String getNewName() {
		return newName;
	}

	@Override
	public Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FillLayout());

		initializeDialogUnits(parent);
		Composite composite = new Composite(area, SWT.NONE);
		int nColumns = 3;
		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createMessageArea(composite, nColumns);
		createBody(composite, nColumns);
		return area;
	}

	@Override
	public void create() {
		super.create();
		validateNewName();
	}

	private void createBody(Composite composite, int nColumns) {
		Label l = new Label(composite, SWT.NULL);
		l.setText("New name:");

		newNameText = new Text(composite, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		newNameText.setLayoutData(gd);
		newNameText.setText(initialName);
		newName = newNameText.getText().trim();
		newNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				handleModifyText();
			}
		});
	}

	protected void handleModifyText() {
		newName = newNameText.getText().trim();
		validateNewName();
	}

	/**
	 * Prevent to rename to exist resource name, based on the key (primary)
	 * resource of the node
	 * 
	 */
	protected void validateNewName() {
		IResource res = node.getKeyResource();
		if (targetContainer.findMember(getNewName() + "." + res.getFileExtension()) != null) {
			setErrorMessage("Duplicate element name");
			Button okButton = this.getButton(IDialogConstants.OK_ID);
			if (okButton == null)
				return;
			okButton.setEnabled(false);
		} else if (getNewName().length()==0) {
			setErrorMessage("Enter element name");
			Button okButton = this.getButton(IDialogConstants.OK_ID);
			if (okButton == null)
				return;
			okButton.setEnabled(false);
		} else {
			setMessage("Enter new name for this element.");
			Button okButton = this.getButton(IDialogConstants.OK_ID);
			if (okButton == null)
				return;
			okButton.setEnabled(true);
		}
	}
}
