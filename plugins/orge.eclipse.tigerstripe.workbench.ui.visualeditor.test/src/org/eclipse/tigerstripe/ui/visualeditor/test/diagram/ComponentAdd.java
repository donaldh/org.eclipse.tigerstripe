package org.eclipse.tigerstripe.ui.visualeditor.test.diagram;



import java.util.ArrayList;

import org.eclipse.tigerstripe.ui.visualeditor.test.finders.LocatorHelper;
import org.eclipse.tigerstripe.ui.visualeditor.test.suite.DiagramConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.gef.locator.PaletteItemLocator;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class ComponentAdd extends UITestCaseSWT {

	private IUIContext ui;
	
	private LocatorHelper helper;
	
	
	@Override
	protected void setUp() throws Exception {
		this.helper = new LocatorHelper();
		super.setUp();
	}

	public void tearDown() throws Exception{
		// Save the diagram - in case it hasn't already!
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
	}

	/**
	 * Main test method.
	 */
	public void testCreateComponents() throws Exception {
		 ui = getUI();
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
		
		
		// Close it
		ui.close(new CTabItemLocator(DiagramConstants.CREATE_DIAGRAM+".wvd"));
		
	}

	private void createAndCheckComponents(IUIContext ui, String entityName)  throws Exception {
		//Add some components to my Entities.
		
		//TEMP

		

		ArrayList<String> items = new ArrayList<String>();
		
		IWidgetLocator entity = this.helper.getManagedEntityLocator(ui,entityName);
		IWidgetLocator attributes = this.helper.getManagedEntityMethodCompartmentLocator(ui,entityName);
		
		ui.click(new PaletteItemLocator("Features/Field"));
		ui.click(attributes);
		items.add("attribute0:String");
		
		
		IWidgetLocator methods = this.helper.getManagedEntityMethodCompartmentLocator(ui,entityName);
		ui.click(new PaletteItemLocator("Features/Method"));
		ui.click(methods);
		items.add("method0():void");
		
		// Need to check they made it onto the diagram, then on to the explorer
		
		ArtifactHelper.checkItemsInExplorer(ui,
				TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+TestingConstants.DIAGRAM_PACKAGE,
				entityName,items);

		// Save the diagram
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));

	}
	
	private void createAndCheckLiterals(IUIContext ui, String enumName)  throws Exception {
		//Add some components to my Entities.

		IWidgetLocator entity = this.helper.getEnumerationLocator(ui,enumName);

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


	
}