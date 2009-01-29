/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jim Strawn (Cisco Systems, Inc.) - initial implementation
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.ui.internal.wizards.export.facetmodel;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class FacetModelExportWizardMainPage extends WizardPage {

	public FacetModelExportWizardMainPage() {

		super("defineInput");
		setTitle("Facet Scoped Model Export");
		setDescription("Enter source project, destination project, facet, and whether or not to include referenced projects.");
	}

	public void createControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FillLayout(SWT.VERTICAL));
		setControl(container);

		createSourceGroupControl(container);
		createDestinationGroupControl(container);
	}

	/**
	 * Creates a Group for the source data input.
	 * 
	 * @param container
	 */
	private void createSourceGroupControl(Composite container) {

		Group srcGrp = new Group(container, SWT.SHADOW_ETCHED_IN);
		srcGrp.setText("Source");

		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 3;
		srcGrp.setLayout(gridlayout);

		// source project selection
		final Label srclbl = new Label(srcGrp, SWT.NONE);
		srclbl.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		srclbl.setText("Project: ");

		Text srcProjectTxt = new Text(srcGrp, SWT.NONE);
		srcProjectTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		final Button srcBtn = new Button(srcGrp, SWT.NONE);
		srcBtn.setLayoutData(new GridData(SWT.CENTER));
		srcBtn.setText("Browse...");

		// source facet selection
		final Label fctlbl = new Label(srcGrp, SWT.NONE);
		fctlbl.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		fctlbl.setText("Model Facet: ");

		Text fctProjectTxt = new Text(srcGrp, SWT.NONE);
		fctProjectTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		final Button fctBtn = new Button(srcGrp, SWT.NONE);
		fctBtn.setLayoutData(new GridData(SWT.CENTER));
		fctBtn.setText("Browse...");

		// reference inclusion selection
		final Button refChkBtn = new Button(srcGrp, SWT.CHECK);
		GridData griddata = new GridData();
		griddata.horizontalSpan = 3;
		refChkBtn.setLayoutData(griddata);
		refChkBtn.setText("Include referenced projects");
	}

	/**
	 * Creates a Group for the destination data input.
	 * 
	 * @param container
	 */
	private void createDestinationGroupControl(Composite container) {

		Group destGrp = new Group(container, SWT.SHADOW_ETCHED_IN);
		destGrp.setText("Destination");

		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 3;
		destGrp.setLayout(gridlayout);

		// destination project selection
		final Label destlbl = new Label(destGrp, SWT.NONE);
		destlbl.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		destlbl.setText("Project: ");

		Text destProjectTxt = new Text(destGrp, SWT.NONE);
		destProjectTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		final Button destBtn = new Button(destGrp, SWT.NONE);
		destBtn.setLayoutData(new GridData(SWT.CENTER));
		destBtn.setText("Browse...");
	}

}
