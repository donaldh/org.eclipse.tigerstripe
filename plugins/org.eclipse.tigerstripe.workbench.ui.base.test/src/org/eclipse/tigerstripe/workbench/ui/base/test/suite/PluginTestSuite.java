package org.eclipse.tigerstripe.workbench.ui.base.test.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.workbench.ui.base.test.generator.NewArtifactRule;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.NewGlobalRule;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.NewPluginProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.SaveAndDeployPlugin;

public class PluginTestSuite extends TestCase
{
	
    public static Test suite()
    {
        TestSuite suite = new TestSuite(); 
        
        
        
        // create a new Plugin
       suite.addTestSuite(NewPluginProject.class);
        
        // Check what happens with rules - audit checks etc
        suite.addTestSuite(NewGlobalRule.class);
        
        suite.addTestSuite(NewArtifactRule.class);
        
        
        // Save/Deploy it
        suite.addTestSuite(SaveAndDeployPlugin.class);

        return suite;
    }
}
