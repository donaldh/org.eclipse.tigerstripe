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
package org.eclipse.tigerstripe.workbench.ui.eclipse.builder;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj.IOssjQuerySpecifics;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IQueryArtifact;

public class QueryArtifactAuditor extends AbstractArtifactAuditor implements
		IArtifactAuditor {

	public QueryArtifactAuditor(IProject project, IAbstractArtifact artifact) {
		super(project, artifact);
	}

	@Override
	public void run(IProgressMonitor monitor) {
		super.run(monitor);

		IQueryArtifact artifact = (IQueryArtifact) getArtifact();
		IOssjQuerySpecifics specifics = (IOssjQuerySpecifics) artifact
				.getIStandardSpecifics();
		IType returnedType = specifics.getReturnedEntityIType();
		if (returnedType == null
				|| returnedType.getFullyQualifiedName().length() == 0) {
			TigerstripeProjectAuditor.reportError(
					"Undefined returned entity type referenced in '"
							+ artifact.getName() + "'.", getIResource(), 222);
		} else {
			// As of 1.0.6 we allow both Datatype and Entity type to be returned
			String fqn = returnedType.getFullyQualifiedName();
			try {
				IArtifactManagerSession session = artifact.getTigerstripeProject()
						.getArtifactManagerSession();
				IAbstractArtifact returnType = session
						.getArtifactByFullyQualifiedName(fqn);
				if (returnType == null) {
					TigerstripeProjectAuditor.reportError(
							"Returned entity type '" + fqn
									+ "' referenced in '" + artifact.getName()
									+ "' cannot be resolved.", getIResource(),
							222);
				} else if (!(returnType instanceof IManagedEntityArtifact || returnType instanceof IDatatypeArtifact)) {
					TigerstripeProjectAuditor
							.reportError(
									"Returned type '"
											+ fqn
											+ "' referenced in '"
											+ artifact.getName()
											+ "' can only be a Managed Entity  or a Datatype artifact.",
									getIResource(), 222);
				}
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

}
