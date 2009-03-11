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

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeSetRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;

/**
 * This is a helper class that is used to delegate the "apply()" method of
 * {@link IModelChangeDelta}.
 * 
 * @author erdillon
 * 
 */
public class ModelChangeDeltaProcessor {

	public static void processModelChangeDelta(IModelChangeDelta delta,
			Collection<Object> toCleanUp) throws TigerstripeException {
		System.out.println("Processing delta:" + delta);
		URI componentURI = delta.getAffectedModelComponentURI();
		IModelComponent component = TigerstripeURIAdapterFactory
				.uriToComponent(componentURI);
		if (component instanceof IField) {
			processIFieldChange((IField) component, delta);
		} else if (component instanceof IAbstractArtifact) {
			processIAbstractArtifactChange((IAbstractArtifact) component,
					delta, toCleanUp);
		} else {
			System.out.println("Unsupported delta " + delta);
		}
	}

	protected static void processIAbstractArtifactChange(
			IAbstractArtifact artifact, IModelChangeDelta delta,
			Collection<Object> toCleanUp) throws TigerstripeException {
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
					newOne.doSave(null);
					toCleanUp.add(artifact);
				} else {
					// renaming an artifact here
					IResource res = (IResource) artifact.getAdapter(IResource.class);
					IArtifactManagerSession session = artifact.getProject()
							.getArtifactManagerSession();
					session.renameArtifact(artifact, (String) delta
							.getNewValue());
					artifact.doSave(null);
					toCleanUp.add(res);
				}
			}
		} else if (IModelChangeDelta.ADD == delta.getType()) {

			artifact.doSave(null);
		} else if (IModelChangeDelta.REMOVE == delta.getType()) {

			artifact.doSave(null);
		}
	}

	protected static void processIFieldChange(IField field,
			IModelChangeDelta delta) throws TigerstripeException {
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
		parent.doSave(null);
	}
}
