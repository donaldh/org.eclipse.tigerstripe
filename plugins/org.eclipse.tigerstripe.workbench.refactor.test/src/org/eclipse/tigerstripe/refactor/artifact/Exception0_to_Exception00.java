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
import com.windowtester.runtime.WidgetNotFoundException;
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

public class Exception0_to_Exception00 extends UITestCaseSWT {

	private static String project="model-refactoring";

	
	private static String[] editors = {"Ent2"};
	
	public static void openRelatedEditors(IUIContext ui) throws Exception{
		// Make sure any related editors are open during the change
		ViewLocator view = new ViewLocator(
		"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		ui.click(2,	new TreeItemLocator(project+"/src/simple/Exception0",view));
		
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
		ui.click(new CTabItemLocator("*Ent2"));
		// Close Attributes and Open Methods section
		GuiUtils.maxminTab(ui, "*Ent2");
		ui.click(new SWTWidgetLocator(Label.class, "&Attributes"));
		ui.click(new SWTWidgetLocator(Label.class, "Methods"));
		TableItemLocator attributeNameInTable = new TableItemLocator("method0(Ent10):Ent10[0..1]");
		ui.click(attributeNameInTable);
//TODO - This is a problem with WindowTester - The Exception is scrolled off screen		
//		try {
//			ui.click(new TableItemLocator("simple.Exception00"));
//		} catch (WidgetNotFoundException wnfe){
//			fail("Referenced type (Method Exception) not updated in Editor");
//		}
		GuiUtils.maxminTab(ui, "*Ent2");
	}
	
	public static void doChangeThroughExplorer(IUIContext ui) throws Exception{
		ViewLocator view = new ViewLocator(
			"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		ui
		.contextClick(
				new TreeItemLocator(
						project+"/src/simple/Exception0",
						view),
		"Refactor/Rename...");
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellShowingCondition("Rename Compilation Unit"));
		LabeledTextLocator locator = new LabeledTextLocator("New na&me:");
		GuiUtils.clearText(ui, locator);
		ui.click(locator);
		ui.enterText("Exception00");
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Rename Compilation Unit"));
		
		// Let the updates happen!
		Thread.sleep(500);
	}
	
	
	public static void checkExplorerUpdates(IUIContext ui) throws Exception{
		ViewLocator view = new ViewLocator(
			"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		// Check for ourself!
		ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "Exception00");

		
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
		IAbstractArtifact event0 = mgrSession
			.getArtifactByFullyQualifiedName("simple.Exception0");
		assertNull("Old artifact is still being returned from the Art Mgr", event0);
		
		IAbstractArtifact event00 = mgrSession
			.getArtifactByFullyQualifiedName("simple.Exception00");
		assertNotNull("New artifact is still not being returned from the Art Mgr", event00);

		
		
		
	}
	
}
