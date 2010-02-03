package org.eclipse.tigerstripe.workbench.ui.generator.test.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.workbench.ui.base.test.generator.NewArtifactRule;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.NewGlobalRule;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.NewPluginProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.SaveAndDeployPlugin;

public class GeneratorTestSuite extends TestCase
{
	
    public static Test suite()
    {
        TestSuite suite = new TestSuite(); 
        

        // create a new Plugin
        // This is in base.test
       suite.addTestSuite(NewPluginProject.class);
       suite.addTestSuite(NewGlobalRule.class);        
       suite.addTestSuite(NewArtifactRule.class);
             
        // Save/Deploy it
        suite.addTestSuite(SaveAndDeployPlugin.class);

        return suite;
    }
}
