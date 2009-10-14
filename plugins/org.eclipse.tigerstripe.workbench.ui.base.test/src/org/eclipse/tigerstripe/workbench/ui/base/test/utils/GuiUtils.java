package org.eclipse.tigerstripe.workbench.ui.base.test.utils;

import org.eclipse.ui.forms.widgets.Form;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class GuiUtils {

	public static void maxminTab(IUIContext ui, String tabName)
			throws Exception {
		ui.click(2, new CTabItemLocator(tabName));

	}

	public static void closeTab(IUIContext ui, String tabName) throws Exception {
		ui.click(new CTabItemLocator(tabName));
		ui.contextClick(new SWTWidgetLocator(Form.class), "&Close");
	}

	public static void openExplorerItem(IUIContext ui, String diagramPath)
			throws Exception {
		ui
				.click(
						2,
						new TreeItemLocator(
								diagramPath,
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));

	}
}
