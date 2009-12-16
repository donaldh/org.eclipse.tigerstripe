package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.CTabItemLocator;

public class CloseProject extends UITestCaseSWT {


	public void testCloseProject() throws Exception {
		IUIContext ui = getUI();
		ui.close(new CTabItemLocator(
			TestingConstants.NEW_MODEL_PROJECT_NAME+"/tigerstripe.xml"));
	}
}
