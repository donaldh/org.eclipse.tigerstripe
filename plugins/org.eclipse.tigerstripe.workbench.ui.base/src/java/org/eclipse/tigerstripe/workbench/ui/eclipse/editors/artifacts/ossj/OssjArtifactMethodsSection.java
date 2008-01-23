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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.api.model.IMethod;
import org.eclipse.tigerstripe.api.model.IType;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.core.model.Method;
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

public class OssjArtifactMethodsSection extends ArtifactSectionPart implements
		IFormPart {

	protected DetailsPart detailsPart;

	public OssjArtifactMethodsSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			IOssjArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider) {
		super(page, parent, toolkit, labelProvider, contentProvider,
				ExpandableComposite.TWISTIE | ExpandableComposite.COMPACT);
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
		sashForm.setWeights(new int[] { 30, 70 });
		form.updateToolBar();

		getSection().setClient(body);
		getToolkit().paintBordersFor(body);
	}

	class MasterContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IAbstractArtifact) {
				IAbstractArtifact artifact = (IAbstractArtifact) inputElement;
				return artifact.getIMethods();
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

		section.setDescription("Defined Methods:");

		Composite sectionClient = toolkit.createComposite(section);
		TableWrapLayout twlayout = new TableWrapLayout();
		twlayout.numColumns = 2;
		sectionClient.setLayout(twlayout);

		Table t = toolkit.createTable(sectionClient, SWT.NULL);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.rowspan = 2;
		td.heightHint = 140;
		t.setLayoutData(td);

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
		td.heightHint = 150;
		l.setLayoutData(td);

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
		IMethod newMethod = artifact.makeIMethod();

		String newMethodName = findNewMethodName();
		newMethod.setName(newMethodName);
		newMethod.setVoid(true);
		IType type = newMethod.makeIType();
		type.setFullyQualifiedName("void");
		type.setMultiplicity(IType.MULTIPLICITY_SINGLE);
		newMethod.setReturnIType(type);
		newMethod.setVoid(true);

		getIArtifact().addIMethod(newMethod);
		viewer.add(newMethod);
		viewer.setSelection(new StructuredSelection(newMethod), true);
		markPageModified();
	}

	private int newMethodCount;

	/**
	 * Finds a new field name
	 */
	private String findNewMethodName() {
		String result = "method" + newMethodCount++;

		// make sure we're not creating a duplicate
		TableItem[] items = viewer.getTable().getItems();
		for (int i = 0; i < items.length; i++) {
			String name = ((IMethod) items[i].getData()).getName();
			if (result.equals(name))
				return findNewMethodName();
		}

		return result;
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
			// remove now
			viewer.remove(selectedMethods);
			getIArtifact().removeIMethods(selectedMethods);
			markPageModified();
		}
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
