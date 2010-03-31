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

import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IRefactoringSupport;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodSetRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;

/**
 * This is a helper class that is used to delegate the "apply()" method of
 * {@link IModelChangeDelta}.
 * 
 * This will handle all "refactor" model change deltas.
 * 
 * Note that for each delta, the change will be propagated directly to the
 * Annotations framework to ensure the annotations are kept up2date.
 * 
 * @author erdillon
 * 
 */
@SuppressWarnings("deprecation")
public class ModelChangeDeltaProcessor {

	private static IRefactoringSupport refactor = AnnotationPlugin.getManager()
			.getRefactoringSupport();

	public static void processModelChangeDelta(ModelChangeDelta delta,
			Collection<Object> toCleanUp, Collection<IAbstractArtifact> toSave)
			throws TigerstripeException {
		Object component = delta.getComponent();
		if (component instanceof IField) {
			processIFieldChange((IField) component, delta, toSave);
		} else if (component instanceof IMethod) {
			processIMethodChange((IMethod) component, delta, toSave);
		} else if (component instanceof IAbstractArtifact) {
			processIAbstractArtifactChange((IAbstractArtifact) component,
					delta, toCleanUp, toSave);
		} else if (component instanceof IArgument) {
			processIArgumentChange((IArgument) component, delta, toCleanUp,
					toSave);
		} else if (component instanceof IException) {
			processIExceptionChange((IException) component, delta, toCleanUp,
					toSave);
		}
	}

	protected static void processIArgumentChange(IArgument arg,
			IModelChangeDelta delta, Collection<Object> toCleanUp,
			Collection<IAbstractArtifact> toSave) throws TigerstripeException {
		if (IModelChangeDelta.SET == delta.getType()) {
			if (IMethodSetRequest.ARGTYPE_FEATURE.equals(delta.getFeature())) {
				arg.getType().setFullyQualifiedName(
						(String) delta.getNewValue());
				if (toSave == null)
					arg.getContainingMethod().getContainingArtifact().doSave(
							null);
				else
					toSave.add(arg.getContainingMethod()
							.getContainingArtifact());
			}
		}
	}

	protected static void processIExceptionChange(IException exp,
			IModelChangeDelta delta, Collection<Object> toCleanUp,
			Collection<IAbstractArtifact> toSave) throws TigerstripeException {
		if (IModelChangeDelta.SET == delta.getType()) {
			if (IMethodSetRequest.EXPTYPE_FEATURE.equals(delta.getFeature())) {
				exp.setFullyQualifiedName((String) delta.getNewValue());
				if (toSave == null)
					exp.getContainingMethod().getContainingArtifact().doSave(
							null);
				else
					toSave.add(exp.getContainingMethod()
							.getContainingArtifact());
			}
		}
	}

