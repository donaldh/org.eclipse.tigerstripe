package org.eclipse.tigerstripe.ui.visualeditor.test.diagram;

import org.eclipse.tigerstripe.ui.visualeditor.test.finders.LocatorHelper;
import org.eclipse.tigerstripe.ui.visualeditor.test.suite.DiagramConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.profile.CreateStereotypes;
import org.eclipse.tigerstripe.workbench.ui.base.test.profile.Explorer;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.NamedWidgetLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class StereotypeTests extends UITestCaseSWT {

private IUIContext ui;
	
	private LocatorHelper helper;
	
	@Override
	protected void setUp() throws Exception {
		this.helper = new LocatorHelper();
		super.setUp();
		ui = getUI();
		// Turn on the stereotype display in the explorer;
		Explorer.enable(ui);
		CreateStereotypes.createStereotypes(ui); 
		
	}

	public void tearDown() throws Exception{
		// Turn off the stereotype display in the explorer;
		Explorer.disable(ui);
		// Save the diagram - in case it hasn't already!
		try {
			ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
			ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		} catch (Exception e){
			// should be saved already! So this is a "just in case"
		}
	}

	/**
	 * Main test method.
	 */
	public void testCreateComponents() throws Exception {
		 
		// Open diagram
			ui.click(2,
					new TreeItemLocator(
							TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+
							TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+TestingConstants.DIAGRAM_PACKAGE+
							"/"+DiagramConstants.CREATE_DIAGRAM,
							new ViewLocator(
									"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
					
			datatypeAddSteroOnDiagram();	 
		 
	}
	
	private void datatypeAddSteroOnDiagram() throws Exception {
		IWidgetLocator dataTypeLocator = helper.getDatatypeLocator(ui,TestingConstants.DATATYPE_NAMES[2]);
		ui.contextClick(dataTypeLocator, "Edit Stereotypes");
		
		ui.click(new NamedWidgetLocator("Add_Stereo_Artifact"));
		ui.wait(new ShellShowingCondition("Stereotype Selection"));
		ui.click(2, new TableItemLocator(TestingConstants.DATATYPE_STEREO));
		ui.wait(new ShellDisposedCondition("Stereotype Selection"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		ui.close(new CTabItemLocator("Datatype2"));
		ui.click(new CTabItemLocator("*PaletteCreate.wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		IWidgetLocator datatypeStereoLocator = helper.getDatatypeStereoEditLocator(ui,TestingConstants.DATATYPE_NAMES[2],TestingConstants.DATATYPE_STEREO);
		assertNotNull(datatypeStereoLocator);
//		ui.click(datatypeStereoLocator);
		
		ui.click(new TreeItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+
				TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+TestingConstants.DIAGRAM_PACKAGE+
				"/<<"+TestingConstants.DATATYPE_STEREO+">>"+TestingConstants.DATATYPE_NAMES[2],
				new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		
		
	}
	
	
}
