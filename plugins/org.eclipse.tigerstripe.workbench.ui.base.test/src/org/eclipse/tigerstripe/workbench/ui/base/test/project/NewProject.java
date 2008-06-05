package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.SectionLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.PullDownMenuItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class NewProject extends UITestCaseSWT {

	private IUIContext ui;
	/**
	 * Main test method.
	 */
	public void testNewProject() throws Exception {
		ui = getUI();
		ui.click(new ContributedToolItemLocator(
				"org.eclipse.tigerstripe.ui.eclipse.openNewProjectAction"));
		ui.wait(new ShellShowingCondition("New Tigerstripe Project"));
		ui.click(new LabeledTextLocator("&Project Name:"));
		ui.enterText(TestingConstants.NEW_PROJECT_NAME);
		LabeledTextLocator artifactPackage = new LabeledTextLocator("Artifacts Package:");
		GuiUtils.clearText(ui, artifactPackage);
		ui.click(artifactPackage);
		ui.enterText(TestingConstants.DEFAULT_ARTIFACT_PACKAGE);
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("New Tigerstripe Project"));
		
		// Make sure what we put in the wizard made it to the project 
		CTabItemLocator descriptorEditor = new CTabItemLocator(
				TestingConstants.NEW_PROJECT_NAME+
				"/tigerstripe.xml");
		try {
			ui.click(descriptorEditor);
		} catch (Exception e) {
			fail("Descriptor editor did not appear");
		}
		
		ui.click(new SWTWidgetLocator(Label.class, "Project Defaults"));
		LabeledTextLocator defaultPackage = new LabeledTextLocator("Default Artifact Package: ");
		assertEquals(TestingConstants.DEFAULT_ARTIFACT_PACKAGE, defaultPackage.getText(ui));
			
		// Now set some other project details
		//TODO
		
		
		// Close the descriptor
		//ui.click(new XYLocator(new CTabItemLocator(
		//	TestingConstants.NEW_PROJECT_NAME+"/tigerstripe.xml"), 169, 13));
		
	}
	
	/**
	 * This is a simple create using just the name.
	 * We need to check the defaults are properly created.
	 * @throws Exception
	 */
	public void testNewManagedEntityDefaults() throws Exception	{
		ui = getUI();
		ui.click(new TreeItemLocator(
				TestingConstants.NEW_PROJECT_NAME,
				new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui.click(new PullDownMenuItemLocator("Entity",
				new ContributedToolItemLocator(
						"org.eclipse.tigerstripe.eclipse.newArtifactAction")));
		ui.wait(new ShellShowingCondition("New Entity Artifact"));
		String thisEntityName = TestingConstants.ENTITY_NAMES[0];
		ui.enterText(thisEntityName);
		
		LabeledTextLocator artifactPackage = new LabeledTextLocator("Artifact Package:");
		assertEquals("Default package for new Entity Wizard is not that set for the project",TestingConstants.DEFAULT_ARTIFACT_PACKAGE, artifactPackage.getText(ui));
		
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("New Entity Artifact"));
		
		CTabItemLocator artifactEditor = new CTabItemLocator(
				TestingConstants.ENTITY_NAMES[0]);
		
		try {
			ui.click(artifactEditor);
		} catch (Exception e) {
			fail("Artifact editor did not appear");
		}
		
		// See if it has the right details
		LabeledTextLocator project = new LabeledTextLocator("Project: ");
		assertEquals("Project for Entity is incorrect",TestingConstants.NEW_PROJECT_NAME, project.getText(ui));
		
		LabeledTextLocator fqn = new LabeledTextLocator("Qualified Name: ");
		assertEquals("FQN for Entity is incorrect",TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+thisEntityName, fqn.getText(ui));
		
		LabeledTextLocator extend = new LabeledTextLocator("Extends: ");
		assertEquals("Extends for Entity should be empty on create","", extend.getText(ui));
		
		LabeledTextLocator description = new LabeledTextLocator("Description: ");
		assertEquals("Description for Entity should be empty on create","", description.getText(ui));
		
		// See if it is in the tree view.
		// Exception thrown if widget not found!
		String pathToEntity = TestingConstants.NEW_PROJECT_NAME+
								"/src/"+
								TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"/"+
								thisEntityName;
		try {
		TreeItemLocator treeItem = new TreeItemLocator(
				pathToEntity,
				new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
		} catch (Exception e){
			fail("Entity is not in the Explorer view");
		}		
	}
	
	public void testNewAttribute() throws Exception {
		ui = getUI();
		
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
		GuiUtils.maxminTab(ui, TestingConstants.ENTITY_NAMES[0]);
		
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
		 * SUPPORT
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
		
	}

	
	public void testCloseEditor() throws Exception{
		ui = getUI();
		GuiUtils.maxminTab(ui, TestingConstants.ENTITY_NAMES[0]);
		ui.click(new XYLocator(new CTabItemLocator(TestingConstants.ENTITY_NAMES[0]), 70, 12));
		
	}
	
	
}