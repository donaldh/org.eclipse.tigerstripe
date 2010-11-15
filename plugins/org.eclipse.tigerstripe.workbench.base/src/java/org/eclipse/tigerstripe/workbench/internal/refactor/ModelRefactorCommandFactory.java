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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactFQRenameRequest;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.workbench.internal.api.project.IPhantomTigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.internal.refactor.diagrams.DiagramChangeDelta;
import org.eclipse.tigerstripe.workbench.internal.refactor.diagrams.DiagramRefactorHelper;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.refactor.IRefactorCommand;
import org.eclipse.tigerstripe.workbench.refactor.ITigerstripeModelRefactorParticipant;
import org.eclipse.tigerstripe.workbench.refactor.ModelRefactorRequest;
import org.eclipse.tigerstripe.workbench.refactor.PackageModelRefactorRefactorRequest;
import org.eclipse.tigerstripe.workbench.refactor.RefactorRequest;
import org.eclipse.tigerstripe.workbench.refactor.ResourceRefactorRequest;
import org.eclipse.tigerstripe.workbench.refactor.diagrams.DiagramRefactorRequest;
import org.eclipse.tigerstripe.workbench.refactor.diagrams.HeadlessDiagramHandle;
import org.eclipse.tigerstripe.workbench.refactor.diagrams.IDiagramChangeDelta;

/**
 * This factory computes the list of refactor commands required for a target
 * request. It delegates the actual computation to a sub-factory based on the
 * type of the Artifact being refactored.
 * 
 * @author erdillon
 * @since 0.4.6
 */
public class ModelRefactorCommandFactory {

	private final static String REFACTOR_PARTICIPANT_EXTPT = "org.eclipse.tigerstripe.workbench.base.modelRefactorParticipant";

	public final static ModelRefactorCommandFactory INSTANCE = new ModelRefactorCommandFactory();

	/**
	 * Exclude class and instance diagrams
	 */
	private static final Set<String> IGNORE_REFACTORING_RESOURCES = new HashSet<String>(
			Arrays.asList("vwm", "wvd", "owm", "wod"));

	private List<ITigerstripeModelRefactorParticipant> participants = new ArrayList<ITigerstripeModelRefactorParticipant>();

	private ModelRefactorCommandFactory() {
		discoverRegisteredParticipants();
	}

	private void discoverRegisteredParticipants() {
		// The first participant is the base
		IConfigurationElement[] elements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(REFACTOR_PARTICIPANT_EXTPT);
		for (IConfigurationElement element : elements) {
			try {
				final ITigerstripeModelRefactorParticipant participant = (ITigerstripeModelRefactorParticipant) element
						.createExecutableExtension("participant");
				participants.add(participant);
			} catch (CoreException e) {
				BasePlugin.log(e);
			}
		}
	}

	/**
	 * Based on the given {@link ModelRefactorRequest}, this method creates a
	 * command ready to be executed to perform the refactor.
	 * 
	 * This includes:
	 * <ul>
	 * <li>A first pass to figure out all derived request, e.g. if the initial
	 * request is about renaming a package, all content should be renamed one by
	 * one. This allows to obtain a list of all Artifacts that will have a
	 * change of FQN.</li>
	 * <li>We then go thru the whole model scope (model + referenced models) to
	 * figure out where the FQNs to be changed appear. This results in the
	 * creation of a collection of {@link IModelChangeDelta} corresponding to
	 * ALL changes to be performed on the model scope. A secondary list is
	 * created containing all diagrams needing to be move/renamed (independantly
	 * of their content).</li>
	 * </ul>
	 * 
	 * At the end, the resulting command is ready to be executed, ie. each delta
	 * to be applied on the model.
	 * 
	 * @param request
	 * @param monitor
	 * @return
	 * @throws TigerstripeException
	 */
	@SuppressWarnings("deprecation")
	public IRefactorCommand getCommand(ModelRefactorRequest request,
			IProgressMonitor monitor) throws TigerstripeException {
		if (request.isValid().getSeverity() == IStatus.OK) {

			IAbstractArtifact original = request.getOriginalArtifact();
			ITigerstripeModelProject originalProject = original.getProject();
			IQueryAllArtifacts query = (IQueryAllArtifacts) originalProject
					.getArtifactManagerSession().makeQuery(
							IQueryAllArtifacts.class.getName());
			query.setIncludeDependencies(true);
			Collection<IAbstractArtifact> artifacts = originalProject
					.getArtifactManagerSession().queryArtifact(query);

			for (IAbstractArtifact artifact : artifacts) {
				if (artifact instanceof AbstractArtifact) {
					((AbstractArtifact) artifact)
							.resolvePackageContainment(monitor);
				}
			}

			// First get all the derived requests if any
			List<RefactorRequest> derivedRequests = new ArrayList<RefactorRequest>();
			derivedRequests.add(request);
			derivedRequests.addAll(buildDerivedRequests(request));

			BaseRefactorCommand cmd = new BaseRefactorCommand(
					derivedRequests.toArray(new RefactorRequest[derivedRequests
							.size()]));

			// Create a map of requests indexed by the FQN that will change as a
			// result of the request
			Map<String, ModelRefactorRequest> mappedRequests = mapRequests(derivedRequests);

			// we visit the whole model and all the referenced projects
			// For each visited Artifact we assess if any of the
			// ModelRefactorRequest would impact that artifact and create
			// IModelChangeDelta accordingly
			for (IAbstractArtifact artifact : artifacts) {
				List<ModelChangeDelta> deltas = createDeltas(artifact,
						mappedRequests);
				cmd.addDeltas(deltas);
			}

			// At this point we still need to assess impact on projects that
			// depend on this originalProject
			ModelReference[] referencingModels = originalProject
					.getReferencingModels(ModelReference.INFINITE_LEVEL);
			for (ModelReference referencingModel : referencingModels) {
				ITigerstripeModelProject project = referencingModel
						.getResolvedModel();
				if (project != null
						&& !(project instanceof ITigerstripeModuleProject)
						&& !(project instanceof IPhantomTigerstripeProject)) {
					query = (IQueryAllArtifacts) project
							.getArtifactManagerSession().makeQuery(
									IQueryAllArtifacts.class.getName());
					query.setIncludeDependencies(false);
					artifacts = project.getArtifactManagerSession()
							.queryArtifact(query);
					for (IAbstractArtifact artifact : artifacts) {
						List<ModelChangeDelta> deltas = createDeltas(artifact,
								mappedRequests);
						cmd.addDeltas(deltas);
					}
				}
			}

			// Then assess any action to be taken on Diagrams after that
			cmd.addDiagramDeltas(createDiagramDeltas(derivedRequests));
			// Add resources
			cmd.addResourceDeltas(createResourceDeltas(derivedRequests));
			return cmd;
		}
		return IRefactorCommand.UNEXECUTABLE;
	}

