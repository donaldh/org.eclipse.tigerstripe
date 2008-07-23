package org.eclipse.tigerstripe.ui.visualeditor.test.diagram;

import org.eclipse.tigerstripe.ui.visualeditor.test.suite.DiagramConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WT;
import com.windowtester.runtime.gef.locator.FigureClassLocator;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class DropOn extends UITestCaseSWT {

	private static class CompartmentCollapseHandleLocator extends FigureClassLocator {
		private static final long serialVersionUID = 1L;

		public CompartmentCollapseHandleLocator() {
			super(
					"org.eclipse.gmf.runtime.diagram.ui.handles.CompartmentCollapseHandle");
		}
	}
	
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
	public void testDropOn() throws Exception {
		IUIContext ui = getUI();
		try {
		ui
				.click(new TreeItemLocator(
						"New Project/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE+
						"/"+TestingConstants.ENTITY_NAMES[0],
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
//		ui
//				.click(
//						1,
//						new TreeItemLocator(
//								"New Project/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE+
//								"/"+TestingConstants.ENTITY_NAMES[1],
//								new ViewLocator(
//										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
//						WT.CTRL);
		ui.dragTo(new XYLocator(new DiagramEditPart$1Locator(),200,200));
		// Save it
		ui.click(new CTabItemLocator("*"+DiagramConstants.DND_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}

}