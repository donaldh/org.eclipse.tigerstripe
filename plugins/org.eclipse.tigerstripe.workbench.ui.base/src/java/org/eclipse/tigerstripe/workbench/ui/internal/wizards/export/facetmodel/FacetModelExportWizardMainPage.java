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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripeProjectNature;
import org.eclipse.tigerstripe.workbench.internal.core.model.export.facets.FacetModelExportInputManager;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLog;
import org.eclipse.ui.dialogs.SelectionDialog;

public class FacetModelExportWizardMainPage extends WizardPage implements ISelectionChangedListener, SelectionListener {

	private static final int SIZING_SELECTION_WIDGET_WIDTH = 480;

	private static final int SIZING_SELECTION_WIDGET_HEIGHT = 150;

	private static final String WIZARD_PAGE_NAME = "EXPORT_WIZARD_MAIN";

	private FacetModelExportInputManager inputManager;

	private TableViewer projectTableViewer;

	private TableViewer facetTableViewer;

	private Button incReferencedBtn;

	private Button overwriteExistingBtn;

	private Text destinationText;

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
				return "";
			}
		}
	}

	private final class TigerstripeFacetLabelProvider extends LabelProvider {

		@Override
		public Image getImage(Object element) {
			return Images.get(Images.CONTRACTSEGMENT_ICON);
		}

		@Override
		public String getText(Object element) {
			return ((IFile) element).getName();
		}
	}

	private final class TigerstripeSourceProjectViewerFilter extends ViewerFilter {
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

	private final class TigerstripeDestinationProjectViewerFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {

			try {
				if (element instanceof IProject && TigerstripeProjectNature.hasNature((IProject) element)) {
					if (inputManager.getSource() != null) {
						if (!((IProject) element).equals(inputManager.getSource().getAdapter(IProject.class))) {
							return true;
						} else {
							return false;
						}
					} else {
						return true;
					}
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
		protected Point getInitialSize() {

			return new Point(280, 350);
		}

		@Override
		protected boolean isResizable() {

			return false;
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
			destProjectTableViewer.setFilters(new ViewerFilter[] { new TigerstripeDestinationProjectViewerFilter() });
			destProjectTableViewer.setInput(ResourcesPlugin.getWorkspace().getRoot().getProjects());

			destProjectTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

				public void selectionChanged(SelectionChangedEvent event) {

					IProject project = (IProject) ((StructuredSelection) event.getSelection()).getFirstElement();
					inputManager.setDestination((ITigerstripeModelProject) project.getAdapter(ITigerstripeModelProject.class));
					checkPageComplete();
				}
			});

			return group;
		}
	}

	public FacetModelExportWizardMainPage() {

		super(WIZARD_PAGE_NAME);
		setTitle("Facet Scoped Model Export");
		setDescription("Enter source project, destination project, facet, and whether or not to include referenced projects.");
	}

	public void createControl(Composite parent) {

		initializeDialogUnits(parent);
		inputManager = ((FacetModelExportWizard) getWizard()).getInputManager();

		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));

		createInputLabel(composite);
		createInputGroup(composite);
		createDestinationLabel(composite);
		createDestinationGroup(composite);
		createOptionsLabel(composite);
		createOptionsGroup(composite);

		setControl(composite);

	}

	private void createInputLabel(Composite composite) {

		new Label(composite, SWT.LEFT).setText("Select the resources to export:");
	}

	private void createInputGroup(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = true;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		createProjectListViewer(composite, SIZING_SELECTION_WIDGET_WIDTH / 2, SIZING_SELECTION_WIDGET_HEIGHT);
		createFacetListViewer(composite, SIZING_SELECTION_WIDGET_WIDTH / 2, SIZING_SELECTION_WIDGET_HEIGHT);

		initialize();

	}

	private void createProjectListViewer(Composite parent, int width, int height) {

		Table table = new Table(parent, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.widthHint = width;
		data.heightHint = height;
		table.setLayoutData(data);

		projectTableViewer = new TableViewer(table);
		projectTableViewer.setContentProvider(new ArrayContentProvider());
		projectTableViewer.setLabelProvider(new TigerstripeProjectLabelProvider());
		projectTableViewer.setFilters(new ViewerFilter[] { new TigerstripeSourceProjectViewerFilter() });
		projectTableViewer.addSelectionChangedListener(this);

	}

	private void createFacetListViewer(Composite parent, int width, int height) {

		Table table = new Table(parent, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.widthHint = width;
		data.heightHint = height;
		table.setLayoutData(data);

		facetTableViewer = new TableViewer(table);
		facetTableViewer.setContentProvider(new ArrayContentProvider());
		facetTableViewer.setLabelProvider(new TigerstripeFacetLabelProvider());
		facetTableViewer.setSorter(new ViewerSorter());
		facetTableViewer.addSelectionChangedListener(this);

	}

	private void createDestinationLabel(Composite composite) {

		new Label(composite, SWT.LEFT).setText("Select the export destination:");
	}

	private void createDestinationGroup(Composite parent) {

		final Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		new Label(composite, SWT.NONE).setText("Project:");

		destinationText = new Text(composite, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
		destinationText.setLayoutData(data);

		Button destinationButton = new Button(composite, SWT.PUSH);
		destinationButton.setText("Browse...");
		destinationButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		destinationButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DestinationProjectDialog dlg = new DestinationProjectDialog(composite.getShell());
				if (dlg.open() == Dialog.OK) {
					destinationText.setText(inputManager.getDestination().getName());
				} else {
					destinationText.setText("");
					inputManager.setDestination(null);
				}
				checkPageComplete();
			}
		});

	}

	private void createOptionsLabel(Composite composite) {

		new Label(composite, SWT.LEFT).setText("Options:");
	}

	private void createOptionsGroup(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		composite.setLayout(layout);

		incReferencedBtn = new Button(composite, SWT.CHECK | SWT.LEFT);
		incReferencedBtn.setText("Include referenced projects");
		incReferencedBtn.addSelectionListener(this);

		overwriteExistingBtn = new Button(composite, SWT.CHECK | SWT.LEFT);
		overwriteExistingBtn.setText("Overwrite existing files without warning");
		overwriteExistingBtn.addSelectionListener(this);

	}

	private void initialize() {

		projectTableViewer.setInput(ResourcesPlugin.getWorkspace().getRoot().getProjects());
		if (inputManager.getSource() != null) {

			try {

				projectTableViewer.getTable().setSelection(getSelectionIndex(inputManager.getSource(), projectTableViewer.getTable()));
				facetTableViewer.setInput(TigerstripeProjectAuditor.findAll((IProject) inputManager.getSource().getAdapter(IProject.class), "wfc"));

			} catch (IllegalArgumentException e) {
				projectTableViewer.getTable().setSelection(0);
				TigerstripeLog.logError(e);
			}
		}

		checkPageComplete();
	}

	private int getSelectionIndex(final ITigerstripeModelProject source, Table table) {

		IProject iProject = (IProject) source.getAdapter(IProject.class);
		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {
			TableItem item = items[i];
			if (item.getData().equals(iProject)) {
				return i;
			}
		}
		throw new IllegalArgumentException("Invalid Selection argument.");
	}

	public void checkPageComplete() {

		if (inputManager.verifyComplete() && inputManager.isOverwriteExisting()) {
			setPageComplete(true);
		} else {
			setPageComplete(false);
		}

	}

	@Override
	public boolean canFlipToNextPage() {

		if (!inputManager.isOverwriteExisting() && inputManager.verifyComplete()) {
			return true;
		} else {
			return false;
		}

	}

	/**************************************************************************
	 * Event handling implementation *
	 **************************************************************************/
	public void selectionChanged(SelectionChangedEvent event) {

		if (event.getSource() == projectTableViewer) {
			if (((StructuredSelection) event.getSelection()).getFirstElement() instanceof IProject) {
				IProject project = (IProject) ((StructuredSelection) event.getSelection()).getFirstElement();
				inputManager.setSource((ITigerstripeModelProject) project.getAdapter(ITigerstripeModelProject.class));
				facetTableViewer.setInput(TigerstripeProjectAuditor.findAll(project, "wfc"));
				if (inputManager.getDestination() != null) {
					destinationText.setText("");
					inputManager.setDestination(null);
				}
				checkPageComplete();
			}
		}
		if (event.getSource() == facetTableViewer) {
			inputManager.setFacet((IFile) ((StructuredSelection) event.getSelection()).getFirstElement());
			checkPageComplete();
		}
	}

	public void widgetDefaultSelected(SelectionEvent e) {

		widgetSelected(e);
	}

	public void widgetSelected(SelectionEvent e) {

		if (e.getSource() == incReferencedBtn) {
			inputManager.setIncludeReferences(incReferencedBtn.getSelection());
		}
		if (e.getSource() == overwriteExistingBtn) {
			inputManager.setOverwriteExisting(overwriteExistingBtn.getSelection());
			checkPageComplete();
		}
	}
}