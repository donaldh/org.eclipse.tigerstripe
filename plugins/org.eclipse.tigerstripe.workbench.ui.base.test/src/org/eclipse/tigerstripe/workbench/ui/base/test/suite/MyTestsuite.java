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
import org.eclipse.tigerstripe.workbench.ui.base.test.project.CloseProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.NewArtifacts;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.NewProject;

public class MyTestsuite extends TestCase
{
	/**
	 * This test suite should 
	 * a) create a profile
	 * b) add primitive type
	 * c) add stereotypes (with attribute)
	 * d) make active.
	 * 
	 * e) create a simple plugin & deploy 
	 * 
	 * 
	 * f) create a project.
	 * g) add artifacts & components - use stereotypes & primitive type
	 * 
	 * h) enable plugin.
	 * i) generate.
	 * j) compare output with expected result
	 * 
	 * k) delete artifacts/components
	 * l) generate
	 * m) compare output with expected result
	 * 
	 * 
	 * 
	 */
    public static Test suite()
    {
        TestSuite suite = new TestSuite(); 
        suite.addTestSuite(CleanWorkspace.class);
        
//        // Create a profile
//        suite.addTestSuite(CreateProfile.class);
//        // Save/Deploy it
//        suite.addTestSuite(SaveAndDeployProfile.class);
//        // Make sure it appears correctly in the menu
//        suite.addTestSuite(TestActiveProfile.class);
//        
//        // create a new Plugin
//        suite.addTestSuite(NewPluginProject.class);
//        
//        // Check what happens with rules - audit checks etc
//        suite.addTestSuite(NewGlobalRule.class);
//        
//        suite.addTestSuite(NewArtifactRule.class);
//        
//        
//        // Save/Deploy it
//        suite.addTestSuite(SaveAndDeployPlugin.class);

        
        // creates a new Project and add artifacts
        suite.addTestSuite(NewProject.class);
        suite.addTestSuite(NewArtifacts.class);
                
        
        
//        // turn on plugin in Project and generate
//        suite.addTestSuite(Generate.class);
//        
        // close Project
        suite.addTestSuite(CloseProject.class);
        
        return suite;
    }
}
