package org.eclipse.tigerstripe.refactor.suite;

import org.eclipse.tigerstripe.refactor.Vanilla.VanillaTests;
import org.eclipse.tigerstripe.refactor.project.ProjectHelper;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RefactorTestSuite extends TestCase {
	
	public static Test suite()
    {
	TestSuite suite = new TestSuite(); 
    suite.addTestSuite(SimpleCleanWorkspace.class);
    
    suite.addTestSuite(VanillaTests.class);
    
    return suite;
    }
}
