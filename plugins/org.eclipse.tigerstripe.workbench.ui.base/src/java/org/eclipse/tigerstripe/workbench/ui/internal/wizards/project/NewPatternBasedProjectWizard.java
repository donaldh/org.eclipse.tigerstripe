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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ProjectSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.project.ProjectDetails;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProjectFactory;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.patterns.IProjectPattern;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
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
import org.eclipse.ui.part.FileEditorInput;

/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "mpe". If a
 * sample multi-page editor (also available as a template) is registered for the
 * same extension, it will be able to open it.
 */

public class NewPatternBasedProjectWizard extends Wizard implements INewWizard {

	protected NewPatternBasedProjectWizardPage pageOne;

//	protected NewProjectWizardPageTwo pageTwo;

	private IStructuredSelection selection;

	private ImageDescriptor image;

	private IPattern pattern;
	
	public IStructuredSelection getSelection() {
		return this.selection;
	}

	public ImageDescriptor getDefaultImageDescriptor() {
		return this.image;
	}

	/**
	 * Constructor for NewProjectWizard.
	 */
	public NewPatternBasedProjectWizard(IPattern pattern) {
		super();
		this.pattern = pattern;
		setNeedsProgressMonitor(true);
		setDefaultPageImageDescriptor(Images.getDescriptor(Images.TS_LOGO));
		setWindowTitle(pattern.getDescription());
	}

	/**
	 * Adding the page to the wizard.
	 */

	@Override
	public void addPages() {

		TigerstripeRuntime.logInfoMessage("Adding pages");
		pageOne = new NewPatternBasedProjectWizardPage(pattern,selection);
		addPage(pageOne);

//		ExternalModules.getInstance().reload();
//TODO
//		if (ExternalModules.modulesExist) {
//			pageTwo = new NewProjectWizardPageTwo(getDefaultImageDescriptor());
//			addPage(pageTwo);
//		}
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {

		IWizardPage[] pages = getPages();
		NewPatternBasedProjectWizardPage pageOne = (NewPatternBasedProjectWizardPage) pages[0];
		IRunnableWithProgress runnable = getRunnable(pageOne);

		try {
			getContainer().run(false, false, runnable);
			openPerspective(TigerstripePerspectiveFactory.ID);
			openProject(pageOne.getProjectNewProjectDetails());
		} catch (InvocationTargetException e) {
			handleFinishException(getShell(), e);
			return false;
		} catch (WorkbenchException e) {
			BasePlugin.log(e);
			return false;
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	protected void handleFinishException(Shell shell,
			InvocationTargetException e) {
		String title = NewWizardMessages.NewElementWizard_op_error_title;
		String message = NewWizardMessages.NewElementWizard_op_error_message;
		ExceptionHandler.handle(e, shell, title, message);
	}
	
	
	public IRunnableWithProgress getRunnable(NewPatternBasedProjectWizardPage pageOne) {
		try {
			
			final String projectName = pageOne.getProjectName();

			IPath defaultPath = Platform.getLocation();
			IPath newPath = pageOne.getLocationPath();
			if (defaultPath.equals(newPath)) {
				newPath = null;
			}
			
			final String defaultArtifactPackage = pageOne.getDefaultArtifactPackageText();
			
			final IPath path = newPath;
			final IProjectPattern pPattern = (IProjectPattern) pattern; 
				
			IRunnableWithProgress op = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor)
				throws InvocationTargetException{
					try {
						
						ITigerstripeModelProject newProject = pPattern.createProject(projectName, path, defaultArtifactPackage);
						pPattern.annotateProject(newProject);

					} catch (Exception e) {
						throw new InvocationTargetException(e);
					} finally {
						monitor.done();
					}
				}
			};

			return op;
		} catch (Exception t){
			return null;
		}
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