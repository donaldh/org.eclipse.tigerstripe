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
package org.eclipse.tigerstripe.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.model.IRelationship;
import org.eclipse.tigerstripe.api.queries.IQueryRelationshipsByArtifact;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.model.ArtifactManager;

public class QueryRelationshipsByArtifact extends ArtifactQueryBase implements
		IQueryRelationshipsByArtifact {

	private boolean includeProjectDependencies;

	private String origFQN;

	private String termFQN;

	@Override
	public Collection run(IArtifactManagerSession managerSession) {
		ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) managerSession;
		ArtifactManager mgr = session.getArtifactManager();

		List<IRelationship> result = new ArrayList<IRelationship>();
		try {
			if (origFQN == null && termFQN != null) {
				result = mgr.getTerminatingRelationshipForFQN(termFQN,
						includeProjectDependencies);
			} else if (termFQN == null && origFQN != null) {
				result = mgr.getOriginatingRelationshipForFQN(origFQN,
						includeProjectDependencies);
			} else if (termFQN != null && origFQN != null) {
				List<IRelationship> tmpResult = mgr
						.getOriginatingRelationshipForFQN(origFQN,
								includeProjectDependencies);
				for (IRelationship rel : tmpResult) {
					if (termFQN.equals(rel.getRelationshipZEnd().getIType()
							.getFullyQualifiedName())) {
						result.add(rel);
					}
				}
			}
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
		return result;
	}

	public void setIncludeProjectDependencies(boolean includeProjectDependencies) {
		this.includeProjectDependencies = includeProjectDependencies;
	}

	public void setOriginatingFrom(String fullyQualifiedName) {
		this.origFQN = fullyQualifiedName;
	}

	public void setTerminatingIn(String fullyQualifiedName) {
		this.termFQN = fullyQualifiedName;
	}

}
