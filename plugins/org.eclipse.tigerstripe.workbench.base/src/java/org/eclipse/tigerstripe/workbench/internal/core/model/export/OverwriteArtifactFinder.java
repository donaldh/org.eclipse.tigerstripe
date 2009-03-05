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

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.QueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;

public class OverwriteArtifactFinder {

	@SuppressWarnings("deprecation")
	public static List<IAbstractArtifact> getArtifactsList(ITigerstripeModelProject source, ITigerstripeModelProject destination)
			throws IllegalArgumentException, TigerstripeException {

		List<IAbstractArtifact> artifacts = new ArrayList<IAbstractArtifact>();

		IArtifactQuery query = new QueryAllArtifacts();
		List<IAbstractArtifact> sourceArtifacts = (List<IAbstractArtifact>) source.getArtifactManagerSession().queryArtifact(query);

		for (IAbstractArtifact artifact : sourceArtifacts) {

			IAbstractArtifact artifactToOverwrite = destination.getArtifactManagerSession().getArtifactByFullyQualifiedName(
					artifact.getFullyQualifiedName());
			
			if(artifactToOverwrite != null) {
				artifacts.add(artifactToOverwrite);
			}

		}

		return artifacts;
	}
}
