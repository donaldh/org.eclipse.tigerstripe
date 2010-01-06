package org.eclipse.tigerstripe.workbench.ui.base.test.profile;

import org.eclipse.swt.widgets.Tree;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.NamedWidgetLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class AddStereos extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void testAddStereos() throws Exception {
		IUIContext ui = getUI();
		ui
				.click(
						2,
						new TreeItemLocator(
								"New Project/src/org/eclipse/Datatype0",
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui.click(new NamedWidgetLocator("Add_Stereo_Artifact"));
		ui.wait(new ShellShowingCondition("Stereotype Selection"));
		ui.click(2, new TableItemLocator("datatype_stereo"));
		ui.wait(new ShellDisposedCondition("Stereotype Selection"));
		ui
				.click(
						2,
						new TreeItemLocator(
								"New Project/src/org/eclipse/Datatype0/datatype0_testField0:String",
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui.click(new NamedWidgetLocator("Add_Stereotype_Attribute"));
		ui.wait(new ShellShowingCondition("Stereotype Selection"));
		ui.click(2, new TableItemLocator("attribute_stereo"));
		ui.wait(new ShellDisposedCondition("Stereotype Selection"));
		
		ui
				.click(2,
						new TreeItemLocator(
						"New Project/src/org/eclipse/Datatype0/datatype0_testLiteral0=\"0\"",
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		
		ui.click(new NamedWidgetLocator("Add_Stereotype_Literal"));
		ui.wait(new ShellShowingCondition("Stereotype Selection"));
		ui.click(2, new TableItemLocator("literal_stereo"));
		ui.wait(new ShellDisposedCondition("Stereotype Selection"));
		ui
				.click(
						2,
						new TreeItemLocator(
								"New Project/src/org/eclipse/Datatype0/datatype0_testMethod0():void",
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui.click(new NamedWidgetLocator("Add_Stereotype_Method"));
		ui.wait(new ShellShowingCondition("Stereotype Selection"));
		ui.click(2, new TableItemLocator("method_stereo"));
		ui.wait(new ShellDisposedCondition("Stereotype Selection"));
		
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
	}

}