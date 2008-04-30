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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.ui.actions.ActionMessages;
import org.eclipse.jdt.ui.IContextMenuConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripePluginProjectNature;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripeProjectNature;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenBasicNewFileWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenBasicNewFolderWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewAnnotationWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewAssociationArtifactWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewAssociationClassArtifactWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewClassWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewDatatypeArtifactWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewDependencyArtifactWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewEntityArtifactWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewEnumArtifactWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewEnumerationWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewExceptionArtifactWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewInterfaceWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewNotificationArtifactWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewPackageWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewQueryArtifactWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewSessionArtifactWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewUpdateProcedureArtifactWizardAction;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.actions.NewProjectAction;
import org.eclipse.ui.actions.NewWizardAction;

/**
 * Action group for all new wizards within the Tigerstripe Explorer.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class NewWizardsActionGroup extends ActionGroup {

	/**
	 * Creates a new <code>NewWizardsActionGroup</code>. The group requires
	 * that the selection provided by the part's selection provider is of type
	 * <code>
	 * org.eclipse.jface.viewers.IStructuredSelection</code>.
	 * 
	 * @param site
	 *            the view part that owns this action group
	 */
	public NewWizardsActionGroup(IWorkbenchSite site) {
	}

	/*
	 * (non-Javadoc) Method declared in ActionGroup
	 */
	@Override
	public void fillContextMenu(IMenuManager menu) {
		super.fillContextMenu(menu);

		ISelection selection = getContext().getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection) selection;
			if (sel.size() <= 1 && isNewTarget(sel.getFirstElement())) {

				Object selObj = sel.getFirstElement();

				if (selObj instanceof IJavaProject) {
					// top level project element is selected for New
					IMenuManager newMenu = new MenuManager(
							ActionMessages.NewWizardsActionGroup_new);
					menu
							.appendToGroup(IContextMenuConstants.GROUP_NEW,
									newMenu);
					newMenu.add(new NewProjectAction());
					newMenu.add(new Separator());

					IJavaProject jProject = (IJavaProject) selObj;
					try {
						if (TigerstripeProjectNature.hasNature(jProject
								.getProject())) {
							addContributedActions(newMenu);
						}
					} catch (CoreException e) {
						EclipsePlugin.log(e);
					}
					newMenu.add(new Separator());
					newMenu.add(new NewWizardAction());
				} else if (selObj instanceof IPackageFragmentRoot
						|| selObj instanceof IPackageFragment) {

					IProject iProject = ((IJavaElement) selObj)
							.getJavaProject().getProject();
					IMenuManager newMenu = new MenuManager(
							ActionMessages.NewWizardsActionGroup_new);

					menu
							.appendToGroup(IContextMenuConstants.GROUP_NEW,
									newMenu);

					// Top level package fragment root selected for new
					// If Tigerstripe project then offer all artifacts
					try {
						if (TigerstripeProjectNature.hasNature(iProject)) {
							// @since 1.2
							// All core artifacts are conditioned by the active
							// profile
							IWorkbenchProfile profile = TigerstripeCore
									.getWorkbenchProfileSession()
									.getActiveProfile();
							CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) profile
									.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);

							newMenu.add(new NewProjectAction());
							newMenu.add(new Separator());

							if (prop.getDetailsForType(
									IManagedEntityArtifact.class.getName())
									.isEnabled()) {
								newMenu
										.add(new OpenNewEntityArtifactWizardAction());
							}

							if (prop.getDetailsForType(
									IDatatypeArtifact.class.getName())
									.isEnabled()) {
								newMenu
										.add(new OpenNewDatatypeArtifactWizardAction());
							}
							if (prop.getDetailsForType(
									IEnumArtifact.class.getName()).isEnabled()) {
								newMenu
										.add(new OpenNewEnumArtifactWizardAction());
							}
							if (prop.getDetailsForType(
									IAssociationArtifact.class.getName())
									.isEnabled()) {
								newMenu
										.add(new OpenNewAssociationArtifactWizardAction());
							}
							if (prop.getDetailsForType(
									IDependencyArtifact.class.getName())
									.isEnabled()) {
								newMenu
										.add(new OpenNewDependencyArtifactWizardAction());
							}
							if (prop.getDetailsForType(
									IAssociationClassArtifact.class.getName())
									.isEnabled()) {
								newMenu
										.add(new OpenNewAssociationClassArtifactWizardAction());
							}
							if (prop.getDetailsForType(
									IQueryArtifact.class.getName()).isEnabled()) {
								newMenu
										.add(new OpenNewQueryArtifactWizardAction());
							}
							if (prop.getDetailsForType(
									IUpdateProcedureArtifact.class.getName())
									.isEnabled()) {
								newMenu
										.add(new OpenNewUpdateProcedureArtifactWizardAction());
							}
							if (prop.getDetailsForType(
									IEventArtifact.class.getName()).isEnabled()) {
								newMenu
										.add(new OpenNewNotificationArtifactWizardAction());
							}
							if (prop.getDetailsForType(
									ISessionArtifact.class.getName())
									.isEnabled()) {
								newMenu
										.add(new OpenNewSessionArtifactWizardAction());
							}
							if (prop.getDetailsForType(
									IExceptionArtifact.class.getName())
									.isEnabled()) {
								newMenu
										.add(new OpenNewExceptionArtifactWizardAction());
							}

							newMenu.add(new Separator());
							addContributedActions(newMenu);

							newMenu.add(new Separator());
							newMenu.add(new OpenNewPackageWizardAction());
							newMenu.add(new OpenBasicNewFolderWizardAction());
							newMenu.add(new OpenBasicNewFileWizardAction());
							newMenu.add(new Separator());
							newMenu.add(new NewWizardAction());
						} else if (TigerstripePluginProjectNature
								.hasNature(iProject)) {
							newMenu.add(new NewProjectAction());
							newMenu.add(new Separator());
							newMenu.add(new OpenNewPackageWizardAction());
							newMenu.add(new OpenNewClassWizardAction());
							newMenu.add(new OpenNewInterfaceWizardAction());
							newMenu.add(new OpenNewEnumerationWizardAction());
							newMenu.add(new OpenNewAnnotationWizardAction());
							newMenu.add(new OpenBasicNewFolderWizardAction());
							newMenu.add(new OpenBasicNewFileWizardAction());
							newMenu.add(new Separator());
							newMenu.add(new NewWizardAction());
						}
					} catch (CoreException e) {
						EclipsePlugin.log(e);
					}
				} else if (selObj instanceof IFolder) {
					IMenuManager newMenu = new MenuManager(
							ActionMessages.NewWizardsActionGroup_new);
					menu
							.appendToGroup(IContextMenuConstants.GROUP_NEW,
									newMenu);

					newMenu.add(new NewProjectAction());
					newMenu.add(new Separator());
					newMenu.add(new OpenBasicNewFolderWizardAction());
					newMenu.add(new OpenBasicNewFileWizardAction());
					newMenu.add(new Separator());
					addContributedActions(newMenu);
					newMenu.add(new Separator());
					newMenu.add(new NewWizardAction());
				}
			}
		}

	}

	private void addContributedActions(IMenuManager newMenu) {
		IExtension[] extensions = EclipsePlugin.getDefault().getDescriptor()
				.getExtensionPoint("explorerMenuContribution").getExtensions();

		for (IExtension extension : extensions) {
			IConfigurationElement[] configElements = extension
					.getConfigurationElements();
			for (IConfigurationElement configElement : configElements) {
				String actionClass = configElement.getAttribute("actionClass");
				try {
					IAction action = (IAction) configElement
							.createExecutableExtension("actionClass");
					newMenu.add(action);
				} catch (CoreException e) {
					EclipsePlugin.log(e);
				}
			}
		}
	}

	private Object findRoot(IPackageFragment frg) {
		if (frg.getParent() instanceof IPackageFragmentRoot)
			return frg.getParent();
		else if (frg.getParent() instanceof IPackageFragment)
			return findRoot((IPackageFragment) frg.getParent());
		else
			return null;
	}

	private boolean isNewTarget(Object element) {
		if (element == null)
			return true;
		if (element instanceof IResource)
			return true;

		if (element instanceof JarPackageFragmentRoot
				|| element instanceof JarPackageFragmentRoot)
			return false;

		if (element instanceof IPackageFragment) {
			Object root = findRoot((IPackageFragment) element);
			if (root instanceof JarPackageFragmentRoot)
				return false;
		}

		if (element instanceof IJavaElement) {
			int type = ((IJavaElement) element).getElementType();
			return type == IJavaElement.JAVA_PROJECT
					|| type == IJavaElement.PACKAGE_FRAGMENT_ROOT
					|| type == IJavaElement.PACKAGE_FRAGMENT
					|| type == IJavaElement.COMPILATION_UNIT
					|| type == IJavaElement.TYPE;
		}
		return false;
	}

}
