package org.eclipse.tigerstripe.workbench.ui.base.test.generator;

import org.eclipse.swt.SWT;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;

public class NewPluginProject extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void testNewPluginProject() throws Exception {
		
		IUIContext ui = getUI();
		ui.click(new MenuItemLocator("File/New/Other..."));
		ui.wait(new ShellShowingCondition("New"));
		ui.click(new TreeItemLocator("Tigerstripe/Tigerstripe Plugin Project"));
		ui.click(new ButtonLocator("&Next >"));
		ui.enterText(TestingConstants.NEW_PLUGIN_PROJECT_NAME);
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition(
				"New Tigerstripe M1-Level Generation Plugin Project"));
		
		
		CTabItemLocator descriptorEditor = new CTabItemLocator(
				TestingConstants.NEW_PLUGIN_PROJECT_NAME+
				"/ts-plugin.xml");
		
		try {
			ui.click(descriptorEditor);
		} catch (Exception e) {
			fail("Descriptor editor did not appear");
		}
		
		LabeledTextLocator version = new LabeledTextLocator("Version: ");
		ui.click(version);
		ui.keyClick(SWT.CTRL, 'a');
		ui.enterText(TestingConstants.NEW_PLUGIN_PROJECT_VERSION);
		
		LabeledTextLocator description = new LabeledTextLocator("Description: ");
		ui.click(description);
		ui.keyClick(SWT.CTRL, 'a');
		ui.enterText(TestingConstants.NEW_PLUGIN_PROJECT_DESCRIPTION);
		
		// Save
		ui.click(new CTabItemLocator(".*"+TestingConstants.NEW_PLUGIN_PROJECT_NAME+"/ts-plugin.xml"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		
	}

}