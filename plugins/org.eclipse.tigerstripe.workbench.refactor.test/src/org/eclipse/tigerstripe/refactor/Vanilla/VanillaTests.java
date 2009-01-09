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

import org.eclipse.tigerstripe.refactor.artifact.Association0_to_Association00;
import org.eclipse.tigerstripe.refactor.artifact.AssociationClass0_to_AssociationClass00;
import org.eclipse.tigerstripe.refactor.artifact.DataBottom_to_DataBottom0;
import org.eclipse.tigerstripe.refactor.artifact.DataMiddle_to_DataMiddle0;
import org.eclipse.tigerstripe.refactor.artifact.Ent1_to_Ent10;
import org.eclipse.tigerstripe.refactor.artifact.Enumeration0_to_Enumeration00;
import org.eclipse.tigerstripe.refactor.project.ProjectHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class VanillaTests extends UITestCaseSWT {

		private ProjectHelper helper;
		private ArtifactHelper artifactHelper;
		private IUIContext ui;
		private ViewLocator view;
		
		public void setUp() throws Exception{
			ui = getUI();
			helper = new ProjectHelper();
			artifactHelper = new ArtifactHelper();
			
			helper.loadProjectFromCVS(ui);
			
			// In this test case we don't want any diagrams.....
			// There are 4 diagrams
			helper.deleteDiagrams(ui);
				
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
			 *  
			 * Ent 1 EXTENDS SuperEnt - need to check ExtendedBy 
			 * Ent 2 EXTENDS Ent1
			 * Ent1 Implements SessionFacade0 - need to check ImplementedBy
			 */
			Ent1_to_Ent10.doChangeThroughExplorer(ui);
			Ent1_to_Ent10.checkExplorerUpdates(ui);
			Ent1_to_Ent10.checkAPI();
				
			/**
			 * Enumeration0 was used by Ent1 - now Ent10
			 */
			Enumeration0_to_Enumeration00.doChangeThroughExplorer(ui);
			Enumeration0_to_Enumeration00.checkExplorerUpdates(ui);
			Enumeration0_to_Enumeration00.checkAPI();
			
			/**
			 * DataMiddle was used in 
			 * 	DataTop & DataBottom
			 *  Ent10 & AssociatedEnt
			 */
			DataMiddle_to_DataMiddle0.doChangeThroughExplorer(ui);
			DataMiddle_to_DataMiddle0.checkExplorerUpdates(ui);
			DataMiddle_to_DataMiddle0.checkAPI();
			
			/**
			 * DataBottom was used in 
			 *  DataBottom (self ref)
			 * 
			 */
			DataBottom_to_DataBottom0.doChangeThroughExplorer(ui);
			DataBottom_to_DataBottom0.checkExplorerUpdates(ui);
			DataBottom_to_DataBottom0.checkAPI();
			
			/**
			 *  Association0 was going
			 *  from AssociatedEnt
			 *  to Ent10
			 */
			Association0_to_Association00.doChangeThroughExplorer(ui);
			Association0_to_Association00.checkExplorerUpdates(ui);
			Association0_to_Association00.checkAPI();
			
			/**
			 *  AssociationClass0 was going
			 *  from Ent
			 *  to Ent10
			 *  Also at the end of Association2
			 */
			AssociationClass0_to_AssociationClass00.doChangeThroughExplorer(ui);
			AssociationClass0_to_AssociationClass00.checkExplorerUpdates(ui);
			AssociationClass0_to_AssociationClass00.checkAPI();
		}
		
		

}
