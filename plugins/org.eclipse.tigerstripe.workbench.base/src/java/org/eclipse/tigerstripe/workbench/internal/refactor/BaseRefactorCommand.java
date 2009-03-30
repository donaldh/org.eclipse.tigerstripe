/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.refactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.annotation.TigerstripeRefactoringSupport;
import org.eclipse.tigerstripe.workbench.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.internal.refactor.diagrams.DiagramChangeDelta;
import org.eclipse.tigerstripe.workbench.internal.refactor.diagrams.DiagramRefactorHelper;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.refactor.IRefactorCommand;
import org.eclipse.tigerstripe.workbench.refactor.RefactorRequest;
import org.eclipse.tigerstripe.workbench.refactor.diagrams.DiagramSynchronizerController;

public class BaseRefactorCommand implements IRefactorCommand {

	private RefactorRequest[] requests;
	private List<ModelChangeDelta> deltas = new ArrayList<ModelChangeDelta>();
	private List<DiagramChangeDelta> diagramDeltas = new ArrayList<DiagramChangeDelta>();

	public BaseRefactorCommand(RefactorRequest[] requests) {
		this.requests = requests;
	}

	/**
	 * Executing a refactor command means applying the list of
	 * {@link IModelChangeDelta} that has been assembled in a locked-up
	 * workspace state.
	 * 
	 * There are 4 kinds of changes that may happen 1.- Artifacts being renamed.
	 * This requires a "rename of the POJO". 2.- Content of artifact being
	 * changed, i.e. in artifact that had a reference to changed FQN. 3.-
	 * Diagram content being changed, i.e. a artifact on the diagram had a
	 * changed FQN 4.- Diagram being moved, i.e. the diagram was in a package
	 * that was renamed.
	 * 
	 * The changes are applied in the following order:
	 * <ul>
	 * <li>Prior to anything else, we disable audits and diagram
	 * synchronization.</li>
	 * <li>(2&1) - we update all contents of artifacts first. This will lead to
	 * the creation of "Proxy artifacts" that will be resolved when the
	 * references are traversed later.</li>
	 * <li>(3) - we update the content of the diagrams with the new FQNs</li>
	 * <li>(4) - we move the diagrams to their new location</li>
	 * <li>We delete the old packages/directories/diagrams as needed.</li>
	 * <li>We re-index changed diagrams.</li>
	 * <li>We trigger a manual audit to re-index the POJOs and make sure the
	 * smart auditing is in sync after all changes.</li>
	 * <li>We re-enable audits and diagram synchronization</li>
	 * </ul>
	 * 
	 * With a bit of luck, all is good at this point.
	 * 
	 * @param monitor
	 *            -
	 */
	public void execute(IProgressMonitor monitor) throws TigerstripeException {
		if (monitor == null)
			monitor = new NullProgressMonitor();

		disableAuditsAndDiagSync();
		disableAnnotationsSync();

		Set<Object> toCleanUp = new HashSet<Object>();

		applyAllDeltas(monitor, toCleanUp);
		updateDiagrams(monitor);
		moveDiagrams(monitor);

		cleanUp(toCleanUp, monitor);

		rebuildIndexes(monitor);

		// Gets the list of all resources that will be impacted.
		// Set<IResource> affectedResources = getAffectedResources();
		// System.out.println(affectedResources);

		enableAnnotationsSync();
		reEnableAuditsAndDiagSync();
	}

	protected void disableAnnotationsSync() {
		TigerstripeRefactoringSupport.INSTANCE.stop();
	}

	protected void enableAnnotationsSync() {
		TigerstripeRefactoringSupport.INSTANCE.start();
	}

	protected void cleanUp(Collection<Object> toCleanUp,
			IProgressMonitor monitor) throws TigerstripeException {
		monitor.beginTask("Cleaning up", toCleanUp.size());
		for (Object obj : toCleanUp) {
			if (obj instanceof IPackageArtifact) {
				IResource res = (IResource) ((IPackageArtifact) obj)
						.getAdapter(IResource.class);
				IFolder f = null;
				if (res instanceof IFolder) {
					f = (IFolder) res;
				} else {
					if (res != null)
						f = (IFolder) res.getParent();
				}
				try {
					if (f != null)
						f.delete(true, monitor);
				} catch (CoreException e) {
					BasePlugin.log(e);
				}
			} else if (obj instanceof IResource) {
				try {
					((IResource) obj).delete(true, null);
				} catch (CoreException e) {
					BasePlugin.log(e);
				}
			}
			monitor.worked(1);
		}
		monitor.done();
	}

