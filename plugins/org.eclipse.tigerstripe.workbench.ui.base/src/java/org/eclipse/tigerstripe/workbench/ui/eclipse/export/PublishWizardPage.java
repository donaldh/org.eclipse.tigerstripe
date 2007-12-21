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
package org.eclipse.tigerstripe.workbench.ui.eclipse.export;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.dialogs.StatusUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.api.publish.IProjectPublisher;
import org.eclipse.tigerstripe.eclipse.wizards.artifacts.TSRuntimeBasedWizardPage;

public class PublishWizardPage extends TSRuntimeBasedWizardPage {

	private final static String PAGE_NAME = "org.eclipse.tigerstripe.workbench.ui.eclipse.wizards.publish.page";

	public PublishWizardPage() {
		super(PAGE_NAME);

		setTitle("Publish Tigerstripe Project");
		setDescription("This wizard publishes a Tigerstripe project.");
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createContainerControls(composite, nColumns);

		Label msg = new Label(composite, SWT.NULL);
		GridData gd = new GridData(GridData.BEGINNING);
		gd.horizontalSpan = nColumns;
		gd.verticalAlignment = SWT.CENTER;
		gd.heightHint = 45;
		msg.setLayoutData(gd);

		msg = new Label(composite, SWT.NULL);
		msg
				.setText("Publish details must be set in the Project Descriptor (tigerstripe.xml).");
		gd = new GridData(GridData.BEGINNING);
		gd.horizontalSpan = nColumns;
		gd.verticalAlignment = SWT.CENTER;
		msg.setLayoutData(gd);

		setControl(composite);

		Dialog.applyDialogFont(composite);
		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);

	}

	@Override
	protected void initFromContext() {
		// nothing
	}

	/**
	 * The wizard owning this page is responsible for calling this method with
	 * the current selection. The selection is used to initialize the fields of
	 * the wizard page.
	 * 
	 * @param selection
	 *            used to initialize the fields
	 */
	public void init(IStructuredSelection selection) {
		IJavaElement jelem = getInitialJavaElement(selection);

		initContainerPage(jelem);
		initTSRuntimeContext(jelem);
		initFromContext();
	}

	@Override
	protected void updateStatus(IStatus status) {
		super.updateStatus(StatusUtil.getMostSevere(new IStatus[] { status,
				updatePage() }));
	}

	protected IStatus updatePage() {
		StatusInfo localStatus = new StatusInfo();
		localStatus.setOK();

		ITigerstripeProject handle = getITigerstripeProject();
		IProjectPublisher publisher = handle.getPublisher();
		if (!publisher.isPublishable()) {
			localStatus
					.setError("Please enable and fill in publish details in Project Descriptor (tigerstripe.xml).");
		}

		return localStatus;
	}

	public ITigerstripeProject getITigerstripeProject() {
		try {
			return getTSRuntimeContext().getProjectHandle();
		} catch (TigerstripeException e) {
			return null;
		}
	}
}
