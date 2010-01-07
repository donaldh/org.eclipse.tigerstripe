package org.eclipse.tigerstripe.refactor.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.refactor.Vanilla.MoveTests;
import org.eclipse.tigerstripe.refactor.Vanilla.RenamePackage;
import org.eclipse.tigerstripe.refactor.Vanilla.TestRenamedPackageContents;
import org.eclipse.tigerstripe.refactor.Vanilla.VanillaTests;
import org.eclipse.tigerstripe.refactor.delete.DeleteTests;
import org.eclipse.tigerstripe.refactor.project.RecreateSimpleProject;

public class RefactorTestSuite extends TestCase {
	
	public static Test suite()
    {
	TestSuite suite = new TestSuite(); 
    suite.addTestSuite(SimpleCleanWorkspace.class);
    
    
    suite.addTestSuite(VanillaTests.class);
    suite.addTestSuite(RenamePackage.class);
    suite.addTestSuite(TestRenamedPackageContents.class);

    suite.addTestSuite(DeleteProject.class);
    suite.addTestSuite(RecreateSimpleProject.class);
    suite.addTestSuite(DeleteProject.class);
    
    suite.addTestSuite(MoveTests.class);
    
    suite.addTestSuite(DeleteProject.class);
    
    suite.addTestSuite(DeleteTests.class);
	
	suite.addTestSuite(DeleteProject.class);
    
    return suite;
    }
}
