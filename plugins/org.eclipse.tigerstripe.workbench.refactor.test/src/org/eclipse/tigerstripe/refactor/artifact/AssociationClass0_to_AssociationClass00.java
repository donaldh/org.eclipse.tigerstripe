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

import org.eclipse.swt.widgets.Label;
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
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class AssociationClass0_to_AssociationClass00 extends UITestCaseSWT {

	private static String project="model-refactoring";
	
	private static String[] editors = {"Association2"};
	
	public static void openRelatedEditors(IUIContext ui) throws Exception{
		// Make sure any related editors are open during the change
		ViewLocator view = new ViewLocator(
		"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		ui.click(2,	new TreeItemLocator(project+"/src/simple/AssociationClass0",view));
		
		for (String editor : editors){
			ui.click(2,	new TreeItemLocator(project+"/src/simple/"+editor,view));
		}
	
	}
	
	public static void saveAndCloseRelatedEditors(IUIContext ui) throws Exception{
		
		// NOTE: The "own" editor gets closed by the rename action!
		for (String editor : editors){
			ui.click(new CTabItemLocator("*"+editor));
			ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
			ui.close(new CTabItemLocator(editor));
		}

	}
	
	public static void checkEditorUpdated(IUIContext ui) throws Exception{
		ui.click(new CTabItemLocator("*Association2"));
		// The helper will click on this...
		ui.click(new SWTWidgetLocator(Label.class, "End &Details"));
		// Check the Z end of this one.
		ArrayList<String> endNames = ArtifactHelper.associationEndNames(ui,"*Association2");
		String endName = endNames.get(1);
		assertEquals("Association End type not updated in Editor","associationClass0_0"+":"+"AssociationClass00", endName);
	}
	
	
	public static void doChangeThroughExplorer(IUIContext ui) throws Exception{
		ViewLocator view = new ViewLocator(
			"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		ui
		.contextClick(
				new TreeItemLocator(
						project+"/src/simple/AssociationClass0",
						view),
		"Refactor/Rename...");
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellShowingCondition("Rename Compilation Unit"));
		LabeledTextLocator locator = new LabeledTextLocator("New na&me:");
		GuiUtils.clearText(ui, locator);
		ui.click(locator);
		ui.enterText("AssociationClass00");
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Rename Compilation Unit"));
		
		// Let the updates happen!
		Thread.sleep(500);
	}
	
	
	public static void checkExplorerUpdates(IUIContext ui) throws Exception{
		ViewLocator view = new ViewLocator(
			"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		// Check for ourself!
		ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "AssociationClass00");

		//Association updates should appear in the explorer
		ArrayList<String> items = new ArrayList<String>();
		items.add("associationClass0_0"+":"+"AssociationClass00");
		ArtifactHelper.checkItemsInExplorer(ui,project,
				"simple","Association2",items);
		
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
		IAbstractArtifact associationClass0 = mgrSession
			.getArtifactByFullyQualifiedName("simple.AssociationClass0");
		assertNull("Old artifact is still being returned from the Art Mgr", associationClass0);
		
		IAbstractArtifact associationClass00 = mgrSession
			.getArtifactByFullyQualifiedName("simple.AssociationClass00");
		assertNotNull("New artifact is still not being returned from the Art Mgr", associationClass00);
		
		// Need to go and look at the end stuff...
		// Goes from Ent10 to Ent10
		// We are also at the end of as Assoc
		
		IQueryRelationshipsByArtifact query = (IQueryRelationshipsByArtifact) mgrSession.makeQuery(IQueryRelationshipsByArtifact.class.getName());
		query.setOriginatingFrom("simple.Ent10");
		Collection<IAbstractArtifact> assocsFromAssociatedEnt = mgrSession.queryArtifact(query);
		assertTrue("Updated association class not returned from the Origniating Entity",assocsFromAssociatedEnt.contains(associationClass00));
		assertFalse("Original association class returned from the Origniating Entity",assocsFromAssociatedEnt.contains(associationClass0));
		
		IQueryRelationshipsByArtifact query2 = (IQueryRelationshipsByArtifact) mgrSession.makeQuery(IQueryRelationshipsByArtifact.class.getName());
		query2.setTerminatingIn("simple.Ent10");
		Collection<IAbstractArtifact> assocsToEnt10 = mgrSession.queryArtifact(query2);
		assertTrue("Updated association class not returned from the Terminating Entity",assocsToEnt10.contains(associationClass00));
		assertFalse("Original association class returned from the Terminating Entity",assocsToEnt10.contains(associationClass0));
		
		IAbstractArtifact association2 = mgrSession
			.getArtifactByFullyQualifiedName("simple.Association2");
			
		IQueryRelationshipsByArtifact query3 = (IQueryRelationshipsByArtifact) mgrSession.makeQuery(IQueryRelationshipsByArtifact.class.getName());
		query3.setTerminatingIn("simple.AssociationClass00");
		Collection<IAbstractArtifact> assocsToAssocClass00 = mgrSession.queryArtifact(query3);
		assertTrue("Association class returned new Association Class as the Terminating Entity",assocsToAssocClass00.contains(association2));
		
		
	}
	
}
