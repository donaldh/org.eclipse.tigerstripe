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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.metamodel.impl.IDependencyArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;

public class DependencyArtifactAuditor extends AbstractArtifactAuditor
		implements IArtifactAuditor {

	public DependencyArtifactAuditor(IProject project,
			IAbstractArtifact artifact) {
		super(project, artifact);
	}

	@Override
	public void run(IProgressMonitor monitor) {
		super.run(monitor);

		IDependencyArtifact artifact = (IDependencyArtifact) getArtifact();

		IType aEndType = artifact.getAEndType();
		if (aEndType == null || aEndType.getFullyQualifiedName().length() == 0) {
			TigerstripeProjectAuditor.reportError("Undefined "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IDependencyArtifactImpl.class.getName()).getLabel()
					+ " end (aEnd) in '" + artifact.getName() + "'.",
					getIResource(), 222);
		}

		IType zEndType = artifact.getZEndType();
		if (zEndType == null || zEndType.getFullyQualifiedName().length() == 0) {
			TigerstripeProjectAuditor.reportError("Undefined "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IDependencyArtifactImpl.class.getName()).getLabel()
					+ " end (zEnd) in '" + artifact.getName() + "'.",
					getIResource(), 222);
		}

	}

}
