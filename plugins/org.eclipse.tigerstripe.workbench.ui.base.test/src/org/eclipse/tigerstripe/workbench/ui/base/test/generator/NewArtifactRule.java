package org.eclipse.tigerstripe.workbench.ui.base.test.generator;

import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CComboItemLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.SectionLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;

public class NewArtifactRule extends UITestCaseSWT {

	
	private RuleAuditHelper helper;
	
	/**
	 * Main test method.
	 */
	public void testNewArtifactRule() throws Exception {
		IUIContext ui = getUI();
		
		
		ui.click(new CTabItemLocator(TestingConstants.NEW_PLUGIN_PROJECT_NAME+"/ts-plugin.xml"));
		ui.click(new CTabItemLocator("Rules"));
		
		ui.click(new SWTWidgetLocator(Label.class, "&Artifact Rules"));
		ui.click(new ButtonLocator("Add", new SectionLocator("&Artifact Rules")));
		
		ui.wait(new ShellShowingCondition("New Plugin Rule"));
		LabeledTextLocator ruleName = new LabeledTextLocator("Rule Name:");
		GuiUtils.clearText(ui, ruleName);
		ui.click(ruleName);
		ui.enterText(TestingConstants.NEW_ARTIFACT_RULE_NAME);
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("New Plugin Rule"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
	}

	// The new rule should throw a few problems until we set some stuff on it.
	public void testProblems() throws Exception {
		IUIContext ui = getUI();
		helper = new RuleAuditHelper(ui);
		ui.click(new CTabItemLocator(
				TestingConstants.NEW_PLUGIN_PROJECT_NAME+"/ts-plugin.xml"));
		// ERRORS
		helper.checkArtifactTypeUndefinedRule(TestingConstants.NEW_ARTIFACT_RULE_NAME, TestingConstants.NEW_PLUGIN_PROJECT_NAME,true);
		helper.checkNoOutputFileRule(TestingConstants.NEW_ARTIFACT_RULE_NAME, TestingConstants.NEW_PLUGIN_PROJECT_NAME,true);
		helper.checkNoTemplateRule(TestingConstants.NEW_ARTIFACT_RULE_NAME, TestingConstants.NEW_PLUGIN_PROJECT_NAME,true);
		
		//INFOS
		helper.checkNoDescriptionRule(TestingConstants.NEW_ARTIFACT_RULE_NAME, TestingConstants.NEW_PLUGIN_PROJECT_NAME,true);
		
	}
	
	public void testSetBasics() throws Exception {
		IUIContext ui = getUI();
		helper = new RuleAuditHelper(ui);
		// Artifact Rule specific checks...
		
		// Set Entity Type
		ui.click(new CComboItemLocator("Entity"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		// Make sure rule has been cleared
		helper.checkArtifactTypeUndefinedRule(TestingConstants.NEW_ARTIFACT_RULE_NAME, TestingConstants.NEW_PLUGIN_PROJECT_NAME,false);
		
		
		// General rule checks
		
		
		
	}
	
}