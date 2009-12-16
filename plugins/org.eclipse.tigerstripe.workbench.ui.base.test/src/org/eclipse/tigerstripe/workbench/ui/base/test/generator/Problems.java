package org.eclipse.tigerstripe.workbench.ui.base.test.generator;

import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.IUIContext;

public class Problems extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void testProblems() throws Exception {
		IUIContext ui = getUI();
		ui.click(new CTabItemLocator("Annotation Property View"));
		ui.click(new CTabItemLocator("Problems"));
	}

}