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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.useCase;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.contract.useCase.IUseCase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.useCase.body.UseCaseBodyDocumentProvider;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.useCase.body.UseCaseBodyPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.useCase.header.OverviewPage;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;

/**
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class UseCaseEditor extends TigerstripeFormEditor {

	private int sourcePageIndex;

	private boolean previousPageWasModel = true;

	private Collection modelPages = new ArrayList();

	private UseCaseSourcePage sourcePage;

	private IUseCase useCase;

	private UseCaseBodyPage bodyPage;

	private int bodyPageIndex;

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

	public IUseCase getUseCase() throws TigerstripeException {
		if (useCase == null) {
			if (getEditorInput() instanceof FileEditorInput) {
				IFile file = ((FileEditorInput) getEditorInput()).getFile();
				try {
					useCase = InternalTigerstripeCore.getIContractSession()
							.makeIUseCase(file.getLocationURI());
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
		return useCase;
	}

	protected CTabFolder getPageFolder() {
		return (CTabFolder) this.getContainer();
	}

	public static boolean isEditable() {
		return true;
	}

	@Override
	public void addPages() {
		// if (LicensedAccess.getLicenseProfile().getWorkbenchProfileRole() !=
		// TSWorkbenchProfileRole.CREATE_EDIT
		// && LicensedAccess.getLicenseProfile().getWorkbenchProfileRole() !=
		// TSWorkbenchProfileRole.DEPLOY_UNDEPLOY) {
		// SegmentEditorErrorPage errorPage = new SegmentEditorErrorPage(this,
		// "errorPage1", "Edit Profile Error");
		// try {
		// addPage(errorPage);
		// } catch (Exception e) {
		// EclipsePlugin.log(e);
		// }
		// } else {
		int headerIndex = -1;
		try {

			OverviewPage overPage = new OverviewPage(this);
			headerIndex = addPage(overPage);
			addModelPage(overPage);

			addBodyPage();

			// PluginDescriptorRulesPage rulesPage = new
			// PluginDescriptorRulesPage(
			// this);
			// addPage(rulesPage);
			// addModelPage(rulesPage);

			// PublishConfigurationPage pubPage = new
			// PublishConfigurationPage(
			// this );
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
		// }
	}

	protected void addBodyPage() throws PartInitException {
		bodyPage = new UseCaseBodyPage(this, UseCaseBodyPage.PAGE_ID, "body");
		bodyPageIndex = addPage(bodyPage, getEditorInput());
		setPageText(bodyPageIndex, "Body");
		bodyPage.setEditable(isEditable());
	}

	protected void addSourcePage() throws PartInitException {
		sourcePage = new UseCaseSourcePage(this, "id", "Source");
		sourcePageIndex = addPage(sourcePage, getEditorInput());
		setPageText(sourcePageIndex, "Source");
		sourcePage.setEditable(isEditable());
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

		// first pull the content of the body page into the model
		try {
			String text = bodyPage.getDocumentProvider().getDocument(
					getEditorInput()).get();
			getUseCase().setBody(text);
		} catch (TigerstripeException e) {
			Status status = new Status(IStatus.WARNING,
					TigerstripePluginConstants.PLUGIN_ID, 111,
					"Unexpected Exception", e);
			EclipsePlugin.log(status);
		}

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
		getEditor(sourcePageIndex).doSave(monitor);
		try {
			((UseCaseBodyDocumentProvider) bodyPage.getDocumentProvider())
					.resetDocument(getEditorInput());
		} catch (CoreException e) {
			EclipsePlugin.log(e);
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
					if (newPageIndex == bodyPageIndex) {
						// new need to refresh the body content based on the
						// text
						IUseCase handle = getUseCase();
						sourcePage.getDocumentProvider().getDocument(
								getEditorInput()).set(handle.getBody());
					}
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
	public boolean isDirty() {
		return isPageModified || super.isDirty();
	}

	@Override
	protected void handlePropertyChange(int propertyId) {
		if (propertyId == IEditorPart.PROP_DIRTY)
			isPageModified = super.isDirty();
		super.handlePropertyChange(propertyId);
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
			IUseCase handle = getUseCase();
			sourcePage.getDocumentProvider().getDocument(getEditorInput()).set(
					handle.asText());
		} catch (TigerstripeException e) {
			Status status = new Status(IStatus.ERROR,
					TigerstripePluginConstants.PLUGIN_ID, 222,
					"Error refreshing source page for Tigerstripe use case", e);
			EclipsePlugin.log(status);
		}
	}

	private void updateModelFromTextEditor() throws TigerstripeException {
		IUseCase handle = getUseCase();
		String text = sourcePage.getDocumentProvider().getDocument(
				getEditorInput()).get();
		StringReader reader = new StringReader(text);
		try {
			handle.parse(reader);
			refreshModelPages();
		} catch (TigerstripeException e) {
			Status status = new Status(IStatus.ERROR,
					TigerstripePluginConstants.PLUGIN_ID, 222,
					"Error refreshing model pages for Tigerstripe use case", e);
			EclipsePlugin.log(status);
		}
	}

}