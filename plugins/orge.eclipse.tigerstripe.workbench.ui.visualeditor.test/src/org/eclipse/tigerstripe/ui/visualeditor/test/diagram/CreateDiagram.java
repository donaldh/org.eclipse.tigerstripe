package org.eclipse.tigerstripe.ui.visualeditor.test.diagram;



import java.util.ArrayList;

import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.ui.visualeditor.test.finders.LocatorHelper;
import org.eclipse.tigerstripe.ui.visualeditor.test.suite.DiagramConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ProjectHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.gef.internal.locator.XYComparator;
import com.windowtester.runtime.gef.locator.FigureCanvasXYLocator;
import com.windowtester.runtime.gef.locator.FigureClassLocator;
import com.windowtester.runtime.gef.locator.PaletteItemLocator;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class CreateDiagram extends UITestCaseSWT {

	private static class DiagramEditPart$1Locator extends FigureClassLocator {
		private static final long serialVersionUID = 1L;

		public DiagramEditPart$1Locator() {
			super(
					"org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart$1");
		}
	}

private DiagramHelper helper;
	
	public void setUp() throws Exception{
		this.helper = new DiagramHelper();
	}
	
	/**
	 * Main test method.
	 */
	public void testCreateArtifacts() throws Exception {
		IUIContext ui = getUI();
		
		helper.createPackage(ui, TestingConstants.DIAGRAM_PACKAGE);
		
		String pckge = TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH;
		ui.contextClick(
						new TreeItemLocator(
								TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+pckge+"/"+TestingConstants.DIAGRAM_PACKAGE,
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
						"New/new Class Diagram...");
		ui.wait(new ShellShowingCondition("New Tigerstripe Diagram"));
		ui.enterText(DiagramConstants.CREATE_DIAGRAM);
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("New Tigerstripe Diagram"));
		
		// Make one of each - Need to position them carefully
		// And check it made it to the Explorer
		ArrayList<String> artifacts = new ArrayList<String>();
		
		int center = 500;
		int step = 50;
		
		ui.click(new PaletteItemLocator("Artifacts/Entity"));
		ui.click(new XYLocator(new DiagramEditPart$1Locator(),center,center));
		//ui.click(new DiagramEditPart$1Locator());
		ui.enterText(TestingConstants.ENTITY_NAMES[2]);
		artifacts.add(TestingConstants.ENTITY_NAMES[2]);
		
		
		ui.click(new PaletteItemLocator("Artifacts/Enumeration"));
		ui.click(new XYLocator(new DiagramEditPart$1Locator(),center-5*step,center-3*step));
		ui.enterText(TestingConstants.ENUMERATION_NAMES[2]);
		artifacts.add(TestingConstants.ENUMERATION_NAMES[2]);
		
		ui.click(new PaletteItemLocator("Artifacts/Datatype"));
		ui.click(new XYLocator(new DiagramEditPart$1Locator(),center-5*step,center));
		ui.enterText(TestingConstants.DATATYPE_NAMES[2]);
		artifacts.add(TestingConstants.DATATYPE_NAMES[2]);
		
		ui.click(new PaletteItemLocator("Artifacts/Query"));
		ui.click(new XYLocator(new DiagramEditPart$1Locator(),center+5*step,center));
		ui.enterText(TestingConstants.QUERY_NAMES[2]);
		artifacts.add(TestingConstants.QUERY_NAMES[2]);
		
		ui.click(new PaletteItemLocator("Artifacts/Update Procedure"));
		ui.click(new XYLocator(new DiagramEditPart$1Locator(),center-5*step,center-6*step));
		ui.enterText(TestingConstants.UPDATE_NAMES[2]);
		artifacts.add(TestingConstants.UPDATE_NAMES[2]);
		
		ui.click(new PaletteItemLocator("Artifacts/Event"));
		ui.click(new XYLocator(new DiagramEditPart$1Locator(),center,center-6*step));
		ui.enterText(TestingConstants.EVENT_NAMES[2]);
		artifacts.add(TestingConstants.EVENT_NAMES[2]);
		
		ui.click(new PaletteItemLocator("Artifacts/Exception"));
		ui.click(new XYLocator(new DiagramEditPart$1Locator(),center+5*step,center-6*step));
		ui.enterText(TestingConstants.EXCEPTION_NAMES[2]);
		artifacts.add(TestingConstants.EXCEPTION_NAMES[2]);
		
		ui.click(new PaletteItemLocator("Artifacts/Session Facade"));
		ui.click(new XYLocator(new DiagramEditPart$1Locator(),center+5*step,center-3*step));
		ui.enterText(TestingConstants.SESSION_NAMES[2]);
		artifacts.add(TestingConstants.SESSION_NAMES[2]);
		
		
		// Now check they are all in the tree view
		ProjectHelper.checkArtifactsInExplorer(ui, TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+TestingConstants.DIAGRAM_PACKAGE, artifacts);
		
		
		// Save it
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		
		// Close it
		ui.close(new CTabItemLocator(DiagramConstants.CREATE_DIAGRAM+".wvd"));
		
	}	
	
}