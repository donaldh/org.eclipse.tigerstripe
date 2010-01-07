package org.eclipse.tigerstripe.workbench.ui.base.test.utils;

import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class ArtifactUtils {

	public static void createArtifact(IUIContext ui, String thisArtifactName, String myType) throws Exception{
		ui.click(new TreeItemLocator(
				TestingConstants.NEW_MODEL_PROJECT_NAME,
				new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui.contextClick(
				new TreeItemLocator(
						TestingConstants.NEW_MODEL_PROJECT_NAME+"/src",
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
				"New/"+myType);
		
		
		ui.wait(new ShellShowingCondition("Create a new " + myType),
				60 * 1000 * 2);
		ui.click(new LabeledTextLocator("Name:"));
		ui.enterText(thisArtifactName);
		
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Create a new " + myType));
		
		GuiUtils.maxminTab(ui, thisArtifactName);
		
		// Collapse default Section
		SWTWidgetLocator attributesSection = new SWTWidgetLocator(
				Label.class, "&Attributes");
		ui.click(attributesSection);
		
		String prefix = thisArtifactName.toLowerCase() + "_";
		ArtifactHelper.newAttribute(ui, thisArtifactName, prefix
				+ TestingConstants.ATTRIBUTE_NAMES[0]);

		ArtifactHelper.newLiteral(ui, thisArtifactName, prefix
				+ TestingConstants.LITERAL_NAMES[0]);

		ArtifactHelper.newMethod(ui, thisArtifactName, prefix
				+ TestingConstants.METHOD_NAMES[0]);
		
		GuiUtils.maxminTab(ui, thisArtifactName);
		
		
		
	}
	
}
