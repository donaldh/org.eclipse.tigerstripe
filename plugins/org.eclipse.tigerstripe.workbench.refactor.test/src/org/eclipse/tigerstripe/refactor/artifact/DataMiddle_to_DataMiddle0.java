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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.ui.visualeditor.test.finders.LocatorHelper;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute3EditPart;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WT;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.locator.WidgetReference;
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

public class DataMiddle_to_DataMiddle0 extends UITestCaseSWT {

	private static String project = "model-refactoring";
	private static String[] editors = { "DataBottom", "Ent10", "AssociatedEnt" };

	public static void doChangeThroughDiagramInSamePackage(IUIContext ui)
			throws Exception {
		LocatorHelper helper = new LocatorHelper();
		ui.click(new CTabItemLocator("default.wvd"));
		IWidgetLocator tfl = helper.getNameEditLocator(ui, "DataMiddle");
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
		internalCheckDiagram(ui, helper, artifactPrefix, artifactPrefix,
				"DataMiddle0", "Ent10");
		artifactPrefix = "simple.";
		ui.click(new CTabItemLocator("inside-moved.wvd"));
		internalCheckDiagram(ui, helper, artifactPrefix, artifactPrefix,
				"DataMiddle0", "Ent10");
		ui.click(new CTabItemLocator("outside-class-diagram.wvd"));
		internalCheckDiagram(ui, helper, artifactPrefix, artifactPrefix,
				"DataMiddle0", "Ent10");

	}

	public static void checkDiagramsAfterMove(IUIContext ui) throws Exception {
		LocatorHelper helper = new LocatorHelper();
		ui.click(new CTabItemLocator("default.wvd"));

		String myArtifactPrefix = "simple.moved.";
		internalCheckDiagram(ui, helper, "", myArtifactPrefix, "DataMiddle",
				"Ent1");

		ui.click(new CTabItemLocator("inside-moved.wvd"));
		internalCheckDiagram(ui, helper, "simple.", "", "DataMiddle", "Ent1");
		ui.click(new CTabItemLocator("outside-class-diagram.wvd"));
		internalCheckDiagram(ui, helper, "simple.", myArtifactPrefix,
				"DataMiddle", "Ent1");

	}

	public static void internalCheckDiagram(IUIContext ui,
			LocatorHelper helper, String artifactPrefix,
			String myArtifactPrefix, String artifactName, String entName)
			throws Exception {

		// Basic rename
		try {
			ui.click(helper.getDatatypeLocator(ui, myArtifactPrefix
					+ artifactName));
		} catch (Exception e) {
			fail("Refactored Entity '" + myArtifactPrefix + artifactName
					+ "' not found on diagram");
		}
		IWidgetLocator attr = helper.getManagedEntityAttributeLocator(ui,
				myArtifactPrefix + entName, "attribute1");
		WidgetReference attrRef = (WidgetReference) attr;
		Attribute3EditPart.AttributeLabelFigure fig = (Attribute3EditPart.AttributeLabelFigure) attrRef
				.getWidget();
		String figText = fig.getText();
		// Return type should be our new Ent, as should the arg type - ie
		// By default, the internal packages are hidden
		String expectedText = "+attribute1:" + artifactName;
		assertEquals("Attribute Ref not updated on diagram", expectedText,
				figText);

		attr = helper.getManagedEntityAttributeLocator(ui, artifactPrefix
				+ "AssociatedEnt", "attribute0");
		attrRef = (WidgetReference) attr;
		fig = (Attribute3EditPart.AttributeLabelFigure) attrRef.getWidget();
		figText = fig.getText();
		// Return type should be our new Ent, as should the arg type - ie
		// By default, the internal packages are hidden
		expectedText = "+attribute0:" + artifactName;
		assertEquals("Attribute Ref not updated on diagram", expectedText,
				figText);

	}

