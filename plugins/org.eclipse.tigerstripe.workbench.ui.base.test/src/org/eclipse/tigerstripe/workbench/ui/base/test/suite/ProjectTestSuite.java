package org.eclipse.tigerstripe.workbench.ui.base.test.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.workbench.ui.base.test.project.ClearExpectedAuditErrors;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.CloseProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.NewArtifacts;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.NewProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

public class ProjectTestSuite extends TestCase {
	/**
	 * 
	 * f) create a project. g) add artifacts & components - use stereotypes &
	 * primitive type
	 * 
	 * 
	 * 
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite();

		// Bring up the Tigerstripe perspective
		GuiUtils.openTSPerspective();

		suite.addTestSuite(CleanWorkspace.class);

		// creates a new Project - do this so we are in the TES perspective
		suite.addTestSuite(NewProject.class);

		// add artifacts to our project
		suite.addTestSuite(NewArtifacts.class);

		suite.addTestSuite(ClearExpectedAuditErrors.class);

		// close Project
		suite.addTestSuite(CloseProject.class);

		return suite;
	}

}
