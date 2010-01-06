package org.eclipse.tigerstripe.workbench.ui.base.test.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.workbench.ui.base.test.generator.Generate;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.NewArtifactRule;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.NewGlobalRule;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.NewPluginProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.SaveAndDeployPlugin;
import org.eclipse.tigerstripe.workbench.ui.base.test.profile.CreateProfile;
import org.eclipse.tigerstripe.workbench.ui.base.test.profile.SaveAndDeployProfile;
import org.eclipse.tigerstripe.workbench.ui.base.test.profile.TestActiveProfile;
import org.eclipse.tigerstripe.workbench.ui.base.test.profile.UpdateProfileArtifacts;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ClearExpectedAuditErrors;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.CloseProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.NewArtifacts;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.NewProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.OneArtifact;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

public class MyTestsuite extends TestCase
{
	/**
	 * This test suite should 
	 * a) create a profile
	 * b) add primitive type
	 * c) add stereotypes (with attribute)
	 * d) make active.
	 * 
	 * e) create a simple plugin & deploy 
	 * 
	 * 
	 * f) create a project.
	 * g) add artifacts & components - use stereotypes & primitive type
	 * 
	 * h) enable plugin.
	 * i) generate.
	 * j) compare output with expected result
	 * 
	 * k) delete artifacts/components
	 * l) generate
	 * m) compare output with expected result
	 * 
	 * 
	 * 
	 */
	public static Test suite()
	{
		TestSuite suite = new TestSuite(
		"Test for org.eclipse.tigerstripe.workbench.ui.base.test.suite");
		//$JUnit-BEGIN$

		// Bring up the Tigerstripe perspective
		GuiUtils.openTSPerspective();


		suite.addTestSuite(CleanWorkspace.class);
		suite.addTest(PluginTestSuite.suite());	
		suite.addTest(ProjectTestSuite.suite());
//		suite.addTestSuite(Generate.class);
//
//		suite.addTestSuite(OneArtifact.class);

		suite.addTest(ProfileTestSuite.suite());

		//$JUnit-END$
		return suite;
	}
}
