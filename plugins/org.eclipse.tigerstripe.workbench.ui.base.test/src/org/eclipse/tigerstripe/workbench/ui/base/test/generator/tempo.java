package org.eclipse.tigerstripe.workbench.ui.base.test.generator;

import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.CoolBar;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;

public class tempo extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void testtempo() throws Exception {
		IUIContext ui = getUI();
		ui.click(new SWTWidgetLocator(ToolItem.class, "", 2,
				new SWTWidgetLocator(ToolBar.class, 1, new SWTWidgetLocator(
						CoolBar.class))));
		ui.wait(new ShellShowingCondition("Generate Tigerstripe Project"));
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellShowingCondition("Generate Result"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Generate Result"));
		ui.wait(new ShellDisposedCondition("Generate Tigerstripe Project"));
	}

}