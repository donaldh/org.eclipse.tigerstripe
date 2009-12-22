package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class CloseProject extends UITestCaseSWT {


	public void testCloseProject() throws Exception {
		IUIContext ui = getUI();
		TreeItemLocator treeItem = new TreeItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME+"/src",
				new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
		ui.click(2,treeItem);
//		ui.click(2,
//				new TreeItemLocator(
//						TestingConstants.NEW_MODEL_PROJECT_NAME+"/src",
//						new ViewLocator(
//								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui.close(new CTabItemLocator(
			TestingConstants.NEW_MODEL_PROJECT_NAME+"/tigerstripe.xml"));
	}
}
