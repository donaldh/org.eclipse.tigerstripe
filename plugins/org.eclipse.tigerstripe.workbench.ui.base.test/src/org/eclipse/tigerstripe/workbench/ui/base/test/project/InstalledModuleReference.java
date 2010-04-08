package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.locator.SectionLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;
import com.windowtester.runtime.swt.locator.eclipse.WorkbenchLocator;

public class InstalledModuleReference extends UITestCaseSWT {

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
	 * No reference to installed module - No access possible
	 * Add Reference to installed module - verify access to contained artifacts
	 * Add local project with sameId - verify access is now to local instance
	 * Remove local project with sameId - verify access to contained artifacts of module
	 * Remove reference - No access possible
	 * 
	 * @throws Exception
	 */
	public void testInstalledModule() throws Exception {
		IUIContext ui = getUI();
		/*
		 * Set up
		 */
		ui.click(new MenuItemLocator("Window/Open Perspective/Other..."));
		ui.wait(new ShellShowingCondition("Open Perspective"));
		ui.click(new TableItemLocator("Tigerstripe"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Open Perspective"));

		ui
				.click(new ContributedToolItemLocator(
						"org.eclipse.tigerstripe.workbench.ui.menu.new.patterns.project.dropdown.org.eclipse.tigerstripe.workbench.base.Project"));
		ui.wait(new ShellShowingCondition("Create a new Tigerstripe Project"));
		ui.enterText("com.cisco.sample.project");
		ui.click(new XYLocator(new LabeledTextLocator("&Project Name:"), -10,
				-2));
		ui.click(new XYLocator(new LabeledTextLocator("Artifacts Package:"),
				-27, 2));
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Create a new Tigerstripe Project"));
		ui.click(new LabeledTextLocator("Id: "));
		ui.enterText("com.cisco.sample.project");
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		ui
				.contextClick(
						new TreeItemLocator(
								"com.cisco.sample.project/src",
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
						"New/Entity");
		ui.wait(new ShellShowingCondition("Create a new Entity"));
		ui.enterText("Entity");
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Create a new Entity"));
		/*
		 * No reference to installed module - No access possible
		 */
		ui.click(new ButtonLocator("Browse"));
		ui.wait(new ShellShowingCondition("Super Entity"));
		ui.assertThat(new TableItemLocator("com.cisco.InstalledEntity")
				.isVisible(false));
		ui.click(new ButtonLocator("Cancel"));
		ui.wait(new ShellDisposedCondition("Super Entity"));
		/*
		 * Add Reference to installed module - Verify access to contained
		 * artifacts
		 */
		ui
				.click(new CTabItemLocator(
						"com.cisco.sample.project/tigerstripe.xml"));
		ui.click(new CTabItemLocator("Dependencies"));
		ui.click(new ButtonLocator("Add", new SectionLocator(
				"Referenced Tigerstripe Projects")));
		ui.wait(new ShellShowingCondition("Select Tigerstripe Project"));
		ui.click(new TableItemLocator("com.cisco.testModule"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Select Tigerstripe Project"));
		ui.assertThat(new TableItemLocator("com.cisco.testModule").isVisible());
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));

		ui.click(new CTabItemLocator("Entity"));
		ui.click(new ButtonLocator("Browse"));
		ui.wait(new ShellShowingCondition("Super Entity"));
		ui.assertThat(new TableItemLocator("com.cisco.InstalledEntity")
				.isVisible());
		ui.click(new ButtonLocator("Cancel"));
		ui.wait(new ShellDisposedCondition("Super Entity"));
		/*
		 * Add local project with sameId - Verify access is now to local
		 * instance
		 */
		ui
				.click(new ContributedToolItemLocator(
						"org.eclipse.tigerstripe.workbench.ui.menu.new.patterns.project.dropdown.org.eclipse.tigerstripe.workbench.base.Project"));
		ui.wait(new ShellShowingCondition("Create a new Tigerstripe Project"));
		ui.enterText("com.cisco.testModule");
		ui
				.click(new XYLocator(new LabeledTextLocator("&Project Name:"),
						54, 8));
		ui.click(new XYLocator(new LabeledTextLocator("&Project Name:"), -43,
				14));
		ui.click(new XYLocator(new LabeledTextLocator("Artifacts Package:"),
				-115, 25));
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Create a new Tigerstripe Project"));
		ui.click(new LabeledTextLocator("Id: "));
		ui.enterText("com.cisco.testModule");
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		ui
				.contextClick(
						new TreeItemLocator(
								"com.cisco.testModule/src",
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
						"New/Entity");
		ui.wait(new ShellShowingCondition("Create a new Entity"));
		ui.enterText("LocalEntity");
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Create a new Entity"));

		ui.click(new CTabItemLocator("Entity"));
		ui.click(new ButtonLocator("Browse"));
		ui.wait(new ShellShowingCondition("Super Entity"));

		ui.assertThat(new TableItemLocator("com.mycompany.LocalEntity")
				.isVisible());
		ui.assertThat(new TableItemLocator("com.cisco.InstalledEntity")
				.isVisible(false));
		ui.click(new ButtonLocator("Cancel"));
		ui.wait(new ShellDisposedCondition("Super Entity"));
		/*
		 * Remove local project with sameId - Verify access to contained
		 * artifacts of module
		 */
		ui
				.contextClick(
						new TreeItemLocator(
								"com.cisco.testModule",
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
						"Delete");
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellShowingCondition("Delete Resources"));
		ui.click(new ButtonLocator(
				"&Delete project contents on disk (cannot be undone)"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Delete Resources"));

		ui.click(new CTabItemLocator("Entity"));
		ui.click(new ButtonLocator("Browse"));
		ui.wait(new ShellShowingCondition("Super Entity"));
		ui.assertThat(new TableItemLocator("com.cisco.InstalledEntity")
				.isVisible());
		ui.assertThat(new TableItemLocator("com.cisco.LocalEntity")
				.isVisible(false));
		ui.click(new ButtonLocator("Cancel"));
		ui.wait(new ShellDisposedCondition("Super Entity"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		/*
		 * Remove reference - No access possible
		 */
		ui
				.click(new CTabItemLocator(
						"com.cisco.sample.project/tigerstripe.xml"));
		ui.click(new CTabItemLocator("Dependencies"));
		ui.click(new TableItemLocator("com.cisco.testModule"));
		ui.click(new ButtonLocator("Remove", new SectionLocator(
				"Referenced Tigerstripe Projects")));
		ui.wait(new ShellShowingCondition("Remove References"));
		ui.click(new ButtonLocator("Yes"));
		ui.wait(new ShellDisposedCondition("Remove References"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));

		ui.click(new CTabItemLocator("Entity"));
		ui.click(new ButtonLocator("Browse"));
		ui.wait(new ShellShowingCondition("Super Entity"));
		ui.assertThat(new TableItemLocator("com.cisco.InstalledEntity")
				.isVisible(false));
		ui.click(new ButtonLocator("Cancel"));
		ui.wait(new ShellDisposedCondition("Super Entity"));
	}
}