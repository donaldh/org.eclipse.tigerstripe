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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.rules;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.ui.components.md.MasterDetails;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.generator.GeneratorDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.generator.GeneratorDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public abstract class RulesSectionPart extends GeneratorDescriptorSectionPart {

	public RulesSectionPart(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit, int style) {
		super(page, parent, toolkit,
				style != ExpandableComposite.NO_TITLE ? style
						| ExpandableComposite.TITLE_BAR
						: ExpandableComposite.NO_TITLE);
	}

	private Table table;
	private MasterDetails masterDeatils;

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
		FormToolkit toolkit = getToolkit();

		GridDataFactory.fillDefaults().grab(true, false).applyTo(getSection());
		Composite body = getToolkit().createComposite(getSection());
		body
				.setLayout(TigerstripeLayoutFactory.createClearGridLayout(1,
						false));

		Composite container = toolkit.createComposite(body);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite masterContainer = toolkit.createComposite(container);
		masterContainer.setLayout(new GridLayout());
		GridDataFactory.fillDefaults().hint(400, SWT.DEFAULT).applyTo(
				masterContainer);

		createMasterPart(managedForm, masterContainer);

		Composite detailsContainer = toolkit.createComposite(container);
		detailsContainer.setLayout(new GridLayout());
		detailsContainer.setLayoutData(new GridData(GridData.FILL_BOTH));

		createDetailsPart(managedForm, detailsContainer);

		getSection().setClient(body);
		getToolkit().paintBordersFor(body);
	}

	protected abstract MasterDetails createMasterDeatils(Composite parent);

	protected abstract String getTooltipText();

	/**
	 * Updates the current state of the master
	 * 
	 */
	protected void updateMaster() {

		// Updates the state of the Remove Button
		if (GeneratorDescriptorEditor.isEditable()
				&& viewer.getSelection() != null
				&& !viewer.getSelection().isEmpty()) {
			removeAttributeButton.setEnabled(true);
		} else {
			removeAttributeButton.setEnabled(false);
		}
	}

	protected void createDetailsPart(final IManagedForm mform, Composite parent) {
		masterDeatils = createMasterDeatils(parent);
	}

	class MasterLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			IRule rule = (IRule) obj;
			return rule.getName();
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

	protected abstract String getDescription();

	protected abstract IStructuredContentProvider getRulesListContentProvider();

	protected void createMasterPart(final IManagedForm managedForm,
			Composite parent) {
		FormToolkit toolkit = getToolkit();
		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new GridData(GridData.FILL_BOTH));
		Composite sectionClient = toolkit.createComposite(section);
		GridLayout layout = new GridLayout(2, false);
		sectionClient.setLayout(layout);
		table = toolkit.createTable(sectionClient, SWT.NULL);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 4;
		table.setLayoutData(gd);

		addAttributeButton = toolkit.createButton(sectionClient, "Add",
				SWT.PUSH);
		addAttributeButton.setEnabled(GeneratorDescriptorEditor.isEditable());
		addAttributeButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_BEGINNING));
		if (GeneratorDescriptorEditor.isEditable()) {
			addAttributeButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					addButtonSelected(event);
				}

				public void widgetDefaultSelected(SelectionEvent event) {
					// empty
				}
			});
		}
		removeAttributeButton = toolkit.createButton(sectionClient, "Remove",
				SWT.PUSH);
		removeAttributeButton
				.setEnabled(GeneratorDescriptorEditor.isEditable());
		removeAttributeButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_BEGINNING));
		if (GeneratorDescriptorEditor.isEditable()) {
			removeAttributeButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					removeButtonSelected(event);
				}

				public void widgetDefaultSelected(SelectionEvent event) {
					// empty
				}
			});
		}

		viewer = new TableViewer(table);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				// managedForm.fireSelectionChanged(RulesSectionPart.this,
				// event.getSelection());
				viewerSelectionChanged(event);
			}
		});
		viewer.setContentProvider(getRulesListContentProvider());
		viewer.setLabelProvider(new MasterLabelProvider());
		viewer.setInput(getIPluggablePluginProject());

		toolkit.paintBordersFor(sectionClient);
		section.setClient(sectionClient);
	}

	/**
	 * Updates the master's side based on the selection on the list of
	 * attributes
	 * 
	 */
	protected void viewerSelectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection)
					.getFirstElement();
			masterDeatils.switchTo(element);
		}
		updateMaster();
	}

	/**
	 * Triggered when the add button is pushed
	 * 
	 */
	protected abstract void addButtonSelected(SelectionEvent event);

	public void markPageModified() {
		GeneratorDescriptorEditor editor = (GeneratorDescriptorEditor) getPage()
				.getEditor();
		editor.pageModified();
	}

	private int newFieldCount;

	/**
	 * Finds a new field name
	 */
	protected String findNewRuleName() {
		String result = "aRule" + newFieldCount++;

		// make sure we're not creating a duplicate
		TableItem[] items = viewer.getTable().getItems();
		for (int i = 0; i < items.length; i++) {
			String name = ((IRule) items[i].getData()).getName();
			if (result.equals(name))
				return findNewRuleName();
		}
		return result;
	}

	/**
	 * Triggered when the remove button is pushed
	 * 
	 */
	protected abstract void removeButtonSelected(SelectionEvent event);

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
	}

	@Override
	public void refresh() {
		viewer.setInput(getIPluggablePluginProject());
		viewer.refresh();
		updateMaster();
	}
}
