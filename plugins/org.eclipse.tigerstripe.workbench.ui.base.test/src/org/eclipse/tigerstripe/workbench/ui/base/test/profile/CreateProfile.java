package org.eclipse.tigerstripe.workbench.ui.base.test.profile;

import org.eclipse.swt.SWT;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WT;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;
public class CreateProfile extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void testCreateProfile() throws Exception {
		IUIContext ui = getUI();
		ui
		.click(new TreeItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME,
				new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui
		.contextClick(
				new TreeItemLocator(
						TestingConstants.NEW_MODEL_PROJECT_NAME,
						new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
		"New/Other...");

		// Create the profile itself

		ui.wait(new ShellShowingCondition("New"));
		ui.click(1, new TreeItemLocator("Java"), WT.SHIFT);
		ui.click(1, new TreeItemLocator("Tigerstripe/Workbench Profile"),
				WT.SHIFT);
		ui.click(new ButtonLocator("&Next >"));
		
		// Make sure the parent is selected.
		ui.click(new TreeItemLocator(TestingConstants.NEW_MODEL_PROJECT_NAME));
		
		LabeledTextLocator profileName = new LabeledTextLocator("File na&me:");
		ui.click(profileName);
		ui.keyClick(SWT.CTRL, 'a');
		ui.enterText(TestingConstants.NEW_PROFILE_NAME+".wbp");
		
		ui.click(new ButtonLocator("&Finish"));
		
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellDisposedCondition(
						"New Tigerstripe Workbench Profile"));
		
		CTabItemLocator profileEditor = new CTabItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME+"/"+
				TestingConstants.NEW_PROFILE_NAME+
				".wbp");
		try {
			ui.click(profileEditor);
		} catch (Exception e) {
			fail("Profile editor did not appear");
		}
		// Set basic profile information
		LabeledTextLocator version = new LabeledTextLocator("Version: ");
		ui.click(version);
		ui.keyClick(SWT.CTRL, 'a');
		ui.enterText(TestingConstants.NEW_PROFILE_VERSION);
		
		LabeledTextLocator description = new LabeledTextLocator("Description: ");
		ui.click(description);
		ui.keyClick(SWT.CTRL, 'a');
		ui.enterText(TestingConstants.NEW_PROFILE_DESCRIPTION);
		
		// create primitive type
		ui.click(new CTabItemLocator("Primitive Types"));
		ui.click(new ButtonLocator("Add"));
		LabeledTextLocator typeName = new LabeledTextLocator("Name: ");
		ui.click(typeName);
		ui.keyClick(SWT.CTRL, 'a');
		ui.enterText(TestingConstants.NEW_PRIMITIVE_TYPE_NAME);
	    
		
	}

}