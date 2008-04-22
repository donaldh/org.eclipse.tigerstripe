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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.profile;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.profile.WorkbenchProfile;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.LicensedAccess;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.TSWorkbenchProfileRole;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.artifacts.ArtifactsPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.general.GeneralPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.header.OverviewPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.primitive.PrimitiveTypesPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.stereotypes.StereotypesPage;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;

/**
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class ProfileEditor extends TigerstripeFormEditor {

	private int sourcePageIndex;

	private boolean previousPageWasModel = true;

	private Collection modelPages = new ArrayList();

	private ProfileSourcePage sourcePage;

	private WorkbenchProfile profile;

	private void updateTitle() {
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

	public WorkbenchProfile getProfile() throws TigerstripeException {
		if (profile == null) {
			if (getEditorInput() instanceof FileEditorInput) {
				IFile file = ((FileEditorInput) getEditorInput()).getFile();
				try {
					profile = (WorkbenchProfile) TigerstripeCore.getWorkbenchProfileSession()
							.getWorkbenchProfileFor(
									file.getLocation().toOSString());
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
		return profile;
	}

	protected CTabFolder getPageFolder() {
		return (CTabFolder) this.getContainer();
	}

	public static boolean isEditable() {
		return LicensedAccess.getWorkbenchProfileRole() == TSWorkbenchProfileRole.CREATE_EDIT;
	}

	@Override
	public void addPages() {
		if (LicensedAccess.getWorkbenchProfileRole() != TSWorkbenchProfileRole.CREATE_EDIT
				&& LicensedAccess.getWorkbenchProfileRole() != TSWorkbenchProfileRole.DEPLOY_UNDEPLOY) {
			ProfileEditorErrorPage errorPage = new ProfileEditorErrorPage(this,
					"errorPage1", "Edit Profile Error");
			try {
				addPage(errorPage);
			} catch (Exception e) {
				EclipsePlugin.log(e);
			}
		} else {
			int headerIndex = -1;
			try {
				// PluginDescriptorPropertiesPage propertiesPage = new
				// PluginDescriptorPropertiesPage(
				// this);

				OverviewPage overPage = new OverviewPage(this);
				headerIndex = addPage(overPage);
				addModelPage(overPage);
				//			
				// addPage(propertiesPage);
				// addModelPage(propertiesPage);

				GeneralPage generalPage = new GeneralPage(this);
				addPage(generalPage);
				addModelPage(generalPage);

				StereotypesPage stereotypesPage = new StereotypesPage(this);
				addPage(stereotypesPage);
				addModelPage(stereotypesPage);

				PrimitiveTypesPage primitiveTypesPage = new PrimitiveTypesPage(
						this);
				addPage(primitiveTypesPage);
				addModelPage(primitiveTypesPage);

				ArtifactsPage artifactsPage = new ArtifactsPage(this);
				addPage(artifactsPage);
				addModelPage(artifactsPage);

				// PluginDescriptorRulesPage rulesPage = new
				// PluginDescriptorRulesPage(
				// this);
				// addPage(rulesPage);
				// addModelPage(rulesPage);

				
				// addPage( pubPage );
				// addModelPage( pubPage );

				// AdvancedConfigurationPage advPage = new
				// AdvancedConfigurationPage( this );
				// addPage( advPage );
				// addModelPage( advPage );

				addSourcePage();
			} catch (PartInitException e) {
				EclipsePlugin.log(e);
			}
			setActivePage(headerIndex);
			updateTitle();
		}
	}

	protected void addSourcePage() throws PartInitException {
		sourcePage = new ProfileSourcePage(this, "id", "Source");
		sourcePageIndex = addPage(sourcePage, getEditorInput());
		setPageText(sourcePageIndex, "Source");
		sourcePage.setEditable(isEditable());
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
		profile = null; // make sure the profile is re-read
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

	@Override
	public void resourceChanged(IResourceChangeEvent event) {

		IResourceDelta selfDelta = lookforSelf(event.getDelta());

		if (selfDelta != null) {

			if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
				try {
					updateModelFromTextEditor();
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
		super.resourceChanged(event);
	}

	private void updateTextEditorFromModel() {
		try {
			IWorkbenchProfile handle = getProfile();
			sourcePage.getDocumentProvider().getDocument(getEditorInput()).set(
					handle.asText());
		} catch (TigerstripeException e) {
			Status status = new Status(IStatus.ERROR,
					TigerstripePluginConstants.PLUGIN_ID, 222,
					"Error refreshing source page for Tigerstripe descriptor",
					e);
			EclipsePlugin.log(status);
		}
	}

	private void updateModelFromTextEditor() throws TigerstripeException {
		IWorkbenchProfile handle = getProfile();
		String text = sourcePage.getDocumentProvider().getDocument(
				getEditorInput()).get();
		StringReader reader = new StringReader(text);
		try {
			handle.parse(reader);
			refreshModelPages();
		} catch (TigerstripeException e) {
			Status status = new Status(IStatus.ERROR,
					TigerstripePluginConstants.PLUGIN_ID, 222,
					"Error refreshing model pages for Tigerstripe descriptor",
					e);
			EclipsePlugin.log(status);
		}
	}

}