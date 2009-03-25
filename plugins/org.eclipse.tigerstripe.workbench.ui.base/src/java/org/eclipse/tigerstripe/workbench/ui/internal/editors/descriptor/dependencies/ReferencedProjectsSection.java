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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.dependencies;

import java.util.TreeSet;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.AbstractTigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.project.DescriptorReferencedProject;
import org.eclipse.tigerstripe.workbench.project.IDescriptorReferencedProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.TigerstripeProjectSelectionDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.TigerstripeDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.FileEditorInput;

public class ReferencedProjectsSection extends TigerstripeDescriptorSectionPart {

	private TableViewer viewer;

	private Button removeButton;

	class ReferencedProjectsContentProvider implements
			IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ITigerstripeModelProject) {
				ITigerstripeModelProject project = (ITigerstripeModelProject) inputElement;
				try {
					return project.getDescriptorsReferencedProjects();
				} catch (TigerstripeException e) {
					return new Object[0];
				}
			}
			return new Object[0];
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}

	class ReferencedProjectsLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			if(((IDescriptorReferencedProject) obj).getProject() != null) return getText(((IDescriptorReferencedProject) obj).getProject());
			else return ((IDescriptorReferencedProject) obj).getProjectName();
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public String getText(Object obj) {
			ITigerstripeModelProject ref = (ITigerstripeModelProject) obj;
			return ref.getName();
		}

		@Override
		public Image getImage(Object obj) {
			return Images.get(Images.TSPROJECT_FOLDER);
		}

	}

	public ReferencedProjectsSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR);

		setTitle("Referenced Tigerstripe Projects");
		getSection().marginWidth = 10;
		getSection().marginHeight = 5;
		getSection().clientVerticalSpacing = 4;
		createContent();
	}

	@Override
	protected void createContent() {
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		getSection().setLayout(layout);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		getSection().setLayoutData(td);

		getSection().clientVerticalSpacing = 5;

		Composite body = getBody();
		body.setLayout(layout);
		createTable(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createTable(Composite parent, FormToolkit toolkit) {

		Table t = toolkit.createTable(parent, SWT.NULL);
		TableWrapData twd = new TableWrapData(TableWrapData.FILL_GRAB);
		twd.rowspan = 2;
		twd.heightHint = 150;
		t.setLayoutData(twd);

		Button addButton = toolkit.createButton(parent, "Add", SWT.PUSH);
		addButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				addButtonSelected();
			}
		});
		addButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		removeButton = toolkit.createButton(parent, "Remove", SWT.PUSH);
		removeButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				removeButtonSelected();
			}
		});

		viewer = new TableViewer(t);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				viewerSelectionChanged(event);
			}
		});
		viewer.setContentProvider(new ReferencedProjectsContentProvider());
		viewer.setLabelProvider(new ReferencedProjectsLabelProvider());

		AbstractTigerstripeProjectHandle handle = (AbstractTigerstripeProjectHandle) getTSProject();
		viewer.setInput(handle);
	}

	protected void addButtonSelected() {
		FileEditorInput input = (FileEditorInput) getPage().getEditorInput();
		ITigerstripeModelProject handle = getTSProject();

		TreeSet<String> filteredOutProjects = new TreeSet<String>();
		filteredOutProjects.add(input.getFile().getProject().getName()); // the
		// current
		// project
		try {
			for (IDescriptorReferencedProject prjRefs : handle
					.getDescriptorsReferencedProjects()) {
				filteredOutProjects.add(prjRefs.getProjectName());
			}
		} catch (TigerstripeException e) {
			// ignore here
		}

		TigerstripeProjectSelectionDialog dialog = new TigerstripeProjectSelectionDialog(
				getSection().getShell(), filteredOutProjects);
		if (dialog.open() == Window.OK) {
			Object[] results = dialog.getResult();
			for (Object res : results) {
				IJavaProject prj = (IJavaProject) res;

				ITigerstripeModelProject tsPrj = (ITigerstripeModelProject) prj
						.getProject()
						.getAdapter(ITigerstripeModelProject.class);
				if (tsPrj != null) {
					try {
						handle.addReferencedProject(tsPrj);
						DescriptorReferencedProject ref = new DescriptorReferencedProject();
						ref.setProject(tsPrj);
						ref.setProjectName(tsPrj.getName());
						viewer.add(ref);
						markPageModified();
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			}
		}
		viewer.refresh();
	}

	protected void removeButtonSelected() {
		TableItem[] selectedItems = viewer.getTable().getSelection();
		IDescriptorReferencedProject[] selectedFields = new IDescriptorReferencedProject[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (IDescriptorReferencedProject) selectedItems[i]
					.getData();
		}

		String message = "Do you really want to remove ";
		if (selectedFields.length > 1) {
			message = message + "these " + selectedFields.length
					+ " references?";
		} else {
			message = message + "this reference?";
		}

		MessageDialog msgDialog = new MessageDialog(getBody().getShell(),
				"Remove References", null, message, MessageDialog.QUESTION,
				new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {
			viewer.remove(selectedFields);
			ITigerstripeModelProject handle = getTSProject();
			try {
				handle.removeReferencedProjects(selectedFields);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			markPageModified();
			viewer.refresh();
		}
	}

	protected void viewerSelectionChanged(SelectionChangedEvent event) {
		updateForm();
	}

	protected void markPageModified() {
		DescriptorEditor editor = (DescriptorEditor) getPage().getEditor();
		editor.pageModified();
	}

	@Override
	public void refresh() {
		updateForm();
	}

	protected void updateForm() {
		TableItem[] selectedItems = viewer.getTable().getSelection();
		if (selectedItems != null && selectedItems.length > 0) {
			removeButton.setEnabled(true);
		} else {
			removeButton.setEnabled(false);
		}
		viewer.refresh(true);
	}
}
