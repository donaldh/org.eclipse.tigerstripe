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

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class DiagramHelper {

	private static String project="model-refactoring";
	private static String[] diagrams = {"src/simple/default", "src/simple/moved/inside-moved","outside-class-diagram"};
	
	public static void openDiagrams(IUIContext ui) throws Exception{
		ViewLocator view = new ViewLocator(
		"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		for (String diagram : diagrams){
			ui.click(2,	new TreeItemLocator(project+"/"+diagram,view));
		}
	}
	
	public static void saveDiagrams(IUIContext ui) throws Exception{
		// We assume they are all dirty!
		for (String diagram : diagrams){
			String diagName[] = diagram.split("/");
			String dName = diagName[diagName.length-1];
			ui.click(new CTabItemLocator("*"+dName+".wvd"));
			ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		}
	}

	public static void closeDiagrams(IUIContext ui) throws Exception{

		for (String diagram : diagrams){
			String diagName[] = diagram.split("/");
			String dName = diagName[diagName.length-1];
			ui.click(new CTabItemLocator(dName+".wvd"));
		}
	}
	
}
