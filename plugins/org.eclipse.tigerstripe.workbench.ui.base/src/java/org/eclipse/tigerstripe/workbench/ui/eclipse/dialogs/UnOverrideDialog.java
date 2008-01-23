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

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.api.model.IMethod.OssjEntityMethodFlavor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.TSMessageDialog;

/**
 * This dialog allows to edit the operation overide on a managed entity.
 * 
 * @author Eric Dillon
 * 
 */
public class UnOverrideDialog extends TSMessageDialog {

	public final static int OVERRIDE_ALL = 0;
	public final static int OVERRIDE_METHOD = 1;
	public final static int OVERRIDE_FLAVOR = 2;

	private int selectedOveride;

	private Button unOverrideAllButton;
	private Button unOverrideMethodButton;
	private Button unOverrideFlavorButton;

	private String methodName;
	private OssjEntityMethodFlavor flavor;

	public UnOverrideDialog(Shell parentShell, String methodName,
			OssjEntityMethodFlavor flavor) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.MAX);
		this.methodName = methodName;
		this.flavor = flavor;
	}

	@Override
	public Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FillLayout());
		GridData gdl = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		area.setLayoutData(gdl);

		initializeDialogUnits(parent);
		Composite composite = new Composite(area, SWT.NONE);
		int nColumns = 1;
		GridLayout layout = new GridLayout();

		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createMessageArea(composite, nColumns);
		createUnoverrideSelector(composite);
		initDialog();

		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
		return area;
	}

	protected void initDialog() {
		getShell().setText("Un-override Operation Details");
		selectedOveride = OVERRIDE_ALL;
		unOverrideAllButton.setSelection(true);
		unOverrideFlavorButton.setSelection(false);
		unOverrideMethodButton.setSelection(false);
	}

	public int getSelectedOverride() {
		return this.selectedOveride;
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

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {
		super.okPressed();

	}

	private void createUnoverrideSelector(Composite parent) {
		setMessage("What do you want to un-override?");

		unOverrideAllButton = new Button(parent, SWT.RADIO);
		unOverrideAllButton.setText("Un-override all methods");
		unOverrideAllButton
				.setToolTipText("The flavor details for all methods will be reset to their definitions\n in the original Entity Artifact");
		unOverrideAllButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				unOverrideButtonPressed(e);
			}
		});

		unOverrideMethodButton = new Button(parent, SWT.RADIO);
		unOverrideMethodButton
				.setText("Un-override all flavors for this method ("
						+ methodName + ") only");
		unOverrideMethodButton
				.setToolTipText("All flavor details for the selected method will be reset to their definitions\n in the original Entity Artifact");
		unOverrideMethodButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				unOverrideButtonPressed(e);
			}
		});
		unOverrideMethodButton.setEnabled(methodName != null);

		unOverrideFlavorButton = new Button(parent, SWT.RADIO);
		String flavorLabel = "unknown";
		if (flavor != null) {
			flavorLabel = flavor.getPojoLabel();
		}
		unOverrideFlavorButton.setText("Un-override this flavor ("
				+ flavorLabel + ") only");
		unOverrideFlavorButton
				.setToolTipText("The flavor details for the selected method will be reset to their definition\n in the original Entity Artifact");
		unOverrideFlavorButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				unOverrideButtonPressed(e);
			}
		});
		unOverrideFlavorButton.setEnabled(methodName != null && flavor != null);
	}

	private void unOverrideButtonPressed(SelectionEvent e) {
		if (e.getSource() == unOverrideAllButton) {
			selectedOveride = OVERRIDE_ALL;
		} else if (e.getSource() == unOverrideFlavorButton) {
			selectedOveride = OVERRIDE_FLAVOR;
		} else if (e.getSource() == unOverrideMethodButton) {
			selectedOveride = OVERRIDE_METHOD;
		}
	}
}
