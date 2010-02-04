package org.eclipse.tigerstripe.workbench.ui.generator.test.generator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.ui.forms.widgets.Section;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CComboItemLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.NamedWidgetLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.SectionLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;

public class SelectionUtils {

	public static void globalProperty(IUIContext ui, String propertyName, String defaultValue) throws Exception {
		ui.click(new CTabItemLocator("Properties"));
		//open section
		ui.click(new SWTWidgetLocator(Label.class, "&Global Properties"));
		ui.click(new ButtonLocator("Add"));
		ui.wait(new ShellShowingCondition("New Plugin Property"));
		ui.click(new LabeledTextLocator("Property Name:"));
		ui.keyClick(SWT.CTRL, 'a');
		ui.enterText(propertyName);
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("New Plugin Property"));
		ui.click(new LabeledTextLocator("Default Value:"));
		ui.enterText(defaultValue);
		
		//collapse again
		ui.click(new SWTWidgetLocator(Label.class, "&Global Properties"));
		
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
	}

	
	public static void globalRule(IUIContext ui, String ruleName, String dirName) throws Exception {
	
		ui.click(new CTabItemLocator("Rules"));
		//open section
		ui.click(new SWTWidgetLocator(Label.class, "&Global Rules"));
		ui.click(new ButtonLocator("Add", new SectionLocator("&Global Rules")));
		ui.wait(new ShellShowingCondition("New Plugin Rule"));
		LabeledTextLocator ruleNameLocator = new LabeledTextLocator("Rule Name:");
		ui.click(ruleNameLocator);
		ui.keyClick(SWT.CTRL, 'a');
		ui.enterText(ruleName);
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("New Plugin Rule"));
		
		/// Need to set template 
		ui.click(new NamedWidgetLocator("Browse_Template"));
		ui.wait(new ShellShowingCondition("Select Velocity Template"));
		ui.click(new TreeItemLocator(TestingConstants.GLOBAL_RULE_TEMPLATE_NAME));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Select Velocity Template"));

		// and output dir
		ui.click(new LabeledTextLocator("Output File:"));
		ui.enterText(dirName+"/"+TestingConstants.GLOBAL_RULE_OUTPUT_FILE_NAME);
		
		//collapse again
		ui.click(new SWTWidgetLocator(Label.class, "&Global Rules"));
		
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
	}
	
	public static void artifactRule(IUIContext ui, String ruleName, String dirName) throws Exception {

		ui.click(new CTabItemLocator("Rules"));
		//open section
		ui.click(new SWTWidgetLocator(Label.class, "&Artifact Rules"));
		ui.click(new ButtonLocator("Add", new SectionLocator("&Artifact Rules")));

		ui.wait(new ShellShowingCondition("New Plugin Rule"));
		LabeledTextLocator ruleNameLocator = new LabeledTextLocator("Rule Name:");
		ui.click(ruleNameLocator);
		ui.keyClick(SWT.CTRL, 'a');
		ui.enterText(ruleName);
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("New Plugin Rule"));

		/// Need to set template 
		ui.click(new NamedWidgetLocator("Browse_Template"));
		ui.wait(new ShellShowingCondition("Select Velocity Template"));
		ui.click(new TreeItemLocator(TestingConstants.GLOBAL_RULE_TEMPLATE_NAME));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Select Velocity Template"));

		// and output dir
		ui.click(new LabeledTextLocator("Output File:"));
		ui.enterText(dirName+"/"+TestingConstants.ARTIFACT_RULE_OUTPUT_FILE_NAME);
		
		// and default ArtifactType
		ui.click(new CComboItemLocator("Any Artifact"));
		
		//collapse again
		ui.click(new SWTWidgetLocator(Label.class, "&Artifact Rules"));

		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
	}

	public static void toggleEnableArtifactRule(IUIContext ui, String ruleName) throws Exception {

		ui.click(new CTabItemLocator("Rules"));
		//open section
		ui.click(new SWTWidgetLocator(Label.class, "&Artifact Rules"));
		ui.click(new TableItemLocator(ruleName));
		ui.click(new ButtonLocator("Enabled"));
		//collapse again
		ui.click(new SWTWidgetLocator(Label.class, "&Artifact Rules"));

		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
	}
	
	public static void selectArtifactRuleArtifactType(IUIContext ui, String ruleName, String type) throws Exception {

		ui.click(new CTabItemLocator("Rules"));
		//open section
		ui.click(new SWTWidgetLocator(Label.class, "&Artifact Rules"));
		ui.click(new TableItemLocator(ruleName));
		ui.click(new CComboItemLocator(type));
		//collapse again
		ui.click(new SWTWidgetLocator(Label.class, "&Artifact Rules"));

		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
	}
	
	
	public static void selectArtifactRuleWrapper(IUIContext ui, String ruleName, String wrapperName, String wrapperType) throws Exception {

		ui.click(new CTabItemLocator("Rules"));
		//open section
		ui.click(new SWTWidgetLocator(Label.class, "&Artifact Rules"));
		ui.click(new TableItemLocator(ruleName));
		IWidgetLocator wrapperNameLocator = new LabeledTextLocator("Wrapper Class Name:");
		ui.click(wrapperNameLocator);
		ui.keyClick(SWT.CTRL, 'a');
		ui.enterText(wrapperName);
		

		ui.click(new NamedWidgetLocator("Browse_Classes"));
		ui.wait(new ShellShowingCondition("Wrapper Class Selection"));
		ui.click(new SWTWidgetLocator(Text.class));
		ui.enterText(wrapperType);
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Wrapper Class Selection"));
		
		//collapse again
		ui.click(new SWTWidgetLocator(Label.class, "&Artifact Rules"));

		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
	}
	
	
	public static void toggleEnableGlobalRule(IUIContext ui, String ruleName) throws Exception {

		ui.click(new CTabItemLocator("Rules"));
		//open section
		ui.click(new SWTWidgetLocator(Label.class, "&Global Rules"));
		ui.click(new TableItemLocator(ruleName));
		ui.click(new ButtonLocator("Enabled"));
		//collapse again
		ui.click(new SWTWidgetLocator(Label.class, "&Global Rules"));

		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
	}
		
	public static void toggleEnableLogging(IUIContext ui) throws Exception {
		ui.click(new CTabItemLocator("Runtime"));
		ui.click(new ButtonLocator("Enable logging for this plugin"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
	}

}
