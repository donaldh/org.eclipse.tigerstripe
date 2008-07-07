package org.eclipse.tigerstripe.workbench.ui.base.test.profile;

import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;

public class Del extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void testDel() throws Exception {
		IUIContext ui = getUI();
		ui
				.click(new TreeItemLocator(
						"New Project/target/tigerstripe.gen",
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui
				.contextClick(
						new TreeItemLocator(
								"New Project/target/tigerstripe.gen",
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
						"Delete");
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellShowingCondition("Confirm Delete"));
		ui.click(new ButtonLocator("&Yes"));
		ui.wait(new ShellDisposedCondition("Confirm Delete"));
	}

}