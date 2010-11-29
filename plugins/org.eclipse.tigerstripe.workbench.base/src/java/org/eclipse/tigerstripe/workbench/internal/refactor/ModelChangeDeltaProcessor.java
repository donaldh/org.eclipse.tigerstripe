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
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringChangesListener;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringNotifier;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.annotation.ITigerstripeLazyObject;
import org.eclipse.tigerstripe.workbench.internal.annotation.PackageLazyObject;
import org.eclipse.tigerstripe.workbench.internal.annotation.TigerstripeLazyObject;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactFQRenameRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactRenameRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodSetRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.internal.core.model.PackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

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

	private static IRefactoringNotifier refactor = AnnotationPlugin
			.getRefactoringNotifier();

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
					arg.getContainingMethod().getContainingArtifact()
							.doSave(null);
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
					exp.getContainingMethod().getContainingArtifact()
							.doSave(null);
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
		ITigerstripeModelProject project = artifact.getProject();
		ITigerstripeLazyObject oldObj = createLazyObject(artifact);
		ITigerstripeLazyObject newObj = createLazyObject(project,
				(String) delta.getNewValue(), oldObj.isPackage());
		if (IModelChangeDelta.SET == delta.getType()) {
			if ("fqn".equals(delta.getFeature())) {
				if (artifact instanceof IPackageArtifact) {
					// renaming a package here, let's handle this with care
					// Let's create the new package, but leave the old one to be
					// cleaned up later to avoid deleting anything we don't want
					// to.

					// propagate to annotations framework
					refactor.fireChanged(oldObj, newObj,
							IRefactoringChangesListener.ABOUT_TO_CHANGE);

					IAbstractArtifact newOne = ((AbstractArtifact) artifact)
							.makeWorkingCopy(null);
					newOne.setFullyQualifiedName((String) delta.getNewValue());

					newOne.doSave(null);

					// propagate to annotations framework
					refactor.fireChanged(oldObj, createLazyObject(newOne),
							IRefactoringChangesListener.CHANGED);

					String[] newPath = delta.getNewValue().toString()
							.split("\\.");
					String[] oldPath = delta.getOldValue().toString()
							.split("\\.");

					if (!isSubPakage(oldPath, newPath)) {
						IPackageArtifact orphan = (IPackageArtifact) artifact;
						if (needToCleanUpPackage(project, orphan, toCleanUp)) {
							for (;;) {
								IModelComponent parentComponent = orphan
										.getContainingModelComponent();
								if (parentComponent instanceof IPackageArtifact) {
									IPackageArtifact parent = (IPackageArtifact) parentComponent;
									Collection<IModelComponent> contained = parent
											.getContainedModelComponents();
									if (contained.size() == 1) {
										IModelComponent first = contained
												.iterator().next();
										if (orphan.equals(first)) {
											orphan = parent;
											continue;
										}
									}
								}
								break;
							}
							toCleanUp.add(orphan);
						}
					}
				} else {
					// renaming an artifact here
					IResource res = (IResource) artifact
							.getAdapter(IResource.class);
					IArtifactManagerSession mgr = artifact.getProject()
							.getArtifactManagerSession();
					String newFqn = (String) delta.getNewValue();
					String oldPackage = delta
							.getOldValue()
							.toString()
							.substring(
									0,
									delta.getOldValue().toString()
											.lastIndexOf('.'));
					String newPackage = newFqn.substring(0,
							newFqn.lastIndexOf('.'));

					// propagate to annotations framework
					refactor.fireChanged(oldObj, newObj,
							IRefactoringChangesListener.ABOUT_TO_CHANGE);

					// Bug 327698 - Need to make rename requests based on if it
					// is in the same package,
					// or being moved to a different package.
					IModelUpdater updater = mgr.getIModelUpdater();
					if (oldPackage.equals(newPackage)) {
						// Renaming in same package
						ArtifactRenameRequest request = (ArtifactRenameRequest) updater
								.getRequestFactory()
								.makeRequest(
										IModelChangeRequestFactory.ARTIFACT_RENAME);
						request.setArtifactFQN(artifact.getFullyQualifiedName());
						request.setNewName(newFqn.substring(newFqn
								.lastIndexOf(".") + 1));
						request.execute(mgr);
					} else {
						// Moving to a different package
						ArtifactFQRenameRequest request = (ArtifactFQRenameRequest) updater
								.getRequestFactory()
								.makeRequest(
										IModelChangeRequestFactory.ARTIFACT_FQRENAME);
						request.setArtifactFQN(artifact.getFullyQualifiedName());
						request.setNewFQName(newFqn);
						request.execute(mgr);
					}

					// propagate to annotations framework
					refactor.fireChanged(oldObj, createLazyObject(artifact),
							IRefactoringChangesListener.CHANGED);

					toCleanUp.add(res);
				}
			} else if (IArtifactSetFeatureRequest.EXTENDS_FEATURE.equals(delta
					.getFeature())) {
				artifact.setExtendedArtifact((String) delta.getNewValue());
				if (toSave == null)
					artifact.doSave(null);
				else
					toSave.add(artifact);
			} else if (IArtifactSetFeatureRequest.RETURNED_TYPE.equals(delta
					.getFeature())) {
				IQueryArtifact query = (IQueryArtifact) artifact;
				IType type = query.makeType();
				type.setFullyQualifiedName((String) delta.getNewValue());
				query.setReturnedType(type);
				if (toSave == null)
					query.doSave(null);
				else
					toSave.add(query);
			} else if (IArtifactSetFeatureRequest.AEND.equals(delta
					.getFeature())) {
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
			} else if (IArtifactSetFeatureRequest.ZEND.equals(delta
					.getFeature())) {
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

			// propagate to annotations framework
			refactor.fireChanged(oldObj, newObj,
					IRefactoringChangesListener.ABOUT_TO_CHANGE);

			IResource res = (IResource) artifact.getAdapter(IResource.class);
			artifact.getProject().getArtifactManagerSession()
					.renameArtifact(artifact, (String) delta.getNewValue());
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
			refactor.fireChanged(oldObj, newObj,
					IRefactoringChangesListener.CHANGED);

			if (artifact instanceof IPackageArtifact) {
				if (needToCleanUpPackage(project, (IPackageArtifact) artifact,
						toCleanUp)) {
					toCleanUp.add(artifact);
				}
			} else {
				toCleanUp.add(res);
				toCleanUp.add(artifact);
			}
			if (topPackageToDelete != null) {
				IProject proj = (IProject) artifact.getTigerstripeProject()
						.getAdapter(IProject.class);
				String ff = "src/" + topPackageToDelete.replace(".", "/");
				IPath path = proj.getFullPath().append(ff);
				toCleanUp.add(path);
			}
		}
	}

	private static boolean needToCleanUpPackage(
			ITigerstripeModelProject project, IPackageArtifact packageArtifact,
			Collection<Object> toCleanUp) throws TigerstripeException {
		Collection<IModelComponent> components = packageArtifact
				.getContainedModelComponents();
		for (IModelComponent component : components) {
			if (component.getProject().equals(project)
					&& !toCleanUp.contains(component)) {
				return false;
			}
		}
		return true;
	}

	private static boolean isSubPakage(String[] subPkg, String[] path) {
		if (path.length < subPkg.length) {
			return false;
		}
		for (int i = 0; i < subPkg.length; ++i) {
			if (!path[i].equals(subPkg[i])) {
				return false;
			}
		}
		return true;
	}

	private static ITigerstripeLazyObject createLazyObject(
			IAbstractArtifact artifact) throws TigerstripeException {
		ITigerstripeModelProject project = artifact.getProject();
		if (artifact instanceof PackageArtifact) {
			return new PackageLazyObject(project,
					artifact.getFullyQualifiedName());
		}
		return new TigerstripeLazyObject(project,
				artifact.getFullyQualifiedName());
	}

	private static ITigerstripeLazyObject createLazyObject(
			ITigerstripeModelProject project, String newValue, boolean isPackage) {
		if (isPackage) {
			return new PackageLazyObject(project, newValue);
		}
		return new TigerstripeLazyObject(project, newValue);
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
