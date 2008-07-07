package org.eclipse.tigerstripe.workbench.ui.base.test.generator;

import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ProjectRecord;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.SectionLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class Generate extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void testGenerate() throws Exception {
		IUIContext ui = getUI();
		GenerateHelper helper = new GenerateHelper(ui);
		
		helper.enablePlugin();
		
		helper.runGenerate();
		helper.checkGlobal();
		helper.checkArtifact();
		helper.checkExtras();
		
	}

}