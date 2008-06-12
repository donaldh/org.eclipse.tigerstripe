package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.PullDownMenuItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;
import com.windowtester.runtime.swt.locator.forms.HyperlinkLocator;

public class NewArtifacts extends UITestCaseSWT {

	private ArtifactHelper helper;
	private ArtifactAuditHelper auditHelper;
	
	
	public void testAll() throws Exception {
		IUIContext ui= getUI();
		ProjectRecord.addArtifact(testNewArtifactDefaults(ui,"Entity", TestingConstants.ENTITY_NAMES[0], true, true, true, false));
		ProjectRecord.addArtifact(testNewArtifactDefaults(ui,"Datatype", TestingConstants.DATATYPE_NAMES[0], true, true, true, false));
		ProjectRecord.addArtifact(testNewArtifactDefaults(ui,"Enumeration", TestingConstants.ENUMERATION_NAMES[0], false, true, false, false));
		ProjectRecord.addArtifact(testNewArtifactDefaults(ui,"Query", TestingConstants.QUERY_NAMES[0], true, true, false, false));
		ProjectRecord.addArtifact(testNewArtifactDefaults(ui,"Update Procedure", TestingConstants.UPDATE_NAMES[0], true, true, false, false));
		ProjectRecord.addArtifact(testNewArtifactDefaults(ui,"Exception", TestingConstants.EXCEPTION_NAMES[0], true, false, false, false));
		ProjectRecord.addArtifact(testNewArtifactDefaults(ui,"Notification", TestingConstants.EVENT_NAMES[0], true, true, false, false));
		ProjectRecord.addArtifact(testNewArtifactDefaults(ui,"Session Facade", TestingConstants.SESSION_NAMES[0], false, false,true, false));

//		// Add a second entity, so that the associations etc can be better checked - No need to add and attributes etc
		ProjectRecord.addArtifact(testNewArtifactDefaults(ui,"Entity", TestingConstants.ENTITY_NAMES[1], false, false, false, false));
		ProjectRecord.addArtifact(testNewArtifactDefaults(ui,"Association", TestingConstants.ASSOCIATION_NAMES[0], false, false,false, true));
		ProjectRecord.addArtifact(testNewArtifactDefaults(ui,"Association Class", TestingConstants.ASSOCIATION_CLASS_NAMES[0], true, false,true, true));
		ProjectRecord.addArtifact(testNewArtifactDefaults(ui,"Dependency", TestingConstants.DEPENDENCY_NAMES[0], false, false,false, true));
		
		
		// Let the auditor run
		Thread.sleep(500);
		// Some artifacts have errors on creation that we need to correct.
		// First check we get them
		auditHelper = new ArtifactAuditHelper(ui);
		String queryName = TestingConstants.QUERY_NAMES[0];

		// Just be sure we actually created the query!
		if (ProjectRecord.getArtifactList().containsValue(queryName)){
			auditHelper.checkUndefinedReturnType(queryName,true);

			// Update the returned type of our Query
			String pathToEntity = TestingConstants.NEW_PROJECT_NAME+
			"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"/"+
			queryName;

			TreeItemLocator treeItem = new TreeItemLocator(
					pathToEntity,
					new ViewLocator(
					"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
			ui.click(2, treeItem);
			CTabItemLocator artifactEditor = new CTabItemLocator(
					queryName);
			ui.click(artifactEditor);
			ui.click(new CTabItemLocator("Details"));
			ui.click(new LabeledLocator(Button.class, "Returned Type: "));
			ui.wait(new ShellShowingCondition("Returned Type"));
			ui.click(new TableItemLocator(TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+
					TestingConstants.ENTITY_NAMES[0]));
			ui.click(new ButtonLocator("OK"));
			ui.wait(new ShellDisposedCondition("Returned Type"));
			//Save it
			ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));

			// Let the auditor run
			Thread.sleep(500);
			auditHelper.checkUndefinedReturnType(queryName,false);
			ui.close(new CTabItemLocator(queryName));
		}
		String[] assocs = {TestingConstants.ASSOCIATION_NAMES[0], TestingConstants.ASSOCIATION_CLASS_NAMES[0]};
		for (String assoc : assocs){
			if (ProjectRecord.getArtifactList().containsValue(assoc)){
				// Let the auditor run
				Thread.sleep(500);
				// This rule uses FQN!
				auditHelper.checkAssociationEndNeedsNavigation(TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+assoc,true);
				// Update one of the ends
				String pathToEntity = TestingConstants.NEW_PROJECT_NAME+
				"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"/"+
				assoc;

				TreeItemLocator treeItem = new TreeItemLocator(
						pathToEntity,
						new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
				ui.click(2, treeItem);
				CTabItemLocator artifactEditor = new CTabItemLocator(
						assoc);
				ui.click(artifactEditor);
				GuiUtils.maxminTab(ui, assoc);
				ui.click(new HyperlinkLocator("aEnd"));
				IWidgetLocator[] allNavs = ui.findAll(new ButtonLocator("isNavigable"));
				// Need to disambiguate
				ui.click(allNavs[0]);
				
				//Save it
				ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
				
				GuiUtils.maxminTab(ui, assoc);
				// Let the auditor run
				Thread.sleep(500);
				auditHelper.checkAssociationEndNeedsNavigation(TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+assoc,false);
				ui.close(new CTabItemLocator(assoc));
			}
		}
		
		
	}
	
	/**
	 * This is a simple create using just the name.
	 * We need to check the defaults are properly created.
	 * @throws Exception
	 */
	public String testNewArtifactDefaults(IUIContext ui,String myType, String thisArtifactName, boolean hasAttributes,
			boolean hasLiterals, boolean hasMethods, boolean hasEnds) throws Exception	{
		
		
		
		ui.click(new TreeItemLocator(
				TestingConstants.NEW_PROJECT_NAME,
				new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui.click(new PullDownMenuItemLocator(myType,
				new ContributedToolItemLocator(
						"org.eclipse.tigerstripe.eclipse.newArtifactAction")));
		ui.wait(new ShellShowingCondition("New "+myType+" Artifact"));
		
		ui.enterText(thisArtifactName);
		
		LabeledTextLocator artifactPackage = new LabeledTextLocator("Artifact Package:");
		assertEquals("Default package for new "+myType+" Wizard is not that set for the project",TestingConstants.DEFAULT_ARTIFACT_PACKAGE, artifactPackage.getText(ui));
		
		// Validate the right stuff is in the list!
		// Then check in the tree view
		
		if (hasEnds){
			ui.click(new LabeledLocator(Button.class, "aEnd Type"));
			ui.wait(new ShellShowingCondition(myType+" End Type"));
			ui.click(new TableItemLocator(TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+
					TestingConstants.ENTITY_NAMES[0]));
			ui.click(new ButtonLocator("OK"));
			ui.wait(new ShellDisposedCondition(myType+" End Type"));
			ui.click(new LabeledLocator(Button.class, "zEnd Type"));
			ui.wait(new ShellShowingCondition(myType+" End Type"));
			ui.click(new TableItemLocator(TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+
					TestingConstants.ENTITY_NAMES[1]));
			ui.click(new ButtonLocator("OK"));
			ui.wait(new ShellDisposedCondition(myType+" End Type"));
		}
		
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("New "+myType+" Artifact"));
		
		CTabItemLocator artifactEditor = new CTabItemLocator(
				thisArtifactName);
		
		try {
			ui.click(artifactEditor);
		} catch (Exception e) {
			fail("Artifact editor did not appear");
		}
		
		// See if it has the right details
		LabeledTextLocator project = new LabeledTextLocator("Project: ");
		assertEquals("Project for "+myType+" is incorrect",TestingConstants.NEW_PROJECT_NAME, project.getText(ui));
		
		LabeledTextLocator fqn = new LabeledTextLocator("Qualified Name: ");
		assertEquals("FQN for "+myType+" is incorrect",TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+thisArtifactName, fqn.getText(ui));
		
		LabeledTextLocator extend = new LabeledTextLocator("Extends: ");
		if (myType.equals("Exception")){
			assertEquals("Extends for "+myType+" should be fixed on create","java.lang.Exception", extend.getText(ui));
		}else {
			assertEquals("Extends for "+myType+" should be empty on create","", extend.getText(ui));
		}
		
		LabeledTextLocator description = new LabeledTextLocator("Description: ");
		assertEquals("Description for "+myType+" should be empty on create","", description.getText(ui));
		
		ArrayList<String> artifacts = new ArrayList<String>();
		artifacts.add(thisArtifactName);
		
		ProjectHelper.checkArtifactsInExplorer(ui, artifacts);

		
		
		// Maximise before we go to do the components
		GuiUtils.maxminTab(ui, thisArtifactName);
		// collapse the Section which is open by default sections - for consistent use in the helper
		if (hasEnds){
			SWTWidgetLocator detailsSection = new SWTWidgetLocator(Label.class, "End &Details");
			ui.click(detailsSection);
		} else if (hasAttributes){
			SWTWidgetLocator attributesSection = new SWTWidgetLocator(Label.class, "&Attributes");
			ui.click(attributesSection);
		} else if (hasLiterals){
			SWTWidgetLocator literalsSection = new SWTWidgetLocator(Label.class, "Constants");
			ui.click(literalsSection);
		} else if (hasMethods){
			SWTWidgetLocator methodsSection = new SWTWidgetLocator(Label.class, "Methods");
			ui.click(methodsSection);
		}
		
		

		ArrayList<String> items = new ArrayList<String>();
		
		if (hasEnds && (myType.equals("Association") || myType.equals("Association Class")) )
			items.addAll(ArtifactHelper.associationEndNames(ui,thisArtifactName));
		if (hasEnds && myType.equals("Dependency")  )
			items.addAll(ArtifactHelper.dependencyEndNames(ui,thisArtifactName));
		if (hasAttributes)
			items.add(ArtifactHelper.newAttribute(ui,thisArtifactName, TestingConstants.ATTRIBUTE_NAMES[0]));
		if (hasLiterals)
			items.add(ArtifactHelper.newLiteral(ui,thisArtifactName, TestingConstants.LITERAL_NAMES[0]));
		if( hasMethods)
			items.add(ArtifactHelper.newMethod(ui,thisArtifactName, TestingConstants.METHOD_NAMES[0]));

		
		GuiUtils.maxminTab(ui, thisArtifactName);
		
		ArtifactHelper.checkItemsInExplorer(ui,thisArtifactName,items);
		// Close the editor
		ui.close(new CTabItemLocator(thisArtifactName));
		return thisArtifactName;
	}

}
