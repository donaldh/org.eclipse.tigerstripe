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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh;

import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.Activator;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizerUtils;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * A best-effort action that tries to refresh a Class Diagram with the content
 * of the model.
 * 
 * @author Eric Dillon
 * 
 */
public class ClassDiagramRefreshAction implements IObjectActionDelegate {

	private DiagramEditor editor;

	private IWorkbenchPart targetPart;

	private Map diagramMap;

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

				try {
					BaseETAdapter.setIgnoreNotify(true);
					return doRefresh(monitor);
				} finally {
					BaseETAdapter.setIgnoreNotify(false);
				}
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
				if (((Diagram) part.getModel()).getElement() instanceof Map) {
					diagramMap = (Map) ((Diagram) part.getModel()).getElement();
				}
			}
		}
		action.setEnabled(diagramMap != null);
	}

	private IStatus doRefresh(IProgressMonitor monitor) {
		monitor.beginTask("Refreshing artifacts in Diagram", diagramMap
				.getArtifacts().size());

		MultiStatus result = new MultiStatus(Activator.PLUGIN_ID, 222,
				"Diagram sync-up", null);

		List<String> forDeletion = new ArrayList<String>();
		for (AbstractArtifact eArtifact : (List<AbstractArtifact>) diagramMap
				.getArtifacts()) {
			String fqn = eArtifact.getFullyQualifiedName();
			try {
				IArtifactManagerSession session = diagramMap
						.getCorrespondingITigerstripeProject()
						.getArtifactManagerSession();
				IAbstractArtifact iArtifact = session
						.getArtifactByFullyQualifiedName(fqn);
				if (iArtifact == null) {
					forDeletion.add(fqn);
				} else {
					updateEArtifact(eArtifact, iArtifact);
				}
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			monitor.worked(1);
		}
		monitor.done();

		monitor.beginTask("Refreshing Associations in Diagram", diagramMap
				.getAssociations().size());
		for (Association eAssoc : (List<Association>) diagramMap
				.getAssociations()) {
			String fqn = eAssoc.getFullyQualifiedName();
			try {
				IArtifactManagerSession session = diagramMap
						.getCorrespondingITigerstripeProject()
						.getArtifactManagerSession();
				IAbstractArtifact iArtifact = session
						.getArtifactByFullyQualifiedName(fqn);
				if (iArtifact == null) {
					forDeletion.add(fqn);
				} else {
					updateEArtifact(eAssoc, iArtifact);
				}
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			monitor.worked(1);
		}
		monitor.done();

		monitor.beginTask("Refreshing Associations in Diagram", diagramMap
				.getDependencies().size());
		for (Dependency eDep : (List<Dependency>) diagramMap.getDependencies()) {
			String fqn = eDep.getFullyQualifiedName();
			try {
				IArtifactManagerSession session = diagramMap
						.getCorrespondingITigerstripeProject()
						.getArtifactManagerSession();
				IAbstractArtifact iArtifact = session
						.getArtifactByFullyQualifiedName(fqn);
				if (iArtifact == null) {
					forDeletion.add(fqn);
				} else {
					updateEArtifact(eDep, iArtifact);
				}
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			monitor.worked(1);
		}
		monitor.done();

		// Process the list of FQNs marked for deletion
		if (forDeletion.size() != 0) {
			String m = "The following artifacts don't exist in the model anymore.\nPlease fix the diagram manually.";
			for (String f : forDeletion) {
				m += "  " + f + "\n";
			}
			final String message = m;
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					MessageDialog.openError(Display.getCurrent()
							.getActiveShell(), "Diagram Sync-up", message);
				}
			});
		}

		return result;
	}

	private void updateEArtifact(QualifiedNamedElement eArtifact,
			IAbstractArtifact iArtifact) throws TigerstripeException {
		if (editor == null)
			throw new TigerstripeException("Editor is null");
		TransactionalEditingDomain editingDomain = editor.getEditingDomain();
		IDiagramEditDomain diagramEditDomain = editor.getDiagramEditDomain();
		final Map map = (Map) editor.getDiagram().getElement();
		final IAbstractArtifact fArtifact = iArtifact;
		try {
			ClassDiagramSynchronizerUtils.handleQualifiedNamedElementChanged(
					editor.getDiagram(), editor.getDiagramEditPart(), map,
					fArtifact, editingDomain, diagramEditDomain);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

	}

}
