package org.eclipse.tigerstripe.ui.visualeditor.test.diagram;



import org.eclipse.swt.widgets.Button;
import org.eclipse.tigerstripe.ui.visualeditor.test.suite.DiagramConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ProjectRecord;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.PullDownMenuItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class DnDDiagram extends UITestCaseSWT {

	
	/**
	 * Main test method.
	 */
	public void testCreateArtifacts() throws Exception {
		IUIContext ui = getUI();
		ui.contextClick(
						new TreeItemLocator(
								TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+TestingConstants.DIAGRAM_PACKAGE,
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
						"New/new Class Diagram...");
		ui.wait(new ShellShowingCondition("New Tigerstripe Diagram"));
		ui.enterText(DiagramConstants.DND_DIAGRAM);
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("New Tigerstripe Diagram"));
		
	
		// Create a couple of Entities and an Association between them
		// Do this in the explorer
		ProjectRecord.addArtifact(addArtifact(ui,"Entity", TestingConstants.ENTITY_NAMES[0], false, false, false, false));
		ProjectRecord.addArtifact(addArtifact(ui,"Entity", TestingConstants.ENTITY_NAMES[1], false, false, false, false));
		ProjectRecord.addArtifact(addArtifact(ui,"Association", TestingConstants.ASSOCIATION_NAMES[0], false, false,false, true));

		
	}

	
	/**
	 * This is a simple create using just the name.
	 * @throws Exception
	 */
	public String addArtifact(IUIContext ui,String myType, String thisArtifactName, boolean hasAttributes,
			boolean hasLiterals, boolean hasMethods, boolean hasEnds) throws Exception	{
		
		
		
		ui.click(new TreeItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME,
				new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui.click(new PullDownMenuItemLocator(myType,
				new ContributedToolItemLocator(
						"org.eclipse.tigerstripe.eclipse.newArtifactAction")));
		ui.wait(new ShellShowingCondition("New "+myType+" Artifact"));
		
		ui.enterText(thisArtifactName);
		
		
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
		
		// Close the editor
		ui.close(new CTabItemLocator(thisArtifactName));
		return thisArtifactName;
	}
	
}