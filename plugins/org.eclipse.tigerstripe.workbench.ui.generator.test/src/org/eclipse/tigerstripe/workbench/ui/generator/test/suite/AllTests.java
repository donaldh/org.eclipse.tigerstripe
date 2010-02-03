package org.eclipse.tigerstripe.workbench.ui.generator.test.suite;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.workbench.ui.base.test.suite.CleanWorkspace;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;
import org.eclipse.tigerstripe.workbench.ui.generator.test.generator.Clicks;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.eclipse.tigerstripe.workbench.ui.generator.test.suite");

		// This reproduces the basic stuff from the base.test
		GuiUtils.openTSPerspective();
		suite.addTestSuite(CleanWorkspace.class);
		suite.addTest(GeneratorTestSuite.suite());		
		
		// Now we need to set lots of options.
		// and generate as we go along
		
		suite.addTestSuite(Clicks.class);		
		
		return suite;
	}

}
