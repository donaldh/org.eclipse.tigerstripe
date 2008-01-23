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
package org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.utils.TigerstripeProgressMonitor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.TSExplorerUtils;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Contains the logic to trigger refresh and rebuild after Refactor action
 * 
 * @author Eric Dillon
 * 
 */
public class RefactorActionHelper {

	private List<ArtifactUnitPair> selectedArtifacts = new ArrayList<ArtifactUnitPair>();

	private class ArtifactUnitPair {
		public IAbstractArtifact artifact;

		public ICompilationUnit unit;

		public ArtifactUnitPair(IAbstractArtifact artifact,
				ICompilationUnit unit) {
			this.artifact = artifact;
			this.unit = unit;
		}
	}

	public RefactorActionHelper(IStructuredSelection selection) {
		for (Iterator iter = selection.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof ICompilationUnit) {
				ICompilationUnit unit = (ICompilationUnit) obj;
				IAbstractArtifact artifact = TSExplorerUtils
						.getArtifactFor(unit);
				if (artifact != null) {
					selectedArtifacts.add(new ArtifactUnitPair(artifact, unit));
				}
			}
		}
	}

	public void refreshAsNeeded() {
		final List<ArtifactUnitPair> fSelectedArtifacts = selectedArtifacts;
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) {
				buildIfNeeded(fSelectedArtifacts, monitor);
			}
		};

		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		Shell shell = win != null ? win.getShell() : null;

		try {
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
			dialog.run(true, false, op);
		} catch (InterruptedException ee) {
			EclipsePlugin.log(ee);
		} catch (InvocationTargetException ee) {
			EclipsePlugin.log(ee.getTargetException());
		}

	}

	private void buildIfNeeded(List<ArtifactUnitPair> selectedArtifacts,
			IProgressMonitor monitor) {

		List<ITigerstripeProject> projectsToRefresh = new ArrayList<ITigerstripeProject>();

		// Figure out the list of projects to refresh
		for (ArtifactUnitPair pair : selectedArtifacts) {
			if (!pair.unit.exists()) {
				ITigerstripeProject prj = pair.artifact.getIProject();
				if (prj != null) {
					projectsToRefresh.add(prj);
				}
			}

		}

		// now see if there's anything left to refresh
		for (ITigerstripeProject project : projectsToRefresh) {
			try {
				if (project != null) {
					project.getArtifactManagerSession().refresh(true,
							new TigerstripeProgressMonitor(monitor));

					IJavaProject jProject = EclipsePlugin
							.getIJavaProject(project);
					final IProject iProject = jProject.getProject();

					new Job("Tigerstripe Project Audit") {
						@Override
						protected IStatus run(IProgressMonitor monitor) {
							try {
								iProject.build(
										IncrementalProjectBuilder.FULL_BUILD,
										TigerstripeProjectAuditor.BUILDER_ID,
										null, monitor);
							} catch (CoreException e) {
								EclipsePlugin.log(e);
							}
							return org.eclipse.core.runtime.Status.OK_STATUS;
						}
					}.schedule();
				}

			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

}
