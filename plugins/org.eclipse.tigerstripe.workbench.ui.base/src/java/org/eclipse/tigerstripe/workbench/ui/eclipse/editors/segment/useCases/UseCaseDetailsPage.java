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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.segment.useCases;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.api.contract.useCase.IUseCaseReference;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class UseCaseDetailsPage implements IDetailsPage {

	private IManagedForm form;

	private UseCaseSection master;

	private IUseCaseReference useCaseRef;

	public UseCaseDetailsPage() {
		super();
	}

	public void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);
		TableWrapData td = new TableWrapData(TableWrapData.FILL);
		td.heightHint = 200;
		parent.setLayoutData(td);

		createFieldInfo(parent);

		form.getToolkit().paintBordersFor(parent);
	}

	// ============================================================
	private void setUseCaseRef(IUseCaseReference useCaseRef) {
		this.useCaseRef = useCaseRef;
	}

	private IUseCaseReference getUseCaseRef() {
		return useCaseRef;
	}

	// ============================================================
	private Text useCaseVersion;

	private Text useCaseDescription;

	private Text useCaseName;

	private void createFieldInfo(Composite parent) {
		FormToolkit toolkit = form.getToolkit();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section);

		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 2;
		sectionClient.setLayout(gLayout);
		GridData mgd = new GridData(GridData.FILL_HORIZONTAL);
		mgd.grabExcessVerticalSpace = true;
		sectionClient.setLayoutData(mgd);

		Label label = toolkit.createLabel(sectionClient, "Name: ");
		useCaseName = toolkit.createText(sectionClient, "");
		useCaseName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		useCaseName.setEnabled(false);

		label = toolkit.createLabel(sectionClient, "Version: ");
		useCaseVersion = toolkit.createText(sectionClient, "");
		useCaseVersion.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		useCaseVersion.setEnabled(false);

		label = toolkit.createLabel(sectionClient, "Description: ");
		useCaseDescription = toolkit.createText(sectionClient, "", SWT.WRAP
				| SWT.MULTI);
		useCaseDescription
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		useCaseDescription.setEditable(false);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 30;
		useCaseDescription.setLayoutData(gd);

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);
	}

	// ===================================================================

	public void initialize(IManagedForm form) {
		this.form = form;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	protected void pageModified() {
		master.markPageModified();
	}

	public boolean isDirty() {
		return master.isDirty();
	}

	public void commit(boolean onSave) {
	}

	public boolean setFormInput(Object input) {
		return false;
	}

	public void setFocus() {
		useCaseName.setFocus();
	}

	public boolean isStale() {
		return false;
	}

	public void refresh() {
		updateForm();
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		if (part instanceof UseCaseSection) {
			master = (UseCaseSection) part;
			Table fieldsTable = master.getViewer().getTable();

			IUseCaseReference selected = (IUseCaseReference) fieldsTable
					.getSelection()[0].getData();
			setUseCaseRef(selected);
			updateForm();
		}
	}

	private void updateForm() {
		try {
			IUseCaseReference useCase = getUseCaseRef();

			if (useCase != null && useCase.canResolve()) {
				useCaseName.setText(useCase.resolve().getName());
				useCaseVersion.setText(useCase.resolve().getVersion());
				useCaseDescription.setText(useCase.resolve().getDescription());
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		} finally {
		}
	}
}
