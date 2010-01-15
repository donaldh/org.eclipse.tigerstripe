package org.eclipse.tigerstripe.workbench.ui.base.test.profile;

import org.eclipse.tigerstripe.workbench.ui.base.test.project.NewArtifacts;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ProjectRecord;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;

public class CheckPrimitives extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void testAddPrims() throws Exception {
		IUIContext ui = getUI();
		
		ProjectRecord.addArtifact(NewArtifacts.testNewArtifactDefaults(ui, "Datatype",
				TestingConstants.DATATYPE_NAMES[2], true, true, true, false, "string"));
		

	}

}