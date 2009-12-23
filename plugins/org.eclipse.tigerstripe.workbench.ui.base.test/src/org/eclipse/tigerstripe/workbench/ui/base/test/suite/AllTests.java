package org.eclipse.tigerstripe.workbench.ui.base.test.suite;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.workbench.ui.base.test.generator.Generate;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.DeleteProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.eclipse.tigerstripe.workbench.ui.base.test.suite");
		//$JUnit-BEGIN$

		// Bring up the Tigerstripe perspective
		GuiUtils.openTSPerspective();
		

		suite.addTestSuite(CleanWorkspace.class);
		
		suite.addTest(PluginTestSuite.suite());		
		suite.addTest(ProjectTestSuite.suite());
		suite.addTestSuite(Generate.class);
		

		suite.addTest(ProfileTestSuite.suite());
		
		//delete Project
		//suite.addTestSuite(DeleteProject.class);

		suite.addTest(ArtifactEditTestSuite.suite());
		
		
		//$JUnit-END$
		return suite;
	}

}
