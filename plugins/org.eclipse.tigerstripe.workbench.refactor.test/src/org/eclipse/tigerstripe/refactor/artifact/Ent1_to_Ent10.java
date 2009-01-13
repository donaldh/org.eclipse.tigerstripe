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
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetNotFoundException;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.SectionLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class Ent1_to_Ent10 extends UITestCaseSWT {

	private static String project="model-refactoring";
	// NOTE: The "own" editor gets closed by the rename action!
	// So don't iunclude it here!
	// TODO - Replace this!	
	private static String[] editors = {"Association0", "Association1", "Association2","AssociationClass0", "Ent2"};
	// TODO = with this
	//private static String[] editors = {"Association0", "Association1", "Association2","AssociationClass0", "Ent2", "Dependency0","Query0"};
	
	public static void openRelatedEditors(IUIContext ui) throws Exception{
		// Make sure any related editors are open during the change
		ViewLocator view = new ViewLocator(
		"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		ui.click(2,	new TreeItemLocator(project+"/src/simple/Ent1",view));
		
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
	
	public static void doChangeThroughExplorer(IUIContext ui) throws Exception{
		ViewLocator view = new ViewLocator(
			"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		ui
		.contextClick(
				new TreeItemLocator(
						project+"/src/simple/Ent1",
						view),
		"Refactor/Rename...");
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellShowingCondition("Rename Compilation Unit"));
		LabeledTextLocator locator = new LabeledTextLocator("New na&me:");
		GuiUtils.clearText(ui, locator);
		ui.click(locator);
		ui.enterText("Ent10");
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Rename Compilation Unit"));
		
		// Let the updates happen!
		Thread.sleep(500);
	}
	
	
	public static void checkExplorerUpdates(IUIContext ui) throws Exception{
		ViewLocator view = new ViewLocator(
			"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		// Check for ourself!
		ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "Ent10");
		
		
		//Association updates should appear in the explorer
		ArrayList<String> items = new ArrayList<String>();
		items.add("ent1_0"+":"+"Ent10");
		ArtifactHelper.checkItemsInExplorer(ui,project,
				"simple","Association0",items);
		
		items = new ArrayList<String>();
		items.add("ent1_1"+":"+"Ent10");
		items.add("ent1_2"+":"+"Ent10");
		ArtifactHelper.checkItemsInExplorer(ui, project,
				"simple","Association1",items);
		
		items = new ArrayList<String>();
		items.add("ent1_5"+":"+"Ent10");
		ArtifactHelper.checkItemsInExplorer(ui,project,
				"simple","Association2",items);
		
		items = new ArrayList<String>();
		items.add("ent1_3"+":"+"Ent10");
		items.add("ent1_4"+":"+"Ent10");
		ArtifactHelper.checkItemsInExplorer(ui,project,
				"simple","AssociationClass0",items);

		items = new ArrayList<String>();
		items.add("method0(Ent10):Ent10[0..1]");
		ArtifactHelper.checkItemsInExplorer(ui,project,
				"simple","Ent2",items);
		
		
// TODO - Replace this!	
//		items = new ArrayList<String>();
//		items.add("Ent10");
//		ArtifactHelper.checkItemsInExplorer(ui,project,
//				"simple","Dependency0",items);
		
	}
	
	public static void checkEditorUpdated(IUIContext ui) throws Exception{
		
		ui.click(new CTabItemLocator("*Association0"));
		// The helper will click on this...
		ui.click(new SWTWidgetLocator(Label.class, "End &Details"));
		// Check the Z end of this one.
		ArrayList<String> endNames = ArtifactHelper.associationEndNames(ui,"*Association0");
		String endName = endNames.get(1);
		assertEquals("Association End type not updated in Editor","ent1_0"+":"+"Ent10", endName);
		
		ui.click(new CTabItemLocator("*Association1"));
		ui.click(new SWTWidgetLocator(Label.class, "End &Details"));
		// Check both end of this one.
		endNames = ArtifactHelper.associationEndNames(ui,"*Association1");
		endName = endNames.get(0);
		assertEquals("Association End type not updated in Editor","ent1_1"+":"+"Ent10", endName);
		endName = endNames.get(1);
		assertEquals("Association End type not updated in Editor","ent1_2"+":"+"Ent10", endName);
		
		ui.click(new CTabItemLocator("*Association2"));
		ui.click(new SWTWidgetLocator(Label.class, "End &Details"));
		// Check the A end of this one.

		endNames = ArtifactHelper.associationEndNames(ui,"*Association2");
		endName = endNames.get(0);
		assertEquals("Association End type not updated in Editor","ent1_5"+":"+"Ent10", endName);
		
		ui.click(new CTabItemLocator("*AssociationClass0"));
		ui.click(new SWTWidgetLocator(Label.class, "End &Details"));
		// Check both end of this one.
		endNames = ArtifactHelper.associationEndNames(ui,"*AssociationClass0");
		endName = endNames.get(0);
		assertEquals("Association Class End type not updated in Editor","ent1_3"+":"+"Ent10", endName);
		endName = endNames.get(1);
		assertEquals("Association Class End type not updated in Editor","ent1_4"+":"+"Ent10", endName);
		
		ui.click(new CTabItemLocator("*Ent2"));
		// Check for Extends in this one
		LabeledTextLocator extend = new LabeledTextLocator("Extends: ");
		assertEquals("Extended type not updated in Editor","simple.Ent10",extend.getText(ui));
		// Now check Method
		// Close Attributes and Open Methods section
		GuiUtils.maxminTab(ui, "*Ent2");
		ui.click(new SWTWidgetLocator(Label.class, "&Attributes"));
		ui.click(new SWTWidgetLocator(Label.class, "Methods"));
		TableItemLocator attributeNameInTable = new TableItemLocator("method0(Ent10):Ent10[0..1]");
		ui.click(attributeNameInTable);
		// These tests are actually redundant if we assume the Explorer and Editor are in synch!
		LabeledTextLocator type = new LabeledTextLocator("Type: ");
		assertEquals("Referenced type (Method return) not updated in Editor","simple.Ent10",type.getText(ui));
		try {
			ui.click(new TableItemLocator("arg0: simple.Ent10"));
		} catch (WidgetNotFoundException wnfe){
			fail("Referenced type (Method argument) not updated in Editor");
		}
		GuiUtils.maxminTab(ui, "*Ent2");
		
// TODO - Replace this!		
//		ui.click(new CTabItemLocator("*Dependency0"));
//		ui.click(new SWTWidgetLocator(Label.class, "End &Details"));
//		endNames = ArtifactHelper.dependencyEndNames(ui,"*Dependency0");
//		endName = endNames.get(0);
//		assertEquals("Dependency End type not updated in Editor","Ent10", endName);

// TODO - Replace this!	
//		ui.click(new CTabItemLocator("*Query0"));
//		ui.click(new CTabItemLocator("Details"));
//		LabeledTextLocator retType = new LabeledTextLocator("Returned Type: ");
//		assertEquals("Query Return type not updated in Editor","Ent10", retType.getText(ui));
		
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
		IAbstractArtifact ent1 = mgrSession
			.getArtifactByFullyQualifiedName("simple.Ent1");
		assertNull("Old artifact is still being returned from the Art Mgr", ent1);
		
		IAbstractArtifact ent10 = mgrSession
			.getArtifactByFullyQualifiedName("simple.Ent10");
		assertNotNull("New artifact is still not being returned from the Art Mgr", ent10);
		
		// Check that superEnt is extended by the new one.
		IAbstractArtifact superEnt = mgrSession
			.getArtifactByFullyQualifiedName("simple.SuperEnt");
		Collection<IAbstractArtifact> superExtending = superEnt.getExtendingArtifacts();
		assertTrue("SuperEnt is not extend by the new Artifact", superExtending.contains(ent10));
		assertFalse("SuperEnt still extended by  the old Artifact", superExtending.contains(ent1));
		
		// And Ent2 Extends our new one
		// TODO This can (should?) also be checked on the UI
		IAbstractArtifact ent2 = mgrSession
			.getArtifactByFullyQualifiedName("simple.Ent2");
		IAbstractArtifact ent2Extends = ent2.getExtendedArtifact();
		assertTrue("Ent2 does not extend our new Artifact",ent2Extends.equals(ent10));
		
		// Finally check the Implemented by...
		IAbstractArtifact sessionFacade0 = mgrSession
			.getArtifactByFullyQualifiedName("simple.SessionFacade0");
		
		//TODO This is not implemented!
		//ISessionArtifact session = (ISessionArtifact) sessionFacade0;
		//Collection<IAbstractArtifact> sessionImplementing = session.getImplementingArtifacts();
		//assertTrue("SessionFacade0 is not implemented by the new Artifact "+sessionImplementing, sessionImplementing.contains(ent10));
		
		
		
	}
	
}
