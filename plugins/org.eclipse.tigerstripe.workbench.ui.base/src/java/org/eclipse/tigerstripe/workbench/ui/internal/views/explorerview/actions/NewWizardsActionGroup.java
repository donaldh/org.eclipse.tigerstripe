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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Adapters;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.ui.actions.ActionMessages;
import org.eclipse.jdt.ui.IContextMenuConstants;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.PatternFactory;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripePluginProjectNature;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripeProjectNature;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.patterns.IArtifactPattern;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenBasicNewFileWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenBasicNewFolderWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewAnnotationWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewClassWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewEnumerationWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewInterfaceWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewPackageWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewPatternBasedArtifactWizardAction;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.NewProjectAction;
import org.eclipse.ui.actions.NewWizardAction;
import org.eclipse.ui.activities.WorkbenchActivityHelper;
import org.eclipse.ui.internal.actions.NewWizardShortcutAction;
import org.eclipse.ui.internal.navigator.wizards.CommonWizardDescriptor;
import org.eclipse.ui.internal.navigator.wizards.CommonWizardDescriptorManager;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.eclipse.ui.wizards.IWizardDescriptor;

/**
 * Action group for all new wizards within the Tigerstripe Explorer.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
@SuppressWarnings("restriction")
public class NewWizardsActionGroup extends BaseActionProvider {

	private INavigatorContentService contentService;

	@Override
	public void init(ICommonActionExtensionSite aSite) {
		super.init(aSite);
		contentService = aSite.getContentService();
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
			if (canEnable(sel)) {
				IWorkbenchWindow window = getSite().getWorkbenchWindow();
				IMenuManager newMenu = new MenuManager(
						ActionMessages.NewWizardsActionGroup_new);
				menu.appendToGroup(IContextMenuConstants.GROUP_NEW, newMenu);

				addNewWizards(window, newMenu);
				newMenu.add(new NewProjectAction());

				Object selObj = sel.getFirstElement();
				
				if (selObj==null)
					return;

				try {
					IResource resource = (IResource) Platform
							.getAdapterManager().getAdapter(selObj,
									IResource.class);
					IProject project = resource.getProject();

					if (TigerstripeProjectNature.hasNature(project)) {
						addArtifacts(newMenu);
					}

					addContributedActions(newMenu, window, selObj);

					if (selObj instanceof IPackageFragmentRoot
							|| selObj instanceof IPackageFragment
							|| selObj instanceof ICompilationUnit) {

						// Top level package fragment root selected for new
						// If Tigerstripe project then offer all artifacts
						if (TigerstripeProjectNature.hasNature(project)) {

							// @since 1.2
							// All core artifacts are conditioned by the active
							// profile
							IWorkbenchProfile profile = TigerstripeCore
									.getWorkbenchProfileSession()
									.getActiveProfile();
							CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) profile
									.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);

							newMenu.add(new Separator());
							// If Package Artifacts are enabled - we call the
							// new Package Artifact
							// dialog instead of the standard add package!
							// Leave it here for consistency (even though it
							// will be in the menu twice).
							// TODO Review in light of custom artifacts...
							if (prop.getDetailsForType(
									IPackageArtifact.class.getName())
									.isEnabled()) {
								// newMenu
								// .add(new
								// OpenNewPackageArtifactWizardAction());
							} else {
								newMenu.add(new OpenNewPackageWizardAction());
							}
							newMenu.add(new OpenBasicNewFolderWizardAction());
							newMenu.add(new OpenBasicNewFileWizardAction());
						} else if (TigerstripePluginProjectNature
								.hasNature(project)) {
							newMenu.add(new Separator());
							newMenu.add(new OpenNewPackageWizardAction());
							newMenu.add(new OpenNewClassWizardAction());
							newMenu.add(new OpenNewInterfaceWizardAction());
							newMenu.add(new OpenNewEnumerationWizardAction());
							newMenu.add(new OpenNewAnnotationWizardAction());
							newMenu.add(new OpenBasicNewFolderWizardAction());
							newMenu.add(new OpenBasicNewFileWizardAction());
						}
					} else if (selObj instanceof IFolder) {
						newMenu.add(new Separator());
						newMenu.add(new OpenBasicNewFolderWizardAction());
						newMenu.add(new OpenBasicNewFileWizardAction());
					}
					newMenu.add(new Separator());
					newMenu.add(new NewWizardAction(window));
				} catch (CoreException e) {
					EclipsePlugin.log(e);
				}

			}

		}

	}

	private void addArtifacts(IMenuManager newMenu) {
		newMenu.add(new Separator());
		for (String patternName : PatternFactory.getInstance()
				.getRegisteredPatterns().keySet()) {
			IPattern pattern = PatternFactory.getInstance().getPattern(
					patternName);
			if (pattern instanceof IArtifactPattern) {
				newMenu
						.add(new OpenNewPatternBasedArtifactWizardAction(
								pattern));
			}
		}
	}

	private void addNewWizards(IWorkbenchWindow window, IMenuManager newMenu) {
		IWorkbenchPage page = window.getActivePage();
		if (page != null) {
			String[] wizardIds = page.getNewWizardShortcuts();
			for (int i = 0; i < wizardIds.length; i++) {
				IAction action = getAction(window, wizardIds[i]);
				if (action != null) {
					if (!WorkbenchActivityHelper.filterItem(action)) {
						newMenu.add(new ActionContributionItem(action));
					}
				}
			}
		}
	}

	/*
	 * Returns the action for the given wizard id, or null if not found.
	 */
	private IAction getAction(IWorkbenchWindow window, String id) {
		// Keep a cache, rather than creating a new action each time,
		// so that image caching in ActionContributionItem works.
		IAction action = (IAction) actions.get(id);
		if (action == null) {
			IWizardDescriptor wizardDesc = PlatformUI.getWorkbench()
					.getNewWizardRegistry().findWizard(id);
			if (wizardDesc != null) {
				action = new NewWizardShortcutAction(window, wizardDesc);
				actions.put(id, action);
				IConfigurationElement element = (IConfigurationElement) Adapters
						.adapt(wizardDesc, IConfigurationElement.class);
				if (element != null) {
					window.getExtensionTracker().registerObject(
							element.getDeclaringExtension(), action,
							IExtensionTracker.REF_WEAK);
				}
			}
		}
		return action;
	}

	private void addContributedActions(IMenuManager newMenu,
			IWorkbenchWindow window, Object element) {
		Map<String, SortedSet<IAction>> groups = findGroups(window, element);
		for (Map.Entry<String, SortedSet<IAction>> entry : groups.entrySet()) {
			newMenu.add(new Separator(entry.getKey()));
			for (IAction action : entry.getValue()) {
				newMenu.add(action);
			}
		}
	}

	private Map<String, SortedSet<IAction>> findGroups(IWorkbenchWindow window,
			Object element) {
		CommonWizardDescriptor[] descriptors = CommonWizardDescriptorManager
				.getInstance().getEnabledCommonWizardDescriptors(element,
						"new", contentService);

		Map<String, SortedSet<IAction>> groups = new TreeMap<String, SortedSet<IAction>>();
		for (CommonWizardDescriptor descriptor : descriptors) {

			String menuGroupId = descriptor.getMenuGroupId();

			if (menuGroupId == null) {
				menuGroupId = CommonWizardDescriptor.DEFAULT_MENU_GROUP_ID;
			}

			if (!menuGroupId.startsWith("tigerstripe.")) {
				continue;
			}

			SortedSet<IAction> sortedWizards = groups.get(menuGroupId);
			if (sortedWizards == null) {
				groups.put(menuGroupId, sortedWizards = new TreeSet<IAction>(
						new ActionComparator()));
			}
			IAction action = getAction(window, descriptor.getWizardId());
			if (action != null) {
				sortedWizards.add(action);
			}
		}
		return groups;
	}

	private Object findRoot(IPackageFragment frg) {
		if (frg.getParent() instanceof IPackageFragmentRoot)
			return frg.getParent();
		else if (frg.getParent() instanceof IPackageFragment)
			return findRoot((IPackageFragment) frg.getParent());
		else
			return null;
	}

	private boolean canEnable(IStructuredSelection sel) {
		if (sel.size() == 0)
			return true;

		List<?> list = sel.toList();
		for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
			if (!isNewTarget(iterator.next()))
				return false;
		}

		return true;
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

	private final Map<String, IAction> actions = new HashMap<String, IAction>(
			32);

	private final class ActionComparator implements Comparator<IAction> {
		public int compare(IAction o1, IAction o2) {
			return ((IAction) o1).getText().compareTo(((IAction) o2).getText());
		}
	}
}
