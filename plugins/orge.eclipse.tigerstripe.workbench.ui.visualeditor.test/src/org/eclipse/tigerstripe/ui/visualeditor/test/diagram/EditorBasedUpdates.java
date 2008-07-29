package org.eclipse.tigerstripe.ui.visualeditor.test.diagram;



import java.util.ArrayList;

import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.ui.visualeditor.test.finders.LocatorFactory;
import org.eclipse.tigerstripe.ui.visualeditor.test.suite.DiagramConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ProjectHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

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
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class EditorBasedUpdates extends UITestCaseSWT {


	/**
	 * Main test method.
	 */
	public void testCreateComponents() throws Exception {
		IUIContext ui = getUI();
		// Open diagram
		ui.click(2,
				new TreeItemLocator(
						TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+
						TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+TestingConstants.DIAGRAM_PACKAGE+
						"/"+DiagramConstants.CREATE_DIAGRAM,
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		
		// basic add on diagram
		createAndCheckComponents(ui, TestingConstants.ENTITY_NAMES[2]);
		createAndCheckLiterals(ui, TestingConstants.ENUMERATION_NAMES[2]);
		
		
		editorCreateAttribute(ui, TestingConstants.ENTITY_NAMES[2], DiagramConstants.ATTRIBUTE_NAMES[0]);
		editorCreateLiteral(ui, TestingConstants.ENUMERATION_NAMES[2], DiagramConstants.LITERAL_NAMES[0]);
		editorCreateMethod(ui, TestingConstants.ENTITY_NAMES[2], DiagramConstants.METHOD_NAMES[0]);
		
		addStereotypeToAttribute(ui, TestingConstants.ENTITY_NAMES[2], DiagramConstants.ATTRIBUTE_NAMES[0]);
		
		// Close it
		ui.close(new CTabItemLocator(DiagramConstants.CREATE_DIAGRAM+".wvd"));
		
	}

	private void createAndCheckComponents(IUIContext ui, String entityName)  throws Exception {
		//Add some components to my Entities.

		IWidgetLocator entity = LocatorFactory.getInstance().getManagedEntityLocator(ui,entityName);

		ArrayList<String> items = new ArrayList<String>();
		ui.click(new PaletteItemLocator("Features/Field"));
		ui.click(entity);
		items.add("attribute0:String");

		ui.click(new PaletteItemLocator("Features/Method"));
		ui.click(entity);

		items.add("method0():void");
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
	 * @param entityName
	 */
	private void editorCreateAttribute(IUIContext ui, String entityName, String attributeName) throws Exception {
		ArrayList<String> items = new ArrayList<String>();
		
		IWidgetLocator entity = LocatorFactory.getInstance().getManagedEntityLocator(ui, entityName);

		ui.contextClick(entity, "Open in Editor");
		GuiUtils.maxminTab(ui, entityName);
		SWTWidgetLocator attributesSection = new SWTWidgetLocator(Label.class, "&Attributes");
		ui.click(attributesSection);
		items.add(ArtifactHelper.newAttribute(ui, entityName, attributeName));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		String attributeText = LocatorFactory.getInstance().getFieldString(ui,entityName,attributeName);
		assertNotNull("Did not find attribute on diagram.", attributeText);
		GuiUtils.maxminTab(ui, entityName);
		ui.close(new CTabItemLocator(entityName));
		
	}
	
	/**
	 * Add stuff in the editor and make sure it gets to the diagram.
	 * 
	 * @param ui
	 * @param enumName
	 */
	private void editorCreateLiteral(IUIContext ui, String enumName, String literalName) throws Exception {
		ArrayList<String> items = new ArrayList<String>();
		
		IWidgetLocator entity = LocatorFactory.getInstance().getEnumerationLocator(ui, enumName);

		ui.contextClick(entity, "Open in Editor");
		GuiUtils.maxminTab(ui, enumName);
		SWTWidgetLocator constantsSection = new SWTWidgetLocator(Label.class, "Constants");
		ui.click(constantsSection);
		items.add(ArtifactHelper.newLiteral(ui, enumName, literalName));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		String literalText = LocatorFactory.getInstance().getLiteralString(ui,enumName,literalName);
		assertNotNull("Did not find literal on diagram.", literalText);
		GuiUtils.maxminTab(ui, enumName);
		ui.close(new CTabItemLocator(enumName));
	}
	
	/**
	 * Add stuff in the editor and make sure it gets to the diagram.
	 * 
	 * @param ui
	 * @param enumName
	 */
	private void editorCreateMethod(IUIContext ui, String entityName, String methodName) throws Exception {
		ArrayList<String> items = new ArrayList<String>();
		
		IWidgetLocator entity = LocatorFactory.getInstance().getManagedEntityLocator(ui, entityName);

		ui.contextClick(entity, "Open in Editor");
		GuiUtils.maxminTab(ui, entityName);
		// Note this is Attributes ON PURPOSE to close the default open section!
		SWTWidgetLocator attributesSection = new SWTWidgetLocator(Label.class, "&Attributes");
		ui.click(attributesSection);
		items.add(ArtifactHelper.newMethod(ui, entityName, methodName));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		String attributeText = LocatorFactory.getInstance().getMethodString(ui,entityName,methodName);
		assertNotNull("Did not find method on diagram.", attributeText);
		GuiUtils.maxminTab(ui, entityName);
		ui.close(new CTabItemLocator(entityName));
	}
	
	private void addStereotypeToAttribute(IUIContext ui, String entityName, String methodName) throws Exception {
		
	}
}