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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.facetRefs;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.impl.AbstractTigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.contract.segment.FacetReference;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.BrowseForFacetsDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.FacetSelectionDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.TigerstripeDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

public class FacetReferencesSection extends TigerstripeDescriptorSectionPart
		implements IFormPart {

	protected DetailsPart detailsPart;

	private Button mergeFacets;

	public FacetReferencesSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.NO_TITLE);
		createContent();
		updateMaster();
	}

	protected SashForm sashForm;

	/**
	 * Creates the content of the master/details block inside the managed form.
	 * This method should be called as late as possible inside the parent part.
	 * 
	 * @param managedForm
	 *            the managed form to create the block in
	 */
	@Override
	public void createContent() {
		IManagedForm managedForm = getPage().getManagedForm();
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = getToolkit();

		GridData td = new GridData(GridData.FILL_BOTH);
		td.horizontalSpan = 2;
		getSection().setLayoutData(td);

		Composite body = getToolkit().createComposite(getSection());
		GridLayout layout = new GridLayout();
		layout.marginHeight = layout.marginWidth = 0;
		layout.verticalSpacing = layout.horizontalSpacing = 0;
		body.setLayout(layout);
		sashForm = new SashForm(body, SWT.HORIZONTAL);
		toolkit.adapt(sashForm, false, false);
		sashForm.setMenu(body.getMenu());
		sashForm.setToolTipText("Define/Edit attributes for this Artifact.");
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		createMasterPart(managedForm, sashForm);
		createDetailsPart(managedForm, sashForm);

		getSection().setClient(body);
		getToolkit().paintBordersFor(body);
	}

	class MasterContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ITigerstripeModelProject) {
				ITigerstripeModelProject project = (ITigerstripeModelProject) inputElement;
				try {
					IFacetReference[] deps = project.getFacetReferences();
					return deps;
				} catch (TigerstripeException e) {
					return new Object[0];
				}
			}
			return new IFacetReference[0];
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}

	class MasterLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public String getText(Object obj) {
			IFacetReference dep = (IFacetReference) obj;

			if (dep.isAbsolute()) {
				try {
					return dep.getURI().toASCIIString();
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
					return "<Unknown>";
				}
			} else {
				String path = dep.getProjectRelativePath();
				if (!dep.getContainingProject().equals(getTSProject())) {
					path += " (" + dep.getContainingProject().getName() + ")";
				}
				return path;
			}
		}

		@Override
		public Image getImage(Object obj) {
			return Images.get(Images.CONTRACTSEGMENT_ICON);
		}

	}

	// ====================================================================
	private TableViewer viewer;

	private Button addAttributeButton;

	private Button addFromDependencies;

	private Button removeAttributeButton;

	private Table table;

	public TableViewer getViewer() {
		return this.viewer;
	}

	protected void createMasterPart(final IManagedForm managedForm,
			Composite parent) {

		FormToolkit toolkit = getToolkit();

		Section section = TigerstripeLayoutFactory.createSection(parent,
				toolkit, ExpandableComposite.TITLE_BAR, "&Facet References",
				null);

		Composite sectionClient = toolkit.createComposite(section);
		GridLayout twlayout = new GridLayout();
		twlayout.numColumns = 2;
		sectionClient.setLayout(twlayout);

		table = toolkit.createTable(sectionClient, SWT.NULL);
		GridData td = new GridData(GridData.FILL_BOTH);
		table.setLayoutData(td);

		Composite buttonsClient = toolkit.createComposite(sectionClient);
		buttonsClient.setLayout(TigerstripeLayoutFactory
				.createButtonsGridLayout());
		buttonsClient.setLayoutData(new GridData(
				GridData.VERTICAL_ALIGN_BEGINNING));

		addAttributeButton = toolkit.createButton(buttonsClient, "Add",
				SWT.PUSH);
		addAttributeButton
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addAttributeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				addButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		addFromDependencies = toolkit.createButton(buttonsClient,
				"Add From...", SWT.PUSH);
		addFromDependencies
				.setToolTipText("Select facets from Referenced Projects");
		addFromDependencies
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addFromDependencies.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				addFromDependenciesButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		removeAttributeButton = toolkit.createButton(buttonsClient, "Remove",
				SWT.PUSH);
		removeAttributeButton.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		removeAttributeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				removeButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		final IFormPart part = this;
		viewer = new TableViewer(table);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(part, event.getSelection());
				viewerSelectionChanged(event);
			}
		});
		viewer.setContentProvider(new MasterContentProvider());
		viewer.setLabelProvider(new MasterLabelProvider());

		AbstractTigerstripeProjectHandle handle = (AbstractTigerstripeProjectHandle) getTSProject();
		viewer.setInput(handle);

		mergeFacets = toolkit.createButton(sectionClient,
				"Merge all facets for generation", SWT.CHECK);
		td = new GridData();
		td.horizontalSpan = 2;
		mergeFacets.setLayoutData(td);
		mergeFacets.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				mergeFacetButtonSelected();
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});
		toolkit.paintBordersFor(sectionClient);
		section.setClient(sectionClient);
	}

	/**
	 * FIXME Used only by FacetReferencesDetailsPage. Just workaround to avoid
	 * appearing scrolls on details part.
	 */
	void setMinimumHeight(int value) {
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 4;
		gd.minimumHeight = value;
		table.setLayoutData(gd);
		getManagedForm().reflow(true);
	}

	// Core dependencies don't exist anymore @see #299
	// protected void addDefaultModulesButtonPressed() {
	// FileEditorInput input = (FileEditorInput) getPage().getEditorInput();
	// ITigerstripeProject handle = (ITigerstripeProject)
	// TSExplorerUtils.getProjectHandleFor(input
	// .getFile());
	// try {
	// handle.attachDefaultCoreModelDependency(true);
	// markPageModified();
	//
	// ErrorDialog dialog = new ErrorDialog(
	// getSection().getShell(),
	// "Core Model Update",
	// "The Core Model has been added to your list of Dependencies.\n Please
	// save the descriptor for this change to be effective",
	// new Status(IStatus.WARNING, EclipsePlugin.PLUGIN_ID, 222,
	// "Core Model Added", null), 0);
	// input.getFile().getProject().refreshLocal(1,
	// new NullProgressMonitor());
	// refresh();
	// dialog.open();
	// } catch (TigerstripeException e) {
	// EclipsePlugin.log(e);
	// } catch (CoreException e) {
	// EclipsePlugin.log(e);
	// }
	// }
	//
	/**
	 * Updates the master's side based on the selection on the list of
	 * attributes
	 * 
	 */
	protected void viewerSelectionChanged(SelectionChangedEvent event) {
		updateMaster();
	}

	protected void addFromDependenciesButtonSelected(SelectionEvent event) {

		try {
			BrowseForFacetsDialog dialog = new BrowseForFacetsDialog(
					getTSProject().getReferencedProjects());
			dialog.setTitle("Referenced Facets");
			dialog.setMessage("Select facets from Referenced Projects");
			IFacetReference[] refs = dialog.browseAvailableArtifacts(
					getSection().getShell(), Arrays.asList(getTSProject()
							.getFacetReferences()));
			for (IFacetReference ref : refs) {
				getTSProject().addFacetReference(ref);
				viewer.add(ref);
				markPageModified();
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

	}

	/**
	 * Triggered when the add button is pushed
	 * 
	 */
	protected void addButtonSelected(SelectionEvent event) {
		if (getPage().getEditorInput() instanceof IFileEditorInput) {
			IFileEditorInput input = (IFileEditorInput) getPage()
					.getEditorInput();
			FacetSelectionDialog dialog = new FacetSelectionDialog(getSection()
					.getShell(), true, false);
			dialog
					.setInput(input.getFile().getProject().getLocation()
							.toFile());
			dialog.setDoubleClickSelects(true);
			dialog.setTitle("Select Facets");

			if (dialog.open() == Window.OK) {
				Object[] toAdd = dialog.getResult();
				for (int i = 0; i < toAdd.length; i++) {
					File file = (File) toAdd[i];

					try {
						String relative = Util.getRelativePath(file, input
								.getFile().getProject().getLocation().toFile());
						ITigerstripeModelProject handle = getTSProject();
						IFacetReference dep = handle
								.makeFacetReference(relative);
						handle.addFacetReference(dep);
						viewer.add(dep);
						markPageModified();
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					} catch (IOException e) {
						EclipsePlugin.log(e);
					}
				}
				updateMaster();
			}
		}
	}

	protected void markPageModified() {
		DescriptorEditor editor = (DescriptorEditor) getPage().getEditor();
		editor.pageModified();
	}

	/**
	 * Triggered when the remove button is pushed
	 * 
	 */
	protected void removeButtonSelected(SelectionEvent event) {
		TableItem[] selectedItems = viewer.getTable().getSelection();
		IFacetReference[] selectedFields = new IFacetReference[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (IFacetReference) selectedItems[i].getData();
		}

		String message = "Do you really want to remove ";
		if (selectedFields.length > 1) {
			message = message + "these " + selectedFields.length
					+ " Facet References?";
		} else {
			message = message + "this Facet Reference?";
		}

		MessageDialog msgDialog = new MessageDialog(getBody().getShell(),
				"Remove Facet References", null, message,
				MessageDialog.QUESTION, new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {
			viewer.remove(selectedFields);
			ITigerstripeModelProject handle = getTSProject();
			try {
				for (IFacetReference ref : selectedFields) {
					handle.removeFacetReference(ref);
				}
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			markPageModified();
		}
		updateMaster();
	}

	protected void mergeFacetButtonSelected() {
		ITigerstripeModelProject handle = getTSProject();
		try {
			handle.getProjectDetails().getProperties().setProperty(
					IProjectDetails.MERGE_FACETS,
					Boolean.toString(mergeFacets.getSelection()));
			markPageModified();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	/**
	 * Updates the current state of the master
	 * 
	 */
	protected void updateMaster() {
		try {
			if (getTSProject().getReferencedProjects().length != 0
					|| getTSProject().getDependencies().length > 1) { // there
				// always
				// the
				// OSSJ-base-dep
				addFromDependencies.setEnabled(true);
			} else {
				addFromDependencies.setEnabled(false);
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		// Updates the state of the Remove Button
		if (viewer.getSelection() != null && !viewer.getSelection().isEmpty()) {
			removeAttributeButton.setEnabled(true);
		} else {
			removeAttributeButton.setEnabled(false);
		}
		try {
			String merge = getTSProject().getProjectDetails().getProperty(
					IProjectDetails.MERGE_FACETS,
					IProjectDetails.MERGE_FACETS_DEFAULT);
			mergeFacets.setSelection("true".equalsIgnoreCase(merge));
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(FacetReference.class, // TODO remove the
				// dependency on
				// Core and use API instead
				new FacetReferencesDetailsPage(this));
	}

	protected void createToolBarActions(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();

		Action haction = new Action("hor", IAction.AS_RADIO_BUTTON) {
			@Override
			public void run() {
				sashForm.setOrientation(SWT.HORIZONTAL);
				form.reflow(true);
			}
		};

		haction.setChecked(true);
		haction.setToolTipText("Horizontal Orientation");

		Action vaction = new Action("ver", IAction.AS_RADIO_BUTTON) {
			@Override
			public void run() {
				sashForm.setOrientation(SWT.VERTICAL);
				form.reflow(true);
			}
		};
		vaction.setChecked(false);
		vaction.setToolTipText("Vertical Orientation");

		form.getToolBarManager().add(haction);
		form.getToolBarManager().add(vaction);
	}

	private void createDetailsPart(final IManagedForm mform, Composite parent) {
		detailsPart = new DetailsPart(mform, parent, SWT.NULL);
		mform.addPart(detailsPart);
		registerPages(detailsPart);
	}

	/**
	 * Commits the part. Subclasses should call 'super' when overriding.
	 * 
	 * @param onSave
	 *            <code>true</code> if the request to commit has arrived as a
	 *            result of the 'save' action.
	 */
	@Override
	public void commit(boolean onSave) {
		super.commit(onSave);
		detailsPart.commit(onSave);
	}

	@Override
	public void refresh() {
		AbstractTigerstripeProjectHandle handle = (AbstractTigerstripeProjectHandle) getTSProject();
		viewer.setInput(handle);

		viewer.refresh();
		updateMaster();
	}

	@Override
	public boolean isReadonly() {
		return super.isReadonly();
	}
}
