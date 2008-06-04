package org.eclipse.tigerstripe.workbench.ui.base.test.generator;

import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.SectionLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class Generate extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void testGenerate() throws Exception {
		IUIContext ui = getUI();
		ui.click(2, new TreeItemLocator(
						TestingConstants.NEW_PROJECT_NAME+"/tigerstripe.xml",
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui.click(new CTabItemLocator("Plugin Settings"));
		
		SWTWidgetLocator pluginLocator = new SWTWidgetLocator(Label.class,
				TestingConstants.NEW_PLUGIN_PROJECT_NAME+
				"("+TestingConstants.NEW_PLUGIN_PROJECT_VERSION +
				") (disabled) [Generic]");
		ui.click(pluginLocator);
		ui.click(new ButtonLocator("Enable", new SectionLocator(
				TestingConstants.NEW_PLUGIN_PROJECT_NAME+
				"("+TestingConstants.NEW_PLUGIN_PROJECT_VERSION +
				") (disabled) [Generic]")));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		ui.click(new TreeItemLocator(
				TestingConstants.NEW_PROJECT_NAME,
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui.click(new ContributedToolItemLocator(
				"org.eclipse.tigerstripe.eclipse.generateAction"));
		ui.wait(new ShellShowingCondition("Generate Tigerstripe Project"));
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellShowingCondition("Generate Result"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Generate Result"));
		ui.wait(new ShellDisposedCondition("Generate Tigerstripe Project"));
		ui.click(new TreeItemLocator(
				TestingConstants.NEW_PROJECT_NAME+"/target/tigerstripe.gen/allArtifacts.txt",
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		
		// Need to check contents!

	}

}