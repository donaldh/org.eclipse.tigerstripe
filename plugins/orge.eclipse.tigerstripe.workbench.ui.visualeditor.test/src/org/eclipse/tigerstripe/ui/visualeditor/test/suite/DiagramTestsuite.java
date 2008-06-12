package org.eclipse.tigerstripe.ui.visualeditor.test.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.ui.visualeditor.test.diagram.DnDDiagram;
import org.eclipse.tigerstripe.ui.visualeditor.test.diagram.DropOn;
import org.eclipse.tigerstripe.ui.visualeditor.test.project.CreatePackage;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.CloseProject;
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
        
        // creates a new Project and add artifacts
        suite.addTestSuite(NewProject.class);
                
        // close Project Descriptor
        suite.addTestSuite(CloseProject.class);
        suite.addTestSuite(CreatePackage.class);
        
        // The CreateDiagram is a place for Creating Artifacts!
        //suite.addTestSuite(CreateDiagram.class);
        
        suite.addTestSuite(DnDDiagram.class);
       // suite.addTestSuite(DropOn.class);
        
        return suite;
    }
}
