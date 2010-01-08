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
package org.eclipse.tigerstripe.refactor.closed;

import org.eclipse.tigerstripe.refactor.Vanilla.MoveTests;
import org.eclipse.tigerstripe.refactor.artifact.DataBottom_to_DataBottom0;
import org.eclipse.tigerstripe.refactor.artifact.DataMiddle_to_DataMiddle0;
import org.eclipse.tigerstripe.refactor.artifact.Ent1_to_Ent10;
import org.eclipse.tigerstripe.refactor.project.ProjectHelper;
import org.eclipse.tigerstripe.refactor.suite.DiagramHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;

import com.windowtester.runtime.WT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class MoveTestsClosed extends MoveTests {
	private static String project = "model-refactoring";

	public void setUp() throws Exception{
		ui = getUI();
		helper = new ProjectHelper();
		artifactHelper = new ArtifactHelper();
		
		helper.reloadProjectFromCVS(ui,project);
		
			
		view = new ViewLocator(
		"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");

	}
	
	public void testRenames() throws Exception {
		
	
		// This is a single move

		Ent1_to_Ent10.openRelatedEditors(ui);
		Ent1_to_Ent10.doChangeByMove(ui);
		// Note this one needs a dot at the end of the package
		// because by default there is no package
		Ent1_to_Ent10.checkExplorerUpdates(ui,"simple.moved.","Ent1");
		Ent1_to_Ent10.checkAPI("simple.moved","Ent1");
		Ent1_to_Ent10.checkEditorUpdated(ui,"simple.moved","Ent1");
		Ent1_to_Ent10.saveAndCloseRelatedEditors(ui);
		
		DiagramHelper.openDiagrams(ui);
		Ent1_to_Ent10.checkDiagramsAfterMove(ui);
		DiagramHelper.closeDiagrams(ui);
		
		
		multipleMove();
		
		doChecks();
		DiagramHelper.openDiagrams(ui);
		doDiagramChecks();
		DiagramHelper.closeDiagrams(ui);
		
	}

	public void doDiagramChecks() throws Exception{
		
		DataMiddle_to_DataMiddle0.checkDiagramsAfterMove(ui);
		DataBottom_to_DataBottom0.checkDiagramsAfterMove(ui);
		
		
	}
	
	
	
}
