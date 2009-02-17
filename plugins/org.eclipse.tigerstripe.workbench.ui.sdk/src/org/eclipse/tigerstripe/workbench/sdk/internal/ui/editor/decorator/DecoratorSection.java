/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.decorator;

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
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;
import org.eclipse.tigerstripe.workbench.sdk.internal.LocalContributions;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.DecoratorContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.ConfigEditor;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.ExtensionSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class DecoratorSection extends ExtensionSectionPart implements
		IFormPart {

	protected DetailsPart detailsPart;

	public DecoratorSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			int style) {
		super(page, parent, toolkit,  null,
				ExpandableComposite.TWISTIE | style);
		setTitle("Contributions to '"+ LocalContributions.DECORATOR_PART+"'");
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
		// createToolBarActions(managedForm);
		sashForm.setWeights(new int[] { 35, 65 });
		form.updateToolBar();

		getSection().setClient(body);
		getToolkit().paintBordersFor(body);
	}

	class MasterContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ISDKProvider) {
				ISDKProvider provider = (ISDKProvider) inputElement;
				return provider.getDecoratorContributions().toArray();
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
			DecoratorContribution field = (DecoratorContribution) obj;
		
			if (index == 1){
				return field.getDecoratorClass();
			} else {
				return field.getContributor();
			}
		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}
	}

	
	Listener listener = new Listener() {
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
			viewer.setSorter(new DecoratorSorter(dir, sortColumn.getText()));
			refresh();
			updateMaster();

		}
	};
	
	// ====================================================================
	private TableViewer viewer;

	private String[] decoratorColumnNames = new String[]{ "Decorator Class","Contributor","Readonly"};
	
	TableColumn decoratorClassColumn;
	TableColumn decoratorContributorColumn;

	private Button addContributionButton;

	private Button removeContributionButton;

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

		Table decoratorRuleTable = toolkit.createTable(sectionClient, SWT.FULL_SELECTION);

		decoratorRuleTable.setHeaderVisible(true);
		decoratorRuleTable.setLinesVisible(true);


		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.bottom = new FormAttachment(100, -150);
		fd.left = new FormAttachment(0, 10);
		fd.right = new FormAttachment(80);
		fd.width = 400;
		decoratorRuleTable.setLayoutData(fd);

		decoratorClassColumn = new TableColumn(decoratorRuleTable, SWT.NULL);
		decoratorClassColumn.setWidth(250);
		decoratorClassColumn.setText(decoratorColumnNames[0]);
		
		
		decoratorContributorColumn = new TableColumn(decoratorRuleTable, SWT.NULL);
		decoratorContributorColumn.setWidth(250);
		decoratorContributorColumn.setText(decoratorColumnNames[1]);
		
		decoratorClassColumn.addListener(SWT.Selection, listener);
		decoratorContributorColumn.addListener(SWT.Selection, listener);

		addContributionButton = toolkit.createButton(sectionClient, "Add",
				SWT.PUSH);
		// Support for testing
		addContributionButton.setData("name", "Add_Attribute");
		addContributionButton.setEnabled(true);
		fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(decoratorRuleTable, 5);
		fd.right = new FormAttachment(100, -5);
		addContributionButton.setLayoutData(fd);
		addContributionButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				addButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		

		
		removeContributionButton = toolkit.createButton(sectionClient, "Remove",
				SWT.PUSH);
		// Support for testing
		removeContributionButton.setData("name", "Remove_Attribute");
		removeContributionButton.setData("name","removeAttributeButton");
		removeContributionButton.setEnabled(true);
		fd = new FormData();
		fd.top = new FormAttachment(addContributionButton, 5);
		fd.left = new FormAttachment(decoratorRuleTable, 5);
		fd.right = new FormAttachment(100, -5);
		removeContributionButton.setLayoutData(fd);

		removeContributionButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				removeButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		// This label keeps the "List" part shorter than the "Details" part to
		// the RHS

		Label l = toolkit.createLabel(sectionClient, "", SWT.NULL);
		fd = new FormData();
		fd.top = new FormAttachment(removeContributionButton, 5);
		fd.left = new FormAttachment(decoratorRuleTable, 5);
		fd.right = new FormAttachment(100, -5);
		fd.height = 250;
		l.setLayoutData(fd);

		final IFormPart part = this;
		viewer = new TableViewer(decoratorRuleTable);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				selIndex = viewer.getTable().getSelectionIndex();
				managedForm.fireSelectionChanged(part, event.getSelection());
				viewerSelectionChanged(event);
			}
		});
		viewer.setContentProvider(new MasterContentProvider());
		viewer.setLabelProvider(new MasterLabelProvider());
		viewer.setInput(((ConfigEditor) getPage().getEditor())
				.getIProvider());
		viewer.getTable().setSortColumn(decoratorClassColumn);
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
//		IAbstractArtifact artifact = getIArtifact();
//		IField newField = artifact.makeField();
//
//		ComponentNameProvider nameFactory = ComponentNameProvider.getInstance();
//		String newFieldName = nameFactory.getNewFieldName(artifact);
//		newField.setName(newFieldName);
//		IType defaultType = newField.makeType();
//		try {
//			defaultType.setFullyQualifiedName(getDefaultTypeName());
//		} catch (TigerstripeException e) {
//			EclipsePlugin.log(e);
//			MessageDialog.openWarning(getPage().getPartControl().getShell(),
//					"Default "
//							+ ArtifactMetadataFactory.INSTANCE.getMetadata(
//									IPrimitiveTypeImpl.class.getName())
//									.getLabel(null) + " For Parameter", e
//							.getMessage());
//			return;
//		}
//
//		defaultType.setTypeMultiplicity(IModelComponent.EMultiplicity.ONE);
//		newField.setType(defaultType);
//		newField.setRefBy(IField.REFBY_VALUE);
//		newField.setVisibility(EVisibility.PUBLIC);
//
//		// getIArtifact().addField(newField);
//
//		// Add the item after the current selection (if there is one, and its
//		// not the last thing in the table.)
//		if (viewer.getTable().getSelectionCount() == 0
//				|| viewer.getTable().getSelectionIndex() == viewer.getTable()
//						.getItemCount()) {
//			viewer.add(newField);
//			TableItem[] allItems = this.viewer.getTable().getItems();
//			IField[] newFields = new IField[allItems.length];
//			for (int i = 0; i < newFields.length; i++) {
//				newFields[i] = (IField) allItems[i].getData();
//			}
//			getIArtifact().setFields(Arrays.asList(newFields));
//
//		} else {
//			int position = viewer.getTable().getSelectionIndex();
//			TableItem[] allItems = this.viewer.getTable().getItems();
//
//			IField[] allFields = new IField[allItems.length];
//			IField[] newFields = new IField[allItems.length + 1];
//			for (int i = 0; i <= position; i++) {
//				newFields[i] = (IField) allItems[i].getData();
//			}
//			newFields[position + 1] = newField;
//
//			for (int i = position + 2; i < newFields.length; i++) {
//				newFields[i] = (IField) allItems[i - 1].getData();
//			}
//			getIArtifact().setFields(Arrays.asList(newFields));
//		}
//
//		refresh();
//
//		viewer.setSelection(new StructuredSelection(newField), true);
//		markPageModified();
//		updateMaster();
//
//		// Record Add Edit
//		try {
//			URI artURI = (URI) getIArtifact().getAdapter(URI.class);
//			URI attrURI = artURI.appendFragment(newFieldName);
////			ModelUndoableEdit edit = new ModelUndoableEdit(artURI,
////					IModelChangeDelta.ADD, newField.getClass().getSimpleName(),
////					null, attrURI, getIArtifact().getProject());
////			((TigerstripeFormEditor) getPage().getEditor()).getUndoManager()
////					.addEdit(edit);
//		} catch (Exception e) {
//			EclipsePlugin.log(e);
//		}
	}

	protected void markPageModified() {
		ConfigEditor editor = (ConfigEditor) getPage().getEditor();
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
//		TableItem[] selectedItems = viewer.getTable().getSelection();
//		IField[] selectedFields = new IField[selectedItems.length];
//
//		for (int i = 0; i < selectedItems.length; i++) {
//			selectedFields[i] = (IField) selectedItems[i].getData();
//		}
//
//		String message = "Do you really want to remove ";
//		if (selectedFields.length > 1) {
//			message = message + "these " + selectedFields.length
//					+ " attributes?";
//		} else {
//			message = message + "this attribute?";
//		}
//
//		MessageDialog msgDialog = new MessageDialog(getBody().getShell(),
//				"Remove attribute", null, message, MessageDialog.QUESTION,
//				new String[] { "Yes", "No" }, 1);
//
//		if (msgDialog.open() == 0) {
//
//			URI[] fieldURIs = new URI[selectedFields.length];
//			String[] fieldTypes = new String[selectedFields.length];
//			int index = 0;
//			for (IField field : selectedFields) {
//				fieldURIs[index] = (URI) field.getAdapter(URI.class);
//				fieldTypes[index] = field.getClass().getSimpleName();
//				index++;
//			}
//
//			viewer.remove(selectedFields);
//			getIArtifact().removeFields(Arrays.asList(selectedFields));
//			markPageModified();
//
//			URI artURI = (URI) getIArtifact().getAdapter(URI.class);
//			for (int i = 0; i < selectedFields.length; i++) {
//				try {
////					ModelUndoableEdit edit = new ModelUndoableEdit(artURI,
////							IModelChangeDelta.REMOVE, fieldTypes[i],
////							fieldURIs[i], null, getIArtifact().getProject());
////					((TigerstripeFormEditor) getPage().getEditor())
////							.getUndoManager().addEdit(edit);
//				} catch (Exception e) {
//					EclipsePlugin.log(e);
//				}
//			}
//		}
//		updateMaster();
	}





	/**
	 * Updates the current state of the master
	 * 
	 * 
	 */
	public void updateMaster() {
		// The remove button should be inactive for "readOnly" items
		if (viewer.getSelection() != null && !viewer.getSelection().isEmpty()
				&& !((DecoratorContribution) viewer.getTable().getSelection()[0].getData()).isReadOnly()) {
			removeContributionButton.setEnabled(true);
		} else {
			removeContributionButton.setEnabled(false);
		}
	}

	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(DecoratorContribution.class, 
				new DecoratorDetailsPage());
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
		viewer.setInput(((ConfigEditor) getPage().getEditor())
				.getIProvider());
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
