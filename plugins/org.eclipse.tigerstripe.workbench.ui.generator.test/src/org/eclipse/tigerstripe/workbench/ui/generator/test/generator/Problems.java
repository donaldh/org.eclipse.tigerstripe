package org.eclipse.tigerstripe.workbench.ui.generator.test.generator;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.CTabItemLocator;

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