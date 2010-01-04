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
package org.eclipse.tigerstripe.ui.visualeditor.test.diagram;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.PullDownMenuItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class DiagramHelper {

	
	public void createPackage(IUIContext ui, String packageName) throws Exception {	
//		ui.click(new TreeItemLocator(
//				TestingConstants.NEW_MODEL_PROJECT_NAME,
//				new ViewLocator(
//						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
//		
//		ui
//		.click(new PullDownMenuItemLocator(
//				"Package",
//				new ContributedToolItemLocator(
//						"org.eclipse.tigerstripe.workbench.ui.menu.new.patterns.dropdown.org.eclipse.tigerstripe.workbench.base.ManagedEntity")));
		
		ui.contextClick(
				new TreeItemLocator(
						TestingConstants.NEW_MODEL_PROJECT_NAME+"/src",
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
				"New/"+"Package");
		ui.wait(new ShellShowingCondition("Create a new Package"));
		ui.click(new LabeledTextLocator("Name:"));
		ui.enterText(packageName);
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Create a new Package"));
		ui.close(new CTabItemLocator(packageName));
		
		
		
	}
	
	/**
	 * This is a simple create using just the name.
	 * @throws Exception
	 */
	public String addArtifact(IUIContext ui,String myType, String packageName, String thisArtifactName, boolean hasAttributes,
			boolean hasLiterals, boolean hasMethods, boolean hasEnds) throws Exception	{
		return addArtifact( ui,  myType,  packageName, thisArtifactName,  hasAttributes,
				 hasLiterals,  hasMethods,  hasEnds, "", "");
	}

	/**
	 * This is a simple create using just the name.
	 * @throws Exception
	 */
	public String addArtifact(IUIContext ui,String myType, String packageName, String thisArtifactName, boolean hasAttributes,
			boolean hasLiterals, boolean hasMethods, boolean hasEnds, String endA, String endB) throws Exception	{
		
		
		
//		ui.click(new TreeItemLocator(
//				TestingConstants.NEW_MODEL_PROJECT_NAME,
//				new ViewLocator(
//						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
//		if (myType.equals("Entity")) {
//			// If it an entity we need the top item - as its not in the drop
//			// down
//			ui
//			.click(new ContributedToolItemLocator(
//					"org.eclipse.tigerstripe.workbench.ui.menu.new.patterns.dropdown.org.eclipse.tigerstripe.workbench.base.ManagedEntity"));
//		} else {
//			ui
//					.click(new PullDownMenuItemLocator(
//							myType,
//							new ContributedToolItemLocator(
//									"org.eclipse.tigerstripe.workbench.ui.menu.new.patterns.dropdown.org.eclipse.tigerstripe.workbench.base.ManagedEntity")));
//		}
		
		ui.contextClick(
				new TreeItemLocator(
						TestingConstants.NEW_MODEL_PROJECT_NAME+"/src",
						new ViewLocator(
								"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
				"New/"+myType);
		
		ui.wait(new ShellShowingCondition("Create a new "+myType));
		
		ui.enterText(thisArtifactName);
		LabeledTextLocator pack = new LabeledTextLocator("Artifact Package:");
		ui.click(pack);
		ui.keyClick(SWT.CTRL, 'a');
		ui.enterText(packageName);
		
		
		if (hasEnds){
			ui.click(new LabeledLocator(Button.class, "aEnd Type"));
			ui.wait(new ShellShowingCondition(myType+" End Type"));
			ui.click(new TableItemLocator(endA));
			ui.click(new ButtonLocator("OK"));
			ui.wait(new ShellDisposedCondition(myType+" End Type"));
			ui.click(new LabeledLocator(Button.class, "zEnd Type"));
			ui.wait(new ShellShowingCondition(myType+" End Type"));
			ui.click(new TableItemLocator(endB));
			ui.click(new ButtonLocator("OK"));
			ui.wait(new ShellDisposedCondition(myType+" End Type"));
		}
		
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Create a new "+myType));
		
		CTabItemLocator artifactEditor = new CTabItemLocator(
				thisArtifactName);
		
		// Close the editor
		ui.close(new CTabItemLocator(thisArtifactName));
		return thisArtifactName;
	}

}
