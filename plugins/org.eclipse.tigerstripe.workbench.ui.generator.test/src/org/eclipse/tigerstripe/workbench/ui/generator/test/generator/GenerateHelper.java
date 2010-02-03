package org.eclipse.tigerstripe.workbench.ui.generator.test.generator;

import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ProjectRecord;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

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

public class GenerateHelper extends UITestCaseSWT {

	IUIContext ui;
	
	public GenerateHelper(IUIContext ui) {
		super();
		this.ui = ui;
	}

	public void enablePlugin() throws Exception {
//		ui.click(2, new TreeItemLocator(
//				TestingConstants.NEW_MODEL_PROJECT_NAME+"/tigerstripe.xml",
//				new ViewLocator(
//				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui.click(new CTabItemLocator("Plugin Settings"));

		SWTWidgetLocator pluginLocator = new SWTWidgetLocator(Label.class,
				TestingConstants.NEW_PLUGIN_PROJECT_NAME+
				"("+TestingConstants.NEW_PLUGIN_PROJECT_VERSION +
		") (disabled) [Generic]");
		ui.click(pluginLocator);
		ui.click(new ButtonLocator("Enable", new SectionLocator(
				TestingConstants.NEW_PLUGIN_PROJECT_NAME+
				"("+TestingConstants.NEW_PLUGIN_PROJECT_VERSION +
		") (disabled) [Generic]")));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
	}
	public void runGenerate() throws Exception {
		
		ui.click(new TreeItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME,
				new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui.click(new ContributedToolItemLocator(
		"org.eclipse.tigerstripe.workbench.ui.toolbar.generate"));
		
		// Add a check for the "Project Contains Errors" dialog
//		try {
//			ui.wait(new ShellShowingCondition("Generate Tigerstripe Project - Errors"));
//			// This might not show up..
//			// If it does proceed anyway
//			ui.click(new ButtonLocator("Yes"));
//			ui.wait(new ShellDisposedCondition("Generate Tigerstripe Project - Errors"));
//			
//		} catch (WaitTimedOutException e){
//			// The project was good, so go ahead!
//		}
//		
		
		ui.wait(new ShellShowingCondition("Generate Tigerstripe Project"));
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellShowingCondition("Generate Result"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Generate Result"));
		ui.wait(new ShellDisposedCondition("Generate Tigerstripe Project"));
	}

	public void checkGlobal() throws Exception {
//		OUTPUT FOR GLOBAL RUlE

		ui.click(new TreeItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME+"/target/tigerstripe.gen/"+TestingConstants.GLOBAL_RULE_OUTPUT_FILE_NAME,
				new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));

		// Need to check contents!
	}
	
	public void checkArtifact() throws Exception {
		// OUTPUT FOR Artifact Rule
		for (String artifact : ProjectRecord.getArtifactList().keySet()){
			ui.click(new TreeItemLocator(
					TestingConstants.NEW_MODEL_PROJECT_NAME+"/target/tigerstripe.gen/"+ProjectRecord.getArtifactList().get(artifact)+".txt",
					new ViewLocator(
					"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));

			// Need to check contents!
		}
	}
	
	public void checkExtras() throws Exception {
		
//		TreeItemLocator treeLocator = new TreeItemLocator(
//				TestingConstants.NEW_PROJECT_NAME+"/target/tigerstripe.gen",
//				new ViewLocator(
//				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
//		
//		
//		
//		new com.windowtester.swt.util.DebugHelper().printWidgets();
//		TreeItem  tree = (TreeItem) ((IWidgetReference) treeLocator).getWidget();
//				int children = tree.getItemCount();
//				// Look for anything that is NOT
//				// in the ProjectReocrd,
//				// TigerstripeReport
//				// allArtifacts.txt
//				
//		int artifacts = ProjectRecord.getArtifactCount();
//		System.out.println(children);
//		System.out.println(artifacts);
//		failNotEquals("Found too many files in the generated directory ", artifacts+2, children);


		
	}

}
