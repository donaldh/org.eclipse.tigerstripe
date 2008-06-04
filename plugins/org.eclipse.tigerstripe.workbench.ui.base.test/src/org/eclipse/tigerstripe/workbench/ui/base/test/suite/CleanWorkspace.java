package org.eclipse.tigerstripe.workbench.ui.base.test.suite;

import org.eclipse.swt.widgets.Shell;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;

public class CleanWorkspace extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void testCleanWorkspace() throws Exception {
		IUIContext ui = getUI();
		ui.click(new XYLocator(new CTabItemLocator("Welcome"), 79, 11));
		ui.click(new XYLocator(new CTabItemLocator("Outline"), 79, 11));
	}

	/** 
	 * This is required to set the profile to defaults before we start.
	 * Otherwise we don't know for sure that the profile will be applied properly!
	 * 
	 * @throws Exception
	 */
	public void testClearProfile() throws Exception {
		IUIContext ui = getUI();
		ui.click(new MenuItemLocator("Tigerstripe/Active Profile Details..."));
		ui.wait(new ShellShowingCondition("Active Profile Details"));
		ui.click(new ButtonLocator("Reset Profile")); 
		ui.wait(new ShellShowingCondition("Reset Active profile to Factory Defaults?"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellShowingCondition("Active Profile Rollback"));
		ui.click(new ButtonLocator("OK"));
		
		LabeledTextLocator name = new LabeledTextLocator("Name");
		System.out.println(name.getText(ui));
		assertEquals("Factory DefaultProfile was not applied", "NO_NAME", name.getText(ui));
		
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Active Profile Details"));
	}
	
	/** 
	 * This is required to undeploy our test plugin before we start.
	 * Otherwise we don't know for sure that the plugin deployed properly!
	 * 
	 * @throws Exception
	 */
	public void testClearPlugin() throws Exception {
		IUIContext ui = getUI();
		
		// This is disabled because when the "undeploy" bit happens,
		// The Deployed Plugins Shell loses focus and I can't seem to get it back!
		
		ui.click(new MenuItemLocator("Tigerstripe/Plugins..."));
		ui.wait(new ShellShowingCondition("Deployed Tigerstripe Plugins"));
//		try {
//			ui.contextClick(new TableItemLocator(TestingConstants.NEW_PLUGIN_PROJECT_NAME+"("+TestingConstants.NEW_PLUGIN_PROJECT_VERSION+")"),
//			"Un-deploy");
//			ui.wait(new ShellShowingCondition("Un-Deploy Tigerstripe Plugin?"));
//			ui.click(new ButtonLocator("OK"));
//			ui.wait(new ShellDisposedCondition("Un-Deploy Tigerstripe Plugin?"));
//			
//			
//		} catch (com.windowtester.runtime.WidgetNotFoundException notFound){
//			// Not an issue as this just means the workspace didn't have the plugin in.
//			// And we were trying to remove it anyway!
//		}
		ui.close(new SWTWidgetLocator(Shell.class,"Deployed Tigerstripe Plugins"));
		ui.wait(new ShellDisposedCondition("Deployed Tigerstripe Plugins"));
	}
	
	
}