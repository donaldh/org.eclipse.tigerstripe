package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.SectionLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class ArtifactHelper extends UITestCaseSWT{

	public void newAttribute(IUIContext ui, String artifactName) throws Exception {
		
		// Select the Entity Editor and maximise
		// Have to do this to get the Attributes Section visible
		// TODO Should not be needed?
		/*
		 * SUPPORT ....
		 * 
		 * I needed to maximise my window here, because otherwise the "Attributes" section
		 * is not visible and the tester will not scroll to bring it into view.
		 * The tester uses the co-ordinates within the form, and ends up clicking on the error view, as that
		 * is covering that part of the form.
		 * 
		 * Is there a way to force the section to come into view?
		 *  
		 */
		GuiUtils.maxminTab(ui, artifactName);
		
		SectionLocator attributesSection = new SectionLocator("&Attributes");
		
		// Make an Attribute
		String thisAttributeName = TestingConstants.ATRIBUTE_NAMES[0];
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
		assertEquals("Default Type of Attribute not respected", "String", type.getText(ui));
	
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
		
		
		// Should check the item is in the explorer!
		// Exception thrown if widget not found!
		String pathToEntity = TestingConstants.NEW_PROJECT_NAME+
								"/src/"+
								TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"/"+
								artifactName+"/"+
								TestingConstants.ATRIBUTE_NAMES[0];
		try {
			TreeItemLocator treeItem = new TreeItemLocator(
				pathToEntity,
				new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
		} catch (Exception e){
			fail("Attribute is not in the Explorer view");
		}		
		
		
		
	}

}
