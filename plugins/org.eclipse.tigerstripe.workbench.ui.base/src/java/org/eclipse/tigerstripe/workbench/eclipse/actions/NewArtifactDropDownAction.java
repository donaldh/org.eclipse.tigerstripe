/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.eclipse.actions;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.profile.IActiveWorkbenchProfileChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate2;

/**
 * @author Eric Dillon
 * 
 * TODO make this configured to be an extension point for new artifacts
 */
public class NewArtifactDropDownAction extends Action implements IMenuCreator,
		IWorkbenchWindowPulldownDelegate2,
		IActiveWorkbenchProfileChangeListener {

	private Menu fMenu;

	private boolean profileChanged = false;

	public void profileChanged(IWorkbenchProfile newActiveProfile) {
		profileChanged = true;
	}

	public NewArtifactDropDownAction() {
		fMenu = null;
		setMenuCreator(this);

		// register for changes in the profile, so the menu can be rebuilt
		// dynamically
		TigerstripeCore.getWorkbenchProfileSession().addActiveProfileListener(this);
	}

	public void dispose() {
		if (fMenu != null) {
			fMenu.dispose();
			fMenu = null;
		}
	}

	public Menu getMenu(Menu parent) {
		return null;
	}

	public Menu getMenu(Control parent) {

		if (profileChanged) {
			if (fMenu != null)
				fMenu.dispose();
			fMenu = null;
			profileChanged = false;
		}

		if (fMenu == null) {
			fMenu = new Menu(parent);

			ArrayList<Action> actionsList = new ArrayList<Action>();

			// @since 1.2
			// All core artifacts are conditioned by the active profile
			IWorkbenchProfile profile = TigerstripeCore.getWorkbenchProfileSession()
					.getActiveProfile();
			CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) profile
					.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);

			if (prop.getDetailsForType(IManagedEntityArtifact.class.getName())
					.isEnabled()) {
				actionsList.add(new OpenNewEntityArtifactWizardAction());
			}
			if (prop.getDetailsForType(IDatatypeArtifact.class.getName())
					.isEnabled()) {
				actionsList.add(new OpenNewDatatypeArtifactWizardAction());
			}
			if (prop.getDetailsForType(IEnumArtifact.class.getName())
					.isEnabled()) {
				actionsList.add(new OpenNewEnumArtifactWizardAction());
			}
			if (prop.getDetailsForType(IAssociationArtifact.class.getName())
					.isEnabled()) {
				actionsList.add(new OpenNewAssociationArtifactWizardAction());
			}
			if (prop.getDetailsForType(IDependencyArtifact.class.getName())
					.isEnabled()) {
				actionsList.add(new OpenNewDependencyArtifactWizardAction());
			}
			if (prop.getDetailsForType(
					IAssociationClassArtifact.class.getName()).isEnabled()) {
				actionsList
						.add(new OpenNewAssociationClassArtifactWizardAction());
			}
			if (prop.getDetailsForType(IQueryArtifact.class.getName())
					.isEnabled()) {
				actionsList.add(new OpenNewQueryArtifactWizardAction());
			}
			if (prop
					.getDetailsForType(IUpdateProcedureArtifact.class.getName())
					.isEnabled()) {
				actionsList
						.add(new OpenNewUpdateProcedureArtifactWizardAction());
			}
			if (prop.getDetailsForType(IEventArtifact.class.getName())
					.isEnabled()) {
				actionsList.add(new OpenNewNotificationArtifactWizardAction());
			}
			if (prop.getDetailsForType(ISessionArtifact.class.getName())
					.isEnabled()) {
				actionsList.add(new OpenNewSessionArtifactWizardAction());
			}
			if (prop.getDetailsForType(IExceptionArtifact.class.getName())
					.isEnabled()) {
				actionsList.add(new OpenNewExceptionArtifactWizardAction());
			}

			for (Action action : actionsList) {
				ActionContributionItem item = new ActionContributionItem(action);
				item.fill(fMenu, -1);
			}

		}
		return fMenu;
	}

	@Override
	public void run() {
		(new OpenNewEntityArtifactWizardAction()).run();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		run();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		IAbstractTigerstripeProject aProject = EclipsePlugin
				.getTSProjectInFocus();
		action.setEnabled(aProject instanceof ITigerstripeModelProject
				&& aProject.exists());
	}

}
