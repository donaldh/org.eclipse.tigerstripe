/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.refactor.artifact;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.ui.visualeditor.test.finders.LocatorHelper;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WT;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class Exception0_to_Exception00 extends UITestCaseSWT {

	private static String project = "model-refactoring";

	public static void doChangeThroughDiagramInSamePackage(IUIContext ui)
			throws Exception {
		LocatorHelper helper = new LocatorHelper();
		ui.click(new CTabItemLocator("default.wvd"));
		IWidgetLocator tfl = helper.getNameEditLocator(ui, "Exception0");
		ui.click(tfl);
		ui.click(tfl);
		Thread.sleep(1000);
		ui.click(tfl);
		ui.click(new SWTWidgetLocator(Text.class, new SWTWidgetLocator(
				FigureCanvas.class)));
		ui.keyClick(WT.END);
		ui.enterText("0");
		ui.keyClick(WT.CR);

	}

	public static void checkDiagrams(IUIContext ui) throws Exception {
		LocatorHelper helper = new LocatorHelper();
		ui.click(new CTabItemLocator("default.wvd"));
		String artifactPrefix = "";
		internalCheckDiagram(ui, helper, artifactPrefix);
		artifactPrefix = "simple.";
		ui.click(new CTabItemLocator("inside-moved.wvd"));
		internalCheckDiagram(ui, helper, artifactPrefix);
		ui.click(new CTabItemLocator("outside-class-diagram.wvd"));
		internalCheckDiagram(ui, helper, artifactPrefix);

	}

	public static void internalCheckDiagram(IUIContext ui,
			LocatorHelper helper, String artifactPrefix) {

		// Basic rename
		try {
			ui.click(helper.getExceptionLocator(ui, artifactPrefix
					+ "Exception00"));
		} catch (Exception e) {
			fail("Refactored Entity not found on diagram");
		}
	}

	private static String[] editors = { "Ent2" };

	public static void openRelatedEditors(IUIContext ui) throws Exception {
		// Make sure any related editors are open during the change
		ViewLocator view = new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");

		ui.click(2, new TreeItemLocator(project + "/src/simple/Exception0",
				view));

		for (String editor : editors) {
			ui.click(2, new TreeItemLocator(project + "/src/simple/" + editor,
					view));
		}

	}

	public static void saveAndCloseRelatedEditors(IUIContext ui)
			throws Exception {

		// NOTE: The "own" editor gets closed by the rename action!
		for (String editor : editors) {
			ui.click(new CTabItemLocator("*" + editor));
			ui
					.click(new ContributedToolItemLocator(
							"org.eclipse.ui.file.save"));
			ui.close(new CTabItemLocator(editor));
		}

	}

	public static void checkEditorUpdated(IUIContext ui) throws Exception {
		ui.click(new CTabItemLocator("*Ent2"));
		// Close Attributes and Open Methods section
		GuiUtils.maxminTab(ui, "*Ent2");
		ui.click(new SWTWidgetLocator(Label.class, "&Attributes"));
		ui.click(new SWTWidgetLocator(Label.class, "Methods"));
		TableItemLocator attributeNameInTable = new TableItemLocator(
				"method0(Ent10):Ent10[0..1]");
		ui.click(attributeNameInTable);

		TableItemLocator exception = new TableItemLocator("simple.Exception00");
		// This is a dodgy test - it will actually throw an exception rather
		// than fail the assertion!
		// assertEquals("simple.Exception00", exception.getText(ui));
		GuiUtils.maxminTab(ui, "*Ent2");
	}

	public static void doChangeThroughExplorer(IUIContext ui) throws Exception {
		ViewLocator view = new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");

		ui.contextClick(new TreeItemLocator(project + "/src/simple/Exception0",
				view), "Refactor Model/Rename...");
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellShowingCondition("Rename Compilation Unit"));
		LabeledTextLocator locator = new LabeledTextLocator("New na&me:");
		ui.keyClick(SWT.CTRL, 'a');
		ui.click(locator);
		ui.enterText("Exception00");
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Rename Compilation Unit"));

		// Let the updates happen!
		Thread.sleep(500);
	}

	public static void checkExplorerUpdates(IUIContext ui) throws Exception {
		ViewLocator view = new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");

		// Check for ourself!
		ArtifactHelper.checkArtifactInExplorer(ui, project, "simple",
				"Exception00");

	}

	// In here we look for the changes that don't necessarily appear on the UI!
	@SuppressWarnings("deprecation")
	public static void checkAPI() throws Exception {
		// First check that the old Entity no longer exists!
		IAbstractTigerstripeProject aProject = TigerstripeCore
				.findProject(project);
		ITigerstripeModelProject modelProject = (ITigerstripeModelProject) aProject;
		IArtifactManagerSession mgrSession = modelProject
				.getArtifactManagerSession();
		// First check that the old Entity no longer exists!
		IAbstractArtifact event0 = mgrSession
				.getArtifactByFullyQualifiedName("simple.Exception0");
		assertNull("Old artifact is still being returned from the Art Mgr",
				event0);

		IAbstractArtifact event00 = mgrSession
				.getArtifactByFullyQualifiedName("simple.Exception00");
		assertNotNull(
				"New artifact is still not being returned from the Art Mgr",
				event00);

	}

}
