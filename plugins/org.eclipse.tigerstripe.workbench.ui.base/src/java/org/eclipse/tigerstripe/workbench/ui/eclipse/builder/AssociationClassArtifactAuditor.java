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
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IManagedEntityArtifact;

public class AssociationClassArtifactAuditor extends AssociationArtifactAuditor
		implements IArtifactAuditor {

	public AssociationClassArtifactAuditor(IProject project,
			IAbstractArtifact artifact) {
		super(project, artifact);
	}

	@Override
	protected void checkSuperArtifact() {
		IAbstractArtifact superArtifact = getArtifact().getExtendedIArtifact();

		if (superArtifact != null) {
			if (superArtifact.getClass() != getArtifact().getClass()
					&& !(superArtifact instanceof IManagedEntityArtifact)) {
				TigerstripeProjectAuditor
						.reportError(
								"Invalid Type Hierarchy in '"
										+ getArtifact().getFullyQualifiedName()
										+ "'. Super-artifact of AssociationClasses can only be of AssociationClass or ManagedEntity type.",
								TigerstripeProjectAuditor
										.getIResourceForArtifact(getIProject(),
												getArtifact()), 222);
			}
		}
	}

}
