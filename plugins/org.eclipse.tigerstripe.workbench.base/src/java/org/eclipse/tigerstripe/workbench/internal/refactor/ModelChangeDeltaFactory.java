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
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactRemoveFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodSetRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.refactor.ModelRefactorRequest;

/**
 * This helper class is used to create deltas for all features of a Tigerstripe
 * Artifact
 * 
 * @author erdillon
 * 
 */
public class ModelChangeDeltaFactory {

	private IAbstractArtifact artifact;
	private Map<String, ModelRefactorRequest> mappedRequests;

	public ModelChangeDeltaFactory(IAbstractArtifact artifact,
			Map<String, ModelRefactorRequest> mappedRequests) {
		this.artifact = artifact;
		this.mappedRequests = mappedRequests;
	}

	public ModelChangeDelta EXTENDS() throws TigerstripeException {
		if (artifact.hasExtends()) {
			String fqn = artifact.getExtendedArtifact().getFullyQualifiedName();
			ModelRefactorRequest request = mappedRequests.get(fqn);
			if (request != null) {
				ModelChangeDelta delta = new ModelChangeDelta(
						IModelChangeDelta.SET);
				delta.setAffectedModelComponentURI((URI) artifact
						.getAdapter(URI.class));
				delta.setComponent(artifact);
				delta.setProject(artifact.getProject());
				delta.setFeature(IArtifactSetFeatureRequest.EXTENDS_FEATURE);
				delta.setOldValue(fqn);
				delta.setNewValue(request.getDestinationFQN());
				delta.setSource(request);
				return delta;
			}
		}
		return null;
	}

	public List<ModelChangeDelta> IMPLEMENTS() throws TigerstripeException {
		List<ModelChangeDelta> deltas = new ArrayList<ModelChangeDelta>();

		if (artifact.getImplementedArtifacts() != null
				&& !artifact.getImplementedArtifacts().isEmpty()) {
			for (IAbstractArtifact art : artifact.getImplementedArtifacts()) {
				String fqn = art.getFullyQualifiedName();
				ModelRefactorRequest request = mappedRequests.get(fqn);
				if (request != null) {
					ModelChangeDelta delta = new ModelChangeDelta(
							IModelChangeDelta.REMOVE);
					delta
							.setFeature(IArtifactRemoveFeatureRequest.IMPLEMENTS_FEATURE);
					delta.setAffectedModelComponentURI((URI) artifact
							.getAdapter(URI.class));
					delta.setComponent(artifact);
					delta.setOldValue(fqn);
					delta.setSource(request);
					deltas.add(delta);

					delta = new ModelChangeDelta(IModelChangeDelta.ADD);
					delta
							.setFeature(IArtifactRemoveFeatureRequest.IMPLEMENTS_FEATURE);
					delta.setAffectedModelComponentURI((URI) artifact
							.getAdapter(URI.class));
					delta.setComponent(artifact);
					delta.setNewValue(request.getDestinationFQN());
					delta.setSource(request);
					deltas.add(delta);
				}
			}
		}

		return deltas;
	}

	public ModelChangeDelta RETURNS() throws TigerstripeException {
		if (artifact instanceof IQueryArtifact) {
			IQueryArtifact art = (IQueryArtifact) artifact;
			if ((art != null) && (art.getReturnedType() != null)) {
				String fqn = art.getReturnedType().getFullyQualifiedName();
				ModelRefactorRequest request = mappedRequests.get(fqn);
				if (request != null) {
					ModelChangeDelta delta = new ModelChangeDelta(
							IModelChangeDelta.SET);
					delta.setFeature(IArtifactSetFeatureRequest.RETURNED_TYPE);
					delta.setAffectedModelComponentURI((URI) artifact
							.getAdapter(URI.class));
					delta.setComponent(artifact);
					delta.setOldValue(fqn);
					delta.setNewValue(request.getDestinationFQN());
					delta.setProject(artifact.getProject());
					delta.setSource(request);
					return delta;
				}
			}
		}
		return null;
	}

	public List<ModelChangeDelta> FIELDS() throws TigerstripeException {
		List<ModelChangeDelta> deltas = new ArrayList<ModelChangeDelta>();

		for (IField field : artifact.getFields()) {
			String type = field.getType().getFullyQualifiedName();
			ModelRefactorRequest req = mappedRequests.get(type);
			if (req != null) {
				ModelChangeDelta delta = new ModelChangeDelta(
						IModelChangeDelta.SET);
				delta.setAffectedModelComponentURI((URI) field
						.getAdapter(URI.class));
				delta.setComponent(field);
				delta.setProject(artifact.getProject());
				delta.setFeature(IAttributeSetRequest.TYPE_FEATURE);
				delta.setOldValue(type);
				delta.setNewValue(req.getDestinationFQN());
				delta.setSource(req);
				deltas.add(delta);
			}
		}
		return deltas;
	}

