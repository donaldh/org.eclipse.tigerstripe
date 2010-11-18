/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.editor;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Router;

public class DiagramPropertiesDialog extends Dialog {

	private final Diagram diagram;
	private Button obliqueButtonButton;
	private Button rectilinearButton;

	protected DiagramPropertiesDialog(Shell parentShell, Diagram diagram) {
		super(parentShell);
		this.diagram = diagram;
	}

	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.RESIZE);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Digaram Properties");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent = (Composite) super.createDialogArea(parent);

		Group group = new Group(parent, SWT.NONE);
		group.setText("Line Routing Style");

		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 10;
		group.setLayout(fillLayout);

		GridDataFactory.fillDefaults().grab(true, true).applyTo(group);
		obliqueButtonButton = new Button(group, SWT.RADIO);
		obliqueButtonButton.setText("Oblique");

		rectilinearButton = new Button(group, SWT.RADIO);
		rectilinearButton.setText("Rectilinear");
		updateControls();
		return parent;
	}

	private void updateControls() {
		if (Router.RECTILINEAR.equals(diagram.getRouter())) {
			rectilinearButton.setSelection(true);
		} else {
			obliqueButtonButton.setSelection(true);
		}
	}

	@Override
	protected void okPressed() {
		if (rectilinearButton.getSelection()) {
			diagram.setRouter(Router.RECTILINEAR);
		} else {
			diagram.setRouter(Router.OBLIQUE);
		}
		super.okPressed();
	}

}
