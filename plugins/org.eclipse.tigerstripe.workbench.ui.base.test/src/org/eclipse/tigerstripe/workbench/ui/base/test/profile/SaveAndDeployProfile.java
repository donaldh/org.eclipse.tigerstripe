package org.eclipse.tigerstripe.workbench.ui.base.test.profile;

import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class SaveAndDeployProfile extends UITestCaseSWT {

	public void testSaveAndDeployProfile() throws Exception {
		IUIContext ui = getUI();
		// Save
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		
		// Make active
		ui.contextClick(new TreeItemLocator("Profiles/"+TestingConstants.NEW_PROFILE_NAME+".wbp",
				new ViewLocator("org.eclipse.jdt.ui.PackageExplorer")),
				"Profile/Set as active profile.");
		ui.wait(new ShellShowingCondition("Save as Active Profile"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Save as Active Profile"));
		ui.wait(new ShellShowingCondition("Success"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Success"));
		
		// Close the editor
		ui.click(new XYLocator(new CTabItemLocator("Profiles/"+TestingConstants.NEW_PROFILE_NAME+".wbp"),
				152, 13));
	}
	
}
