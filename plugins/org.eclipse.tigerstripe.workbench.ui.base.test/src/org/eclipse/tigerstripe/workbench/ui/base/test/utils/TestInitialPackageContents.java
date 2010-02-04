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
package org.eclipse.tigerstripe.workbench.ui.base.test.utils;

import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;

public class TestInitialPackageContents extends UITestCaseSWT {

		
		private static String project="model-refactoring";
		
		public static String[] artifacts = new String[]{
				"Ent1",
				"Ent2",
				"SuperEnt",
				"AssociatedEnt",
				"Association0",
				"Association1",
				"Association2",
				"AssociationClass0",
				
				"DataTop",
				"DataMiddle",
				"DataBottom",
				
				"Enumeration0",
				"Query0",
				"Update0",
				"Dependency0",
				"Exception0",
				"Event0",
				"SessionFacade0"
				};
		
		
		public static void testArtifacts(IUIContext ui) throws Exception {
			// Check we still have all of our artifacts?
			for (String artifact : artifacts){
				ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", artifact);
			}
			ArtifactHelper.checkPackageInExplorer(ui, project, "simple");
			ArtifactHelper.checkPackageInExplorer(ui, project, "simple.moved");
			ArtifactHelper.checkPackageInExplorer(ui, project, "simple.movedOther");
			
		}
		
		

}
