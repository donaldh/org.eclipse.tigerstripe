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

import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class DataMiddle_to_DataMiddle0 extends UITestCaseSWT {

	private static String project="model-refactoring";
	
	public static void doChangeThroughExplorer(IUIContext ui) throws Exception{
		ViewLocator view = new ViewLocator(
			"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		ui
		.contextClick(
				new TreeItemLocator(
						project+"/src/simple/DataMiddle",
						view),
		"Refactor/Rename...");
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellShowingCondition("Rename Compilation Unit"));
		LabeledTextLocator locator = new LabeledTextLocator("New na&me:");
		GuiUtils.clearText(ui, locator);
		ui.click(locator);
		ui.enterText("DataMiddle0");
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Rename Compilation Unit"));
		
		// Let the updates happen!
		Thread.sleep(500);
	}
	
	
	public static void checkExplorerUpdates(IUIContext ui) throws Exception{
		ViewLocator view = new ViewLocator(
			"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		// Check for ourself!
		ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "DataMiddle0");
		
		
		//Referenced Attribute updates should appear in the explorer
		ArrayList<String> items = new ArrayList<String>();
		items.add("attribute1"+":"+"DataMiddle0");
		ArtifactHelper.checkItemsInExplorer(ui,project,
				"simple","Ent10",items);
		
		items = new ArrayList<String>();
		items.add("attribute0"+":"+"DataMiddle0");
		ArtifactHelper.checkItemsInExplorer(ui,project,
				"simple","AssociatedEnt",items);
		
		
	}
	
	// In here we look for the changes that don't necessarily appear on the UI!
	@SuppressWarnings("deprecation")
	public static void checkAPI() throws Exception{

		IAbstractTigerstripeProject aProject = TigerstripeCore.findProject(project);
		ITigerstripeModelProject modelProject = (ITigerstripeModelProject) aProject;
		IArtifactManagerSession mgrSession = modelProject
			.getArtifactManagerSession();
		// First check that the old Entity no longer exists!
		IAbstractArtifact dm = mgrSession
			.getArtifactByFullyQualifiedName("simple.DataMiddle");
		assertNull("Old artifact is still being returned from the Art Mgr", dm);
		
		IAbstractArtifact dm0 = mgrSession
			.getArtifactByFullyQualifiedName("simple.DataMiddle0");
		assertNotNull("New artifact is still not being returned from the Art Mgr", dm0);
		
		// Check that DataTop is extended by the new one.
		// TODO This can (should?) also be checked on the UI
		IAbstractArtifact dataTop = mgrSession
			.getArtifactByFullyQualifiedName("simple.DataTop");
		Collection<IAbstractArtifact> dtExtending = dataTop.getExtendingArtifacts();
		assertTrue("DataTop is not extend by the new Artifact", dtExtending.contains(dm0));
		assertFalse("DataTop still extended by  the old Artifact", dtExtending.contains(dm));
		
		// And DataBottom Extends our new one
		// TODO This can (should?) also be checked on the UI
		IAbstractArtifact dataBottom = mgrSession
			.getArtifactByFullyQualifiedName("simple.DataBottom");
		IAbstractArtifact dbExtends = dataBottom.getExtendedArtifact();
		assertTrue("DataBottom does not extend our new Artifact", dbExtends.equals(dm0));
		
		
		
	}
	
}
