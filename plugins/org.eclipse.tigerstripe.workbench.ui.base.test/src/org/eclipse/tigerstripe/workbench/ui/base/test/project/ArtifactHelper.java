package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.locator.IWidgetReference;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.NamedWidgetLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.SectionLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class ArtifactHelper extends UITestCaseSWT{
	
	public static void openArtifactEditor(IUIContext ui, String artifactName) throws Exception{
		ui.click(new TreeItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME,
				new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		
	}

	public static String newAttribute(IUIContext ui, String artifactName, String thisAttributeName) throws Exception {
		
		
		SWTWidgetLocator sectionLabel = new SWTWidgetLocator(Label.class, "&Attributes");
		// expand the Attributes sections
		ui.click(sectionLabel);
		
		SectionLocator attributesSection = new SectionLocator("&Attributes");
		
		
		// Make an Attribute
		ui.click(new ButtonLocator("Add", attributesSection));
		LabeledTextLocator name = new LabeledTextLocator("Name: ");
		GuiUtils.clearText(ui, name);
		ui.click(name);
		ui.enterText(thisAttributeName);
		//Save it
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		
		TableItemLocator attributeNameInTable = new TableItemLocator(thisAttributeName);
		ui.click(attributeNameInTable);
		assertEquals("Attribute Name  changed on save", thisAttributeName, name.getText(ui));
		
		//TODO - Extract these into a a generic attribute handling class
		LabeledTextLocator type  = new LabeledTextLocator("Type: ");
		String typeValue = type.getText(ui);
		assertEquals("Default Type of Attribute not respected", "String", typeValue);
	
		/*
		 * 
		 * How do I test the value of the current selection of the the ComboBox.?
		 * 
		 */
		/*CComboItemLocator multiList = new CComboItemLocator("Multiplicity: ", attributesSection);
		assertEquals("1",multiList.getText(ui));*/
		
		ButtonLocator publicVisibility = new ButtonLocator("Public");
		assertTrue("Default 'Public' visibility is not respected",publicVisibility.isSelected(ui));
		
		/* This is now disabled by default inthe profile */
		//ButtonLocator optional = new ButtonLocator("Optional");
		//assertFalse("Default 'false' optionality is not respected",optional.isSelected(ui));
		
		ButtonLocator readOnly = new ButtonLocator("Readonly");
		assertFalse("Default 'false' readOnly is not respected",readOnly.isSelected(ui));
				
		// collapse the Attributes sections
		ui.click(sectionLabel);
		return thisAttributeName+":"+typeValue;
	}

	public static String newLiteral(IUIContext ui, String artifactName, String thisLiteralName) throws Exception {

		SWTWidgetLocator sectionLabel = new SWTWidgetLocator(Label.class, "Constants");
		
		// expand the section
		ui.click(sectionLabel);
		
		SectionLocator constantsSection = new SectionLocator("Constants");
		// Make a Literal

		ui.click(new ButtonLocator("Add", constantsSection));
		LabeledTextLocator name = new LabeledTextLocator("Name: ");
		GuiUtils.clearText(ui, name);
		ui.click(name);
		ui.enterText(thisLiteralName);
		//Save it
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		
		TableItemLocator literalNameInTable = new TableItemLocator(thisLiteralName);
		ui.click(literalNameInTable);
		assertEquals("Literal Name  changed on save", thisLiteralName, name.getText(ui));
		
		LabeledTextLocator value  = new LabeledTextLocator("Value: ");
		String valueValue = value.getText(ui);

				
		// collapse the section
		ui.click(sectionLabel);
		return thisLiteralName+"="+valueValue;
	}

	public static String newMethod(IUIContext ui, String artifactName, String thisMethodName) throws Exception {
		
		SWTWidgetLocator sectionLabel = new SWTWidgetLocator(Label.class, "Methods");
		
		// expand the section
		ui.click(sectionLabel);
		
		SectionLocator methodsSection = new SectionLocator("Methods");
		// Make a Literal

		ui.click(new ButtonLocator("Add", methodsSection));
		LabeledTextLocator name = new LabeledTextLocator("Name: ");
		GuiUtils.clearText(ui, name);
		ui.click(name);
		ui.enterText(thisMethodName);
		//Save it
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		String methodSignature = thisMethodName+"():void";
		TableItemLocator methodNameInTable = new TableItemLocator(methodSignature);
		ui.click(methodNameInTable);
		assertEquals("Method Name  changed on save", thisMethodName, name.getText(ui));	
		
		// collapse the section
		ui.click(sectionLabel);
		return methodSignature;
	}
	
	public static ArrayList<String> associationEndNames(IUIContext ui, String artifactName){
		ArrayList<String> ends = new ArrayList<String>();
		/*
		 * We are not really adding them in this case - just getting the name and 
		 * Types so we can validate against the explorer 
		 */
		try {

			SWTWidgetLocator sectionLabel = new SWTWidgetLocator(Label.class, "End &Details");

			// expand the section
			ui.click(sectionLabel);			

			SectionLocator endsSection = new SectionLocator("End &Details");	

			IWidgetReference aNameRef = (IWidgetReference)ui.find(new NamedWidgetLocator("aEndNameText")); 
			Text aNameWidget = (Text) aNameRef.getWidget();
			String aNameTextValue = new abbot.tester.swt.TextTester().getText(aNameWidget);
			
			IWidgetReference aTypeRef = (IWidgetReference)ui.find(new NamedWidgetLocator("aEndTypeText")); 
			Text aTypeWidget = (Text) aTypeRef.getWidget();
			String aTypeTextValue = new abbot.tester.swt.TextTester().getText(aTypeWidget);
			
			
			if (aTypeTextValue.contains(".")){
				aTypeTextValue = aTypeTextValue.substring(aTypeTextValue.lastIndexOf(".")+1);
			}
			ends.add(aNameTextValue+":"+aTypeTextValue);


			
			IWidgetReference zNameRef = (IWidgetReference)ui.find(new NamedWidgetLocator("zEndNameText")); 
			Text zNameWidget = (Text) zNameRef.getWidget();
			String zNameTextValue = new abbot.tester.swt.TextTester().getText(zNameWidget);
			
			IWidgetReference zTypeRef = (IWidgetReference)ui.find(new NamedWidgetLocator("zEndTypeText")); 
			Text zTypeWidget = (Text) zTypeRef.getWidget();
			String zTypeTextValue = new abbot.tester.swt.TextTester().getText(zTypeWidget);
			
			if (zTypeTextValue.contains(".")){
				zTypeTextValue = zTypeTextValue.substring(zTypeTextValue.lastIndexOf(".")+1);
			}
			
			ends.add(zNameTextValue+":"+zTypeTextValue);

			// collapse the section
			ui.click(sectionLabel);

		} catch (WidgetSearchException wse){
			fail("Did not find widgets '"+wse.getMessage()+"' for ends of "+artifactName);
		}
		
		
		return ends;
	}
	
	public static ArrayList<String> dependencyEndNames(IUIContext ui, String artifactName){
		ArrayList<String> ends = new ArrayList<String>();
		/*
		 * We are not really adding them in this case - just getting the name and 
		 * Types so we can validate against the explorer 
		 */
		try {

			SWTWidgetLocator sectionLabel = new SWTWidgetLocator(Label.class, "End &Details");

			// expand the section
			ui.click(sectionLabel);			

			SectionLocator endsSection = new SectionLocator("End &Details");
			
			IWidgetReference aTypeRef = (IWidgetReference)ui.find(new NamedWidgetLocator("aEndTypeText")); 
			Text aTypeWidget = (Text) aTypeRef.getWidget();
			String aTypeTextValue = new abbot.tester.swt.TextTester().getText(aTypeWidget);
			
			
			if (aTypeTextValue.contains(".")){
				aTypeTextValue = aTypeTextValue.substring(aTypeTextValue.lastIndexOf(".")+1);
			}
			
			ends.add(aTypeTextValue);
			
			IWidgetReference zTypeRef = (IWidgetReference)ui.find(new NamedWidgetLocator("zEndTypeText")); 
			Text zTypeWidget = (Text) zTypeRef.getWidget();
			String zTypeTextValue = new abbot.tester.swt.TextTester().getText(zTypeWidget);
			
			if (zTypeTextValue.contains(".")){
				zTypeTextValue = zTypeTextValue.substring(zTypeTextValue.lastIndexOf(".")+1);
			}
			ends.add(zTypeTextValue);
			
			// collapse the section
			ui.click(sectionLabel);

		} catch (WidgetSearchException wse){
			fail("Did not find widgets '"+wse.getMessage()+"' for ends of "+artifactName);
		}
		
		
		return ends;
	}
	
	public static void checkItemsInExplorer (IUIContext ui,String artifactName, ArrayList<String> items ){
		for (String item : items){

			String packagePath = TestingConstants.DEFAULT_ARTIFACT_PACKAGE.replaceAll("\\.", "/");
			
			String pathToItem = TestingConstants.NEW_MODEL_PROJECT_NAME+
			"/src/"+
			packagePath+"/"+
			artifactName+"/"+
			item;


			try {	
				TreeItemLocator treeItem = new TreeItemLocator(
						pathToItem,
					new ViewLocator(
							"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
				ui.click(treeItem);
			} catch (Exception e){
				fail("Item '"+ pathToItem+ "' is not in the Explorer view");
			}
		}
		
	}
	
	public static void checkItemsInExplorer (IUIContext ui, String packageName, String artifactName, ArrayList<String> items ){
		for (String item : items){

			String packagePath = packageName.replaceAll("\\.", "/");
			
			String pathToItem = TestingConstants.NEW_MODEL_PROJECT_NAME+
			"/src/"+
			packagePath+"/"+
			artifactName+"/"+
			item;

			try {	
				TreeItemLocator treeItem = new TreeItemLocator(
						pathToItem,
					new ViewLocator(
							"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
				ui.click(treeItem);
			} catch (Exception e){
				fail("Item '"+ pathToItem+ "' is not in the Explorer view");
			}
		}
		
	}
	
}
