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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.m0Generator;

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
import org.eclipse.jdt.core.IJavaProject;
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
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM0GeneratorProject;
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

public class NewM0GeneratorProjectWizard extends Wizard implements INewWizard {

	// Newly created project
	private IProject newProject;

	private IJavaProject newJavaProject;

	private NewM0GeneratorProjectWizardPage pageOne;

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
	public NewM0GeneratorProjectWizard() {
		super();

		setNeedsProgressMonitor(true);
		image = Images.getDescriptor(Images.TS_LOGO);
		setWindowTitle("New M0-Level Generator Project");
	}

	/**
	 * Adding the page to the wizard.
	 */

	@Override
	public void addPages() {
		if (LicensedAccess.getWorkbenchPluggablePluginRole() != TSWorkbenchPluggablePluginRole.CREATE_EDIT) {
			NewPluggablePluginErrorPage errorPage = new NewPluggablePluginErrorPage(
					"errorPage1");
			errorPage.setTitle("New M0-Level Generator Project Error");
			addPage(errorPage);
		} else {
			pageOne = new NewM0GeneratorProjectWizardPage(selection,
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

		final NewM0GeneratorProjectWizardPage.NewProjectDetails details = pageOne
				.getProjectNewProjectDetails();

		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			@Override
			protected void execute(IProgressMonitor monitor)
					throws CoreException {
				IProjectDetails projectDetails = TigerstripeCore
						.makeProjectDetails();
				projectDetails.setName(details.getProjectName());

				try {
					ITigerstripeM0GeneratorProject project = (ITigerstripeM0GeneratorProject) TigerstripeCore
							.createProject(projectDetails, null,
									ITigerstripeM0GeneratorProject.class, null,
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
			getContainer().run(false, true, op);
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
									.getFile(ITigerstripeConstants.M0_GENERATOR_DESCRIPTOR);

							IWorkbenchPage page = PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getActivePage();
							page.openEditor(new FileEditorInput(ifile),
									TSOpenAction.M0_DESCRIPTOR_EDITOR);
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

//	/***************************************************************************
//	 * Creates a new project resource with the name selected in the wizard page.
//	 * Project creation is wrapped in a <code>WorkspaceModifyOperation</code>.
//	 * <p>
//	 * 
//	 * @see org.eclipse.ui.actions.WorkspaceModifyOperation
//	 * 
//	 * @return the created project resource, or <code>null</code> if the
//	 *         project was not created
//	 */
//	public IProject createNewProject(
//			NewM0GeneratorProjectWizardPage.NewProjectDetails projectDetails) {
//		if (newProject != null)
//			return newProject;
//
//		// get a project handle
//		final IProject newProjectHandle = pageOne.getProjectHandle();
//		// get a project descriptor
//		IPath defaultPath = Platform.getLocation();
//		IPath newPath = pageOne.getLocationPath();
//		if (defaultPath.equals(newPath)) {
//			newPath = null;
//		}
//		IWorkspace workspace = ResourcesPlugin.getWorkspace();
//		final IProjectDescription description = workspace
//				.newProjectDescription(newProjectHandle.getName());
//		description.setLocation(newPath);
//
//		final NewM0GeneratorProjectWizardPage.NewProjectDetails details = projectDetails;
//
//		// create the new project operation
//		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
//			@Override
//			protected void execute(IProgressMonitor monitor)
//					throws CoreException {
//				createProject(details, description, newProjectHandle, monitor);
//				addJavaNature(newProjectHandle);
//				createProjectContent(details, newProjectHandle, monitor);
//			}
//		};
//
//		// run the new project creation operation
//		try {
//			getContainer().run(false, true, op);
//		} catch (InterruptedException e) {
//			return null;
//		} catch (InvocationTargetException e) {
//			// ie.- one of the steps resulted in a core exception
//			// resultError(
//			// "NewOWLProjectWizard:createNewProject:",
//			// "Project creation failed");
//			// TigerstripeRuntime.logErrorMessage("InvocationTargetException
//			// detected", e);
//			// OWLCore.logError(WizardsPlugin.getID(), "Project creation
//			// failed",
//			// e);
//			TigerstripeRuntime.logErrorMessage(
//					"InvocationTargetException detected", e);
//			return null;
//		}
//
//		newProject = newProjectHandle;
//
//		return newProject;
//	}
//
//	/***************************************************************************
//	 * Creates a project resource given the project handle and description.
//	 * 
//	 * @param description
//	 *            the project description to create a project resource for
//	 * @param projectHandle
//	 *            the project handle to create a project resource for
//	 * @param monitor
//	 *            the progress monitor to show visual progress with
//	 * 
//	 * @exception CoreException
//	 *                if the operation fails
//	 */
//	public void createProject(
//			NewM0GeneratorProjectWizardPage.NewProjectDetails projectDetails,
//			IProjectDescription description, IProject projectHandle,
//			IProgressMonitor monitor) throws CoreException {
//		try {
//			monitor.beginTask("", 2000);
//
//			projectHandle.create(description, new SubProgressMonitor(monitor,
//					1000));
//
//			if (monitor.isCanceled())
//				throw new OperationCanceledException();
//
//			projectHandle.open(new SubProgressMonitor(monitor, 1000));
//
//		} finally {
//			monitor.done();
//		}
//	}
//
//	private void addJavaNature(IProject projectHandle) {
//		IProjectDescription description;
//		try {
//			description = projectHandle.getDescription();
//			List<String> newIds = new ArrayList<String>();
//			newIds.addAll(Arrays.asList(description.getNatureIds()));
//			int tsNatureIndex = newIds
//					.indexOf(BuilderConstants.M0Generator_NATURE_ID);
//			if (tsNatureIndex == -1) {
//				newIds.add(BuilderConstants.M0Generator_NATURE_ID);
//			}
//
//			int index = newIds.indexOf(JavaCore.NATURE_ID);
//			if (index == -1) {
//				newIds.add(JavaCore.NATURE_ID);
//			}
//
//			description.setNatureIds((String[]) newIds
//					.toArray(new String[newIds.size()]));
//
//			projectHandle.setDescription(description, null);
//
//		} catch (CoreException e) {
//			TigerstripeRuntime.logErrorMessage("CoreException detected", e);
//		}
//	}
//
//	private void createProjectContent(
//			NewM0GeneratorProjectWizardPage.NewProjectDetails projectDetails,
//			IProject projectHandle, IProgressMonitor monitor) {
//
//		// first create the corresponding java project handle
//		newJavaProject = JavaCore.create(projectHandle);
//
//		// Create the src folder
//		IFolder folder = projectHandle.getFolder("src");
//		IFolder binFolder = projectHandle.getFolder("bin");
//		IFolder templateFolder = projectHandle.getFolder("templates");
//		try {
//			folder.create(true, true, null);
//			binFolder.create(true, true, null);
//			templateFolder.create(true, true, null);
//
//			// set the build path
//			IClasspathEntry[] buildPath = {
//					JavaCore.newSourceEntry(projectHandle.getFolder("src")
//							.getFullPath()),
//					JavaRuntime.getDefaultJREContainerEntry(),
//					JavaCore.newVariableEntry(new Path(
//							ITigerstripeConstants.EQUINOX_COMMON), null, null),
//					JavaCore.newVariableEntry(new Path(
//							ITigerstripeConstants.EXTERNALAPI_LIB), null, null) };
//
//			newJavaProject.setRawClasspath(buildPath, projectHandle
//					.getFullPath().append("bin"), null);
//
//			createDefaultM0Descriptor(projectHandle, monitor);
//
//			newJavaProject.getProject().refreshLocal(IResource.DEPTH_INFINITE,
//					monitor);
//		} catch (CoreException e) {
//			TigerstripeRuntime.logErrorMessage("CoreException detected", e);
//		}
//	}
//
//	private void createDefaultM0Descriptor(IProject projectHandle,
//			IProgressMonitor monitor) {
//
//		final IFile file = projectHandle
//				.getFile(ITigerstripeConstants.M0_GENERATOR_DESCRIPTOR);
//		try {
//			M0ProjectDescriptor descriptor = new M0ProjectDescriptor(
//					projectHandle.getLocation().toFile());
//			IProjectDetails details = descriptor.getProjectDetails();
//			details.setName(projectHandle.getName());
//			descriptor.setProjectDetails((ProjectDetails) details);
//			String contents = descriptor.asText();
//			ByteArrayInputStream stream = new ByteArrayInputStream(contents
//					.getBytes());
//
//			if (file.exists()) {
//				file.setContents(stream, true, true, monitor);
//			} else {
//				file.create(stream, true, monitor);
//			}
//			stream.close();
//		} catch (CoreException e) {
//			EclipsePlugin.log(e);
//		} catch (IOException e) {
//			EclipsePlugin.log(e);
//		} catch (TigerstripeException e) {
//			EclipsePlugin.log(e);
//		}
//	}
}