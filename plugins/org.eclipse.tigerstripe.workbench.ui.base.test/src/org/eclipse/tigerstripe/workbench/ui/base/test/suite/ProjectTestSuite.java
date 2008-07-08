package org.eclipse.tigerstripe.workbench.ui.base.test.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.workbench.ui.base.test.generator.Generate;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.NewArtifactRule;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.NewGlobalRule;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.NewPluginProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.SaveAndDeployPlugin;
import org.eclipse.tigerstripe.workbench.ui.base.test.profile.CreateProfile;
import org.eclipse.tigerstripe.workbench.ui.base.test.profile.SaveAndDeployProfile;
import org.eclipse.tigerstripe.workbench.ui.base.test.profile.TestActiveProfile;
import org.eclipse.tigerstripe.workbench.ui.base.test.profile.UpdateProfileArtifacts;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.CloseProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.NewArtifacts;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.NewProject;

public class ProjectTestSuite extends TestCase
{
	/**
	 
	 * f) create a project.
	 * g) add artifacts & components - use stereotypes & primitive type
	 * 
	 
	 * 
	 */
    public static Test suite()
    {
        TestSuite suite = new TestSuite(); 
        suite.addTestSuite(CleanWorkspace.class);
        
        // creates a new Project - do this so we are in the TES perspective
        suite.addTestSuite(NewProject.class);
        
        
        //  add artifacts to our project
        suite.addTestSuite(NewArtifacts.class);
        
        // close Project
        suite.addTestSuite(CloseProject.class);
        
        return suite;
    }
}
