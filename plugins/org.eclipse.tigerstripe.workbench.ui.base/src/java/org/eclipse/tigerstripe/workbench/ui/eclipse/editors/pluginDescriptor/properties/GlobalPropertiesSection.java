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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.properties;

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
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.properties.BooleanPPluginProperty;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.properties.StringPPluginProperty;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.properties.TablePPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.project.ITigerstripePluginProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.NewPPluginPropertySelectionDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.PluginDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.properties.details.BooleanPropertyDetailsPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.properties.details.StringPropertyDetailsPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.properties.details.TablePropertyDetailsPage;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

/**
 * 
 * @author Eric Dillon
 * 
 */
public class GlobalPropertiesSection extends PropertiesSectionPart implements
		IFormPart {

	protected DetailsPart detailsPart;

	private Button upButton;

	private Button downButton;

	public GlobalPropertiesSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TWISTIE);
		setTitle("&Global Properties");
		setDescription("Define global properties within the context of this plugin.");
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
		sashForm
				.setToolTipText("Define/Edit global properties for this plugin.");
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
			if (inputElement instanceof ITigerstripePluginProject) {
				ITigerstripePluginProject pPlugin = (ITigerstripePluginProject) inputElement;
				try {
					return pPlugin.getGlobalProperties();
				} catch (TigerstripeException e) {
					return new IPluginProperty[0];
				}
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
			IPluginProperty field = (IPluginProperty) obj;
			return field.getName();
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

		Composite sectionClient = toolkit.createComposite(section);
		TableWrapLayout twlayout = new TableWrapLayout();
		twlayout.numColumns = 2;
		sectionClient.setLayout(twlayout);

		Table t = toolkit.createTable(sectionClient, SWT.NULL);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.rowspan = 4;
		td.heightHint = 150;
		t.setLayoutData(td);

		addAttributeButton = toolkit.createButton(sectionClient, "Add",
				SWT.PUSH);
		addAttributeButton.setEnabled(PluginDescriptorEditor.isEditable());
		addAttributeButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		if (PluginDescriptorEditor.isEditable()) {
			addAttributeButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					addButtonSelected(event);
				}

				public void widgetDefaultSelected(SelectionEvent event) {
					// empty
				}
			});
		}

		upButton = toolkit.createButton(sectionClient, "Up", SWT.PUSH);
		upButton.setEnabled(PluginDescriptorEditor.isEditable());
		upButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		if (PluginDescriptorEditor.isEditable()) {
			upButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					upArgButtonPressed();
				}

				public void widgetDefaultSelected(SelectionEvent event) {
					// empty
				}
			});
		}

		downButton = toolkit.createButton(sectionClient, "Down", SWT.PUSH);
		downButton.setEnabled(PluginDescriptorEditor.isEditable());
		downButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		if (PluginDescriptorEditor.isEditable()) {
			downButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					downArgButtonPressed();
				}

				public void widgetDefaultSelected(SelectionEvent event) {
					// empty
				}
			});
		}

		removeAttributeButton = toolkit.createButton(sectionClient, "Remove",
				SWT.PUSH);
		removeAttributeButton.setLayoutData(new TableWrapData());
		if (PluginDescriptorEditor.isEditable()) {
			removeAttributeButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					removeButtonSelected(event);
				}

				public void widgetDefaultSelected(SelectionEvent event) {
					// empty
				}
			});
		}

		Label l = toolkit.createLabel(sectionClient, "", SWT.NULL);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = 100;
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
		updateMaster();
	}

	/**
	 * Triggered when the add button is pushed
	 * 
	 */
	protected void addButtonSelected(SelectionEvent event) {

		ITigerstripePluginProject pProject = getIPluggablePluginProject();
		NewPPluginPropertySelectionDialog dialog = new NewPPluginPropertySelectionDialog(
				getBody().getShell(), findNewPropertyName(), pProject);

		if (dialog.open() == Window.OK) {
			IPluginProperty newProp = dialog.getNewPPluginProperty();
			if (newProp != null) {
				try {
					pProject.addGlobalProperty(newProp);
					viewer.add(newProp);
					viewer.setSelection(new StructuredSelection(newProp), true);
					markPageModified();
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
	}

	public void markPageModified() {
		PluginDescriptorEditor editor = (PluginDescriptorEditor) getPage()
				.getEditor();
		editor.pageModified();
	}

	private int newFieldCount;

	/**
	 * Finds a new field name
	 */
	private String findNewPropertyName() {
		String result = "aProperty" + newFieldCount++;

		// make sure we're not creating a duplicate
		TableItem[] items = viewer.getTable().getItems();
		for (int i = 0; i < items.length; i++) {
			String name = ((IPluginProperty) items[i].getData())
					.getName();
			if (result.equals(name))
				return findNewPropertyName();
		}
		return result;
	}

	/**
	 * Triggered when the remove button is pushed
	 * 
	 */
	protected void removeButtonSelected(SelectionEvent event) {
		TableItem[] selectedItems = viewer.getTable().getSelection();
		IPluginProperty[] selectedFields = new IPluginProperty[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (IPluginProperty) selectedItems[i]
					.getData();
		}

		String message = "Do you really want to remove ";
		if (selectedFields.length > 1) {
			message = message + "these " + selectedFields.length
					+ " properties?";
		} else {
			message = message + "this property?";
		}

		MessageDialog msgDialog = new MessageDialog(getBody().getShell(),
				"Remove property", null, message, MessageDialog.QUESTION,
				new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {
			try {
				getIPluggablePluginProject().removeGlobalProperties(
						selectedFields);
				viewer.remove(selectedFields);
				markPageModified();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
		updateMaster();
	}

	/**
	 * Updates the current state of the master
	 * 
	 */
	protected void updateMaster() {

		// Updates the state of the Remove Button
		if (PluginDescriptorEditor.isEditable()
				&& viewer.getSelection() != null
				&& !viewer.getSelection().isEmpty()) {
			removeAttributeButton.setEnabled(true);
		} else {
			removeAttributeButton.setEnabled(false);
		}

		upButton.setEnabled(PluginDescriptorEditor.isEditable()
				&& viewer.getTable().getSelectionCount() == 1
				&& viewer.getTable().getSelectionIndex() > 0);
		downButton.setEnabled(PluginDescriptorEditor.isEditable()
				&& viewer.getTable().getSelectionCount() == 1
				&& viewer.getTable().getSelectionIndex() < viewer.getTable()
						.getItems().length - 1);

	}

	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(StringPPluginProperty.class,
				new StringPropertyDetailsPage());
		detailsPart.registerPage(BooleanPPluginProperty.class,
				new BooleanPropertyDetailsPage());
		detailsPart.registerPage(TablePPluginProperty.class,
				new TablePropertyDetailsPage());
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
		viewer.setInput(getIPluggablePluginProject());
		viewer.refresh();
		updateMaster();
	}

	private void upArgButtonPressed() {
		ITigerstripePluginProject pProject = getIPluggablePluginProject();
		TableItem[] selectedItems = this.viewer.getTable().getSelection();
		IPluginProperty[] selectedArgs = new IPluginProperty[selectedItems.length];
		for (int i = 0; i < selectedItems.length; i++) {
			selectedArgs[i] = (IPluginProperty) selectedItems[i]
					.getData();
		}
		TableItem[] allItems = this.viewer.getTable().getItems();
		IPluginProperty[] allArgs = new IPluginProperty[allItems.length];
		IPluginProperty[] newArgs = new IPluginProperty[allItems.length];
		for (int i = 0; i < allArgs.length; i++) {
			newArgs[i] = (IPluginProperty) allItems[i].getData();
			if (allItems[i].getData().equals(selectedArgs[0]) && i != 0) {
				newArgs[i] = newArgs[i - 1];
				newArgs[i - 1] = (IPluginProperty) allItems[i]
						.getData();
			}
		}
		try {
			pProject.setGlobalProperties(newArgs);
			markPageModified();
			refresh();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private void downArgButtonPressed() {
		ITigerstripePluginProject pProject = getIPluggablePluginProject();
		TableItem[] selectedItems = this.viewer.getTable().getSelection();
		IPluginProperty[] selectedArgs = new IPluginProperty[selectedItems.length];
		for (int i = 0; i < selectedItems.length; i++) {
			selectedArgs[i] = (IPluginProperty) selectedItems[i]
					.getData();
		}
		TableItem[] allItems = this.viewer.getTable().getItems();
		IPluginProperty[] allArgs = new IPluginProperty[allItems.length];
		IPluginProperty[] newArgs = new IPluginProperty[allItems.length];
		for (int i = allArgs.length - 1; i > -1; i--) {
			newArgs[i] = (IPluginProperty) allItems[i].getData();
			if (allItems[i].getData().equals(selectedArgs[0])
					&& i != allArgs.length - 1) {
				newArgs[i] = newArgs[i + 1];
				newArgs[i + 1] = (IPluginProperty) allItems[i]
						.getData();
			}
		}
		try {
			pProject.setGlobalProperties(newArgs);
			markPageModified();
			refresh();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

}
