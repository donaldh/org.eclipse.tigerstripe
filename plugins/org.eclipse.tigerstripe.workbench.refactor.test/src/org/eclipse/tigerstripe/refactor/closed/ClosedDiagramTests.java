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
import org.eclipse.tigerstripe.refactor.suite.DiagramHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class ClosedDiagramTests extends UITestCaseSWT {

	private ProjectHelper helper;
	private ArtifactHelper artifactHelper;
	private IUIContext ui;
	private ViewLocator view;
	
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



		/**
			 * Ent 1 was used in:
			 * 	Association0 - Zend - called ent1_0
			 *  Association1 - AEnd, Zend
			 *  Association2 - AEnd
			 *  AssociationClass0 - AEnd, Zend
			 *  Dependency0 - AEnd
			 *  
			 *  Returned by Query0
			 *  
			 *  Ent2 Method Return type and Arg1 type
			 *  
			 * Ent 1 EXTENDS SuperEnt - need to check ExtendedBy 
			 * Ent 2 EXTENDS Ent1
			 * Ent1 Implements SessionFacade0 - need to check ImplementedBy
			 */
//			Ent1_to_Ent10.openRelatedEditors(ui);
			Ent1_to_Ent10.doChangeThroughExplorer(ui);
//			Ent1_to_Ent10.checkExplorerUpdates(ui);
//			Ent1_to_Ent10.checkAPI();
//			Ent1_to_Ent10.checkEditorUpdated(ui);
//			Ent1_to_Ent10.saveAndCloseRelatedEditors(ui);
			DiagramHelper.openDiagrams(ui);
			Ent1_to_Ent10.checkDiagrams(ui);
			DiagramHelper.closeDiagrams(ui);
//				
//			/**
//			 * Enumeration0 was used by Ent1 - now Ent10
//			 */
//			Enumeration0_to_Enumeration00.openRelatedEditors(ui);
//			Enumeration0_to_Enumeration00.doChangeThroughExplorer(ui);
//			Enumeration0_to_Enumeration00.checkExplorerUpdates(ui);
//			Enumeration0_to_Enumeration00.checkAPI();
//			Enumeration0_to_Enumeration00.checkEditorUpdated(ui);
//			Enumeration0_to_Enumeration00.saveAndCloseRelatedEditors(ui);
//			
//			/**
//			 * DataMiddle was used in 
//			 * 	DataTop & DataBottom
//			 *  Ent10 & AssociatedEnt
//			 */
//			DataMiddle_to_DataMiddle0.openRelatedEditors(ui);
//			DataMiddle_to_DataMiddle0.doChangeThroughExplorer(ui);
//			DataMiddle_to_DataMiddle0.checkExplorerUpdates(ui);
//			DataMiddle_to_DataMiddle0.checkAPI();
//			DataMiddle_to_DataMiddle0.checkEditorUpdated(ui);
//			DataMiddle_to_DataMiddle0.saveAndCloseRelatedEditors(ui);
//			
//			
//			/**
//			 * DataBottom was used in 
//			 *  DataBottom (self ref)
//			 * 
//			 * Note - only impacted editor is ourself!
//			 * 
//			 */
//			DataBottom_to_DataBottom0.doChangeThroughExplorer(ui);
//			DataBottom_to_DataBottom0.checkExplorerUpdates(ui);
//			DataBottom_to_DataBottom0.checkAPI();
//			DataBottom_to_DataBottom0.checkEditorUpdated(ui);
//			
//			/**
//			 *  Association0 was going
//			 *  from AssociatedEnt
//			 *  to Ent10
//			 *  No editors impacted
//			 *  
//			 */
//			Association0_to_Association00.doChangeThroughExplorer(ui);
//			Association0_to_Association00.checkExplorerUpdates(ui);
//			Association0_to_Association00.checkAPI();
//
//			/**
//			 *  AssociationClass0 was going
//			 *  from Ent
//			 *  to Ent10
//			 *  Also at the end of Association2
//			 */
//			AssociationClass0_to_AssociationClass00.openRelatedEditors(ui);
//			AssociationClass0_to_AssociationClass00.doChangeThroughExplorer(ui);
//			AssociationClass0_to_AssociationClass00.checkExplorerUpdates(ui);
//			AssociationClass0_to_AssociationClass00.checkAPI();
//			AssociationClass0_to_AssociationClass00.checkEditorUpdated(ui);
//			AssociationClass0_to_AssociationClass00.saveAndCloseRelatedEditors(ui);
//			
//			
//			/**
//			 * Dependency0
//			 * From Ent10 to DataTop
//			 *  No editors impacted
//			 */
//			Dependency0_to_Dependency00.doChangeThroughExplorer(ui);
//			Dependency0_to_Dependency00.checkExplorerUpdates(ui);
//			Dependency0_to_Dependency00.checkAPI();
//			
//			
//			/**
//			 * Exception0
//			 * Used in a method on Ent1
//			 */
//			Exception0_to_Exception00.openRelatedEditors(ui);
//			Exception0_to_Exception00.doChangeThroughExplorer(ui);
//			Exception0_to_Exception00.checkExplorerUpdates(ui);
//			Exception0_to_Exception00.checkAPI();
//			Exception0_to_Exception00.checkEditorUpdated(ui);
//			Exception0_to_Exception00.saveAndCloseRelatedEditors(ui);
//			
//			/**
//			 * Update0
//			 * Not used anywhere!
//			 *  No editors impacted
//			 */
//			Update0_to_Update00.doChangeThroughExplorer(ui);
//			Update0_to_Update00.checkExplorerUpdates(ui);
//			Update0_to_Update00.checkAPI();
//			
//			/**
//			 * Event0
//			 *  Not used anywhere!
//			 *  No editors impacted
//			 */
//			Event0_to_Event00.doChangeThroughExplorer(ui);
//			Event0_to_Event00.checkExplorerUpdates(ui);
//			Event0_to_Event00.checkAPI();
//			
//			
//			/**
//			 * Query0
//			 *  Not used anywhere!
//			 *  No editors impacted
//			 */
//			Query0_to_Query00.doChangeThroughExplorer(ui);
//			Query0_to_Query00.checkExplorerUpdates(ui);
//			Query0_to_Query00.checkAPI();
//			
//			/**
//			 *  SessionFacade0 was implemented
//			 *  by Ent10
//			 */
//			Session0_to_Session00.openRelatedEditors(ui);
//			Session0_to_Session00.doChangeThroughExplorer(ui);
//			Session0_to_Session00.checkExplorerUpdates(ui);
//			Session0_to_Session00.checkAPI();
//			Session0_to_Session00.checkEditorUpdated(ui);
//			Session0_to_Session00.saveAndCloseRelatedEditors(ui);
//			
//			
//			
//			
			
		}
		
		

}
