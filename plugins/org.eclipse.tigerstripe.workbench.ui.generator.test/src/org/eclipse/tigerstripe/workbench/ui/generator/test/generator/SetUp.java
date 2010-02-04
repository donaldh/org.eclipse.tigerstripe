package org.eclipse.tigerstripe.workbench.ui.generator.test.generator;

import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.ProjectHelper;

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

public class SetUp extends UITestCaseSWT {

	private IUIContext ui;
	private ProjectHelper helper;

	private static String projectName  = "model-refactoring";
	/**
	 * Main test method.
	 */
	public void testSetUp() throws Exception {
		ui = getUI();
		helper = new ProjectHelper();
		helper.loadProjectFromCVS(ui);
		
		// In this test case we don't want any diagrams.....
		// There are 4 diagrams
		// helper.deleteDiagrams(ui);
		
		ui
		.click(
				2,
				new TreeItemLocator(
						TestingConstants.NEW_PLUGIN_PROJECT_NAME+"/ts-plugin.xml",
						new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));

		// Need to ensure this is done before any of the next steps
		setGlobalPropertyGlobal();
		setGlobalPropertyArt();
		deployPlugin();
		// enable
		enablePlugin();
		// close project editor
		ui.close(new CTabItemLocator(projectName+"/tigerstripe.xml"));
		// select plugin editor
		ui.click(new CTabItemLocator(TestingConstants.NEW_PLUGIN_PROJECT_NAME+"/ts-plugin.xml"));
	}

	public void setGlobalPropertyGlobal() throws Exception{
		// The effect will only be tested later...
		SelectionUtils.globalProperty(ui, "global", "global");
	}

	public void setGlobalPropertyArt() throws Exception{
		// The effect will only be tested later...
		SelectionUtils.globalProperty(ui, "artifact", "artifact");
	}

	
	private void deployPlugin() throws Exception {
		TreeItemLocator treeItem = new TreeItemLocator(
				TestingConstants.NEW_PLUGIN_PROJECT_NAME+"/ts-plugin.xml",
				new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
		
		ui.contextClick( treeItem,	"Plugin/Deploy");
		
		ui.wait(new ShellShowingCondition("Deploy new plugin"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Deploy new plugin"));
		ui.wait(new ShellShowingCondition(TestingConstants.NEW_PLUGIN_PROJECT_NAME+" Plugin"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition(TestingConstants.NEW_PLUGIN_PROJECT_NAME+" Plugin"));
	}

	private void enablePlugin() throws Exception {
		ui.click(2, new TreeItemLocator(
				projectName+"/tigerstripe.xml",
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
		
	}
	
}