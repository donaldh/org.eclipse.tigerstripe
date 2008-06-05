package org.eclipse.tigerstripe.workbench.ui.base.test.generator;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.FileUtils;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.SectionLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

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
		GuiUtils.clearText(ui, version);
		ui.click(version);
		ui.enterText(TestingConstants.NEW_PLUGIN_PROJECT_VERSION);
		
		LabeledTextLocator description = new LabeledTextLocator("Description: ");
		GuiUtils.clearText(ui,description);
		ui.click(description);
		ui.enterText(TestingConstants.NEW_PLUGIN_PROJECT_DESCRIPTION);
		
		// Save
		ui.click(new CTabItemLocator(".*"+TestingConstants.NEW_PLUGIN_PROJECT_NAME+"/ts-plugin.xml"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		
	}

}