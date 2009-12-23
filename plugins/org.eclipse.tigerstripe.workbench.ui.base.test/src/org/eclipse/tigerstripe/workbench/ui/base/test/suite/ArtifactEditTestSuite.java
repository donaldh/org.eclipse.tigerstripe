package org.eclipse.tigerstripe.workbench.ui.base.test.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.workbench.ui.base.test.project.ClearExpectedAuditErrors;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.CloseProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.DeleteProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.NewProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.OneArtifact;

public class ArtifactEditTestSuite extends TestCase
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
        //suite.addTestSuite(CleanWorkspace.class);
        
        // creates a new Project - do this so we are in the TES perspective
        //suite.addTestSuite(NewProject.class);
        
        
        //  add artifact to our project
        suite.addTestSuite(OneArtifact.class);
        
        // Need to check changes AREN't made to the model if we 
        // choose NO when closing an editor.
        
        // close Project
        suite.addTestSuite(CloseProject.class);
        
      //delete Project
		suite.addTestSuite(DeleteProject.class);

        return suite;
    }
}
