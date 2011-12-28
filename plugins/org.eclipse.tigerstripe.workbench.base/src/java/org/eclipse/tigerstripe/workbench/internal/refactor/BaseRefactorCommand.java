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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringChangesListener;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.annotation.TigerstripeLazyObject;
import org.eclipse.tigerstripe.workbench.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.internal.core.util.ResourceUtils;
import org.eclipse.tigerstripe.workbench.internal.refactor.diagrams.DiagramChangeDelta;
import org.eclipse.tigerstripe.workbench.internal.refactor.diagrams.DiagramRefactorHelper;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.refactor.IRefactorCommand;
import org.eclipse.tigerstripe.workbench.refactor.ModelRefactorRequest;
import org.eclipse.tigerstripe.workbench.refactor.RefactorRequest;
import org.eclipse.tigerstripe.workbench.refactor.diagrams.DiagramSynchronizerController;

public class BaseRefactorCommand implements IRefactorCommand {

	private final RefactorRequest[] requests;
	private final List<ModelChangeDelta> deltas = new ArrayList<ModelChangeDelta>();
	private final List<DiagramChangeDelta> diagramDeltas = new ArrayList<DiagramChangeDelta>();
	private final List<ResourceChangeDelta> resourceDeltas = new ArrayList<ResourceChangeDelta>();

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
	public void execute(IProgressMonitor pm) throws TigerstripeException {
		final SubMonitor monitor = SubMonitor.convert(pm, 6);

		// long time = System.currentTimeMillis();
		// System.out.println("Starting refactor");
		disableAuditsAndDiagSync();

		Set<Object> toCleanUp = new HashSet<Object>();

		// First move all diagrams and resource. After that we can remove all
		// empty orphaned packages.
		moveDiagrams(monitor.newChild(1));
		moveResources(monitor.newChild(1));

		boolean crossProjectCmd = isCrossProjectCmd();

		ITigerstripeModelProject[] affectedProjects = applyAllDeltas(monitor.newChild(1),
				toCleanUp, crossProjectCmd);
		// long time1 = System.currentTimeMillis();
		// System.out.println("Done with Deltas: " + (time1 - time));

		updateDiagrams(monitor.newChild(1));
		
		if (crossProjectCmd) {
			// At this stage the artifacts have been refactored inside the
			// source project the refactored artifacts need to be moved over to
			// the new project now.
			Set<ITigerstripeModelProject> affectedProjects2 = new HashSet<ITigerstripeModelProject>();
			affectedProjects2.addAll(Arrays.asList(affectedProjects));
			affectedProjects2.addAll(Arrays
					.asList(moveAllArtifactsAcross(toCleanUp)));

			// Make sure the tgt project's index is rebuilt
			affectedProjects = affectedProjects2
					.toArray(new ITigerstripeModelProject[affectedProjects2
							.size()]);
		}

		cleanUp(toCleanUp, monitor.newChild(1));
		// time1 = System.currentTimeMillis();
		// System.out.println("Done clean up: " + (time1 - time));

		rebuildIndexes(affectedProjects, monitor.newChild(1));
		// time1 = System.currentTimeMillis();
		// System.out.println("Done index rebuild: " + (time1 - time));

		// Gets the list of all resources that will be impacted.
		// Set<IResource> affectedResources = getAffectedResources();
		// System.out.println(affectedResources);

		reEnableAuditsAndDiagSync();
		// System.out.println("Refactor Time: "
		// + (System.currentTimeMillis() - time));
	}

	protected void moveResources(IProgressMonitor monitor)
			throws TigerstripeException {
		monitor.beginTask("Moving Resources", resourceDeltas.size());
		for (ResourceChangeDelta delta : resourceDeltas) {
			try {
				IPath destPath = delta.getDestinationPath();
				IResource res = delta.getOriginalResource();
				IFolder f = ResourcesPlugin.getWorkspace().getRoot()
						.getFolder(destPath);
				ResourceUtils.createFolders(f, monitor);
				IPath newPath = destPath.append(res.getName());
				res.move(newPath, true, monitor);
			} catch (Exception e) {
				Status status = new Status(IStatus.ERROR, BasePlugin.PLUGIN_ID,
						"Error occured while trying to move Resource: "
								+ delta.getOriginalResource().getName(), e);
				BasePlugin.log(status);
			}
			monitor.worked(1);
		}
		monitor.done();
	}

	protected ITigerstripeModelProject[] moveAllArtifactsAcross(
			Set<Object> toCleanUp) {
		
		IAnnotationManager manager = AnnotationPlugin.getManager();
		
		Set<ITigerstripeModelProject> destProjects = new HashSet<ITigerstripeModelProject>();
		for (RefactorRequest req : requests) {
			if (req instanceof ModelRefactorRequest) {
				ModelRefactorRequest mRReq = (ModelRefactorRequest) req;
				if (mRReq.isCrossProjectCmd()) {
					try {
						IAbstractArtifact art = mRReq
								.getOriginalProject()
								.getArtifactManagerSession()
								.getArtifactByFullyQualifiedName(
										mRReq.getDestinationFQN());

						// propagate to annotations framework
						TigerstripeLazyObject oldPath = new TigerstripeLazyObject(
								art);
						manager.fireChanged(
								oldPath,
								new TigerstripeLazyObject(mRReq
										.getDestinationProject(), art
										.getFullyQualifiedName()),
								IRefactoringChangesListener.ABOUT_TO_CHANGE);

						IAbstractArtifact dest = ((IAbstractArtifactInternal) art)
								.makeWorkingCopy(null);
						mRReq.getDestinationProject()
								.getArtifactManagerSession().addArtifact(dest);
						dest.doSave(null);

						// propagate to annotations framework
						manager.fireChanged(oldPath,
								new TigerstripeLazyObject(dest),
								IRefactoringChangesListener.CHANGED);

						toCleanUp.add(art);
						destProjects.add(mRReq.getDestinationProject());
					} catch (TigerstripeException e) {
						BasePlugin.log(e);
					}
				}
			}
		}
		return destProjects.toArray(new ITigerstripeModelProject[destProjects
				.size()]);
	}

