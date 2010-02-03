package org.eclipse.tigerstripe.workbench.ui.generator.test.generator;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class Clicks extends UITestCaseSWT {

	private IUIContext ui;


	/**
	 * Main test method.
	 */
	public void setUp() throws Exception {
		ui = getUI();
		ui
		.click(
				2,
				new TreeItemLocator(
						"New Plugin Project/ts-plugin.xml",
						new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));


	}

	public void testGlobalPropertyGlobal() throws Exception{
		SelectionUtils.globalProperty(ui, "global", "global");
	}

	public void testGlobalPropertyArt() throws Exception{
		SelectionUtils.globalProperty(ui, "artifact", "artifact");
	}

	public void testGlobalRule() throws Exception{
		SelectionUtils.globalRule(ui, "secondGlobal", "defaultValue");
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