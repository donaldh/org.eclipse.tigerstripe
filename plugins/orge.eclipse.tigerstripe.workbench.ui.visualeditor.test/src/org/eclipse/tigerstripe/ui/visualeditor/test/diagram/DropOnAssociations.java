package org.eclipse.tigerstripe.ui.visualeditor.test.diagram;

import org.eclipse.tigerstripe.ui.visualeditor.test.finders.LocatorHelper;
import org.eclipse.tigerstripe.ui.visualeditor.test.suite.DiagramConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WT;
import com.windowtester.runtime.WidgetNotFoundException;
import com.windowtester.runtime.gef.locator.FigureClassLocator;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class DropOnAssociations extends UITestCaseSWT {
	
	private static class DiagramEditPart$1Locator extends FigureClassLocator {
		private static final long serialVersionUID = 1L;

		public DiagramEditPart$1Locator() {
			super(
					"org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart$1");
		}
	}
	
	private LocatorHelper helper;
	
	
	@Override
	protected void setUp() throws Exception {
		this.helper = new LocatorHelper();
		super.setUp();
	}

	public void tearDown() throws Exception {
		// Save it
		ui.click(new CTabItemLocator("*"+DiagramConstants.DND_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));

	}

	private IUIContext ui;
	/**
	 * Main test method.
	 */
	public void testDropOnAssociations() throws Exception {
		ui = getUI();
		
		String packagePath = (TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+TestingConstants.DIAGRAM_3_PACKAGE).replaceAll("\\.", "/");
		
		String pathToItem = TestingConstants.NEW_MODEL_PROJECT_NAME+
			"/src/"+
			packagePath+"/"+
			TestingConstants.ENTITY_NAMES[3];
	
		TreeItemLocator treeItem = new TreeItemLocator(
					pathToItem,
				new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
		ui.click(treeItem);
		pathToItem = TestingConstants.NEW_MODEL_PROJECT_NAME+
			"/src/"+packagePath+"/"+TestingConstants.ENTITY_NAMES[4];
		treeItem = new TreeItemLocator(
				pathToItem,
			new ViewLocator(
					"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
		
		ui.click(1,treeItem,WT.CTRL);
		ui.dragTo(new XYLocator(new DiagramEditPart$1Locator(),100,100));
		
		// Firstly check for the two Entities,and their Associations
		assertNotNull( "Entity Not found on Diagram after dragging",this.helper.getManagedEntityLocator(ui,TestingConstants.ENTITY_NAMES[3]));
		assertNotNull( "Entity Not found on Diagram after dragging",this.helper.getManagedEntityLocator(ui,TestingConstants.ENTITY_NAMES[4]));
		
		assertNotNull( "Association Not found on Diagram after dragging associated Entities",this.helper.getAssociationFigureLocator(ui,TestingConstants.ASSOCIATION_NAMES[0]));		
		assertNotNull( "Association Not found on Diagram after dragging associated Entities",this.helper.getAssociationFigureLocator(ui,TestingConstants.ASSOCIATION_NAMES[1]));
		
		// Now make sure the non - interesting ones are NOT there

		IWidgetLocator entityLoc = this.helper.getManagedEntityLocator(ui,TestingConstants.ENTITY_NAMES[5]);
		assertNull("Found extra Entity on Diagram", entityLoc);
		IWidgetLocator assocLoc = this.helper.getAssociationFigureLocator(ui,TestingConstants.ASSOCIATION_NAMES[2]);
		assertNull("Found extra Association on Diagram",assocLoc);
	
		
		pathToItem = TestingConstants.NEW_MODEL_PROJECT_NAME+
			"/src/"+packagePath+"/"+TestingConstants.DATATYPE_NAMES[3];
		treeItem = new TreeItemLocator(
				pathToItem,
				new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));

		ui.click(treeItem);

		pathToItem = TestingConstants.NEW_MODEL_PROJECT_NAME+
			"/src/"+packagePath+"/"+TestingConstants.DATATYPE_NAMES[4];
		treeItem = new TreeItemLocator(
				pathToItem,
				new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
	
		ui.click(1,treeItem,WT.CTRL);
		ui.dragTo(new XYLocator(new DiagramEditPart$1Locator(),100,400));
		
		
		assertNotNull( "Datatype Not found on Diagram after dragging",this.helper.getDatatypeLocator(ui,TestingConstants.DATATYPE_NAMES[3]));
		assertNotNull( "Datatype Not found on Diagram after dragging",this.helper.getDatatypeLocator(ui,TestingConstants.DATATYPE_NAMES[4]));
		
		assertNotNull( "Dependency Not found on Diagram after dragging associated Datatypes",this.helper.getDependencyFigureLocator(ui,TestingConstants.DEPENDENCY_NAMES[0]));		
		
		assertNotNull( "Association Class Not found on Diagram after dragging associated Entity & Datatype",this.helper.getAssociationClassFigureLocator(ui,TestingConstants.ASSOCIATION_CLASS_NAMES[0]));		
		
		
	}

}