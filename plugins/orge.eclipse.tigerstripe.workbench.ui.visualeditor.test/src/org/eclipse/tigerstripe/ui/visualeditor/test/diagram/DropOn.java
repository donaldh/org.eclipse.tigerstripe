package org.eclipse.tigerstripe.ui.visualeditor.test.diagram;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WT;
import com.windowtester.runtime.gef.locator.FigureClassLocator;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
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
		ui
				.click(new TreeItemLocator(
						"New Project/src/org.eclipse/Entity2",
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui
				.click(
						1,
						new TreeItemLocator(
								"New Project/src/org.eclipse/Entity1",
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
						WT.CTRL);
		ui.dragTo(new XYLocator(new DiagramEditPart$1Locator(),200,200));
	}

}