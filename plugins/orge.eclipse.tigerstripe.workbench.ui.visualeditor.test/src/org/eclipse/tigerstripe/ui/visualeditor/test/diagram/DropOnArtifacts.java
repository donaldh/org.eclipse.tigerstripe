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

public class DropOnArtifacts extends UITestCaseSWT {
	
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
		ui.click(new CTabItemLocator("*"+DiagramConstants.DND_2_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));

	}

	private IUIContext ui;
	/**
	 * Main test method.
	 */
	public void testDropOnArtifacts() throws Exception {
		ui = getUI();
		
		String packagePath = (TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+"/"+TestingConstants.DIAGRAM_2_PACKAGE);
		
		String pathBase = TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+packagePath+"/";
		ViewLocator view = new ViewLocator("org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		
		String pathToItem = pathBase+TestingConstants.ENTITY_NAMES[1];
		TreeItemLocator treeItem = new TreeItemLocator(
					pathToItem,view);
		ui.click(treeItem);
		ui.dragTo(new XYLocator(new DiagramEditPart$1Locator(),100,100));
		assertNotNull("Entity Not found on Diagram after dragging",this.helper.getManagedEntityLocator(ui,TestingConstants.ENTITY_NAMES[1]));
		
		pathToItem = pathBase+TestingConstants.DATATYPE_NAMES[1];
		treeItem = new TreeItemLocator(
					pathToItem,view);
		ui.click(treeItem);
		ui.dragTo(new XYLocator(new DiagramEditPart$1Locator(),200,100));
		assertNotNull("Datatype Not found on Diagram after dragging",this.helper.getDatatypeLocator(ui,TestingConstants.DATATYPE_NAMES[1]));
		
		pathToItem = pathBase+TestingConstants.ENUMERATION_NAMES[1];
		treeItem = new TreeItemLocator(
					pathToItem,view);
		ui.click(treeItem);
		ui.dragTo(new XYLocator(new DiagramEditPart$1Locator(),300,100));
		assertNotNull("Enum Not found on Diagram after dragging",this.helper.getEnumerationLocator(ui,TestingConstants.ENUMERATION_NAMES[1]));
		
		pathToItem = pathBase+TestingConstants.QUERY_NAMES[1];
		treeItem = new TreeItemLocator(
					pathToItem,view);
		ui.click(treeItem);
		ui.dragTo(new XYLocator(new DiagramEditPart$1Locator(),100,200));
		assertNotNull("Query Not found on Diagram after dragging",this.helper.getQueryLocator(ui,TestingConstants.QUERY_NAMES[1]));
		
		
		pathToItem = pathBase+TestingConstants.UPDATE_NAMES[1];
		treeItem = new TreeItemLocator(
					pathToItem,view);
		ui.click(treeItem);
		ui.dragTo(new XYLocator(new DiagramEditPart$1Locator(),200,200));
		assertNotNull("Update Proc Not found on Diagram after dragging",this.helper.getUpdateProcedureLocator(ui,TestingConstants.UPDATE_NAMES[1]));
		
		pathToItem = pathBase+TestingConstants.EXCEPTION_NAMES[1];
		treeItem = new TreeItemLocator(
					pathToItem,view);
		ui.click(treeItem);
		ui.dragTo(new XYLocator(new DiagramEditPart$1Locator(),300,200));
		assertNotNull("Exception Not found on Diagram after dragging",this.helper.getExceptionLocator(ui,TestingConstants.EXCEPTION_NAMES[1]));
		
		pathToItem = pathBase+TestingConstants.EVENT_NAMES[1];
		treeItem = new TreeItemLocator(
					pathToItem,view);
		ui.click(treeItem);
		ui.dragTo(new XYLocator(new DiagramEditPart$1Locator(),100,300));
		assertNotNull("Event Not found on Diagram after dragging",this.helper.getNotificationLocator(ui,TestingConstants.EVENT_NAMES[1]));
		
		pathToItem = pathBase+TestingConstants.SESSION_NAMES[1];
		treeItem = new TreeItemLocator(
					pathToItem,view);
		ui.click(treeItem);
		ui.dragTo(new XYLocator(new DiagramEditPart$1Locator(),200,300));
		assertNotNull("Session Not found on Diagram after dragging ",this.helper.getSessionFacadeLocator(ui,TestingConstants.SESSION_NAMES[1]));
	
	
	}

}