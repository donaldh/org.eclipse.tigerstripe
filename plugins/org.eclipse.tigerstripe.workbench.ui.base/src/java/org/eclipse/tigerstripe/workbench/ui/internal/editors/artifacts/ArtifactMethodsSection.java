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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import java.util.Arrays;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.ComponentNameProvider;
import org.eclipse.tigerstripe.workbench.internal.core.model.Method;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.undo.ModelUndoableEdit;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class ArtifactMethodsSection extends ArtifactSectionPart implements
		IFormPart {

	protected DetailsPart detailsPart;

	public ArtifactMethodsSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit, IArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider, int style) {
		super(page, parent, toolkit, labelProvider, contentProvider,
				ExpandableComposite.TWISTIE | style);
		setTitle("Methods");
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
		sashForm.setToolTipText("Define/Edit methods for this Artifact.");
		sashForm.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createMasterPart(managedForm, sashForm);
		createDetailsPart(managedForm, sashForm);
		// createToolBarActions(managedForm);
		sashForm.setWeights(new int[] { 35, 65 });
		form.updateToolBar();

		getSection().setClient(body);
		getToolkit().paintBordersFor(body);
	}

	class MasterContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IAbstractArtifact) {
				IAbstractArtifact artifact = (IAbstractArtifact) inputElement;
				return artifact.getMethods().toArray();
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
			IMethod method = (IMethod) obj;
			return method.getLabelString();
		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}
	}

	// ====================================================================
	private TableViewer viewer;
	TableColumn nameColumn;
	private Button addAttributeButton;
	private Button upAttributeButton;
	private Button downAttributeButton;
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
		FormLayout layout = new FormLayout();
		sectionClient.setLayout(layout);

		Table t = toolkit.createTable(sectionClient, SWT.NULL);
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.bottom = new FormAttachment(100, -150);
		fd.left = new FormAttachment(0, 10);
		fd.right = new FormAttachment(80);
		fd.width = 100;
		t.setLayoutData(fd);

		t.setHeaderVisible(true);
		t.setLinesVisible(true);

		// Make a header for the table
		nameColumn = new TableColumn(t, SWT.NULL);
		nameColumn.setText("Name");
		nameColumn.setWidth(250);

		nameColumn.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				// determine new sort column and direction
				TableColumn sortColumn = viewer.getTable().getSortColumn();
				TableColumn currentColumn = (TableColumn) e.widget;
				int dir = viewer.getTable().getSortDirection();

				if (sortColumn == currentColumn) {
					dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
				} else {
					viewer.getTable().setSortColumn(currentColumn);
					dir = SWT.UP;
				}

				viewer.getTable().setSortDirection(dir);
				viewer.setSorter(new Sorter(dir));
				TableItem[] allItems = viewer.getTable().getItems();
				IMethod[] newFields = new IMethod[allItems.length];
				for (int i = 0; i < newFields.length; i++) {
					newFields[i] = (IMethod) allItems[i].getData();
				}
				getIArtifact().setMethods(Arrays.asList(newFields));
				refresh();
				updateMaster();
				markPageModified();
			}
		});

		addAttributeButton = toolkit.createButton(sectionClient, "Add",
				SWT.PUSH);
		addAttributeButton.setEnabled(!isReadonly());
		fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(t, 5);
		fd.right = new FormAttachment(100, -5);
		addAttributeButton.setLayoutData(fd);
		addAttributeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				addButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});
		upAttributeButton = toolkit.createButton(sectionClient, "Up", SWT.PUSH);
		upAttributeButton.setEnabled(!getIArtifact().isReadonly());
		fd = new FormData();
		fd.top = new FormAttachment(addAttributeButton, 5);
		fd.left = new FormAttachment(t, 5);
		fd.right = new FormAttachment(100, -5);
		upAttributeButton.setLayoutData(fd);
		upAttributeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				upButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		downAttributeButton = toolkit.createButton(sectionClient, "Down",
				SWT.PUSH);
		downAttributeButton.setEnabled(!getIArtifact().isReadonly());
		fd = new FormData();
		fd.top = new FormAttachment(upAttributeButton, 5);
		fd.left = new FormAttachment(t, 5);
		fd.right = new FormAttachment(100, -5);
		downAttributeButton.setLayoutData(fd);
		downAttributeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				downButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		removeAttributeButton = toolkit.createButton(sectionClient, "Remove",
				SWT.PUSH);
		removeAttributeButton.setEnabled(!getIArtifact().isReadonly());
		fd = new FormData();
		fd.top = new FormAttachment(downAttributeButton, 5);
		fd.left = new FormAttachment(t, 5);
		fd.right = new FormAttachment(100, -5);
		removeAttributeButton.setLayoutData(fd);

		removeAttributeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				removeButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		Label l = toolkit.createLabel(sectionClient, "", SWT.NULL);
		fd = new FormData();
		fd.top = new FormAttachment(removeAttributeButton, 5);
		fd.left = new FormAttachment(t, 5);
		fd.right = new FormAttachment(100, -5);
		fd.height = 250;
		l.setLayoutData(fd);

		final IFormPart part = this;
		viewer = new TableViewer(t);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				selIndex = viewer.getTable().getSelectionIndex();
				managedForm.fireSelectionChanged(part, event.getSelection());
				viewerSelectionChanged(event);
			}
		});
		viewer.setContentProvider(new MasterContentProvider());
		viewer.setLabelProvider(new MasterLabelProvider());
		viewer.setInput(((ArtifactEditorBase) getPage().getEditor())
				.getIArtifact());

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
		IAbstractArtifact artifact = getIArtifact();
		IMethod newMethod = artifact.makeMethod();

		ComponentNameProvider nameFactory = ComponentNameProvider.getInstance();
		String newMethodName = nameFactory.getNewMethodName(artifact);

		newMethod.setName(newMethodName);
		newMethod.setVoid(true);
		IType type = newMethod.makeType();
		type.setFullyQualifiedName("void");
		type.setTypeMultiplicity(EMultiplicity.ZERO_ONE);
		newMethod.setReturnType(type);
		newMethod.setVoid(true);
		newMethod.setVisibility(EVisibility.PUBLIC);

		// Add the item after the current selection (if there is one, and its
		// not the last thing in the table.)
		if (viewer.getTable().getSelectionCount() == 0
				|| viewer.getTable().getSelectionIndex() == viewer.getTable()
						.getItemCount()) {
			viewer.add(newMethod);
			TableItem[] allItems = this.viewer.getTable().getItems();
			IMethod[] newFields = new IMethod[allItems.length];
			for (int i = 0; i < newFields.length; i++) {
				newFields[i] = (IMethod) allItems[i].getData();
			}
			getIArtifact().setMethods(Arrays.asList(newFields));

		} else {
			int position = viewer.getTable().getSelectionIndex();
			TableItem[] allItems = this.viewer.getTable().getItems();

			IMethod[] allFields = new IMethod[allItems.length];
			IMethod[] newFields = new IMethod[allItems.length + 1];
			for (int i = 0; i <= position; i++) {
				newFields[i] = (IMethod) allItems[i].getData();
			}
			newFields[position + 1] = newMethod;

			for (int i = position + 2; i < newFields.length; i++) {
				newFields[i] = (IMethod) allItems[i - 1].getData();
			}
			getIArtifact().setMethods(Arrays.asList(newFields));
		}

		refresh();

		viewer.setSelection(new StructuredSelection(newMethod), true);
		markPageModified();
		updateMaster();

		// Record Add Edit
		try {
			URI artURI = (URI) getIArtifact().getAdapter(URI.class);
			URI attrURI = (URI) newMethod.getAdapter(URI.class);
			ModelUndoableEdit edit = new ModelUndoableEdit(artURI,
					IModelChangeDelta.ADD,
					newMethod.getClass().getSimpleName(), null, attrURI,
					getIArtifact().getProject());
			((TigerstripeFormEditor) getPage().getEditor()).getUndoManager()
					.addEdit(edit);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

	}

	/**
	 * Triggered when the remove button is pushed
	 * 
	 */
	protected void removeButtonSelected(SelectionEvent event) {
		TableItem[] selectedItems = viewer.getTable().getSelection();
		IMethod[] selectedMethods = new IMethod[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedMethods[i] = (IMethod) selectedItems[i].getData();
		}

		String message = "Do you really want to remove ";
		if (selectedMethods.length > 1) {
			message = message + "these " + selectedMethods.length + " methods?";
		} else {
			message = message + "this method?";
		}

		MessageDialog msgDialog = new MessageDialog(getBody().getShell(),
				"Remove method", null, message, MessageDialog.QUESTION,
				new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {

			URI[] methodURIs = new URI[selectedMethods.length];
			String[] methodTypes = new String[selectedMethods.length];
			int index = 0;
			for (IMethod method : selectedMethods) {
				methodURIs[index] = (URI) method.getAdapter(URI.class);
				methodTypes[index] = method.getClass().getSimpleName();
				index++;
			}

			// remove now
			viewer.remove(selectedMethods);
			getIArtifact().removeMethods(Arrays.asList(selectedMethods));
			markPageModified();

			URI artURI = (URI) getIArtifact().getAdapter(URI.class);
			for (int i = 0; i < selectedMethods.length; i++) {
				try {
					ModelUndoableEdit edit = new ModelUndoableEdit(artURI,
							IModelChangeDelta.REMOVE, methodTypes[i],
							methodURIs[i], null, getIArtifact().getProject());
					((TigerstripeFormEditor) getPage().getEditor())
							.getUndoManager().addEdit(edit);
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
		updateMaster();
	}

	/**
	 * Triggered when the up button is pushed
	 * 
	 */
	protected void upButtonSelected(SelectionEvent event) {

		// If you go up/down then the sort order ion the viewer has to be
		// removed!
		viewer.setSorter(null);

		TableItem[] selectedItems = viewer.getTable().getSelection();
		IMethod[] selectedFields = new IMethod[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (IMethod) selectedItems[i].getData();
		}
		TableItem[] allItems = this.viewer.getTable().getItems();

		IMethod[] allFields = new IMethod[allItems.length];
		IMethod[] newFields = new IMethod[allItems.length];

		for (int i = 0; i < allFields.length; i++) {
			newFields[i] = (IMethod) allItems[i].getData();
			if (allItems[i].getData().equals(selectedFields[0]) && i != 0) {
				newFields[i] = newFields[i - 1];
				newFields[i - 1] = (IMethod) allItems[i].getData();
			}
		}

		// TODO - This should be wrapped in case of error?
		selIndex = selIndex - 1;
		getIArtifact().setMethods(Arrays.asList(newFields));
		markPageModified();
		refresh();
		updateMaster();
	}

	/**
	 * Triggered when the down button is pushed
	 * 
	 */
	protected void downButtonSelected(SelectionEvent event) {

		// If you go up/down then the sort order ion the viewer has to be
		// removed!
		viewer.setSorter(null);

		TableItem[] selectedItems = viewer.getTable().getSelection();
		IMethod[] selectedFields = new IMethod[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (IMethod) selectedItems[i].getData();
		}
		TableItem[] allItems = this.viewer.getTable().getItems();

		IMethod[] allFields = new IMethod[allItems.length];
		IMethod[] newFields = new IMethod[allItems.length];

		for (int i = allFields.length - 1; i > -1; i--) {
			newFields[i] = (IMethod) allItems[i].getData();
			if (allItems[i].getData().equals(selectedFields[0])
					&& i != allFields.length - 1) {
				newFields[i] = newFields[i + 1];
				newFields[i + 1] = (IMethod) allItems[i].getData();
			}
		}

		// TODO - This should be wrapped in case of error?
		selIndex = selIndex + 1;
		getIArtifact().setMethods(Arrays.asList(newFields));
		markPageModified();
		refresh();
		updateMaster();
	}

	protected void markPageModified() {
		ArtifactEditorBase editor = (ArtifactEditorBase) getPage().getEditor();
		editor.pageModified();
	}

	/**
	 * Updates the current state of the master
	 * 
	 */
	public void updateMaster() {

		// Updates the state of the Remove Button
		if (viewer.getSelection() != null && !viewer.getSelection().isEmpty()) {
			removeAttributeButton.setEnabled(!isReadonly());
		} else {
			removeAttributeButton.setEnabled(false);
		}
	}

	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(Method.class, // TODO remove the dependency on
				// Core and use API instead
				new ArtifactMethodDetailsPage(getIArtifact().isReadonly()));
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

	private int selIndex = -1;

	@Override
	public void refresh() {
		viewer.setInput(((ArtifactEditorBase) getPage().getEditor())
				.getIArtifact());
		viewer.refresh(true);

		if (selIndex != -1) {
			Object refreshedMethod = viewer.getTable().getItem(selIndex)
					.getData();
			viewer.setSelection(new StructuredSelection(refreshedMethod), true);
		}
		updateMaster();
	}

	public DetailsPart getDetailsPart() {
		return detailsPart;
	}

}
