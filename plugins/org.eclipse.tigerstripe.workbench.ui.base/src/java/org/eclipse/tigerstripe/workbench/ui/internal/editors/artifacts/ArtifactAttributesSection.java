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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
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

public class ArtifactAttributesSection extends ModelComponentSectionPart implements
		IFormPart {

	protected DetailsPart detailsPart;

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

	protected SashForm sashForm;

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
		body
				.setLayout(TigerstripeLayoutFactory.createClearGridLayout(1,
						false));
		sashForm = new SashForm(body, SWT.HORIZONTAL);
		toolkit.adapt(sashForm, false, false);
		sashForm.setMenu(body.getMenu());
		sashForm.setToolTipText("Define/Edit attributes for this Artifact.");
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		createMasterPart(managedForm, sashForm);
		createDetailsPart(managedForm, sashForm);
        
        sashForm.setWeights(new int[] {1, 2});

		getSection().setClient(body);
		getToolkit().paintBordersFor(body);
	}

	class MasterContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IAbstractArtifact) {
				IAbstractArtifact artifact = (IAbstractArtifact) inputElement;
				return artifact.getFields().toArray();
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
			IField field = (IField) obj;
			return field.getName();
		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}
	}

	// ====================================================================
	private TableViewer viewer;

	TableColumn nameColumn;

	private ViewerSorter nameSorter;

	private Button addAttributeButton;

	private Button removeAttributeButton;

	private Button upAttributeButton;

	private Button downAttributeButton;

	@Override
	public TableViewer getViewer() {
		return this.viewer;
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
		GridData gridData = new GridData(GridData.FILL_BOTH | GridData.GRAB_VERTICAL);
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
				IField[] newFields = new IField[allItems.length];
				for (int i = 0; i < newFields.length; i++) {
					newFields[i] = (IField) allItems[i].getData();
				}
				getIArtifact().setFields(Arrays.asList(newFields));
				refresh();
				updateMaster();
				markPageModified();
			}
		});
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
			public void widgetSelected(SelectionEvent event) {
				addButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		upAttributeButton = toolkit.createButton(sectionClient, "Up", SWT.PUSH);
		upAttributeButton.setEnabled(!getIArtifact().isReadonly());
		upAttributeButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_BEGINNING));
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
		downAttributeButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_BEGINNING));
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
		// Support for testing
		removeAttributeButton.setData("name", "Remove_Attribute");

		removeAttributeButton.setEnabled(!getIArtifact().isReadonly());
		removeAttributeButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_BEGINNING));

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
		IField newField = artifact.makeField();

		ComponentNameProvider nameFactory = ComponentNameProvider.getInstance();
		String newFieldName = nameFactory.getNewFieldName(artifact);
		newField.setName(newFieldName);
		IType defaultType = newField.makeType();
		try {
			defaultType.setFullyQualifiedName(getDefaultTypeName());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
			MessageDialog.openWarning(getPage().getPartControl().getShell(),
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
		if (viewer.getTable().getSelectionCount() == 0
				|| viewer.getTable().getSelectionIndex() == viewer.getTable()
						.getItemCount()) {
			viewer.add(newField);
			TableItem[] allItems = this.viewer.getTable().getItems();
			IField[] newFields = new IField[allItems.length];
			for (int i = 0; i < newFields.length; i++) {
				newFields[i] = (IField) allItems[i].getData();
			}
			getIArtifact().setFields(Arrays.asList(newFields));

		} else {
			int position = viewer.getTable().getSelectionIndex();
			TableItem[] allItems = this.viewer.getTable().getItems();

			IField[] allFields = new IField[allItems.length];
			IField[] newFields = new IField[allItems.length + 1];
			for (int i = 0; i <= position; i++) {
				newFields[i] = (IField) allItems[i].getData();
			}
			newFields[position + 1] = newField;

			for (int i = position + 2; i < newFields.length; i++) {
				newFields[i] = (IField) allItems[i - 1].getData();
			}
			getIArtifact().setFields(Arrays.asList(newFields));
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

	protected void markPageModified() {
		ArtifactEditorBase editor = (ArtifactEditorBase) getPage().getEditor();
		editor.pageModified();
	}

	/**
	 * Gets the default attribute type from the active profile.
	 */
	private String getDefaultTypeName() throws TigerstripeException {
		IWorkbenchProfile profile = TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile();
		return profile.getDefaultPrimitiveTypeString();
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
	 * Triggered when the up button is pushed
	 * 
	 */
	protected void upButtonSelected(SelectionEvent event) {

		// If you go up/down then the sort order ion the viewer has to be
		// removed!
		viewer.setSorter(null);

		TableItem[] selectedItems = viewer.getTable().getSelection();
		IField[] selectedFields = new IField[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (IField) selectedItems[i].getData();
		}
		TableItem[] allItems = this.viewer.getTable().getItems();

		IField[] allFields = new IField[allItems.length];
		IField[] newFields = new IField[allItems.length];

		for (int i = 0; i < allFields.length; i++) {
			newFields[i] = (IField) allItems[i].getData();
			if (allItems[i].getData().equals(selectedFields[0]) && i != 0) {
				newFields[i] = newFields[i - 1];
				newFields[i - 1] = (IField) allItems[i].getData();
			}
		}

		// TODO - This should be wrapped in case of error?
		selIndex = selIndex - 1;
		getIArtifact().setFields(Arrays.asList(newFields));
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
		IField[] selectedFields = new IField[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (IField) selectedItems[i].getData();
		}
		TableItem[] allItems = this.viewer.getTable().getItems();

		IField[] allFields = new IField[allItems.length];
		IField[] newFields = new IField[allItems.length];

		for (int i = allFields.length - 1; i > -1; i--) {
			newFields[i] = (IField) allItems[i].getData();
			if (allItems[i].getData().equals(selectedFields[0])
					&& i != allFields.length - 1) {
				newFields[i] = newFields[i + 1];
				newFields[i + 1] = (IField) allItems[i].getData();
			}
		}

		// TODO - This should be wrapped in case of error?
		selIndex = selIndex + 1;
		getIArtifact().setFields(Arrays.asList(newFields));
		markPageModified();
		refresh();
		updateMaster();
	}

	/**
	 * Updates the current state of the master
	 * 
	 */
	@Override
	public void updateMaster() {

		// Updates the state of the Up/Down/Remove Buttons
		if (viewer.getSelection() != null && !viewer.getSelection().isEmpty()
				&& !getIArtifact().isReadonly()) {
			removeAttributeButton.setEnabled(true);
		} else {
			removeAttributeButton.setEnabled(false);

		}

		upAttributeButton.setEnabled(viewer.getTable().getSelectionIndex() > 0
				&& viewer.getTable().getSelectionCount() == 1
				&& !getIArtifact().isReadonly());

		downAttributeButton
				.setEnabled((viewer.getTable().getSelectionIndex() < viewer
						.getTable().getItems().length - 1)
						&& viewer.getTable().getSelectionCount() == 1
						&& !getIArtifact().isReadonly());

	}

	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(Field.class, // TODO remove the dependency on
				// Core and use API instead
				new ArtifactAttributeDetailsPage(this, getIArtifact()
						.isReadonly()));
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

	private Table table;
	private Composite tableComposite;

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

	public DetailsPart getDetailsPart() {
		return detailsPart;
	}

}
