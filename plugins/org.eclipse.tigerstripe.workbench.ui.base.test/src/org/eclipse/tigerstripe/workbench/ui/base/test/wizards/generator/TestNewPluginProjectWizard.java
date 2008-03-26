package org.eclipse.tigerstripe.workbench.ui.base.test.wizards.generator;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.pluggablePlugin.NewPluginProjectWizard;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.pluggablePlugin.NewPluginProjectWizardPage;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

public class TestNewPluginProjectWizard extends NewPluginProjectWizard {

	
	public boolean testCreate( TestNewPluginProjectWizardPage pageOne) {

		final NewPluginProjectWizardPage.NewProjectDetails projectDetails = pageOne
				.getProjectNewProjectDetails();

		createNewProject(projectDetails);


		return true;
	}
	
	/***************************************************************************
	 * Dummy method to create a plugin project 
	 * Uses wizard code. just without the GUI part
	 */
	public void createNewProjectWizard(TestNewPluginProjectWizardPage pageOne
			) {
		
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

		final NewPluginProjectWizardPage.NewProjectDetails details = pageOne.getProjectNewProjectDetails();

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
			op.run(new NullProgressMonitor());
		} catch (InterruptedException e) {
			return;
		} catch (InvocationTargetException e) {
			TigerstripeRuntime.logErrorMessage(
					"InvocationTargetException detected", e);
			return ;
		}

	}
	
}