	protected void cleanUp(Collection<Object> toCleanUp,
			IProgressMonitor monitor) throws TigerstripeException {
		monitor.beginTask("Cleaning up", toCleanUp.size());
		for (Object obj : toCleanUp) {
			if (obj instanceof IPackageArtifact) {
				IResource res = (IResource) ((IPackageArtifact) obj)
						.getAdapter(IResource.class);

				// Also removing from Art Mgr
				IPackageArtifact art = (IPackageArtifact) obj;
				ITigerstripeModelProject proj = art.getProject();
				if (proj != null)
					proj.getArtifactManagerSession().removeArtifact(art);

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
			} else if (obj instanceof IAbstractArtifact) {
				IAbstractArtifact art = (IAbstractArtifact) obj;
				ITigerstripeModelProject proj = art.getProject();
				if (proj != null)
					proj.getArtifactManagerSession().removeArtifact(art);

				IResource res = (IResource) ((IAbstractArtifact) obj)
						.getAdapter(IResource.class);

				try {
					if (res != null)
						res.delete(true, monitor);
				} catch (CoreException e) {
					BasePlugin.log(e);
				}
			} else if (obj instanceof IResource) {
				try {
					((IResource) obj).delete(true, null);
				} catch (CoreException e) {
					BasePlugin.log(e);
				}
			} else if (obj instanceof IPath) {
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IResource file = root.findMember((IPath) obj);
				try {
					file.delete(true, null);
				} catch (CoreException e) {
					BasePlugin.log(e);
				}

			}
			monitor.worked(1);
		}
		monitor.done();
	}

	protected void rebuildIndexes(ITigerstripeModelProject[] projectsToRebuild,
			IProgressMonitor monitor) throws TigerstripeException {
		// Rebuilding indexes is necessary for the Auditor to be in sync with
		// the model since it was put to sleep during the refactor process.
		// TigerstripeProjectAuditor.rebuildIndexes(projectsToRebuild, monitor);
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

	/**
	 * Update the diagrams as part of a refactor operation
	 * 
	 * @param monitor
	 * @param isCrossProject
	 *            - whether the refactor is a cross-project move. special
	 *            handling on diagrams is required in this case
	 * @throws TigerstripeException
	 */
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

	protected ITigerstripeModelProject[] applyAllDeltas(
			IProgressMonitor monitor, Collection<Object> toCleanUp, boolean crossProjectCmd) {
		monitor.beginTask("Applying deltas", 2 * deltas.size());

		Set<ITigerstripeModelProject> affectedProjects = new HashSet<ITigerstripeModelProject>();
		Set<IAbstractArtifact> toSave = new HashSet<IAbstractArtifact>();

		// Update all the relationship ends.
		for (ModelChangeDelta delta : deltas) {
			try {
				if (delta.isRelationEndRefactor()
						|| delta.isComponentRefactor())
					delta.apply(toCleanUp, toSave);
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			}
			monitor.worked(1);
		}

		// move artifacts that need to be moved
		for (ModelChangeDelta delta : deltas) {
			try {
				if (!delta.isComponentRefactor()
						&& !delta.isRelationEndRefactor())
					delta.apply(toCleanUp, toSave);
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			}
			monitor.worked(1);
		}
		if (!crossProjectCmd) {
			for (IAbstractArtifact art : toSave) {
				try {
					art.doSave(monitor);

					if (art.getProject() != null)
						affectedProjects.add(art.getProject());
				} catch (TigerstripeException e) {
					BasePlugin.log(e);
				}
			}
		}
		monitor.done();

		return affectedProjects
				.toArray(new ITigerstripeModelProject[affectedProjects.size()]);

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
		if (deltas.size() > 0) {
			this.deltas.addAll(deltas);
			reorderDeltas();
		}
	}

	private void reorderDeltas() {
		// Pacakge artifact delta must process after package childs.
		Collections.sort(deltas, new Comparator<ModelChangeDelta>() {

			public int compare(ModelChangeDelta o1, ModelChangeDelta o2) {
				Object c1 = o1.getComponent();
				Object c2 = o2.getComponent();
				if (c1 == c2) {
					return 0;
				}
				return level(c2) - level(c1);
			}

			private int level(Object component) {
				if (!(component instanceof IModelComponent)) {
					return -1;
				}
				int level = 1;
				IModelComponent mc = (IModelComponent) component;
				while (mc != null) {
					level++;
					mc = mc.getContainingModelComponent();
				}
				return level;
			}
		});

	}

	public void addDiagramDeltas(Collection<DiagramChangeDelta> deltas) {
		this.diagramDeltas.addAll(deltas);
	}

	public void addResourceDeltas(List<ResourceChangeDelta> deltas) {
		resourceDeltas.addAll(deltas);
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

	/**
	 * Returns true if the command implies moving artifacts from one project to
	 * another.
	 * 
	 * @return
	 */
	public boolean isCrossProjectCmd() {
		for (RefactorRequest req : requests) {
			if (req instanceof ModelRefactorRequest) {
				ModelRefactorRequest mRReq = (ModelRefactorRequest) req;
				if (mRReq.isCrossProjectCmd())
					return true;
			}
		}
		return false;
	}

	public Collection<ResourceChangeDelta> getResourceDeltas() {
		return resourceDeltas;
	}
}
