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

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class DiagramHelper {

	private static String project="model-refactoring";
	private static String projectReference = "model-refactoring-reference";
	private static String[] model_refactoringdiagrams = {"src/simple/default", "src/simple/moved/inside-moved","outside-class-diagram"};
	private static String[] model_refactoring_referencediagrams = {"src/simple/classDiagram","src/simple/instanceDiagram"};
	private static String[] model_refactoring_reference_class_diagrams= {"src/simple/classDiagram"};
	private static String instanceDiagram = "outside-instance-diagram";
	private static String refInstanceDiagram = "instanceDiagram";
	
	public static void openDiagrams(IUIContext ui,String projectName) throws Exception{
		Thread.sleep(5000);
		ViewLocator view = new ViewLocator(
		"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		String[] diagrams = null;
		if(projectName.equals(project)){
			diagrams = new String[model_refactoringdiagrams.length];
			diagrams = model_refactoringdiagrams;
		}
		else if(projectName.equals(projectReference)){
			diagrams = new String[model_refactoring_referencediagrams.length];
			diagrams = model_refactoring_referencediagrams;
		}
		for (String diagram : diagrams){
			ui.click(2,	new TreeItemLocator(projectName+"/"+diagram,view));
		}
	}
	
	public static void openInstanceDiagram(IUIContext ui) throws Exception{
		Thread.sleep(5000);
		ViewLocator view = new ViewLocator("org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		ui.click(2, new TreeItemLocator(project+"/"+instanceDiagram,view));
	}
	
	public static void saveDiagrams(IUIContext ui) throws Exception{
		// We assume they are all dirty!
		for (String diagram : model_refactoringdiagrams){
			String diagName[] = diagram.split("/");
			String dName = diagName[diagName.length-1];
			try {
				ui.click(new CTabItemLocator("*"+dName+".wvd"));
				ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
			} catch (Exception e){
				// Guess it was not dirty!
			}
			
		}
	}

	public static void closeClassDiagrams(IUIContext ui, String projectName) throws Exception{
		String[] diagrams = null;
		if(projectName.equals(project)){
			diagrams = new String[model_refactoringdiagrams.length];
			diagrams = model_refactoringdiagrams;
		}
		else if(projectName.equals(projectReference)){
			diagrams = new String[model_refactoring_reference_class_diagrams.length];
			diagrams = model_refactoring_reference_class_diagrams;
		}
		for (String diagram : diagrams){
			String diagName[] = diagram.split("/");
			String dName = diagName[diagName.length-1];
			ui.click(new CTabItemLocator(dName+".wvd"));
		}
	}
	
	
	
}
