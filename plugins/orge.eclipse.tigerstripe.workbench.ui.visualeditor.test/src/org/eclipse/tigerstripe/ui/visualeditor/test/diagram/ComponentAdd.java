package org.eclipse.tigerstripe.ui.visualeditor.test.diagram;



import java.util.ArrayList;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.ui.visualeditor.test.finders.LocatorHelper;
import org.eclipse.tigerstripe.ui.visualeditor.test.suite.DiagramConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute3EditPart;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.gef.locator.PaletteItemLocator;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.locator.WidgetReference;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
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
		
		// And now do the same in the editor - see they get to the diagram
		editorCreateAttribute(ui, TestingConstants.ENTITY_NAMES[2], TestingConstants.ATTRIBUTE_NAMES[2]);
		editorCreateMethod(ui, TestingConstants.ENTITY_NAMES[2], TestingConstants.METHOD_NAMES[2]);
		editorCreateLiteral(ui, TestingConstants.ENUMERATION_NAMES[2], TestingConstants.LITERAL_NAMES[2]);
		
		
		editorUpdateAttribute(ui, TestingConstants.ENTITY_NAMES[2], TestingConstants.ATTRIBUTE_NAMES[2]);
		
		// Close it
		ui.close(new CTabItemLocator(DiagramConstants.CREATE_DIAGRAM+".wvd"));
		
	}

	private void createAndCheckComponents(IUIContext ui, String entityName)  throws Exception {
		//Add some components to my Entities from the palette.

		ArrayList<String> items = new ArrayList<String>();
		
		IWidgetLocator attributes = this.helper.getManagedEntityAttributeCompartmentLocator(ui,entityName);
		
		ui.click(new PaletteItemLocator("Features/Field"));
		ui.click(attributes);
		
		// NOTE this would be much better if we found out the name, rather than assumed we know best!
		
		items.add("attribute0:String");
		
		
		IWidgetLocator methods = this.helper.getManagedEntityMethodCompartmentLocator(ui,entityName);
		ui.click(new PaletteItemLocator("Features/Method"));
		ui.click(methods);
		items.add("method0():void");
		
		// Need to check they made it onto the diagram, and the explorer
		ArtifactHelper.checkItemsInExplorer(ui,
				TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+TestingConstants.DIAGRAM_PACKAGE,
				entityName,items);

		
		
		IWidgetLocator attribute = this.helper.getManagedEntityAttributeLocator(ui, entityName, "attribute0");	
		assertNotNull("Didn't find the attribute on the diagram (drop on attribute)",attribute);
		
		// This is how to get the info from the Attribute..
		//Attribute3EditPart.AttributeLabelFigure attributeFig = ((Attribute3EditPart.AttributeLabelFigure) ((WidgetReference) attribute).getWidget());
		//String text = attributeFig.getText();
		//System.out.println(text);
		
		// This isn't great if you have two variants of the same method (with different signatures)
		IWidgetLocator method = this.helper.getManagedEntityMethodLocator(ui, entityName, "method0");	
		assertNotNull("Didn't find the method on the diagram (drop on method)",method);
		
		
		// Save the diagram
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));

	}
	
	private void createAndCheckLiterals(IUIContext ui, String enumName)  throws Exception {
		//Add some components to my Entities.

		IWidgetLocator literals = this.helper.getEnumerationLiteralCompartmentLocator(ui,enumName);

		ArrayList<String> items = new ArrayList<String>();
		ui.click(new PaletteItemLocator("Features/Literal"));
		ui.click(literals);
		items.add("literal0=0");


		ArtifactHelper.checkItemsInExplorer(ui,
				TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+TestingConstants.DIAGRAM_PACKAGE,
				enumName,items);

		IWidgetLocator literal = this.helper.getEnumerationLiteralLocator(ui, enumName, "literal0");	
		assertNotNull("Didn't find the literal on the diagram (drop on literal)",literal);
		
		
		// Save the diagram
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));

	}

	/**
	 * Add stuff in the editor and make sure it gets to the diagram.
	 * 
	 * @param ui
	 * @param entityName
	 */
	private void editorCreateAttribute(IUIContext ui, String entityName, String attributeName) throws Exception {
		ArrayList<String> items = new ArrayList<String>();
		
		IWidgetLocator entity = this.helper.getManagedEntityLocator(ui, entityName);

		ui.contextClick(entity, "Open in Editor");
		GuiUtils.maxminTab(ui, entityName);
		SWTWidgetLocator attributesSection = new SWTWidgetLocator(Label.class, "&Attributes");
		ui.click(attributesSection);
		items.add(ArtifactHelper.newAttribute(ui, entityName, attributeName));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		IWidgetLocator attribute = this.helper.getManagedEntityAttributeLocator(ui,entityName,attributeName);
		assertNotNull("Did not find attribute on diagram (created in editor).", attribute);
		GuiUtils.maxminTab(ui, entityName);
		ui.close(new CTabItemLocator(entityName));
		
	}
	
	private void editorUpdateAttribute(IUIContext ui, String entityName, String attributeName) throws Exception {
		ArrayList<String> items = new ArrayList<String>();
		
		IWidgetLocator entity = this.helper.getManagedEntityLocator(ui, entityName);

		ui.contextClick(entity, "Open in Editor");
		GuiUtils.maxminTab(ui, entityName);
		
		// Select this attribute
		TableItemLocator attributeNameInTable = new TableItemLocator(attributeName);
		ui.click(attributeNameInTable);
		// This should make the attribute selected...
		
	//	Type:
		ui.click(new LabeledLocator(Button.class, "Type: "));
		ui.wait(new ShellShowingCondition("Artifact Type Selection"));
		ui.click(new TableItemLocator(" boolean"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Artifact Type Selection"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		// Bring diagram back into view!
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		
		IWidgetLocator attribute = this.helper.getManagedEntityAttributeLocator(ui,entityName,attributeName);
		assertNotNull("Did not find attribute on diagram (updated in editor).", attribute);
		Attribute3EditPart.AttributeLabelFigure attributeFig = ((Attribute3EditPart.AttributeLabelFigure) ((WidgetReference) attribute).getWidget());
		String text = attributeFig.getText();
		// Should now look like name:boolean
		if (!text.endsWith(":boolean")){
			fail ("Attribute type not updated to boolean type in diagram");
		}
		ui.click(new CTabItemLocator(entityName));
		
	//	Multiplicity:	
	//	Visibility:
		ui.click(attributeNameInTable);
		ui.click(new ButtonLocator("Private"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		// Bring diagram back into view!
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		
		attribute = this.helper.getManagedEntityAttributeLocator(ui,entityName,attributeName);
		assertNotNull("Did not find attribute on diagram (updated in editor).", attribute);
		attributeFig = ((Attribute3EditPart.AttributeLabelFigure) ((WidgetReference) attribute).getWidget());
		text = attributeFig.getText();
		if (!text.startsWith("-")){
			fail ("Attribute visibility not updated to boolean type in diagram");
		}
		
		
	//	DefaultValue:
			
			
		
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		
		
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
		
		IWidgetLocator entity = this.helper.getEnumerationLocator(ui, enumName);

		ui.contextClick(entity, "Open in Editor");
		GuiUtils.maxminTab(ui, enumName);
		SWTWidgetLocator constantsSection = new SWTWidgetLocator(Label.class, "Constants");
		ui.click(constantsSection);
		items.add(ArtifactHelper.newLiteral(ui, enumName, literalName));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		IWidgetLocator literal = this.helper.getEnumerationLiteralLocator(ui,enumName,literalName);
		assertNotNull("Did not find literal on diagram (created in editor).", literal);
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
		
		IWidgetLocator entity = this.helper.getManagedEntityLocator(ui, entityName);

		ui.contextClick(entity, "Open in Editor");
		GuiUtils.maxminTab(ui, entityName);
		// Note this is Attributes ON PURPOSE to close the default open section!
		SWTWidgetLocator attributesSection = new SWTWidgetLocator(Label.class, "&Attributes");
		ui.click(attributesSection);
		items.add(ArtifactHelper.newMethod(ui, entityName, methodName));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		ui.click(new CTabItemLocator("*"+DiagramConstants.CREATE_DIAGRAM+".wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		IWidgetLocator method = this.helper.getManagedEntityMethodLocator(ui,entityName,methodName);
		assertNotNull("Did not find method on diagram (created in editor).", method);
		GuiUtils.maxminTab(ui, entityName);
		ui.close(new CTabItemLocator(entityName));
	}
	
}