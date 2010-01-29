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
		// Open diagram
		ui.click(2,
				new TreeItemLocator(
						TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+
						TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+"/"+TestingConstants.DIAGRAM_PACKAGE+
						"/"+DiagramConstants.CREATE_DIAGRAM,
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
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

	
	public void testDatatypeAddSteroOnDiagram() throws Exception {
		ui.click(new CTabItemLocator(DiagramConstants.CREATE_DIAGRAM+".wvd"));
		
		IWidgetLocator dataTypeLocator = helper.getDatatypeLocator(ui,TestingConstants.DATATYPE_NAMES[2]);
		ui.contextClick(dataTypeLocator, "Edit Stereotypes");
		
		ui.click(new NamedWidgetLocator("Add_Stereo_Artifact"));
		ui.wait(new ShellShowingCondition("Stereotype Selection"));
		ui.click(2, new TableItemLocator(TestingConstants.DATATYPE_STEREO));
		ui.wait(new ShellDisposedCondition("Stereotype Selection"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		ui.close(new CTabItemLocator(TestingConstants.DATATYPE_NAMES[2]));
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		IWidgetLocator datatypeStereoLocator = helper.getDatatypeStereoEditLocator(ui,TestingConstants.DATATYPE_NAMES[2],TestingConstants.DATATYPE_STEREO);
		assertNotNull(datatypeStereoLocator);
//		ui.click(datatypeStereoLocator);
		
		ui.click(new TreeItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+
				TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+"/"+TestingConstants.DIAGRAM_PACKAGE+
				"/<<"+TestingConstants.DATATYPE_STEREO+">>"+TestingConstants.DATATYPE_NAMES[2],
				new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		
		
	}
	
	public void testMEAttributeAddSteroOnDiagram() throws Exception {
		ui.click(new CTabItemLocator(DiagramConstants.CREATE_DIAGRAM+".wvd"));
		
		IWidgetLocator attributeLocator = helper.getManagedEntityAttributeLocator(ui, 
				TestingConstants.ENTITY_NAMES[2], TestingConstants.ATTRIBUTE_NAMES[2]);
		ui.contextClick(attributeLocator, "Edit Stereotypes");
		
		ui.click(new NamedWidgetLocator("Add_Stereo_Attribute"));
		ui.wait(new ShellShowingCondition("Stereotype Selection"));
		ui.click(2, new TableItemLocator(TestingConstants.ATTRIBUTE_STEREO));
		ui.wait(new ShellDisposedCondition("Stereotype Selection"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		ui.close(new CTabItemLocator(TestingConstants.ENTITY_NAMES[2]));
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		IWidgetLocator attributeStereoLocator = helper.getManagedEntityAttributeLocator(ui, 
				TestingConstants.ENTITY_NAMES[2], TestingConstants.ATTRIBUTE_NAMES[2],TestingConstants.ATTRIBUTE_STEREO);
		assertNotNull(attributeStereoLocator);
//		ui.click(attributeStereoLocator);
		
		ui.click(new TreeItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+
				TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+"/"+TestingConstants.DIAGRAM_PACKAGE+
				TestingConstants.ENTITY_NAMES[2]+"/"+
				"<<"+TestingConstants.ATTRIBUTE_STEREO+">>"+TestingConstants.ATTRIBUTE_NAMES[2]+".*",
				new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		
		
	}
	
	
	public void testMEMethodAddSteroOnDiagram() throws Exception {
		ui.click(new CTabItemLocator(DiagramConstants.CREATE_DIAGRAM+".wvd"));
		
		IWidgetLocator methodLocator = helper.getManagedEntityMethodLocator(ui, 
				TestingConstants.ENTITY_NAMES[2], TestingConstants.METHOD_NAMES[2]);
		ui.contextClick(methodLocator, "Edit Stereotypes");
		
		ui.click(new NamedWidgetLocator("Add_Stereo_Method"));
		ui.wait(new ShellShowingCondition("Stereotype Selection"));
		ui.click(2, new TableItemLocator(TestingConstants.METHOD_STEREO));
		ui.wait(new ShellDisposedCondition("Stereotype Selection"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		ui.close(new CTabItemLocator(TestingConstants.ENTITY_NAMES[2]));
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		IWidgetLocator methodStereoLocator = helper.getManagedEntityAttributeLocator(ui, 
				TestingConstants.ENTITY_NAMES[2], TestingConstants.METHOD_NAMES[2],TestingConstants.METHOD_STEREO);
		assertNotNull(methodStereoLocator);
//		ui.click(attributeStereoLocator);
		
		ui.click(new TreeItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+
				TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+"/"+TestingConstants.DIAGRAM_PACKAGE+
				TestingConstants.ENTITY_NAMES[2]+"/"+
				"<<"+TestingConstants.METHOD_STEREO+">>"+TestingConstants.METHOD_NAMES[2]+".*",
				new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		
		
	}
	
	
	public void testEnumLiteralAddSteroOnDiagram() throws Exception {
		ui.click(new CTabItemLocator(DiagramConstants.CREATE_DIAGRAM+".wvd"));
		
		IWidgetLocator literalLocator = helper.getEnumerationLiteralLocator(ui, 
				TestingConstants.ENUMERATION_NAMES[2], TestingConstants.LITERAL_NAMES[2]);
		ui.contextClick(literalLocator, "Edit Stereotypes");
		
		ui.click(new NamedWidgetLocator("Add_Stereo_Literal"));
		ui.wait(new ShellShowingCondition("Stereotype Selection"));
		ui.click(2, new TableItemLocator(TestingConstants.LITERAL_STEREO));
		ui.wait(new ShellDisposedCondition("Stereotype Selection"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		ui.close(new CTabItemLocator(TestingConstants.ENUMERATION_NAMES[2]));
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		IWidgetLocator literalStereoLocator = helper.getManagedEntityAttributeLocator(ui, 
				TestingConstants.ENUMERATION_NAMES[2], TestingConstants.LITERAL_NAMES[2],TestingConstants.LITERAL_STEREO);
		assertNotNull(literalStereoLocator);
//		ui.click(attributeStereoLocator);
		
		ui.click(new TreeItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+
				TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+"/"+TestingConstants.DIAGRAM_PACKAGE+
				TestingConstants.ENUMERATION_NAMES[2]+"/"+
				"<<"+TestingConstants.LITERAL_STEREO+">>"+TestingConstants.LITERAL_NAMES[2]+".*",
				new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		
		
	}
	
}
