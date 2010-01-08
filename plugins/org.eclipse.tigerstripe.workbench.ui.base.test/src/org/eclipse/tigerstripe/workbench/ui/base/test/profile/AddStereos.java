package org.eclipse.tigerstripe.workbench.ui.base.test.profile;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.ArtifactUtils;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.NamedWidgetLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;
import com.windowtester.runtime.swt.locator.eclipse.WorkbenchLocator;

public class AddStereos extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void testAddStereos() throws Exception {
		IUIContext ui = getUI();
		ArtifactUtils.createArtifact(ui, TestingConstants.DATATYPE_NAMES[1], "Datatype");
		String prefix = TestingConstants.DATATYPE_NAMES[1].toLowerCase() + "_";
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		
		
		ui.click(new CTabItemLocator(TestingConstants.DATATYPE_NAMES[1]));
		ui.click(2,	new TreeItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME+
				"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+"/"+
				TestingConstants.DATATYPE_NAMES[1],
				new ViewLocator(
					"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		GuiUtils.maxminTab(ui, TestingConstants.DATATYPE_NAMES[1]);

		ui.click(new NamedWidgetLocator("Add_Stereo_Artifact"));
		ui.wait(new ShellShowingCondition("Stereotype Selection"));
		ui.click(2, new TableItemLocator(TestingConstants.DATATYPE_STEREO));
		ui.wait(new ShellDisposedCondition("Stereotype Selection"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		GuiUtils.maxminTab(ui, TestingConstants.DATATYPE_NAMES[1]);
			
		ui.click(
						2,
						new TreeItemLocator(
								TestingConstants.NEW_MODEL_PROJECT_NAME+
								"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+"/"+
								TestingConstants.DATATYPE_NAMES[1]+"/"+prefix+TestingConstants.ATTRIBUTE_NAMES[0]+":String",
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		GuiUtils.maxminTab(ui, TestingConstants.DATATYPE_NAMES[1]);

		ui.click(new NamedWidgetLocator("Add_Stereotype_Attribute"));
		ui.wait(new ShellShowingCondition("Stereotype Selection"));
		ui.click(2, new TableItemLocator(TestingConstants.ATTRIBUTE_STEREO));
		ui.wait(new ShellDisposedCondition("Stereotype Selection"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		GuiUtils.maxminTab(ui, TestingConstants.DATATYPE_NAMES[1]);
		
		SWTWidgetLocator sectionLabel = new SWTWidgetLocator(Label.class, "&Attributes");
		// collapse the Attributes sections
		ui.click(sectionLabel);
		sectionLabel = new SWTWidgetLocator(Label.class, "Constants");
		// expand the Constants sections
		ui.click(sectionLabel);
		
		ui
				.click(2,
						new TreeItemLocator(
								TestingConstants.NEW_MODEL_PROJECT_NAME+
								"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+"/"+
								TestingConstants.DATATYPE_NAMES[1]+"/"+prefix+TestingConstants.LITERAL_NAMES[0]+"=\"0\"",
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		GuiUtils.maxminTab(ui, TestingConstants.DATATYPE_NAMES[1]);
		
		ui.click(new NamedWidgetLocator("Add_Stereotype_Literal"));
		ui.wait(new ShellShowingCondition("Stereotype Selection"));
		ui.click(2, new TableItemLocator(TestingConstants.LITERAL_STEREO));
		ui.wait(new ShellDisposedCondition("Stereotype Selection"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		GuiUtils.maxminTab(ui, TestingConstants.DATATYPE_NAMES[1]);
		
		sectionLabel = new SWTWidgetLocator(Label.class, "Constants");
		// collapse the Constants sections
		ui.click(sectionLabel);
		
		sectionLabel = new SWTWidgetLocator(Label.class, "Methods");
		// expand the Methods sections
		ui.click(sectionLabel);
		
		ui
				.click(
						2,
						new TreeItemLocator(
								TestingConstants.NEW_MODEL_PROJECT_NAME+
								"/src/"+TestingConstants.DEFAULT_ARTIFACT_PACKAGE_AS_PATH+"/"+
								TestingConstants.DATATYPE_NAMES[1]+"/"+prefix+TestingConstants.METHOD_NAMES[0]+"():void",
								new ViewLocator(
										"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		GuiUtils.maxminTab(ui, TestingConstants.DATATYPE_NAMES[1]);
		ui.click(new NamedWidgetLocator("Add_Stereotype_Method"));
		ui.wait(new ShellShowingCondition("Stereotype Selection"));
		ui.click(2, new TableItemLocator(TestingConstants.METHOD_STEREO));
		ui.wait(new ShellDisposedCondition("Stereotype Selection"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		GuiUtils.maxminTab(ui, TestingConstants.DATATYPE_NAMES[1]);
		
		sectionLabel = new SWTWidgetLocator(Label.class, "Methods");
		// collapse the Methods sections
		ui.click(sectionLabel);
		
		
		
	}

}