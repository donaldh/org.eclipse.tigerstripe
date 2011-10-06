package org.eclipse.tigerstripe.workbench.ui.internal.actions;

import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * NM US7583
 * Main action responsible for launching model comparator in Tigerstripe
 * 
 * TODO: Incomplete.
 * 
 * @author Navid Mehregani
 * 
 * @since 2.0
 */
public class ModelCompareAction implements IObjectActionDelegate{

	private ITigerstripeModelProject fFirstProject = null;
	private ITigerstripeModelProject fSecondProject = null;
	
	public void run(IAction action) {
		if (fFirstProject==null || fSecondProject==null) { 
			MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Projects are not selected properly", "Two Tigerstripe projects need to be selected before running this action");
			return;
		}
		
		IProject iProjectOne = (IProject) fFirstProject.getAdapter(IProject.class);
		IProject iProjectTwo = (IProject) fSecondProject.getAdapter(IProject.class);
		
		String firstVersion = null;
		String secondVersion = null;
		
		try {
			firstVersion = fFirstProject.getProjectDetails().getVersion();
			secondVersion = fSecondProject.getProjectDetails().getVersion();
		} catch (TigerstripeException e) {
			// Ignore.  Let user pick more up-to-date project
		}
		
		VersionCompareResult result = VersionCompareResult.SAME;
		
		if (firstVersion!=null && secondVersion!=null) 
			result = compareVersion(firstVersion, secondVersion);
		
		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		
		MoreUpToDateDialog moreUpToDateDialog = new MoreUpToDateDialog(shell, fFirstProject, fSecondProject, result);
		if (moreUpToDateDialog.open() == Window.OK) {
			ITigerstripeModelProject selectedProject = moreUpToDateDialog.getProjectSelected(); // TODO: This is not working as expected.
			if (selectedProject.equals(fFirstProject)) 
				System.out.println("First");
			else
				System.out.println("Second");
		}
		
		
		
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = ((IStructuredSelection) selection);
			if (structuredSelection.size()==2) {
				Iterator<?> selectionIterator = structuredSelection.iterator();
				
				Object firstProject = selectionIterator.next();
				Object secondProject = selectionIterator.next();
				
				if (firstProject instanceof ITigerstripeModelProject)
					fFirstProject = (ITigerstripeModelProject)firstProject;
				if (secondProject instanceof ITigerstripeModelProject) 
					fSecondProject = (ITigerstripeModelProject)secondProject;
			}
			
			
		}
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
		
	}
	
	public enum VersionCompareResult {
		FIRST, SAME, SECOND, UNKNOWN;
	}
	
	/**
	 * Version has to be in the following format: x.x.x.   Where x is an int. 
	 * 
	 * @param first - First version to compare
	 * @param second - Second version to compare
	 * @return FIRST if first version is higher, SECOND if second is higher, SAME if they're
	 * both the same, and UNKNOWN if it cannot be determined.
	 */
	private VersionCompareResult compareVersion(String first, String second) {
		if (first==null || second==null) 
			return VersionCompareResult.UNKNOWN;
		
		try {
			
			VersionFormat firstFormat = new VersionFormat(first);
			VersionFormat secondFormat = new VersionFormat(second);

			// Compare max
			int first_max = firstFormat.getMax();
			int second_max = secondFormat.getMax();
			if (first_max > second_max) 
				return VersionCompareResult.FIRST;
			else if (first_max < second_max)
				return VersionCompareResult.SECOND;
			
			// Compare min
			int first_min = firstFormat.getMin();
			int second_min = secondFormat.getMin();
			if (first_min > second_min) 
				return VersionCompareResult.FIRST;
			else if (first_min < second_min)
				return VersionCompareResult.SECOND;
			
			// Compare Qualifier
			int first_qualifier = firstFormat.getQualifier();
			int second_qualifier = secondFormat.getQualifier();
			if (first_qualifier > second_qualifier) 
				return VersionCompareResult.FIRST;
			else if (first_qualifier < second_qualifier) 
				return VersionCompareResult.SECOND;
			
		} catch (Exception e) {
			
		}
		
		return VersionCompareResult.UNKNOWN;
	}
	
	private class VersionFormat {
		
		private String fVersion = null;
		
		/**
		 * Expects format to be 
		 * x, x.x, or x.x.x (where x is an integer)
		 *
		 * @param version - Cannot be null
		 */
		public VersionFormat(String version) {
			fVersion = version;
		}
		
		public int getMax() throws NumberFormatException, IndexOutOfBoundsException {
			int indexOfDot = fVersion.indexOf(".");
			if (indexOfDot==-1)
				return Integer.parseInt(fVersion);
			
			return Integer.parseInt(fVersion.substring(0, indexOfDot));
		}
		
		public int getMin() throws NumberFormatException,IndexOutOfBoundsException {
			
			String min = getStringAfterFirstDot(fVersion);
			if (min==null)
				return 0;
			
			int indexOfDot = min.indexOf(".");
			if (indexOfDot==-1)
				return Integer.parseInt(min);
			
			return Integer.parseInt(min.substring(0, indexOfDot));
		}
		
		public int getQualifier() throws NumberFormatException,IndexOutOfBoundsException {
			String qualifier = getStringAfterFirstDot(fVersion);
			if (qualifier==null)
				return 0;
			
			qualifier = getStringAfterFirstDot(qualifier);
			if (qualifier==null)
				return 0;
			
			int indexOfDot = qualifier.indexOf(".");
			if (indexOfDot==-1)
				return Integer.parseInt(qualifier);
			
			return Integer.parseInt(qualifier.substring(0, indexOfDot));
			
		}
		
		private String getStringAfterFirstDot(String version) {
			int indexOfFirstDot = version.indexOf(".");
			if (indexOfFirstDot==-1)
				return null;
			
			return version.substring(indexOfFirstDot + 1);
		}
	}

}
