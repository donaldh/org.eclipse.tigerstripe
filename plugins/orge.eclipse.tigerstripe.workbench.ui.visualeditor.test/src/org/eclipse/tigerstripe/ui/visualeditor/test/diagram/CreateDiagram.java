package org.eclipse.tigerstripe.ui.visualeditor.test.diagram;



import java.util.ArrayList;

import org.eclipse.tigerstripe.ui.visualeditor.test.finders.LocatorFactory;
import org.eclipse.tigerstripe.ui.visualeditor.test.suite.DiagramConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ProjectHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.gef.locator.FigureClassLocator;
import com.windowtester.runtime.gef.locator.PaletteItemLocator;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
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
		ui.enterText(DiagramConstants.CREATE_DIAGRAM);
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("New Tigerstripe Diagram"));
		
		// Make one of each - Need to position them carefully
		// And check it made it to the Explorer
		ArrayList<String> artifacts = new ArrayList<String>();
		
		ui.click(new PaletteItemLocator("Artifacts/Entity"));
		ui.click(new XYLocator(new DiagramEditPart$1Locator(),0,-100));
		ui.enterText(TestingConstants.ENTITY_NAMES[2]);
		artifacts.add(TestingConstants.ENTITY_NAMES[2]);
		
		ui.click(new PaletteItemLocator("Artifacts/Datatype"));
		ui.click(new XYLocator(new DiagramEditPart$1Locator(),-200,0));
		ui.enterText(TestingConstants.DATATYPE_NAMES[2]);
		artifacts.add(TestingConstants.DATATYPE_NAMES[2]);
		
		ui.click(new PaletteItemLocator("Artifacts/Enumeration"));
		ui.click(new XYLocator(new DiagramEditPart$1Locator(),0,0));
		ui.enterText(TestingConstants.ENUMERATION_NAMES[2]);
		artifacts.add(TestingConstants.ENUMERATION_NAMES[2]);
		
		ui.click(new PaletteItemLocator("Artifacts/Query"));
		ui.click(new XYLocator(new DiagramEditPart$1Locator(),200,0));
		ui.enterText(TestingConstants.QUERY_NAMES[2]);
		artifacts.add(TestingConstants.QUERY_NAMES[2]);
		
		ui.click(new PaletteItemLocator("Artifacts/Update Procedure"));
		ui.click(new XYLocator(new DiagramEditPart$1Locator(),-200,100));
		ui.enterText(TestingConstants.UPDATE_NAMES[2]);
		artifacts.add(TestingConstants.UPDATE_NAMES[2]);
		
		ui.click(new PaletteItemLocator("Artifacts/Event"));
		ui.click(new XYLocator(new DiagramEditPart$1Locator(),0,100));
		ui.enterText(TestingConstants.EVENT_NAMES[2]);
		artifacts.add(TestingConstants.EVENT_NAMES[2]);
		
		ui.click(new PaletteItemLocator("Artifacts/Exception"));
		ui.click(new XYLocator(new DiagramEditPart$1Locator(),200,100));
		ui.enterText(TestingConstants.EXCEPTION_NAMES[2]);
		artifacts.add(TestingConstants.EXCEPTION_NAMES[2]);
		
		ui.click(new PaletteItemLocator("Artifacts/Session Facade"));
		ui.click(new XYLocator(new DiagramEditPart$1Locator(),200,-100));
		ui.enterText(TestingConstants.SESSION_NAMES[2]);
		artifacts.add(TestingConstants.SESSION_NAMES[2]);
		
		
		// Now check they are all in the tree view
		ProjectHelper.checkArtifactsInExplorer(ui, TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+TestingConstants.DIAGRAM_PACKAGE, artifacts);
		
		
		// Save it
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		createAndCheckComponents(ui, TestingConstants.ENTITY_NAMES[2]);
		createAndCheckLiterals(ui, TestingConstants.ENUMERATION_NAMES[2]);
		
		// Close it
		ui.close(new CTabItemLocator(DiagramConstants.CREATE_DIAGRAM+".wvd"));
		
	}

	private void createAndCheckComponents(IUIContext ui, String entityName)  throws Exception {
		//Add some components to my Entities.

		IWidgetLocator entity = LocatorFactory.getInstance().getManagedEntityLocator(ui,entityName);

		ArrayList<String> items = new ArrayList<String>();
		ui.click(new PaletteItemLocator("Features/Field"));
		ui.click(entity);
		items.add("attribute0::String");

		ui.click(new PaletteItemLocator("Features/Method"));
		ui.click(entity);

		items.add("method0()::void");
		ArtifactHelper.checkItemsInExplorer(ui,
				TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+TestingConstants.DIAGRAM_PACKAGE,
				entityName,items);

		// Save the diagram
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));

	}
	
	private void createAndCheckLiterals(IUIContext ui, String enumName)  throws Exception {
		//Add some components to my Entities.

		IWidgetLocator entity = LocatorFactory.getInstance().getEnumerationLocator(ui,enumName);

		ArrayList<String> items = new ArrayList<String>();
		ui.click(new PaletteItemLocator("Features/Literal"));
		ui.click(entity);
		items.add("literal0=0");


		ArtifactHelper.checkItemsInExplorer(ui,
				TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+TestingConstants.DIAGRAM_PACKAGE,
				enumName,items);

		// Save the diagram
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));

		// Now check the Explorer.
	}


	/**
	 * Add stuff in the editor and make sure it gets to the diagram.
	 * 
	 * @param ui
	 * @param datatypeName
	 */
	private void editorCreateComponents(IUIContext ui, String datatypeName) throws Exception {
		ArrayList<String> items = new ArrayList<String>(); 
		items.add(ArtifactHelper.newAttribute(ui, datatypeName, TestingConstants.ATTRIBUTE_NAMES[1]));
		IWidgetLocator datatype = LocatorFactory.getInstance().getEnumerationLocator(ui,datatypeName);
		
		//TODO FInd the Attrubute Compartment for the Datatype, and get the contents!
		
		
		
	}
}