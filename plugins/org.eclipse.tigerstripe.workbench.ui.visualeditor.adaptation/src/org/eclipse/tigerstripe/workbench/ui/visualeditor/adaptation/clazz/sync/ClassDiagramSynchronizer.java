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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.render.editparts.RenderedDiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.editor.FileDiagramEditor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.model.IActiveFacetChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.internal.tools.compare.Comparer;
import org.eclipse.tigerstripe.workbench.internal.tools.compare.Difference;
import org.eclipse.tigerstripe.workbench.model.IMarkDirty;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.etadapter.ETAdapterFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.etadapter.MapETAdapter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.DiagramEditorHelper;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.progress.IProgressService;

/**
 * This class is responsible for synchronizing the content of a class diagram
 * with the IArtifacts that live in the corresponding Tigerstripe projects.
 * 
 * It basically has 2 main tasks: 
 * 1) making sure any change in the diagram is
 * propagated into the tigerstripe model. This is handled by creating EMF
 * Adapters on every EObject living in the EModel of a Diagram, and having them
 * issue IModelChangeRequests accordingly. 
 * 2) making sure any change in the
 * Tigerstripe model is propagated into the diagram.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class ClassDiagramSynchronizer implements IArtifactChangeListener,
		IActiveFacetChangeListener {

	private FileDiagramEditor editor;
	private Comparer comp = new Comparer();

	public ClassDiagramSynchronizer(FileDiagramEditor editor) {
		this.editor = editor;
	}

	protected void doInitialRefresh() {
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) {
				initialRefresh(monitor);
			}
		};

		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		Shell shell = win != null ? win.getShell() : null;

		try {
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
			dialog.run(true, false, op);
		} catch (InterruptedException e) {
			EclipsePlugin.log(e);
		} catch (InvocationTargetException e) {
			EclipsePlugin.log(e);
		}
	}

	public void startSynchronizing() {
		try {
			// Since bug 936 this is no needed anymore
			// doInitialRefresh();

			// Register for changes in the Diagrams
			registerModelAdapters();
			startListeningToArtifactMgr();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	/**
	 * This stores the appropriate ITigerstripeProject at the map level in a
	 * custom variable. This becomes then very handy to perform late look-up
	 * 
	 * For some reason we never thought of that before although it would have
	 * saved quite of few headaches...
	 * 
	 * @since 2.1
	 */
	public void initializeTSProjectInMap() {

		// make sure the project is up2date before we do that or else
		// icons may not be computable when GMF needs them because the
		// corresponding
		// project is still building
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			@Override
			public void execute(IProgressMonitor monitor) {
				// We need to make sure the ArtifactManager finished loading
				// before
				// we start refreshing, or else the artifacts wouldn't be found
				// and the
				// content
				// of the diagram would be deleted.
				try {
					monitor.beginTask("Building " + getTSProject().getName(),
							10);
					for (ITigerstripeModelProject project : getTSProject()
							.getReferencedProjects()) {
						monitor.subTask("Building referenced project: "
								+ project.getName());
						project.getArtifactManagerSession().refresh(monitor);
						monitor.worked(1);
					}

					getTSProject().getArtifactManagerSession().refresh(monitor);
					monitor.worked(2);

					monitor.done();
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		};
		IProgressService service = PlatformUI.getWorkbench()
				.getProgressService();

		try {
			service.run(true, false, op);
		} catch (InterruptedException e) {
			EclipsePlugin.log(e);
		} catch (InvocationTargetException e) {
			EclipsePlugin.log(e);
		}

		// all we've got to do is create the first adapter at the top level,
		// on the Map...
		Map map = (Map) editor.getDiagram().getElement();
		map.setCorrespondingITigerstripeProject(getTSProject());
	}

	public void stopSynchronizing() {
		try {

			// Don't bother un-registering if the project's been deleted
			if (getTSProject() == null)
				return;

			getTSProject().getArtifactManagerSession()
					.removeArtifactChangeListener(this);
			getTSProject().getArtifactManagerSession()
					.removeActiveFacetListener(this);

			for (ITigerstripeModelProject project : getTSProject()
					.getReferencedProjects()) {
				project.getArtifactManagerSession()
						.removeArtifactChangeListener(this);
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	protected ITigerstripeModelProject getTSProject() {
		IEditorPart editorPart = ((DiagramEditDomain) editor
				.getDiagramEditDomain()).getEditorPart();

		DiagramEditorHelper dHelper = new DiagramEditorHelper(editorPart);
		ITigerstripeModelProject diagramProject = dHelper
				.getCorrespondingTigerstripeProject();
		return diagramProject;
	}

	private void startListeningToArtifactMgr() throws TigerstripeException {
		getTSProject().getArtifactManagerSession().addArtifactChangeListener(
				this);
		getTSProject().getArtifactManagerSession().addActiveFacetListener(this);

		for (ITigerstripeModelProject project : getTSProject()
				.getReferencedProjects()) {
			project.getArtifactManagerSession().addArtifactChangeListener(this);
		}
	}

	/**
	 * Creates and registers the initial adapters for this Synchronizer.
	 * 
	 * Initially we walk the content of the EModel to create adapters (@see
	 * ETAdapter) for every single EObject in the model. These ETAdapters then
	 * know to keep creating additional ETAdapters as EObjects are created in
	 * the EModel
	 * 
	 */
	private void registerModelAdapters() {
		// all we've got to do is create the first adapter at the top level,
		// on the Map...
		Map map = (Map) editor.getDiagram().getElement();
		try {

			initializeBasePackage(map);

			MapETAdapter adapter = (MapETAdapter) ETAdapterFactory
					.makeETAdapterFor(map, getTSProject()
							.getArtifactManagerSession().getIModelUpdater(),
							this);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	/**
	 * The base package for the Map is the package in which artifacts are
	 * created
	 * 
	 * It is set to the default package for the project if the diagram is saved
	 * outside of any package in the src/ directory or is the given package
	 * otherwise.
	 * 
	 * @param map
	 */
	private void initializeBasePackage(final Map map) {

		TransactionalEditingDomain editingDomain = editor.getEditingDomain();
		IEditorPart editorPart = ((DiagramEditDomain) editor
				.getDiagramEditDomain()).getEditorPart();
		DiagramEditorHelper dHelper = new DiagramEditorHelper(editorPart);
		final String basePackage = dHelper.getInitialBasePackage();

		Command cmd = new AbstractCommand() {

			@Override
			public boolean canExecute() {
				return true;
			}

			public void execute() {

				if (map.getBasePackage() == null) {
					map.setBasePackage(basePackage);
				}
			}

			public void redo() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean canUndo() {
				return false;
			}
		};

		try {
			BaseETAdapter.setIgnoreNotify(true);
			editingDomain.getCommandStack().execute(cmd);
			editingDomain.getCommandStack().flush();
			IDiagramEditDomain editDomain = editor.getDiagramEditDomain();
			editDomain.getDiagramCommandStack().flush();
		} finally {
			BaseETAdapter.setIgnoreNotify(false);
		}
	}

	public void artifactAdded(IAbstractArtifact artifact) {
	}

	public void artifactChanged(IAbstractArtifact artifact, IAbstractArtifact oldArtifact) {
		ArrayList<Difference> diffs = comp.compareArtifacts(oldArtifact,artifact , true);
		if (diffs.size()>0){
		    handleArtifactChanged(artifact);
		}
	}
	
	private void handleArtifactChanged(IAbstractArtifact artifact) {
		TransactionalEditingDomain editingDomain = editor.getEditingDomain();
		IDiagramEditDomain diagramEditDomain = editor.getDiagramEditDomain();
		
		if (editor==null || editor.getDiagram()==null)
			return;
		
		final Map map = (Map) editor.getDiagram().getElement();
		final IAbstractArtifact fArtifact = artifact;
		try {
			ClassDiagramSynchronizerUtils.handleQualifiedNamedElementChanged(
					editor.getDiagram(), editor.getDiagramEditPart(), map,
					fArtifact, editingDomain, diagramEditDomain);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	public void artifactRemoved(IAbstractArtifact artifact) {
		if (artifact == null)
			return; // should never happen

		TransactionalEditingDomain editingDomain = editor.getEditingDomain();
		IDiagramEditDomain diagramEditDomain = editor.getDiagramEditDomain();
		final Map map = (Map) editor.getDiagram().getElement();

		try {
			ClassDiagramSynchronizerUtils.handleQualifiedNamedElementRemoved(
					map, artifact.getFullyQualifiedName(), editingDomain,
					diagramEditDomain);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	public void artifactRenamed(IAbstractArtifact artifact, String fromFQN) {
		if (artifact == null || fromFQN == null || fromFQN.length() == 0)
			return;

		TransactionalEditingDomain editingDomain = editor.getEditingDomain();
		IDiagramEditDomain diagramEditDomain = editor.getDiagramEditDomain();
		final Map map = (Map) editor.getDiagram().getElement();
		try {
			ClassDiagramSynchronizerUtils.handleQualifiedNamedElementRenamed(
					map, fromFQN, artifact.getFullyQualifiedName(),
					editingDomain, diagramEditDomain);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	public void managerReloaded() {
	}

	/**
	 * Performs an initial refresh of this diagram against the model upon open
	 * 
	 * 
	 */
	protected void initialRefresh(IProgressMonitor monitor) {
		//
		// Map map = (Map) editor.getDiagram().getElement();
		//
		// monitor.beginTask("Refreshing artifacts in Diagram",
		// map.getArtifacts()
		// .size());
		// List<String> forDeletion = new ArrayList<String>();
		// for (AbstractArtifact eArtifact : (List<AbstractArtifact>) map
		// .getArtifacts()) {
		// String fqn = eArtifact.getFullyQualifiedName();
		// try {
		// IArtifactManagerSession session = getTSProject()
		// .getArtifactManagerSession();
		// IAbstractArtifact iArtifact = session
		// .getArtifactByFullyQualifiedName(fqn);
		// if (iArtifact == null) {
		// forDeletion.add(fqn);
		// } else {
		// updateEArtifact(eArtifact, iArtifact);
		// }
		// } catch (TigerstripeException e) {
		// EclipsePlugin.log(e);
		// }
		// monitor.worked(1);
		// }
		// monitor.done();
		//
		// monitor.beginTask("Refreshing Associations in Diagram", map
		// .getAssociations().size());
		// for (Association eAssoc : (List<Association>) map.getAssociations())
		// {
		// String fqn = eAssoc.getFullyQualifiedName();
		// try {
		// IArtifactManagerSession session = getTSProject()
		// .getArtifactManagerSession();
		// IAbstractArtifact iArtifact = session
		// .getArtifactByFullyQualifiedName(fqn);
		// if (iArtifact == null) {
		// forDeletion.add(fqn);
		// } else {
		// updateEArtifact(eAssoc, iArtifact);
		// }
		// } catch (TigerstripeException e) {
		// EclipsePlugin.log(e);
		// }
		// monitor.worked(1);
		// }
		// monitor.done();
		//
		// monitor.beginTask("Refreshing Associations in Diagram", map
		// .getDependencies().size());
		// for (Dependency eDep : (List<Dependency>) map.getDependencies()) {
		// String fqn = eDep.getFullyQualifiedName();
		// try {
		// IArtifactManagerSession session = getTSProject()
		// .getArtifactManagerSession();
		// IAbstractArtifact iArtifact = session
		// .getArtifactByFullyQualifiedName(fqn);
		// if (iArtifact == null) {
		// forDeletion.add(fqn);
		// } else {
		// updateEArtifact(eDep, iArtifact);
		// }
		// } catch (TigerstripeException e) {
		// EclipsePlugin.log(e);
		// }
		// monitor.worked(1);
		// }
		// monitor.done();
		//
		// // Process the list of FQNs marked for deletion
		// monitor.beginTask("Removing deleted artifacts from Diagram",
		// forDeletion.size());
		// for (String fqn : forDeletion) {
		// internalHandleArtifactRemoved(fqn);
		// monitor.worked(1);
		// }
		// monitor.done();
	}

	private static Display getDisplay() {
		Display display = Display.getCurrent();
		// may be null if outside of UI thread
		if (display == null) {
			display = Display.getDefault();
		}
		return display;
	}

	public void facetChanged(IFacetReference oldFacet, IFacetReference newFacet) {
		// all we have to do is refresh the diagram so the icons are being
		// re-picked-up.
		getDisplay().asyncExec(new Runnable() {
			public void run() {
				IDiagramGraphicalViewer viewer = editor
						.getDiagramGraphicalViewer();
				RenderedDiagramRootEditPart rootPart = (RenderedDiagramRootEditPart) viewer
						.getRootEditPart();
				List<EditPart> partList = rootPart.getChildren();
				for (EditPart part : partList) {
					for (EditPart subPart : (List<EditPart>) part.getChildren()) {
						// these are the shapes and nodes in the diagram
						for (EditPart subsubPart : (List<EditPart>) subPart
								.getChildren()) {
							subsubPart.refresh();
						}
					}
				}
			}

		});
	}
}