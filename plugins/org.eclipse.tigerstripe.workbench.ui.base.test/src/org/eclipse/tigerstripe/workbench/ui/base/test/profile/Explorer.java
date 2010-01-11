package org.eclipse.tigerstripe.workbench.ui.base.test.profile;

import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.FilteredTreeItemLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.locator.NamedWidgetLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class Explorer extends UITestCaseSWT {

	private String prefix = TestingConstants.DATATYPE_NAMES[1].toLowerCase() + "_";

	/**
	 * Main test method.
	 */
	public void testExplorer() throws Exception {
		IUIContext ui = getUI();
		enable(ui);
		checkExplorerOn(ui);
		
		// Need to do an and remove to see if Explorer is updated
		// in "real time"
		addOne(ui);
		checkOneAdded(ui);
		removeOne(ui);
		checkOneRemoved(ui);
		disable(ui);
		checkExplorerOff(ui);
	}

	private void enable(IUIContext ui) throws Exception{

		ui.click(new MenuItemLocator("Window/Preferences"));
		ui.wait(new ShellShowingCondition("Preferences"));
		ui.click(new FilteredTreeItemLocator("Tigerstripe/Explorer"));
		ui.click(new ButtonLocator("on Artifacts"));
		ui.click(new ButtonLocator("on Attributes"));
		ui.click(new ButtonLocator("on Methods"));
		ui.click(new ButtonLocator("on Literals"));
		ui.click(new ButtonLocator("on Method Arguments"));
		ui.click(new ButtonLocator("on Relationship ends"));
		ui.click(new ButtonLocator("&Apply"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Preferences"));
	}


	private void disable(IUIContext ui) throws Exception{

		ui.click(new MenuItemLocator("Window/Preferences"));
		ui.wait(new ShellShowingCondition("Preferences"));
		ui.click(new FilteredTreeItemLocator("Tigerstripe/Explorer"));
		ui.click(new ButtonLocator("on Artifacts"));
		ui.click(new ButtonLocator("on Attributes"));
		ui.click(new ButtonLocator("on Methods"));
		ui.click(new ButtonLocator("on Method Arguments"));
		ui.click(new ButtonLocator("on Literals"));
		ui.click(new ButtonLocator("on Methods"));
		ui.click(new ButtonLocator("on Relationship ends"));
		ui.click(new ButtonLocator("&Apply"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Preferences"));

	}
	
	private void addOne(IUIContext ui) throws Exception{
		ui.click(new CTabItemLocator(TestingConstants.DATATYPE_NAMES[1]));
		ui.click(2,	new TreeItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME+
				"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+"/"+
				"/<<"+TestingConstants.DATATYPE_STEREO+">>"+TestingConstants.DATATYPE_NAMES[1],
				new ViewLocator(
					"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		GuiUtils.maxminTab(ui, TestingConstants.DATATYPE_NAMES[1]);

		ui.click(new NamedWidgetLocator("Add_Stereo_Artifact"));
		ui.wait(new ShellShowingCondition("Stereotype Selection"));
		ui.click(2, new TableItemLocator(TestingConstants.DATATYPE_STEREO_TWO));
		ui.wait(new ShellDisposedCondition("Stereotype Selection"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		GuiUtils.maxminTab(ui, TestingConstants.DATATYPE_NAMES[1]);
	}
	
	private void removeOne(IUIContext ui) throws Exception{
		ui.click(new CTabItemLocator(TestingConstants.DATATYPE_NAMES[1]));
		ui.click(2,	new TreeItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME+
				"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+"/"+
				TestingConstants.DATATYPE_NAMES[1],
				new ViewLocator(
					"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		GuiUtils.maxminTab(ui, TestingConstants.DATATYPE_NAMES[1]);

		ui.click(new TableItemLocator(TestingConstants.DATATYPE_STEREO_TWO));
		ui.click(new NamedWidgetLocator("Remove_Stereo_Artifact"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		GuiUtils.maxminTab(ui, TestingConstants.DATATYPE_NAMES[1]);
	}
	
	private void checkOneAdded(IUIContext ui) throws Exception{
		// Note order is alphabetical!
		ui
		.click(new TreeItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+
				"/<<"+TestingConstants.DATATYPE_STEREO_TWO+","+
				TestingConstants.DATATYPE_STEREO+">>"+TestingConstants.DATATYPE_NAMES[1],
				new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
	}
	
	private void checkOneRemoved(IUIContext ui) throws Exception{
		ui
		.click(new TreeItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+
				"/<<"+TestingConstants.DATATYPE_STEREO+">>"+TestingConstants.DATATYPE_NAMES[1],
				new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
	}
	
	private void checkExplorerOn(IUIContext ui) throws Exception{
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

		//TODO - When WT fixed
		//		ui
		//				.click(new TreeItemLocator(
		//						TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+
		//						"/<<"+TestingConstants.DATATYPE_STEREO+">>"+TestingConstants.DATATYPE_NAMES[1]+
		//                        "/<<"+TestingConstants.LITERAL_STEREO+">>"+
		//                        prefix+TestingConstants.LITERAL_NAMES[0]+"=\"0\"",
		//						new ViewLocator(
		//								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		//		ui
		//				.click(new TreeItemLocator(
		//						TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+
		//						"/<<"+TestingConstants.DATATYPE_STEREO+">>"+TestingConstants.DATATYPE_NAMES[1]+
		//						"/<<"+TestingConstants.METHOD_STEREO+">>"+
		//						prefix+TestingConstants.METHOD_NAMES[0]+"():void",
		//						new ViewLocator(
		//								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));

	}
	private void checkExplorerOff(IUIContext ui) throws Exception{
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
		//TODO When WT is fixed
		//		ui
		//				.click(new TreeItemLocator(
		//						TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+"/"+
		//						TestingConstants.DATATYPE_NAMES[1]+"/"+
		//						prefix+TestingConstants.LITERAL_NAMES[0]+"=\"0\"",
		//						new ViewLocator(
		//								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		//		ui
		//				.click(new TreeItemLocator(
		//						TestingConstants.NEW_MODEL_PROJECT_NAME+"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+"/"+
		//						TestingConstants.DATATYPE_NAMES[1]+"/"+
		//						prefix+TestingConstants.METHOD_NAMES[0]+"():void",
		//						new ViewLocator(
		//								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
	}

}