	private List<ResourceChangeDelta> createResourceDeltas(
			List<RefactorRequest> requests) {
		List<ResourceChangeDelta> deltas = new ArrayList<ResourceChangeDelta>();
		for (RefactorRequest request : requests) {
			if (request instanceof ResourceRefactorRequest) {
				ResourceRefactorRequest req = (ResourceRefactorRequest) request;
				ResourceChangeDelta delta = new ResourceChangeDelta(
						ResourceChangeDelta.Type.MOVE, req.getResource(), req
								.getDestination().getFullPath());
				deltas.add(delta);
			}
		}
		return deltas;
	}

	protected List<DiagramChangeDelta> createDiagramDeltas(
			List<RefactorRequest> requests) {
		List<DiagramChangeDelta> diagramDeltas = new ArrayList<DiagramChangeDelta>();
		for (RefactorRequest request : requests) {
			if (request instanceof DiagramRefactorRequest) {
				DiagramRefactorRequest req = (DiagramRefactorRequest) request;
				DiagramChangeDelta delta = new DiagramChangeDelta(
						IDiagramChangeDelta.MOVE);
				delta.setAffectedDiagramHandle(req.getOriginalDiagramHandle());
				delta.setDestinationPath(req.getDestination().getFullPath());
				diagramDeltas.add(delta);
			}
		}
		return diagramDeltas;
	}

	/**
	 * Indexes ModelRefactorRequests in the given list by originalFQN
	 * 
	 * @param requests
	 * @return
	 */
	protected Map<String, ModelRefactorRequest> mapRequests(
			List<RefactorRequest> requests) {
		HashMap<String, ModelRefactorRequest> mappedRequests = new HashMap<String, ModelRefactorRequest>();

		for (RefactorRequest request : requests) {
			if (request instanceof ModelRefactorRequest) {
				ModelRefactorRequest mReq = (ModelRefactorRequest) request;
				String originalFQN = mReq.getOriginalFQN();
				mappedRequests.put(originalFQN, mReq);
			}
		}
		return mappedRequests;
	}

