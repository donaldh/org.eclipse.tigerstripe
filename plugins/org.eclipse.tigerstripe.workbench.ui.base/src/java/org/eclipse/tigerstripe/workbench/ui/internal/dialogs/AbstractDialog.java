/*******************************************************************************
 * Copyright (c) 2010 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Initial Contribution:
 *    Navid Mehregani (Cisco Systems, Inc.)
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.ui.internal.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;


/**
 * This is an abstract dialog used used by various menu options (e.g. generator and 
 * module dialog)
 * 
 */
public abstract class AbstractDialog extends Dialog {
	
	// List used to display the items for this dialog
	protected Table fListTable;

	// Description of the selected item in the list
	protected Text fDescriptionText;

	public AbstractDialog(IShellProvider parentShell) {
		super(parentShell);
	}

	public AbstractDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,	true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);

		fListTable = new Table(area, SWT.FULL_SELECTION | SWT.SINGLE	| SWT.V_SCROLL);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL	| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		gd.heightHint = 150;
		gd.widthHint = 350;
		fListTable.setLayoutData(gd);

		fListTable.setHeaderVisible(true);
		fListTable.setLinesVisible(true);
		fListTable.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				int index = fListTable.getSelectionIndex();
				
				if (fDescriptionText != null) {
					if (index==-1)
						fDescriptionText.setText("");
					else
						fDescriptionText.setText(getItemDescription(index));
				}
					
			}
		});
		
		addColumns();

		populateTable();

		fListTable.pack();

		Group descriptionGroup = new Group(area, SWT.V_SCROLL);
		descriptionGroup.setText("Description");
		descriptionGroup.setLayout(new FillLayout());
		GridData gd2 = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		gd2.heightHint = 50;
		descriptionGroup.setLayoutData(gd2);

		fDescriptionText = new Text(descriptionGroup, SWT.NONE);
		fDescriptionText.setEditable(false);

		createContextMenu();
		getShell().setText(getDialogTitle());

		return area;
	}
		
	public abstract void populateTable();
	protected abstract void createContextMenu();
	protected abstract String getDialogTitle();
	protected abstract String getItemDescription(int index);
	protected abstract void addColumns();

}
