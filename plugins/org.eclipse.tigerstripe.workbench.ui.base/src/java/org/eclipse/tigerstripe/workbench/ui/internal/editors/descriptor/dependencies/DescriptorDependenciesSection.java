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

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
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
import org.eclipse.tigerstripe.workbench.internal.api.impl.AbstractTigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ExternalModules;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModuleHeader;
import org.eclipse.tigerstripe.workbench.internal.core.module.InvalidModuleException;
import org.eclipse.tigerstripe.workbench.internal.core.project.Dependency;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProjectFactory;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.FileExtensionBasedSelectionDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.TigerstripeDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class DescriptorDependenciesSection extends
		TigerstripeDescriptorSectionPart implements IFormPart {

	protected DetailsPart detailsPart;

	public DescriptorDependenciesSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.NO_TITLE);
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

		GridData td = new GridData(GridData.FILL_BOTH);
		td.horizontalSpan = 2;
		getSection().setLayoutData(td);

		Composite body = getToolkit().createComposite(getSection());
		body
				.setLayout(TigerstripeLayoutFactory.createClearGridLayout(1,
						false));
		sashForm = new SashForm(body, SWT.HORIZONTAL);
		toolkit.adapt(sashForm, false, false);
		sashForm.setMenu(body.getMenu());
		sashForm.setToolTipText("Define/Edit attributes for this Artifact.");
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		createMasterPart(managedForm, sashForm);
		createDetailsPart(managedForm, sashForm);

		getSection().setClient(body);
		getToolkit().paintBordersFor(body);
	}

	class MasterContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ITigerstripeModelProject) {
				ITigerstripeModelProject project = (ITigerstripeModelProject) inputElement;
				try {
					return project.getDependencies();
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
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
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public String getText(Object obj) {
			IDependency dep = (IDependency) obj;
			return dep.getPath();
		}

		@Override
		public Image getImage(Object obj) {
			IDependency dep = (IDependency) obj;
			try {
				IModuleHeader header = dep.parseIModuleHeader();
				return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_JAR);
			} catch (InvalidModuleException e) {
				return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_UNKNOWN);
			}
		}
	}

	// ====================================================================
	private TableViewer viewer;

	private Button addAttributeButton;

	private Button removeAttributeButton;

	private Table table;

	public TableViewer getViewer() {
		return this.viewer;
	}

	protected void createMasterPart(final IManagedForm managedForm,
			Composite parent) {

		FormToolkit toolkit = getToolkit();

		Section section = TigerstripeLayoutFactory.createSection(parent,
				toolkit, ExpandableComposite.TITLE_BAR, "&Dependencies", null);

		Composite sectionClient = toolkit.createComposite(section);
		GridLayout twlayout = new GridLayout();
		twlayout.numColumns = 2;
		sectionClient.setLayout(twlayout);

		table = toolkit.createTable(sectionClient, SWT.NULL);
		GridData td = new GridData(GridData.FILL_BOTH);
		table.setLayoutData(td);

		Composite buttonsClient = toolkit.createComposite(sectionClient);
		buttonsClient.setLayout(TigerstripeLayoutFactory
				.createButtonsGridLayout());
		buttonsClient.setLayoutData(new GridData(
				GridData.VERTICAL_ALIGN_BEGINNING));

		addAttributeButton = toolkit.createButton(buttonsClient, "Add",
				SWT.PUSH);
		addAttributeButton
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addAttributeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				addButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		removeAttributeButton = toolkit.createButton(buttonsClient, "Remove",
				SWT.PUSH);
		removeAttributeButton.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		removeAttributeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				removeButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		// Create the sentence and hyperlink for additional standard modules
		FormText rtext = toolkit.createFormText(sectionClient, true);
		td = new GridData(GridData.FILL_HORIZONTAL);
		td.horizontalSpan = 2;
		rtext.setLayoutData(td);
		StringBuffer buf = new StringBuffer();

		buf.append("<form>");
		buf
				.append("<li><a href=\"standardModuleAdd\">Add</a> standard built-in modules.</li>");
		// Core dependencies don't exist anymore @see #299
		// buf
		// .append("<li><a href=\"coreModuleAdd\">Update</a> the Tigerstripe
		// Core model.</li>");
		buf.append("</form>");
		rtext.setText(buf.toString(), true, false);
		rtext.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				if ("standardModuleAdd".equals(e.getHref())) {
					addStandardModuleSelected();
					// Core dependencies don't exist anymore @see #299
					// } else if ("coreModuleAdd".equals((String) e.getHref()))
					// {
					// addDefaultModulesButtonPressed();
				}
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

		AbstractTigerstripeProjectHandle handle = (AbstractTigerstripeProjectHandle) getTSProject();
		viewer.setInput(handle);

		toolkit.paintBordersFor(sectionClient);
		section.setClient(sectionClient);
	}

	/**
	 * FIXME Used only by ArtifactAttributeDetailsPage. Just workaround to avoid
	 * appearing scrolls on details part.
	 */
	void setMinimumHeight(int value) {
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 4;
		gd.minimumHeight = value;
		table.setLayoutData(gd);
		getManagedForm().reflow(true);
	}

	// Core dependencies don't exist anymore @see #299
	// protected void addDefaultModulesButtonPressed() {
	// FileEditorInput input = (FileEditorInput) getPage().getEditorInput();
	// ITigerstripeProject handle = (ITigerstripeProject)
	// TSExplorerUtils.getProjectHandleFor(input
	// .getFile());
	// try {
	// handle.attachDefaultCoreModelDependency(true);
	// markPageModified();
	//
	// ErrorDialog dialog = new ErrorDialog(
	// getSection().getShell(),
	// "Core Model Update",
	// "The Core Model has been added to your list of Dependencies.\n Please
	// save the descriptor for this change to be effective",
	// new Status(IStatus.WARNING, EclipsePlugin.PLUGIN_ID, 222,
	// "Core Model Added", null), 0);
	// input.getFile().getProject().refreshLocal(1,
	// new NullProgressMonitor());
	// refresh();
	// dialog.open();
	// } catch (TigerstripeException e) {
	// EclipsePlugin.log(e);
	// } catch (CoreException e) {
	// EclipsePlugin.log(e);
	// }
	// }
	//
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
		if (getPage().getEditorInput() instanceof IFileEditorInput) {
			IFileEditorInput input = (IFileEditorInput) getPage()
					.getEditorInput();
			FileExtensionBasedSelectionDialog dialog = new FileExtensionBasedSelectionDialog(
					getSection().getShell(), true, false);
			dialog.setFileExtensions(new String[] { ".zip", ".jar" });
			dialog.setInput(input.getFile().getProject());
			dialog.setDoubleClickSelects(true);
			dialog.setTitle("Select Tigerstripe Dependencies");

			if (dialog.open() == Window.OK) {
				Object[] toAdd = dialog.getResult();
				for (int i = 0; i < toAdd.length; i++) {
					File file = ((IFile) toAdd[i]).getLocation().toFile();

					try {
						String relative = Util.getRelativePath(file, input
								.getFile().getProject().getLocation().toFile());

						IPath path = new Path(relative);
						IResource res = input.getFile().getParent().findMember(
								path);

						ITigerstripeModelProject handle = getTSProject();

						IDependency dep = handle.makeDependency(res
								.getProjectRelativePath().toOSString());
						handle.addDependency(dep, new NullProgressMonitor());
						viewer.add(dep);
						markPageModified();
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					} catch (IOException e) {
						EclipsePlugin.log(e);
					}
				}
				updateMaster();
			}
		}
	}

	protected void addStandardModuleSelected() {
		if (getPage().getEditorInput() instanceof IFileEditorInput) {

			IFileEditorInput input = (IFileEditorInput) getPage()
					.getEditorInput();
			FileExtensionBasedSelectionDialog dialog = new FileExtensionBasedSelectionDialog(
					getSection().getShell(), true, false);
			dialog.setFileExtensions(new String[] { "zip", "jar" });
			dialog.setInput(ExternalModules.getModuleFolder());
			dialog.setDoubleClickSelects(true);
			dialog.setTitle("Select Standard Module");

			if (dialog.open() == Window.OK) {
				Object[] toAdd = dialog.getResult();
				for (int i = 0; i < toAdd.length; i++) {
					File file = ((IFile) toAdd[i]).getLocation().toFile();

					try {
						IDependency dep = TigerstripeProjectFactory.INSTANCE
								.getProjectSession().makeIDependency(
										file.getAbsolutePath());

						TigerstripeProjectHandle handle = (TigerstripeProjectHandle) getTSProject();

						IDependency localDependency = Dependency
								.copyToAsLocalDependency(handle.getTSProject(),
										dep);
						try {
							input.getFile().getParent().refreshLocal(1,
									new NullProgressMonitor());
						} catch (CoreException e) {
							EclipsePlugin.log(e);
						}
						handle.addDependency(localDependency,
								new NullProgressMonitor());
						viewer.add(localDependency);
						markPageModified();
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
				updateMaster();
			}
		}
	}

	protected void markPageModified() {
		DescriptorEditor editor = (DescriptorEditor) getPage().getEditor();
		editor.pageModified();
	}

	/**
	 * Triggered when the remove button is pushed
	 * 
	 */
	protected void removeButtonSelected(SelectionEvent event) {
		TableItem[] selectedItems = viewer.getTable().getSelection();
		IDependency[] selectedFields = new IDependency[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (IDependency) selectedItems[i].getData();
		}

		String message = "Do you really want to remove ";
		if (selectedFields.length > 1) {
			message = message + "these " + selectedFields.length
					+ " dependencies?";
		} else {
			message = message + "this dependency?";
		}

		MessageDialog msgDialog = new MessageDialog(getBody().getShell(),
				"Remove Dependency", null, message, MessageDialog.QUESTION,
				new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {
			viewer.remove(selectedFields);
			ITigerstripeModelProject handle = getTSProject();
			try {
				handle.removeDependencies(selectedFields,
						new NullProgressMonitor());
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
		if (viewer.getSelection() != null && !viewer.getSelection().isEmpty()) {
			removeAttributeButton.setEnabled(true);
		} else {
			removeAttributeButton.setEnabled(false);
		}
	}

	protected void registerPages(DetailsPart detailsPart) {
		DescriptorDependenciesDetailsPage page = new DescriptorDependenciesDetailsPage(
				this);
		detailsPart.registerPage(Dependency.class, // TODO remove the
				// dependency on
				// Core and use API instead
				page);
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
		AbstractTigerstripeProjectHandle handle = (AbstractTigerstripeProjectHandle) getTSProject();
		viewer.setInput(handle);

		viewer.refresh();
		updateMaster();
	}
}
