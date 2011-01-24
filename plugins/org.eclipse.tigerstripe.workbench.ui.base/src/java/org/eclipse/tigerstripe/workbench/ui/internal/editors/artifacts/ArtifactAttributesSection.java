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
import org.eclipse.tigerstripe.metamodel.impl.IPrimitiveTypeImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.ComponentNameProvider;
import org.eclipse.tigerstripe.workbench.internal.core.model.Field;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
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

public class ArtifactAttributesSection extends ModelComponentSectionPart
		implements IFormPart {

	class MasterContentProvider implements IStructuredContentProvider {

		public void dispose() {
		}

		public Object[] getElements(Object inputElement) {
			Collection<IAbstractArtifact> hierarhy = getHierarchy(false);
			List<IField> fields = new ArrayList<IField>();
			for (IAbstractArtifact arti : hierarhy) {
				fields.addAll(arti.getFields());
			}
			return fields.toArray();
		}

		public void inputChanged(final Viewer viewer, Object oldInput,
				Object newInput) {
			if (newInput instanceof IAbstractArtifact) {
				fieldsInModel = new HashSet<IField>(
						((IAbstractArtifact) newInput).getFields());
			}
		}
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
			IField field = (IField) obj;
			if (fieldsInModel.contains(field)) {
				return field.getName();
			} else {
				return field.getName() + "("
						+ field.getContainingArtifact().getName() + ")";
			}
		}

		public Color getForeground(Object element, int columnIndex) {
			if (!fieldsInModel.contains(element)) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
			} else {
				return null;
			}
		}
	}

	private Button addAttributeButton;

	protected DetailsPart detailsPart;

	private Button downAttributeButton;

	private Set<IField> fieldsInModel = Collections.emptySet();

	TableColumn nameColumn;

	private Button removeAttributeButton;

	protected SashForm sashForm;

	private int selIndex = -1;

	private Table table;

	private Composite tableComposite;

	private Button upAttributeButton;

	// ====================================================================
	private TableViewer viewer;

	public ArtifactAttributesSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			IArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider, int style) {
		super(page, parent, toolkit, labelProvider, contentProvider,
				ExpandableComposite.TWISTIE | style);
		setTitle("&Attributes");
		createContent();
		updateMaster();
	}

	/**
	 * Triggered when the add button is pushed
	 */
	protected void addButtonSelected(SelectionEvent event) {
		IAbstractArtifact artifact = getIArtifact();
		IField newField = artifact.makeField();

		ComponentNameProvider nameFactory = ComponentNameProvider.getInstance();
		String newFieldName = nameFactory.getNewFieldName(artifact);
		newField.setName(newFieldName);
		IType defaultType = newField.makeType();
		try {
			defaultType.setFullyQualifiedName(getDefaultTypeName());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
			MessageDialog.openWarning(
					getPage().getPartControl().getShell(),
					"Default "
							+ ArtifactMetadataFactory.INSTANCE.getMetadata(
									IPrimitiveTypeImpl.class.getName())
									.getLabel(null) + " For Parameter", e
							.getMessage());
			return;
		}

		defaultType.setTypeMultiplicity(IModelComponent.EMultiplicity.ONE);
		newField.setType(defaultType);
		newField.setRefBy(IField.REFBY_VALUE);
		newField.setVisibility(EVisibility.PUBLIC);

		// getIArtifact().addField(newField);

		// Add the item after the current selection (if there is one, and its
		// not the last thing in the table.)
		TableItem[] selection = viewer.getTable().getSelection();
		if (selection.length == 0) {
			List<IField> newFields = new ArrayList<IField>(getIArtifact()
					.getFields());
			newFields.add(newField);
			getIArtifact().setFields(newFields);
		} else {

			List<IField> newFields = new ArrayList<IField>(getIArtifact()
					.getFields());

			int afterPos = newFields.indexOf(selection[selection.length - 1]
					.getData());

			if (afterPos >= 0 && afterPos < newFields.size()) {
				newFields.add(afterPos + 1, newField);
			} else {
				newFields.add(newField);
			}
			getIArtifact().setFields(newFields);
		}

		refresh();

		viewer.setSelection(new StructuredSelection(newField), true);
		markPageModified();
		updateMaster();

		// Record Add Edit
		try {
			URI artURI = (URI) getIArtifact().getAdapter(URI.class);
			URI attrURI = artURI.appendFragment(newFieldName);
			ModelUndoableEdit edit = new ModelUndoableEdit(artURI,
					IModelChangeDelta.ADD, newField.getClass().getSimpleName(),
					null, attrURI, getIArtifact().getProject());
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
		GridData gridData = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_VERTICAL);
		gridData.verticalSpan = 4;
		gridData.widthHint = MASTER_TABLE_COMPONENT_WIDTH;
		tableComposite.setLayoutData(gridData);
		TableColumnLayout tcLayout = new TableColumnLayout();
		tableComposite.setLayout(tcLayout);

		table = toolkit.createTable(tableComposite, SWT.NONE);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Make a header for the table
		nameColumn = new TableColumn(table, SWT.NONE);
		nameColumn.setText("Name");

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
					List<IField> newFields = new ArrayList<IField>(
							allItems.length);
					for (int i = 0; i < allItems.length; i++) {
						IField field = (IField) allItems[i].getData();
						if (fieldsInModel.contains(field)) {
							newFields.add(field);
						}
					}
					getIArtifact().setFields(newFields);
					refresh();
					updateMaster();
					markPageModified();
				}
			});
		}
		tcLayout.setColumnData(nameColumn, new ColumnWeightData(100, false));

		addAttributeButton = toolkit.createButton(sectionClient, "Add",
				SWT.PUSH);
		// Support for testing
		addAttributeButton.setData("name", "Add_Attribute");
		addAttributeButton.setEnabled(!getIArtifact().isReadonly());
		addAttributeButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_BEGINNING));
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
		downAttributeButton.setEnabled(!getIArtifact().isReadonly());
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
		removeAttributeButton.setData("name", "Remove_Attribute");

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

		IField[] selectedFields = new IField[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (IField) selectedItems[i].getData();
		}

		List<IField> newFields = new ArrayList<IField>(getIArtifact()
				.getFields());

		boolean wasSwap = false;
		for (IField selectedField : selectedFields) {

			int toDownIndex = newFields.indexOf(selectedField);

			if (toDownIndex >= 0 && toDownIndex < newFields.size() - 1) {
				Collections.swap(newFields, toDownIndex, toDownIndex + 1);
				wasSwap = true;
			}
		}

		// TODO - This should be wrapped in case of error?
		if (wasSwap) {
			++selIndex;
		}
		getIArtifact().setFields(newFields);
		markPageModified();
		refresh();
		updateMaster();
	}

	/**
	 * Gets the default attribute type from the active profile.
	 */
	private String getDefaultTypeName() throws TigerstripeException {
		IWorkbenchProfile profile = TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile();
		return profile.getDefaultPrimitiveTypeString();
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
			if (!fieldsInModel.contains((selectedItems[i].getData()))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void refresh() {
		int si = selIndex;
		viewer.setInput(((ArtifactEditorBase) getPage().getEditor())
				.getIArtifact());
		selIndex = si;
		viewer.refresh();
		if (selIndex != -1) {
			Object refreshedMethod = viewer.getTable().getItem(selIndex)
					.getData();
			viewer.setSelection(new StructuredSelection(refreshedMethod), true);
		}
		updateMaster();
	}

	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(Field.class, // TODO remove the dependency on
				// Core and use API instead
				new ArtifactAttributeDetailsPage(this, getIArtifact()
						.isReadonly()));
	}

	/**
	 * Triggered when the remove button is pushed
	 * 
	 */
	protected void removeButtonSelected(SelectionEvent event) {
		TableItem[] selectedItems = viewer.getTable().getSelection();
		IField[] selectedFields = new IField[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (IField) selectedItems[i].getData();
		}

		String message = "Do you really want to remove ";
		if (selectedFields.length > 1) {
			message = message + "these " + selectedFields.length
					+ " attributes?";
		} else {
			message = message + "this attribute?";
		}

		MessageDialog msgDialog = new MessageDialog(getBody().getShell(),
				"Remove attribute", null, message, MessageDialog.QUESTION,
				new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {

			URI[] fieldURIs = new URI[selectedFields.length];
			String[] fieldTypes = new String[selectedFields.length];
			int index = 0;
			for (IField field : selectedFields) {
				fieldURIs[index] = (URI) field.getAdapter(URI.class);
				fieldTypes[index] = field.getClass().getSimpleName();
				index++;
			}

			viewer.remove(selectedFields);
			getIArtifact().removeFields(Arrays.asList(selectedFields));
			markPageModified();

			URI artURI = (URI) getIArtifact().getAdapter(URI.class);
			for (int i = 0; i < selectedFields.length; i++) {
				try {
					ModelUndoableEdit edit = new ModelUndoableEdit(artURI,
							IModelChangeDelta.REMOVE, fieldTypes[i],
							fieldURIs[i], null, getIArtifact().getProject());
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
	 * FIXME Used only by ArtifactAttributeDetailsPage. Just workaround to avoid
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

		IField[] selectedFields = new IField[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (IField) selectedItems[i].getData();
		}

		List<IField> newFields = new ArrayList<IField>(getIArtifact()
				.getFields());

		boolean wasSwap = false;
		for (IField selectedField : selectedFields) {

			int toUpIndex = newFields.indexOf(selectedField);

			if (toUpIndex > 0) {
				Collections.swap(newFields, toUpIndex, toUpIndex - 1);
				wasSwap = true;
			}
		}

		// TODO - This should be wrapped in case of error?
		if (wasSwap) {
			--selIndex;
		}
		getIArtifact().setFields(newFields);
		markPageModified();
		refresh();
		updateMaster();
	}

	private void updateButtons() {

		List<IField> fields = new ArrayList<IField>(getIArtifact().getFields());

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
		return false;
	}

}
