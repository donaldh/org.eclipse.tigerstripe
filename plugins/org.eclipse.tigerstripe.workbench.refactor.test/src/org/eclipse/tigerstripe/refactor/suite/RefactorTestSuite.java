package org.eclipse.tigerstripe.refactor.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.refactor.changes.UpdateComponents;
import org.eclipse.tigerstripe.ui.visualeditor.test.diagram.CreateDiagram;
import org.eclipse.tigerstripe.ui.visualeditor.test.diagram.EditorBasedUpdates;
import org.eclipse.tigerstripe.ui.visualeditor.test.project.CreatePackage;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.NewProject;

public class RefactorTestSuite extends TestCase {
	
	public static Test suite()
    {
	TestSuite suite = new TestSuite(); 
    suite.addTestSuite(SimpleCleanWorkspace.class);
    
    // Most of the following could be replaced by an XML Load process
    
    // creates a new Project - do this so we are in the TS perspective
    suite.addTestSuite(NewProject.class);
    // Add a package to locate the diagram
    suite.addTestSuite(CreatePackage.class);
    
    // The CreateDiagram is a place for Creating Artifacts!
    suite.addTestSuite(CreateDiagram.class);
    suite.addTestSuite(EditorBasedUpdates.class);
    
    // Up to HERE
    
    // This is the good stuff...
    suite.addTestSuite(UpdateComponents.class);
    
    
    return suite;
    }
}
