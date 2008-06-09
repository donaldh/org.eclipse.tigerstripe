package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
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

public class NewArtifacts extends UITestCaseSWT {

	private ArtifactHelper helper;
	
	
	public void testAll() throws Exception {
		testNewArtifactDefaults("Entity", TestingConstants.ENTITY_NAMES[0], true, true, true, false);
		// Add a second entity, so that the associations etc can be better checked
		testNewArtifactDefaults("Entity", TestingConstants.ENTITY_NAMES[1], true, true, true, false);
		testNewArtifactDefaults("Datatype", TestingConstants.DATATYPE_NAMES[0], true, true, true, false);
		testNewArtifactDefaults("Enumeration", TestingConstants.ENUMERATION_NAMES[0], false, true, false, false);
		testNewArtifactDefaults("Query", TestingConstants.QUERY_NAMES[0], true, true, false, false);
		testNewArtifactDefaults("Update Procedure", TestingConstants.UPDATE_NAMES[0], true, true, false, false);
		testNewArtifactDefaults("Exception", TestingConstants.EXCEPTION_NAMES[0], true, false, false, false);
		testNewArtifactDefaults("Notification", TestingConstants.EVENT_NAMES[0], true, true, false, false);
		testNewArtifactDefaults("Session Facade", TestingConstants.SESSION_NAMES[0], false, false,true, false);
		testNewArtifactDefaults("Association", TestingConstants.ASSOCIATION_NAMES[0], false, false,false, true);
		testNewArtifactDefaults("Association Class", TestingConstants.ASSOCIATION_CLASS_NAMES[0], true, false,true, true);
		testNewArtifactDefaults("Dependency", TestingConstants.DEPENDENCY_NAMES[0], false, false,false, true);
	}
	
	/**
	 * This is a simple create using just the name.
	 * We need to check the defaults are properly created.
	 * @throws Exception
	 */
	public void testNewArtifactDefaults(String myType, String thisArtifactName, boolean hasAttributes,
			boolean hasLiterals, boolean hasMethods, boolean hasEnds) throws Exception	{
		
		
		IUIContext ui= getUI();
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
		
		// TODO Add some more entities to check against
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
		
		// See if it is in the tree view.
		// Exception thrown if widget not found!
		String pathToEntity = TestingConstants.NEW_PROJECT_NAME+
								"/src/"+
								TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"/"+
								thisArtifactName;
		try {
			TreeItemLocator treeItem = new TreeItemLocator(
				pathToEntity,
				new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
			ui.click(treeItem);
		} catch (Exception e){
			fail(""+myType+" is not in the Explorer view");
		}		
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
		
		
		helper = new ArtifactHelper();
		ArrayList<String> items = new ArrayList<String>();
		
		if (hasEnds && (myType.equals("Association") || myType.equals("Association Class")) )
			items.addAll(helper.associationEndNames(ui,thisArtifactName));
		if (hasEnds && myType.equals("Dependency")  )
			items.addAll(helper.dependencyEndNames(ui,thisArtifactName));
		if (hasAttributes)
			items.add(helper.newAttribute(ui,thisArtifactName, TestingConstants.ATTRIBUTE_NAMES[0]));
		if (hasLiterals)
			items.add(helper.newLiteral(ui,thisArtifactName, TestingConstants.LITERAL_NAMES[0]));
		if( hasMethods)
			items.add(helper.newMethod(ui,thisArtifactName, TestingConstants.METHOD_NAMES[0]));

		
		GuiUtils.maxminTab(ui, thisArtifactName);
		
		helper.checkItemsInExplorer(ui,thisArtifactName,items);
		
		ui.click(new XYLocator(new CTabItemLocator(thisArtifactName), 70, 12));
	}

}
