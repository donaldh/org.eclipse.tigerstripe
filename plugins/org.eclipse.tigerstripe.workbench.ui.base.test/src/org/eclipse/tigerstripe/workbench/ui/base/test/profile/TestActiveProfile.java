package org.eclipse.tigerstripe.workbench.ui.base.test.profile;

import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;

public class TestActiveProfile extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void testProfileActive() throws Exception {
		IUIContext ui = getUI();
		ui.click(new MenuItemLocator("Tigerstripe/Active Profile Details..."));
		ui.wait(new ShellShowingCondition("Active Profile Details"));
		LabeledTextLocator name = new LabeledTextLocator("Name");
		assertEquals("Deployed Profile name does not match", TestingConstants.NEW_PROFILE_NAME, name.getText(ui));

		LabeledTextLocator version = new LabeledTextLocator("Version");
		assertEquals("Deployed Profile version does not match",TestingConstants.NEW_PROFILE_VERSION, version.getText(ui));
		
		LabeledTextLocator description = new LabeledTextLocator("Description");
		assertEquals("Deployed Profile description does not match",TestingConstants.NEW_PROFILE_DESCRIPTION, description.getText(ui));
		
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Active Profile Details"));
	}

}