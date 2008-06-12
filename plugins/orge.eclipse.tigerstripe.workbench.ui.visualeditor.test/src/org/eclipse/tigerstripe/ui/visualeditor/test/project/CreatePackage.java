package org.eclipse.tigerstripe.ui.visualeditor.test.project;

import org.eclipse.tigerstripe.ui.visualeditor.test.suite.DiagramConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
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
		ui.contextClick(
						new TreeItemLocator(
								TestingConstants.NEW_PROJECT_NAME+"/src/",
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
						"New/Package");
		ui.wait(new ShellShowingCondition("New Java Package"));
		ui.enterText(TestingConstants.DEFAULT_ARTIFACT_PACKAGE);
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("New Java Package"));
	}

}