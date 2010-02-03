package org.eclipse.tigerstripe.workbench.ui.generator.test.suite;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.workbench.ui.base.test.generator.Generate;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.eclipse.tigerstripe.workbench.ui.base.test.suite");

		GuiUtils.openTSPerspective();
		suite.addTestSuite(CleanWorkspace.class);
		// This reproduces the basic stuff from the base.test
		suite.addTest(GeneratorTestSuite.suite());		
		
		// Now we need to set lots of options.
		// and generate as we go along
		

		
		return suite;
	}

}
