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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceRuleFactory;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.builder.DiagramAuditorFactory;
import org.eclipse.tigerstripe.workbench.ui.internal.builder.IDiagramAuditor;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.synchronization.DiagramHandle;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class AuditDiagramsActionDelegate implements IObjectActionDelegate {

	private final static int ALL_DIAGRAMS = 0;

	private final static int THIS_DIAGRAM = 1;

	private final static int UNKNOWN = -1;

	private IProject targetProject;

	private IWorkbenchPart targetPart;

	private int auditType;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.targetPart = targetPart;
	}

	public void run(IAction action) {

		String jobName = "";
		if (auditType == ALL_DIAGRAMS) {
			jobName = "Diagrams Audit";
		} else {
			jobName = "Open Diagram Audit";
		}

		Job auditJob = new Job(jobName) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				IStatus status = Status.CANCEL_STATUS;
				switch (auditType) {
				case ALL_DIAGRAMS:
					status = auditDiagrams(monitor);
					break;
				case THIS_DIAGRAM:
					if (targetPart instanceof DiagramEditor) {
						status = auditDiagram((DiagramEditor) targetPart,
								monitor);
					}
					break;
				}

				final IStatus fStatus = status;
				if (status.isOK()) {
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							MessageDialog.openInformation(targetPart.getSite()
									.getShell(), fStatus.getMessage(),
									"No discrepancy was found.");
						}
					});
					return Status.OK_STATUS;
				} else if (status.getSeverity() != IStatus.CANCEL) {
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							MessageDialog
									.openError(targetPart.getSite().getShell(),
											fStatus.getMessage(),
											"Some discrepancies were found. (See Error Log for details)");
						}
					});
				}
				return status;
			}
		};

		auditJob.setRule(getSchedulingRule());
		auditJob.setUser(true);
		auditJob.schedule();
	}

	private IStatus auditDiagram(DiagramEditor editor, IProgressMonitor monitor) {
		IStatus subStatus = null;
		try {
			IDiagramAuditor auditor = DiagramAuditorFactory.make(editor);
			Diagram diagram = editor.getDiagram();
			subStatus = auditor.auditDiagram(diagram, monitor);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		MultiStatus status = new MultiStatus(EclipsePlugin.getPluginId(), 222,
				"Open Diagram audit result", null);
		status.add(subStatus);
		return status;
	}

	private ISchedulingRule getSchedulingRule() {
		IResourceRuleFactory ruleFactory = ResourcesPlugin.getWorkspace()
				.getRuleFactory();
		return ruleFactory.buildRule();
	}

	private final Set<IResource> getAllDiagrams() {
		Set<IResource> diagramFiles = new HashSet<IResource>();
		for (String fileExtension : DiagramAuditorFactory
				.getAuditableDiagramfileExtensions()) {
			List<IResource> rawList = TigerstripeProjectAuditor.findAll(
					targetProject, fileExtension);

			// We are ignoring the bin/... that are simply copied by the
			// Java
			// compiler
			for (IResource res : rawList) {
				Path binPath = new Path("bin");
				if (res.getProjectRelativePath().matchingFirstSegments(binPath) == 0)
					diagramFiles.add(res);
			}
		}
		return diagramFiles;
	}

	private final IStatus auditDiagrams(IProgressMonitor monitor) {
		Set<IResource> allDiagrams = getAllDiagrams();
		List<IStatus> statuses = new ArrayList<IStatus>();
		int inError = 0;
		monitor.beginTask("Auditing", allDiagrams.size());
		for (IResource diagramFile : allDiagrams) {
			try {
				DiagramHandle handle = new DiagramHandle(diagramFile);
				IDiagramAuditor auditor = DiagramAuditorFactory.make(handle);
				IStatus subStatus = auditor.auditDiagram(handle, monitor);
				if (!subStatus.isOK()) {
					statuses.add(subStatus);
					inError++;
				}
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			if (monitor.isCanceled())
				return Status.CANCEL_STATUS;
			monitor.worked(1);
		}
		monitor.done();

		if (statuses.size() == 0)
			return Status.OK_STATUS;

		MultiStatus status = new MultiStatus(EclipsePlugin.getPluginId(), 222,
				"Diagram audit result (" + targetProject.getName() + ": "
						+ inError + "/" + allDiagrams.size()
						+ " diagrams have errors)", null);
		for (IStatus st : statuses) {
			status.add(st);
		}
		return status;
	}

	public void selectionChanged(IAction action, ISelection selection) {
		targetProject = null;
		auditType = UNKNOWN;
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.getFirstElement() instanceof IResource) {
				IResource res = (IResource) ssel.getFirstElement();
				if ((res.getProject()
						.getAdapter(ITigerstripeModelProject.class)) instanceof ITigerstripeModelProject) {
					targetProject = res.getProject();
					auditType = ALL_DIAGRAMS;
					action.setText("Audit all diagrams");
				}
			} else if (ssel.getFirstElement() instanceof IJavaElement) {
				IJavaElement jElem = (IJavaElement) ssel.getFirstElement();
				try {
					IResource res = jElem.getCorrespondingResource();
					if (res != null)
						targetProject = res.getProject();
				} catch (JavaModelException e) {
					// This mean the underlying resource is not available i.e.
					// the project is closed.
					action.setEnabled(false);
					return;
				}
				if (targetProject != null
						&& (targetProject
								.getAdapter(ITigerstripeModelProject.class) instanceof ITigerstripeModelProject)) {
					auditType = ALL_DIAGRAMS;
					action.setText("Audit all diagrams");
				}
			} else if (ssel.getFirstElement() instanceof DiagramEditPart) {
				auditType = THIS_DIAGRAM;
				action.setText("Audit this diagram");
				// selection will use the targetPart
			}
			action.setEnabled(auditType != UNKNOWN);
		}
	}

}
