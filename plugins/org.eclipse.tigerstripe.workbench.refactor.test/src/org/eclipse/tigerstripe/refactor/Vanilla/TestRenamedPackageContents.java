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
import org.eclipse.tigerstripe.refactor.artifact.Session0_to_Session00;
import org.eclipse.tigerstripe.refactor.pckge.Simple_to_Complicated;
import org.eclipse.tigerstripe.refactor.project.ProjectHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class TestRenamedPackageContents extends UITestCaseSWT {

		
		private static String project="model-refactoring";
		
		
		public void testForAllRenamedArtifacts() throws Exception {
			IUIContext ui = getUI();
			// Check we still have all of our artifacts?
			ArtifactHelper.checkArtifactInExplorer(ui, project, "complicated", "Ent10");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "complicated", "Ent2");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "complicated", "SuperEnt");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "complicated", "AssociatedEnt");
			
			ArtifactHelper.checkArtifactInExplorer(ui, project, "complicated", "Association00");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "complicated", "Association1");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "complicated", "Association2");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "complicated", "AssociationClass00");
			
			ArtifactHelper.checkArtifactInExplorer(ui, project, "complicated", "DataTop");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "complicated", "DataMiddle0");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "complicated", "DataBottom0");
			
			ArtifactHelper.checkArtifactInExplorer(ui, project, "complicated", "Enumeration00");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "complicated", "Query00");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "complicated", "Update00");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "complicated", "Dependency00");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "complicated", "Exception00");
			
			ArtifactHelper.checkArtifactInExplorer(ui, project, "complicated", "SessionFacade00");
			
			ArtifactHelper.checkPackageInExplorer(ui, project, "complicated.moved");
			ArtifactHelper.checkPackageInExplorer(ui, project, "complicated.movedOther");
			
		}
		
		public void testForExtraComponentInExplorer() throws Exception {
			//TODO Find out what is extra - In ONE simple message!
		}
		

}
