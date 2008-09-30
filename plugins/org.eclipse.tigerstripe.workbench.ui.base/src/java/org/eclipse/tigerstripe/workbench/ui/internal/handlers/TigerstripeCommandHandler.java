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
package org.eclipse.tigerstripe.workbench.ui.internal.handlers;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Category;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jdt.ui.actions.IJavaEditorActionDefinitionIds;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.PatternFactory;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenGenerateInterfaceWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewPatternBasedArtifactWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewPatternBasedProjectWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewProjectWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.PluginsControlDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.ProfileDetailsDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;

public class TigerstripeCommandHandler extends AbstractHandler implements IHandler {

	private final String PLUGIN_COMMAND ="org.eclipse.tigerstripe.workbench.ui.base.plugins";
	private final String PROFILES_COMMAND ="org.eclipse.tigerstripe.workbench.ui.base.profile";
	private final String GENERATE_COMMAND ="org.eclipse.tigerstripe.workbench.ui.base.generate";
	private final String PATTERN_COMMAND ="org.eclipse.tigerstripe.workbench.ui.base.patternBasedCreate";
	private final String PROJECT_PATTERN_COMMAND ="org.eclipse.tigerstripe.workbench.ui.base.patternBasedProjectCreate";
	
	public Object execute(ExecutionEvent event)
		throws ExecutionException {
		
		Command command = event.getCommand();
		String id = command.getId();

		IWorkbenchWindow window = 
			HandlerUtil.getActiveWorkbenchWindowChecked(event);

		if (id.equals(PLUGIN_COMMAND)){
			PluginsControlDialog dialog = new PluginsControlDialog(window
					.getShell());
			dialog.open();
		} else if (id.equals(PROFILES_COMMAND)){
			ProfileDetailsDialog dialog = new ProfileDetailsDialog(window
					.getShell());
			dialog.open();
		} else if (id.equals(GENERATE_COMMAND)){
			OpenGenerateInterfaceWizardAction generateAction = new OpenGenerateInterfaceWizardAction();
			generateAction.run();
		} else if (id.equals(PATTERN_COMMAND)){
			String patternName = event.getParameter("org.eclipse.tigerstripe.workbench.ui.base.patternName");
			IPattern pattern = PatternFactory.getInstance().getPattern(patternName);
			OpenNewPatternBasedArtifactWizardAction artifactAction = new OpenNewPatternBasedArtifactWizardAction(pattern);
			artifactAction.run();
		} else if (id.equals(PROJECT_PATTERN_COMMAND)){
			String patternName = event.getParameter("org.eclipse.tigerstripe.workbench.ui.base.patternName");
			IPattern pattern = PatternFactory.getInstance().getPattern(patternName);
			OpenNewPatternBasedProjectWizardAction newProjectAction = new OpenNewPatternBasedProjectWizardAction(pattern);
			newProjectAction.run();
		}

		return null;

	}
}
