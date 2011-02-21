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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.metamodel.impl.IDependencyArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;

public class DependencyArtifactAuditor extends AbstractArtifactAuditor {

	public void run(IProgressMonitor monitor) {

		IDependencyArtifact artifact = (IDependencyArtifact) getArtifact();
		boolean aEndDefined = false;
		boolean zEndDefined = false;

		IType aEndType = artifact.getAEndType();
		if (aEndType == null || aEndType.getFullyQualifiedName().length() == 0) {
			TigerstripeProjectAuditor.reportError(
					"Undefined "
							+ ArtifactMetadataFactory.INSTANCE.getMetadata(
									IDependencyArtifactImpl.class.getName())
									.getLabel(artifact) + " end (aEnd) in '"
							+ artifact.getName() + "'.", getIResource(), 222);
		} else
			aEndDefined = true;

		IType zEndType = artifact.getZEndType();
		if (zEndType == null || zEndType.getFullyQualifiedName().length() == 0) {
			TigerstripeProjectAuditor.reportError(
					"Undefined "
							+ ArtifactMetadataFactory.INSTANCE.getMetadata(
									IDependencyArtifactImpl.class.getName())
									.getLabel(artifact) + " end (zEnd) in '"
							+ artifact.getName() + "'.", getIResource(), 222);
		} else
			zEndDefined = true;
		if (aEndDefined && zEndDefined) {
			checkSuitableEndTypes(aEndType, zEndType); // Bug 249966
		}
	}

	/**
	 * Check the the end types are defined as "suitable" types for the ends Pass
	 * in the types as we have already extracted them.
	 */
	protected void checkSuitableEndTypes(IType aEndType, IType zEndType) {
		if (!DependencyArtifact.isSuitableType(aEndType)) {
			String typeLabel = "";
			if (aEndType.isArtifact()) {
				typeLabel = aEndType.getArtifact().getLabel();
			} else {
				typeLabel = aEndType.getFullyQualifiedName();
			}

			TigerstripeProjectAuditor.reportError(
					"The A End must be of a suitable Type. Dependency Ends may not be of type '"
							+ typeLabel + "'.", getIResource(), 222);
		}
		if (!DependencyArtifact.isSuitableType(zEndType)) {
			String typeLabel = "";
			if (zEndType.isArtifact()) {
				typeLabel = zEndType.getArtifact().getLabel();
			} else {
				typeLabel = zEndType.getFullyQualifiedName();
			}

			TigerstripeProjectAuditor.reportError(
					"The Z End must be of a suitable Type. Dependency Ends may not be of type '"
							+ typeLabel + "'.", getIResource(), 222);
		}
	}

}
