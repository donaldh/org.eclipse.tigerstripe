package org.eclipse.tigerstripe.workbench.ui.base.test.profile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.NamedWidgetLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class CreateStereotypes extends UITestCaseSWT {

	private IUIContext ui;

	public void testStereotypes() throws Exception {
// Create stereotypes of each various "type"
		ui = getUI();
		createStereotypes(ui);
		
	}
	
	public void tearDown() throws Exception{
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		ui.contextClick(
				new TreeItemLocator(
						TestingConstants.NEW_MODEL_PROJECT_NAME+"/"+
						TestingConstants.NEW_PROFILE_NAME+".wbp",
						new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
		"Profile/Set as active profile.");
		ui.wait(new ShellShowingCondition("Save as Active Profile"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Save as Active Profile"));
		ui.wait(new ShellShowingCondition("Success"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Success"));
	}
	
	public void createStereotypes(IUIContext ui) throws Exception {	
			ui.click(2,
					new TreeItemLocator(
							TestingConstants.NEW_MODEL_PROJECT_NAME+"/"+
									TestingConstants.NEW_PROFILE_NAME+".wbp",
									new ViewLocator(
											"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
			Thread.sleep(500);
			ui.click(new CTabItemLocator("Stereotypes"));
			
			
			ui.click(new NamedWidgetLocator("Add_Stereotype"));
			ui.click(new LabeledTextLocator("Name: "));
			ui.keyClick(SWT.CTRL, 'a');
			ui.enterText("method_stereo");
			ui.click(new ButtonLocator("Method"));
			
			ui.click(new NamedWidgetLocator("Add_Stereotype"));
			ui.click(new LabeledTextLocator("Name: "));
			ui.keyClick(SWT.CTRL, 'a');
			ui.enterText("attribute_stereo");
			ui.click(new ButtonLocator("Attribute"));
		
			ui.click(new NamedWidgetLocator("Add_Stereotype"));
			ui.click(new LabeledTextLocator("Name: "));
			ui.keyClick(SWT.CTRL, 'a');
			ui.enterText("literal_stereo");
			ui.click(new ButtonLocator("Literal"));
			
			ui.click(new NamedWidgetLocator("Add_Stereotype"));
			ui.click(new LabeledTextLocator("Name: "));
			ui.keyClick(SWT.CTRL, 'a');
			ui.enterText("argument_stereo");
			ui.click(new ButtonLocator("Argument"));
			
			ui.click(new NamedWidgetLocator("Add_Stereotype"));
			ui.click(new LabeledTextLocator("Name: "));
			ui.keyClick(SWT.CTRL, 'a');
			ui.enterText("end_stereo");
			ui.click(new ButtonLocator("Assoc. End"));
			
			// now the Artifact based ones.
			
			ui.click(new NamedWidgetLocator("Add_Stereotype"));
			ui.click(new LabeledTextLocator("Name: "));
			ui.keyClick(SWT.CTRL, 'a');
			ui.enterText("association_stereo");
			ui.click(new TableItemLocator("", 0, new SWTWidgetLocator(
							Table.class)));
			
			ui.click(new NamedWidgetLocator("Add_Stereotype"));
			ui.click(new LabeledTextLocator("Name: "));
			ui.keyClick(SWT.CTRL, 'a');
			ui.enterText("associationClass_stereo");
			ui.click(new TableItemLocator("", 1, new SWTWidgetLocator(
							Table.class)));
			
			ui.click(new NamedWidgetLocator("Add_Stereotype"));
			ui.click(new LabeledTextLocator("Name: "));
			ui.keyClick(SWT.CTRL, 'a');
			ui.enterText("datatype_stereo");
			ui.click(new TableItemLocator("", 2, new SWTWidgetLocator(
							Table.class)));
//TODOD re-activate when WT is fixed		
//			ui.click(new NamedWidgetLocator("Add_Stereotype"));
//			ui.click(new LabeledTextLocator("Name: "));
//			ui.keyClick(SWT.CTRL, 'a');
//			ui.enterText("dependency_stereo");
//			ui.click(new TableItemLocator("", 2, new SWTWidgetLocator(
//							Table.class)));
//			
//			ui.click(new NamedWidgetLocator("Add_Stereotype"));
//			ui.click(new LabeledTextLocator("Name: "));
//			ui.keyClick(SWT.CTRL, 'a');
//			ui.enterText("enumeration_stereo");
//			ui.click(new TableItemLocator("", 3, new SWTWidgetLocator(
//							Table.class)));
//			
//			ui.click(new NamedWidgetLocator("Add_Stereotype"));
//			ui.click(new LabeledTextLocator("Name: "));
//			ui.keyClick(SWT.CTRL, 'a');
//			ui.enterText("event_stereo");
//			ui.click(new TableItemLocator("", 4, new SWTWidgetLocator(
//							Table.class)));
//			
//			ui.click(new NamedWidgetLocator("Add_Stereotype"));
//			ui.click(new LabeledTextLocator("Name: "));
//			ui.keyClick(SWT.CTRL, 'a');
//			ui.enterText("exception_stereo");
//			ui.click(new TableItemLocator("", 5, new SWTWidgetLocator(
//							Table.class)));
//			
//			ui.click(new NamedWidgetLocator("Add_Stereotype"));
//			ui.click(new LabeledTextLocator("Name: "));
//			ui.keyClick(SWT.CTRL, 'a');
//			ui.enterText("entity_stereo");
//			ui.click(new TableItemLocator("", 6, new SWTWidgetLocator(
//							Table.class)));
//			
//			ui.click(new NamedWidgetLocator("Add_Stereotype"));
//			ui.click(new LabeledTextLocator("Name: "));
//			ui.keyClick(SWT.CTRL, 'a');
//			ui.enterText("package_stereo");
//			ui.click(new TableItemLocator("", 6, new SWTWidgetLocator(
//							Table.class)));
//			
//			ui.click(new NamedWidgetLocator("Add_Stereotype"));
//			ui.click(new LabeledTextLocator("Name: "));
//			ui.keyClick(SWT.CTRL, 'a');
//			ui.enterText("primitiveType_stereo");
//			ui.click(new TableItemLocator("", 7, new SWTWidgetLocator(
//							Table.class)));
//			
//			ui.click(new NamedWidgetLocator("Add_Stereotype"));
//			ui.click(new LabeledTextLocator("Name: "));
//			ui.keyClick(SWT.CTRL, 'a');
//			ui.enterText("query_stereo");
//			ui.click(new TableItemLocator("", 8, new SWTWidgetLocator(
//							Table.class)));
//			
//			ui.click(new NamedWidgetLocator("Add_Stereotype"));
//			ui.click(new LabeledTextLocator("Name: "));
//			ui.keyClick(SWT.CTRL, 'a');
//			ui.enterText("session_stereo");
//			ui.click(new TableItemLocator("", 9, new SWTWidgetLocator(
//							Table.class)));
//			
//			ui.click(new NamedWidgetLocator("Add_Stereotype"));
//			ui.click(new LabeledTextLocator("Name: "));
//			ui.keyClick(SWT.CTRL, 'a');
//			ui.enterText("updateProcedure_stereo");
//			ui.click(new TableItemLocator("", 10, new SWTWidgetLocator(
//							Table.class)));
//			
//			//Finally add a second Entity one that has some Attributes
//			ui.click(new NamedWidgetLocator("Add_Stereotype"));
//			ui.click(new LabeledTextLocator("Name: "));
//			ui.keyClick(SWT.CTRL, 'a');
//			ui.enterText("entity_stereo_withAttributes");
//			ui.click(new TableItemLocator("", 6, new SWTWidgetLocator(
//							Table.class)));
//			ui.click(new NamedWidgetLocator("Add_Attribute"));
//			ui.wait(new ShellShowingCondition("Stereotype Attribute Edit"));
//			ui.click(new LabeledTextLocator("Name:"));
//			ui.enterText("string_attr");
//			ui.click(new LabeledTextLocator("Default value:"));
//			ui.enterText("Start");
//			ui.click(new ButtonLocator("OK"));
//			ui.wait(new ShellDisposedCondition("Stereotype Attribute Edit"));
//			
			
			
			
	}
	
	
		
}