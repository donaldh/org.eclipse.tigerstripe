package org.eclipse.tigerstripe.workbench.ui.base.test.profile;

import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import org.eclipse.swt.widgets.Table;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.Generate;
import org.eclipse.tigerstripe.workbench.ui.base.test.generator.GenerateHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ProjectRecord;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;

public class UpdateProfileArtifacts extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void testUpdateArtifacts() throws Exception {
		IUIContext ui = getUI();
		// TODO  This is based on positions in the list! Nasty
		//disableArtifactType(ui, 0, TestingConstants.SESSION_NAMES[0]);
		disableArtifactType(ui, 1, TestingConstants.QUERY_NAMES[0]);
	}

	public void disableArtifactType(IUIContext ui, int index, String name) throws Exception {	
		ui.click(2,
				new TreeItemLocator(
						TestingConstants.NEW_MODEL_PROJECT_NAME+"/"+
								TestingConstants.NEW_PROFILE_NAME+".wbp",
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui.click(new CTabItemLocator("Artifacts"));
		ui.click(new TableItemLocator("", index, new SWTWidgetLocator(
						Table.class)));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		ui.contextClick(
						new TreeItemLocator(
								TestingConstants.NEW_MODEL_PROJECT_NAME+"/"+
								TestingConstants.NEW_PROFILE_NAME+".wbp",
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
						"Profile/Set as active profile.");
		ui.wait(new ShellShowingCondition("Save as Active Profile"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Save as Active Profile"));
		ui.wait(new ShellShowingCondition("Success"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Success"));
		
		
		// Just be sure we actually created the thing in the first place!
		if (ProjectRecord.getArtifactList().containsValue(name)){
			String pathToEntity = TestingConstants.NEW_MODEL_PROJECT_NAME+
			"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"/"+
			name;

			TreeItemLocator treeItem = new TreeItemLocator(
					pathToEntity,
					new ViewLocator(
					"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
			// This should not be valid any more!
			try {
				ui.click(treeItem);
				// TODO This is a known BUG 219575
				//	fail("Artifact is still being shown although disabled in Profile : "+name);
			} catch (Exception noWidget){
				// This is what we want to happen!
			}
			
			GenerateHelper helper = new GenerateHelper(ui);
		
			// Make sure we aren't still generating it
			ProjectRecord.removeArtifactbyName(name);
			clearOutput(ui);
			helper.runGenerate();
			helper.checkGlobal();
			helper.checkArtifact();
			helper.checkExtras();
			
		}
		
		
		
	}
	
	private void clearOutput (IUIContext ui) throws Exception{
		ui
		.contextClick(
				new TreeItemLocator(
						"New Project/target/tigerstripe.gen",
						new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
		"Delete");
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellShowingCondition("Confirm Delete"));
		ui.click(new ButtonLocator("&Yes"));
		ui.wait(new ShellDisposedCondition("Confirm Delete"));
		
		
	}
	
}