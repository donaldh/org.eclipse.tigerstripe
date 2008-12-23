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
package org.eclipse.tigerstripe.workbench.internal.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeResourceAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.internal.builder.WorkspaceHelper.IResourceFilter;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.IProjectDependencyChangeListener;
import org.eclipse.tigerstripe.workbench.project.IProjectDependencyDelta;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;

/**
 * This is the incremental auditor for a Tigerstripe Project.
 * 
 * @author Eric Dillon
 * 
 */
public class TigerstripeProjectAuditor extends IncrementalProjectBuilder
		implements IProjectDependencyChangeListener, IArtifactChangeListener {

	private ModelAuditorHelper modelAuditorHelper = null;

	private boolean fullBuildRequired = false;

	public static final String BUILDER_ID = BuilderConstants.PROJECT_BUILDER_ID;

	public TigerstripeProjectAuditor() {
		super();
	}

	@Override
	protected void startupOnInitialize() {
		super.startupOnInitialize();
		ITigerstripeModelProject tsProject = (ITigerstripeModelProject) getProject()
				.getAdapter(ITigerstripeModelProject.class);
		tsProject.addProjectDependencyChangeListener(this);
	}

	// ===================================================================
	// ===================================================================
	// force a full build the next build around...
	public void projectDependenciesChanged(IProjectDependencyDelta delta) {
		fullBuildRequired = true;
	}

	// ===================================================================

	public void artifactAdded(IAbstractArtifact artifact) {
		// TODO Auto-generated method stub

	}

	public void artifactChanged(IAbstractArtifact artifact) {
		// TODO Auto-generated method stub

	}

	public void artifactRemoved(IAbstractArtifact artifact) {
		// TODO Auto-generated method stub

	}

	public void artifactRenamed(IAbstractArtifact artifact, String fromFQN) {
		// TODO Auto-generated method stub

	}

	public void managerReloaded() {
		// TODO Auto-generated method stub

	}

	// ===================================================================
	// ===================================================================

	/**
	 * 
	 * @param kind
	 * @param monitor
	 */
	protected void smartModelAudit(int kind, IProgressMonitor monitor) {

		if (modelAuditorHelper == null) {
			modelAuditorHelper = new ModelAuditorHelper(
					(ITigerstripeModelProject) getProject().getAdapter(
							ITigerstripeModelProject.class));
		}

		Set<String> artifactsToAudit = new HashSet<String>();

		if (fullBuildRequired || kind == FULL_BUILD || kind == CLEAN_BUILD) {

			fullBuildRequired = false;
			ITigerstripeModelProject tsProject = (ITigerstripeModelProject) getProject()
					.getAdapter(ITigerstripeModelProject.class);

			try {
				modelAuditorHelper.reset();
				IArtifactManagerSession session = tsProject
						.getArtifactManagerSession();
				IArtifactQuery query = session
						.makeQuery(IQueryAllArtifacts.class.getName());
				query.setIncludeDependencies(false); // check only local
				// artifacts!
				Collection<IAbstractArtifact> artifacts = session
						.queryArtifact(query);

				IResource srcRes = getProject().findMember("src");
				if (srcRes != null)
					deleteAuditMarkers(srcRes, IResource.DEPTH_INFINITE);

				monitor.beginTask("Auditing Artifacts", artifacts.size());
				for (IAbstractArtifact artifact : artifacts) {
					monitor.subTask(artifact.getFullyQualifiedName());
					IArtifactAuditor auditor = ArtifactAuditorFactory
							.getInstance().newArtifactAuditor(getProject(),
									artifact);
					auditor.run(monitor);
					monitor.worked(1);
				}
				monitor.done();
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			}

		} else {
			List<String> changedArtifacts = new ArrayList<String>();
			List<String> addedArtifacts = new ArrayList<String>();
			List<String> removedArtifacts = new ArrayList<String>();

			// First figure out what append since last audit
			extractModelDeltas(kind, changedArtifacts, addedArtifacts,
					removedArtifacts);

			for (String artifactFQN : addedArtifacts)
				artifactsToAudit.addAll(modelAuditorHelper
						.auditListForArtifactAdded(artifactFQN));

			for (String artifactFQN : changedArtifacts)
				artifactsToAudit.addAll(modelAuditorHelper
						.auditListForArtifactChanged(artifactFQN));

			for (String artifactFQN : removedArtifacts)
				artifactsToAudit.addAll(modelAuditorHelper
						.auditListForArtifactFQNRemoved(artifactFQN));

			// Clear any existing marker for these
			ITigerstripeModelProject tsProject = (ITigerstripeModelProject) getProject()
					.getAdapter(ITigerstripeModelProject.class);

			// Ready to audit

			for (String artifactFQN : artifactsToAudit) {
				try {
					IArtifactManagerSession session = tsProject
							.getArtifactManagerSession();
					IAbstractArtifact artifact = session
							.getArtifactByFullyQualifiedName(artifactFQN);
					if (artifact != null) {
						deleteAuditMarkers(artifact);
						monitor.subTask(artifact.getFullyQualifiedName());
						IArtifactAuditor auditor = ArtifactAuditorFactory
								.getInstance().newArtifactAuditor(getProject(),
										artifact);
						auditor.run(monitor);
						modelAuditorHelper.artifactAudited(artifact);
						monitor.worked(1);
					}
				} catch (TigerstripeException e) {
					BasePlugin.log(e);
				}
			}

			for (String fqn : removedArtifacts)
				modelAuditorHelper.artifactRemoved(fqn);
		}
	}

	/**
	 * Populate the 3 given lists with FQNs of changed, added and removed
	 * artifact based on workspace delta.
	 * 
	 * @param kind
	 * @param changedArtifacts
	 * @param addedArtifacts
	 * @param removedArtifacts
	 */
	protected void extractModelDeltas(int kind, List<String> changedArtifacts,
			List<String> addedArtifacts, List<String> removedArtifacts) {
		IResourceDelta delta = getDelta(getProject());
		// Get the list of removed resources
		Collection<IResource> removedResources = new HashSet<IResource>();
		Collection<IResource> changedResources = new HashSet<IResource>();
		Collection<IResource> addedResources = new HashSet<IResource>();

		// We don't care about .class files as they are being changed by the
		// JDT
		IResourceFilter noClassFileOrFolderFilter = new IResourceFilter() {

			public boolean select(IResource resource) {
				if (resource instanceof IFolder)
					return false;
				return !"class".equals(resource.getFileExtension());
			}

		};

		WorkspaceHelper.buildResourcesLists(delta, removedResources,
				changedResources, addedResources, noClassFileOrFolderFilter);

		for (IResource res : changedResources) {
			if ("java".equals(res.getFileExtension())
					|| ".package".equals(res.getName())) {
				String fqn = TigerstripeResourceAdapterFactory
						.fqnForResource(res);
				changedArtifacts.add(fqn);
			}
		}

		for (IResource res : addedResources) {
			if ("java".equals(res.getFileExtension())
					|| ".package".equals(res.getName())) {
				String fqn = TigerstripeResourceAdapterFactory
						.fqnForResource(res);
				addedArtifacts.add(fqn);
			}
		}

		for (IResource res : removedResources) {
			if ("java".equals(res.getFileExtension())
					|| ".package".equals(res.getName())) {
				String fqn = TigerstripeResourceAdapterFactory
						.fqnForResource(res);
				removedArtifacts.add(fqn);
			}
		}

	}

	@Override
	@SuppressWarnings("unchecked")
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {

		if (shouldAudit(kind)) {
			auditProject(kind, monitor);
		}
		return null;
	}

	/**
	 * Run auditors that need to run on specific files
	 * 
	 */
	private void runAuditorsByFileExtensions(int kind, IProgressMonitor monitor) {

		// Run any custom rules that are defined in the extension point.
		try {
			IConfigurationElement[] elements = Platform
					.getExtensionRegistry()
					.getConfigurationElementsFor(
							"org.eclipse.tigerstripe.workbench.base.fileExtensionBasedAuditor");
			for (IConfigurationElement element : elements) {
				final IFileExtensionBasedAuditor customRule = (IFileExtensionBasedAuditor) element
						.createExecutableExtension("auditorClass");
				final IProgressMonitor finalMonitor = monitor;
				SafeRunner.run(new ISafeRunnable() {
					public void handleException(Throwable exception) {
						BasePlugin.log(exception);
					}

					public void run() throws Exception {
						String extension = customRule.getFileExtension();
						List<IResource> resources = findAll(getProject(),
								extension);
						deleteAuditMarkers(resources, IResource.DEPTH_ZERO);
						customRule.run(getProject(), resources, finalMonitor);
					}

				});
			}
		} catch (CoreException e) {
			TigerstripeProjectAuditor.reportError(
					"Invalid custom audit definitions: " + e.getMessage(),
					getProject(), 222);
		}

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
			IMarker marker = srcFile.createMarker(BuilderConstants.MARKER_ID);
			marker.setAttribute(IMarker.MESSAGE, msg);
			marker.setAttribute(IMarker.SEVERITY, severity);
		} catch (CoreException e) {
			BasePlugin.log(e);
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

		if (isTurnedOffForImport())
			return false;

		// if (kind == FULL_BUILD)
		// return true;
		//
		// if (kind == AUTO_BUILD) {
		//
		// IResourceDelta delta = getDelta(getProject());
		// // Get the list of removed resources
		// Collection<IResource> removedResources = new HashSet<IResource>();
		// Collection<IResource> changedResources = new HashSet<IResource>();
		// Collection<IResource> addedResources = new HashSet<IResource>();
		//
		// // We don't care about .class files as they are being changed by the
		// // JDT
		// IResourceFilter noClassFileOrFolderFilter = new IResourceFilter() {
		//
		// public boolean select(IResource resource) {
		// if (resource instanceof IFolder)
		// return false;
		// return !"class".equals(resource.getFileExtension());
		// }
		//
		// };
		//
		// WorkspaceHelper
		// .buildResourcesLists(delta, removedResources,
		// changedResources, addedResources,
		// noClassFileOrFolderFilter);
		//
		// // Only trying to avoid full project build when an artifact
		// // has changed since it would have been audited anyway through
		// // the artifactChangeListener
		// boolean javaOnly = true;
		// for (IResource res : changedResources) {
		// // When diagrams are saved no need to rebuild project
		// if ("vwm".equals(res.getFileExtension())
		// || "wvd".equals(res.getFileExtension())
		// || "wod".equals(res.getFileExtension())
		// || "owm".equals(res.getFileExtension())) {
		// continue;
		// } else if (!"java".equals(res.getFileExtension())
		// && !"class".equals(res.getFileExtension())) {
		// javaOnly = false;
		// break;
		// }
		// }
		//
		// return !javaOnly;
		// }
		// if (getDelta(getProject()) == null)
		// return false;
		//
		// // fow now, just build everything no matter what.
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
			} else {
				if (root instanceof IContainer) {
					// We need to avoid the "bin" directory - or we get two of
					// everything!
					// Hoever this may not be called "bin" so we have to do some
					// Gymnastics to work out if we are the "outputLocation"...
					if (root.getParent() instanceof IProject) {
						IProject project = (IProject) root.getParent();
						IJavaProject jProject = JavaCore.create(project);
						if (jProject != null) {
							if (jProject.getOutputLocation().equals(
									root.getFullPath())) {
								// System.out.println("this is the output folder "
								// +root);
								return result;
							}
						}
					}
					IContainer rootFolder = (IContainer) root;
					for (IResource res : rootFolder.members()) {
						result.addAll(findAll(res, extension));
					}
				}
			}
		} catch (CoreException e) {
			BasePlugin.log(e);
		}

		return result;
	}

	private void auditProject(int kind, IProgressMonitor monitor) {

		monitor.beginTask("Audit Tigerstripe Project", 9);

		ITigerstripeModelProject tsProject = (ITigerstripeModelProject) getProject()
				.getAdapter(ITigerstripeModelProject.class);

		IArtifactManagerSession session = null;
		try {
			session = tsProject.getArtifactManagerSession();

			// this refresh will indirectly trigger an audit of every individual
			// artifact as they are parsed by the Art. Mgr. This is not
			// necessary since all artifacts will be audited below.
			session.setBroadcastMask(IArtifactChangeListener.NOTIFY_NONE);
			session.refreshAll(monitor);
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
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

		runAuditorsByFileExtensions(kind, monitor);

		if (checkCancel(monitor))
			return;

		if (shouldCheckDescriptor(kind)) {
			checkDescriptor(monitor);
		}

		monitor.worked(1);
		if (checkCancel(monitor))
			return;

		smartModelAudit(kind, monitor);
		if (checkCancel(monitor))
			return;

		monitor.done();
	}

	class LookForDescriptorVisitor implements IResourceDeltaVisitor {

		private boolean descriptorFound = false;
		private String descriptorName = "";

		public LookForDescriptorVisitor(String descriptorName) {
			this.descriptorName = descriptorName;
		}

		public boolean visit(IResourceDelta delta) throws CoreException {
			descriptorFound = descriptorName.equals(delta.getResource()
					.getName())
					&& delta.getKind() == IResourceDelta.CHANGED;
			return !descriptorFound;
		}

		public boolean hasChanged() {
			return descriptorFound;
		}
	}

	/**
	 * This method determines if a check of the auditor is necessary.
	 * 
	 * @return
	 */
	private boolean shouldCheckDescriptor(int kind) {
		if (kind == FULL_BUILD || kind == CLEAN_BUILD) {
			return true;
		}

		IResourceDelta delta = getDelta(getProject());
		LookForDescriptorVisitor vis = new LookForDescriptorVisitor(
				ITigerstripeConstants.PROJECT_DESCRIPTOR);
		try {
			delta.accept(vis);
		} catch (CoreException e) {
			BasePlugin.log(e);
		}
		return vis.hasChanged();
	}

	private void checkDescriptor(IProgressMonitor monitor) {
		DescriptorAuditor auditor = new DescriptorAuditor(getProject());
		auditor.run(monitor);
	}

	private void checkArtifacts(IProgressMonitor monitor) {
		ITigerstripeModelProject tsProject = (ITigerstripeModelProject) getProject()
				.getAdapter(ITigerstripeModelProject.class);

		try {
			IArtifactManagerSession session = tsProject
					.getArtifactManagerSession();
			IArtifactQuery query = session.makeQuery(IQueryAllArtifacts.class
					.getName());
			query.setIncludeDependencies(false); // check only local
			// artifacts!
			Collection<IAbstractArtifact> artifacts = session
					.queryArtifact(query);

			monitor.beginTask("Auditing Artifacts", artifacts.size());
			for (IAbstractArtifact artifact : artifacts) {
				monitor.subTask(artifact.getFullyQualifiedName());
				IArtifactAuditor auditor = ArtifactAuditorFactory.getInstance()
						.newArtifactAuditor(getProject(), artifact);
				auditor.run(monitor);
				monitor.worked(1);
			}
			monitor.done();
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
	}

	public boolean deleteAuditMarkers(IAbstractArtifact artifact) {

		AbstractArtifact aArt = (AbstractArtifact) artifact;
		try {
			String artPath = aArt.getArtifactPath();
			IResource res = getProject().findMember(artPath);
			if (res != null) {
				res.deleteMarkers(BuilderConstants.MARKER_ID, false,
						IResource.DEPTH_ZERO);
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

	public static boolean deleteAuditMarkers(List<IResource> resources,
			int depth) {
		boolean result = false;
		for (IResource resource : resources) {
			result = result | deleteAuditMarkers(resource, depth);
		}
		return result;
	}

	public static boolean deleteAuditMarkers(IResource resource, int depth) {
		try {
			if (resource != null)
				resource
						.deleteMarkers(BuilderConstants.MARKER_ID, false, depth);
			return true;
		} catch (CoreException e) {
			BasePlugin.log(e);
			return false;
		}
	}

	private boolean checkCancel(IProgressMonitor monitor) {
		if (monitor.isCanceled())
			throw new OperationCanceledException();

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
			BasePlugin.log(e);
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
			BasePlugin.log(e);
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
		List<ICommand> newCmds = new ArrayList<ICommand>();
		newCmds.addAll(Arrays.asList(cmds));
		newCmds.add(newCmd);
		description.setBuildSpec((ICommand[]) newCmds
				.toArray(new ICommand[newCmds.size()]));
		try {
			project.setDescription(description, null);
		} catch (CoreException e) {
			BasePlugin.log(e);
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
			BasePlugin.log(e);
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
		List<ICommand> newCmds = new ArrayList<ICommand>();
		newCmds.addAll(Arrays.asList(cmds));
		newCmds.remove(index);
		description.setBuildSpec((ICommand[]) newCmds
				.toArray(new ICommand[newCmds.size()]));

		try {
			project.setDescription(description, null);
		} catch (CoreException e) {
			BasePlugin.log(e);
		}
	}

	public static IResource getIResourceForArtifact(IProject project,
			IAbstractArtifact artifact) {
		AbstractArtifact art = (AbstractArtifact) artifact;
		try {
			String realPath = art.getArtifactPath();
			return project.findMember(realPath);
		} catch (TigerstripeException e) {
			return null;
		}
	}

}
