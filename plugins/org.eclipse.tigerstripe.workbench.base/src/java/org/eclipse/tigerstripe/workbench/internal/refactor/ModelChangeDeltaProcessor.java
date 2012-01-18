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
import java.util.Iterator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringChangesListener;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.annotation.ITigerstripeLazyObject;
import org.eclipse.tigerstripe.workbench.internal.annotation.PackageLazyObject;
import org.eclipse.tigerstripe.workbench.internal.annotation.TigerstripeLazyObject;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactRemoveFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodSetRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
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
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
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
public class ModelChangeDeltaProcessor {

	private static IAnnotationManager manager = AnnotationPlugin.getManager();

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
		IArgument rcArg = arg;
		if (toSave != null) {
			rcArg = getRefactoringComponent(arg, toSave);
		}
		if (IModelChangeDelta.SET == delta.getType()) {
			if (IMethodSetRequest.ARGTYPE_FEATURE.equals(delta.getFeature())) {
				rcArg.getType().setFullyQualifiedName(
						(String) delta.getNewValue());

				IAbstractArtifact parent = rcArg.getContainingMethod()
						.getContainingArtifact();
				if (toSave == null) {
					parent.doSave(null);
				} else {
					toSave.add(parent);
				}
			}
		}
	}

	protected static void processIExceptionChange(IException exp,
			IModelChangeDelta delta, Collection<Object> toCleanUp,
			Collection<IAbstractArtifact> toSave) throws TigerstripeException {
		IException rcExp = exp;
		if (toSave != null) {
			rcExp = getRefactoringComponent(exp, toSave);
		}

		if (IModelChangeDelta.SET == delta.getType()) {
			if (IMethodSetRequest.EXPTYPE_FEATURE.equals(delta.getFeature())) {
				rcExp.setFullyQualifiedName((String) delta.getNewValue());

				IAbstractArtifact parent = rcExp.getContainingMethod()
						.getContainingArtifact();
				if (toSave == null) {
					parent.doSave(null);
				} else {
					toSave.add(parent);
				}
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
					manager.fireChanged(oldObj, newObj,
							IRefactoringChangesListener.ABOUT_TO_CHANGE);

					IAbstractArtifact newOne = ((IAbstractArtifactInternal) artifact)
							.makeWorkingCopy(null);
					newOne.setFullyQualifiedName((String) delta.getNewValue());

					newOne.doSave(null);

					// propagate to annotations framework
					manager.fireChanged(oldObj, createLazyObject(newOne),
							IRefactoringChangesListener.CHANGED);

					String[] newPath = delta.getNewValue().toString().split("\\.");
					String[] oldPath = delta.getOldValue().toString().split("\\.");

					if (!isSubPakage(oldPath, newPath)) {
						IResource resource = (IResource) artifact .getAdapter(IResource.class);
						toCleanUp.add(resource);
						toCleanUp.add(resource.getParent());
					}
				} else {
					IAbstractArtifact rcArtifact = getRefactoringComponent(
							artifact, toSave, false);
					if (rcArtifact == null) {
						rcArtifact = artifact;
					}

					// renaming an artifact here
					IResource res = (IResource) rcArtifact
							.getAdapter(IResource.class);
					IArtifactManagerSession session = rcArtifact.getProject()
							.getArtifactManagerSession();

					// propagate to annotations framework
					manager.fireChanged(oldObj, newObj,
							IRefactoringChangesListener.ABOUT_TO_CHANGE);

					session.renameArtifact(rcArtifact,
							(String) delta.getNewValue());

					IModelComponent container = artifact
							.getContainingModelComponent();
					if (container instanceof IPackageArtifact) {
						((IAbstractArtifactInternal) container)
								.removeContainedModelComponent(artifact);
					}

					if (toSave == null) {
						rcArtifact.doSave(null);
					} else {
						toSave.add(rcArtifact);
					}

					// propagate to annotations framework
					manager.fireChanged(oldObj, createLazyObject(rcArtifact),
							IRefactoringChangesListener.CHANGED);

					if (res != null) {
						try {
							res.delete(true, null);
						} catch (CoreException e) {
							BasePlugin.log(e);
						}
					}
				}
			} else {
				IAbstractArtifact rcArtifact = getRefactoringComponent(
						artifact, toSave);
				if (IArtifactSetFeatureRequest.EXTENDS_FEATURE.equals(delta
						.getFeature())) {
					rcArtifact
							.setExtendedArtifact((String) delta.getNewValue());
					if (toSave == null)
						rcArtifact.doSave(null);
					else
						toSave.add(rcArtifact);
				} else if (IArtifactSetFeatureRequest.RETURNED_TYPE
						.equals(delta.getFeature())) {
					IQueryArtifact query = (IQueryArtifact) rcArtifact;
					IType type = query.makeType();
					type.setFullyQualifiedName((String) delta.getNewValue());
					query.setReturnedType(type);
					if (toSave == null)
						query.doSave(null);
					else
						toSave.add(query);
				} else if (IArtifactSetFeatureRequest.AEND.equals(delta
						.getFeature())) {
					if (rcArtifact instanceof IAssociationArtifact) {
						IAssociationArtifact assoc = (IAssociationArtifact) rcArtifact;
						IType type = assoc.getAEnd().getType();
						type.setFullyQualifiedName((String) delta.getNewValue());
						if (toSave == null)
							rcArtifact.doSave(null);
						else
							toSave.add(rcArtifact);
					} else if (rcArtifact instanceof IDependencyArtifact) {
						IDependencyArtifact assoc = (IDependencyArtifact) rcArtifact;
						IType type = assoc.getAEndType();
						type.setFullyQualifiedName((String) delta.getNewValue());
						if (toSave == null)
							rcArtifact.doSave(null);
						else
							toSave.add(rcArtifact);
					}
				} else if (IArtifactSetFeatureRequest.ZEND.equals(delta
						.getFeature())) {
					if (rcArtifact instanceof IAssociationArtifact) {
						IAssociationArtifact assoc = (IAssociationArtifact) rcArtifact;
						IType type = assoc.getZEnd().getType();
						type.setFullyQualifiedName((String) delta.getNewValue());
						if (toSave == null)
							rcArtifact.doSave(null);
						else
							toSave.add(rcArtifact);
					} else if (rcArtifact instanceof IDependencyArtifact) {
						IDependencyArtifact assoc = (IDependencyArtifact) rcArtifact;
						IType type = assoc.getZEndType();
						type.setFullyQualifiedName((String) delta.getNewValue());
						if (toSave == null)
							rcArtifact.doSave(null);
						else
							toSave.add(rcArtifact);
					}
				} else {
					Status status = new Status(IStatus.ERROR,
							BasePlugin.PLUGIN_ID,
							"Unsupported refactor delta: " + delta);
					BasePlugin.log(status);
				}
			}
		} else if (IModelChangeDelta.ADD == delta.getType()) {
			if (IArtifactRemoveFeatureRequest.IMPLEMENTS_FEATURE.equals(delta
					.getFeature())) {
				IAbstractArtifact rcArtifact = getRefactoringComponent(
						artifact, toSave);
				IAbstractArtifact newImpl = rcArtifact
						.getProject()
						.getArtifactManagerSession()
						.getArtifactByFullyQualifiedName(
								(String) delta.getNewValue());
				if (newImpl == null) {
					// Make a dummy one
					newImpl = rcArtifact.getProject()
							.getArtifactManagerSession()
							.makeArtifact(ISessionArtifact.class.getName());
					newImpl.setFullyQualifiedName((String) delta.getNewValue());

				}
				Collection<IAbstractArtifact> implemented = new ArrayList<IAbstractArtifact>();
				implemented.addAll(rcArtifact.getImplementedArtifacts());
				implemented.add(newImpl);
				rcArtifact.setImplementedArtifacts(implemented);

				if (toSave == null) {
					rcArtifact.doSave(null);
				} else {
					toSave.add(rcArtifact);
				}
			}

		} else if (IModelChangeDelta.REMOVE == delta.getType()) {
			if (IArtifactRemoveFeatureRequest.IMPLEMENTS_FEATURE.equals(delta
					.getFeature())) {
				IAbstractArtifact rcArtifact = getRefactoringComponent(
						artifact, toSave);

				Collection<IAbstractArtifact> implemented = new ArrayList<IAbstractArtifact>();
				implemented.addAll(rcArtifact.getImplementedArtifacts());
				for (Iterator<IAbstractArtifact> it = implemented.iterator(); it
						.hasNext();) {
					IAbstractArtifact implArtifact = it.next();
					if (implArtifact.getFullyQualifiedName().equals(
							delta.getOldValue())) {
						it.remove();
						break;
					}
				}
				rcArtifact.setImplementedArtifacts(implemented);

				if (toSave == null) {
					rcArtifact.doSave(null);
				} else {
					toSave.add(rcArtifact);
				}
			}
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
			manager.fireChanged(oldObj, newObj,
					IRefactoringChangesListener.ABOUT_TO_CHANGE);

			IAbstractArtifact rcArtifact = getRefactoringComponent(artifact,
					toSave, false);
			if (rcArtifact == null) {
				rcArtifact = artifact;
			}
			IResource res = (IResource) rcArtifact.getAdapter(IResource.class);
			rcArtifact.getProject().getArtifactManagerSession()
					.renameArtifact(rcArtifact, (String) delta.getNewValue());
			if (toSave == null)
				rcArtifact.doSave(null);
			else
				toSave.add(rcArtifact);

			IAbstractArtifact newOne = ((IAbstractArtifactInternal) rcArtifact)
					.makeWorkingCopy(null);
			delta.getProject().getArtifactManagerSession().addArtifact(newOne);

			if (toSave == null)
				newOne.doSave(null);
			else
				toSave.add(newOne);

			// propagate to annotations framework
			manager.fireChanged(oldObj, newObj,
					IRefactoringChangesListener.CHANGED);

			if (rcArtifact instanceof IPackageArtifact) {
				if (needToCleanUpPackage(project,
						(IPackageArtifact) rcArtifact, toCleanUp)) {
					toCleanUp.add(rcArtifact);
				}
			} else {
				toCleanUp.add(res);
				toCleanUp.add(rcArtifact);
			}
			if (topPackageToDelete != null) {
				IProject proj = (IProject) rcArtifact.getTigerstripeProject()
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
		try {
			IResource packageResource = (IResource) packageArtifact
					.getAdapter(IResource.class);
			if (packageResource != null) {
				IContainer packageContainer = packageResource.getParent();
				for (IResource resource : packageContainer.members()) {
					boolean inCleanup = false;
					if (resource.equals(packageResource)) {
						inCleanup = true;
					} else {
						for (Object cleanupObject : toCleanUp) {
							if (cleanupObject instanceof IAdaptable) {
								IResource cleanupResource = (IResource) ((IAdaptable) cleanupObject)
										.getAdapter(IResource.class);
								if (cleanupResource != null
										&& resource.equals(cleanupResource)) {
									inCleanup = true;
									break;
								}
							}
						}
					}
					if (!inCleanup) {
						return false;
					}
				}
			}
		} catch (CoreException e) {
			throw new TigerstripeException(e.getMessage(), e);
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
		if (artifact instanceof IPackageArtifact) {
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
		IMethod rcMethod = method;
		if (toSave != null) {
			rcMethod = getRefactoringComponent(method, toSave);
		}

		if (IMethodSetRequest.TYPE_FEATURE.equals(delta.getFeature())) {
			IType returnType = rcMethod.getReturnType();
			returnType.setFullyQualifiedName((String) delta.getNewValue());

			IAbstractArtifact parent = (IAbstractArtifact) rcMethod
					.getContainingModelComponent();
			if (toSave == null) {
				parent.doSave(null);
			} else {
				toSave.add(parent);
			}
		}
	}

	protected static void processIFieldChange(IField field,
			IModelChangeDelta delta, Collection<IAbstractArtifact> toSave)
			throws TigerstripeException {
		IField rcField = field;
		if (toSave != null) {
			rcField = getRefactoringComponent(field, toSave);
		}

		if (IAttributeSetRequest.NAME_FEATURE.equals(delta.getFeature())) {
			rcField.setName((String) delta.getNewValue());
		} else if (IAttributeSetRequest.TYPE_FEATURE.equals(delta.getFeature())) {
			IType type = rcField.getType();
			type.setFullyQualifiedName((String) delta.getNewValue());
		} else {
			throw new UnsupportedOperationException(
					"Can't apply delta for IField " + delta);
		}

		IAbstractArtifact parent = (IAbstractArtifact) rcField
				.getContainingModelComponent();
		if (toSave == null) {
			parent.doSave(null);
		} else {
			toSave.add(parent);
		}
	}

	private static IField getRefactoringComponent(IField field,
			Collection<IAbstractArtifact> toSave) {
		IField result = field;
		IAbstractArtifact rcArtifact = getRefactoringComponent(
				(IAbstractArtifact) field.getContainingModelComponent(), toSave);
		for (IField rcField : rcArtifact.getFields()) {
			if (field.getName().equals(rcField.getName())) {
				result = rcField;
				break;
			}
		}
		return result;
	}

	private static IMethod getRefactoringComponent(IMethod method,
			Collection<IAbstractArtifact> toSave) {
		IMethod result = method;
		IAbstractArtifact rcArtifact = getRefactoringComponent(
				(IAbstractArtifact) method.getContainingModelComponent(),
				toSave);
		for (IMethod rcMethod : rcArtifact.getMethods()) {
			if (method.getName().equals(rcMethod.getName())) {
				result = rcMethod;
				break;
			}
		}
		return result;
	}

	private static IArgument getRefactoringComponent(IArgument arg,
			Collection<IAbstractArtifact> toSave) {
		IArgument result = arg;
		IAbstractArtifact rcArtifact = getRefactoringComponent(
				(IAbstractArtifact) arg.getContainingMethod()
						.getContainingModelComponent(), toSave);
		for (IMethod rcMethod : rcArtifact.getMethods()) {
			if (arg.getContainingMethod().getName().equals(rcMethod.getName())) {
				for (IArgument rcArg : rcMethod.getArguments()) {
					if (arg.getName().equals(rcArg.getName())) {
						result = rcArg;
						break;
					}
				}
			}
		}
		return result;
	}

	private static IException getRefactoringComponent(IException exp,
			Collection<IAbstractArtifact> toSave) {
		IException result = exp;
		IMethod rcMethod = getRefactoringComponent(exp.getContainingMethod(),
				toSave);
		for (IException rcExp : rcMethod.getExceptions()) {
			if (exp.getFullyQualifiedName().equals(
					rcExp.getFullyQualifiedName())) {
				result = rcExp;
				break;
			}
		}
		return result;
	}

	private static IAbstractArtifact getRefactoringComponent(
			IAbstractArtifact artifact, Collection<IAbstractArtifact> toSave) {
		return getRefactoringComponent(artifact, toSave, true);
	}

	private static IAbstractArtifact getRefactoringComponent(
			IAbstractArtifact artifact, Collection<IAbstractArtifact> toSave,
			boolean create) {
		IAbstractArtifact result = null;
		for (IAbstractArtifact wc : toSave) {
			if (artifact.getFullyQualifiedName().equals(
					wc.getFullyQualifiedName())) {
				result = wc;
				break;
			}
		}

		if (result == null && create) {
			try {
				result = artifact.makeWorkingCopy(new NullProgressMonitor());
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			}
		}
		return result;
	}
}
