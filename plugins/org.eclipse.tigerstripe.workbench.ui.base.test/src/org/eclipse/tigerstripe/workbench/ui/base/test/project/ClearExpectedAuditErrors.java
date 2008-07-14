package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.PullDownMenuItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class ClearExpectedAuditErrors extends UITestCaseSWT {

	private ArtifactHelper helper;
	private ArtifactAuditHelper auditHelper;
	
	
	public void testClearExpectedAuditErrors() throws Exception {
		IUIContext ui= getUI();
		
		// Let the auditor run
		Thread.sleep(500);
		// Some artifacts have errors on creation that we need to correct.
		// First check we get them
		auditHelper = new ArtifactAuditHelper(ui);
		String queryName = TestingConstants.QUERY_NAMES[0];

		// Just be sure we actually created the query!
		if (ProjectRecord.getArtifactList().containsValue(queryName)){
			auditHelper.checkUndefinedReturnType(queryName,true);
			// Update the returned type of our Query
			String pathToEntity = TestingConstants.NEW_MODEL_PROJECT_NAME+
			"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"/"+
			queryName;

			TreeItemLocator treeItem = new TreeItemLocator(
					pathToEntity,
					new ViewLocator(
					"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
			ui.click(2, treeItem);
			CTabItemLocator artifactEditor = new CTabItemLocator(
					queryName);
			ui.click(artifactEditor);
			ui.click(new CTabItemLocator("Details"));
			ui.click(new LabeledLocator(Button.class, "Returned Type: "));
			ui.wait(new ShellShowingCondition("Returned Type"));
			ui.click(new TableItemLocator(TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"."+
					TestingConstants.ENTITY_NAMES[0]));
			ui.click(new ButtonLocator("OK"));
			ui.wait(new ShellDisposedCondition("Returned Type"));
			//Save it
			ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));

			// Let the auditor run
			Thread.sleep(500);
			auditHelper.checkUndefinedReturnType(queryName,false);
			ui.close(new CTabItemLocator(queryName));
		}
		
	}
	
}
