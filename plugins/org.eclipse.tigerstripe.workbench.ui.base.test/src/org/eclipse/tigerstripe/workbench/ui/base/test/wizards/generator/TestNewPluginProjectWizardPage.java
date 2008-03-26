package org.eclipse.tigerstripe.workbench.ui.base.test.wizards.generator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.pluggablePlugin.NewPluginProjectWizardPage;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.pluggablePlugin.NewPluginProjectWizardPage.NewProjectDetails;

public class TestNewPluginProjectWizardPage extends NewPluginProjectWizardPage {


	private IProject projectHandle ;
	
	NewProjectDetails result = new NewProjectDetails();
	
	public TestNewPluginProjectWizardPage(ISelection selection,
			ImageDescriptor image) {
		super(selection, image);

	}
	
	public NewProjectDetails getProjectNewProjectDetails() {
		return result;
	}
	
	public void setProjectNewProjectDetails(String name, String projectDirectory){
		result.projectName = name;
		result.projectDirectory = projectDirectory;

	}

	public void setProjectHandle(IProject project){
		this.projectHandle = project;
	}

	@Override
	public IProject getProjectHandle() {
		// TODO Auto-generated method stub
		return projectHandle;
	}

	public IPath getLocationPath() {
		return new Path(result.projectDirectory);
	}
	
}
