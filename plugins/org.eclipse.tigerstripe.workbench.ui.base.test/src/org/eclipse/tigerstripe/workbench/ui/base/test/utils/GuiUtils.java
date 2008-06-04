package org.eclipse.tigerstripe.workbench.ui.base.test.utils;

import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.Form;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;

public class GuiUtils {

	public static void clearText(IUIContext ui, LabeledTextLocator locator)throws Exception {
		ui.click(locator);
		while (locator.getText(ui).length() >0){
			ui.keyClick(SWT.BS);        //send backspace
		}
	}
	
	public static void maxminTab(IUIContext ui, String tabName) throws Exception	{
		ui.click(2, new CTabItemLocator(tabName));

	}
	
	public static void closeTab(IUIContext ui, String tabName) throws Exception	{
		ui.click(new CTabItemLocator(tabName));
		ui.contextClick(new SWTWidgetLocator(Form.class), "&Close");
	}
}
