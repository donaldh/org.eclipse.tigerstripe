package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.CTabItemLocator;

public class CloseProject extends UITestCaseSWT {


	public void testCloseProject() throws Exception {
		IUIContext ui = getUI();
		ui.click(new XYLocator(new CTabItemLocator(
			TestingConstants.NEW_PROJECT_NAME+"/tigerstripe.xml"), 169, 13));
	}
}
