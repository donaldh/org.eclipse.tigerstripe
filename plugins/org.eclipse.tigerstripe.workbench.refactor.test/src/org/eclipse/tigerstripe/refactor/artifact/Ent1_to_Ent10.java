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
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MethodEditPart;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WT;
import com.windowtester.runtime.WidgetNotFoundException;
import com.windowtester.runtime.gef.locator.FigureClassLocator;
import com.windowtester.runtime.gef.locator.LRLocator;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.locator.WidgetReference;
import com.windowtester.runtime.locator.XYLocator;
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

public class Ent1_to_Ent10 extends UITestCaseSWT {

	private static String project = "model-refactoring";
	// NOTE: The "own" editor gets closed by the rename action!
	// So don't iunclude it here!
	// TODO - Replace this!
	private static String[] editors = { "Association0", "Association1",
			"Association2", "AssociationClass0", "Ent2" };

	// TODO = with this
	// private static String[] editors = {"Association0", "Association1",
	// "Association2","AssociationClass0", "Ent2", "Dependency0","Query0"};

	public static void doChangeThroughDiagramInSamePackage(IUIContext ui)
			throws Exception {
		LocatorHelper helper = new LocatorHelper();
		ui.click(new CTabItemLocator("default.wvd"));
		IWidgetLocator tfl = helper.getNameEditLocator(ui, "Ent1");
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

	public static void checkDiagramsAfterMove(IUIContext ui) throws Exception {
		LocatorHelper helper = new LocatorHelper();
		ui.click(new CTabItemLocator("*default.wvd"));
		String artifactPrefix = "";
		String myArtifactPrefix = "simple.moved.";
		internalCheckDiagram(ui, helper, artifactPrefix, myArtifactPrefix,
				"Ent1");
		artifactPrefix = "simple.";
		ui.click(new CTabItemLocator("*inside-moved.wvd"));
		internalCheckDiagram(ui, helper, artifactPrefix, "", "Ent1");
		ui.click(new CTabItemLocator("*outside-class-diagram.wvd"));
		internalCheckDiagram(ui, helper, artifactPrefix, myArtifactPrefix,
				"Ent1");

	}

	public static void checkDiagrams(IUIContext ui) throws Exception {
		LocatorHelper helper = new LocatorHelper();
		ui.click(new CTabItemLocator("default.wvd"));
		String artifactPrefix = "";
		internalCheckDiagram(ui, helper, artifactPrefix, artifactPrefix,
				"Ent10");
		artifactPrefix = "simple.";
		ui.click(new CTabItemLocator("inside-moved.wvd"));
		internalCheckDiagram(ui, helper, artifactPrefix, artifactPrefix,
				"Ent10");
		ui.click(new CTabItemLocator("outside-class-diagram.wvd"));
		internalCheckDiagram(ui, helper, artifactPrefix, artifactPrefix,
				"Ent10");

	}

	public static void internalCheckDiagram(IUIContext ui,
			LocatorHelper helper, String artifactPrefix,
			String myArtifactPrefix, String artifactName) throws Exception {

		// Basic rename
		try {
			ui.click(helper.getManagedEntityLocator(ui, myArtifactPrefix
					+ artifactName));
		} catch (Exception e) {
			fail("Refactored Entity '" + myArtifactPrefix + artifactName
					+ "' not found on diagram");
		}
		// Now check Refs.
		IWidgetLocator method = helper.getManagedEntityMethodLocator(ui,
				artifactPrefix + "Ent2", "method0");
		WidgetReference methodRef = (WidgetReference) method;
		MethodEditPart.MethodLabelFigure fig = (MethodEditPart.MethodLabelFigure) methodRef
				.getWidget();
		String figText = fig.getText();
		// Return type should be our new Ent, as should the arg type - ie
		// By default, the internal packages are hidden
		String expectedText = "+method0(" + artifactName + "):" + artifactName
				+ "[0..1]";
		assertEquals("Method signature not updated on diagram", expectedText,
				figText);

		// Do we need to check all of the associations are still there?
		// If the refactor didn't get passed around, the assoc/dependencies will
		// disappear form the doagrm, so I guess yes!
		ui.click(helper.getAssociationFigureLocator(ui, artifactPrefix
				+ "Association0"));
		ui.click(helper.getAssociationFigureLocator(ui, artifactPrefix
				+ "Association1"));
		ui.click(helper.getAssociationFigureLocator(ui, artifactPrefix
				+ "Association2"));
		ui.click(helper.getAssociationClassFigureLocator(ui, artifactPrefix
				+ "AssociationClass0"));

		// TODO Replace this
		// ui.click(helper.getDependencyFigureLocator(ui,
		// artifactPrefix+"Dependency0"));

	}

	public static void openRelatedEditors(IUIContext ui) throws Exception {
		// Make sure any related editors are open during the change
		ViewLocator view = new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");

		ui.click(2, new TreeItemLocator(project + "/src/simple/Ent1", view));

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

	public static void doChangeThroughExplorer(IUIContext ui) throws Exception {
		ViewLocator view = new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");

		ui.contextClick(
				new TreeItemLocator(project + "/src/simple/Ent1", view),
				"Refactor Model/Rename...");
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellShowingCondition("Rename Model Artifact"));
		//LabeledTextLocator locator = new LabeledTextLocator("New name:");
		ui.keyClick(SWT.CTRL, 'a');
		//ui.click(locator);
		ui.enterText("simple.Ent10");
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Rename Model Artifact"));

		// Let the updates happen!
		Thread.sleep(500);
	}

	public static void doChangeByDnd(IUIContext ui) throws Exception {
		ViewLocator view = new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		ui.click(new TreeItemLocator(project + "/src/simple/Ent1", view));
		ui
				.dragTo(new TreeItemLocator(
						"model-refactoring/src/simple/moved",
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui.wait(new ShellShowingCondition("Move"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Move"));

	}

	public static void doChangeByMove(IUIContext ui) throws Exception {
		ViewLocator view = new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		ui.contextClick(
				new TreeItemLocator(project + "/src/simple/Ent1", view),
				"Refactor Model/Move...");
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellShowingCondition("Move"));
		ui.click(new TreeItemLocator("model-refactoring/src/simple/moved"));
		ui.click(new ButtonLocator("Finish"));
		ui.wait(new ShellDisposedCondition("Move"));

	}

	public static void checkExplorerUpdates(IUIContext ui) throws Exception {
		checkExplorerUpdates(ui, "simple", "Ent10");
	}

	public static void checkExplorerUpdates(IUIContext ui, String myPackage,
			String name) throws Exception {
		ViewLocator view = new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");

		// Check for ourself!
		ArtifactHelper.checkArtifactInExplorer(ui, project, myPackage, name);

		// Association updates should appear in the explorer
		ArrayList<String> items = new ArrayList<String>();
		items.add("ent1_0" + ":" + name);
		ArtifactHelper.checkItemsInExplorer(ui, project, "simple",
				"Association0", items);

		items = new ArrayList<String>();
		items.add("ent1_1" + ":" + name);
		items.add("ent1_2" + ":" + name);
		ArtifactHelper.checkItemsInExplorer(ui, project, "simple",
				"Association1", items);

		items = new ArrayList<String>();
		items.add("ent1_5" + ":" + name);
		ArtifactHelper.checkItemsInExplorer(ui, project, "simple",
				"Association2", items);

		items = new ArrayList<String>();
		items.add("ent1_3" + ":" + name);
		items.add("ent1_4" + ":" + name);
		ArtifactHelper.checkItemsInExplorer(ui, project, "simple",
				"AssociationClass0", items);

		items = new ArrayList<String>();
		items.add("method0(" + name + "):" + name + "[0..1]");
		ArtifactHelper.checkItemsInExplorer(ui, project, "simple", "Ent2",
				items);

		// TODO - Replace this!
		// items = new ArrayList<String>();
		// items.add("Ent10");
		// ArtifactHelper.checkItemsInExplorer(ui,project,
		// "simple","Dependency0",items);

	}

	public static void checkEditorUpdated(IUIContext ui) throws Exception {
		checkEditorUpdated(ui, "simple", "Ent10");
	}

	public static void checkEditorUpdated(IUIContext ui, String myPackage,
			String name) throws Exception {

		String newName = myPackage + "." + name;

		ui.click(new CTabItemLocator("*Association0"));
		// The helper will click on this...
		ui.click(new SWTWidgetLocator(Label.class, "End &Details"));
		// Check the Z end of this one.
		ArrayList<String> endNames = ArtifactHelper.associationEndNames(ui,
				"*Association0");
		String endName = endNames.get(1);
		assertEquals("Association End type not updated in Editor", "ent1_0"
				+ ":" + name, endName);

		ui.click(new CTabItemLocator("*Association1"));
		ui.click(new SWTWidgetLocator(Label.class, "End &Details"));
		// Check both end of this one.
		endNames = ArtifactHelper.associationEndNames(ui, "*Association1");
		endName = endNames.get(0);
		assertEquals("Association End type not updated in Editor", "ent1_1"
				+ ":" + name, endName);
		endName = endNames.get(1);
		assertEquals("Association End type not updated in Editor", "ent1_2"
				+ ":" + name, endName);

		ui.click(new CTabItemLocator("*Association2"));
		ui.click(new SWTWidgetLocator(Label.class, "End &Details"));
		// Check the A end of this one.

		endNames = ArtifactHelper.associationEndNames(ui, "*Association2");
		endName = endNames.get(0);
		assertEquals("Association End type not updated in Editor", "ent1_5"
				+ ":" + name, endName);

		ui.click(new CTabItemLocator("*AssociationClass0"));
		ui.click(new SWTWidgetLocator(Label.class, "End &Details"));
		// Check both end of this one.
		endNames = ArtifactHelper.associationEndNames(ui, "*AssociationClass0");
		endName = endNames.get(0);
		assertEquals("Association Class End type not updated in Editor",
				"ent1_3" + ":" + name, endName);
		endName = endNames.get(1);
		assertEquals("Association Class End type not updated in Editor",
				"ent1_4" + ":" + name, endName);

		ui.click(new CTabItemLocator("*Ent2"));
		// Check for Extends in this one
		LabeledTextLocator extend = new LabeledTextLocator("Extends: ");
		assertEquals("Extended type not updated in Editor", newName, extend
				.getText(ui));
		// Now check Method
		// Close Attributes and Open Methods section
		GuiUtils.maxminTab(ui, "*Ent2");
		ui.click(new SWTWidgetLocator(Label.class, "&Attributes"));
		ui.click(new SWTWidgetLocator(Label.class, "Methods"));
		TableItemLocator attributeNameInTable = new TableItemLocator("method0("
				+ name + "):" + name + "[0..1]");
		ui.click(attributeNameInTable);
		// These tests are actually redundant if we assume the Explorer and
		// Editor are in synch!
		LabeledTextLocator type = new LabeledTextLocator("Type: ");
		assertEquals("Referenced type (Method return) not updated in Editor",
				newName, type.getText(ui));
		try {
			ui.click(new TableItemLocator("arg0: " + newName));
		} catch (WidgetNotFoundException wnfe) {
			fail("Referenced type (Method argument) not updated in Editor");
		}
		GuiUtils.maxminTab(ui, "*Ent2");

		// TODO - Replace this!
		// ui.click(new CTabItemLocator("*Dependency0"));
		// ui.click(new SWTWidgetLocator(Label.class, "End &Details"));
		// endNames = ArtifactHelper.dependencyEndNames(ui,"*Dependency0");
		// endName = endNames.get(0);
		// assertEquals("Dependency End type not updated in Editor","Ent10",
		// endName);

		// TODO - Replace this!
		// ui.click(new CTabItemLocator("*Query0"));
		// ui.click(new CTabItemLocator("Details"));
		// LabeledTextLocator retType = new
		// LabeledTextLocator("Returned Type: ");
		// assertEquals("Query Return type not updated in Editor","Ent10",
		// retType.getText(ui));

	}

	public static void checkAPI() throws Exception {
		checkAPI("simple", "Ent10");
	}

	// In here we look for the changes that don't necessarily appear on the UI!
	@SuppressWarnings("deprecation")
	public static void checkAPI(String myPackage, String name) throws Exception {

		String newName = myPackage + "." + name;

		// First check that the old Entity no longer exists!
		IAbstractTigerstripeProject aProject = TigerstripeCore
				.findProject(project);
		ITigerstripeModelProject modelProject = (ITigerstripeModelProject) aProject;
		IArtifactManagerSession mgrSession = modelProject
				.getArtifactManagerSession();
		// First check that the old Entity no longer exists!
		IAbstractArtifact ent1 = mgrSession
				.getArtifactByFullyQualifiedName("simple.Ent1");
		assertNull("Old artifact is still being returned from the Art Mgr",
				ent1);

		IAbstractArtifact ent10 = mgrSession
				.getArtifactByFullyQualifiedName(newName);
		assertNotNull("New artifact '" + newName
				+ "' is still not being returned from the Art Mgr", ent10);

		// Check that superEnt is extended by the new one.
		IAbstractArtifact superEnt = mgrSession
				.getArtifactByFullyQualifiedName("simple.SuperEnt");
		Collection<IAbstractArtifact> superExtending = superEnt
				.getExtendingArtifacts();
		assertTrue("SuperEnt is not extend by the new Artifact", superExtending
				.contains(ent10));
		assertFalse("SuperEnt still extended by  the old Artifact",
				superExtending.contains(ent1));

		// And Ent2 Extends our new one
		// TODO This can (should?) also be checked on the UI
		IAbstractArtifact ent2 = mgrSession
				.getArtifactByFullyQualifiedName("simple.Ent2");
		IAbstractArtifact ent2Extends = ent2.getExtendedArtifact();
		assertTrue("Ent2 does not extend our new Artifact", ent2Extends
				.equals(ent10));

		// Finally check the Implemented by...
		IAbstractArtifact sessionFacade0 = mgrSession
				.getArtifactByFullyQualifiedName("simple.SessionFacade0");

		// TODO This is not implemented!
		// ISessionArtifact session = (ISessionArtifact) sessionFacade0;
		// Collection<IAbstractArtifact> sessionImplementing =
		// session.getImplementingArtifacts();
		// assertTrue("SessionFacade0 is not implemented by the new Artifact "+sessionImplementing,
		// sessionImplementing.contains(ent10));

	}

}
