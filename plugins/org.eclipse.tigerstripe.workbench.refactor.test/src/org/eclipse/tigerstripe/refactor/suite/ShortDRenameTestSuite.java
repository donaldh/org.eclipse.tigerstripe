package org.eclipse.tigerstripe.refactor.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.refactor.Vanilla.RenamePackage;
import org.eclipse.tigerstripe.refactor.Vanilla.TestRenamedPackageContents;
import org.eclipse.tigerstripe.refactor.Vanilla.VanillaTests;
import org.eclipse.tigerstripe.refactor.closed.ClosedDiagramTests;
import org.eclipse.tigerstripe.refactor.open.DiagramRenameTests;
import org.eclipse.tigerstripe.refactor.open.OpenDiagramTests;
import org.eclipse.tigerstripe.refactor.project.RecreateSimpleProject;

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