	public static void openRelatedEditors(IUIContext ui) throws Exception {
		// Make sure any related editors are open during the change
		ViewLocator view = new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");

		ui.click(2, new TreeItemLocator(project + "/src/simple/DataMiddle",
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
		ui.click(new CTabItemLocator("*DataBottom"));
		// Check for Extends in this one
		LabeledTextLocator extend = new LabeledTextLocator("Extends: ");
		assertEquals("Extended type not updated in Editor",
				"simple.DataMiddle0", extend.getText(ui));

		ui.click(new CTabItemLocator("*Ent10"));
		// Check for An AttributeRef
		TableItemLocator attributeNameInTable = new TableItemLocator(
				"attribute1");
		ui.click(attributeNameInTable);
		LabeledTextLocator type = new LabeledTextLocator("Type: ");
		assertEquals("Referenced type not updated in Editor",
				"simple.DataMiddle0", type.getText(ui));

		ui.click(new CTabItemLocator("*AssociatedEnt"));
		// Check for An AttributeRef
		attributeNameInTable = new TableItemLocator("attribute0");
		ui.click(attributeNameInTable);
		type = new LabeledTextLocator("Type: ");
		assertEquals("Referenced type not updated in Editor",
				"simple.DataMiddle0", type.getText(ui));

	}

	public static void doChangeThroughExplorer(IUIContext ui) throws Exception {
		ViewLocator view = new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");

		ui.contextClick(new TreeItemLocator(project + "/src/simple/DataMiddle",
				view), "Refactor/Rename...");
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellShowingCondition("Rename Compilation Unit"));
		LabeledTextLocator locator = new LabeledTextLocator("New na&me:");
		ui.keyClick(SWT.CTRL, 'a');
		ui.click(locator);
		ui.enterText("DataMiddle0");
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Rename Compilation Unit"));

		// Let the updates happen!
		Thread.sleep(500);
	}

	public static void checkExplorerUpdates(IUIContext ui) throws Exception {
		checkExplorerUpdates(ui, "simple", "DataMiddle0");
	}

	public static void checkExplorerUpdates(IUIContext ui, String myPackage,
			String name) throws Exception {
		ViewLocator view = new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");

		// Check for ourself!
		ArtifactHelper.checkArtifactInExplorer(ui, project, myPackage, name);

		// Referenced Attribute updates should appear in the explorer
		ArrayList<String> items = new ArrayList<String>();
		items.add("attribute1" + ":" + name);
		ArtifactHelper.checkItemsInExplorer(ui, project, "simple", "Ent10",
				items);

		items = new ArrayList<String>();
		items.add("attribute0" + ":" + name);
		ArtifactHelper.checkItemsInExplorer(ui, project, "simple",
				"AssociatedEnt", items);

	}

	// In here we look for the changes that don't necessarily appear on the UI!
	@SuppressWarnings("deprecation")
	public static void checkAPI() throws Exception {

		IAbstractTigerstripeProject aProject = TigerstripeCore
				.findProject(project);
		ITigerstripeModelProject modelProject = (ITigerstripeModelProject) aProject;
		IArtifactManagerSession mgrSession = modelProject
				.getArtifactManagerSession();
		// First check that the old Entity no longer exists!
		IAbstractArtifact dm = mgrSession
				.getArtifactByFullyQualifiedName("simple.DataMiddle");
		assertNull("Old artifact is still being returned from the Art Mgr", dm);

		IAbstractArtifact dm0 = mgrSession
				.getArtifactByFullyQualifiedName("simple.DataMiddle0");
		assertNotNull(
				"New artifact is still not being returned from the Art Mgr",
				dm0);

		// Check that DataTop is extended by the new one.
		// TODO This can (should?) also be checked on the UI
		IAbstractArtifact dataTop = mgrSession
				.getArtifactByFullyQualifiedName("simple.DataTop");
		Collection<IAbstractArtifact> dtExtending = dataTop
				.getExtendingArtifacts();
		assertTrue("DataTop is not extend by the new Artifact", dtExtending
				.contains(dm0));
		assertFalse("DataTop still extended by  the old Artifact", dtExtending
				.contains(dm));

		// And DataBottom Extends our new one
		// TODO This can (should?) also be checked on the UI
		IAbstractArtifact dataBottom = mgrSession
				.getArtifactByFullyQualifiedName("simple.DataBottom");
		IAbstractArtifact dbExtends = dataBottom.getExtendedArtifact();
		assertTrue("DataBottom does not extend our new Artifact", dbExtends
				.equals(dm0));

	}

}
