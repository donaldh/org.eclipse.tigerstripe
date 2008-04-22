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
package org.eclipse.tigerstripe.workbench.ui.internal.elements;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public abstract class TSMessageDialog extends Dialog {

	private Label messageLabel;

	public TSMessageDialog(Shell parent) {
		super(parent);
	}

	protected void setMessage(String message) {
		if (messageLabel != null) {
			messageLabel.setText(message);
			messageLabel.setForeground(new Color(null, 0, 0, 0));
		}
	}

	protected void setErrorMessage(String message) {
		if (messageLabel != null) {
			messageLabel.setText(message);
			messageLabel.setForeground(new Color(null, 255, 0, 0));
		}
	}

	protected void createMessageArea(Composite composite, int nColumns) {

		Composite inComposite = new Composite(composite, SWT.LEFT);
		int inColumns = 2;
		GridLayout layout = new GridLayout();
		layout.numColumns = inColumns;
		inComposite.setLayout(layout);
		inComposite.setBackground(new Color(null, 255, 255, 255));

		GridData inCompositeGridData = new GridData();
		inCompositeGridData.horizontalSpan = nColumns;
		inCompositeGridData.horizontalAlignment = SWT.FILL;
		inCompositeGridData.grabExcessHorizontalSpace = true;
		inComposite.setLayoutData(inCompositeGridData);

		messageLabel = new Label(inComposite, SWT.LEFT);
		messageLabel.setBackground(new Color(null, 255, 255, 255));
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		gridData.horizontalSpan = nColumns;
		messageLabel.setLayoutData(gridData);

		createSeparator(composite, nColumns);
	}

	protected void createSeparator(Composite composite, int nColumns) {
		(new Separator(SWT.SEPARATOR | SWT.HORIZONTAL)).doFillIntoGrid(
				composite, nColumns, convertHeightInCharsToPixels(1));
	}

}
