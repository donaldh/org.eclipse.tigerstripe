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
package org.eclipse.tigerstripe.refactor.artifact;

import java.util.ArrayList;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.ui.visualeditor.test.finders.LocatorHelper;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute5EditPart;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WT;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.locator.WidgetReference;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class DataBottom_to_DataBottom0 extends UITestCaseSWT {

	private static String project="model-refactoring";
	
	
	public static void doChangeThroughDiagramInSamePackage(IUIContext ui) throws Exception{
		LocatorHelper helper = new LocatorHelper();
		ui.click(new CTabItemLocator("default.wvd"));
		IWidgetLocator tfl = helper.getNameEditLocator(ui,"DataBottom");
		ui.click(tfl);
		ui.click(tfl);
		Thread.sleep(1000);
		ui.click(tfl);
		ui.click(new SWTWidgetLocator(Text.class,
				new SWTWidgetLocator(FigureCanvas.class)));
		ui.keyClick(WT.END);
		ui.enterText("0");
		ui.keyClick(WT.CR);
		
	}
	
	public static void checkDiagramsAfterMove(IUIContext ui) throws Exception{
		LocatorHelper helper = new LocatorHelper();
		ui.click(new CTabItemLocator("default.wvd"));
		String artifactPrefix = "simple.moved.";
		internalCheckDiagram(ui, helper, artifactPrefix,"DataBottom");
		artifactPrefix = "";
		ui.click(new CTabItemLocator("inside-moved.wvd"));
		internalCheckDiagram(ui, helper, artifactPrefix,"DataBottom");
		artifactPrefix = "simple.moved.";
		ui.click(new CTabItemLocator("outside-class-diagram.wvd"));
		internalCheckDiagram(ui, helper, artifactPrefix,"DataBottom");
		
	}
	
	public static void checkDiagrams(IUIContext ui) throws Exception{
		LocatorHelper helper = new LocatorHelper();
		ui.click(new CTabItemLocator("default.wvd"));
		String artifactPrefix = "simple";
		internalCheckDiagram(ui, helper, artifactPrefix,"DataBottom0");
		artifactPrefix = "simple.";
		ui.click(new CTabItemLocator("inside-moved.wvd"));
		internalCheckDiagram(ui, helper, artifactPrefix,"DataBottom0");
		ui.click(new CTabItemLocator("outside-class-diagram.wvd"));
		internalCheckDiagram(ui, helper, artifactPrefix,"DataBottom0");
		
	}
	
	public static void internalCheckDiagram(IUIContext ui, LocatorHelper helper, String artifactPrefix, String artifactName){
		
		// Basic rename
		try {
			ui.click(helper.getDatatypeLocator(ui, artifactPrefix+artifactName));
		} catch (Exception e){
			fail("Refactored Entity '"+ artifactPrefix+artifactName +"' not found on diagram");		}
		
		IWidgetLocator attr = helper.getDatatypeAttributeLocator(ui, artifactPrefix+artifactName, "recur");
		WidgetReference attrRef = (WidgetReference) attr;
		Attribute5EditPart.AttributeLabelFigure fig = (Attribute5EditPart.AttributeLabelFigure) attrRef.getWidget();
		String figText = fig.getText();
		// Return type should be our new Ent, as should the arg type - ie
		// By default, the internal packages are hidden
		String expectedText = "+recur:"+artifactName;
		assertEquals("Attribute Ref not updated on diagram",expectedText, figText);
	}
	
	
	
	/**
	 * NOTE THAT THIS IS DIFFERNET FROM OTHERS.
	 * 
	 * We need to chck the thing we just renamed - which gets closed
	 * when we do a rename
	 * 
	 */
	public static void checkEditorUpdated(IUIContext ui) throws Exception{
		ViewLocator view = new ViewLocator(
		"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		ui.click(2,	new TreeItemLocator(project+"/src/simple/DataBottom0",view));
		// Check for An AttributeRef
		TableItemLocator attributeNameInTable = new TableItemLocator("recur");
		ui.click(attributeNameInTable);
		LabeledTextLocator type = new LabeledTextLocator("Type: ");
		assertEquals("Referenced type not updated in Editor","simple.DataBottom0",type.getText(ui));
		
		ui.click(new CTabItemLocator("DataBottom0"));
		ui.click(new ContributedToolItemLocator("org.eclipse.ui.file.save"));
		ui.close(new CTabItemLocator("DataBottom0"));
		
	}
	
	public static void doChangeThroughExplorer(IUIContext ui) throws Exception{
		ViewLocator view = new ViewLocator(
			"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		ui
		.contextClick(
				new TreeItemLocator(
						project+"/src/simple/DataBottom",
						view),
		"Refactor/Rename...");
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellShowingCondition("Rename Compilation Unit"));
		LabeledTextLocator locator = new LabeledTextLocator("New na&me:");
		GuiUtils.clearText(ui, locator);
		ui.click(locator);
		ui.enterText("DataBottom0");
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Rename Compilation Unit"));
		
		// Let the updates happen!
		Thread.sleep(500);
	}
	
	
	public static void checkExplorerUpdates(IUIContext ui) throws Exception{
		ViewLocator view = new ViewLocator(
			"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		
		// Check for ourself!
		ArtifactHelper.checkArtifactInExplorer(ui, project, "simple", "DataBottom0");
		
		
		//Referenced Attribute updates should appear in the explorer
		ArrayList<String> items = new ArrayList<String>();
		items.add("recur"+":"+"DataBottom0");
		ArtifactHelper.checkItemsInExplorer(ui,project,
				"simple","DataBottom0",items);
		
		
	}
	
	// In here we look for the changes that don't necessarily appear on the UI!
	@SuppressWarnings("deprecation")
	public static void checkAPI() throws Exception{

		IAbstractTigerstripeProject aProject = TigerstripeCore.findProject(project);
		ITigerstripeModelProject modelProject = (ITigerstripeModelProject) aProject;
		IArtifactManagerSession mgrSession = modelProject
			.getArtifactManagerSession();
		// First check that the old Entity no longer exists!
		IAbstractArtifact db = mgrSession
			.getArtifactByFullyQualifiedName("simple.DataBottom");
		assertNull("Old artifact is still being returned from the Art Mgr", db);
		
		IAbstractArtifact db0 = mgrSession
			.getArtifactByFullyQualifiedName("simple.DataBottom0");
		assertNotNull("New artifact is still not being returned from the Art Mgr", db0);
		
		
	}
	
}
