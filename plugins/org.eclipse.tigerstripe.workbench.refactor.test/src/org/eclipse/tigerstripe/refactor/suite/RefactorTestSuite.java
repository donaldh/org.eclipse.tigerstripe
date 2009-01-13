package org.eclipse.tigerstripe.refactor.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.refactor.Vanilla.VanillaTests;
import org.eclipse.tigerstripe.refactor.Vanilla.TestRenamedPackageContents;
import org.eclipse.tigerstripe.refactor.Vanilla.RenamePackage;

public class RefactorTestSuite extends TestCase {
	
	public static Test suite()
    {
	TestSuite suite = new TestSuite(); 
    suite.addTestSuite(SimpleCleanWorkspace.class);
    
    
    suite.addTestSuite(VanillaTests.class);
    suite.addTestSuite(RenamePackage.class);
    suite.addTestSuite(TestRenamedPackageContents.class);
    return suite;
    }
}
