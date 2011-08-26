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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.pluggablePlugin;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.LicensedAccess;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.TSWorkbenchPluggablePluginRole;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.perspective.TigerstripePerspectiveFactory;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.TSOpenAction;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.part.FileEditorInput;

public class NewPluginProjectWizard extends Wizard implements INewWizard {

	private NewPluginProjectWizardPage pageOne;

	private IStructuredSelection selection;

	private ImageDescriptor image;

	public IStructuredSelection getSelection() {
		return this.selection;
	}

	public ImageDescriptor getDefaultImageDescriptor() {
		return this.image;
	}

	/**
	 * Constructor for NewProjectWizard.
	 */
	public NewPluginProjectWizard() {
		super();

		setNeedsProgressMonitor(true);
		image = Images.getDescriptor(Images.TS_LOGO);
		setWindowTitle("New Tigerstripe M1-Level Generation Project");
	}

	/**
	 * Adding the page to the wizard.
	 */

	@Override
	public void addPages() {
		if (LicensedAccess.getWorkbenchPluggablePluginRole() != TSWorkbenchPluggablePluginRole.CREATE_EDIT) {
			NewPluggablePluginErrorPage errorPage = new NewPluggablePluginErrorPage(
					"errorPage1");
			errorPage
					.setTitle("New Tigerstripe M1-Level Generation Project");
			errorPage.setTitle("New Generation Project Error");
			addPage(errorPage);
		} else {
			pageOne = new NewPluginProjectWizardPage(selection,
					getDefaultImageDescriptor());
			addPage(pageOne);
		}
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {

		final NewPluginProjectWizardPage.NewProjectDetails details = pageOne
				.getProjectNewProjectDetails();

		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			@Override
			protected void execute(IProgressMonitor monitor)
					throws CoreException {
				IProjectDetails projectDetails = TigerstripeCore
						.makeProjectDetails();

				try {
					ITigerstripeM1GeneratorProject project = (ITigerstripeM1GeneratorProject) TigerstripeCore
							.createProject(details.getProjectName(),
									projectDetails, null,
									ITigerstripeM1GeneratorProject.class, null,
									null);
				} catch (TigerstripeException e) {
					throw new CoreException(new Status(IStatus.ERROR,
							EclipsePlugin.getPluginId(),
							"Couldn't create project: " + e.getMessage(), e));
				}
			}
		};

		// run the new project creation operation
		try {
			getContainer().run(true, true, op);
		} catch (InterruptedException e) {
			EclipsePlugin.log(e);
		} catch (InvocationTargetException e) {
			EclipsePlugin.log(e);
		}

		openPerspective(TigerstripePerspectiveFactory.ID);
		openProject(details.getProjectName());
		return true;
	}

	/**
	 * Implements Open Perspective.
	 */
	private void openPerspective(String perspId) {
		try {
			IWorkbench workbench = PlatformUI.getWorkbench();
			workbench.showPerspective(perspId, workbench
					.getActiveWorkbenchWindow());
		} catch (WorkbenchException e) {
			EclipsePlugin.log(e);
		}
	}

	/**
	 * 
	 */
	private void openProject(final String projectName) {
		final IWorkbenchPage activePage = EclipsePlugin.getActivePage();
		if (activePage != null) {
			final Display display = getShell().getDisplay();
			if (display != null) {
				display.asyncExec(new Runnable() {
					public void run() {
						try {
							IWorkspace workspace = ResourcesPlugin
									.getWorkspace();
							IWorkspaceRoot root = workspace.getRoot();
							IProject iproject = root.getProject(projectName);
							IFile ifile = iproject
									.getFile(ITigerstripeConstants.PLUGIN_DESCRIPTOR);

							IWorkbenchPage page = PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getActivePage();
							page.openEditor(new FileEditorInput(ifile),
									TSOpenAction.PLUGIN_DESCRIPTOR_EDITOR);
						} catch (PartInitException e) {
							EclipsePlugin.log(e);

						}
					}
				});
			}
		}
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

}