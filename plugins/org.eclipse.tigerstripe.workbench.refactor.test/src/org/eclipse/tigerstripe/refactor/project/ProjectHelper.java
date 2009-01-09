package org.eclipse.tigerstripe.refactor.project;

import org.eclipse.swt.widgets.Tree;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WT;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.FilteredTreeItemLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class ProjectHelper {

	/**
	 * Main test method.
	 */
	public void loadProjectFromCVS(IUIContext ui) throws Exception {
		
		ui.contextClick(new SWTWidgetLocator(Tree.class, new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")), "&Import...");
		ui.wait(new ShellShowingCondition("Import"));
		ui.click(new FilteredTreeItemLocator("CVS/Projects from CVS"));
		ui.click(new ButtonLocator("&Next >"));
		ui.enterText("dev.eclipse.org");		
		ui.keyClick(WT.TAB);
		ui.enterText("/cvsroot/technology");
		ui.keyClick(WT.TAB);
		ui.enterText("anonymous");
		ui.click(new ButtonLocator("&Next >"));
		ui.enterText("org.eclipse.tigerstripe/samples/test-models/model-refactoring");
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Checkout from CVS"));
		
		Thread.sleep(5000); // Let the workspace build
		
		// Disconnect from CVS tso we don't have to deal with all of the decorations!
		
		ui
		.contextClick(
				new TreeItemLocator(
						"model-refactoring   [dev.eclipse.org]",
						new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
		"Team/Disconnect...");
		ui.wait(new ShellShowingCondition("Confirm Disconnect from CVS"));
		ui.click(new ButtonLocator("&Yes"));
		ui.wait(new ShellDisposedCondition("Confirm Disconnect from CVS"));
		ui.wait(new ShellDisposedCondition("Progress Information"));
		
	}

	public void deleteDiagrams(IUIContext ui) throws Exception {
		ViewLocator view = new ViewLocator(
			"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		String project="model-refactoring";
		ui.click(new TreeItemLocator(
				project+"/src/simple/moved/inside-moved",
				view));
		ui.click(
				1,
				new TreeItemLocator(
						project+"/src/simple/default",
						view),
						WT.CTRL);
		ui.click(
				1,
				new TreeItemLocator(
						project+"/outside-class-diagram",
						view),
						WT.CTRL);
		ui.click(
				1,
				new TreeItemLocator(
						project+"/outside-instance-diagram",
						view),
						WT.CTRL);
		ui.keyClick(WT.DEL);
		ui.wait(new ShellShowingCondition("Delete..."));
		ui.click(new ButtonLocator("&Yes"));
		ui.wait(new ShellDisposedCondition("Delete..."));
	}

}