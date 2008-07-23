package org.eclipse.tigerstripe.ui.visualeditor.test.profile;

import org.eclipse.swt.widgets.Table;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;

public class StereotypeAdditions extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void testStereotypeAdditions() throws Exception {
		IUIContext ui = getUI();
		
		ui.click(new CTabItemLocator("Stereotypes"));
		LabeledTextLocator nameLabel = new LabeledTextLocator("Name: ");
		IWidgetLocator[] allAdds = ui.findAll(new ButtonLocator("Add"));
		
		ui.click(allAdds[0]);
		GuiUtils.clearText(ui, nameLabel);
		ui.click(nameLabel);
		ui.enterText("methodStereo");
		ui.click(new ButtonLocator("Method"));
		
		ui.click(allAdds[0]);		
		GuiUtils.clearText(ui, nameLabel);
		ui.click(nameLabel);
		ui.enterText("attributeStereo");
		ui.click(new ButtonLocator("Attribute"));
		
		ui.click(allAdds[0]);		
		GuiUtils.clearText(ui, nameLabel);
		ui.click(nameLabel);
		ui.enterText("literalStereo");
		ui.click(new ButtonLocator("Literal"));
		
		ui.click(allAdds[0]);		
		GuiUtils.clearText(ui, nameLabel);
		ui.click(nameLabel);
		ui.enterText("argumentStereo");
		ui.click(new ButtonLocator("Argument"));
		
		// This is never used, because we can't add Stereotypes to the ends
//		ui.click(allAdds[0]);		
//		GuiUtils.clearText(ui, nameLabel);
//		ui.click(nameLabel);
//		ui.enterText("endStereo");
//		ui.click(new ButtonLocator("Assoc. End"));
		
		// This bit does't work effectively
//		ui.click(allAdds[0]);
//		GuiUtils.clearText(ui, nameLabel);
//		ui.click(nameLabel);
//		ui.enterText("entityStereo");
//		ui
//				.click(new TableItemLocator("", 7, new SWTWidgetLocator(
//						Table.class)));
		

	}

}