package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.internal.forms.widgets.WrappedPageBook;

import abbot.finder.swt.MultipleWidgetsFoundException;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.locator.IWidgetReference;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
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
	 return newAttribute(ui, artifactName, thisAttributeName, null);	
	}
	
	public static String newAttribute(IUIContext ui, String artifactName, String thisAttributeName, String primitiveToUse) throws Exception {
		
		
		SWTWidgetLocator sectionLabel = new SWTWidgetLocator(Label.class, "&Attributes");
		// expand the Attributes sections
		ui.click(sectionLabel);
		
		SectionLocator attributesSection = new SectionLocator("&Attributes");
		
		
		// Make an Attribute - if the section already has an attribute we get an issue
		// with the "Stereotype 'Add' button
		

		ui.click(new NamedWidgetLocator("Add_Attribute"));		
		
		
		LabeledTextLocator name = new LabeledTextLocator("Name: ");
		ui.click(name);
		ui.keyClick(SWT.CTRL, 'a');
		ui.enterText(thisAttributeName);
		
		
		if (primitiveToUse != null){
			ui.click(new ButtonLocator("Browse", attributesSection));
			ui.wait(new ShellShowingCondition("Artifact Type Selection"));
			ui.click(new TableItemLocator(" "+primitiveToUse));
			ui.click(new ButtonLocator("OK"));
			ui.wait(new ShellDisposedCondition("Artifact Type Selection"));
		}

		
		
		//Save it
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		
		TableItemLocator attributeNameInTable = new TableItemLocator(thisAttributeName);
		ui.click(attributeNameInTable);
		assertEquals("Attribute Name  changed on save", thisAttributeName, name.getText(ui));
		
		//TODO - Extract these into a a generic attribute handling class
		LabeledTextLocator type  = new LabeledTextLocator("Type: ");
		String typeValue = type.getText(ui);
		
		String expectedType = "String";
		String explorerTypeName = "String";
		String message = "Default Type of Attribute not respected";
		
		if (primitiveToUse != null){
			expectedType= "primitive."+primitiveToUse;
			explorerTypeName = primitiveToUse;
			message = "Primitive Type of Attribute not respected";
		}
		
		assertEquals(message, expectedType, typeValue);

		
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

		ui.click(new NamedWidgetLocator("Add_Literal"));
		LabeledTextLocator name = new LabeledTextLocator("Name: ");
		ui.click(name);
		ui.keyClick(SWT.CTRL, 'a');
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
		ui.click(name);
		ui.keyClick(SWT.CTRL, 'a');
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
	
	// This variant just checks for the package
	public static void checkPackageInExplorer (IUIContext ui, String projectName, String packageName)throws Exception{
		String packagePath = packageName.replaceAll("\\.", "/");
		
		String pathToItem = projectName+
		"/src/"+
		packagePath;


		TreeItemLocator treeItem = new TreeItemLocator(
				pathToItem,
				new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
		ui.click(treeItem);
		
	}
	
	// This variant just checks for the artifact
	public static void checkArtifactInExplorer (IUIContext ui, String projectName, String packageName, String artifactName)throws Exception{
		String packagePath = packageName.replaceAll("\\.", "/");
		
		String pathToItem = projectName+
		"/src/"+
		packagePath+"/"+
		artifactName;


		TreeItemLocator treeItem = new TreeItemLocator(
				pathToItem,
				new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
		ui.click(treeItem);
		
	}
	
	public static void checkItemsInExplorer (IUIContext ui,String artifactName, ArrayList<String> items )throws Exception{
		checkItemsInExplorer ( ui,  TestingConstants.NEW_MODEL_PROJECT_NAME, TestingConstants.DEFAULT_ARTIFACT_PACKAGE,  artifactName,  items );
		
	}
	
	public static void checkItemsInExplorer (IUIContext ui, String packageName, String artifactName, ArrayList<String> items )throws Exception{
		checkItemsInExplorer ( ui,  TestingConstants.NEW_MODEL_PROJECT_NAME, packageName,  artifactName,  items );
	}
	
	public static void checkItemsInExplorer (IUIContext ui, String projectName, String packageName, String artifactName, ArrayList<String> items ) throws Exception{
		for (String item : items){

			String packagePath = packageName.replaceAll("\\.", "/");
			
			String pathToItem = projectName+
			"/src/"+
			packagePath+"/"+
			artifactName+"/"+
			item;


			TreeItemLocator treeItem = new TreeItemLocator(
					pathToItem,
					new ViewLocator(
							"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
			ui.click(treeItem);
		}
	}

	
	
}
