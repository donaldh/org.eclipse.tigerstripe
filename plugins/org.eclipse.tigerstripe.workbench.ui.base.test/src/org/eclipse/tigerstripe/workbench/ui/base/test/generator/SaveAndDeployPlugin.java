package org.eclipse.tigerstripe.workbench.ui.base.test.generator;

import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class SaveAndDeployPlugin extends UITestCaseSWT {

	public void testSaveAndDeployPlugin() throws Exception {
		IUIContext ui = getUI();
		
		// Save the Project
		ui.click(new CTabItemLocator(".*"+TestingConstants.NEW_PLUGIN_PROJECT_NAME+"/ts-plugin.xml"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
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
		
		
		// See if its in the list of deployed plugins..
		ui.click(new MenuItemLocator("Tigerstripe/Plugins..."));
		ui.wait(new ShellShowingCondition("Deployed Tigerstripe Plugins"));
		ui.click(new TableItemLocator(TestingConstants.NEW_PLUGIN_PROJECT_NAME+"("+TestingConstants.NEW_PLUGIN_PROJECT_VERSION+")"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Deployed Tigerstripe Plugins"));
		
		
		// Close the editor
		ui.close(
				new CTabItemLocator(TestingConstants.NEW_PLUGIN_PROJECT_NAME+"/ts-plugin.xml"));
	}

}
