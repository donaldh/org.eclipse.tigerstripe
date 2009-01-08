package org.eclipse.tigerstripe.ui.visualeditor.test.diagram;



import org.eclipse.tigerstripe.ui.visualeditor.test.suite.DiagramConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ProjectRecord;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class DnDAssociationsDiagram extends UITestCaseSWT {

	private DiagramHelper helper;
	
	public void setUp() throws Exception{
		this.helper = new DiagramHelper();
	}
	
	/**
	 * Main test method.
	 */
	public void testCreateArtifacts() throws Exception {
		
		IUIContext ui = getUI();
		
		String packageName = TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+TestingConstants.DIAGRAM_3_PACKAGE;
		helper.createPackage(ui, TestingConstants.DIAGRAM_3_PACKAGE);
		ui.contextClick(
						new TreeItemLocator(
								TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+packageName,
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
						"New/new Class Diagram...");
		ui.wait(new ShellShowingCondition("New Tigerstripe Diagram"));
		ui.enterText(DiagramConstants.DND_DIAGRAM);
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("New Tigerstripe Diagram"));
		
	
		// Create a couple of Entities and an Association between them
		// Do this in the explorer
		ProjectRecord.addArtifact(helper.addArtifact(ui,"Entity", packageName, TestingConstants.ENTITY_NAMES[3], false, false, false, false));
		ProjectRecord.addArtifact(helper.addArtifact(ui,"Entity", packageName,TestingConstants.ENTITY_NAMES[4], false, false, false, false));
		ProjectRecord.addArtifact(helper.addArtifact(ui,"Association", packageName,TestingConstants.ASSOCIATION_NAMES[0], false, false,false, true,
				packageName+"."+TestingConstants.ENTITY_NAMES[3],packageName+"."+TestingConstants.ENTITY_NAMES[4]));
		ProjectRecord.addArtifact(helper.addArtifact(ui,"Association", packageName,TestingConstants.ASSOCIATION_NAMES[1], false, false,false, true, 
				packageName+"."+TestingConstants.ENTITY_NAMES[3],packageName+"."+TestingConstants.ENTITY_NAMES[4]));

		// These should not be on the diagram! Is this really likely?
		ProjectRecord.addArtifact(helper.addArtifact(ui,"Entity", packageName,TestingConstants.ENTITY_NAMES[5], false, false, false, false));
		ProjectRecord.addArtifact(helper.addArtifact(ui,"Association", packageName,TestingConstants.ASSOCIATION_NAMES[2], false, false,false, true,
				packageName+"."+TestingConstants.ENTITY_NAMES[3],packageName+"."+TestingConstants.ENTITY_NAMES[5]));
		
		
		ProjectRecord.addArtifact(helper.addArtifact(ui,"Datatype", packageName, TestingConstants.DATATYPE_NAMES[3], false, false, false, false));
		ProjectRecord.addArtifact(helper.addArtifact(ui,"Datatype", packageName,TestingConstants.DATATYPE_NAMES[4], false, false, false, false));
		ProjectRecord.addArtifact(helper.addArtifact(ui,"Dependency", packageName,TestingConstants.DEPENDENCY_NAMES[0], false, false,false, true,
				packageName+"."+TestingConstants.DATATYPE_NAMES[3],packageName+"."+TestingConstants.DATATYPE_NAMES[4]));
		
		
		ProjectRecord.addArtifact(helper.addArtifact(ui,"Association Class", packageName,TestingConstants.ASSOCIATION_CLASS_NAMES[0], false, false,false, true,
				packageName+"."+TestingConstants.ENTITY_NAMES[4],packageName+"."+TestingConstants.DATATYPE_NAMES[4]));
		
		
	}
	
}