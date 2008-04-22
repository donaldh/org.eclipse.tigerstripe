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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.generator;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.pluggable.GeneratorProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.impl.pluggable.TigerstripePluginProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.GeneratorProjectDescriptor;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.LicensedAccess;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.TSWorkbenchPluggablePluginRole;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.header.OverviewPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.properties.PluginDescriptorPropertiesPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.rules.PluginDescriptorRulesPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.runtime.RuntimePage;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TSExplorerUtils;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;

/**
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public abstract class GeneratorDescriptorEditor extends TigerstripeFormEditor {

	private int sourcePageIndex;

	private boolean previousPageWasModel = true;

	private Collection<TigerstripeFormPage> modelPages = new ArrayList<TigerstripeFormPage>();

	private GeneratorDescriptorSourcePage sourcePage;

	private ITigerstripeGeneratorProject workingHandle;

	protected void updateTitle() {
		IEditorInput input = getEditorInput();
		setPartName(((IResource) input.getAdapter(IResource.class))
				.getProject().getName()
				+ "/" + input.getName());
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

	public CTabFolder getPageFolder() {
		return (CTabFolder) this.getContainer();
	}

	public static boolean isEditable() {
		return LicensedAccess.getWorkbenchPluggablePluginRole() == TSWorkbenchPluggablePluginRole.CREATE_EDIT;
	}

	@Override
	public void addPages() {
		if (LicensedAccess.getWorkbenchPluggablePluginRole() != TSWorkbenchPluggablePluginRole.CREATE_EDIT
				&& LicensedAccess.getWorkbenchPluggablePluginRole() != TSWorkbenchPluggablePluginRole.DEPLOY_UNDEPLOY) {
			GeneratorDescriptorErrorPage errorPage = new GeneratorDescriptorErrorPage(
					this, "errorPage1", "Edit Plugin Error");
			try {
				addPage(errorPage);
			} catch (Exception e) {
				EclipsePlugin.log(e);
			}
		} else {
			int headerIndex = -1;
			try {
				PluginDescriptorPropertiesPage propertiesPage = new PluginDescriptorPropertiesPage(
						this);

				OverviewPage overPage = new OverviewPage(this);
				headerIndex = addPage(overPage);
				addModelPage(overPage);

				addPage(propertiesPage);
				addModelPage(propertiesPage);

				PluginDescriptorRulesPage rulesPage = new PluginDescriptorRulesPage(
						this);
				addPage(rulesPage);
				addModelPage(rulesPage);

				RuntimePage runtimePage = new RuntimePage(this);
				addPage(runtimePage);
				addModelPage(runtimePage);

				addSourcePage();
			} catch (PartInitException e) {
				EclipsePlugin.log(e);
			}
			setActivePage(headerIndex);
			updateTitle();
		}
	}

	protected void addSourcePage() throws PartInitException {
		sourcePage = new GeneratorDescriptorSourcePage(this, "id", "Source");
		sourcePageIndex = addPage(sourcePage, getEditorInput());
		setPageText(sourcePageIndex, "Source");
		sourcePage.setEditable(isEditable());
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

		if (getActivePage() == sourcePageIndex) {
			try {
				updateModelFromTextEditor();
			} catch (TigerstripeException ee) {
				Status status = new Status(IStatus.WARNING,
						TigerstripePluginConstants.PLUGIN_ID, 111,
						"Unexpected Exception", ee);
				EclipsePlugin.log(status);
			}
		}

		try {
			getProjectHandle().commit(monitor);
		} catch (TigerstripeException ee) {
			EclipsePlugin.log(ee);
		}

		if (getActivePage() != sourcePageIndex) {
			((GeneratorDescriptorSourcePage) getEditor(sourcePageIndex))
					.firePropertyChange(IEditorPart.PROP_DIRTY);
			firePropertyChange(IEditorPart.PROP_DIRTY);
		} else {
			getEditor(sourcePageIndex).doSave(monitor);
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
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
		for (TigerstripeFormPage page : modelPages) {
			page.refresh();
		}
	}

	private void updateTextEditorFromModel() {
		try {
			if (getEditorInput() instanceof FileEditorInput) {
				ITigerstripeGeneratorProject projectHandle = getProjectHandle();
				GeneratorProjectDescriptor descriptor = ((GeneratorProjectHandle) projectHandle)
						.getDescriptor();

				sourcePage.getDocumentProvider().getDocument(getEditorInput())
						.set(descriptor.asText());
			}
		} catch (TigerstripeException e) {
			Status status = new Status(IStatus.ERROR,
					TigerstripePluginConstants.PLUGIN_ID, 222,
					"Error refreshing source page for Tigerstripe descriptor",
					e);
			EclipsePlugin.log(status);
		}
	}

	// Bug #894: need to make a copy of the handle to work on it, rather than
	// working directly on the primary object, similarly to the DescriptorEditor
	// behavior
	public ITigerstripeGeneratorProject getProjectHandle() {
		if (workingHandle == null) {
			IEditorInput input = getEditorInput();
			ITigerstripeGeneratorProject handle = null;
			if (input instanceof IFileEditorInput) {
				IFileEditorInput fileInput = (IFileEditorInput) input;
				handle = (ITigerstripeGeneratorProject) TSExplorerUtils
						.getProjectHandleFor(fileInput.getFile());
				if (handle != null) {
					// Create a working Copy where we substitute a new object
					// for the underlying plugin project
					// workingHandle = new TigerstripePluginProjectHandle(handle
					// .getLocation().toFile().toURI());
					try {
						workingHandle = (ITigerstripeGeneratorProject) handle
								.makeWorkingCopy(null);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			}
		}
		return workingHandle;
	}

	private void updateModelFromTextEditor() throws TigerstripeException {
		if (getEditorInput() instanceof FileEditorInput) {
			ITigerstripeGeneratorProject projectHandle = getProjectHandle();
			GeneratorProjectDescriptor originalDescriptor = ((TigerstripePluginProjectHandle) projectHandle)
					.getDescriptor();

			String text = sourcePage.getDocumentProvider().getDocument(
					getEditorInput()).get();
			StringReader reader = new StringReader(text);
			try {
				originalDescriptor.parse(reader);
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

}