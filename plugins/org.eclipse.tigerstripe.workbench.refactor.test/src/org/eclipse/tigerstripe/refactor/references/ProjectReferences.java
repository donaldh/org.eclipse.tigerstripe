package org.eclipse.tigerstripe.refactor.references;

import java.util.ArrayList;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.refactor.artifact.Ent1_to_Ent10;
import org.eclipse.tigerstripe.refactor.delete.DeleteTests;
import org.eclipse.tigerstripe.refactor.suite.DiagramHelper;
import org.eclipse.tigerstripe.ui.visualeditor.test.finders.LocatorHelper;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.ProjectHelper;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WT;
import com.windowtester.runtime.WidgetNotFoundException;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class ProjectReferences extends UITestCaseSWT{
	
	private static IUIContext ui;
	private ProjectHelper helper;
	//private ArtifactHelper artifactHelper;
	
	private static String referenceProjectName ="model-refactoring-reference"; 
	private static String projectName = "model-refactoring";
	
	private static String[] editors = {"Association3","AssociationClass1","Dependency1","Ent3","Query1"};
	
	
	public void setUp() throws Exception{
		ui = getUI();
		helper = new ProjectHelper();
		//artifactHelper = new ArtifactHelper();
		
		helper.reloadProjectFromCVS(ui,projectName);
		//helper.loadProjectFromCVS(ui);
		Thread.sleep(30000);
		helper.reloadProjectFromCVS(ui, referenceProjectName);
		
		//open all diagrams
		DiagramHelper.openDiagrams(ui, referenceProjectName);
		DiagramHelper.openNamedDiagram(ui, projectName,"src/simple/default");
		//open all editors
		openRelatedEditors(ui);
	}
	
	public void tearDown() throws Exception{
		//close all artifact editors
		saveAndCloseRelatedEditors(ui);
		
		//close all diagrams
		saveAndCloseDiagram(ui,"default.wvd");
		saveAndCloseDiagram(ui,"instanceDiagram.wod");		
		saveAndCloseDiagram(ui,"classDiagram.wvd");
	}
	

	public void testReference() throws Exception{
		
		//clear all Error logs
		clearErrorLog(ui);
		
		// check Ent1 in class diagram and instance diagram
		checkPresentInDiagrams(ui,"Ent1",false);
		
		//Rename Ent1 to Ent10
		Ent1_to_Ent10.doChangeThroughExplorer(ui);
		
		//check Error log and clear it
		//checkNoErrorsFromTS(ui,"Rename of Ent1 to Ent10");
		clearErrorLog(ui);
		
		//check Ent1 updated in class diagram and instance diagram
		checkPresentInDiagrams(ui,"Ent10",true);
		
		//check artifacts for references to ent10 to see updated
		checkEditorUpdates(ui,  "simple", "Ent10",false);
		
		//check explorer updates
		checkExplorerUpdates(ui,"simple","Ent10");
		
		//move Ent10 to different package
		doChangeByMove(ui);
		
		//check diagrams and artifact references are updated to new location
		checkPresentInDiagrams(ui,"simple.moved.Ent10",true);
		checkEditorUpdates(ui,  "simple.moved", "Ent10",true);
		
		//check Error log and clear it
		//checkNoErrorsFromTS(ui,"Move of Ent10 from simple to simple.moved");
		clearErrorLog(ui);

		
		//rename Ent10 in its local project in class diagram to Ent100
		//doChangeThroughDiagramInSamePackage(ui);
		
        //check diagrams and artifact references are updated to new location
		
		//check Error log and clear it
		
        //rename Ent100 in remote project in class diagram to Ent1000
		
        //check diagrams and artifact references are updated to new location
		
		//check Error log and clear it
		
		//delete Ent1000
		
        //check diagrams and artifact references are updated to new location
		
		//check Error log and clear it
		
		
	}
	
	public static void doChangeThroughDiagramInSamePackage(IUIContext ui)
	throws Exception {
		LocatorHelper helper = new LocatorHelper();
		ui.click(new CTabItemLocator("*default.wvd"));
		//IWidgetLocator tfl = helper.getNameEditLocator(ui, "simple.moved.Ent10");
		IWidgetLocator tfl = helper.getNameEditLocator(ui, "Ent10");
		ui.click(tfl);
		ui.click(tfl);
		Thread.sleep(1000);
		ui.click(tfl);
		ui.click(new SWTWidgetLocator(Text.class, new SWTWidgetLocator(
				FigureCanvas.class)));
		ui.keyClick(WT.END);
		ui.enterText("0");
		ui.keyClick(WT.CR);

}
	
	public static void checkExplorerUpdates(IUIContext ui, String myPackage, String name) throws Exception{
		ViewLocator view = new ViewLocator(
		"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		// Check for ourself!
		ArtifactHelper.checkArtifactInExplorer(ui, projectName, myPackage, name);
		
		ArrayList<String> items = new ArrayList<String>();
		items.add("ent1:" + name);
		ArtifactHelper.checkItemsInExplorer(ui, referenceProjectName, "simple", "Association3", items);
		items = new ArrayList<String>();
		items.add("ent1_1:"+name);
		ArtifactHelper.checkItemsInExplorer(ui, referenceProjectName, "simple", "AssociationClass1", items);
		items = new ArrayList<String>();
		items.add(name);
		ArtifactHelper.checkItemsInExplorer(ui, referenceProjectName, "simple", "Dependency1", items);
		items = new ArrayList<String>();
		items.add("attribute0:"+name);
		items.add("method0("+name+"):"+name+"[0..1]");
		ArtifactHelper.checkItemsInExplorer(ui, referenceProjectName, "simple", "Ent3", items);
	}
	
	public static void saveAndCloseDiagram(IUIContext ui,String name) throws Exception{
		
			try {
				ui.click(new CTabItemLocator("*"+name));
				ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
				ui.click(new CTabItemLocator(name));
			} catch (Exception e){
				// Guess it was not dirty!
			}		
	}
	
	
	
	public static void checkEditorUpdates(IUIContext ui, String myPackage,
			String name,boolean subsequent) throws Exception {
		
		String newName = myPackage + "." + name;
		
		ui.click(new CTabItemLocator("*Association3"));
		if(!subsequent)
		ui.click(new SWTWidgetLocator(Label.class, "End &Details"));
		ArrayList<String> endNames = ArtifactHelper.associationEndNames(ui, "*Association3");
		String endName = endNames.get(1);
		assertEquals("Association End type not updated in editor","ent1"+":"+name,endName);
		
		ui.click(new CTabItemLocator("*AssociationClass1"));
		if(!subsequent)
		ui.click(new SWTWidgetLocator(Label.class, "End &Details"));
		endNames = ArtifactHelper.associationEndNames(ui, "*AssociationClass1");
		endName = endNames.get(1);
		assertEquals("Association End type not updated in editor","ent1_1"+":"+name,endName);
		
		//TODO dependency end name check
		
		//TODO Query return type check
		
		ui.click(new CTabItemLocator("*Ent3"));
		LabeledTextLocator extend = new LabeledTextLocator("Extends: ");
		assertEquals("Extended type not updated in Editor", newName, extend
				.getText(ui));
		// TODO check attribute type updated
		GuiUtils.maxminTab(ui, "*Ent3");
		if(!subsequent)
		ui.click(new SWTWidgetLocator(Label.class, "&Attributes"));
		if(!subsequent)
		ui.click(new SWTWidgetLocator(Label.class, "Methods"));
		TableItemLocator attributeNameInTable = new TableItemLocator("method0("
				+ name + "):" + name + "[0..1]");
		ui.click(attributeNameInTable);
		LabeledTextLocator type = new LabeledTextLocator("Type: ");
		assertEquals("Referenced type (Method return) not updated in Editor",
				newName, type.getText(ui));
		try {
			ui.click(new TableItemLocator("arg0: " + newName));
		} catch (WidgetNotFoundException wnfe) {
			fail("Referenced type (Method argument) not updated in Editor");
		}
		GuiUtils.maxminTab(ui, "*Ent3");
		
	}
	
	private static void checkNoErrorsFromTS(IUIContext ui, String test) throws Exception {
		ui.click(new CTabItemLocator("Error Log"));
		ui.enterText("org.eclipse.tigerstripe");
		TreeItemLocator errorLog = new TreeItemLocator("Internal Error",new ViewLocator("org.eclipse.pde.runtime.LogView"));
		try{
			ui.click(errorLog);
			if(errorLog != null){
				fail("Error logs from org.eclipse.tigerstripe following : "+test);
			}
		}catch(Exception noWidget){
			
		}
		
	}

	private static void clearErrorLog(IUIContext ui) throws Exception{
		BasePlugin.logErrorMessage("Select this to delete");
		ui.click(new CTabItemLocator("Error Log"));
		ui.contextClick(new TreeItemLocator("Select this to delete", new ViewLocator("org.eclipse.pde.runtime.LogView")), "Clear Log Viewer");
		//ui.wait(new ShellShowingCondition("Confirm Delete"));
		//ui.click(new ButtonLocator("OK"));
		//ui.wait(new ShellDisposedCondition("Confirm Delete"));
	}
	
	public static void checkPresentInDiagrams(IUIContext ui,String entityName,boolean dirty) throws Exception {
		LocatorHelper helper = new LocatorHelper();
		String d = "";
		if(dirty)
			d = "*";
		ui.click(new CTabItemLocator(d+"classDiagram.wvd"));
		String artifactPrefix = "";
		DeleteTests.internalCheckPresentInDiagram(ui, helper, artifactPrefix, artifactPrefix,
				entityName);
		ui.click(new CTabItemLocator(d+"instanceDiagram.wod"));
		DeleteTests.internalCheckPresentInInstanceDiagram(ui, helper, "Ent1:"+entityName);
	}
	
	public static void saveAndCloseRelatedEditors(IUIContext ui)
	throws Exception {
     // NOTE: The "own" editor gets closed by the rename action!
     for (String editor : editors) {
	 ui.click(new CTabItemLocator("*" + editor));
	 ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
	ui.close(new CTabItemLocator(editor));
       }
    }
	
	private static void openRelatedEditors(IUIContext ui) throws Exception {
		ViewLocator view = new ViewLocator("org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		ui.click(2, new TreeItemLocator(projectName+"/src/simple/Ent1", view));
		for(String editor : editors){
			ui.click(2, new TreeItemLocator(referenceProjectName+"/src/simple/"+editor,view));
		}
		
	}
	
	public static void doChangeByMove(IUIContext ui) throws Exception {
		ViewLocator view = new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		ui.contextClick(
				new TreeItemLocator(projectName + "/src/simple/Ent10", view),
				"Refactor Model/Move...");
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellShowingCondition("Move"));
		ui.click(new TreeItemLocator("model-refactoring/src/simple/moved"));
		ui.click(new ButtonLocator("Finish"));
		ui.wait(new ShellDisposedCondition("Move"));

	}

}
