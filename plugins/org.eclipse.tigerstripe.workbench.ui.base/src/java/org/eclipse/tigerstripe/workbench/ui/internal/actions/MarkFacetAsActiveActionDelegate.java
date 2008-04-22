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
package org.eclipse.tigerstripe.workbench.ui.internal.actions;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetPredicate;
import org.eclipse.tigerstripe.workbench.internal.contract.segment.FacetReference;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.SchedulingUtils;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * This action marks the selected facet as the active facet for the project.
 * 
 * @author Eric Dillon
 * 
 */
public class MarkFacetAsActiveActionDelegate implements IObjectActionDelegate {

	private ITigerstripeModelProject targetProject;

	private IProject iProject;

	private URI mySelectedURI;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub

	}

	public void run(IAction action) {

		final FacetReference ref = new FacetReference(mySelectedURI,
				targetProject);
		if (ref.canResolve()) {
			try {
				IContractSegment segment = ref.resolve();
				Job activateJob = new Job("Activating facet: "
						+ segment.getName()) {

					@Override
					protected IStatus run(final IProgressMonitor monitor) {

						// First make sure the project is built properly
						try {
							targetProject.getArtifactManagerSession().refresh(
									monitor);
						} catch (TigerstripeException e) {
							EclipsePlugin.log(e);
						}

						// compute the facet predicate while in the feedback
						// thread
						IFacetPredicate fPred = ref
								.computeFacetPredicate(monitor);
						Display.getDefault().syncExec(new Runnable() {

							public void run() {
								try {
									monitor.beginTask("Applying Facet",
											IProgressMonitor.UNKNOWN);
									targetProject
											.setActiveFacet(ref, monitor);
									monitor.done();
								} catch (TigerstripeException e) {
									IStatus status = new Status(
											IStatus.ERROR,
											TigerstripePluginConstants.PLUGIN_ID,
											222,
											"An exception was detected while trying to activate facet: "
													+ e.getMessage(), e);
									EclipsePlugin.log(status);
								}
							}

						});
						if (!fPred.isConsistent()) {
							MultiStatus status = new MultiStatus(
									TigerstripePluginConstants.PLUGIN_ID,
									222,
									"Inconsistent Facet: while resolving the facet scope, Inconsistencies were detected in the resulting model.",
									null);
							for (IStatus error : fPred
									.getInconsistencies().getChildren()) {
								status.add(error);
							}
							return status;
						}
						return Status.OK_STATUS;
					}

				};

				IProject iTargetProject = EclipsePlugin
						.getIProject(targetProject);
				if (iTargetProject != null) {
					List<IResource> projects = new ArrayList<IResource>();
					projects.add(iTargetProject);
					try {
						projects.addAll(Arrays.asList(iTargetProject
								.getReferencedProjects()));
					} catch (CoreException e) {
						EclipsePlugin.log(e);
					}
					activateJob.setRule(SchedulingUtils.multiRuleFor(projects));
				}
				activateJob.setUser(true);
				activateJob.schedule();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			// IWorkbench wb = PlatformUI.getWorkbench();
			// IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
			// final Shell shell = win != null ? win.getShell() : null;
			//
			// IRunnableWithProgress op = new IRunnableWithProgress() {
			// public void run(IProgressMonitor monitor) {
			// }
			// };
			//
			// try {
			// ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
			// dialog.run(false, false, op);
			// } catch (InterruptedException ee) {
			// EclipsePlugin.log(ee);
			// } catch (InvocationTargetException ee) {
			// EclipsePlugin.log(ee);
			// } catch (TigerstripeException ee) {
			// EclipsePlugin.log(ee);
			// }
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			checkEnabled(action, (IStructuredSelection) selection);
		} else {
			action.setEnabled(false);
		}
	}

	private void checkEnabled(IAction action, IStructuredSelection selection) {
		if (selection.getFirstElement() instanceof IFile) {
			IFile targetResource = (IFile) selection.getFirstElement();
			iProject = targetResource.getProject();
			IAbstractTigerstripeProject tsProject = EclipsePlugin
					.getITigerstripeProjectFor(iProject);
			if (tsProject instanceof ITigerstripeModelProject) {
				targetProject = (ITigerstripeModelProject) tsProject;
				if (IContractSegment.FILE_EXTENSION.equals(targetResource
						.getFileExtension())) {
					mySelectedURI = targetResource.getLocationURI();
					action.setEnabled(true);
					return;
				}
			}
		}
		action.setEnabled(false);
	}
}
