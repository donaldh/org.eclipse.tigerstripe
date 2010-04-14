package org.eclipse.tigerstripe.workbench.ui.base.test.generator;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.FilteredTreeItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;
import com.windowtester.runtime.swt.locator.eclipse.WorkbenchLocator;

public class TestInstallableModuleGeneration extends UITestCaseSWT {

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		IUIContext ui = getUI();
		ui.ensureThat(new WorkbenchLocator().hasFocus());
		ui.ensureThat(ViewLocator.forName("Welcome").isClosed());
		ui.ensureThat(new WorkbenchLocator().isMaximized());
	}

	/**
	 * Create installable module from the UI
	 */
	public void testInstallableModuleGeneration() throws Exception {
		IUIContext ui = getUI();
		ui.click(new MenuItemLocator("Window/Open Perspective/Other..."));
		ui.wait(new ShellShowingCondition("Open Perspective"));
		ui.click(new TableItemLocator("Tigerstripe"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Open Perspective"));
		ui
				.click(new ContributedToolItemLocator(
						"org.eclipse.tigerstripe.workbench.ui.menu.new.patterns.project.dropdown.org.eclipse.tigerstripe.workbench.base.Project"));
		ui.wait(new ShellShowingCondition("Create a new Tigerstripe Project"));
		ui.enterText("com.cisco.test");
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Create a new Tigerstripe Project"));
		ui.click(new LabeledTextLocator("Id: "));
		ui.enterText("com.cisco.test");
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		ui
				.contextClick(
						new TreeItemLocator(
								"com.cisco.test/src",
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
						"New/Entity");
		ui.wait(new ShellShowingCondition("Create a new Entity"));
		ui.enterText("Entity");
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Create a new Entity"));
		ui
				.contextClick(
						new TreeItemLocator(
								"com.cisco.test",
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
						"Export...");
		ui.wait(new ShellShowingCondition("Export"));
		ui.click(new FilteredTreeItemLocator("Tigerstripe/Tigerstripe Module"));
		ui.click(new ButtonLocator("&Next >"));
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Export to Tigerstripe module"));
	}

}