	protected void rebuildIndexes(IProgressMonitor monitor)
			throws TigerstripeException {
		// Rebuilding indexes is necessary for the Auditor to be in sync with
		// the model since it was put to sleep during the refactor process.
		TigerstripeProjectAuditor.rebuildIndexes(monitor);
	}

	protected void moveDiagrams(IProgressMonitor monitor)
			throws TigerstripeException {
		monitor.beginTask("Moving Diagrams", diagramDeltas.size());
		for (DiagramChangeDelta diagramDelta : diagramDeltas) {
			try {
				DiagramRefactorHelper.applyDelta(diagramDelta, monitor);
			} catch (Exception e) {
				Status status = new Status(IStatus.ERROR, BasePlugin.PLUGIN_ID,
						"Error occured while trying to move Diagram: "
								+ diagramDelta.getAffDiagramHandle()
										.getDiagramResource().getName(), e);
				BasePlugin.log(status);
			}
			monitor.worked(1);
		}
		monitor.done();
	}

	protected void updateDiagrams(IProgressMonitor monitor)
			throws TigerstripeException {
		// While we applied all changes in the model, the change requests on the
		// diagrams have been queued up.
		// All we need to do here is flush out all the requests so they get
		// applied.
		DiagramSynchronizerController.getController()
				.flushSynchronizationRequests(true, monitor);
	}

	protected void disableAuditsAndDiagSync() {
		// avoid any building
		TigerstripeProjectAuditor.setTurnedOffForImport(true);

		// Hold synchronization and flush anything that was in the quere before
		// we do anything else.
		DiagramSynchronizerController.getController().holdSynchronization();
		DiagramSynchronizerController.getController()
				.flushSynchronizationRequests(true, null);
	}

	protected void reEnableAuditsAndDiagSync() {
		// At this stage, all diagram requests will have been flushed and
		// applied.
		// We simply need to restart the batch processing
		DiagramSynchronizerController.getController().restartSynchronization();

		TigerstripeProjectAuditor.setTurnedOffForImport(false);
	}

	protected void applyAllDeltas(IProgressMonitor monitor,
			Collection<Object> toCleanUp) {
		monitor.beginTask("Applying deltas", deltas.size());

		// We need to apply deltas in 2 passes
		// First pass for all non-renaming deltas. This ensures we take care of
		// the
		// inside of all artifact changes first.
		for (ModelChangeDelta delta : deltas) {
			try {
				if (delta.isComponentRefactor())
					delta.apply(toCleanUp);
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			}
			monitor.worked(1);
		}

		// then a second pass that will actually move artifacts that need to
		// be moved
		for (ModelChangeDelta delta : deltas) {
			try {
				if (!delta.isComponentRefactor())
					delta.apply(toCleanUp);
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			}
			monitor.worked(1);
		}

		monitor.done();
	}

	public RefactorRequest[] getRequests() {
		return requests;
	}

	public Collection<DiagramChangeDelta> getDiagramDeltas() {
		return diagramDeltas;
	}

	public Collection<ModelChangeDelta> getDeltas() {
		return deltas;
	}

	public void addDeltas(Collection<ModelChangeDelta> deltas) {
		this.deltas.addAll(deltas);
	}

	public void addDiagramDeltas(Collection<DiagramChangeDelta> deltas) {
		this.diagramDeltas.addAll(deltas);
	}

	/**
	 * Traverses the list of IModelChangeDeltas to return a list of all affected
	 * resources.
	 * 
	 * @return
	 */
	protected Set<IResource> getAffectedResources() {
		Set<IResource> resources = new HashSet<IResource>();
		for (IModelChangeDelta delta : deltas) {
			IResource res = delta.getAffectedResource();
			if (res != null)
				resources.add(res);
		}
		return resources;
	}
}
