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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.segment.facetRefs;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class FacetReferencesDetailsPage implements IDetailsPage {

	private IManagedForm form;

	private FacetReferencesSection master;

	private IFacetReference facet;

	public FacetReferencesDetailsPage(FacetReferencesSection master) {
		super();
		this.master = master;
	}

	public void createContents(Composite parent) {
		parent.setLayout(TigerstripeLayoutFactory
				.createDetailsTableWrapLayout());
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		parent.setLayoutData(td);

		int height = createFieldInfo(parent);
		/*
		 * FIXME Just workaround to avoid appearing scrolls on details part.
		 */
		master.setMinimumHeight(height);

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
	private Text facetVersion;

	private Text facetDescription;

	private Text facetName;

	private int createFieldInfo(Composite parent) {
		FormToolkit toolkit = form.getToolkit();

		Section section = TigerstripeLayoutFactory.createSection(parent,
				toolkit, ExpandableComposite.TITLE_BAR,
				"Facet Reference Details", null);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section);

		sectionClient.setLayout(TigerstripeLayoutFactory
				.createFormTableWrapLayout(2, false));
		sectionClient.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		toolkit.createLabel(sectionClient, "Name: ");
		facetName = toolkit.createText(sectionClient, "");
		facetName.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		facetName.setEditable(false);
		facetName.setEnabled(false);

		toolkit.createLabel(sectionClient, "Version: ");
		facetVersion = toolkit.createText(sectionClient, "");
		facetVersion.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		facetVersion.setEditable(false);
		facetVersion.setEnabled(false);

		toolkit.createLabel(sectionClient, "Description: ");
		facetDescription = toolkit.createText(sectionClient, "", SWT.WRAP
				| SWT.MULTI | SWT.V_SCROLL);
		facetDescription.setEditable(false);
		TableWrapData gd = new TableWrapData(TableWrapData.FILL_GRAB);
		gd.heightHint = 70;
		facetDescription.setLayoutData(gd);
		facetDescription.setEnabled(false);

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);

		return sectionClient.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
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

	public boolean isStale() {
		return false;
	}

	public void refresh() {
		updateForm();
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		if (part instanceof FacetReferencesSection) {
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
	}

	public void setFocus() {
	}
}
