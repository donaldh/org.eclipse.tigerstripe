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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.runtime;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.AnnotationPluginSelectionDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.generator.GeneratorDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.generator.GeneratorDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.PluginDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class AnnotationPluginsSection extends GeneratorDescriptorSectionPart
		implements IFormPart {

	public AnnotationPluginsSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR);
		setTitle("&Required Annotation Generators");

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
		FormToolkit toolkit = getToolkit();

		getSection().setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite body = getToolkit().createComposite(getSection());
		body
				.setLayout(TigerstripeLayoutFactory.createClearGridLayout(1,
						false));

		sashForm = new SashForm(body, SWT.HORIZONTAL);
		toolkit.adapt(sashForm, false, false);
		sashForm.setMenu(body.getMenu());
		sashForm
				.setToolTipText("Add/Remove required Annotation Generators.");
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		createMasterPart(managedForm, sashForm);

		getSection().setClient(body);
		getToolkit().paintBordersFor(body);
	}

	class MasterContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			ITigerstripeGeneratorProject project = (ITigerstripeGeneratorProject) inputElement;
			try {
				return project.getRequiredAnnotationPlugins();
			} catch (TigerstripeException e) {
				return new Object[0];
			}
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}

	class MasterLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public String getText(Object obj) {
			return (String) obj;
		}

	}

	// ====================================================================
	private TableViewer viewer;

	private Button addPluginButton;

	private Button removePluginButton;

	public TableViewer getViewer() {
		return this.viewer;
	}

	protected void createMasterPart(final IManagedForm managedForm,
			Composite parent) {

		FormToolkit toolkit = getToolkit();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);

		Composite sectionClient = toolkit.createComposite(section);
		sectionClient.setLayout(new GridLayout(2, false));

		Table table = toolkit.createTable(sectionClient, SWT.NULL);
		table.setEnabled(PluginDescriptorEditor.isEditable());
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 4;
		table.setLayoutData(gd);

		addPluginButton = toolkit.createButton(sectionClient, "Add", SWT.PUSH);
		addPluginButton.setEnabled(PluginDescriptorEditor.isEditable());
		addPluginButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_BEGINNING));
		if (PluginDescriptorEditor.isEditable()) {
			addPluginButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					addButtonSelected(event);
				}

				public void widgetDefaultSelected(SelectionEvent event) {
					// empty
				}
			});
		}

		removePluginButton = toolkit.createButton(sectionClient, "Remove",
				SWT.PUSH);
		removePluginButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_BEGINNING));
		removePluginButton.addSelectionListener(new SelectionListener() {
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

	private static class FileArchiveFileFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parent, Object element) {
			if (element instanceof File) {
				File file = (File) element;
				if (file.isFile())
					return !file.getName().startsWith("ts-external");
			}
			return false;
		}
	}

	/**
	 * Triggered when the add button is pushed
	 * 
	 */
	protected void addButtonSelected(SelectionEvent event) {

		AnnotationPluginSelectionDialog dialog = new AnnotationPluginSelectionDialog(
				getSection().getShell(), new MasterContentProvider(),
				new MasterLabelProvider(), "Select Annotation Generators");

		try {
			dialog.setInitialElementSelections(Arrays
					.asList(getIPluggablePluginProject()
							.getRequiredAnnotationPlugins()));
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		if (dialog.open() == Window.OK) {
			Object[] toSelect = dialog.getResult();

			try {
				Set<String> sel = new HashSet<String>();
				for (Object o : toSelect) {
					sel.add((String) o);
				}
				getIPluggablePluginProject().setRequiredAnnotationPlugins(
						sel.toArray(new String[sel.size()]));
				markPageModified();
				viewer.refresh();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			updateMaster();
		}
	}

	protected void markPageModified() {
		GeneratorDescriptorEditor editor = (GeneratorDescriptorEditor) getPage()
				.getEditor();
		editor.pageModified();
	}

	/**
	 * Triggered when the remove button is pushed
	 * 
	 */
	protected void removeButtonSelected(SelectionEvent event) {
		TableItem[] selectedItems = viewer.getTable().getSelection();
		String[] selectedFields = new String[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (String) selectedItems[i].getData();
		}

		String message = "Do you really want to remove ";
		if (selectedFields.length > 1) {
			message = message + "these " + selectedFields.length
					+ " annotation generators?";
		} else {
			message = message + "this annotation plugin?";
		}

		MessageDialog msgDialog = new MessageDialog(getBody().getShell(),
				"Remove generators", null, message, MessageDialog.QUESTION,
				new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {
			viewer.remove(selectedFields);
			ITigerstripeGeneratorProject handle = getIPluggablePluginProject();
			try {

				handle.removeRequiredAnnotationPlugins(selectedFields);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			markPageModified();
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
			removePluginButton.setEnabled(true);
		} else {
			removePluginButton.setEnabled(false);
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
	}

	@Override
	public void refresh() {
		ITigerstripeGeneratorProject handle = getIPluggablePluginProject();
		viewer.setInput(handle);

		viewer.refresh();
		updateMaster();
	}
}
