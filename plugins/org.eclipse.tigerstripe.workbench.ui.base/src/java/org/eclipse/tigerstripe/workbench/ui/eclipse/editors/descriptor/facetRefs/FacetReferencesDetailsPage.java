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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.facetRefs;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class FacetReferencesDetailsPage implements IDetailsPage {

	private IManagedForm form;

	private FacetReferencesSection master;

	private IFacetReference facet;

	public FacetReferencesDetailsPage() {
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
	private void setFacet(IFacetReference facet) {
		this.facet = facet;
	}

	private IFacetReference getFacet() {
		return facet;
	}

	// ============================================================
	private Text generationDir;

	private Text facetVersion;

	private Text facetDescription;

	private Text facetName;

	private void createFieldInfo(Composite parent) {
		FormToolkit toolkit = form.getToolkit();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section);

		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 2;
		sectionClient.setLayout(gLayout);
		sectionClient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = toolkit.createLabel(sectionClient, "Name: ");
		facetName = toolkit.createText(sectionClient, "");
		facetName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		facetName.setEditable(false);
		facetName.setEnabled(false);

		label = toolkit.createLabel(sectionClient, "Version: ");
		facetVersion = toolkit.createText(sectionClient, "");
		facetVersion.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		facetVersion.setEditable(false);
		facetVersion.setEnabled(false);

		label = toolkit.createLabel(sectionClient, "Description: ");
		facetDescription = toolkit.createText(sectionClient, "", SWT.WRAP
				| SWT.MULTI);
		facetDescription.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		facetDescription.setEditable(false);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 70;
		facetDescription.setLayoutData(gd);
		facetDescription.setEnabled(false);

		label = toolkit.createLabel(sectionClient, "Output Directory: ");
		generationDir = toolkit.createText(sectionClient, "");
		generationDir.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		generationDir.setEditable(true);
		generationDir.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String str = generationDir.getText();
				if (!str.equals(getFacet().getGenerationDir())) {
					getFacet().setGenerationDir(str);
					pageModified();
				}
			}
		});

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
		generationDir.setFocus();
	}

	public boolean isStale() {
		return false;
	}

	public void refresh() {
		updateForm();
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		if (part instanceof FacetReferencesSection) {
			master = (FacetReferencesSection) part;
			Table fieldsTable = master.getViewer().getTable();

			IFacetReference selected = (IFacetReference) fieldsTable
					.getSelection()[0].getData();
			setFacet(selected);
			updateForm();
		}
	}

	private void updateForm() {
		IFacetReference facetRef = getFacet();

		String name = null;
		String version = null;
		String description = null;

		if (facetRef.canResolve()) {
			try {
				IContractSegment facet = facetRef.resolve();
				name = facet.getName();
				version = facet.getVersion();
				description = facet.getDescription();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}

		facetName.setText((name != null) ? name : "<unknown>");
		facetVersion.setText((version != null) ? version : "<unknown>");
		facetDescription.setText((description != null) ? description
				: "<unknown>");
		generationDir.setText((facetRef.getGenerationDir() != null) ? facetRef
				.getGenerationDir() : "");
		generationDir.setEnabled(!master.isReadonly());

	}
}
