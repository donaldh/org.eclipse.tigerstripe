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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripeProjectNature;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.ui.dialogs.SelectionDialog;

public class FacetModelExportWizardMainPage extends WizardPage {

	private IFile facet;

	private boolean includeReferences = false;

	private ITigerstripeModelProject sourceProject;

	private ITigerstripeModelProject destinationProject;

	private final class TigerstripeProjectLabelProvider extends LabelProvider {

		@Override
		public Image getImage(Object element) {
			return Images.get(Images.TSPROJECT_FOLDER);
		}

		@Override
		public String getText(Object element) {

			if (element instanceof IProject) {
				return ((IProject) element).getName();
			} else {
				return null;
			}
		}
	}

	private final class TigerstripeProjectViewerFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {

			try {
				if (element instanceof IProject && TigerstripeProjectNature.hasNature((IProject) element)) {
					return true;
				}
			} catch (CoreException e) {
				EclipsePlugin.log(e);
				return false;
			}
			return false;
		}
	}

	private final class DestinationProjectDialog extends SelectionDialog {

		TableViewer destProjectTableViewer;

		protected DestinationProjectDialog(Shell parentShell) {
			super(parentShell);
		}

		@Override
		protected Control createDialogArea(Composite parent) {

			GridData gd1 = new GridData(SWT.FILL, SWT.FILL, true, true);

			((GridLayout) parent.getLayout()).marginTop = 3;
			((GridLayout) parent.getLayout()).marginBottom = 3;
			((GridLayout) parent.getLayout()).verticalSpacing = 3;
			((GridLayout) parent.getLayout()).horizontalSpacing = 3;

			Group group = new Group(parent, SWT.SHADOW_NONE);

			final GridLayout gridlayout = new GridLayout();
			group.setLayout(gridlayout);
			group.setLayoutData(gd1);
			group.setText("Select destination project:");

			destProjectTableViewer = new TableViewer(group, SWT.SINGLE);
			destProjectTableViewer.getTable().setLayoutData(gd1);
			destProjectTableViewer.setContentProvider(new ArrayContentProvider());
			destProjectTableViewer.setLabelProvider(new TigerstripeProjectLabelProvider());
			destProjectTableViewer.setFilters(new ViewerFilter[] { new TigerstripeProjectViewerFilter() });
			destProjectTableViewer.setInput(ResourcesPlugin.getWorkspace().getRoot().getProjects());
			destProjectTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

				public void selectionChanged(SelectionChangedEvent event) {

					IProject project = (IProject) ((StructuredSelection) event.getSelection()).getFirstElement();
					destinationProject = (ITigerstripeModelProject) project.getAdapter(ITigerstripeModelProject.class);
					isPageComplete();
				}
			});

			return group;
		}
	}

	public FacetModelExportWizardMainPage() {

		super("model-export-main-page");
		setTitle("Facet Scoped Model Export");
		setDescription("Enter source project, destination project, facet, and whether or not to include referenced projects.");
	}

	public IFile getFacet() {
		return facet;
	}

	public boolean isIncludeReferences() {
		return includeReferences;
	}

	public ITigerstripeModelProject getSourceProject() {
		return sourceProject;
	}

	public ITigerstripeModelProject getDestinationProject() {
		return destinationProject;
	}


	public void createControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FillLayout(SWT.VERTICAL));
		setControl(container);

		createSourceGroupControl(container);
		createDestinationGroupControl(container);
	}
	
	public void checkPageComplete() {
		
		if (sourceProject != null && destinationProject != null && facet != null) {
			setPageComplete(true);
		} else {
			setPageComplete(false);
		}
	}

	private void createSourceGroupControl(final Composite container) {

		Group srcGrp = new Group(container, SWT.SHADOW_ETCHED_IN);
		srcGrp.setText("Source");

		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		srcGrp.setLayout(gridlayout);

		GridData gd1 = new GridData(SWT.FILL, SWT.FILL, true, true);

		final TableViewer srcProjectTableViewer = new TableViewer(srcGrp, SWT.SINGLE);
		final TableViewer srcFacetTableViewer = new TableViewer(srcGrp, SWT.SINGLE);

		srcProjectTableViewer.getTable().setLayoutData(gd1);
		srcProjectTableViewer.setContentProvider(new ArrayContentProvider());
		srcProjectTableViewer.setLabelProvider(new TigerstripeProjectLabelProvider());
		srcProjectTableViewer.setFilters(new ViewerFilter[] { new TigerstripeProjectViewerFilter() });

		srcProjectTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {

				if (((StructuredSelection) event.getSelection()).getFirstElement() instanceof IProject) {

					IProject project = (IProject) ((StructuredSelection) event.getSelection()).getFirstElement();
					sourceProject = (ITigerstripeModelProject) project.getAdapter(ITigerstripeModelProject.class);
					srcFacetTableViewer.setInput(TigerstripeProjectAuditor.findAll(project, "wfc"));
					checkPageComplete();
				}
			}
		});

		srcProjectTableViewer.setInput(ResourcesPlugin.getWorkspace().getRoot().getProjects());

		srcFacetTableViewer.getTable().setLayoutData(gd1);
		srcFacetTableViewer.setContentProvider(new ArrayContentProvider());
		srcFacetTableViewer.setLabelProvider(new LabelProvider() {

			@Override
			public Image getImage(Object element) {
				return Images.get(Images.CONTRACTSEGMENT_ICON);
			}

			@Override
			public String getText(Object element) {
				return ((IFile) element).getName();
			}
		});

		srcFacetTableViewer.setSorter(new ViewerSorter());

		srcFacetTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {

				facet = (IFile) ((StructuredSelection) event.getSelection()).getFirstElement();
				checkPageComplete();
			}
		});

		/*
		 * reference inclusion selection
		 */
		final Button refChkBtn = new Button(srcGrp, SWT.CHECK);
		GridData griddata = new GridData();
		griddata.horizontalSpan = 2;
		refChkBtn.setLayoutData(griddata);
		refChkBtn.setText("Include referenced projects");
		refChkBtn.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {

				if (e.getSource() == refChkBtn) {

					includeReferences = ((Button) e.getSource()).getEnabled();
				}
			}
		});
	}

	private void createDestinationGroupControl(final Composite container) {

		Group destGrp = new Group(container, SWT.SHADOW_ETCHED_IN);
		destGrp.setText("Destination");

		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 3;
		destGrp.setLayout(gridlayout);

		final Label destlbl = new Label(destGrp, SWT.NONE);
		destlbl.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		destlbl.setText("Destination project: ");

		final Text destProjectTxt = new Text(destGrp, SWT.NONE);
		destProjectTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Button destBtn = new Button(destGrp, SWT.NONE);
		destBtn.setLayoutData(new GridData(SWT.CENTER));
		destBtn.setText("Browse...");
		destBtn.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {

				DestinationProjectDialog dlg = new DestinationProjectDialog(container.getShell());

				if (dlg.open() == Dialog.OK) {
					destProjectTxt.setText(destinationProject.getName());
				} else {
					destProjectTxt.setText("");
					destinationProject = null;
				}
				checkPageComplete();
			}
		});
	}

}
