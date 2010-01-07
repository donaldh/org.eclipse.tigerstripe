package org.eclipse.tigerstripe.workbench.ui.base.test.profile;

import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.FilteredTreeItemLocator;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class Explorer extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void testExplorer() throws Exception {
		IUIContext ui = getUI();
		
		String prefix = TestingConstants.DATATYPE_NAMES[1].toLowerCase() + "_";
		
		
		ui.click(new MenuItemLocator("Window/Preferences"));
		ui.wait(new ShellShowingCondition("Preferences"));
		ui.click(new FilteredTreeItemLocator("Tigerstripe/Explorer"));
		ui.click(new ButtonLocator("on Artifacts"));
		ui.click(new ButtonLocator("on Attributes"));
		ui.click(new ButtonLocator("on Methods"));
		ui.click(new ButtonLocator("on Literals"));
		ui.click(new ButtonLocator("on Method Arguments"));
		ui.click(new ButtonLocator("on relationship ends"));
		ui.click(new ButtonLocator("&Apply"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Preferences"));
		ui
				.click(new TreeItemLocator(
						TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+
						"/<<"+TestingConstants.DATATYPE_STEREO+">>"+TestingConstants.DATATYPE_NAMES[1],
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui
				.click(new TreeItemLocator(
						TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+
						"/<<"+TestingConstants.DATATYPE_STEREO+">>"+TestingConstants.DATATYPE_NAMES[1]+
						"/<<"+TestingConstants.ATTRIBUTE_STEREO+">>"+
						prefix+TestingConstants.ATTRIBUTE_NAMES[0]+":String",
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui
				.click(new TreeItemLocator(
						TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+
						"/<<"+TestingConstants.DATATYPE_STEREO+">>"+TestingConstants.DATATYPE_NAMES[1]+
                        "/<<"+TestingConstants.LITERAL_STEREO+">>"+
                        prefix+TestingConstants.LITERAL_NAMES[0]+"=\"0\"",
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui
				.click(new TreeItemLocator(
						TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+
						"/<<"+TestingConstants.DATATYPE_STEREO+">>"+TestingConstants.DATATYPE_NAMES[1]+
						"/<<"+TestingConstants.METHOD_STEREO+">>"+
						prefix+TestingConstants.METHOD_NAMES[0]+"():void",
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		
		ui.click(new MenuItemLocator("Window/Preferences"));
		ui.wait(new ShellShowingCondition("Preferences"));
		ui.click(new FilteredTreeItemLocator("Tigerstripe/Explorer"));
		ui.click(new ButtonLocator("on Artifacts"));
		ui.click(new ButtonLocator("on Attributes"));
		ui.click(new ButtonLocator("on Methods"));
		ui.click(new ButtonLocator("on Method Arguments"));
		ui.click(new ButtonLocator("on Literals"));
		ui.click(new ButtonLocator("on Methods"));
		ui.click(new ButtonLocator("on relationship ends"));
		ui.click(new ButtonLocator("&Apply"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Preferences"));
		ui
				.click(new TreeItemLocator(
						TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+"/"+
						TestingConstants.DATATYPE_NAMES[1],
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui
				.click(new TreeItemLocator(
						TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+"/"+
						TestingConstants.DATATYPE_NAMES[1]+"/"+
						prefix+TestingConstants.ATTRIBUTE_NAMES[0]+":String",
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui
				.click(new TreeItemLocator(
						TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+"/"+
						TestingConstants.DATATYPE_NAMES[1]+"/"+
						prefix+TestingConstants.LITERAL_NAMES[0]+"=\"0\"",
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui
				.click(new TreeItemLocator(
						TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+"/"+
						TestingConstants.DATATYPE_NAMES[1]+"/"+
						prefix+TestingConstants.METHOD_NAMES[0]+"():void",
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
	}

}