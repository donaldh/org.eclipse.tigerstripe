package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;

public class NewProject extends UITestCaseSWT {

	private IUIContext ui;
	/**
	 * Main test method.
	 */
	public void testNewProject() throws Exception {
		ui = getUI();
		ui.click(new ContributedToolItemLocator(
				"org.eclipse.tigerstripe.ui.eclipse.openNewProjectAction"));
		ui.wait(new ShellShowingCondition("New Tigerstripe Project"));
		ui.click(new LabeledTextLocator("&Project Name:"));
		ui.enterText(TestingConstants.NEW_PROJECT_NAME);
		LabeledTextLocator artifactPackage = new LabeledTextLocator("Artifacts Package:");
		GuiUtils.clearText(ui, artifactPackage);
		ui.click(artifactPackage);
		ui.enterText(TestingConstants.DEFAULT_ARTIFACT_PACKAGE);
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("New Tigerstripe Project"));
		
		// Make sure what we put in the wizard made it to the project 
		CTabItemLocator descriptorEditor = new CTabItemLocator(
				TestingConstants.NEW_PROJECT_NAME+
				"/tigerstripe.xml");
		try {
			ui.click(descriptorEditor);
		} catch (Exception e) {
			fail("Descriptor editor did not appear");
		}
		
		ui.click(new SWTWidgetLocator(Label.class, "Project Defaults"));
		LabeledTextLocator defaultPackage = new LabeledTextLocator("Default Artifact Package: ");
		assertEquals(TestingConstants.DEFAULT_ARTIFACT_PACKAGE, defaultPackage.getText(ui));
			
		// Now set some other project details
		//TODO
		
		
		
	}
	
	
}