	public List<ModelChangeDelta> DEP_ENDS() throws TigerstripeException {
		List<ModelChangeDelta> deltas = new ArrayList<ModelChangeDelta>();

		if (artifact instanceof IDependencyArtifact) {
			IDependencyArtifact dep = (IDependencyArtifact) artifact;
			String aEndType = dep.getAEndType().getFullyQualifiedName();
			ModelRefactorRequest req = mappedRequests.get(aEndType);
			if (req != null) {
				ModelChangeDelta delta = new ModelChangeDelta(
						IModelChangeDelta.SET);
				delta.setAffectedModelComponentURI((URI) artifact
						.getAdapter(URI.class));
				delta.setComponent(artifact);
				delta.setProject(artifact.getProject());
				delta.setFeature(IArtifactSetFeatureRequest.AEND);
				delta.setOldValue(aEndType);
				delta.setNewValue(req.getDestinationFQN());
				delta.setSource(req);
				deltas.add(delta);
			}

			String zEndType = dep.getZEndType().getFullyQualifiedName();
			req = mappedRequests.get(zEndType);
			if (req != null) {
				ModelChangeDelta delta = new ModelChangeDelta(
						IModelChangeDelta.SET);
				delta.setAffectedModelComponentURI((URI) artifact
						.getAdapter(URI.class));
				delta.setComponent(artifact);
				delta.setProject(artifact.getProject());
				delta.setFeature(IArtifactSetFeatureRequest.ZEND);
				delta.setOldValue(zEndType);
				delta.setNewValue(req.getDestinationFQN());
				delta.setSource(req);
				deltas.add(delta);
			}
		}

		return deltas;
	}

	public List<ModelChangeDelta> ASSOC_ENDS() throws TigerstripeException {
		List<ModelChangeDelta> deltas = new ArrayList<ModelChangeDelta>();

		if (artifact instanceof IAssociationArtifact) {
			IAssociationArtifact assoc = (IAssociationArtifact) artifact;
			String aEndType = assoc.getAEnd().getType().getFullyQualifiedName();
			ModelRefactorRequest req = mappedRequests.get(aEndType);
			if (req != null) {
				ModelChangeDelta delta = new ModelChangeDelta(
						IModelChangeDelta.SET);
				delta.setAffectedModelComponentURI((URI) artifact
						.getAdapter(URI.class));
				delta.setComponent(artifact);
				delta.setProject(artifact.getProject());
				delta.setFeature(IArtifactSetFeatureRequest.AEND);
				delta.setOldValue(aEndType);
				delta.setNewValue(req.getDestinationFQN());
				delta.setSource(req);
				deltas.add(delta);
			}

			String zEndType = assoc.getZEnd().getType().getFullyQualifiedName();
			req = mappedRequests.get(zEndType);
			if (req != null) {
				ModelChangeDelta delta = new ModelChangeDelta(
						IModelChangeDelta.SET);
				delta.setAffectedModelComponentURI((URI) artifact
						.getAdapter(URI.class));
				delta.setComponent(artifact);
				delta.setProject(artifact.getProject());
				delta.setFeature(IArtifactSetFeatureRequest.ZEND);
				delta.setOldValue(zEndType);
				delta.setNewValue(req.getDestinationFQN());
				delta.setSource(req);
				deltas.add(delta);
			}
		}

		return deltas;
	}

	public List<ModelChangeDelta> METHODS() throws TigerstripeException {
		List<ModelChangeDelta> deltas = new ArrayList<ModelChangeDelta>();

		for (IMethod method : artifact.getMethods()) {
			String fqn = method.getReturnType().getFullyQualifiedName();
			ModelRefactorRequest request = mappedRequests.get(fqn);
			if (request != null) {
				ModelChangeDelta delta = new ModelChangeDelta(
						IModelChangeDelta.SET);
				delta.setFeature(IMethodSetRequest.TYPE_FEATURE);
				delta.setAffectedModelComponentURI((URI) method
						.getAdapter(URI.class));
				delta.setComponent(method);
				delta.setProject(artifact.getProject());
				delta.setSource(request);
				delta.setOldValue(fqn);
				delta.setNewValue(request.getDestinationFQN());
				deltas.add(delta);
			}

			for (IArgument arg : method.getArguments()) {
				String argFQN = arg.getType().getFullyQualifiedName();
				ModelRefactorRequest argReq = mappedRequests.get(argFQN);
				if (argReq != null) {
					ModelChangeDelta delta = new ModelChangeDelta(
							IModelChangeDelta.SET);
					delta.setFeature(IMethodSetRequest.ARGTYPE_FEATURE);
					delta.setAffectedModelComponentURI(arg.toURI());
					delta.setComponent(arg);
					delta.setSource(argReq);
					delta.setOldValue(argFQN);
					delta.setNewValue(argReq.getDestinationFQN());
					deltas.add(delta);
				}
			}

			for (IException exp : method.getExceptions()) {
				String expFQN = exp.getFullyQualifiedName();
				ModelRefactorRequest expReq = mappedRequests.get(expFQN);
				if (expReq != null) {
					ModelChangeDelta delta = new ModelChangeDelta(
							IModelChangeDelta.SET);
					delta.setFeature(IMethodSetRequest.EXPTYPE_FEATURE);
					delta.setComponent(exp);
					delta.setSource(expReq);
					delta.setOldValue(expFQN);
					delta.setNewValue(expReq.getDestinationFQN());
					deltas.add(delta);
				}
			}
		}

		return deltas;
	}
}