	protected static void processIAbstractArtifactChange(
			IAbstractArtifact artifact, IModelChangeDelta delta,
			Collection<Object> toCleanUp, Collection<IAbstractArtifact> toSave)
			throws TigerstripeException {
		if (IModelChangeDelta.SET == delta.getType()) {
			if ("fqn".equals(delta.getFeature())) {
				if (artifact instanceof IPackageArtifact) {
					// renaming a package here, let's handle this with care
					// Let's create the new package, but leave the old one to be
					// cleaned up later to avoid deleting anything we don't want
					// to.
					IAbstractArtifact newOne = ((AbstractArtifact) artifact)
							.makeWorkingCopy(null);
					newOne.setFullyQualifiedName((String) delta.getNewValue());
					if (toSave == null)
						newOne.doSave(null);
					else
						toSave.add(newOne);

					// propagate to annotations framework
					URI oldUri = (URI) artifact.getAdapter(URI.class);
					URI newUri = (URI) newOne.getAdapter(URI.class);
					refactor.changed(oldUri, newUri, true);

					toCleanUp.add(artifact);
				} else {
					// renaming an artifact here
					URI oldUri = (URI) artifact.getAdapter(URI.class);
					IResource res = (IResource) artifact
							.getAdapter(IResource.class);
					IArtifactManagerSession session = artifact.getProject()
							.getArtifactManagerSession();
					session.renameArtifact(artifact, (String) delta
							.getNewValue());
					if (toSave == null)
						artifact.doSave(null);
					else
						toSave.add(artifact);

					// propagate to annotations framework
					URI newUri = (URI) artifact.getAdapter(URI.class);
					refactor.changed(oldUri, newUri, true);

					toCleanUp.add(res);
				}
			} else if (ArtifactSetFeatureRequest.EXTENDS_FEATURE.equals(delta
					.getFeature())) {
				artifact.setExtendedArtifact((String) delta.getNewValue());
				if (toSave == null)
					artifact.doSave(null);
				else
					toSave.add(artifact);
			} else if (ArtifactSetFeatureRequest.RETURNED_TYPE.equals(delta
					.getFeature())) {
				IQueryArtifact query = (IQueryArtifact) artifact;
				IType type = query.makeType();
				type.setFullyQualifiedName((String) delta.getNewValue());
				query.setReturnedType(type);
				if (toSave == null)
					query.doSave(null);
				else
					toSave.add(query);
			} else if (ArtifactSetFeatureRequest.AEND
					.equals(delta.getFeature())) {
				if (artifact instanceof IAssociationArtifact) {
					IAssociationArtifact assoc = (IAssociationArtifact) artifact;
					IType type = assoc.getAEnd().getType();
					type.setFullyQualifiedName((String) delta.getNewValue());
					if (toSave == null)
						artifact.doSave(null);
					else
						toSave.add(artifact);
				} else if (artifact instanceof IDependencyArtifact) {
					IDependencyArtifact assoc = (IDependencyArtifact) artifact;
					IType type = assoc.getAEndType();
					type.setFullyQualifiedName((String) delta.getNewValue());
					if (toSave == null)
						artifact.doSave(null);
					else
						toSave.add(artifact);
				}
			} else if (ArtifactSetFeatureRequest.ZEND
					.equals(delta.getFeature())) {
				if (artifact instanceof IAssociationArtifact) {
					IAssociationArtifact assoc = (IAssociationArtifact) artifact;
					IType type = assoc.getZEnd().getType();
					type.setFullyQualifiedName((String) delta.getNewValue());
					if (toSave == null)
						artifact.doSave(null);
					else
						toSave.add(artifact);
				} else if (artifact instanceof IDependencyArtifact) {
					IDependencyArtifact assoc = (IDependencyArtifact) artifact;
					IType type = assoc.getZEndType();
					type.setFullyQualifiedName((String) delta.getNewValue());
					if (toSave == null)
						artifact.doSave(null);
					else
						toSave.add(artifact);
				}
			} else {
				Status status = new Status(IStatus.ERROR, BasePlugin.PLUGIN_ID,
						"Unsupported refactor delta: " + delta);
				BasePlugin.log(status);
			}
		} else if (IModelChangeDelta.ADD == delta.getType()) {
			if (toSave == null)
				artifact.doSave(null);
			else
				toSave.add(artifact);
		} else if (IModelChangeDelta.REMOVE == delta.getType()) {
			if (toSave == null)
				artifact.doSave(null);
			else
				toSave.add(artifact);
		} else if (IModelChangeDelta.MOVE == delta.getType()) {
			// In order for the diagrams to be updated properly before the move
			// we need to rename the artifact to the target location in the
			// source
			// project to its target name in the target project, and then
			// create the artifact in the target project.
			// the renamed ones shall then be cleaned up.

			// When a package gets moved, it first gets move within the local
			// project
			// to maintain coherent diagrams. The side effect is that all
			// packages need to be
			// deleted after the fact.
			String topPackageToDelete = null;
			if (artifact instanceof IPackageArtifact) {
				String newValue = (String) delta.getNewValue();
				String[] segments = newValue.split("\\.");
				for (int i = 0; i < segments.length; i++) {
					String fqn = "";
					for (int j = 0; j <= i; j++) {
						if (fqn.length() != 0)
							fqn = fqn + ".";
						fqn = fqn + segments[j];
					}
					try {
						IAbstractArtifact pack = artifact.getProject()
								.getArtifactManagerSession()
								.getArtifactByFullyQualifiedName(fqn);
						if (pack == null) {
							topPackageToDelete = fqn;
							break;
						}
					} catch (TigerstripeException e) {
						// ignore
					}
				}
			}

			// That way diagrams are updated with the new names before they are
			// moved
			IResource res = (IResource) artifact.getAdapter(IResource.class);
			artifact.getProject().getArtifactManagerSession().renameArtifact(
					artifact, (String) delta.getNewValue());
			if (toSave == null)
				artifact.doSave(null);
			else
				toSave.add(artifact);

			IAbstractArtifact newOne = ((AbstractArtifact) artifact)
					.makeWorkingCopy(null);
			delta.getProject().getArtifactManagerSession().addArtifact(newOne);

			if (toSave == null)
				newOne.doSave(null);
			else
				toSave.add(newOne);

			// propagate to annotations framework
			URI oldUri = (URI) artifact.getAdapter(URI.class);
			URI newUri = (URI) newOne.getAdapter(URI.class);
			refactor.changed(oldUri, newUri, true);

			toCleanUp.add(res);
			toCleanUp.add(artifact);
			if (topPackageToDelete != null) {
				IProject proj = (IProject) artifact.getTigerstripeProject()
						.getAdapter(IProject.class);
				String ff = "src/" + topPackageToDelete.replace(".", "/");
				IPath path = proj.getFullPath().append(ff);
				toCleanUp.add(path);
			}
		}
	}

	protected static void processIMethodChange(IMethod method,
			IModelChangeDelta delta, Collection<IAbstractArtifact> toSave)
			throws TigerstripeException {
		if (IMethodSetRequest.TYPE_FEATURE.equals(delta.getFeature())) {
			IType returnType = method.getReturnType();
			returnType.setFullyQualifiedName((String) delta.getNewValue());
			if (toSave == null)
				method.getContainingArtifact().doSave(null);
			else
				toSave.add(method.getContainingArtifact());
		}
	}

	protected static void processIFieldChange(IField field,
			IModelChangeDelta delta, Collection<IAbstractArtifact> toSave)
			throws TigerstripeException {
		if (IAttributeSetRequest.NAME_FEATURE.equals(delta.getFeature())) {
			field.setName((String) delta.getNewValue());
		} else if (IAttributeSetRequest.TYPE_FEATURE.equals(delta.getFeature())) {
			IType type = field.getType();
			type.setFullyQualifiedName((String) delta.getNewValue());
		} else {
			throw new UnsupportedOperationException(
					"Can't apply delta for IField " + delta);
		}

		IAbstractArtifact parent = (IAbstractArtifact) field
				.getContainingModelComponent();
		if (toSave == null)
			parent.doSave(null);
		else
			toSave.add(parent);
	}
}
