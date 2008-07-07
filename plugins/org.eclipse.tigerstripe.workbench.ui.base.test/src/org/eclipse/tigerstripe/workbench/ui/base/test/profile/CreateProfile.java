package org.eclipse.tigerstripe.workbench.ui.base.test.profile;

import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

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
		GuiUtils.clearText(ui, profileName);
		ui.click(profileName);
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
		GuiUtils.clearText(ui, version);
		ui.click(version);
		ui.enterText(TestingConstants.NEW_PROFILE_VERSION);
		
		LabeledTextLocator description = new LabeledTextLocator("Description: ");
		GuiUtils.clearText(ui,description);
		ui.click(description);
		ui.enterText(TestingConstants.NEW_PROFILE_DESCRIPTION);
		
		// create primitive type
		ui.click(new CTabItemLocator("Primitive Types"));
		ui.click(new ButtonLocator("Add"));
		LabeledTextLocator typeName = new LabeledTextLocator("Name: ");
		GuiUtils.clearText(ui, typeName);
		ui.click(typeName);
		ui.enterText(TestingConstants.NEW_PRIMITIVE_TYPE_NAME);
		
		// create some stereotypes
		ui.click(new CTabItemLocator("Stereotypes"));
		com.windowtester.runtime.locator.IWidgetLocator[] allAdds = ui.findAll(new ButtonLocator("Add"));
		ui.click(allAdds[0]);
		
		// There's another one now!
        allAdds = ui.findAll(new ButtonLocator("Add"));
		ui.click(allAdds[0]); // This one works
		ui.click(allAdds[1]); // This one seems to be out of view!
		
		/*ButtonLocator addAnnotationButton = new ButtonLocator("Add");
		ui.click(addAnnotationButton);
		// Artifact level Annotation
		// TODO Making this apply to everything!
		LabeledTextLocator annotationName = new LabeledTextLocator("Name: ");
		GuiUtils.clearText(ui, annotationName);
		ui.click(annotationName);
		ui.enterText("method");
		
		ui.click(new TableItemLocator("", 0, new SWTWidgetLocator(
				Table.class)));
		// TODO These are out of scrollable view!
		ui.click(new TableItemLocator("", 1, new SWTWidgetLocator(
				Table.class)));
		ui.click(new TableItemLocator("", 2, new SWTWidgetLocator(
				Table.class)));
		ui.click(new TableItemLocator("", 3, new SWTWidgetLocator(
				Table.class)));
		ui.click(new TableItemLocator("", 4, new SWTWidgetLocator(
				Table.class)));
		ui.click(new TableItemLocator("", 5, new SWTWidgetLocator(
				Table.class)));
		ui.click(new TableItemLocator("", 6, new SWTWidgetLocator(
				Table.class)));
		ui.click(new TableItemLocator("", 7, new SWTWidgetLocator(
				Table.class)));
		ui.click(new TableItemLocator("", 8, new SWTWidgetLocator(
				Table.class)));
		ui.click(new TableItemLocator("", 9, new SWTWidgetLocator(
				Table.class)));
		ui.click(new TableItemLocator("", 10, new SWTWidgetLocator(
				Table.class)));
		ui.click(new TableItemLocator("", 11, new SWTWidgetLocator(
				Table.class)));
		
		ui.click(new ButtonLocator("Method"));
		ui.click(new ButtonLocator("Attribute"));
		ui.click(new ButtonLocator("Literal"));
		ui.click(new ButtonLocator("Argument"));*/
	    
		
	}

}