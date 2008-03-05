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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj;

import java.util.Arrays;

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
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj.IOssjEnumSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.Literal;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class OssjArtifactConstantsSection extends ArtifactSectionPart implements
		IFormPart {

	protected DetailsPart detailsPart;

	public OssjArtifactConstantsSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			IOssjArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider) {
		super(page, parent, toolkit, labelProvider, contentProvider,
				ExpandableComposite.TWISTIE | ExpandableComposite.COMPACT);
		setTitle("Constants");
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
		sashForm.setToolTipText("Define/Edit constants for this Artifact.");
		sashForm.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createMasterPart(managedForm, sashForm);
		createDetailsPart(managedForm, sashForm);
		// createToolBarActions(managedForm);
		sashForm.setWeights(new int[] { 30, 70 });
		form.updateToolBar();

		getSection().setClient(body);
		getToolkit().paintBordersFor(body);
	}

	class MasterContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IAbstractArtifact) {
				IAbstractArtifact artifact = (IAbstractArtifact) inputElement;
				return artifact.getLiterals().toArray();
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
			ILiteral literal = (ILiteral) obj;
			switch (index) {
			case 1:
				return literal.getValue();
			default:
				return literal.getName();
			}
		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}
	}

	// ====================================================================
	private TableViewer viewer;

	private Button addAttributeButton;

	private Button removeAttributeButton;

	private ViewerSorter nameSorter;

	private ViewerSorter valueSorter;

	public TableViewer getViewer() {
		return this.viewer;
	}

	protected void createMasterPart(final IManagedForm managedForm,
			Composite parent) {

		FormToolkit toolkit = getToolkit();
		final IFormPart part = this;

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);

		Composite sectionClient = toolkit.createComposite(section);
		TableWrapLayout twlayout = new TableWrapLayout();
		twlayout.numColumns = 2;
		sectionClient.setLayout(twlayout);

		Table t = toolkit.createTable(sectionClient, SWT.SINGLE
				| SWT.FULL_SELECTION);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.rowspan = 2;
		td.heightHint = 140;
		t.setLayoutData(td);

		TableColumn nameColumn = new TableColumn(t, SWT.NULL);
		nameColumn.setText("Name");
		nameColumn.pack();
		nameColumn.setWidth(80);

		TableColumn valueColumn = new TableColumn(t, SWT.NULL);
		valueColumn.setText("Value");
		valueColumn.pack();
		valueColumn.setWidth(80);

		t.setHeaderVisible(true);
		t.setLinesVisible(true);

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

		nameSorter = new ViewerSorter() {
			@Override
			public int compare(Viewer viewer, Object label1, Object label2) {
				return ((Literal) label1).getName().compareToIgnoreCase(
						((Literal) label2).getName());
			}
		};

		valueSorter = new ViewerSorter() {
			@Override
			public int compare(Viewer viewer, Object label1, Object label2) {
				return ((Literal) label1).getValue().compareToIgnoreCase(
						((Literal) label2).getValue());
			}
		};

		viewer.setSorter(valueSorter);

		valueColumn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Empty
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				viewer.setSorter(valueSorter);
				viewer.refresh(true);
			}

		});

		nameColumn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Empty
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				viewer.setSorter(nameSorter);
				viewer.refresh(true);
			}

		});

		addAttributeButton = toolkit.createButton(sectionClient, "Add",
				SWT.PUSH);
		addAttributeButton.setEnabled(!isReadonly());
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

		org.eclipse.swt.widgets.Label l = toolkit.createLabel(sectionClient,
				"", SWT.NULL);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = 40;
		l.setLayoutData(td);

		toolkit.paintBordersFor(sectionClient);
		section.setClient(sectionClient);
	}

	/**
	 * Updates the master's side based on the selection on the list of
	 * attributes
	 * 
	 */
	protected void viewerSelectionChanged(SelectionChangedEvent event) {
		selIndex = viewer.getTable().getSelectionIndex();
		updateMaster();
	}

	private String getInitialLiteralValue(IType type) {
		if ("int".equals(type.getFullyQualifiedName()))
			return String.valueOf(findNewLiteralValue());
		else if ("String".equals(Misc.removeJavaLangString(type
				.getFullyQualifiedName())))
			return "\"" + String.valueOf(findNewLiteralValue()) + "\"";
		return "0";
	}

	/**
	 * Triggered when the add button is pushed
	 * 
	 */
	protected void addButtonSelected(SelectionEvent event) {
		IAbstractArtifact artifact = getIArtifact();
		ILiteral newLiteral = artifact.makeLiteral();

		String newLabelName = findNewFieldName();
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

		getIArtifact().addLiteral(newLiteral);
		viewer.add(newLiteral);
		viewer.setSelection(new StructuredSelection(newLiteral), true);
		markPageModified();
	}

	protected void markPageModified() {
		ArtifactEditorBase editor = (ArtifactEditorBase) getPage().getEditor();
		editor.pageModified();
	}

	private int newFieldCount;

	/**
	 * Finds a new field name
	 */
	private String findNewFieldName() {
		String result = "literal" + newFieldCount;

		// make sure we're not creating a duplicate
		TableItem[] items = viewer.getTable().getItems();
		for (int i = 0; i < items.length; i++) {
			String name = ((ILiteral) items[i].getData()).getName();
			if (result.equals(name)) {
				newFieldCount++;
				return findNewFieldName();
			}
		}
		return result;
	}
	
	private int newLiteralValue;
	
	private String findNewLiteralValue() {
		String result = String.valueOf(newLiteralValue);
		
		// make sure we're not creating a duplicate
		TableItem[] items = viewer.getTable().getItems();
		for (int i = 0; i < items.length; i++) {
			String value = ((ILiteral) items[i].getData()).getValue();
			if (result.equals(value)) {
				newLiteralValue++;
				return findNewLiteralValue();
			}
		}
		return result;
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
			viewer.remove(selectedLabels);
			getIArtifact().removeLiterals(Arrays.asList(selectedLabels));
			markPageModified();
		}
		updateMaster();
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
		detailsPart.registerPage(Literal.class, // TODO remove the dependency on
				// Core and use API instead
				new ArtifactConstantDetailsPage(getIArtifact().isReadonly()));
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
		viewer.refresh();
		if (selIndex != -1) {
			Object refreshedMethod = viewer.getTable().getItem(selIndex)
					.getData();
			viewer.setSelection(new StructuredSelection(refreshedMethod), true);
		}
		updateMaster();
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

	public DetailsPart getDetailsPart() {
		return detailsPart;
	}
}
