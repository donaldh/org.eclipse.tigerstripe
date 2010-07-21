package org.eclipse.tigerstripe.workbench.ui.internal.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.jdt.ui.actions.OpenProjectAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.OpenResourceAction;

/**
 * Introduced because of bugzilla 320048.  TS-specific action for opening closed projects in TS explorer.
 * 
 * @author N. Mehregani
 */
public class TSOpenProjectAction extends OpenProjectAction { 
	
	private OpenResourceAction fWorkbenchAction;

	/**
	 * Creates a new <code>TSOpenProjectAction</code>. The action requires
	 * that the selection provided by the site's selection provider is of type <code>
	 * org.eclipse.jface.viewers.IStructuredSelection</code>.
	 *
	 * @param site the site providing context information for this action
	 */
	public TSOpenProjectAction(IWorkbenchSite site) {
		super(site);
		fWorkbenchAction= new OpenResourceAction(site);
		setText(fWorkbenchAction.getText());
		setToolTipText(fWorkbenchAction.getToolTipText());
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJavaHelpContextIds.OPEN_PROJECT_ACTION);
		setEnabled(hasClosedProjectsInWorkspace());
	}
	
	public void run(IStructuredSelection selection) {
		ArrayList allClosedProjects= new ArrayList();
		if (evaluateSelection(selection, allClosedProjects)) {
			fWorkbenchAction.selectionChanged(new StructuredSelection(allClosedProjects));
			fWorkbenchAction.run();
		} else {
			super.run(selection);
		}
	}
	
	private boolean evaluateSelection(IStructuredSelection selection, List allClosedProjects) {
		Object[] array= selection.toArray();
		int selectionStatus = 0;
		boolean result = false;
		for (int i= 0; i < array.length; i++) {
			Object curr= array[i];
			if (isClosedProject(curr)) {
				if (allClosedProjects != null)
					allClosedProjects.add(curr);
				result = true;
			}
		}
		return result;
	}
	
	private boolean hasClosedProjectsInWorkspace() {
		IProject[] projects= ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (int i = 0; i < projects.length; i++) {
			if (!projects[i].isOpen())
				return true;
		}
		return false;
	}
	
	private static boolean isClosedProject(Object element) {
		return element instanceof IJavaProject && !((IJavaProject) element).isOpen();
	}	
}