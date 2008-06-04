package org.eclipse.tigerstripe.workbench.ui.base.test.generator;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.FileUtils;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
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
		
		// Save the Project
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		// Add the template now, so that the rule can see it.
		ui.contextClick(
				new TreeItemLocator(
						TestingConstants.NEW_PLUGIN_PROJECT_NAME+"/templates",
						new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
				"New/New File");
		ui.wait(new ShellShowingCondition("New File"));
		ui.click(new LabeledTextLocator("File na&me:"));
		ui.enterText(TestingConstants.GLOBAL_RULE_TEMPLATE_NAME);
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellDisposedCondition("New File"));
	
		
		// Open the rules section of the editor
		ui.click(new CTabItemLocator(TestingConstants.NEW_PLUGIN_PROJECT_NAME+"/ts-plugin.xml"));
		ui.click(new CTabItemLocator("Rules"));
		ui.click(new SWTWidgetLocator(Label.class, "&Global Rules"));
		
		ui.click(new ButtonLocator("Add", new SectionLocator("&Global Rules")));
		ui.wait(new ShellShowingCondition("New Plugin Rule"));
		LabeledTextLocator ruleName = new LabeledTextLocator("Rule Name:");
		GuiUtils.clearText(ui, ruleName);
		ui.click(ruleName);
		ui.enterText(TestingConstants.NEW_GLOBAL_RULE_NAME);
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("New Plugin Rule"));
		
		// Create the template file so we can browse to it
		/*
		 * We need to add the template into the plugin templates dir.
		 * 
		 * Send output to the template file
		 * 
		 */
		FileUtils.appendResourceToWorkspace(
				"templates/"+TestingConstants.GLOBAL_RULE_TEMPLATE_NAME,
				TestingConstants.NEW_PLUGIN_PROJECT_NAME+"/templates",
				TestingConstants.GLOBAL_RULE_TEMPLATE_NAME);
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		root.refreshLocal(0, new NullProgressMonitor());
		
		// Set the template
		/**
		 * This doesn't work by direct typing.
		 * Need to use the browse.
		 */
		//ui.click(new LabeledTextLocator("Template:"));
		//ui.enterText(TestingConstants.TEMPLATE_NAME);
		
		ui.click(new LabeledLocator(Button.class, "Template:"));
		ui.wait(new ShellShowingCondition("Select Velocity Template"));
		ui.click(new TreeItemLocator(TestingConstants.GLOBAL_RULE_TEMPLATE_NAME));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Select Velocity Template"));
		
		// Set the output file
		ui.click(new LabeledTextLocator("Output File:"));
		ui.enterText(TestingConstants.GLOBAL_RULE_OUTPUT_FILE_NAME);
		
		
		// Close this section
		ui.click(new SWTWidgetLocator(Label.class, "&Global Rules"));
		
		
		
	}

}