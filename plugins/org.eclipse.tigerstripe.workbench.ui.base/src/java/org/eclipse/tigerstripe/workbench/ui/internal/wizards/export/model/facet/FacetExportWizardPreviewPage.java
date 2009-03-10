/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Jim Strawn
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.ui.internal.wizards.export.model.facet;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.tigerstripe.workbench.internal.core.model.export.facets.FacetExporterInput;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.AbstractArtifactLabelProvider;

public class FacetExportWizardPreviewPage extends WizardPage {

	public final static String PAGE_NAME = "FacetExportPreview";
	
	private FacetExporterInput inputManager;

	private TableViewer tableViewer;

	protected FacetExportWizardPreviewPage() {

		super(PAGE_NAME);
		setTitle("Facet Scoped Model Export");
		setDescription("Verify changes below and complete the export.");
	}

	public void createControl(Composite parent) {

		inputManager = ((FacetExportWizard) getWizard()).getInputManager();

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		new Label(composite, SWT.LEFT).setText("The following files will be overwritten:");

		Table table = new Table(composite, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(data);

		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new AbstractArtifactLabelProvider());
		tableViewer.setFilters(new ViewerFilter[]{ new TigerstripePackageViewerFilter()});
		tableViewer.setSorter(new ViewerSorter());

		setControl(composite);
		
	}

	public void initialize() {
		
		tableViewer.setInput(inputManager.getOverwrites());
		setPageComplete(true);
	}

	private final class TigerstripePackageViewerFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {

			if (element instanceof IPackageArtifact) {
				return false;
			}
			return true;
		}
	}

}
