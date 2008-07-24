package org.eclipse.tigerstripe.refactor.changes;

import java.util.ArrayList;

import org.eclipse.tigerstripe.ui.visualeditor.test.finders.LocatorFactory;
import org.eclipse.tigerstripe.ui.visualeditor.test.suite.DiagramConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;

public class UpdateComponents extends UITestCaseSWT {

	String entityName = TestingConstants.ENTITY_NAMES[2];
	String enumerationName = TestingConstants.ENUMERATION_NAMES[2];
	
	
	String pathRoot = TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+
					  TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+
					  TestingConstants.DIAGRAM_PACKAGE+
					  "/";
	
	/**
	 * Main test method.
	 */
	public void testUpdateComponents() throws Exception {
		IUIContext ui = getUI();
		// Open the diagram
		GuiUtils.openExplorerItem(ui, pathRoot+DiagramConstants.CREATE_DIAGRAM);
		checkAttribute(ui);
		checkMethod(ui);
		
	}
	
	public void checkAttribute(IUIContext ui) throws Exception{
		// Start as THIS MUST PRE_EXIST
		String attributeName = DiagramConstants.ATTRIBUTE_NAMES[0];
		// change to
		String newAttributeName = DiagramConstants.ATTRIBUTE_NAMES[1];
		// Open the artifact/go to the attribute
		GuiUtils.openExplorerItem(ui, pathRoot+entityName+"/"+attributeName+":"+"String");
		
		// Change it
		LabeledTextLocator name = new LabeledTextLocator("Name: ");
		GuiUtils.clearText(ui, name);
		ui.click(name);
		
		ui.enterText(newAttributeName);
		// Save Everything
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		ui.click(new CTabItemLocator("*PaletteCreate.wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		// now look for the changed Item on the diagram
		
		ArrayList<String> items = new ArrayList<String>();
		String attributeText = LocatorFactory.getInstance().getFieldString(ui,entityName,newAttributeName);
		assertNotNull("Did not find renamed attribute on diagram.", attributeText);
		
		// Should also look at diagrams that were closed when we did the modification
		
		// remember to close it afterwards
		
	}

	public void checkMethod(IUIContext ui) throws Exception{
		// Start as THIS MUST PRE_EXIST
		String methodName = DiagramConstants.METHOD_NAMES[0];
		// change to
		String newMethodName = DiagramConstants.METHOD_NAMES[1];
		// Open the artifact/go to the attribute
		GuiUtils.openExplorerItem(ui, pathRoot+entityName+"/"+methodName+"():void");
		
		// Change it
		LabeledTextLocator name = new LabeledTextLocator("Name: ");
		GuiUtils.clearText(ui, name);
		ui.click(name);
		
		ui.enterText(newMethodName);
		// Save Everything
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		ui.click(new CTabItemLocator("*PaletteCreate.wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		// now look for the changed Item on the diagram
		
		ArrayList<String> items = new ArrayList<String>();
		String methodText = LocatorFactory.getInstance().getMethodString(ui,entityName,newMethodName);
		assertNotNull("Did not find renamed method on diagram.", methodText);
		
		// Should also look at diagrams that were closed when we did the modification
		
		// remember to close it afterwards
		
	}
	
	public void checkLiteral(IUIContext ui) throws Exception{
		// Start as THIS MUST PRE_EXIST
		String literalName = DiagramConstants.LITERAL_NAMES[0];
		// change to
		String newliteralName = DiagramConstants.LITERAL_NAMES[1];
		// Open the artifact/go to the attribute
		GuiUtils.openExplorerItem(ui, pathRoot+enumerationName+"/"+literalName+"=0");
		
		// Change it
		LabeledTextLocator name = new LabeledTextLocator("Name: ");
		GuiUtils.clearText(ui, name);
		ui.click(name);
		
		ui.enterText(newliteralName);
		// Save Everything
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		ui.click(new CTabItemLocator("*PaletteCreate.wvd"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		// now look for the changed Item on the diagram
		
		ArrayList<String> items = new ArrayList<String>();
		String literalText = LocatorFactory.getInstance().getLiteralString(ui,entityName,newliteralName);
		assertNotNull("Did not find renamed literal on diagram.", literalText);
		
		// Should also look at diagrams that were closed when we did the modification
		
		// remember to close it afterwards
		
	}
	
}