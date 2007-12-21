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

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.api.contract.useCase.IUseCaseReference;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.contract.useCase.UseCaseReference;
import org.eclipse.tigerstripe.core.util.Util;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.eclipse.runtime.images.TigerstripePluginImages;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.UseCaseSelectionDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.segment.SegmentEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.segment.TigerstripeSegmentSectionPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class UseCaseSection extends TigerstripeSegmentSectionPart implements
		IFormPart {

	protected DetailsPart detailsPart;

	public UseCaseSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR);
		setTitle("&Use Cases");
		getSection().marginWidth = 10;
		getSection().marginHeight = 5;
		getSection().clientVerticalSpacing = 4;

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

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		getSection().setLayoutData(td);

		Composite body = getToolkit().createComposite(getSection());
		GridLayout layout = new GridLayout();
		layout.marginWidth = 5;
		layout.marginHeight = 5;
		body.setLayout(layout);
		sashForm = new SashForm(body, SWT.HORIZONTAL);
		toolkit.adapt(sashForm, false, false);
		sashForm.setMenu(body.getMenu());
		sashForm.setToolTipText("Define/Edit attributes for this Artifact.");
		sashForm.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createMasterPart(managedForm, sashForm);
		createDetailsPart(managedForm, sashForm);
		createToolBarActions(managedForm);
		sashForm.setWeights(new int[] { 30, 70 });
		form.updateToolBar();

		getSection().setClient(body);
		getToolkit().paintBordersFor(body);
	}

	class MasterContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IContractSegment) {
				IContractSegment facet = (IContractSegment) inputElement;
				IUseCaseReference[] useCases = facet.getUseCaseRefs();
				return useCases;
			}
			return new Object[0];
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
			IUseCaseReference uc = (IUseCaseReference) obj;
			if (uc != null && uc.canResolve()) {
				try {
					if (uc.isAbsolute())
						return uc.getURI().toASCIIString();
					else
						return uc.getProjectRelativePath();
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}

			if (uc != null && uc.getProjectRelativePath() != null)
				return uc.getProjectRelativePath();
			return "Unknown";
		}

		@Override
		public Image getImage(Object obj) {
			return TigerstripePluginImages
					.get(TigerstripePluginImages.CONTRACTUSECASE_ICON);
		}
	}

	// ====================================================================
	private TableViewer viewer;

	private Button addAttributeButton;

	private Button removeAttributeButton;

	public TableViewer getViewer() {
		return this.viewer;
	}

	protected void createMasterPart(final IManagedForm managedForm,
			Composite parent) {

		FormToolkit toolkit = getToolkit();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);

		Composite sectionClient = toolkit.createComposite(section);
		TableWrapLayout twlayout = new TableWrapLayout();
		twlayout.numColumns = 2;
		sectionClient.setLayout(twlayout);

		Table t = toolkit.createTable(sectionClient, SWT.NULL);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.rowspan = 2;
		td.heightHint = 150;
		t.setLayoutData(td);

		addAttributeButton = toolkit.createButton(sectionClient, "Add",
				SWT.PUSH);
		addAttributeButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		addAttributeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				addButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		removeAttributeButton = toolkit.createButton(sectionClient, "Remove",
				SWT.PUSH);
		removeAttributeButton.setLayoutData(new TableWrapData());
		removeAttributeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				removeButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		Label l = toolkit.createLabel(sectionClient, "", SWT.NULL);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		// td.heightHint = 100;
		l.setLayoutData(td);

		final IFormPart part = this;
		viewer = new TableViewer(t);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(part, event.getSelection());
				viewerSelectionChanged(event);
			}
		});
		viewer.setContentProvider(new MasterContentProvider());
		viewer.setLabelProvider(new MasterLabelProvider());

		IContractSegment handle = null;

		try {
			handle = getFacet();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		viewer.setInput(handle);

		toolkit.paintBordersFor(sectionClient);
		section.setClient(sectionClient);
	}

	/**
	 * Updates the master's side based on the selection on the list of
	 * attributes
	 * 
	 */
	protected void viewerSelectionChanged(SelectionChangedEvent event) {
		updateMaster();
	}

	/**
	 * Triggered when the add button is pushed
	 * 
	 */
	protected void addButtonSelected(SelectionEvent event) {
		if (getPage().getEditorInput() instanceof IFileEditorInput) {
			IFileEditorInput input = (IFileEditorInput) getPage()
					.getEditorInput();
			UseCaseSelectionDialog dialog = new UseCaseSelectionDialog(
					getSection().getShell(), true, false);
			dialog
					.setInput(input.getFile().getProject().getLocation()
							.toFile());
			dialog.setDoubleClickSelects(true);
			dialog.setTitle("Select Use Cases");

			if (dialog.open() == Window.OK) {
				Object[] toAdd = dialog.getResult();
				for (int i = 0; i < toAdd.length; i++) {
					File file = (File) toAdd[i];

					try {
						String relative = Util.getRelativePath(file, input
								.getFile().getProject().getLocation().toFile());

						IPath path = new Path(relative);
						IResource res = input.getFile().getParent().findMember(
								path);

						ITigerstripeProject handle = getContainingProject();

						IUseCaseReference dep = handle
								.makeIUseCaseReference(relative);
						((UseCasesPage) getPage()).getFacet().addUseCase(dep);
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
		SegmentEditor editor = (SegmentEditor) getPage().getEditor();
		editor.pageModified();
	}

	/**
	 * Triggered when the remove button is pushed
	 * 
	 */
	protected void removeButtonSelected(SelectionEvent event) {
		TableItem[] selectedItems = viewer.getTable().getSelection();
		IUseCaseReference[] selectedFields = new IUseCaseReference[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (IUseCaseReference) selectedItems[i].getData();
		}

		String message = "Do you really want to remove ";
		if (selectedFields.length > 1) {
			message = message + "these " + selectedFields.length
					+ " use case references?";
		} else {
			message = message + "this use case reference?";
		}

		MessageDialog msgDialog = new MessageDialog(getBody().getShell(),
				"Remove Use Case", null, message, MessageDialog.QUESTION,
				new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {
			viewer.remove(selectedFields);
			try {
				IContractSegment handle = getFacet();
				for (IUseCaseReference useCase : selectedFields) {
					handle.removeUseCase(useCase);
				}
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			markPageModified();
		}
		updateMaster();
	}

	/**
	 * Updates the current state of the master
	 * 
	 */
	protected void updateMaster() {

		// Updates the state of the Remove Button
		if (viewer.getSelection() != null && !viewer.getSelection().isEmpty()) {
			removeAttributeButton.setEnabled(true);
		} else {
			removeAttributeButton.setEnabled(false);
		}
	}

	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(UseCaseReference.class, // TODO remove the
				// dependency on
				// Core and use API instead
				new UseCaseDetailsPage());
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
		try {
			IContractSegment handle = getFacet();
			viewer.setInput(handle);
			viewer.refresh();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		updateMaster();
	}
}
