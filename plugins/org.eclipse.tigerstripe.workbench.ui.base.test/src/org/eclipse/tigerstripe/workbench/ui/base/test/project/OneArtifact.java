package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.NamedWidgetLocator;
import com.windowtester.runtime.swt.locator.SectionLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;

public class OneArtifact extends UITestCaseSWT {

	private ArtifactHelper helper;
	private ArtifactAuditHelper auditHelper;
	
	
	public void testArtifactComponents() throws Exception {
		IUIContext ui= getUI();
//		ProjectRecord.addArtifact(NewArtifacts.testNewArtifactDefaults(ui,"Package", TestingConstants.PACKAGE_NAMES[0], false, false, false, false));
		ProjectRecord.addArtifact(NewArtifacts.testNewArtifactDefaults(ui,"Entity", TestingConstants.ENTITY_NAMES[2], true, true, true, false));
		
		
		// So now I have an Entity with "bits" on..
		
		//Try Removing the item
		// Then close editor, with No save
		// The item should still be in the explorer and in the reopened editor
		
		
		TreeItemLocator treeItem = ProjectHelper.checkArtifactInExplorer(ui,TestingConstants.ENTITY_NAMES[2]);
		ui.click(2, treeItem);
		// We know the Attributes sectiomn opens by default
		// so find our attribute
		GuiUtils.maxminTab(ui, TestingConstants.ENTITY_NAMES[2]);
		String prefix = TestingConstants.ENTITY_NAMES[2].toLowerCase()+"_";
		String thisAttributeName = prefix+TestingConstants.ATTRIBUTE_NAMES[0];
		
		TableItemLocator attributeNameInTable = new TableItemLocator(thisAttributeName);
		ui.click(attributeNameInTable);
		
		// If we have it, remove it.
		SectionLocator attributesSection = new SectionLocator("&Attributes");
		
		
		IWidgetLocator removeButtonLocator =ui.find(new NamedWidgetLocator("Remove_Attribute"));
		
		ui.click(removeButtonLocator);
		ui.wait(new ShellShowingCondition("Remove attribute"));
		ui.click(new ButtonLocator("Yes"));
		ui.wait(new ShellDisposedCondition("Remove attribute"));
		
		//See if it' gone from the table
		TableItemLocator attributeNameInTable2 = new TableItemLocator(thisAttributeName);
		try {
			ui.click(attributeNameInTable2);
			closeNoSave(ui);
			fail("Attribute is still in table after a Remove");	
		} catch (Exception noWidget){
			// This is what we want ie no Attribute
		}
		
		closeNoSave(ui);
		
		//So if we reopen - it should still be there!
		treeItem = ProjectHelper.checkArtifactInExplorer(ui,TestingConstants.ENTITY_NAMES[2]);
		ui.click(2, treeItem);
		GuiUtils.maxminTab(ui, TestingConstants.ENTITY_NAMES[2]);
		//See if it' gone from the table
		TableItemLocator attributeNameInTable3 = new TableItemLocator(thisAttributeName);
		try {
			ui.click(attributeNameInTable3);
			
		} catch (Exception noWidget){
			ui.close(new CTabItemLocator(
					TestingConstants.ENTITY_NAMES[2]));
			fail("Attribute should NOT have been removed but is NOT in table on re-open");
		}
		IWidgetLocator loc = new CTabItemLocator(
				TestingConstants.ENTITY_NAMES[2]);
		ui.close(loc);
		
	}
	
	private void closeNoSave(IUIContext ui) throws Exception{
		// Close the editor WITHOUT Saving
		
		IWidgetLocator loc = new CTabItemLocator(
				"*"+TestingConstants.ENTITY_NAMES[2]);
		ui.close(loc);
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellShowingCondition("Save Resource"));
		ui.click(new ButtonLocator("&No"));
		ui.wait(new ShellDisposedCondition("Save Resource"));
		
	}

}
