/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.Activator;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.InstanceDiagramSynchronizerUtils;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class InstanceDiagramRefreshAction implements IObjectActionDelegate {

	private DiagramEditor editor;

	private IWorkbenchPart targetPart;

	private InstanceMap diagramMap;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.targetPart = targetPart;
	}

	public void run(IAction action) {

		String diagramName = "";
		if (targetPart instanceof DiagramEditor) {
			editor = (DiagramEditor) targetPart;
			if (editor.getEditorInput() instanceof IFileEditorInput) {
				IFileEditorInput input = (IFileEditorInput) editor
						.getEditorInput();
				diagramName = ": " + input.getFile().getName();
			}
		}

		Job diagramSyncUpJob = new Job("Diagram Sync-up" + diagramName) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				return doRefresh(monitor);
			}
		};

		diagramSyncUpJob.setUser(true);
		diagramSyncUpJob.schedule();
	}

	public void selectionChanged(IAction action, ISelection selection) {
		diagramMap = null;
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			Object obj = ssel.getFirstElement();
			if (obj instanceof DiagramEditPart) {
				DiagramEditPart part = (DiagramEditPart) obj;
				if (((Diagram) part.getModel()).getElement() instanceof InstanceMap) {
					diagramMap = (InstanceMap) ((Diagram) part.getModel())
							.getElement();
				}
			}
		}
		action.setEnabled(diagramMap != null);
	}

	private IStatus doRefresh(IProgressMonitor monitor) {
		MultiStatus result = new MultiStatus(Activator.PLUGIN_ID, 222,
				"Diagram sync-up", null);

		Set<String> deletedFQNs = new HashSet<String>();
		Map<String, IAbstractArtifact> toUpdateArtifacts = new HashMap<String, IAbstractArtifact>();

		monitor.beginTask("Synchronizing class instances", diagramMap
				.getClassInstances().size());
		Iterator<?> it = diagramMap.getClassInstances().iterator();
		while (it.hasNext()) {
			processInstance(deletedFQNs, toUpdateArtifacts,
					(Instance) it.next());
			monitor.worked(1);
		}
		monitor.done();

		monitor.beginTask("Synchronizing association instances", diagramMap
				.getAssociationInstances().size());
		it = diagramMap.getAssociationInstances().iterator();
		while (it.hasNext()) {
			processInstance(deletedFQNs, toUpdateArtifacts,
					(Instance) it.next());
			monitor.worked(1);
		}
		monitor.done();

		monitor.beginTask("Refreshing instances on diagram",
				toUpdateArtifacts.size());
		for (IAbstractArtifact iArtifact : toUpdateArtifacts.values()) {
			try {
				updateInstance(iArtifact);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			monitor.worked(1);
		}
		monitor.done();

		// Process the list of FQNs marked for deletion
		if (deletedFQNs.size() != 0) {
			final StringBuilder messageBuilder = new StringBuilder(
					"Some artifacts don't exist in the model anymore. Please fix the diagram manually.");
			for (String deletedFQN : deletedFQNs) {
				messageBuilder.append("\n  ");
				messageBuilder.append(deletedFQN);
			}
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					MessageDialog.openError(Display.getCurrent()
							.getActiveShell(), "Diagram Sync-up",
							messageBuilder.toString());
				}
			});
		}
		return result;
	}

	private void processInstance(Set<String> deletedFQNs,
			Map<String, IAbstractArtifact> toUpdateArtifacts, Instance instance) {
		try {
			String fqn = instance.getFullyQualifiedName();
			if (toUpdateArtifacts.containsKey(fqn) || deletedFQNs.contains(fqn)) {
				return;
			}

			IAbstractArtifact iArtifact = getArtifactByFQN(fqn);
			if (iArtifact == null) {
				deletedFQNs.add(fqn);
			} else {
				toUpdateArtifacts.put(fqn, iArtifact);
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private IAbstractArtifact getArtifactByFQN(String fqn)
			throws TigerstripeException {
		IArtifactManagerSession session = diagramMap
				.getCorrespondingITigerstripeProject()
				.getArtifactManagerSession();
		return session.getArtifactByFullyQualifiedName(fqn);
	}

	private void updateInstance(IAbstractArtifact iArtifact)
			throws TigerstripeException {
		if (editor == null) {
			throw new TigerstripeException("Editor is null");
		}
		TransactionalEditingDomain editingDomain = editor.getEditingDomain();
		IDiagramEditDomain diagramEditDomain = editor.getDiagramEditDomain();
		final InstanceMap map = (InstanceMap) editor.getDiagram().getElement();
		try {
			InstanceDiagramSynchronizerUtils.handleArtifactChanged(
					editor.getDiagram(), editor.getDiagramEditPart(), map,
					iArtifact, editingDomain, diagramEditDomain);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}
}