	/**
	 * This method builds a list of deltas to apply to the given artifact, to
	 * service the hash of requests.
	 * 
	 * This is done by comparing all occurrences of FQNs in the artifact with
	 * keys in the mappedRequests
	 * 
	 * @param artifact
	 * @param mappedRequests
	 * @return
	 */
	protected List<ModelChangeDelta> createDeltas(IAbstractArtifact artifact,
			Map<String, ModelRefactorRequest> mappedRequests)
			throws TigerstripeException {

		List<ModelChangeDelta> deltas = new ArrayList<ModelChangeDelta>();
		ModelChangeDeltaFactory factory = new ModelChangeDeltaFactory(artifact,
				mappedRequests);

		// Artifact itself
		String fqn = artifact.getFullyQualifiedName();
		ModelRefactorRequest request = mappedRequests.get(fqn);
		if (request != null
				&& artifact.getProject().equals(request.getOriginalProject())) {
			ModelChangeDelta delta = new ModelChangeDelta(IModelChangeDelta.SET);
			delta.setFeature(IArtifactFQRenameRequest.FQN_FEATURE);
			delta.setAffectedModelComponentURI((URI) artifact
					.getAdapter(URI.class));
			delta.setComponent(artifact);
			delta.setProject(artifact.getProject());
			delta.setOldValue(fqn);
			delta.setNewValue(request.getDestinationFQN());
			delta.setSource(request);
			deltas.add(delta);
		}

		// Extends
		ModelChangeDelta delta = factory.EXTENDS();
		if (delta != null)
			deltas.add(delta);
		deltas.addAll(factory.IMPLEMENTS());

		delta = factory.RETURNS();
		if (delta != null)
			deltas.add(delta);

		deltas.addAll(factory.ASSOC_ENDS());
		deltas.addAll(factory.DEP_ENDS());

		deltas.addAll(factory.FIELDS());
		deltas.addAll(factory.METHODS());

		return deltas;
	}

	// Each request may require derived requests (e.g. if the moved artifact is
	// a IPackageArtifact)
	// Note that it can lead to non-model requests (i.e.
	// DiagramRefactorRequests).
	protected List<RefactorRequest> buildDerivedRequests(
			ModelRefactorRequest request) throws TigerstripeException {
		IAbstractArtifact original = request.getOriginalArtifact();
		Set<RefactorRequest> result = new LinkedHashSet<RefactorRequest>();
		if (!(original instanceof IPackageArtifact)) {
			return new ArrayList<RefactorRequest>(result);
		}

		// Is there any diagram that need to be moved at this level?
		try {
			IResource packageMarker = (IResource) original
					.getAdapter(IResource.class);
			IFolder folder = null;
			if (packageMarker == null) {
				// this means the .package file in the folder doesn't exist
				// let's try something else
				IPath path = new Path(original.getArtifactPath());
				path = path.removeLastSegments(1);
				IProject proj = (IProject) original.getProject().getAdapter(
						IProject.class);
				folder = (IFolder) proj.findMember(path);
			} else {
				folder = (IFolder) packageMarker.getParent();
			}

			ITigerstripeModelProject destProj = request.getDestinationProject();
			IProject destIProj = (IProject) destProj.getAdapter(IProject.class);
			if (destIProj != null) {
				String destPackage = Util.packageOf(request.getDestinationFQN()
						+ "."); // note the "." as we know this is a package
				IResource dest = ResourcesPlugin
						.getWorkspace()
						.getRoot()
						.getFolder(
								destIProj
										.getFullPath()
										.append("src")
										.append(destPackage.replace('.',
												IPath.SEPARATOR)));

				for (IResource member : folder.members()) {
					HeadlessDiagramHandle handle = DiagramRefactorHelper
							.getDiagramHandle(member);
					if (handle != null) {
						DiagramRefactorRequest req = new DiagramRefactorRequest();
						req.setOriginalDiagramHandle(handle);
						req.setDestination(dest);
						result.add(req);

					}
					if (isRefactoringResource(member)) {
						result.add(new ResourceRefactorRequest(member, dest));
					}
				}
			}
		} catch (CoreException e) {
			BasePlugin.log(e);
		}

		// Then look for all artifacts at this level, and dive recursively
		String destPackageFQN = request.getDestinationFQN();
		for (IModelComponent comp : original.getContainedModelComponents()) {
			if (canHandle(comp, request)) {
				if (comp instanceof IAbstractArtifact) {
					IAbstractArtifact containedArtifact = (IAbstractArtifact) comp;
					String oFQN = containedArtifact.getFullyQualifiedName();
					String dFQN = destPackageFQN + "."
							+ containedArtifact.getName();
					ModelRefactorRequest dRequest = new ModelRefactorRequest();
					dRequest.setOriginal(request.getOriginalProject(), oFQN);
					dRequest.setDestination(request.getDestinationProject(),
							dFQN);
					result.add(dRequest);
					if (containedArtifact instanceof IPackageArtifact) {
						result.addAll(buildDerivedRequests(dRequest));
					}
				}
			}
		}

		return new ArrayList<RefactorRequest>(result);
	}

	private boolean canHandle(IModelComponent component,
			ModelRefactorRequest request) {
		if (!(component instanceof IAbstractArtifact)) {
			return false;
		}
		if (request instanceof PackageModelRefactorRefactorRequest) {
			if (component instanceof IPackageArtifact
					&& !((PackageModelRefactorRefactorRequest) request)
							.isRenameSubpackages()) {
				return false;
			}
		}
		return true;
	}

	private boolean isRefactoringResource(IResource member) {
		if (member.getAdapter(IModelComponent.class) != null) {
			return false;
		}
		String extension = member.getFileExtension();
		if (extension == null) {
			return true;
		} else {
			return !IGNORE_REFACTORING_RESOURCES.contains(extension);
		}
	}
}
