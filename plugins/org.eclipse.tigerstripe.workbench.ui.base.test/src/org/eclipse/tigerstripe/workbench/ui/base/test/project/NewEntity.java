package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.PullDownMenuItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class NewEntity extends UITestCaseSWT {

	private ArtifactHelper helper;
	
	/**
	 * This is a simple create using just the name.
	 * We need to check the defaults are properly created.
	 * @throws Exception
	 */
	public void testNewManagedEntityDefaults() throws Exception	{
		IUIContext ui= getUI();
		ui.click(new TreeItemLocator(
				TestingConstants.NEW_PROJECT_NAME,
				new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui.click(new PullDownMenuItemLocator("Entity",
				new ContributedToolItemLocator(
						"org.eclipse.tigerstripe.eclipse.newArtifactAction")));
		ui.wait(new ShellShowingCondition("New Entity Artifact"));
		String thisEntityName = TestingConstants.ENTITY_NAMES[0];
		ui.enterText(thisEntityName);
		
		LabeledTextLocator artifactPackage = new LabeledTextLocator("Artifact Package:");
		assertEquals("Default package for new Entity Wizard is not that set for the project",TestingConstants.DEFAULT_ARTIFACT_PACKAGE, artifactPackage.getText(ui));
		
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("New Entity Artifact"));
		
		CTabItemLocator artifactEditor = new CTabItemLocator(
				TestingConstants.ENTITY_NAMES[0]);
		
		try {
			ui.click(artifactEditor);
		} catch (Exception e) {
			fail("Artifact editor did not appear");
		}
		
		// See if it has the right details
		LabeledTextLocator project = new LabeledTextLocator("Project: ");
		assertEquals("Project for Entity is incorrect",TestingConstants.NEW_PROJECT_NAME, project.getText(ui));
		
		LabeledTextLocator fqn = new LabeledTextLocator("Qualified Name: ");
		assertEquals("FQN for Entity is incorrect",TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+thisEntityName, fqn.getText(ui));
		
		LabeledTextLocator extend = new LabeledTextLocator("Extends: ");
		assertEquals("Extends for Entity should be empty on create","", extend.getText(ui));
		
		LabeledTextLocator description = new LabeledTextLocator("Description: ");
		assertEquals("Description for Entity should be empty on create","", description.getText(ui));
		
		// See if it is in the tree view.
		// Exception thrown if widget not found!
		String pathToEntity = TestingConstants.NEW_PROJECT_NAME+
								"/src/"+
								TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"/"+
								thisEntityName;
		try {
			TreeItemLocator treeItem = new TreeItemLocator(
				pathToEntity,
				new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
		} catch (Exception e){
			fail("Entity is not in the Explorer view");
		}		
		
		helper = new ArtifactHelper();
		helper.newAttribute(ui,TestingConstants.ENTITY_NAMES[0]);
		
		GuiUtils.maxminTab(ui, TestingConstants.ENTITY_NAMES[0]);
		ui.click(new XYLocator(new CTabItemLocator(TestingConstants.ENTITY_NAMES[0]), 70, 12));
	}

}
