package org.eclipse.tigerstripe.workbench.ui.generator.test.suite;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;
import com.windowtester.runtime.swt.locator.eclipse.WorkbenchLocator;

public class CleanWorkspace extends UITestCaseSWT {

	protected void setUp() throws Exception {
		super.setUp();
		IUIContext ui = getUI();
		ui.ensureThat(new WorkbenchLocator().hasFocus());
		ui.ensureThat(ViewLocator.forName("Welcome").isClosed());
		ui.ensureThat(new WorkbenchLocator().isMaximized());
	}

	
	
	/**
	 * Main test method.
	 */
	public void testCleanWorkspace() throws Exception {
		
		IUIContext ui = getUI();

	/* 
	 * This is required to set the profile to defaults before we start.
	 * Otherwise we don't know for sure that the profile will be applied properly!
	 * 
	 */

		ui.click(new MenuItemLocator("Tigerstripe/Active Profile Details..."));
		ui.wait(new ShellShowingCondition("Active Profile Details"));
		ui.click(new ButtonLocator("Reset Profile")); 
		ui.wait(new ShellShowingCondition("Reset Active profile to Factory Defaults?"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellShowingCondition("Active Profile Rollback"));
		ui.click(new ButtonLocator("OK"));
		
		LabeledTextLocator name = new LabeledTextLocator("Name");
		assertEquals("Factory DefaultProfile was not applied", "NO_NAME", name.getText(ui));
		
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Active Profile Details"));

	
	/*
	 * This is required to undeploy our test plugin before we start.
	 * Otherwise we don't know for sure that the plugin deployed properly!
	 * 
	 */

		
		// This is disabled because when the "undeploy" bit happens,
		// The Deployed Plugins Shell loses focus and I can't seem to get it back!
		
		ui.click(new MenuItemLocator("Tigerstripe/Plugins..."));
		ui.wait(new ShellShowingCondition("Deployed Tigerstripe Plugins"));
		try {
			ui.contextClick(new TableItemLocator(TestingConstants.NEW_PLUGIN_PROJECT_NAME+"("+TestingConstants.NEW_PLUGIN_PROJECT_VERSION+")"),
			"Un-deploy");
			ui.wait(new ShellShowingCondition("Un-Deploy Tigerstripe Plugin?"));
			ui.click(new ButtonLocator("OK"));
			ui.wait(new ShellDisposedCondition("Un-Deploy Tigerstripe Plugin?"));
			ui.click(new ButtonLocator("OK"));

			
			
		} catch (com.windowtester.runtime.WidgetNotFoundException notFound){
			// Not an issue as this just means the workspace didn't have the plugin in.
			// And we were trying to remove it anyway!
			
		}
		ui.wait(new ShellDisposedCondition("Deployed Tigerstripe Plugins"));
		
	}
	
	
}