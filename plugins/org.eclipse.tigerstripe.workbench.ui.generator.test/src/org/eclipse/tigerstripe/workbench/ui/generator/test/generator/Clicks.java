package org.eclipse.tigerstripe.workbench.ui.generator.test.generator;

import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.ProjectHelper;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.SectionLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class Clicks extends UITestCaseSWT {

	private IUIContext ui;
	private ProjectHelper helper;

	private static String projectName  = "model-refactoring";
	/**
	 * Main test method.
	 */
	public void setUp() throws Exception {
		ui = getUI();
		
		ui
		.click(
				2,
				new TreeItemLocator(
						TestingConstants.NEW_PLUGIN_PROJECT_NAME+"/ts-plugin.xml",
						new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));

		
	}

	
	public void testGlobalRule() throws Exception{
		SelectionUtils.globalRule(ui, "secondGlobal", "${ppProp.global}");
	}

	public void testArtifactRule() throws Exception{
		SelectionUtils.artifactRule(ui, "secondArtifact", "defaultValue");

	}

	public void testEnableArtifactRule() throws Exception{
		SelectionUtils.toggleEnableArtifactRule(ui,"secondArtifact" );
	}

	public void testSelectArtifactRuleType() throws Exception{
		SelectionUtils.selectArtifactRuleArtifactType(ui, "secondArtifact", "Entity");
	}


	public void testEnableGlobalRule() throws Exception{
		SelectionUtils.toggleEnableGlobalRule(ui,"secondGlobal" );

	}

	public void testEnableLogging() throws Exception{
		SelectionUtils.toggleEnableLogging(ui);
	}
	
	
	
}