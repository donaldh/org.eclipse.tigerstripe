package org.eclipse.tigerstripe.ui.visualeditor.test.diagram;



import java.util.ArrayList;

import org.eclipse.tigerstripe.ui.visualeditor.test.suite.DiagramConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ProjectHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute3EditPart;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.gef.IFigureReference;
import com.windowtester.runtime.gef.locator.FigureClassLocator;
import com.windowtester.runtime.gef.locator.LRLocator;
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

public class ComponentsDiagram extends UITestCaseSWT {

	private static class DiagramEditPart$1Locator extends FigureClassLocator {
		private static final long serialVersionUID = 1L;

		public DiagramEditPart$1Locator() {
			super(
					"org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart$1");
		}
	}

	private static class WrapLabelLocator extends FigureClassLocator {
		private static final long serialVersionUID = 1L;

		public WrapLabelLocator() {
			super("org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel");
		}
	}

	private static class MyFigureLocator extends FigureClassLocator {
		private static final long serialVersionUID = 1L;

		public MyFigureLocator() {
			super("org.eclipse.draw2d.Figure");
		}
	}
	
	private static class AttributeFinder  {
		
		private String text;
		
		public AttributeFinder (IUIContext ui) {
			IWidgetLocator[] matches = ui.findAll(new FigureClassLocator("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute3EditPart$AttributeLabelFigure"));
			for (IWidgetLocator match : matches) {
			  Attribute3EditPart.AttributeLabelFigure attrib = (Attribute3EditPart.AttributeLabelFigure) ((IFigureReference) match).getFigure();
			  setText(attrib.getText());
			}
		}

		public String getText() {
			return text;
		}

		private void setText(String text) {
			this.text = text;
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
		ui.enterText(DiagramConstants.COMPONENTS_DIAGRAM);
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("New Tigerstripe Diagram"));
		
		ArrayList<String> artifacts = new ArrayList<String>();
		
		ui.click(new PaletteItemLocator("Artifacts/Entity"));
		ui.click(new XYLocator(new DiagramEditPart$1Locator(),0,-100));
		ui.enterText(TestingConstants.ENTITY_NAMES[3]);
		artifacts.add(TestingConstants.ENTITY_NAMES[3]);
		
		
		// Now check they are all in the tree view
		ProjectHelper.checkArtifactsInExplorer(ui, TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+TestingConstants.DIAGRAM_PACKAGE, artifacts);
		
		
		// Save the diagram
		ui.click(new CTabItemLocator("*"+DiagramConstants.COMPONENTS_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
//		ui.click(new LRLocator(0, new WrapLabelLocator()));
//		ui.click(new LRLocator(7, new MyFigureLocator()));
//		ui.click(new LRLocator(8, new MyFigureLocator()));
//		ui
//				.click(new TreeItemLocator(
//						"New Project/src/org.eclipse/Entity3",
//						new ViewLocator(
//								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
//		ui.click(new PaletteItemLocator("Features/Field"));
//		ui.click(new LRLocator(0, new WrapLabelLocator()));
//		ui.click(new PaletteItemLocator("Features/Field"));
//		ui.click(new LRLocator(0, new WrapLabelLocator()));
//		ui.click(new PaletteItemLocator("Features/Method"));
//		ui.click(new LRLocator(8, new MyFigureLocator()));
//		ui
//				.click(new TreeItemLocator(
//						"New Project/src/org.eclipse/Entity3/AFIELD::String",
//						new ViewLocator(
//								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
//		ui
//				.click(new TreeItemLocator(
//						"New Project/src/org.eclipse/Entity3/method0()::void",
//						new ViewLocator(
//								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		
		// Save it
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
//		IWidgetLocator[] matches = ui.findAll(new FigureClassLocator("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute3EditPart$AttributeLabelFigure"));
//		for (IWidgetLocator match : matches) {
//		  new com.windowtester.runtime.gef.internal.helpers.GEFDebugHelper().printFigures(((IFigureReference)match).getFigure());
//		}
		
		AttributeFinder finder = new AttributeFinder(ui);
		finder.getText();
		
		// Close it
		ui.close(new CTabItemLocator(DiagramConstants.COMPONENTS_DIAGRAM+".wvd"));
		
	}

}