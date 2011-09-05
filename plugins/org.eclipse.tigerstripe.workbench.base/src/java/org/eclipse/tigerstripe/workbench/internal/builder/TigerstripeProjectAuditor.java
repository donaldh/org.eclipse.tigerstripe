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

import static org.eclipse.core.resources.IResourceDelta.REMOVED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeResourceAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.builder.WorkspaceHelper.IResourceFilter;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.IProjectDependencyChangeListener;
import org.eclipse.tigerstripe.workbench.project.IProjectDependencyDelta;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.utils.AdaptHelper;

/**
 * This is the incremental auditor for a Tigerstripe Project.
 * 
 * @author Eric Dillon
 * 
 */
public class TigerstripeProjectAuditor extends IncrementalProjectBuilder
		implements IProjectDependencyChangeListener {

	private ModelAuditorHelper modelAuditorHelper = null;

	private boolean fullBuildRequired = false;

	public static final String BUILDER_ID = BuilderConstants.PROJECT_BUILDER_ID;

	private IPath javaOutputPath = null;

	public TigerstripeProjectAuditor() {
		super();
	}

	@Override
	protected void startupOnInitialize() {
		super.startupOnInitialize();
		ITigerstripeModelProject tsProject = (ITigerstripeModelProject) getProject()
				.getAdapter(ITigerstripeModelProject.class);
		tsProject.addProjectDependencyChangeListener(this);
		// See if its a java project, and that the start of the resource path is
		// equal to the output directory
		IProject project = getProject();
		try {
			IJavaProject jProject = JavaCore.create(project);
			if (jProject != null) {
				javaOutputPath = jProject.getOutputLocation();
			}
		} catch (JavaModelException j) {
			// ignore
		}
	}

	// ===================================================================
	// ===================================================================
	// force a full build the next build around...
	public void projectDependenciesChanged(IProjectDependencyDelta delta) {
		fullBuildRequired = true;
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
				Collection<IAbstractArtifact> artifacts = session
						.queryArtifact(query);
				IResource srcRes = getProject().findMember("src");
				if (srcRes != null) {
					deleteAuditMarkers(srcRes, IResource.DEPTH_ZERO);
				}

				monitor.beginTask("Auditing Artifacts", artifacts.size());
				for (IAbstractArtifact artifact : artifacts) {
					deleteAuditMarkers(artifact);
					monitor.subTask(artifact.getFullyQualifiedName());
					IArtifactAuditor auditor = ArtifactAuditorFactory.INSTANCE
							.newArtifactAuditor(getProject(), artifact);
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
						IArtifactAuditor auditor = ArtifactAuditorFactory.INSTANCE
								.newArtifactAuditor(getProject(), artifact);
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

		// We only care about .java and .package files
		// However - we need to avoid .packages in the bin directory!
		// Which may not be called "bin" !
		IResourceFilter noClassFileOrFolderFilter = new IResourceFilter() {

			public boolean select(IResource resource) {
				if (resource instanceof IFolder)
					return false;
				if (javaOutputPath != null) {
					if (resource.getFullPath().toString()
							.startsWith(javaOutputPath.toString()))
						return false;
				}
				if ("java".equals(resource.getFileExtension())
						|| ".package".equals(resource.getName()))
					return true;

				return false;
			}

		};
		// DateFormat format = new SimpleDateFormat("yyyy.MM.dd-hh.mm.ss");
		// String dateStr = format.format(new Date())+ " : ";
		// System.out.println( dateStr+"Project Auditor extracting");
		WorkspaceHelper.buildResourcesLists(delta, removedResources,
				changedResources, addedResources, noClassFileOrFolderFilter);

		// dateStr = format.format(new Date())+ " : ";
		// System.out.println(
		// dateStr+"Project Auditor "+getProject().getName()+" Built changes for "+addedResources.size()+" "+changedResources.size()+
		// " "+removedResources.size());

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

	@SuppressWarnings("rawtypes")
	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {

		try {
			checkUnresolvedAnnotations(kind);
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
		checkUnresolvedModelReferences();

		if ("True".equals(args.get("rebuildIndexes"))) {
			smartModelAudit(kind, monitor);
		} else if (shouldAudit(kind)) {
			auditProject(kind, monitor);
		}
		return getRequiredProjects();
	}

	private IProject[] getRequiredProjects() {
		List<IProject> required = new ArrayList<IProject>();
		IProject self = getProject();

		try {
			Set<String> modelIds = new HashSet<String>();

			ITigerstripeModelProject model = (ITigerstripeModelProject) self
					.getAdapter(ITigerstripeModelProject.class);
			for (ModelReference ref : model.getModelReferences()) {
				modelIds.add(ref.getToModelId());
			}

			if (!modelIds.isEmpty()) {

				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				for (IProject project : root.getProjects()) {
					if (project.equals(self))
						continue;
					ProjectInfo details = BasePlugin.getDefault()
							.getProjectDetails(project);
					if (details != null) {
						String modelId = details.getModelId();
						if (modelId != null && modelIds.contains(modelId))
							required.add(project);
					}
				}
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}

		return required.toArray(new IProject[0]);
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
		if (isTurnedOffForImport()) {
			// System.out.println("Turned off");
			return false;
		}
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
						try {
							IJavaProject jProject = JavaCore.create(project);
							if (jProject != null) {
								if (jProject.getOutputLocation().equals(
										root.getFullPath())) {
									// System.out.println("this is the output folder "
									// +root);
									return result;
								}
							}
						} catch (JavaModelException j) {
							// ignore
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
		runAuditorsByFileExtensions(kind, monitor);
		if (checkCancel(monitor)) {
			return;
		}
		if (shouldCheckDescriptor(kind)) {
			checkDescriptor(monitor);
		}
		monitor.worked(1);
		if (checkCancel(monitor)) {
			return;
		}
		smartModelAudit(kind, monitor);
		if (checkCancel(monitor)) {
			return;
		}
		monitor.done();
	}

	private boolean shouldCheckDescriptor(int kind) {
		if (kind == FULL_BUILD || kind == CLEAN_BUILD) {
			return true;
		}

		IResourceDelta delta = getDelta(getProject());
		// look for project descriptor or facets
		LookForResourceVisitor vis = new LookForResourceVisitor() {
			@Override
			public boolean isLookedFor(int deltaKind, IResource resource) {
				if (resource instanceof IFile) {
					if ((deltaKind == IResourceDelta.CHANGED && ITigerstripeConstants.PROJECT_DESCRIPTOR
							.equals(resource.getName()))
							|| IContractSegment.FILE_EXTENSION.equals(resource
									.getFileExtension())) {
						return true;
					}
				}
				return false;
			}
		};
		try {
			delta.accept(vis);
		} catch (CoreException e) {
			BasePlugin.log(e);
		}
		return vis.hasLookedFor();
	}

	private void checkDescriptor(IProgressMonitor monitor) {
		DescriptorAuditor auditor = new DescriptorAuditor(getProject());
		auditor.run(monitor);
	}

	public boolean deleteAuditMarkers(IAbstractArtifact artifact) {

		IAbstractArtifactInternal aArt = (IAbstractArtifactInternal) artifact;
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
				resource.deleteMarkers(BuilderConstants.MARKER_ID, false, depth);
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
		IAbstractArtifactInternal art = (IAbstractArtifactInternal) artifact;
		try {
			String realPath = art.getArtifactPath();
			return project.findMember(realPath);
		} catch (TigerstripeException e) {
			return null;
		}
	}

	public static void rebuildIndexes(
			ITigerstripeModelProject[] projectsToRebuild,
			IProgressMonitor monitor) {
		// look for all relevant builders
		for (ITigerstripeModelProject mProj : projectsToRebuild) {
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("rebuildIndexes", "True");
				IProject proj = (IProject) mProj.getAdapter(IProject.class);
				if (proj != null)
					proj.build(FULL_BUILD,
							TigerstripeProjectAuditor.BUILDER_ID, map, monitor);
			} catch (CoreException e) {
				BasePlugin.log(e);
			}
		}
	}

	private void checkUnresolvedModelReferences() {
		IProject project = getProject();
		ITigerstripeModelProject tsProject = (ITigerstripeModelProject) project
				.getAdapter(ITigerstripeModelProject.class);
		IFile projectDescriptor = project
				.getFile(ITigerstripeConstants.PROJECT_DESCRIPTOR);
		try {
			ModelReference[] references = tsProject.getModelReferences();
			for (ModelReference reference : references) {
				ITigerstripeModelProject model = reference.getResolvedModel();
				if (model == null) {
					TigerstripeProjectAuditor.reportError(
							"Unresolved model reference with id '"
									+ reference.getToModelId() + "'",
							projectDescriptor, 222);
				}
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
	}

	protected void checkUnresolvedAnnotations(int kind) throws TigerstripeException, CoreException {
		IProject project = getProject();
		String modelId = getModelId();
		if (modelId == null) {
			return;
		}
		List<URI> uries = Collections.singletonList(createAnnUri(modelId));
		if (kind == FULL_BUILD) {
			uries = Collections.singletonList(createAnnUri(modelId));
		} else {
			Set<IResource> deletedResources = findDeleted();
			if (!deletedResources.isEmpty()) {
				uries = Collections.singletonList(createAnnUri(modelId));
			} else {
				uries = Collections.emptyList();
			}
			       
//			if (!deletedResources.isEmpty()) {
//				uries = new ArrayList<URI>();
//				for (IResource dr : deletedResources) {
//					IAbstractArtifact artifact = AdaptHelper.adapt(dr, IAbstractArtifact.class);
//					if (artifact == null) {
//						continue;
//					}
//					uries.add(createAnnUri(modelId, artifact.getFullyQualifiedName()));
//				}
//			} else {
//				return;
//			}
		}
		
		if (uries.isEmpty()) {
			return;
		}
		
		try {
			project.deleteMarkers(BuilderConstants.ANNOTATION_MARKER_ID, false,
					IProject.DEPTH_INFINITE);
		} catch (CoreException e) {
			BasePlugin.log(e);
		}
		
		for (URI uri : uries) {
			IAnnotationManager am = AnnotationPlugin.getManager();
			List<Annotation> annotations = am.getPostfixAnnotationsRaw(uri);
			for (Annotation annotation : annotations) {
				Object object = am.getAnnotatedObject(annotation);
				if (object == null) {
					addAnnotationMarker(annotation);
				}
			}
		}

	}

	private String getModelId() {
		ITigerstripeModelProject modelProject = AdaptHelper.adapt(getProject(), ITigerstripeModelProject.class);
		if (modelProject == null) {
			return null;
		}
		try {
			return modelProject.getModelId();
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return null;
		}
	}

	private Set<IResource> findDeleted() throws CoreException {
		IResourceDelta delta = getDelta(getProject());
		if (delta == null) {
			return Collections.emptySet();
		}
		final Set<IResource> deleted = new HashSet<IResource>();;
		
		delta.accept(new IResourceDeltaVisitor() {
			
			public boolean visit(IResourceDelta delta) throws CoreException {
				IResource resource = delta.getResource();
				if (!(resource instanceof IFile)) {
					return true;
				} else {
					if (delta.getKind() == REMOVED ){
						deleted.add(resource);
					}
					return false;
				}
			}
		});
		return deleted;
	}

	private URI createAnnUri(String... segments) throws TigerstripeException {
		return URI.createHierarchicalURI(
				TigerstripeURIAdapterFactory.SCHEME_TS, null, null, segments,
				null, null);
	}

	private void addAnnotationMarker(Annotation annotation) {
		try {
			
//			annotation.getContent().eResource().ge;
			
//			annotation.getContent().eContainer().eContainer().eResource()
			
			
			Path path = new Path(annotation.eResource().getURI().toPlatformString(true));
			
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			
			if (file == null || !file.exists()) {
				return;
			}
			
			IMarker marker = file.createMarker(
					BuilderConstants.ANNOTATION_MARKER_ID);
			marker.setAttribute(IMarker.MESSAGE,
					"Annotation file contains unresolved annotation");
			URI aUri = annotation.getUri();
			aUri = URI.createHierarchicalURI(aUri.segments(), aUri.query(),
					aUri.fragment());
			marker.setAttribute(IMarker.LOCATION, aUri.toString());
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
			marker.setAttribute(BuilderConstants.ANNOTATION_ID,
					annotation.getId());
		} catch (Exception e) {
			BasePlugin.log(e);
		}
	}

	private abstract class LookForResourceVisitor implements
			IResourceDeltaVisitor {

		private boolean found = false;

		public boolean visit(IResourceDelta delta) throws CoreException {
			found = isLookedFor(delta.getKind(), delta.getResource());
			return !found;
		}

		public boolean hasLookedFor() {
			return found;
		}

		public abstract boolean isLookedFor(int deltaKind, IResource resource);
	}
}
