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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.generator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

public class GeneratorDescriptorErrorPage extends TigerstripeFormPage {

	private IManagedForm managedForm = null;

	public GeneratorDescriptorErrorPage(FormEditor editor, String id,
			String title) {
		super(editor, id, title);
		// TODO Auto-generated constructor stub
	}

	public GeneratorDescriptorErrorPage(String id, String title) {
		super(id, title);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void refresh() {
		if (managedForm != null) {
			managedForm.refresh();
		}
	}

	protected Image getSWTImage(final int imageID) {
		Shell shell = Display.getCurrent().getActiveShell();
		final Display display;
		if (shell == null) {
			display = Display.getCurrent();
		} else {
			display = shell.getDisplay();
		}

		final Image[] image = new Image[1];
		display.syncExec(new Runnable() {
			public void run() {
				image[0] = display.getSystemImage(imageID);
			}
		});

		return image[0];

	}

	@Override
	public void createFormContent(IManagedForm managedForm) {

		Composite textPanel = new Composite(managedForm.getForm().getBody(),
				SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		textPanel.setLayout(gl);

		Image errorImage = getSWTImage(SWT.ICON_ERROR);
		Label imageLabel = new Label(textPanel, SWT.NULL);
		errorImage.setBackground(imageLabel.getBackground());
		imageLabel.setImage(errorImage);
		imageLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER
				| GridData.VERTICAL_ALIGN_BEGINNING));

		Text warningLabel = new Text(textPanel, SWT.MULTI | SWT.WRAP
				| SWT.READ_ONLY);

		warningLabel
				.setText("\nYou cannot edit/view Tigerstripe plugins\n\n\n"
						+ "Your Tigerstripe license has insufficient privileges for this operation, "
						+ "please contact Tigerstripe if you wish to edit/view Tigerstripe plugins.");
		GridData gd = new GridData(GridData.GRAB_VERTICAL
				| GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL);
		gd.widthHint = 500;
		warningLabel.setLayoutData(gd);
		textPanel.pack();

		Color panelBg = textPanel.getBackground();
		managedForm.getForm().getBody().setBackground(panelBg);
		GeneratorDescriptorErrorPage pluginDescriptorErrPage = (GeneratorDescriptorErrorPage) managedForm
				.getContainer();
		GeneratorDescriptorEditor plugDescEditor = (GeneratorDescriptorEditor) pluginDescriptorErrPage
				.getEditor();
		CTabFolder folder = plugDescEditor.getPageFolder();
		folder.setEnabled(false);
		folder.setSelectionBackground(panelBg);
	}

}
