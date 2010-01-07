package org.eclipse.tigerstripe.refactor.delete;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.tigerstripe.refactor.project.ProjectHelper;
import org.eclipse.tigerstripe.refactor.suite.DiagramHelper;
import org.eclipse.tigerstripe.ui.visualeditor.test.finders.LocatorHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.gef.locator.FigureClassLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.PullDownMenuItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class DeleteTests extends UITestCaseSWT {
	
	private ProjectHelper helper;
	private ArtifactHelper artifactHelper;
	private static IUIContext ui;
	private ViewLocator view;
	
	private static String project = "model-refactoring";
	private static String[] diagrams = {"default.wvd","outside-class-diagram.wvd","inside-moved.wvd","outside-instance-diagram.wod"};
	private static String[] ent2Diagrams = {"default.wvd","inside-moved.wvd","outside-instance-diagram.wod"};
	private static String errors    = "Errors \\([0-9]* item[s]?\\)/";
	private static String instanceDiagram = "outside-instance-diagram.wod";
	
	public void setUp() throws Exception{
		ui = getUI();
		helper = new ProjectHelper();
		artifactHelper = new ArtifactHelper();
		
		helper.reloadProjectFromCVS(ui);
		
		view = new ViewLocator(
		"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		// Open all of the diagrams
		
		DiagramHelper.openDiagrams(ui);
		DiagramHelper.openInstanceDiagram(ui);
		
	}
	
	public void testDelete() throws Exception{
		// Check Ent1 currently in class diagrams
		checkPresentInDiagrams(ui,"Ent1");
		
		// Delete Ent1 from explorer
		ViewLocator view = new ViewLocator(
		"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");

        ui.contextClick(
		      new TreeItemLocator(project + "/src/simple/Ent1", view),
		      "Delete");
        ui.wait(new ShellShowingCondition("Confirm Delete"));
        ui.click(new ButtonLocator("OK"));        
        ui.wait(new ShellDisposedCondition("Confirm Delete"));

		// Let the updates happen!
		Thread.sleep(500);
		
		//Check dirty diagrams for deletion of Ent1
		checkDeletedFromDiagrams(ui, "Ent1");
		
		//saveDiagrams
		saveDiagrams(ui);
		
		//check for problems view entries
		checkProblemsView(ui);
		
		//check ent2 in diagrams
		checkPresentInDiagrams(ui,"Ent2");
		
		//Delete ent2 from class diagram
		ui.click(new CTabItemLocator("default.wvd"));
		String artifactPrefix = "";
		String entityName = "Ent2";
		LocatorHelper helper = new LocatorHelper();
		ui.contextClick(helper.getManagedEntityLocator(ui, artifactPrefix
				+ entityName), "Remove from Model");
		ui.wait(new ShellShowingCondition("Delete from Model"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Delete from Model"));
		
		//check gone from explorer
		boolean deleted = false;
		try {
		ArtifactHelper.checkArtifactInExplorer(ui,project,"simple", entityName);
		}catch (Exception e){
			deleted = true;
		}finally{
			if(!deleted)
				fail("Entity '" + entityName + "' not deleted from explorer");
		}
		
		//check gone in other class diagram and instance diagram
		checkDeletedFromDiagrams(ui, "Ent2");
		
		//Save and close diagrams
		saveEnt2Diagrams(ui);
		closeDiagrams(ui);
	}
	
	public static void checkDeletedFromDiagrams(IUIContext ui, String entityName) throws Exception {
		LocatorHelper helper = new LocatorHelper();
		if(entityName.equals("Ent1")){
		ui.click(new CTabItemLocator("*default.wvd"));
		String artifactPrefix = "";
		internalCheckDeletedFromDiagram(ui, helper, artifactPrefix, artifactPrefix,
				entityName);
		artifactPrefix = "simple.";
		ui.click(new CTabItemLocator("*inside-moved.wvd"));
		internalCheckDeletedFromDiagram(ui, helper, artifactPrefix, artifactPrefix,
				entityName);
		ui.click(new CTabItemLocator("*outside-class-diagram.wvd"));
		internalCheckDeletedFromDiagram(ui, helper, artifactPrefix, artifactPrefix,
				entityName);
		ui.click(new CTabItemLocator('*'+instanceDiagram));
		internalCheckDeletedFromInstanceDiagram(ui, helper, "ent1:simple.Ent1");
		internalCheckDeletedFromInstanceDiagram(ui, helper, "ent11:simple.Ent1");
		} else if(entityName.equals("Ent2")){
			String artifactPrefix = "simple.";
			ui.click(new CTabItemLocator("*inside-moved.wvd"));
			internalCheckDeletedFromDiagram(ui, helper, artifactPrefix, artifactPrefix,
					entityName);
			ui.click(new CTabItemLocator('*'+instanceDiagram));
			internalCheckDeletedFromInstanceDiagram(ui, helper, "ent2:simple.Ent2");
		}
	}
	
	public static void checkPresentInDiagrams(IUIContext ui,String entityName) throws Exception {
		LocatorHelper helper = new LocatorHelper();
		ui.click(new CTabItemLocator("default.wvd"));
		String artifactPrefix = "";
		internalCheckPresentInDiagram(ui, helper, artifactPrefix, artifactPrefix,
				entityName);
		artifactPrefix = "simple.";
		ui.click(new CTabItemLocator("inside-moved.wvd"));
		internalCheckPresentInDiagram(ui, helper, artifactPrefix, artifactPrefix,
				entityName);
		if(entityName.equals("Ent1")){
		ui.click(new CTabItemLocator("outside-class-diagram.wvd"));
		internalCheckPresentInDiagram(ui, helper, artifactPrefix, artifactPrefix,
				"Ent1");
		ui.click(new CTabItemLocator(instanceDiagram));
		internalCheckPresentInInstanceDiagram(ui,helper,"ent1:simple.Ent1");
		internalCheckPresentInInstanceDiagram(ui,helper,"ent11:simple.Ent1");
		} else if(entityName.equals("Ent2")){
			ui.click(new CTabItemLocator(instanceDiagram));
			internalCheckPresentInInstanceDiagram(ui, helper, "ent2:simple.Ent2");
		}
	}
	
	public static void internalCheckPresentInInstanceDiagram(IUIContext ui, LocatorHelper helper, String artifactName){
		try{
			ui.click(helper.getManagedEntityInstanceLocator(ui, artifactName));
		} catch (Exception e){
			fail("Entity instance '"+artifactName+"' not found on instance diagram.");
		}
	}
	
	public static void internalCheckPresentInDiagram(IUIContext ui,
			LocatorHelper helper, String artifactPrefix,
			String myArtifactPrefix, String artifactName) throws Exception {
		try {
			ui.click(helper.getManagedEntityLocator(ui, myArtifactPrefix
					+ artifactName));
		} catch (Exception e) {
			fail("Entity '" + myArtifactPrefix + artifactName
					+ "' not found on diagram");
		}		
	}
	
	public static void internalCheckDeletedFromDiagram(IUIContext ui,
			LocatorHelper helper, String artifactPrefix,
			String myArtifactPrefix, String artifactName) throws Exception {
		boolean found = true;
		try {
			ui.click(helper.getManagedEntityLocator(ui, myArtifactPrefix
					+ artifactName));
		} catch (Exception e) {
			found = false;
		} finally {
			if(found)
			fail("Entity '" + myArtifactPrefix + artifactName
					+ "' found on diagram");
		}
	}
	
	public static void internalCheckDeletedFromInstanceDiagram(IUIContext ui,
			LocatorHelper helper, String artifactName) throws Exception {
		boolean found = true;
		try {
			ui.click(helper.getManagedEntityInstanceLocator(ui, artifactName));
		} catch (Exception e) {
			found = false;
		} finally {
			if(found)
			fail("Entity '" + artifactName + "' found on diagram");
		}
	}
	
	public static void saveEnt2Diagrams(IUIContext ui)
	throws Exception {

    for (String diagram : ent2Diagrams) {
	   ui.click(new CTabItemLocator("*" + diagram));
	   ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
     }
   }
	
	public static void closeDiagrams(IUIContext ui) throws Exception{
		for(String diagram : diagrams){
		ui.close(new CTabItemLocator(diagram));
		}
	}
	
	public static void saveDiagrams(IUIContext ui)
	throws Exception {

    for (String diagram : diagrams) {
	   ui.click(new CTabItemLocator("*" + diagram));
	   ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));	   
     }	   
   }
	
	private static void checkProblemsView(IUIContext ui) throws Exception{
		ViewLocator probView = new ViewLocator(
		"org.eclipse.ui.views.ProblemView");
		ui.click(new CTabItemLocator("Problems"));
		ui.click(new PullDownMenuItemLocator("Show/Errors\\\\/Warnings on Selection",
				probView));
		ui.click(new TreeItemLocator(project + "/src/simple/Ent2",
				new ViewLocator("org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		checkRule(errors,"simple.Ent1 cannot be resolved to a type",true);
	}
	
	private static void checkRule(
			String type, 
			String message,
			boolean expected){
	TreeItemLocator auditRule = new TreeItemLocator(
			type+message,
			new ViewLocator("org.eclipse.ui.views.ProblemView"));
	try {
		ui.click(auditRule);
		if (! expected && auditRule != null)
			fail("Rule with text "+message+"  is being fired unexpectedly");
	} catch (Exception noWidget){
		if ( expected)
			fail("Rule with text "+message+" is not being fired when expected");	
	}
	}
	
	private static class DiagramEditPart$1Locator extends FigureClassLocator {
		private static final long serialVersionUID = 1L;

		public DiagramEditPart$1Locator() {
			super(
					"org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart$1");
		}
	}

}
