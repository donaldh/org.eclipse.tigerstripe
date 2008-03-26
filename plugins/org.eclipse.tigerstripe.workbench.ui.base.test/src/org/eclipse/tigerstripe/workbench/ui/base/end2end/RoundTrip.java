package org.eclipse.tigerstripe.workbench.ui.base.end2end;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.ui.base.test.wizards.artifacts.NewArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.wizards.project.NewProjectWizardHelper;

public class RoundTrip extends TestCase {
	
	private String PROJECT_NAME = "RoundTrip";
	
	private String ENTITY_NAME = "ENITITY1";
	private IProject projectHandle; // of the Model project
	
	public final void setUp() throws Exception {
		NewProjectWizardHelper.createModelProject(PROJECT_NAME);
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		projectHandle = root.getProject(PROJECT_NAME);
		NewArtifactHelper.createArtifactWithWizard("com.test", ENTITY_NAME, IManagedEntityArtifact.class, projectHandle);
		
		
		
	}

	public final void tearDown() throws Exception {
		NewProjectWizardHelper.removeProject(PROJECT_NAME);

	}
	
	
	public final void testRoundTrip(){
		
	}
	
}
