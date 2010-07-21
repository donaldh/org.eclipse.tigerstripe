package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.IContextMenuConstants;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.actions.CloseResourceAction;
import org.eclipse.ui.actions.CloseUnrelatedProjectsAction;
import org.eclipse.ui.ide.IDEActionFactory;

/**
 * Introduced because of bugzilla 320048. TS-specific action group for opening
 * and closing projects in TS Explorer. This is pretty much a duplicate of JDT's
 * ProjectActionGroup. The only difference is that it returns a TS-specific
 * action for opening closed projects
 * 
 * @author N. Mehregani
 */
public class TSProjectActionGroup extends BaseActionProvider {

	private TSOpenProjectAction fOpenAction;
	private boolean fEnableOpenInContextMenu = true;
	private CloseResourceAction fCloseAction;
	private CloseResourceAction fCloseUnrelatedAction;

	private ISelectionChangedListener fSelectionChangedListener;

	@Override
	protected void doInit(IWorkbenchPartSite site) {
		ISelectionProvider provider = site.getSelectionProvider();
		ISelection selection = provider.getSelection();

		fCloseAction = new CloseResourceAction(site);
		fCloseAction
				.setActionDefinitionId(IWorkbenchCommandConstants.PROJECT_CLOSE_PROJECT);

		fCloseUnrelatedAction = new CloseUnrelatedProjectsAction(site);
		fCloseUnrelatedAction
				.setActionDefinitionId(IWorkbenchCommandConstants.PROJECT_CLOSE_UNRELATED_PROJECTS);

		fOpenAction = new TSOpenProjectAction(site);
		fOpenAction
				.setActionDefinitionId(IWorkbenchCommandConstants.PROJECT_OPEN_PROJECT);
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection s = (IStructuredSelection) selection;
			fOpenAction.selectionChanged(s);
			fCloseAction.selectionChanged(s);
			fCloseUnrelatedAction.selectionChanged(s);
		}

		fSelectionChangedListener = new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection s = event.getSelection();
				if (s instanceof IStructuredSelection) {
					performSelectionChanged((IStructuredSelection) s);
				}
			}
		};
		provider.addSelectionChangedListener(fSelectionChangedListener);

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(fOpenAction);
		workspace.addResourceChangeListener(fCloseAction);
		workspace.addResourceChangeListener(fCloseUnrelatedAction);
	}

	protected void performSelectionChanged(
			IStructuredSelection structuredSelection) {
		Object[] array = structuredSelection.toArray();
		ArrayList<IProject> openProjects = new ArrayList<IProject>();
		int selectionStatus = evaluateSelection(array, openProjects);
		StructuredSelection sel = new StructuredSelection(openProjects);

		fOpenAction
				.setEnabled((selectionStatus & CLOSED_PROJECTS_SELECTED) != 0
						|| (selectionStatus == 0 && hasClosedProjectsInWorkspace()));
		fEnableOpenInContextMenu = (selectionStatus & CLOSED_PROJECTS_SELECTED) != 0
				|| (selectionStatus == 0 && array.length == 0 && hasClosedProjectsInWorkspace());
		fCloseAction.selectionChanged(sel);
		fCloseUnrelatedAction.selectionChanged(sel);
	}

	private int CLOSED_PROJECTS_SELECTED = 1;
	private int NON_PROJECT_SELECTED = 2;

	private int evaluateSelection(Object[] array, List<IProject> allOpenProjects) {
		int status = 0;
		for (int i = 0; i < array.length; i++) {
			Object curr = array[i];
			if (curr instanceof IJavaProject) {
				curr = ((IJavaProject) curr).getProject();
			}
			if (curr instanceof IProject) {
				IProject project = (IProject) curr;
				if (project.isOpen()) {
					allOpenProjects.add(project);
				} else {
					status |= CLOSED_PROJECTS_SELECTED;
				}
			} else {
				if (curr instanceof IWorkingSet) {
					int res = evaluateSelection(((IWorkingSet) curr)
							.getElements(), allOpenProjects);
					status |= res;
				} else {
					status |= NON_PROJECT_SELECTED;
				}
			}
		}
		return status;
	}

	private boolean hasClosedProjectsInWorkspace() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		for (int i = 0; i < projects.length; i++) {
			if (!projects[i].isOpen())
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc) Method declared in ActionGroup
	 */
	public void fillActionBars(IActionBars actionBars) {
		super.fillActionBars(actionBars);
		actionBars.setGlobalActionHandler(IDEActionFactory.CLOSE_PROJECT
				.getId(), fCloseAction);
		actionBars.setGlobalActionHandler(
				IDEActionFactory.CLOSE_UNRELATED_PROJECTS.getId(),
				fCloseUnrelatedAction);
		actionBars.setGlobalActionHandler(
				IDEActionFactory.OPEN_PROJECT.getId(), fOpenAction);
	}

	/*
	 * (non-Javadoc) Method declared in ActionGroup
	 */
	public void fillContextMenu(IMenuManager menu) {
		super.fillContextMenu(menu);
		if (fOpenAction.isEnabled() && fEnableOpenInContextMenu)
			menu.appendToGroup(IContextMenuConstants.GROUP_BUILD, fOpenAction);
		if (fCloseAction.isEnabled())
			menu.appendToGroup(IContextMenuConstants.GROUP_BUILD, fCloseAction);
		if (fCloseUnrelatedAction.isEnabled()
				&& areOnlyProjectsSelected(fCloseUnrelatedAction
						.getStructuredSelection()))
			menu.appendToGroup(IContextMenuConstants.GROUP_BUILD,
					fCloseUnrelatedAction);
	}

	/**
	 * Returns the open project action contained in this project action group.
	 * 
	 * @return returns the open project action
	 * 
	 * @since 3.3
	 */
	public TSOpenProjectAction getOpenProjectAction() {
		return fOpenAction;
	}

	private boolean areOnlyProjectsSelected(IStructuredSelection selection) {
		if (selection.isEmpty())
			return false;

		Iterator<?> iter = selection.iterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof IAdaptable) {
				if (((IAdaptable) obj).getAdapter(IProject.class) == null)
					return false;
			}
		}
		return true;
	}

	/*
	 * @see ActionGroup#dispose()
	 */
	public void dispose() {
		getSelectionProvider().removeSelectionChangedListener(
				fSelectionChangedListener);

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.removeResourceChangeListener(fOpenAction);
		workspace.removeResourceChangeListener(fCloseAction);
		workspace.removeResourceChangeListener(fCloseUnrelatedAction);
		super.dispose();
	}
}
