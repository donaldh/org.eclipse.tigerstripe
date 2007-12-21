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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.dependencies;

import java.util.TreeSet;

import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
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
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.impl.AbstractTigerstripeProjectHandle;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.TigerstripeProjectSelectionDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.TigerstripeDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.TSExplorerUtils;
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
			if (inputElement instanceof ITigerstripeProject) {
				ITigerstripeProject project = (ITigerstripeProject) inputElement;
				try {
					return project.getReferencedProjects();
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
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public String getText(Object obj) {
			ITigerstripeProject ref = (ITigerstripeProject) obj;
			return ref.getProjectLabel();
		}

		@Override
		public Image getImage(Object obj) {
			return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_JAVA_MODEL);
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
		ITigerstripeProject handle = getTSProject();

		TreeSet<String> filteredOutProjects = new TreeSet<String>();
		filteredOutProjects.add(input.getFile().getProject().getName()); // the
		// current
		// project
		try {
			for (ITigerstripeProject prjRefs : handle.getReferencedProjects()) {
				filteredOutProjects.add(prjRefs.getProjectLabel());
			}
		} catch (TigerstripeException e) {
			// ignore here
		}

		TigerstripeProjectSelectionDialog dialog = new TigerstripeProjectSelectionDialog(
				getSection().getShell(), filteredOutProjects);
		if (dialog.open() == Window.OK) {
			Object[] results = dialog.getResult();
			for (Object res : results) {
				JavaProject prj = (JavaProject) res;

				ITigerstripeProject tsPrj = (ITigerstripeProject) TSExplorerUtils
						.getProjectHandleFor(prj.getProject());
				if (tsPrj != null) {
					try {
						handle.addReferencedProject(tsPrj);
						viewer.add(tsPrj);
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
		ITigerstripeProject[] selectedFields = new ITigerstripeProject[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (ITigerstripeProject) selectedItems[i]
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
			FileEditorInput input = (FileEditorInput) getPage()
					.getEditorInput();

			ITigerstripeProject handle = getTSProject();
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
