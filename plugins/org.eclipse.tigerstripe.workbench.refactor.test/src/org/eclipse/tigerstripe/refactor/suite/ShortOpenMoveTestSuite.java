package org.eclipse.tigerstripe.refactor.suite;

import org.eclipse.tigerstripe.refactor.open.OpenDiagramMoveTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ShortOpenMoveTestSuite extends TestCase {
	
	public static Test suite()
    {
	TestSuite suite = new TestSuite(); 
    suite.addTestSuite(SimpleCleanWorkspace.class);
    
    suite.addTestSuite(DownloadProject.class);
    suite.addTestSuite(DeleteProject.class);
    
    suite.addTestSuite(OpenDiagramMoveTests.class);
    
    return suite;
    }
}
