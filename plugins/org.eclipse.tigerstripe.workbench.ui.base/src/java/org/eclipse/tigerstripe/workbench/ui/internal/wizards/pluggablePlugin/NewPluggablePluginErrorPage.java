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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.pluggablePlugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewPluggablePluginErrorPage extends WizardPage {

	public NewPluggablePluginErrorPage(String pageName) {
		super(pageName);
	}

	public NewPluggablePluginErrorPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	private Image getSWTImage(final int imageID) {
		Shell shell = getShell();
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

	public void createControl(Composite parent) {

		Composite textPanel = new Composite(parent, SWT.NONE);
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
				.setText("\nYou cannot create new Tigerstripe Plugin Project\n\n\n"
						+ "Your Tigerstripe license has insufficient privileges for this operation, "
						+ "please contact Tigerstripe if you wish to create a new Plugin Project.");
		GridData gd = new GridData(GridData.GRAB_VERTICAL
				| GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL);
		gd.widthHint = 300;
		warningLabel.setLayoutData(gd);

		setControl(textPanel);

	}

	@Override
	public boolean isPageComplete() {
		return false;
	}
}
