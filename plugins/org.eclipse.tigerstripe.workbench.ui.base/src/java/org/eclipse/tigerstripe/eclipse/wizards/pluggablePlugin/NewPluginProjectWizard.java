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
package org.eclipse.tigerstripe.eclipse.wizards.pluggablePlugin;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.eclipse.runtime.images.TigerstripePluginImages;
import org.eclipse.tigerstripe.workbench.API;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.TigerstripeLicenseException;
import org.eclipse.tigerstripe.workbench.internal.api.project.IProjectSession;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.LicensedAccess;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.TSWorkbenchPluggablePluginRole;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.perspective.TigerstripePerspectiveFactory;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.actions.TSOpenAction;
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

	// Newly created project
	private IProject newProject;

	private IJavaProject newJavaProject;

	private NewPluginProjectWizardPage pageOne;

	private IStructuredSelection selection;

	private ImageDescriptor image;

	private ByteArrayOutputStream buffer;

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
		image = TigerstripePluginImages.DESC_TS_LOGO;
		setWindowTitle("New Tigerstripe Plugin Project");
	}

	/**
	 * Adding the page to the wizard.
	 */

	@Override
	public void addPages() {
		if (LicensedAccess.getWorkbenchPluggablePluginRole() != TSWorkbenchPluggablePluginRole.CREATE_EDIT) {
			NewPluggablePluginErrorPage errorPage = new NewPluggablePluginErrorPage(
					"errorPage1");
			errorPage.setTitle("New Tigerstripe Plugin Project");
			errorPage.setTitle("New Plugin Project Error");
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

		final NewPluginProjectWizardPage.NewProjectDetails projectDetails = pageOne
				.getProjectNewProjectDetails();

		createNewProject(projectDetails);
		openPerspective(TigerstripePerspectiveFactory.ID);
		openProject(projectDetails);
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
	private void openProject(
			final NewPluginProjectWizardPage.NewProjectDetails projectDetails) {
		final IWorkbenchPage activePage = EclipsePlugin.getActivePage();
		if (activePage != null) {
			final Display display = getShell().getDisplay();
			if (display != null) {
				display.asyncExec(new Runnable() {
					public void run() {
						try {
							IProjectSession session = API
									.getDefaultProjectSession();
							String desc = projectDetails.projectDirectory
									+ File.separator
									+ projectDetails.projectName;
							File file = new File(desc);
							// IPluggablePluginProject project =
							// (IPluggablePluginProject) session
							// .makeTigerstripeProject(file.toURI(), null);

							IWorkspace workspace = ResourcesPlugin
									.getWorkspace();
							IWorkspaceRoot root = workspace.getRoot();
							IProject iproject = root.getProject(projectDetails
									.getProjectName());
							IFile ifile = iproject
									.getFile(ITigerstripeConstants.PLUGIN_DESCRIPTOR);

							IWorkbenchPage page = PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getActivePage();
							page.openEditor(new FileEditorInput(ifile),
									TSOpenAction.PLUGIN_DESCRIPTOR_EDITOR);
						} catch (PartInitException e) {
							EclipsePlugin.log(e);
						} catch (TigerstripeLicenseException e) {
							EclipsePlugin.log(e);
							// } catch (TigerstripeException e) {
							// EclipsePlugin.log(e);
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

	/***************************************************************************
	 * Creates a new project resource with the name selected in the wizard page.
	 * Project creation is wrapped in a <code>WorkspaceModifyOperation</code>.
	 * <p>
	 * 
	 * @see org.eclipse.ui.actions.WorkspaceModifyOperation
	 * 
	 * @return the created project resource, or <code>null</code> if the
	 *         project was not created
	 */
	public IProject createNewProject(
			NewPluginProjectWizardPage.NewProjectDetails projectDetails) {
		if (newProject != null)
			return newProject;

		// get a project handle
		final IProject newProjectHandle = pageOne.getProjectHandle();
		// get a project descriptor
		IPath defaultPath = Platform.getLocation();
		IPath newPath = pageOne.getLocationPath();
		if (defaultPath.equals(newPath)) {
			newPath = null;
		}
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProjectDescription description = workspace
				.newProjectDescription(newProjectHandle.getName());
		description.setLocation(newPath);

		final NewPluginProjectWizardPage.NewProjectDetails details = projectDetails;

		// create the new project operation
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			@Override
			protected void execute(IProgressMonitor monitor)
					throws CoreException {
				createProject(details, description, newProjectHandle, monitor);
				addJavaNature(newProjectHandle);
				createProjectContent(details, newProjectHandle, monitor);
			}
		};

		// run the new project creation operation
		try {
			getContainer().run(false, true, op);
		} catch (InterruptedException e) {
			return null;
		} catch (InvocationTargetException e) {
			// ie.- one of the steps resulted in a core exception
			// resultError(
			// "NewOWLProjectWizard:createNewProject:",
			// "Project creation failed");
			// TigerstripeRuntime.logErrorMessage("InvocationTargetException
			// detected", e);
			// OWLCore.logError(WizardsPlugin.getID(), "Project creation
			// failed",
			// e);
			TigerstripeRuntime.logErrorMessage(
					"InvocationTargetException detected", e);
			return null;
		}

		newProject = newProjectHandle;

		return newProject;
	}

	/***************************************************************************
	 * Creates a project resource given the project handle and description.
	 * 
	 * @param description
	 *            the project description to create a project resource for
	 * @param projectHandle
	 *            the project handle to create a project resource for
	 * @param monitor
	 *            the progress monitor to show visual progress with
	 * 
	 * @exception CoreException
	 *                if the operation fails
	 */
	public void createProject(
			NewPluginProjectWizardPage.NewProjectDetails projectDetails,
			IProjectDescription description, IProject projectHandle,
			IProgressMonitor monitor) throws CoreException {
		try {
			monitor.beginTask("", 2000);

			projectHandle.create(description, new SubProgressMonitor(monitor,
					1000));

			if (monitor.isCanceled())
				throw new OperationCanceledException();

			projectHandle.open(new SubProgressMonitor(monitor, 1000));

		} finally {
			monitor.done();
		}
	}

	private void addJavaNature(IProject projectHandle) {
		IProjectDescription description;
		try {
			description = projectHandle.getDescription();
			List newIds = new ArrayList();
			newIds.addAll(Arrays.asList(description.getNatureIds()));
			int tsNatureIndex = newIds
					.indexOf(TigerstripePluginConstants.PLUGINPROJECT_NATURE_ID);
			if (tsNatureIndex == -1) {
				newIds.add(TigerstripePluginConstants.PLUGINPROJECT_NATURE_ID);
			}

			int index = newIds.indexOf(JavaCore.NATURE_ID);
			if (index == -1) {
				newIds.add(JavaCore.NATURE_ID);
			}

			description.setNatureIds((String[]) newIds
					.toArray(new String[newIds.size()]));

			projectHandle.setDescription(description, null);

		} catch (CoreException e) {
			// TODO proper exception handling
			TigerstripeRuntime.logErrorMessage("CoreException detected", e);
			IStatus[] statuses = e.getStatus().getChildren();
		}
	}

	private void createProjectContent(
			NewPluginProjectWizardPage.NewProjectDetails projectDetails,
			IProject projectHandle, IProgressMonitor monitor) {

		// first create the corresponding java project handle
		newJavaProject = JavaCore.create(projectHandle);

		// Create the src folder
		IFolder folder = projectHandle.getFolder("src");
		IFolder binFolder = projectHandle.getFolder("bin");
		IFolder templateFolder = projectHandle.getFolder("templates");
		try {
			folder.create(true, true, null);
			binFolder.create(true, true, null);
			templateFolder.create(true, true, null);

			// set the build path
			IClasspathEntry[] buildPath = {
					JavaCore.newSourceEntry(projectHandle.getFolder("src")
							.getFullPath()),
					JavaRuntime.getDefaultJREContainerEntry(),
					JavaCore.newVariableEntry(new Path(
							ITigerstripeConstants.EXTERNALAPI_LIB), null, null) };

			newJavaProject.setRawClasspath(buildPath, projectHandle
					.getFullPath().append("bin"), null);

			createDefaultTigerstripePluginDescriptor(projectHandle,
					projectDetails, monitor);

			newJavaProject.getProject().refreshLocal(IResource.DEPTH_INFINITE,
					monitor);
		} catch (CoreException e) {
			TigerstripeRuntime.logErrorMessage("CoreException detected", e);
		}
	}

	private void createDefaultTigerstripePluginDescriptor(
			IProject projectHandle,
			NewPluginProjectWizardPage.NewProjectDetails projectDetails,
			IProgressMonitor monitor) {

		final IFile file = projectHandle
				.getFile(ITigerstripeConstants.PLUGIN_DESCRIPTOR);
		try {
			InputStream stream = openContentStream(projectDetails);
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		} catch (IOException e) {
			EclipsePlugin.log(e);
		}
	}

	/**
	 * We will initialize file contents with a sample text.
	 * 
	 * @param pageProperties -
	 *            the properties gathered through the wizard
	 */
	private InputStream openContentStream(
			NewPluginProjectWizardPage.NewProjectDetails projectDetails) {

		byte[] bytes = null;
		buffer = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				buffer));

		NewTigerstripePluginDescriptorGenerator generator = new NewTigerstripePluginDescriptorGenerator(
				projectDetails, writer);
		generator.applyTemplate();

		bytes = buffer.toByteArray();
		return new ByteArrayInputStream(bytes);
	}
}