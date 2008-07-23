package org.eclipse.tigerstripe.ui.visualeditor.test.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.ui.visualeditor.test.diagram.CreateDiagram;
import org.eclipse.tigerstripe.ui.visualeditor.test.diagram.EditorBasedUpdates;
import org.eclipse.tigerstripe.ui.visualeditor.test.project.CreatePackage;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.NewProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.CleanWorkspace;

public class DiagramTestsuite extends TestCase
{
	/**
	 * This test suite should 
	 * a) create a project
	 * 
	 * create the package
	 * create a diagram
	 * 
	 * create artifacts on diagram
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
    public static Test suite()
    {
        TestSuite suite = new TestSuite(); 
        suite.addTestSuite(CleanWorkspace.class);
        
        // creates a new Project - do this so we are in the TS perspective
        suite.addTestSuite(NewProject.class);
        
        // Create a profile
//        suite.addTestSuite(CreateProfile.class);
        
        // Add a series of stereotypes
//        suite.addTestSuite(StereotypeAdditions.class);
                
        // Save/Deploy it
//        suite.addTestSuite(SaveAndDeployProfile.class);
          
        // close Project Descriptor
        // This is closed when we deploy!
        // suite.addTestSuite(CloseProject.class);
        
        // Add a package to locate the diagram
        suite.addTestSuite(CreatePackage.class);
        
        // The CreateDiagram is a place for Creating Artifacts!
        suite.addTestSuite(CreateDiagram.class);
        suite.addTestSuite(EditorBasedUpdates.class);
        
        
        
//        suite.addTestSuite(DnDDiagram.class);
//       suite.addTestSuite(DropOn.class);
        
        return suite;
    }
}
