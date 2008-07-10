package org.eclipse.tigerstripe.ui.visualeditor.test.project;

import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.PullDownMenuItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class CreatePackage extends UITestCaseSWT {


	/**
	 * Main test method.
	 * 
	 * We need to do this to have a placeholder for our diagram.
	 * The base test doesn't use this as the wizards create the packages for us
	 * 
	 */
	public void testCreatePackage() throws Exception {
		IUIContext ui = getUI();
//		ui.contextClick(
//						new TreeItemLocator(
//								TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/",
//								1,
//								new ViewLocator(
//										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
//						"New/Package");
//		ui.wait(new ShellShowingCondition("New Java Package"));
//		ui.enterText(TestingConstants.DEFAULT_ARTIFACT_PACKAGE);
//		ui.click(new ButtonLocator("&Finish"));
//		ui.wait(new ShellDisposedCondition("New Java Package"));
		
		ui.click(new TreeItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME,
				new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui.click(new PullDownMenuItemLocator("Package",
				new ContributedToolItemLocator(
						"org.eclipse.tigerstripe.eclipse.newArtifactAction")));
		ui.wait(new ShellShowingCondition("New Package Artifact"));
		ui.click(new LabeledTextLocator("Name:"));
		ui.enterText(TestingConstants.DIAGRAM_PACKAGE);
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("New Package Artifact"));
		ui.close(new CTabItemLocator(TestingConstants.DIAGRAM_PACKAGE));
		
		
		
	}

}