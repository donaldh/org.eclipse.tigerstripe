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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.TigerstripeLicenseException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeOssjProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.project.IProjectSession;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.advanced.AdvancedConfigurationPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.dependencies.DescriptorDependenciesPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.facetRefs.FacetReferencesPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.header.OverviewPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.plugins.PluginConfigurationPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.utils.TigerstripeProgressMonitor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.TSExplorerUtils;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class DescriptorEditor extends TigerstripeFormEditor {

	private int sourcePageIndex;

	private boolean previousPageWasModel = true;

	private Collection modelPages = new ArrayList();

	private DescriptorSourcePage sourcePage;

	private ITigerstripeProject workingHandle;

	private void updateTitle() {
		IEditorInput input = getEditorInput();
		if (input instanceof IFileEditorInput) {
			setPartName(((IResource) input.getAdapter(IResource.class))
					.getProject().getName()
					+ "/" + input.getName());
		} else {
			setPartName(input.getName());
		}
	}

	@Override
	public Object getViewPartInput() {
		IJavaProject jProject = null;

		IResource res = (IResource) getEditorInput()
				.getAdapter(IResource.class);
		if (res != null) {
			IProject project = res.getProject();
			jProject = JavaCore.create(project);
		}
		return jProject;
	}

	@Override
	public void addPages() {
		int headerIndex = -1;
		try {
			PluginConfigurationPage pluginPage = new PluginConfigurationPage(
					this);

			OverviewPage overPage = new OverviewPage(this);
			headerIndex = addPage(overPage);
			addModelPage(overPage);

			addPage(pluginPage);
			addModelPage(pluginPage);

			// DescriptorRepositoriesPage repoPage = new
			// DescriptorRepositoriesPage(
			// this);
			// addPage(repoPage);
			// addModelPage(repoPage);

			if (getEditorInput() instanceof IFileEditorInput) {
				DescriptorDependenciesPage depPage = new DescriptorDependenciesPage(
						this);
				addPage(depPage);
				addModelPage(depPage);
			}

			if (getEditorInput() instanceof IFileEditorInput) {
				AdvancedConfigurationPage advPage = new AdvancedConfigurationPage(
						this);
				addPage(advPage);
				addModelPage(advPage);
			}

			if (getEditorInput() instanceof IFileEditorInput) {
				FacetReferencesPage facetPage = new FacetReferencesPage(this);
				addPage(facetPage);
				addModelPage(facetPage);
			}

			if (getEditorInput() instanceof IFileEditorInput) {
				addSourcePage();
			}
		} catch (PartInitException e) {
			EclipsePlugin.log(e);
		}
		setActivePage(headerIndex);
		updateTitle();
	}

	protected void addSourcePage() throws PartInitException {
		sourcePage = new DescriptorSourcePage(this, "id", "Source");
		sourcePageIndex = addPage(sourcePage, getEditorInput());
		setPageText(sourcePageIndex, "Source");
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		if (getActivePage() != sourcePageIndex) {
			updateTextEditorFromModel();
		} else {
			try {
				updateModelFromTextEditor();
			} catch (TigerstripeException ee) {
				Status status = new Status(IStatus.WARNING,
						TigerstripePluginConstants.PLUGIN_ID, 111,
						"Unexpected Exception", ee);
				EclipsePlugin.log(status);
			}
		}

		// Force a refresh on the project session cache
		// Bug 584: the cached handle needs to see his underlying
		// TigerstripeProject
		// updated
		try {
			final IProjectSession session = TigerstripeCore.getDefaultProjectSession();

			IRunnableWithProgress op = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					try {
						monitor.beginTask("Refreshing Project Cache...", 10);
						session.refreshCacheFor(getTSProject().getURI(),
								getTSProject(), new TigerstripeProgressMonitor(
										monitor));
						monitor.done();
					} catch (TigerstripeException ee) {
						EclipsePlugin.log(ee);
					}
				}
			};

			IWorkbench wb = PlatformUI.getWorkbench();
			IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
			Shell shell = win != null ? win.getShell() : null;

			try {
				ProgressMonitorDialog pDialog = new ProgressMonitorDialog(shell);
				pDialog.run(true, false, op);
			} catch (InterruptedException ee) {
				EclipsePlugin.log(ee);
			} catch (InvocationTargetException ee) {
				EclipsePlugin.log(ee);
			}

		} catch (TigerstripeLicenseException e) {
			EclipsePlugin.log(e);
		}

		getEditor(sourcePageIndex).doSave(monitor);
	}

	@Override
	public void pageChange(int newPageIndex) {
		if (newPageIndex == sourcePageIndex) {
			if (isPageModified)
				updateTextEditorFromModel();
			previousPageWasModel = false;
		} else {
			if (!previousPageWasModel && isDirty()) {
				try {
					updateModelFromTextEditor();
				} catch (TigerstripeException ee) {
					Status status = new Status(IStatus.WARNING,
							TigerstripePluginConstants.PLUGIN_ID, 111,
							"Unexpected Exception", ee);
					EclipsePlugin.log(status);
				}
			}
			previousPageWasModel = true;
		}
		super.pageChange(newPageIndex);
	}

	private boolean isPageModified;

	public void pageModified() {
		isPageModified = true;
		if (!super.isDirty())
			firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	protected void handlePropertyChange(int propertyId) {
		if (propertyId == IEditorPart.PROP_DIRTY)
			isPageModified = super.isDirty();
		super.handlePropertyChange(propertyId);
	}

	@Override
	public boolean isDirty() {
		return isPageModified || super.isDirty();
	}

	protected void addModelPage(TigerstripeFormPage page) {
		modelPages.add(page);
	}

	private void refreshModelPages() {
		for (Iterator iter = modelPages.iterator(); iter.hasNext();) {
			TigerstripeFormPage page = (TigerstripeFormPage) iter.next();
			page.refresh();
		}
	}

	private void updateTextEditorFromModel() {
		try {
			if (getEditorInput() instanceof FileEditorInput) {
				TigerstripeProjectHandle projectHandle = (TigerstripeProjectHandle) getTSProject();
				TigerstripeProject project = projectHandle.getTSProject();

				sourcePage.getDocumentProvider().getDocument(getEditorInput())
						.set(project.asText());
			}
		} catch (TigerstripeException e) {
			Status status = new Status(IStatus.ERROR,
					TigerstripePluginConstants.PLUGIN_ID, 222,
					"Error refreshing source page for Tigerstripe descriptor",
					e);
			EclipsePlugin.log(status);
		}
	}

	private void updateModelFromTextEditor() throws TigerstripeException {
		if (getEditorInput() instanceof FileEditorInput) {
			TigerstripeProjectHandle projectHandle = (TigerstripeProjectHandle) getTSProject();
			TigerstripeProject originalProject = projectHandle.getTSProject();

			String text = sourcePage.getDocumentProvider().getDocument(
					getEditorInput()).get();
			StringReader reader = new StringReader(text);
			try {
				originalProject.parse(reader);
				refreshModelPages();
			} catch (TigerstripeException e) {
				Status status = new Status(
						IStatus.ERROR,
						TigerstripePluginConstants.PLUGIN_ID,
						222,
						"Error refreshing model pages for Tigerstripe descriptor",
						e);
				EclipsePlugin.log(status);
			}
		}
	}

	protected ITigerstripeProject getTSProject() {
		if (workingHandle == null) {
			IEditorInput input = getEditorInput();
			ITigerstripeProject handle = null;
			if (input instanceof IFileEditorInput) {
				IFileEditorInput fileInput = (IFileEditorInput) input;
				handle = (ITigerstripeProject) TSExplorerUtils
						.getProjectHandleFor(fileInput.getFile());
				if (handle != null) {
					// Create a working Copy where we substitute a new object
					// for
					// the underlying TigerstripeProject
					try {
						workingHandle = new TigerstripeOssjProjectHandle(handle
								.getURI());
					} catch (TigerstripeException e) {
						return null;
					}
				}
			} else if (input instanceof ReadOnlyDescriptorEditorInput) {
				ReadOnlyDescriptorEditorInput descInput = (ReadOnlyDescriptorEditorInput) input;
				workingHandle = descInput.getTSProject();
			}
		}
		return workingHandle;
	}
}