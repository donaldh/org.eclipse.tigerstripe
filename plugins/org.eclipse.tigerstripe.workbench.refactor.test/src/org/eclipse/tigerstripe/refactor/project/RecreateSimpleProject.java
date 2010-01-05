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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.queries.IQueryRelationshipsByArtifact;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.eclipse.tigerstripe.workbench.ui.base.test.utils.GuiUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.locator.XYLocator;
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

public class RecreateSimpleProject extends UITestCaseSWT {

	private static String project="model-refactoring";

	
	public void testCreateThroughExplorer() throws Exception{
		IUIContext ui = getUI();
		//ui.click(new SWTWidgetLocator(ToolItem.class, "", 0,
		//		new SWTWidgetLocator(ToolBar.class, 2, new SWTWidgetLocator(
		//				CoolBar.class))));
		ui
		  .click(new ContributedToolItemLocator(
		    "org.eclipse.tigerstripe.workbench.ui.menu.new.patterns.project.dropdown.org.eclipse.tigerstripe.workbench.base.Project"));
		
		ui.wait(new ShellShowingCondition("Create a new Tigerstripe Project"));
		ui.click(new LabeledTextLocator("&Project Name:"));
		ui.enterText(project);
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Create a new Tigerstripe Project"));
		
		// It should be empty!
		// Just make sure we got what we expected!
		IAbstractTigerstripeProject aProject = TigerstripeCore.findProject("model-refactoring");
		ITigerstripeModelProject modelProject = (ITigerstripeModelProject) aProject;
		IArtifactManagerSession mgrSession = modelProject
			.getArtifactManagerSession();

		//How Many Artifacts should we have?
		IArtifactQuery query = mgrSession.makeQuery(IQueryAllArtifacts.class.getName());
		Collection<IAbstractArtifact> allArtifacts = mgrSession.queryArtifact(query);
//		for (IAbstractArtifact art: allArtifacts){
//			System.out.println("On reload : "+art.getFullyQualifiedName());
//		}
		assertEquals("Incorrect number of artifacts", 0,allArtifacts.size());
		
	}
	
	
	
}
