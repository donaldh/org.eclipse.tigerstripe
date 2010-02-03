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
package org.eclipse.tigerstripe.refactor.suite;

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
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.ProjectHelper;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class DeleteProject extends UITestCaseSWT {

		private ProjectHelper helper;
		private static String project="model-refactoring";
		private static String referenceProject = "model-refactoring-reference";
		
		private IUIContext ui;
		
		public void setUp() throws Exception{
			ui = getUI();
			helper = new ProjectHelper();			
			
			
		}
		
		public void testDeleteIt() throws Exception {
			helper.deleteProject(ui,project);
			
			// Make sure it no longer exists
			IAbstractTigerstripeProject aProject = TigerstripeCore.findProject(project);
			assertNull("Project still exists after Delete",aProject);
			
			IAbstractTigerstripeProject bProject = TigerstripeCore.findProject(referenceProject);
			if(bProject != null && bProject.exists()){
				helper.deleteProject(ui,referenceProject);
				
				// Make sure it no longer exists
				bProject = TigerstripeCore.findProject(referenceProject);
				assertNull("Project still exists after Delete",bProject);
			}
			
		}
		
		

}
