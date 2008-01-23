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
package org.eclipse.tigerstripe.workbench.ui.eclipse.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.model.IActiveFacetChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactRenameRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeRemoveRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.ILabelCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.ILabelRemoveRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.ILabelSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodRemoveRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodSetRequest;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.utils.TigerstripeProgressMonitor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.TSExplorerUtils;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.TigerstripeExplorerPart;
import org.eclipse.tigerstripe.workbench.ui.gmf.synchronization.DiagramSynchronizationManager;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;

/**
 * This is the incremental auditor for a Tigerstripe Project.
 * 
 * @author Eric Dillon
 * 
 */
public class TigerstripeProjectAuditor extends IncrementalProjectBuilder
		implements IModelChangeListener, IActiveFacetChangeListener,
		IArtifactChangeListener {

	private boolean listeningToModelChanges = false;

	private boolean startupBuildDone = false;

	// This holds the pending audits posted thru callback from the Artifact
	// Mgr, to be processed when the auditor wakes up.
	private Set<String> pendingAudits = Collections
			.synchronizedSet(new HashSet<String>());

	public static final String BUILDER_ID = EclipsePlugin.getDefault()
			.getDescriptor().getUniqueIdentifier()
			+ ".tigerstripeProjectAuditor";

	// private ClasspathUpdater classpathUpdater;

	public TigerstripeProjectAuditor() {
		super();

	}

	// This callback is called whenever an artifact is added to the
	// Artifact Manager for this project. The given artifact's FQN is placed in
	// a list of pending audits that will be processed when the auditor wakes
	// up.
	// Note: because the auditor doesn't look at changes in the workspace
	// properly this is required to let it know which artifacts need to be
	// audited.
	public void artifactAdded(IAbstractArtifact artifact) {
		addPendingAudit(artifact);
	}

	// This callback is called whenever an artifact has changed in the
	// Artifact Manager for this project. The given artifact's FQN is placed in
	// a list of pending audits that will be processed when the auditor wakes
	// up.
	// Note: because the auditor doesn't look at changes in the workspace
	// properly this is required to let it know which artifacts need to be
	// audited.
	public void artifactChanged(final IAbstractArtifact artifact) {
		addPendingAudit(artifact);
	}

	public void artifactRemoved(IAbstractArtifact artifact) {
		removePendingAudit(artifact);
	}

	public void artifactRenamed(IAbstractArtifact artifact, String fromFQN) {
		addPendingAudit(artifact);
		// If the fromFQN had been added as a pending audit, it will be ignored
		// when processing the pending audit list
	}

	private void addPendingAudit(IAbstractArtifact artifact) {
		pendingAudits.add(artifact.getFullyQualifiedName());
	}

	private void removePendingAudit(IAbstractArtifact artifact) {
		pendingAudits.remove(artifact.getFullyQualifiedName());
	}

	private void clearPendingAudits() {
		pendingAudits.clear();
	}

	private boolean hasPendingAudits() {
		return !pendingAudits.isEmpty();
	}

	private void processPendingAudits(IProgressMonitor monitor)
			throws TigerstripeException {
		ITigerstripeProject tsProject = (ITigerstripeProject) TSExplorerUtils
				.getProjectHandleFor(getProject());
		IArtifactManagerSession session = tsProject.getArtifactManagerSession();
		monitor.beginTask("Processing Pending Audits", pendingAudits.size());
		for (Iterator<String> iter = pendingAudits.iterator(); iter.hasNext();) {
			String fqn = iter.next();
			IAbstractArtifact artifact = session
					.getArtifactByFullyQualifiedName(fqn);

			if (artifact == null) {
				// The artifact was either deleted or removed since it had been
				// added for audit
				TigerstripeRuntime
						.logDebugMessage("Artifact "
								+ fqn
								+ " won't be audited as it has disappeared. Ignoring from pending audits list.");
			} else {
				IArtifactAuditor auditor = ArtifactAuditorFactory.getInstance()
						.newArtifactAuditor(getProject(), artifact);
				// Remove any marker associated with this artifact
				deleteAuditMarkers(artifact);
				auditor.run(monitor);
			}
			monitor.worked(1);
		}
		clearPendingAudits();
		monitor.done();
	}

	// private void auditArtifact(final IAbstractArtifact artifact) {
	// try {
	// ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
	// public void run(IProgressMonitor monitor) throws CoreException {
	// try {
	// IArtifactAuditor auditor = ArtifactAuditorFactory
	// .getInstance().newArtifactAuditor(getProject(),
	// artifact);
	// // Remove any marker associated with this artifact
	// deleteAuditMarkers(artifact);
	// auditor.run(monitor);
	// } catch (TigerstripeException e) {
	// EclipsePlugin.log(e);
	// }
	// }
	// }, new NullProgressMonitor());
	// } catch (CoreException e) {
	// EclipsePlugin.log(e);
	// }
	// }

	public void managerReloaded() {
		// ignore.
	}

	/**
	 * This forces a refresh on the labels in the TS Explorer for the
	 * corresponding project upon active facet change
	 */
	public void facetChanged(IFacetReference oldFacet, IFacetReference newFacet) {
		IWorkbenchPage activePage = EclipsePlugin.getActivePage();

		if (activePage != null) {
			IViewPart tsExplorerPart = activePage
					.findView(TigerstripeExplorerPart.AEXPLORER_ID);
			if (tsExplorerPart instanceof TigerstripeExplorerPart) {
				final TigerstripeExplorerPart part = (TigerstripeExplorerPart) tsExplorerPart;
				final IJavaProject jProject = JavaCore.create(getProject());
				if (jProject != null) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							part.projectStateChanged(jProject);
						}
					});
				}
			}
		}
	}

	private boolean refreshArtifact(String fqn, IProgressMonitor monitor) {
		try {
			ITigerstripeProject proj = (ITigerstripeProject) EclipsePlugin
					.getITigerstripeProjectFor(getProject());
			IArtifactManagerSession session = proj.getArtifactManagerSession();
			IAbstractArtifact art = session.getArtifactByFullyQualifiedName(
					fqn, true);
			ITigerstripeProject targetProject = art.getIProject();
			if (targetProject != null) {
				IJavaProject jProject = EclipsePlugin
						.getIJavaProject(targetProject);
				String packageName = Util.packageOf(fqn);
				String name = Util.nameOf(fqn);
				IPackageFragment[] packFrags = jProject.getPackageFragments();
				for (IPackageFragment pack : packFrags) {
					if (pack.getElementName().equals(packageName)) {
						ICompilationUnit unit = pack.getCompilationUnit(name
								+ ".java");
						if (unit.getCorrespondingResource().exists()) {
							unit.getCorrespondingResource().refreshLocal(
									IResource.DEPTH_ONE, monitor);
							return true;
						}
					}
				}
			}
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return false;
	}

	private boolean refreshPackageFor(String packageName,
			IProgressMonitor monitor) {

		ITigerstripeProject tsProject = (ITigerstripeProject) TSExplorerUtils
				.getProjectHandleFor(getProject());
		if (tsProject instanceof TigerstripeProjectHandle) {
			TigerstripeProjectHandle handle = (TigerstripeProjectHandle) tsProject;
			try {
				String repoLoc = handle.getTSProject().getRepositoryLocation();
				IResource res = getProject().findMember(
						repoLoc + File.separator
								+ packageName.replace('.', File.separatorChar));
				if (res != null) {
					res.refreshLocal(IResource.DEPTH_ONE, monitor);
					return true;
				}
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
		}

		// try {
		// IJavaProject jProject = JavaCore.create(getProject());
		// IPackageFragment[] packFrags = jProject.getPackageFragments();
		// for (IPackageFragment pack : packFrags) {
		// if (pack.getElementName().equals(packageName)) {
		// pack.getCorrespondingResource().refreshLocal(
		// IResource.DEPTH_ONE, monitor);
		// return true;
		// }
		// }
		// } catch (CoreException e) {
		// EclipsePlugin.log(e);
		// }
		return false;
	}

	public void notifyModelChanged(final IModelChangeRequest executedRequest) {
		try {
			ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					boolean success = false;
					if (executedRequest instanceof IArtifactCreateRequest) {
						IArtifactCreateRequest req = (IArtifactCreateRequest) executedRequest;
						success = refreshPackageFor(req.getArtifactPackage(),
								monitor);
					} else if (executedRequest instanceof IArtifactRenameRequest) {
						IArtifactRenameRequest req = (IArtifactRenameRequest) executedRequest;
						success = refreshPackageFor(Util.packageOf(req
								.getArtifactFQN()), monitor);
					} else if (executedRequest instanceof IAttributeSetRequest) {
						IAttributeSetRequest req = (IAttributeSetRequest) executedRequest;
						success = refreshArtifact(req.getArtifactFQN(), monitor);
					} else if (executedRequest instanceof IAttributeCreateRequest) {
						IAttributeCreateRequest req = (IAttributeCreateRequest) executedRequest;
						success = refreshArtifact(req.getArtifactFQN(), monitor);
					} else if (executedRequest instanceof IAttributeRemoveRequest) {
						IAttributeRemoveRequest req = (IAttributeRemoveRequest) executedRequest;
						success = refreshArtifact(req.getArtifactFQN(), monitor);
					} else if (executedRequest instanceof IMethodCreateRequest) {
						IMethodCreateRequest req = (IMethodCreateRequest) executedRequest;
						success = refreshArtifact(req.getArtifactFQN(), monitor);
					} else if (executedRequest instanceof IMethodRemoveRequest) {
						IMethodRemoveRequest req = (IMethodRemoveRequest) executedRequest;
						success = refreshArtifact(req.getArtifactFQN(), monitor);
					} else if (executedRequest instanceof IMethodSetRequest) {
						IMethodSetRequest req = (IMethodSetRequest) executedRequest;
						success = refreshArtifact(req.getArtifactFQN(), monitor);
					} else if (executedRequest instanceof ILabelCreateRequest) {
						ILabelCreateRequest req = (ILabelCreateRequest) executedRequest;
						success = refreshArtifact(req.getArtifactFQN(), monitor);
					} else if (executedRequest instanceof ILabelSetRequest) {
						ILabelSetRequest req = (ILabelSetRequest) executedRequest;
						success = refreshArtifact(req.getArtifactFQN(), monitor);
					} else if (executedRequest instanceof ILabelRemoveRequest) {
						ILabelRemoveRequest req = (ILabelRemoveRequest) executedRequest;
						success = refreshArtifact(req.getArtifactFQN(), monitor);
					}
					if (!success)
						getProject().refreshLocal(IResource.DEPTH_INFINITE,
								new SubProgressMonitor(monitor, 1));
				}
			}, new NullProgressMonitor());
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		}
	}

	@Override
	protected void startupOnInitialize() {
		super.startupOnInitialize();
		if (!listeningToModelChanges) {
			registerForAllChanges();
		}
	}

	private void registerForAllChanges() {
		try {
			ITigerstripeProject proj = (ITigerstripeProject) EclipsePlugin
					.getITigerstripeProjectFor(getProject());
			proj.getArtifactManagerSession().getIModelUpdater()
					.addModelChangeListener(this);
			proj.getArtifactManagerSession().addActiveFacetListener(this);
			proj.getArtifactManagerSession().addArtifactChangeListener(this);

			// Bug 936: registering with the DiagramSynchronizationManager
			DiagramSynchronizationManager.getInstance().addTSProjectToWatch(
					proj);

			listeningToModelChanges = true;
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {

		if (!listeningToModelChanges) {
			registerForAllChanges();
		}

		// TigerstripeRuntime.logInfoMessage(" Building project=" +
		// getProject().getName() + "
		// " + this);

		if (hasPendingAudits()) {
			try {
				processPendingAudits(monitor);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}

		if (shouldAudit(kind)) {
			final int internalKind = kind;
			// ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
			// public void run(IProgressMonitor monitor) throws CoreException {
			auditProject(internalKind, monitor);
			// }
			// }, monitor);
		}

		// TigerstripeRuntime.logInfoMessage(" Done with project=" +
		// getProject().getName() + "
		// " + this);
		return null;
	}

	/**
	 * Run auditors that need to run on specific files
	 * 
	 */
	private void runAuditorsByFileExtensions(int kind, IProgressMonitor monitor) {

		// First the Profiles
		String extension = IWorkbenchProfile.FILE_EXTENSION;
		ProfileDescriptorAuditor auditor = new ProfileDescriptorAuditor(
				getProject());
		IResourceDelta delta = getDelta(getProject());
		List<IResource> wbps = null;

		// if ( delta.getKind() != IResourceDelta.NO_CHANGE ) {
		// wbps = findResourcesFromDelta(delta, extension);
		// } else if (kind == FULL_BUILD || kind == CLEAN_BUILD ) {
		wbps = findAll(getProject(), extension);
		// }
		if (checkCancel(monitor))
			return;

		if (wbps != null && wbps.size() != 0)
			auditor.run(wbps.toArray(new IResource[wbps.size()]), monitor);

		// Then the ContractSegments
		extension = IContractSegment.FILE_EXTENSION;
		ContractSegmentAuditor cAuditor = new ContractSegmentAuditor(
				getProject());
		List<IResource> wcss = null;
		wcss = findAll(getProject(), extension);

		if (checkCancel(monitor))
			return;

		if (wcss != null && wcss.size() != 0)
			cAuditor.run(wcss.toArray(new IResource[wcss.size()]), monitor);

	}

	public static void reportError(String msg, IResource srcFile, int violation) {
		reportProblem(msg, srcFile, violation, IMarker.SEVERITY_ERROR);
	}

	public static void reportWarning(String msg, IResource srcFile,
			int violation) {
		reportProblem(msg, srcFile, violation, IMarker.SEVERITY_WARNING);
	}

	public static void reportInfo(String msg, IResource srcFile, int violation) {
		reportProblem(msg, srcFile, violation, IMarker.SEVERITY_INFO);
	}

	private static void reportProblem(String msg, IResource srcFile,
			int violation, int severity) {

		if (srcFile == null)
			return;
		try {
			IMarker marker = srcFile
					.createMarker(TigerstripePluginConstants.MARKER_ID);
			marker.setAttribute(IMarker.MESSAGE, msg);
			marker.setAttribute(IMarker.SEVERITY, severity);
		} catch (CoreException e) {
			EclipsePlugin.log(e);
			return;
		}
	}

	private static Boolean turnedOffForImport = false;

	private static boolean isTurnedOffForImport() {
		synchronized (turnedOffForImport) {
			return turnedOffForImport;
		}
	}

	public static void setTurnedOffForImport(boolean turn) {
		synchronized (turnedOffForImport) {
			turnedOffForImport = turn;
		}
	}

	private boolean shouldAudit(int kind) {

		// Bug 949: making sure an initial build is always triggered at startup
		// of
		// builder.
		if (!startupBuildDone) {
			startupBuildDone = true;
			return true;
		}

		if (isTurnedOffForImport())
			return false;

		if (kind == FULL_BUILD)
			return true;

		if (kind == AUTO_BUILD) {

			IResourceDelta delta = getDelta(getProject());
			// Get the list of removed resources
			Collection<IResource> removedResources = new HashSet<IResource>();
			Collection<IResource> changedResources = new HashSet<IResource>();
			Collection<IResource> addedResources = new HashSet<IResource>();
			WorkspaceListener.buildResourcesLists(delta, removedResources,
					changedResources, addedResources);

			if (removedResources.size() != 0)
				return true;

			if (addedResources.size() != 0)
				return true;

			// Only trying to avoid full project build when an artifact
			// has changed since it would have been audited anyway through
			// the artifactChangeListener
			boolean javaOnly = true;
			for (IResource res : changedResources) {
				// When diagrams are saved no need to rebuild project
				if ("vwm".equals(res.getFileExtension())
						|| "wvd".equals(res.getFileExtension())
						|| "wod".equals(res.getFileExtension())
						|| "owm".equals(res.getFileExtension())) {
					continue;
				} else if (!"java".equals(res.getFileExtension())
						&& !"class".equals(res.getFileExtension())) {
					javaOnly = false;
					break;
				}
			}

			return !javaOnly;
		}
		if (getDelta(getProject()) == null)
			return false;

		// fow now, just build everything no matter what.
		return true;
	}

	public static List<IResource> findAll(IResource root, String extension) {
		List<IResource> result = new ArrayList<IResource>();

		if (extension == null || extension.length() == 0)
			return result;

		try {
			if (root instanceof IFile
					&& extension.equals(root.getFileExtension())) {
				result.add(root);
			} else if (root instanceof IContainer) {
				IContainer rootFolder = (IContainer) root;
				for (IResource res : rootFolder.members()) {
					result.addAll(findAll(res, extension));
				}
			}
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		}

		return result;
	}

	private void auditProject(int kind, IProgressMonitor monitor) {

		monitor.beginTask("Audit Tigerstripe Project", 9);

		ITigerstripeProject tsProject = (ITigerstripeProject) TSExplorerUtils
				.getProjectHandleFor(getProject());

		IArtifactManagerSession session = null;
		try {
			session = tsProject.getArtifactManagerSession();

			// this refresh will indirectly trigger an audit of every individual
			// artifact as they are parsed by the Art. Mgr. This is not
			// necessary since all artifacts will be audited below.
			session.setBroadcastMask(IArtifactChangeListener.NOTIFY_NONE);
			session.refreshAll(new TigerstripeProgressMonitor(monitor));
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		} finally {
			if (session != null) {
				try {
					session
							.setBroadcastMask(IArtifactChangeListener.NOTIFY_ALL);
				} catch (TigerstripeException e) {
					// ignore here
				}
			}
		}
		if (!deleteAuditMarkers(getProject()))
			return;

		runAuditorsByFileExtensions(kind, monitor);

		if (checkCancel(monitor))
			return;

		checkDescriptor(monitor);
		monitor.worked(1);
		if (checkCancel(monitor))
			return;

		checkArtifacts(monitor);
		if (checkCancel(monitor))
			return;

		monitor.done();
	}

	private void checkDescriptor(IProgressMonitor monitor) {
		DescriptorAuditor auditor = new DescriptorAuditor(getProject());
		auditor.run(monitor);
	}

	private void checkArtifacts(IProgressMonitor monitor) {
		ITigerstripeProject tsProject = (ITigerstripeProject) TSExplorerUtils
				.getProjectHandleFor(getProject());

		try {
			IArtifactManagerSession session = tsProject
					.getArtifactManagerSession();
			IArtifactQuery query = session.makeQuery(IQueryAllArtifacts.class
					.getName());
			query.setIncludeDependencies(false); // check only local
			// artifacts!
			Collection artifacts = session.queryArtifact(query);

			monitor.beginTask("Auditing Artifacts", artifacts.size());
			for (Iterator iter = artifacts.iterator(); iter.hasNext();) {
				IAbstractArtifact artifact = (IAbstractArtifact) iter.next();
				monitor.subTask(artifact.getFullyQualifiedName());
				IArtifactAuditor auditor = ArtifactAuditorFactory.getInstance()
						.newArtifactAuditor(getProject(), artifact);
				auditor.run(monitor);
				monitor.worked(1);
			}
			monitor.done();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	public boolean deleteAuditMarkers(IAbstractArtifact artifact) {

		AbstractArtifact aArt = (AbstractArtifact) artifact;
		try {
			String artPath = aArt.getArtifactPath();
			IResource res = getProject().findMember(artPath);
			if (res != null) {
				res.deleteMarkers(TigerstripePluginConstants.MARKER_ID, false,
						IResource.DEPTH_ONE);
				return true;
			}
			return true;
		} catch (TigerstripeException e) {
			return false;
		} catch (CoreException e) {
			// we can ignore here it means the resource doesn't exist yet, so no
			// need
			// to bother with removing markers.
			return false;
		}

		// try {
		// String fqn = artifact.getFullyQualifiedName();
		// IJavaProject jProject = JavaCore.create(getProject());
		// String packageName = Util.packageOf(fqn);
		// String name = Util.nameOf(fqn);
		// IPackageFragment[] packFrags = jProject.getPackageFragments();
		// for (IPackageFragment pack : packFrags) {
		// if (pack.getElementName().equals(packageName)) {
		// ICompilationUnit unit = pack.getCompilationUnit(name
		// + ".java");
		// if (unit.getCorrespondingResource().exists()) {
		// IResource res = unit.getCorrespondingResource();
		// res.deleteMarkers(TigerstripePluginConstants.MARKER_ID,
		// false, IResource.DEPTH_ONE);
		// return true;
		// }
		// }
		// }
		// return true;
		// } catch (CoreException e) {
		// // we can ignore here it means the resource doesn't exist yet, so no
		// // need
		// // to bother with removing markers.
		// return false;
		// }
	}

	public static boolean deleteAuditMarkers(IProject project) {
		try {
			project.deleteMarkers(TigerstripePluginConstants.MARKER_ID, false,
					IResource.DEPTH_INFINITE);
			return true;
		} catch (CoreException e) {
			EclipsePlugin.log(e);
			return false;
		}
	}

	private boolean checkCancel(IProgressMonitor monitor) {
		if (monitor.isCanceled())
			// TODO do we need to discard build state?
			throw new OperationCanceledException();

		// if (isInterrupted()) {
		// // TODO do we need to discard build state?
		// return true;
		// }
		//
		return false;
	}

	public static boolean hasTigerstripeProjectAuditor(IProject project) {

		if (!project.isAccessible())
			// closed
			return true;

		// Get the description
		IProjectDescription description;
		try {
			description = project.getDescription();
		} catch (CoreException e) {
			EclipsePlugin.log(e);
			return false;
		}

		// Look for Builder already associated
		ICommand[] cmds = description.getBuildSpec();
		for (int j = 0; j < cmds.length; j++) {
			if (cmds[j].getBuilderName().equals(BUILDER_ID))
				return true;
		}
		return false;
	}

	public static void addBuilderToProject(IProject project) {

		// Cannot modify closed projects
		if (!project.isOpen())
			return;

		// Get the description
		IProjectDescription description;
		try {
			description = project.getDescription();
		} catch (CoreException e) {
			EclipsePlugin.log(e);
			return;
		}

		// Look for Builder already associated
		ICommand[] cmds = description.getBuildSpec();
		for (int j = 0; j < cmds.length; j++) {
			if (cmds[j].getBuilderName().equals(BUILDER_ID))
				return;
		}

		// Associate Builder with Project
		ICommand newCmd = description.newCommand();
		newCmd.setBuilderName(BUILDER_ID);
		List newCmds = new ArrayList();
		newCmds.addAll(Arrays.asList(cmds));
		newCmds.add(newCmd);
		description.setBuildSpec((ICommand[]) newCmds
				.toArray(new ICommand[newCmds.size()]));
		try {
			project.setDescription(description, null);
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		}
	}

	public static void removeBuilderFromProject(IProject project) {

		// Cannot modify closed projects
		if (!project.isOpen())
			return;

		// Get the description
		IProjectDescription description = null;
		try {
			description = project.getDescription();
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		}

		// Look for Builder
		int index = -1;
		ICommand[] cmds = description.getBuildSpec();
		for (int j = 0; j < cmds.length; j++) {
			if (cmds[j].getBuilderName().equals(BUILDER_ID)) {
				index = j;
				break;
			}
		}
		if (index == -1)
			return;

		// Remove builder from project
		List newCmds = new ArrayList();
		newCmds.addAll(Arrays.asList(cmds));
		newCmds.remove(index);
		description.setBuildSpec((ICommand[]) newCmds
				.toArray(new ICommand[newCmds.size()]));

		try {
			project.setDescription(description, null);
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		}
	}

	public static IResource getIResourceForArtifact(IProject project,
			IAbstractArtifact artifact) {
		String fqn = artifact.getFullyQualifiedName();
		AbstractArtifact art = (AbstractArtifact) artifact;
		try {
			String realPath = art.getArtifactPath();
			return project.findMember(realPath);
		} catch (TigerstripeException e) {
			return null;
		}
	}

}
