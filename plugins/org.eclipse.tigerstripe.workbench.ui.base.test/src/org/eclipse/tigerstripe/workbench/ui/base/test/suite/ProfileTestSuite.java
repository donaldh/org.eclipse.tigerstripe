package org.eclipse.tigerstripe.workbench.ui.base.test.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.workbench.ui.base.test.generator.Generate;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.NewArtifactRule;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.NewGlobalRule;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.NewPluginProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.SaveAndDeployPlugin;
import org.eclipse.tigerstripe.workbench.ui.base.test.profile.AddStereos;
import org.eclipse.tigerstripe.workbench.ui.base.test.profile.CreateProfile;
import org.eclipse.tigerstripe.workbench.ui.base.test.profile.Explorer;
import org.eclipse.tigerstripe.workbench.ui.base.test.profile.SaveAndDeployProfile;
import org.eclipse.tigerstripe.workbench.ui.base.test.profile.TestActiveProfile;
import org.eclipse.tigerstripe.workbench.ui.base.test.profile.UpdateProfileArtifacts;
import org.eclipse.tigerstripe.workbench.ui.base.test.profile.CreateStereotypes;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.CloseProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.NewArtifacts;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.NewProject;

import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;

public class ProfileTestSuite extends TestCase
{
	/**
	 * This test suite should 
	 * a) create a profile
	 * 
	 * 
	 */
    public static Test suite()
    {
        TestSuite suite = new TestSuite(); 
        
        // Create a profile
       suite.addTestSuite(CreateProfile.class);
        // Save/Deploy it
        suite.addTestSuite(SaveAndDeployProfile.class);
        // Make sure it appears correctly in the menu
        suite.addTestSuite(TestActiveProfile.class);
        
        suite.addTestSuite(UpdateProfileArtifacts.class);
        
        //Create Stereos
        suite.addTestSuite(CreateStereotypes.class);
		// Add one to the relevant object
        suite.addTestSuite(AddStereos.class);
        
        suite.addTestSuite(Explorer.class);
			// Check it appears in the correct place
			// check it *doesn't* appear in the wrong place?
		
		// Remove it again
			// see if its gone!
		
		// remove from profile
			// make sure they are no longer allowed
        
        
        
        // close Project
       // suite.addTestSuite(CloseProject.class);
        
        return suite;
    }
}
