package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import junit.framework.Assert;

import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class DeleteProject extends UITestCaseSWT {


	/**
	 * Main test method.
	 */
	public void testDeleteProject() throws Exception {
		IUIContext ui = getUI();
	
		ui.contextClick(
						new TreeItemLocator(
								TestingConstants.NEW_MODEL_PROJECT_NAME,
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
						"Delete");
		
		ui.wait(new ShellShowingCondition("Delete Resources"));
		ui.click(new ButtonLocator(
				"&Delete project contents on disk (cannot be undone)"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Delete Resources"));
		
		try {
			ui
			.click(new TreeItemLocator(
					TestingConstants.NEW_MODEL_PROJECT_NAME,
					new ViewLocator(
							"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
			fail("Project was not deleted");
		} catch (Exception e){
			// We want this as its should not be there any more!

		}
	}

}