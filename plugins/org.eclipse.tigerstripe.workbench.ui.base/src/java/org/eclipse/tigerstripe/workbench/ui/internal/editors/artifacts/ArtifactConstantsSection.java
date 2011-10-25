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
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjEnumSpecifics;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.undo.ModelUndoableEdit;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class ArtifactConstantsSection extends ModelComponentSectionPart
		implements IFormPart {

	class MasterContentProvider implements IStructuredContentProvider {

		public void dispose() {

		}

		public Object[] getElements(Object inputElement) {
			Collection<IAbstractArtifact> hierarhy = getHierarchy(false);
			List<ILiteral> literals = new ArrayList<ILiteral>();

			for (IAbstractArtifact arti : hierarhy) {
				literals.addAll(arti.getLiterals());
			}
			return literals.toArray();
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (newInput instanceof IAbstractArtifact) {
				literalsInModel = new HashSet<ILiteral>(
						((IAbstractArtifact) newInput).getLiterals());
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
			ILiteral literal = (ILiteral) obj;
			switch (index) {
			case 1:
				return literal.getValue();
			default:
				if (literalsInModel.contains(literal)) {
					return literal.getLabelString();
				} else {
					return literal.getLabelString() + "("
							+ literal.getContainingArtifact().getName() + ")";
				}
			}
		}

		public Color getForeground(Object element, int columnIndex) {
			if (!literalsInModel.contains(element)) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);
			} else {
				return null;
			}
		}
	}

	private static final int INT_TYPE = 0;

	private static final int STRING_TYPE = 1;

	private Button addAttributeButton;

	protected DetailsPart detailsPart;

	private Button downAttributeButton;

	private Set<ILiteral> literalsInModel = Collections.emptySet();

	private int newLiteralValue;

	private Button removeAttributeButton;

	protected SashForm sashForm;

	private int selIndex = -1;
	private Table table;
	private Composite tableComposite;
	private Button upAttributeButton;

	// ====================================================================
	private TableViewer viewer;

	public ArtifactConstantsSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit, IArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider, int style) {
		super(page, parent, toolkit, labelProvider, contentProvider,
				ExpandableComposite.TWISTIE | style);
		setTitle("Constants");
		createContent();
		updateMaster();
	}

	/**
	 * Triggered when the add button is pushed
	 * 
	 */
	protected void addButtonSelected(SelectionEvent event) {
		IAbstractArtifact artifact = getIArtifact();
		ILiteral newLiteral = artifact.makeLiteral();

		ComponentNameProvider nameFactory = ComponentNameProvider.getInstance();

		String newLabelName = nameFactory.getNewLiteralName(artifact);
		newLiteral.setName(newLabelName);
		IType defaultType = newLiteral.makeType();

		// See bug #77, #90
		if (getForcedBaseType() != null) {
			defaultType.setFullyQualifiedName(getForcedBaseType());
		} else {
			defaultType.setFullyQualifiedName("String");
		}
		defaultType.setTypeMultiplicity(EMultiplicity.ZERO_ONE);
		newLiteral.setType(defaultType);
		newLiteral.setVisibility(EVisibility.PUBLIC);
		newLiteral.setValue(getInitialLiteralValue(defaultType));

		// Add the item after the current selection (if there is one, and its
		// not the last thing in the table.)
		TableItem[] selection = viewer.getTable().getSelection();
		if (selection.length == 0) {
			List<ILiteral> newLiterals = new ArrayList<ILiteral>(getIArtifact()
					.getLiterals());
			newLiterals.add(newLiteral);
			getIArtifact().setLiterals(newLiterals);
		} else {

			List<ILiteral> newLiterals = new ArrayList<ILiteral>(getIArtifact()
					.getLiterals());

			int afterPos = newLiterals.indexOf(selection[selection.length - 1]
					.getData());

			if (afterPos >= 0 && afterPos < newLiterals.size()) {
				newLiterals.add(afterPos + 1, newLiteral);
			} else {
				newLiterals.add(newLiteral);
			}
			getIArtifact().setLiterals(newLiterals);
		}

		refresh();

		viewer.setSelection(new StructuredSelection(newLiteral), true);
		markPageModified();
		updateMaster();

		// Record Add Edit
		try {
			URI artURI = (URI) getIArtifact().getAdapter(URI.class);
			URI attrURI = artURI.appendFragment(newLabelName);
			ModelUndoableEdit edit = new ModelUndoableEdit(artURI,
					IModelChangeDelta.ADD, newLiteral.getClass()
							.getSimpleName(), null, attrURI, getIArtifact()
							.getProject());
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
		final IFormPart part = this;

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

		TableColumn nameColumn = new TableColumn(table, SWT.NULL);
		nameColumn.setText("Name");
		tcLayout.setColumnData(nameColumn, new ColumnWeightData(50, false));

		TableColumn valueColumn = new TableColumn(table, SWT.NULL);
		valueColumn.setText("Value");
		tcLayout.setColumnData(valueColumn, new ColumnWeightData(50, false));

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

					List<ILiteral> newLiterals = new ArrayList<ILiteral>(
							allItems.length);
					for (int i = 0; i < allItems.length; i++) {
						ILiteral literal = (ILiteral) allItems[i].getData();
						if (literalsInModel.contains(literal)) {
							newLiterals.add(literal);
						}
					}
					getIArtifact().setLiterals(newLiterals);

					refresh();
					updateMaster();
					markPageModified();
				}
			});
		}

		valueColumn.addListener(SWT.Selection, new Listener() {
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

				List<ILiteral> newLiterals = new ArrayList<ILiteral>(
						allItems.length);
				for (int i = 0; i < allItems.length; i++) {
					ILiteral literal = (ILiteral) allItems[i].getData();
					if (literalsInModel.contains(literal)) {
						newLiterals.add(literal);
					}
				}
				getIArtifact().setLiterals(newLiterals);

				refresh();
				updateMaster();
				markPageModified();
			}
		});

		addAttributeButton = toolkit.createButton(sectionClient, "Add",
				SWT.PUSH);
		// Support for testing
		addAttributeButton.setData("name", "Add_Literal");
		addAttributeButton.setEnabled(!isReadonly());
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
		upAttributeButton.setEnabled(!isReadonly());
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
		removeAttributeButton.setData("name", "Remove_Literal");
		removeAttributeButton.setEnabled(!isReadonly());
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

		ILiteral[] selectedLiterals = new ILiteral[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedLiterals[i] = (ILiteral) selectedItems[i].getData();
		}

		List<ILiteral> newLiterals = new ArrayList<ILiteral>(getIArtifact()
				.getLiterals());

		boolean wasSwap = false;
		for (ILiteral selectedLiteral : selectedLiterals) {

			int toDownIndex = newLiterals.indexOf(selectedLiteral);

			if (toDownIndex >= 0 && toDownIndex < newLiterals.size() - 1) {
				Collections.swap(newLiterals, toDownIndex, toDownIndex + 1);
				wasSwap = true;
			}
		}

		// TODO - This should be wrapped in case of error?
		if (wasSwap) {
			++selIndex;
		}
		getIArtifact().setLiterals(newLiterals);

		markPageModified();
		refresh();
		updateMaster();
	}

	private String findNewLiteralValue(int type) {
		String result = String.valueOf(newLiteralValue);

		if (type == STRING_TYPE)
			result = "\"" + result + "\"";

		// make sure we're not creating a duplicate
		TableItem[] items = viewer.getTable().getItems();
		for (int i = 0; i < items.length; i++) {
			String value = ((ILiteral) items[i].getData()).getValue();
			if (result.equals(value)) {
				newLiteralValue++;
				return findNewLiteralValue(type);
			}
		}
		return result;
	}

	public DetailsPart getDetailsPart() {
		return detailsPart;
	}

	// ======
	// See bug #90, handle Enumerations slightly differently
	public String getForcedBaseType() {
		IAbstractArtifact artifact = ((ArtifactEditorBase) getPage()
				.getEditor()).getIArtifact();
		if (artifact instanceof IEnumArtifact) {
			IEnumArtifact enumArtifact = (IEnumArtifact) artifact;
			IOssjEnumSpecifics specs = (IOssjEnumSpecifics) enumArtifact
					.getIStandardSpecifics();
			if (specs.getBaseIType() != null)
				return specs.getBaseIType().getFullyQualifiedName();
		}
		return null;
	}

	private String getInitialLiteralValue(IType type) {
		if ("int".equals(type.getFullyQualifiedName()))
			return findNewLiteralValue(INT_TYPE);
		else if ("String".equals(Misc.removeJavaLangString(type
				.getFullyQualifiedName())))
			return findNewLiteralValue(STRING_TYPE);
		return "0";
	}

	@Override
	public TableViewer getViewer() {
		return this.viewer;
	}

	protected void markPageModified() {
		ArtifactEditorBase editor = (ArtifactEditorBase) getPage().getEditor();
		editor.pageModified();
	}

	private boolean onlyNative(TableItem[] selectedItems) {
		for (int i = 0; i < selectedItems.length; i++) {
			if (!literalsInModel.contains((selectedItems[i].getData()))) {
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
		viewer.refresh(true);

		if (selIndex != -1 && selIndex < table.getItemCount()) {
			Object refreshedMethod = table.getItem(selIndex)
					.getData();
			viewer.setSelection(new StructuredSelection(refreshedMethod), true);
		}
		updateMaster();
	}

	protected void registerPages(DetailsPart detailsPart) {
		IDetailsPage detailsPage = new ArtifactConstantDetailsPage(this,
				isReadonly());
		registerDetailsPage(detailsPart, detailsPage,
				ILiteral.class);
	}

	/**
	 * Triggered when the remove button is pushed
	 * 
	 */
	protected void removeButtonSelected(SelectionEvent event) {
		TableItem[] selectedItems = viewer.getTable().getSelection();
		ILiteral[] selectedLabels = new ILiteral[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedLabels[i] = (ILiteral) selectedItems[i].getData();
		}

		String message = "Do you really want to remove ";
		if (selectedLabels.length > 1) {
			message = message + "these " + selectedLabels.length
					+ " constants?";
		} else {
			message = message + "this constant?";
		}

		MessageDialog msgDialog = new MessageDialog(getBody().getShell(),
				"Remove Constant", null, message, MessageDialog.QUESTION,
				new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {

			URI[] literalURIs = new URI[selectedLabels.length];
			String[] literalTypes = new String[selectedLabels.length];
			int index = 0;
			for (ILiteral literal : selectedLabels) {
				literalURIs[index] = (URI) literal.getAdapter(URI.class);
				literalTypes[index] = literal.getClass().getSimpleName();
				index++;
			}

			viewer.remove(selectedLabels);
			getIArtifact().removeLiterals(Arrays.asList(selectedLabels));
			markPageModified();

			URI artURI = (URI) getIArtifact().getAdapter(URI.class);
			for (int i = 0; i < selectedLabels.length; i++) {
				try {
					ModelUndoableEdit edit = new ModelUndoableEdit(artURI,
							IModelChangeDelta.REMOVE, literalTypes[i],
							literalURIs[i], null, getIArtifact().getProject());
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
	 * FIXME Used only by ArtifactConstantDetailsPage. Just workaround to avoid
	 * appearing scrolls on details part.
	 */
	void setMinimumHeight(int value) {
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 4;
		gd.widthHint = 250;
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

		ILiteral[] selectedLiterals = new ILiteral[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedLiterals[i] = (ILiteral) selectedItems[i].getData();
		}

		List<ILiteral> newLiterals = new ArrayList<ILiteral>(getIArtifact()
				.getLiterals());

		boolean wasSwap = false;
		for (ILiteral selectedLiteral : selectedLiterals) {

			int toUpIndex = newLiterals.indexOf(selectedLiteral);

			if (toUpIndex > 0) {
				Collections.swap(newLiterals, toUpIndex, toUpIndex - 1);
				wasSwap = true;
			}
		}

		// TODO - This should be wrapped in case of error?
		if (wasSwap) {
			--selIndex;
		}
		getIArtifact().setLiterals(newLiterals);

		markPageModified();
		refresh();
		updateMaster();
	}

	private void updateButtons() {

		List<ILiteral> fields = new ArrayList<ILiteral>(getIArtifact()
				.getLiterals());

		TableItem[] selection = viewer.getTable().getSelection();

		boolean readonly = isReadonly();
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
