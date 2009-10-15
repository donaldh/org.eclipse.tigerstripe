package org.eclipse.tigerstripe.workbench.ui.base.test.suite;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.eclipse.tigerstripe.workbench.ui.base.test.suite");
		//$JUnit-BEGIN$
		suite.addTest(MyTestsuite.suite());
		suite.addTest(PluginTestSuite.suite());
		suite.addTest(ArtifactEditTestSuite.suite());
		suite.addTest(ProjectTestSuite.suite());
		suite.addTestSuite(CleanWorkspace.class);
		suite.addTestSuite(TestClean.class);
		suite.addTest(ProfileTestSuite.suite());
		//$JUnit-END$
		return suite;
	}

}
