/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.sdk.internal.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CreatePatternFileDialog extends Dialog{

	public CreatePatternFileDialog(Shell parentShell) {
		super(parentShell);
		
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		composite.setLayout(gridLayout);

		final Label sourceArtifactLabel = new Label(composite, SWT.NONE);
		sourceArtifactLabel.setText("Source Artifact");
		final Text sourceArtifactText = new Text(composite, SWT.BORDER);
		sourceArtifactText.setLayoutData(new GridData(198, SWT.DEFAULT));
		sourceArtifactText.setText("Source Artifact");

		final Button chooseButton = new Button(composite, SWT.NONE);
		chooseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(final SelectionEvent e) {
				
				// Get the current Selection and find it's project
				
				
//				BrowseForArtifactDialog dialog = new BrowseForArtifactDialog(getIArtifact()
//						.getTigerstripeProject(),
//						new IAbstractArtifact[] { });
//				dialog.setTitle("SourceArtifact");
//				dialog.setMessage("Select the artifact" + " to be used as base for the pattern file.");
//
//				AbstractArtifact[] artifacts = dialog.browseAvailableArtifacts(
//						getShell(), Arrays
//								.asList(new Object[] { }));
			}
		});
		chooseButton.setText("Choose");

		final Label patternNameLabel = new Label(composite, SWT.NONE);
		patternNameLabel.setText("Pattern Name");

		final Text patternNameText = new Text(composite, SWT.BORDER);
		final GridData gd_patternNameText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		patternNameText.setLayoutData(gd_patternNameText);
		new Label(composite, SWT.NONE);

		final Label label = new Label(composite, SWT.NONE);
		label.setText("UI Label");

		final Text uILabelText = new Text(composite, SWT.BORDER);
		final GridData gd_uILabelText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		uILabelText.setLayoutData(gd_uILabelText);
		new Label(composite, SWT.NONE);

		final Label iconPathLabel = new Label(composite, SWT.NONE);
		final GridData gd_iconPathLabel = new GridData(SWT.FILL, SWT.TOP, false, true);
		iconPathLabel.setLayoutData(gd_iconPathLabel);
		iconPathLabel.setText("Icon Path");

		final Text iconPathText = new Text(composite, SWT.BORDER);
		final GridData gd_iconPathText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		iconPathText.setLayoutData(gd_iconPathText);

		final Button browseButton = new Button(composite, SWT.NONE);
		final GridData gd_browseButton = new GridData(SWT.FILL, SWT.FILL, true, true);
		browseButton.setLayoutData(gd_browseButton);
		browseButton.setText("Browse");

		final Label indexLabel = new Label(composite, SWT.NONE);
		indexLabel.setText("Index");

		final Text indexText = new Text(composite, SWT.BORDER);
		final GridData gd_indexText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		indexText.setLayoutData(gd_indexText);
		new Label(composite, SWT.NONE);

		final Label descriptionLabel = new Label(composite, SWT.NONE);
		descriptionLabel.setText("Description");

		final Text descriptionText = new Text(composite, SWT.BORDER);
		final GridData gd_descriptionText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_descriptionText.heightHint = 84;
		descriptionText.setLayoutData(gd_descriptionText);
		new Label(composite, SWT.NONE);
		
		return composite;
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Create an Artifact Pattern File");
	}
	
}
