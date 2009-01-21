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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.project;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ProjectSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProjectFactory;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
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

/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "mpe". If a
 * sample multi-page editor (also available as a template) is registered for the
 * same extension, it will be able to open it.
 */

public class NewProjectWizard extends Wizard implements INewWizard {

	protected NewProjectWizardPage pageOne;

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
	public NewProjectWizard() {
		super();

		setNeedsProgressMonitor(true);
		image = Images.getDescriptor(Images.TS_LOGO);
		setWindowTitle("New Tigerstripe Project");
	}

	/**
	 * Adding the page to the wizard.
	 */

	@Override
	public void addPages() {

		TigerstripeRuntime.logInfoMessage("Adding pages");
		pageOne = new NewProjectWizardPage(selection,
				getDefaultImageDescriptor());
		addPage(pageOne);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {

		try {
			Map reqModules = new HashMap();
			IRunnableWithProgress moduleCopier = null;
			IRunnableWithProgress projectCreator = pageOne
					.getRunnable(reqModules);
			runInContainer(new IRunnableWithProgress[] { moduleCopier,
					projectCreator });

			openPerspective(TigerstripePerspectiveFactory.ID);
			openProject(pageOne.getProjectNewProjectDetails());
			return true;
		} catch (InterruptedException e) {
			EclipsePlugin.log(e);
			return false;
		} catch (InvocationTargetException e) {
			EclipsePlugin.log(e);
			return false;
		} catch (WorkbenchException e) {
			EclipsePlugin.log(e);
			return false;
		}
	}

	/**
	 * Runs the given runnable with the wizard container in a single
	 * WorkspaceModifyOperation.
	 * 
	 * This means that workspace changes will not be propagated to listened
	 * until the operation completes.
	 * 
	 * @param runnable
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	private void runInContainer(final IRunnableWithProgress[] runnables)
			throws InterruptedException, InvocationTargetException {

		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			@Override
			protected void execute(IProgressMonitor monitor)
					throws CoreException, InvocationTargetException,
					InterruptedException {
				for (IRunnableWithProgress runnable : runnables) {
					if (runnable != null)
						runnable.run(monitor);
				}
			}
		};

		getContainer().run(false, true, op);
	}

	/**
	 * Implements Open Perspective.
	 */
	private void openPerspective(String perspId) throws WorkbenchException {
		IWorkbench workbench = PlatformUI.getWorkbench();
		workbench
				.showPerspective(perspId, workbench.getActiveWorkbenchWindow());
	}

	/**
	 * 
	 */
	private void openProject(final NewProjectDetails projectDetails) {
		final IWorkbenchPage activePage = EclipsePlugin.getActivePage();
		if (activePage != null) {
			final Display display = getShell().getDisplay();
			if (display != null) {
				display.asyncExec(new Runnable() {
					public void run() {
						try {
							ProjectSessionImpl session = TigerstripeProjectFactory.INSTANCE
									.getProjectSession();
							String desc = projectDetails.projectDirectory
									+ File.separator
									+ projectDetails.projectName;
							// + File.separator + "tigerstripe.xml";
							File file = new File(desc);
							ITigerstripeModelProject project = (ITigerstripeModelProject) session
									.makeTigerstripeProject(file.toURI(), null);

							IWorkspace workspace = ResourcesPlugin
									.getWorkspace();
							IWorkspaceRoot root = workspace.getRoot();
							IProject iproject = root.getProject(projectDetails
									.getProjectName());
							IFile ifile = iproject
									.getFile(ITigerstripeConstants.PROJECT_DESCRIPTOR);

							IWorkbenchPage page = PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getActivePage();
							page.openEditor(new FileEditorInput(ifile),
									TSOpenAction.DESCRIPTOR_EDITOR);
						} catch (PartInitException e) {
							EclipsePlugin.log(e);
						} catch (TigerstripeException e) {
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