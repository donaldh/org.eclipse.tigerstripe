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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.AbstractTigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.module.InstalledModule;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.TigerstripeProjectSelectionDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutUtil;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.TigerstripeDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
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
					return project.getModelReferences();
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
			ModelReference ref = (ModelReference) obj;

			String modelId = ref.getToModelId();
			String projectName = null, version = null;
			if (ref.isResolved()) {
				ITigerstripeModelProject project = ref.getResolvedModel();
				projectName = project.getName();
				try {
					version = project.getProjectDetails().getVersion();
				} catch (Exception e) {
				}
			}

			StringBuffer buffer = new StringBuffer();
			if (modelId != null && modelId.length() > 0) {
				buffer.append(modelId);
			} else {
				buffer.append(projectName);
			}

			if (version != null && version.length() > 0) {
				buffer.append(" (");
				buffer.append(version);
				buffer.append(")");
			}

			return buffer.toString();
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public String getText(Object obj) {
			return getColumnText(obj, 0);
		}

		@Override
		public Image getImage(Object obj) {
			ModelReference ref = (ModelReference) obj;
			if (ref.isResolved()) {
				if (ref.isWorkspaceReference())
					return Images.get(Images.TSPROJECT_FOLDER);
				else if (ref.isInstalledModuleReference())
					return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_JAR);
			}
			return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_UNKNOWN);
		}
	}

	public ReferencedProjectsSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR);
		setTitle("Referenced Tigerstripe Projects");
		createContent();
	}

	@Override
	protected void createContent() {
		GridData td = new GridData(GridData.FILL_BOTH);
		getSection().setLayoutData(td);

		Composite client = TigerstripeLayoutUtil.createSectionBodyGridLayout(
				getSection(), getToolkit(), 2);

		createTable(client, getToolkit());

		getSection().setClient(client);
		getToolkit().paintBordersFor(client);
	}

	private void createTable(Composite parent, FormToolkit toolkit) {
		Table t = toolkit.createTable(parent, SWT.NULL);
		GridData twd = new GridData(GridData.FILL_BOTH);
		twd.verticalSpan = 2;
		// twd.heightHint = 150;
		t.setLayoutData(twd);

		Composite buttonsClient = TigerstripeLayoutUtil.createButtonsClient(
				parent, toolkit);

		Button addButton = toolkit.createButton(buttonsClient, "Add", SWT.PUSH);
		addButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				addButtonSelected();
			}
		});
		addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		removeButton = toolkit.createButton(buttonsClient, "Remove", SWT.PUSH);
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

		List<ModelReference> filteredOutProjects = new ArrayList<ModelReference>();

		try {
			filteredOutProjects
					.add(ModelReference.referenceFromProject(handle)); // the
			// current
			// project
			for (ModelReference prjRefs : handle.getModelReferences()) {
				filteredOutProjects.add(prjRefs);
			}
		} catch (TigerstripeException e) {
			// ignore here
		}

		TigerstripeProjectSelectionDialog dialog = new TigerstripeProjectSelectionDialog(
				getSection().getShell(), filteredOutProjects);
		if (dialog.open() == Window.OK) {
			Object[] results = dialog.getResult();
			for (Object res : results) {
				ModelReference ref = null;
				if (res instanceof IJavaProject) {
					IJavaProject prj = (IJavaProject) res;

					ITigerstripeModelProject tsPrj = (ITigerstripeModelProject) prj
							.getProject().getAdapter(
									ITigerstripeModelProject.class);
					if (tsPrj != null) {
						try {
							ref = ModelReference.referenceFromProject(tsPrj);
						} catch (TigerstripeException e) {
							EclipsePlugin.log(e);
						}
					}
				} else if (res instanceof InstalledModule) {
					InstalledModule module = (InstalledModule) res;
					ref = new ModelReference(getTSProject(), module
							.getModuleID());
				}
				if (ref != null) {
					try {
						handle.addModelReference(ref);
						viewer.refresh(true);
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
		ModelReference[] selectedFields = new ModelReference[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (ModelReference) selectedItems[i].getData();
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
			ITigerstripeModelProject handle = getTSProject();
			try {
				handle.removeModelReferences(selectedFields);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			markPageModified();
			viewer.refresh(true);
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
