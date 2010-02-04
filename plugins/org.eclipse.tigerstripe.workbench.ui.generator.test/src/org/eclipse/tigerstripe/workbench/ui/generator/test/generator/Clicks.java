package org.eclipse.tigerstripe.workbench.ui.generator.test.generator;

import java.util.Arrays;

import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.ProjectHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.TestInitialPackageContents;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetNotFoundException;
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
		clearTarget();
		SelectionUtils.globalRule(ui, "secondGlobal", "${ppProp.global}");
		// generate
		runGenerate();
		// checkOutput
		// Need to look for original file
		ui.click(new TreeItemLocator(
				projectName+"/target/tigerstripe.gen/"+TestingConstants.GLOBAL_RULE_OUTPUT_FILE_NAME,
				new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		// Need to look for file in the "global" directory
		ui.click(new TreeItemLocator(
				projectName+"/target/tigerstripe.gen/global/"+TestingConstants.GLOBAL_RULE_OUTPUT_FILE_NAME,
				new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		clearTarget();
	}

	public void testArtifactRule() throws Exception{
		clearTarget();
		SelectionUtils.artifactRule(ui, "secondArtifact", "${ppProp.artifact}");
		// generate
		runGenerate();
		// checkOutput
		for (String artifact : TestInitialPackageContents.artifacts){
			ui.click(new TreeItemLocator(
					projectName+"/target/tigerstripe.gen/"+artifact+".txt",
					new ViewLocator(
					"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
			ui.click(new TreeItemLocator(
					projectName+"/target/tigerstripe.gen/artifact/"+artifact+".txt",
					new ViewLocator(
					"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		}
		clearTarget();

	}

	public void testEnableArtifactRule() throws Exception{
		clearTarget();
		// Have to turn off the original because we are of certain of the order of tests
		SelectionUtils.toggleEnableArtifactRule(ui,TestingConstants.NEW_ARTIFACT_RULE_NAME );
		// generate
		runGenerate();
		// checkOutput
		for (String artifact : TestInitialPackageContents.artifacts){
			TreeItemLocator origArtFile = new TreeItemLocator(
					projectName+"/target/tigerstripe.gen/"+artifact+".txt",
					new ViewLocator(
					"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
			try { 
				ui.click(origArtFile); 
				fail("Found Unexpected File! "+projectName+"/target/tigerstripe.gen/"+artifact+".txt");
			} catch (WidgetNotFoundException e){
				// This is the desired behaviour
			}
			
		}
		clearTarget();
		// Turn back on
		SelectionUtils.toggleEnableArtifactRule(ui,TestingConstants.NEW_ARTIFACT_RULE_NAME );
	}

	public void testSelectArtifactRuleType() throws Exception{
		clearTarget();
		SelectionUtils.selectArtifactRuleArtifactType(ui, TestingConstants.NEW_ARTIFACT_RULE_NAME, "Entity");
		
		// generate
		runGenerate();
		// checkOutput
		String[] entities = new String[]{"Ent1","Ent2"};
		for (String artifact : TestInitialPackageContents.artifacts){
			TreeItemLocator origArtFile = new TreeItemLocator(
					projectName+"/target/tigerstripe.gen/"+artifact+".txt",
					new ViewLocator(
					"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
			TreeItemLocator newArtFile = new TreeItemLocator(
					projectName+"/target/tigerstripe.gen/artifact/"+artifact+".txt",
					new ViewLocator(
					"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
			if (Arrays.asList(entities).contains(artifact)){
				try {
					ui.click(origArtFile);
				} catch (WidgetNotFoundException e){
					fail("File not found "+projectName+"/target/tigerstripe.gen/"+artifact+".txt");
				}
				
			} else {
				try {
					ui.click(origArtFile);
					fail("Unexpected file found");
				} catch (WidgetNotFoundException e){
					// This is the desired behaviour
				}
			}
			// Other rule should be unaffected
			try {
				ui.click(newArtFile);
			} catch (WidgetNotFoundException e){
				fail("File not found "+projectName+"/target/tigerstripe.gen/artifact/"+artifact+".txt");
			}
			
			
		}
		clearTarget();
	}


	public void testEnableGlobalRule() throws Exception{
		clearTarget();
		// Have to turn off the original because we are of certain of the order of tests
		SelectionUtils.toggleEnableGlobalRule(ui,TestingConstants.NEW_GLOBAL_RULE_NAME );
		// generate
		runGenerate();
		// checkOutput
		TreeItemLocator origFile = new TreeItemLocator(
				projectName+"/target/tigerstripe.gen/"+TestingConstants.GLOBAL_RULE_OUTPUT_FILE_NAME,
				new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
		try {
			ui.click(origFile);
			fail("Found Unexpected File! "+TestingConstants.GLOBAL_RULE_OUTPUT_FILE_NAME);
		}catch (Exception e){
			// This is what we want
		}
		
		clearTarget();
		// Turn back on
		SelectionUtils.toggleEnableGlobalRule(ui,TestingConstants.NEW_GLOBAL_RULE_NAME );
	}

	public void testEnableLogging() throws Exception{
		clearTarget();
		SelectionUtils.toggleEnableLogging(ui);
		// generate
		runGenerate();
		// checkOutput
		ui.click(new TreeItemLocator(
				projectName+"/target/tigerstripe.gen/"+TestingConstants.NEW_PLUGIN_PROJECT_NAME+".log",
				new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		SelectionUtils.toggleEnableLogging(ui);
	}
	
	public void runGenerate() throws Exception {
		deployPlugin();
		ui.click(new TreeItemLocator(
				projectName,
				new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui.click(new ContributedToolItemLocator(
		"org.eclipse.tigerstripe.workbench.ui.toolbar.generate"));


		ui.wait(new ShellShowingCondition("Generate Tigerstripe Project"));
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellShowingCondition("Generate Result"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Generate Result"));
		ui.wait(new ShellDisposedCondition("Generate Tigerstripe Project"));
	}

	
	private void clearTarget() throws Exception{
		
		try {
			ui
			.contextClick(
					new TreeItemLocator(
							projectName+"/target/tigerstripe.gen",
							new ViewLocator(
							"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
			"Delete");
			ui.wait(new ShellDisposedCondition("Progress Information"));
			ui.wait(new ShellShowingCondition("Confirm Delete"));
			ui.click(new ButtonLocator("OK"));
			ui.wait(new ShellDisposedCondition("Confirm Delete"));
		} catch (Exception e){
			// DON'T CARE - probably the first generate that has been run
			//e.printStackTrace();
		}
		
	}
	
	private void deployPlugin() throws Exception {
		TreeItemLocator treeItem = new TreeItemLocator(
				TestingConstants.NEW_PLUGIN_PROJECT_NAME+"/ts-plugin.xml",
				new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
		
		ui.contextClick( treeItem,	"Plugin/Deploy");
		
		ui.wait(new ShellShowingCondition("Deploy new plugin"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Deploy new plugin"));
		ui.wait(new ShellShowingCondition(TestingConstants.NEW_PLUGIN_PROJECT_NAME+" Plugin"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition(TestingConstants.NEW_PLUGIN_PROJECT_NAME+" Plugin"));
	}
	
}