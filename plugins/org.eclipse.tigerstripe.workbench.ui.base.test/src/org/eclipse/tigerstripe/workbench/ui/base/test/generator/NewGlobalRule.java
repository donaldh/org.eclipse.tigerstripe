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
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.SectionLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class NewGlobalRule extends UITestCaseSWT {

	private RuleAuditHelper helper;
	
	public void testNewGlobalRule() throws Exception {
		IUIContext ui = getUI();
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
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		

	
	// The new rule should throw a few problems until we set some stuff on it.

		helper = new RuleAuditHelper(ui);
		ui.click(new CTabItemLocator(
				".*"+TestingConstants.NEW_PLUGIN_PROJECT_NAME+"/ts-plugin.xml"));
		// ERRORS
		helper.checkNoOutputFileRule(TestingConstants.NEW_GLOBAL_RULE_NAME, TestingConstants.NEW_PLUGIN_PROJECT_NAME,true);
		helper.checkNoTemplateRule(TestingConstants.NEW_GLOBAL_RULE_NAME, TestingConstants.NEW_PLUGIN_PROJECT_NAME,true);
		
		//INFOS
		helper.checkNoDescriptionRule(TestingConstants.NEW_GLOBAL_RULE_NAME, TestingConstants.NEW_PLUGIN_PROJECT_NAME,true);
		

		helper = new RuleAuditHelper(ui);
		// Artifact Rule specific checks...
		
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
		
		
		
		
		// Add some content!
		FileUtils.appendResourceToWorkspace(
				"templates/"+TestingConstants.GLOBAL_RULE_TEMPLATE_NAME,
				TestingConstants.NEW_PLUGIN_PROJECT_NAME+"/templates",
				TestingConstants.GLOBAL_RULE_TEMPLATE_NAME);
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		root.refreshLocal(0, new NullProgressMonitor());
		
		ui.click(
				new XYLocator(new CTabItemLocator(TestingConstants.GLOBAL_RULE_TEMPLATE_NAME),
				104, 12));
		
		// Set the template
		ui.click(new LabeledLocator(Button.class, "Template:"));
		ui.wait(new ShellShowingCondition("Select Velocity Template"));
		ui.click(new TreeItemLocator(TestingConstants.GLOBAL_RULE_TEMPLATE_NAME));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Select Velocity Template"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		// Make sure rule has been cleared
		helper.checkNoTemplateRule(TestingConstants.NEW_ARTIFACT_RULE_NAME, TestingConstants.NEW_PLUGIN_PROJECT_NAME,false);
		
		// Set the output File
		ui.click(new LabeledTextLocator("Output File:"));
		ui.enterText(TestingConstants.GLOBAL_RULE_OUTPUT_FILE_NAME);
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		// Make sure rule has been cleared
		helper.checkNoOutputFileRule(TestingConstants.NEW_ARTIFACT_RULE_NAME, TestingConstants.NEW_PLUGIN_PROJECT_NAME,false);
		
		// Set the description
		ui.click(new LabeledTextLocator("Description: "));
		ui.enterText(TestingConstants.GLOBAL_RULE_DESCRIPTION);
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		// Make sure rule has been cleared
		helper.checkNoDescriptionRule(TestingConstants.NEW_ARTIFACT_RULE_NAME, TestingConstants.NEW_PLUGIN_PROJECT_NAME,false);
		
		// Close this section
		ui.click(new SWTWidgetLocator(Label.class, "&Global Rules"));
		// Save
		ui.click(new CTabItemLocator(".*"+TestingConstants.NEW_PLUGIN_PROJECT_NAME+"/ts-plugin.xml"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
	}
	
}
