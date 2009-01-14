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
package org.eclipse.tigerstripe.refactor.project;

import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;

public class TestInitialPackageContents extends UITestCaseSWT {

		
		private static String project="model-refactoring";
		
		
		public static void testArtifacts(IUIContext ui) throws Exception {
			// Check we still have all of our artifacts?
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "Ent1");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "Ent2");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "SuperEnt");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "AssociatedEnt");
			
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "Association0");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "Association1");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "Association2");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "AssociationClass0");
			
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "DataTop");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "DataMiddle");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "DataBottom");
			
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "Enumeration0");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "Query0");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "Update0");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "Dependency0");
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "Exception0");
			
			ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "SessionFacade0");
			
			ArtifactHelper.checkPackageInExplorer(ui, project, "simple.moved");
			ArtifactHelper.checkPackageInExplorer(ui, project, "simple.movedOther");
			
		}
		
		

}
