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
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class Association0_to_Association00 extends UITestCaseSWT {

	private static String project="model-refactoring";

	public static void checkDiagrams(IUIContext ui) throws Exception{
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
	
	public static void internalCheckDiagram(IUIContext ui, LocatorHelper helper, String artifactPrefix){
		
		// Basic rename
		try {
			ui.click(helper.getAssociationFigureLocator(ui, artifactPrefix+"Association00"));
		} catch (Exception e){
			fail("Refactored Entity not found on diagram");
		}
	}
	
	
	public static void doChangeThroughExplorer(IUIContext ui) throws Exception{
		ViewLocator view = new ViewLocator(
			"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		ui
		.contextClick(
				new TreeItemLocator(
						project+"/src/simple/Association0",
						view),
		"Refactor/Rename...");
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellShowingCondition("Rename Compilation Unit"));
		LabeledTextLocator locator = new LabeledTextLocator("New na&me:");
		GuiUtils.clearText(ui, locator);
		ui.click(locator);
		ui.enterText("Association00");
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Rename Compilation Unit"));
		
		// Let the updates happen!
		Thread.sleep(500);
	}
	
	
	public static void checkExplorerUpdates(IUIContext ui) throws Exception{
		ViewLocator view = new ViewLocator(
			"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		// Check for ourself!
		ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "Association00");

		
	}
	
	// In here we look for the changes that don't necessarily appear on the UI!
	@SuppressWarnings("deprecation")
	public static void checkAPI() throws Exception{
		// First check that the old Entity no longer exists!
		IAbstractTigerstripeProject aProject = TigerstripeCore.findProject(project);
		ITigerstripeModelProject modelProject = (ITigerstripeModelProject) aProject;
		IArtifactManagerSession mgrSession = modelProject
			.getArtifactManagerSession();
		// First check that the old Entity no longer exists!
		IAbstractArtifact association0 = mgrSession
			.getArtifactByFullyQualifiedName("simple.Association0");
		assertNull("Old artifact is still being returned from the Art Mgr", association0);
		
		IAbstractArtifact association00 = mgrSession
			.getArtifactByFullyQualifiedName("simple.Association00");
		assertNotNull("New artifact is still not being returned from the Art Mgr", association00);
		
		// Need to go and look at the end stuff...
		// Goes from associatedEnt to Ent10
		
		IQueryRelationshipsByArtifact query = (IQueryRelationshipsByArtifact) mgrSession.makeQuery(IQueryRelationshipsByArtifact.class.getName());
		query.setOriginatingFrom("simple.AssociatedEnt");
		Collection<IAbstractArtifact> assocsFromAssociatedEnt = mgrSession.queryArtifact(query);
		assertTrue("Updated association not returned from the Origniating Entity",assocsFromAssociatedEnt.contains(association00));
		assertFalse("Original association returned from the Origniating Entity",assocsFromAssociatedEnt.contains(association0));
		
		IQueryRelationshipsByArtifact query2 = (IQueryRelationshipsByArtifact) mgrSession.makeQuery(IQueryRelationshipsByArtifact.class.getName());
		query2.setTerminatingIn("simple.Ent10");
		Collection<IAbstractArtifact> assocsToEnt10 = mgrSession.queryArtifact(query2);
		assertTrue("Updated association not returned from the Terminating Entity",assocsToEnt10.contains(association00));
		assertFalse("Original association returned from the Terminating Entity",assocsToEnt10.contains(association0));
		
		
		
	}
	
}
