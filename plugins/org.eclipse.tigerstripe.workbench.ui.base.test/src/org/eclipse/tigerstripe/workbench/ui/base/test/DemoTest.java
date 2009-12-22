package org.eclipse.tigerstripe.workbench.ui.base.test;

import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.eclipse.PullDownMenuItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.FilteredTreeItemLocator;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;

public class DemoTest extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void testDemo() throws Exception {
		IUIContext ui = getUI();
		ui.click(new PullDownMenuItemLocator("Project...",
				new ContributedToolItemLocator("newWizardDropDown")));
		ui.wait(new ShellShowingCondition("New Project"));
		ui.click(new FilteredTreeItemLocator("Java Project"));
		ui.click(new ButtonLocator("&Next >"));
		ui.click(new LabeledTextLocator("&Project name:"));
		ui.enterText("Pete");
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellShowingCondition("Open Associated Perspective?"));
		ui.click(new ButtonLocator("&Yes"));
		ui.wait(new ShellDisposedCondition("Open Associated Perspective?"));
		ui.wait(new ShellDisposedCondition("New Java Project"));
	}

}