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
package org.eclipse.tigerstripe.refactor.Vanilla;

import java.util.ArrayList;

import org.eclipse.tigerstripe.refactor.artifact.Association0_to_Association00;
import org.eclipse.tigerstripe.refactor.artifact.AssociationClass0_to_AssociationClass00;
import org.eclipse.tigerstripe.refactor.artifact.DataBottom_to_DataBottom0;
import org.eclipse.tigerstripe.refactor.artifact.DataMiddle_to_DataMiddle0;
import org.eclipse.tigerstripe.refactor.artifact.Dependency0_to_Dependency00;
import org.eclipse.tigerstripe.refactor.artifact.Ent1_to_Ent10;
import org.eclipse.tigerstripe.refactor.artifact.Enumeration0_to_Enumeration00;
import org.eclipse.tigerstripe.refactor.artifact.Event0_to_Event00;
import org.eclipse.tigerstripe.refactor.artifact.Exception0_to_Exception00;
import org.eclipse.tigerstripe.refactor.artifact.Query0_to_Query00;
import org.eclipse.tigerstripe.refactor.artifact.Session0_to_Session00;
import org.eclipse.tigerstripe.refactor.artifact.Update0_to_Update00;
import org.eclipse.tigerstripe.refactor.project.ProjectHelper;
import org.eclipse.tigerstripe.refactor.project.TestInitialPackageContents;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WT;
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

public class MoveTests extends UITestCaseSWT {

		public ProjectHelper helper;
		public ArtifactHelper artifactHelper;
		public IUIContext ui;
		public ViewLocator view;
		
		public void setUp() throws Exception{
			ui = getUI();
			helper = new ProjectHelper();
			artifactHelper = new ArtifactHelper();
			
			helper.reloadProjectFromCVS(ui);
			
			// In this test case we don't want any diagrams.....
			// There are 4 diagrams
			helper.deleteDiagrams(ui);
				
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
				
			multipleMove();
			doChecks();
			
			
		}
	
		
		public void multipleMove() throws Exception{

			String project="model-refactoring";
			
			// Open the editors
			
			String[] editors = {"simple/AssociatedEnt", "simple/moved/Ent1"};
			for (String editor : editors){
				ui.click(2,	new TreeItemLocator(project+"/src/" + editor,view));
			}
			
			
			
			ui.click(new TreeItemLocator(
					project+"/src/simple/DataTop",
					view));
			ui.click(
					1,
					new TreeItemLocator(
							project+"/src/simple/DataMiddle",
							view),
							WT.CTRL);
			ui.click(
					1,
					new TreeItemLocator(
							project+"/src/simple/DataBottom",
							view),
							WT.CTRL);

			ui.contextClick(
					new TreeItemLocator(
							project+"/src/simple/DataBottom",
							view),
				"Refactor/Move...");
			ui.wait(new ShellDisposedCondition("Progress Information"));
			ui.wait(new ShellShowingCondition("Move"));
			ui.click(new TreeItemLocator("model-refactoring/src/simple.moved"));
			ui.click(new ButtonLocator("OK"));
			ui.wait(new ShellDisposedCondition("Move"));
			
		}
		
		public void doChecks() throws Exception {
			
			String project="model-refactoring";
			// Now what to test for?
			// Explorer tests
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple.moved", "DataTop");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple.moved", "DataMiddle");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple.moved", "DataBottom");
			
			ArrayList<String> items = new ArrayList<String>();
			items.add("attribute1"+":"+"DataMiddle");
			ArtifactHelper.checkItemsInExplorer(ui,project,
					"simple.moved","Ent1",items);
			
			items = new ArrayList<String>();
			items.add("attribute0"+":"+"DataMiddle");
			ArtifactHelper.checkItemsInExplorer(ui,project,
					"simple","AssociatedEnt",items);
			
			items = new ArrayList<String>();
			items.add("recur"+":"+"DataBottom");
			ArtifactHelper.checkItemsInExplorer(ui,project,
					"simple.moved","DataBottom",items);
			
			
			
			// Editor tests
			// First open the editors for the moved objects
			String[] ownEditors = {"simple/moved/DataTop" , "simple/moved/DataMiddle" , "simple/moved/DataBottom"};
			for (String editor : ownEditors){
				ui.click(2,	new TreeItemLocator(project+"/src/" + editor,view));
			}
			

			ui.click(new CTabItemLocator("DataBottom"));
			// Check for Extends in this one
			LabeledTextLocator extend = new LabeledTextLocator("Extends: ");
			assertEquals("Extended type not updated in Editor","simple.moved.DataMiddle",extend.getText(ui));
			
			// Check for An AttributeRef
			TableItemLocator attributeNameInTable = new TableItemLocator("recur");
			ui.click(attributeNameInTable);
			LabeledTextLocator type = new LabeledTextLocator("Type: ");
			assertEquals("Referenced type not updated in Editor","simple.moved.DataBottom",type.getText(ui));
			
			ui.click(new CTabItemLocator("*Ent1"));
			// Check for An AttributeRef
			 attributeNameInTable = new TableItemLocator("attribute1");
			ui.click(attributeNameInTable);
			type = new LabeledTextLocator("Type: ");
			assertEquals("Referenced type not updated in Editor","simple.moved.DataMiddle",type.getText(ui));
			
			ui.click(new CTabItemLocator("*AssociatedEnt"));
			// Check for An AttributeRef
			attributeNameInTable = new TableItemLocator("attribute0");
			ui.click(attributeNameInTable);
			type = new LabeledTextLocator("Type: ");
			assertEquals("Referenced type not updated in Editor","simple.moved.DataMiddle",type.getText(ui));

			
			// Checks for Top
			ui.click(new CTabItemLocator("DataMiddle"));
			// Check for Extends in this one
			extend = new LabeledTextLocator("Extends: ");
			assertEquals("Extended type not updated in Editor","simple.moved.DataTop",extend.getText(ui));
			
			String[] shortEditors = {"AssociatedEnt", "Ent1"};
			for (String editor : shortEditors){
				ui.click(new CTabItemLocator("*"+editor));
				ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
				ui.close(new CTabItemLocator(editor));
			}
			
			String[] shortOwnEditors = {"DataTop", "DataMiddle", "DataBottom"};
			for (String editor : shortOwnEditors){
				ui.close(new CTabItemLocator(editor));
			}
			
		}


}
