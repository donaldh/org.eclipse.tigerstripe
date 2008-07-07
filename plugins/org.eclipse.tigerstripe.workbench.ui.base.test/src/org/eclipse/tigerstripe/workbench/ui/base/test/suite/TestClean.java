package org.eclipse.tigerstripe.workbench.ui.base.test.suite;

import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;

public class TestClean extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void testTestClean() throws Exception {
		IUIContext ui = getUI();
		ui.click(new MenuItemLocator("Tigerstripe/Plugins..."));
		ui.wait(new ShellShowingCondition("Deployed Tigerstripe Plugins"));
		ui.click(new TableItemLocator("Drop()"));
		ui.contextClick(new TableItemLocator("Drop()"), "Un-deploy");
		ui.wait(new ShellShowingCondition("Un-Deploy Tigerstripe Plugin?"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Un-Deploy Tigerstripe Plugin?"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Deployed Tigerstripe Plugins"));
	}

}