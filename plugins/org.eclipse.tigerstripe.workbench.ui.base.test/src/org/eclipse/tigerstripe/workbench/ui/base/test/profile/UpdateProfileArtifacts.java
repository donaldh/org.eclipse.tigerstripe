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
		// This is based on positions in the list which is aplhabetical based on the full Name
		// eg ManagedEntity, not Entity! 

		
		disableArtifactType(ui, 0, TestingConstants.ASSOCIATION_NAMES[0]);
		disableArtifactType(ui, 1, TestingConstants.ASSOCIATION_CLASS_NAMES[0]);
		disableArtifactType(ui, 2, TestingConstants.DATATYPE_NAMES[0]);
		disableArtifactType(ui, 3, TestingConstants.DEPENDENCY_NAMES[0]);
		disableArtifactType(ui, 4, TestingConstants.ENUMERATION_NAMES[0]);
		
		disableArtifactType(ui, 5, TestingConstants.EVENT_NAMES[0]);
		disableArtifactType(ui, 6, TestingConstants.EXCEPTION_NAMES[0]);
		
		// Don't try to generate becase the project will have all sorts of erros on it
		disableArtifactType(ui, 7, TestingConstants.ENTITY_NAMES[0], false);
		disableArtifactType(ui, 8, TestingConstants.PACKAGE_NAMES[0]);
		// Don't do for Primitive Types
		disableArtifactType(ui, 10, TestingConstants.QUERY_NAMES[0]);
		disableArtifactType(ui, 11, TestingConstants.SESSION_NAMES[0]);
		disableArtifactType(ui, 12, TestingConstants.UPDATE_NAMES[0]);

		// When complete just need to do one more deploy to make sure everything turned back on.
		
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

	}

	public void disableArtifactType(IUIContext ui, int index, String name) throws Exception {
		disableArtifactType(ui,index,name,true);
	}
	
	public void disableArtifactType(IUIContext ui, int index, String name, boolean generate) throws Exception {	
		ui.click(2,
				new TreeItemLocator(
						TestingConstants.NEW_MODEL_PROJECT_NAME+"/"+
								TestingConstants.NEW_PROFILE_NAME+".wbp",
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		Thread.sleep(500);
		ui.click(new CTabItemLocator("Artifacts"));
		// Select the type
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
			} catch (Exception noWidget){
				// This is what we want to happen!
			}
			
			if (generate){
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
		// re-enable the type in the profile.
		ui.click(2,
				new TreeItemLocator(
						TestingConstants.NEW_MODEL_PROJECT_NAME+"/"+
								TestingConstants.NEW_PROFILE_NAME+".wbp",
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		Thread.sleep(500);
		ui.click(new CTabItemLocator("Artifacts"));
		// Select the type
		ui.click(new TableItemLocator("", index, new SWTWidgetLocator(
						Table.class)));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		
	}
	
	private void clearOutput (IUIContext ui) throws Exception{
		
		try {
			ui
			.contextClick(
					new TreeItemLocator(
							TestingConstants.NEW_MODEL_PROJECT_NAME+"/target/tigerstripe.gen",
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
	
}