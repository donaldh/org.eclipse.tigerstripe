package org.eclipse.tigerstripe.ui.visualeditor.test.diagram;



import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.tigerstripe.ui.visualeditor.test.suite.DiagramConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ProjectRecord;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
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

public class DnDArtifactsDiagram extends UITestCaseSWT {

	private DiagramHelper helper;
	
	public void setUp() throws Exception{
		this.helper = new DiagramHelper();
	}
	
	/**
	 * Main test method.
	 */
	public void testCreateArtifacts() throws Exception {
		
		IUIContext ui = getUI();
		
		String packageName = TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+TestingConstants.DIAGRAM_2_PACKAGE;
		
		helper.createPackage(ui, TestingConstants.DIAGRAM_2_PACKAGE);
		
		ui.contextClick(
						new TreeItemLocator(
								TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+packageName,
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
						"New/new Class Diagram...");
		ui.wait(new ShellShowingCondition("New Tigerstripe Diagram"));
		ui.enterText(DiagramConstants.DND_2_DIAGRAM);
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("New Tigerstripe Diagram"));
		
	
		// Create one of each thing (Nodes only at this point)
		// Do this in the explorer
		ProjectRecord.addArtifact(helper.addArtifact(ui,"Entity", packageName, TestingConstants.ENTITY_NAMES[1], false, false, false, false));
		ProjectRecord.addArtifact(helper.addArtifact(ui,"Datatype", packageName,TestingConstants.DATATYPE_NAMES[1], true, true, true, false));
		ProjectRecord.addArtifact(helper.addArtifact(ui,"Enumeration", packageName,TestingConstants.ENUMERATION_NAMES[1], false, true, false, false));
		ProjectRecord.addArtifact(helper.addArtifact(ui,"Query", packageName,TestingConstants.QUERY_NAMES[1], true, true, false, false));
		ProjectRecord.addArtifact(helper.addArtifact(ui,"Update Procedure", packageName,TestingConstants.UPDATE_NAMES[1], true, true, false, false));
		ProjectRecord.addArtifact(helper.addArtifact(ui,"Exception", packageName,TestingConstants.EXCEPTION_NAMES[1], true, false, false, false));
		ProjectRecord.addArtifact(helper.addArtifact(ui,"Event", packageName,TestingConstants.EVENT_NAMES[1], true, true, false, false));
		ProjectRecord.addArtifact(helper.addArtifact(ui,"Session Facade", packageName,TestingConstants.SESSION_NAMES[1], false, false,true, false));
		
		
		
	}

}