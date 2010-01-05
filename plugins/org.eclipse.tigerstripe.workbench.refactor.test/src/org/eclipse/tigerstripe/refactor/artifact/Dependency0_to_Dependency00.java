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
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryRelationshipsByArtifact;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WT;
import com.windowtester.runtime.locator.IWidgetLocator;
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

public class Dependency0_to_Dependency00 extends UITestCaseSWT {

	private static String project = "model-refactoring";

	public static void doChangeThroughDiagramInSamePackage(IUIContext ui)
			throws Exception {
		LocatorHelper helper = new LocatorHelper();
		ui.click(new CTabItemLocator("default.wvd"));
		IWidgetLocator tfl = helper.getNameEditLocator(ui, "Dependency0");
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
			ui.click(helper.getDependencyFigureLocator(ui, artifactPrefix
					+ "Dependency00"));
		} catch (Exception e) {
			fail("Refactored Entity not found on diagram");
		}
	}

	public static void doChangeThroughExplorer(IUIContext ui) throws Exception {
		ViewLocator view = new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");

		ui.contextClick(new TreeItemLocator(
				project + "/src/simple/Dependency0", view),
				"Refactor Model/Rename...");
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellShowingCondition("Rename Model Artifact"));
		//LabeledTextLocator locator = new LabeledTextLocator("New na&me:");
		ui.keyClick(SWT.CTRL, 'a');
		//ui.click(locator);
		ui.enterText("simple.Dependency00");
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Rename Model Artifact"));

		// Let the updates happen!
		Thread.sleep(500);
	}

	public static void checkExplorerUpdates(IUIContext ui) throws Exception {
		ViewLocator view = new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");

		// Check for ourself!
		ArtifactHelper.checkArtifactInExplorer(ui, project, "simple",
				"Dependency00");

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
		IAbstractArtifact dependency0 = mgrSession
				.getArtifactByFullyQualifiedName("simple.Dependency0");
		assertNull("Old artifact is still being returned from the Art Mgr",
				dependency0);

		IAbstractArtifact dependency00 = mgrSession
				.getArtifactByFullyQualifiedName("simple.Dependency00");
		assertNotNull(
				"New artifact is still not being returned from the Art Mgr",
				dependency00);
		// TODO - Replace These!
		// IQueryRelationshipsByArtifact query = (IQueryRelationshipsByArtifact)
		// mgrSession.makeQuery(IQueryRelationshipsByArtifact.class.getName());
		// query.setOriginatingFrom("simple.Ent10");
		// Collection<IAbstractArtifact> assocsFromAssociatedEnt =
		// mgrSession.queryArtifact(query);
		// assertTrue("Updated dependency not returned from the Origniating Entity",assocsFromAssociatedEnt.contains(dependency00));
		// assertFalse("Original dependency returned from the Origniating Entity",assocsFromAssociatedEnt.contains(dependency0));
		//		
		// IQueryRelationshipsByArtifact query2 =
		// (IQueryRelationshipsByArtifact)
		// mgrSession.makeQuery(IQueryRelationshipsByArtifact.class.getName());
		// query2.setTerminatingIn("simple.DataTop");
		// Collection<IAbstractArtifact> assocsToEnt10 =
		// mgrSession.queryArtifact(query2);
		// assertTrue("Updated dependency not returned from the Terminating Entity",assocsToEnt10.contains(dependency00));
		// assertFalse("Original dependency returned from the Terminating Entity",assocsToEnt10.contains(dependency0));

	}

}
