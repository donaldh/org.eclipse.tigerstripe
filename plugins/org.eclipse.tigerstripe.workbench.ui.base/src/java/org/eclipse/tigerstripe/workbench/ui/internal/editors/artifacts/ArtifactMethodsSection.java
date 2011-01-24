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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.ComponentNameProvider;
import org.eclipse.tigerstripe.workbench.internal.core.model.Method;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.undo.ModelUndoableEdit;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class ArtifactMethodsSection extends ModelComponentSectionPart implements
		IFormPart {

	class MasterContentProvider implements IStructuredContentProvider {

		public void dispose() {

		}

		public Object[] getElements(Object inputElement) {
			Collection<IAbstractArtifact> hierarchy = getHierarchy(true);

			List<IMethod> methods = new ArrayList<IMethod>();

			for (IAbstractArtifact arti : hierarchy) {
				methods.addAll(arti.getMethods());
			}
			return methods.toArray();
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (newInput instanceof IAbstractArtifact) {
				methodsInModel = new HashSet<IMethod>(
						((IAbstractArtifact) newInput).getMethods());
			}
		}
	}

	public void getAllImplementedArtifacts(IAbstractArtifact artifact,
			Set<IAbstractArtifact> hierarchy) {
	}

	class MasterLabelProvider extends LabelProvider implements
			ITableLabelProvider, ITableColorProvider {

		public Color getBackground(Object element, int columnIndex) {
			return null;
		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}

		public String getColumnText(Object obj, int index) {
			IMethod method = (IMethod) obj;
			if (methodsInModel.contains(method)) {
				return method.getLabelString();
			} else {
				return method.getLabelString() + "("
						+ method.getContainingArtifact().getName() + ")";
			}
		}

		public Color getForeground(Object element, int columnIndex) {
			if (!methodsInModel.contains(element)) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
			} else {
				return null;
			}
		}
	}

	private Button addAttributeButton;

	protected DetailsPart detailsPart;

	private Button downAttributeButton;

	private Set<IMethod> methodsInModel = Collections.emptySet();

	TableColumn nameColumn;

	private Button removeAttributeButton;

	protected SashForm sashForm;
	private int selIndex = -1;
	private Table table;
	private Composite tableComposite;
	private Button upAttributeButton;
	// ====================================================================
	private TableViewer viewer;

	public ArtifactMethodsSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit, IArtifactFormLabelProvider labelProvider,

			IOssjArtifactFormContentProvider contentProvider, int style) {
		super(page, parent, toolkit, labelProvider, contentProvider,
				ExpandableComposite.TWISTIE | style);
		setTitle("Methods");
		createContent();
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
		TableItem[] selection = viewer.getTable().getSelection();
		if (selection.length == 0) {
			List<IMethod> newMethods = new ArrayList<IMethod>(getIArtifact()
					.getMethods());
			newMethods.add(newMethod);
			getIArtifact().setMethods(newMethods);
		} else {

			List<IMethod> newMethods = new ArrayList<IMethod>(getIArtifact()
					.getMethods());

			int afterPos = newMethods.indexOf(selection[selection.length - 1]
					.getData());

			if (afterPos >= 0 && afterPos < newMethods.size()) {
				newMethods.add(afterPos + 1, newMethod);
			} else {
				newMethods.add(newMethod);
			}
			getIArtifact().setMethods(newMethods);
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

	private void createDetailsPart(final IManagedForm mform, Composite parent) {
		detailsPart = new DetailsPart(mform, parent, SWT.NULL);
		mform.addPart(detailsPart);
		registerPages(detailsPart);
	}

	/**
	 * Creates the content of the master/details block inside the managed form.
	 * This method should be called as late as possible inside the parent part.
	 * 
	 * @param managedForm
	 *            the managed form to create the block in
	 */
	@Override
	public void createInternalContent() {
		IManagedForm managedForm = getPage().getManagedForm();
		FormToolkit toolkit = getToolkit();

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		getSection().setLayoutData(td);

		Composite body = getToolkit().createComposite(getSection());
		body.setLayout(TigerstripeLayoutFactory.createClearGridLayout(1, false));
		sashForm = new SashForm(body, SWT.HORIZONTAL);
		toolkit.adapt(sashForm, false, false);
		sashForm.setMenu(body.getMenu());
		sashForm.setToolTipText("Define/Edit attributes for this Artifact.");
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		createMasterPart(managedForm, sashForm);
		createDetailsPart(managedForm, sashForm);

		sashForm.setWeights(new int[] { 1, 2 });

		getSection().setClient(body);
		getToolkit().paintBordersFor(body);
	}

	protected void createMasterPart(final IManagedForm managedForm,
			Composite parent) {

		FormToolkit toolkit = getToolkit();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);

		Composite sectionClient = toolkit.createComposite(section);
		GridLayout layout = new GridLayout(2, false);
		sectionClient.setLayout(layout);

		tableComposite = toolkit.createComposite(sectionClient, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 4;
		gd.widthHint = MASTER_TABLE_COMPONENT_WIDTH;
		tableComposite.setLayoutData(gd);
		TableColumnLayout tcLayout = new TableColumnLayout();
		tableComposite.setLayout(tcLayout);

		table = toolkit.createTable(tableComposite, SWT.NULL);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Make a header for the table
		nameColumn = new TableColumn(table, SWT.NULL);
		nameColumn.setText("Name");
		tcLayout.setColumnData(nameColumn, new ColumnWeightData(100, false));

		if (!isReadonly()) {
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
					viewer.setSorter(new Sorter(dir, getIArtifact()));
					TableItem[] allItems = viewer.getTable().getItems();

					List<IMethod> newMethods = new ArrayList<IMethod>(
							allItems.length);
					for (int i = 0; i < allItems.length; i++) {
						IMethod method = (IMethod) allItems[i].getData();
						if (methodsInModel.contains(method)) {
							newMethods.add(method);
						}
					}
					getIArtifact().setMethods(newMethods);
					refresh();
					updateMaster();
					markPageModified();
				}
			});
		}

		addAttributeButton = toolkit.createButton(sectionClient, "Add",
				SWT.PUSH);
		// Support for testing
		addAttributeButton.setData("name", "Add_Method");
		addAttributeButton.setEnabled(!isReadonly());
		addAttributeButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_BEGINNING));
		;
		addAttributeButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}

			public void widgetSelected(SelectionEvent event) {
				addButtonSelected(event);
			}
		});
		upAttributeButton = toolkit.createButton(sectionClient, "Up", SWT.PUSH);
		upAttributeButton.setEnabled(!getIArtifact().isReadonly());
		upAttributeButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_BEGINNING));
		upAttributeButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}

			public void widgetSelected(SelectionEvent event) {
				upButtonSelected(event);
			}
		});

		downAttributeButton = toolkit.createButton(sectionClient, "Down",
				SWT.PUSH);
		downAttributeButton.setEnabled(!isReadonly());
		downAttributeButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_BEGINNING));
		downAttributeButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}

			public void widgetSelected(SelectionEvent event) {
				downButtonSelected(event);
			}
		});

		removeAttributeButton = toolkit.createButton(sectionClient, "Remove",
				SWT.PUSH);
		// Support for testing
		removeAttributeButton.setData("name", "Remove_Method");
		removeAttributeButton.setEnabled(!getIArtifact().isReadonly());
		removeAttributeButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_BEGINNING));

		removeAttributeButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}

			public void widgetSelected(SelectionEvent event) {
				removeButtonSelected(event);
			}
		});

		final IFormPart part = this;
		viewer = new TableViewer(table);
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
	 * Triggered when the down button is pushed
	 * 
	 */
	protected void downButtonSelected(SelectionEvent event) {

		TableItem[] selectedItems = viewer.getTable().getSelection();
		if (!onlyNative(selectedItems)) {
			return;
		}

		// If you go up/down then the sort order ion the viewer has to be
		// removed!
		viewer.setSorter(null);

		IMethod[] selectedMethods = new IMethod[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedMethods[i] = (IMethod) selectedItems[i].getData();
		}

		List<IMethod> newMethods = new ArrayList<IMethod>(getIArtifact()
				.getMethods());

		boolean wasSwap = false;
		for (IMethod selectedMethod : selectedMethods) {

			int toDownIndex = newMethods.indexOf(selectedMethod);

			if (toDownIndex >= 0 && toDownIndex < newMethods.size() - 1) {
				Collections.swap(newMethods, toDownIndex, toDownIndex + 1);
				wasSwap = true;
			}
		}

		// TODO - This should be wrapped in case of error?
		if (wasSwap) {
			++selIndex;
		}
		getIArtifact().setMethods(newMethods);
		markPageModified();
		refresh();
		updateMaster();
	}

	public DetailsPart getDetailsPart() {
		return detailsPart;
	}

	@Override
	public TableViewer getViewer() {
		return this.viewer;
	}

	protected void markPageModified() {
		ArtifactEditorBase editor = (ArtifactEditorBase) getPage().getEditor();
		editor.pageModified();
	}

	@Override
	protected void onExtendedArtifactChange(IAbstractArtifact artifact) {
		if (viewer != null && !viewer.getTable().isDisposed()) {
			viewer.refresh();
		}
	}

	private boolean onlyNative(TableItem[] selectedItems) {
		for (int i = 0; i < selectedItems.length; i++) {
			if (!methodsInModel.contains((selectedItems[i].getData()))) {
				return false;
			}
		}
		return true;
	}

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

	protected void registerPages(DetailsPart detailsPart) {
		detailsPart
				.registerPage(
						Method.class, // TODO remove the dependency on
						// Core and use API instead
						new ArtifactMethodDetailsPage(this, getIArtifact()
								.isReadonly()));
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
	 * FIXME Used only by ArtifactMethodDetailsPage. Just workaround to avoid
	 * appearing scrolls on details part.
	 */
	void setMinimumHeight(int value) {
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 4;
		gd.widthHint = MASTER_TABLE_COMPONENT_WIDTH;
		gd.minimumHeight = value;
		tableComposite.setLayoutData(gd);
		getManagedForm().reflow(true);
	}

	/**
	 * Triggered when the up button is pushed
	 * 
	 */
	protected void upButtonSelected(SelectionEvent event) {

		TableItem[] selectedItems = viewer.getTable().getSelection();
		if (!onlyNative(selectedItems)) {
			return;
		}

		// If you go up/down then the sort order ion the viewer has to be
		// removed!
		viewer.setSorter(null);

		IMethod[] selectedFields = new IMethod[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (IMethod) selectedItems[i].getData();
		}

		List<IMethod> newMethods = new ArrayList<IMethod>(getIArtifact()
				.getMethods());

		boolean wasSwap = false;
		for (IMethod selectedField : selectedFields) {

			int toUpIndex = newMethods.indexOf(selectedField);

			if (toUpIndex > 0) {
				Collections.swap(newMethods, toUpIndex, toUpIndex - 1);
				wasSwap = true;
			}
		}

		// TODO - This should be wrapped in case of error?
		if (wasSwap) {
			--selIndex;
		}
		getIArtifact().setMethods(newMethods);

		markPageModified();
		refresh();
		updateMaster();
	}

	private void updateButtons() {

		List<IMethod> fields = new ArrayList<IMethod>(getIArtifact()
				.getMethods());

		TableItem[] selection = viewer.getTable().getSelection();

		boolean readonly = getIArtifact().isReadonly();
		boolean empty = selection.length == 0;
		boolean onlyNative = onlyNative(selection);

		boolean singleUpperSelection = viewer.getTable().getSelectionCount() == 1
				&& fields.indexOf((selection[0].getData())) == 0;

		boolean singleDownSelection = viewer.getTable().getSelectionCount() == 1
				&& fields.indexOf((selection[0].getData())) == fields.size() - 1;

		upAttributeButton.setEnabled(!empty && !readonly && onlyNative
				&& !singleUpperSelection);
		downAttributeButton.setEnabled(!empty && !readonly && onlyNative
				&& !singleDownSelection);
		removeAttributeButton.setEnabled(!empty && !readonly && onlyNative);
	}

	/**
	 * Updates the current state of the master
	 * 
	 */
	@Override
	public void updateMaster() {
		updateButtons();
	}

	/**
	 * Updates the master's side based on the selection on the list of
	 * attributes
	 * 
	 */
	protected void viewerSelectionChanged(SelectionChangedEvent event) {
		updateMaster();
		updateButtons();
	}

	@Override
	protected boolean isListenImplemented() {
		return true;
	}

}
