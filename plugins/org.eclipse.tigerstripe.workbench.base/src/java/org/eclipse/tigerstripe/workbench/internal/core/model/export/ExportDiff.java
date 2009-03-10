/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Jim Strawn
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.internal.core.model.export;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.QueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.internal.core.model.export.facets.FacetExporterInput;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;

public class ExportDiff {

	@SuppressWarnings("deprecation")
	public static List<IAbstractArtifact> getDuplicates(FacetExporterInput inputManager) throws IllegalArgumentException,
			TigerstripeException, CoreException {

		ExportFacetManager facetManager =  new ExportFacetManager(inputManager.getSource());
		facetManager.applyExportFacet(inputManager.getFacet());

		List<IAbstractArtifact> artifacts = new ArrayList<IAbstractArtifact>();

		IArtifactQuery query = new QueryAllArtifacts();
		List<IAbstractArtifact> sourceArtifacts = (List<IAbstractArtifact>) inputManager.getSource().getArtifactManagerSession().queryArtifact(query);

		for (IAbstractArtifact artifact : sourceArtifacts) {

			if (artifact.isInActiveFacet()) {

				IAbstractArtifact artifactToOverwrite = inputManager.getDestination().getArtifactManagerSession().getArtifactByFullyQualifiedName(
						artifact.getFullyQualifiedName());

				if (artifactToOverwrite != null) {
					artifacts.add(artifactToOverwrite);
				}
			}

		}

		facetManager.restoreActiveFacet();

		return artifacts;
	}
}
