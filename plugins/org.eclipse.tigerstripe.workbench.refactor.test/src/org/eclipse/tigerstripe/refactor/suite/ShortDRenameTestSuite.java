package org.eclipse.tigerstripe.refactor.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.refactor.open.DiagramRenameTests;

public class ShortDRenameTestSuite extends TestCase {
	
	public static Test suite()
    {
	TestSuite suite = new TestSuite(); 
    suite.addTestSuite(SimpleCleanWorkspace.class);
    
    suite.addTestSuite(DownloadProject.class);
    suite.addTestSuite(DeleteProject.class);
    
    suite.addTestSuite(DiagramRenameTests.class);
    
    return suite;
    }
}
