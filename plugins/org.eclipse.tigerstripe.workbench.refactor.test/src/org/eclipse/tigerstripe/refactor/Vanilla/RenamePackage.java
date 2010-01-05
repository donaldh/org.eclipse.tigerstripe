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

import org.eclipse.tigerstripe.refactor.pckge.Simple_to_Complicated;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;

public class RenamePackage extends UITestCaseSWT {

		private IUIContext ui;

		
		public void testPackageRename() throws Exception {
						
			/**
			 * Rename the package!
			 * 
			 */
			IUIContext ui = getUI();
			Simple_to_Complicated.doChangeThroughExplorer(ui);
			Simple_to_Complicated.checkAPI();
			Simple_to_Complicated.checkExplorerUpdates(ui);
			
			
		}
		
		

}
