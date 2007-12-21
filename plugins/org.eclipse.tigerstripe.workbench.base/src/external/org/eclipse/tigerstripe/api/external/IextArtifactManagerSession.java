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
package org.eclipse.tigerstripe.api.external;

import java.util.Collection;

import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;
import org.eclipse.tigerstripe.api.external.queries.IArtifactQuery;

public interface IextArtifactManagerSession {

	/**
	 * Returns all known artifacts with the given FQN. If multiple definitions
	 * are found along the classpath (modules and dependencies), they are
	 * returned in the order they are found.
	 * 
	 * @param fqn
	 * @return
	 */
	public IArtifact[] getAllKnownIArtifactsByFullyQualifiedName(String fqn);

	// TODO What about excpetions!
	public IArtifact getIArtifactByFullyQualifiedName(String fqn);

	public IArtifact getIArtifactByFullyQualifiedName(String fqn,
			boolean includeDependencies);

	/**
	 * Returns a list of all supported Artifact Types
	 * 
	 */
	public String[] getSupportedIArtifacts();

	/**
	 * Makes a new Artifact Query
	 */
	public IArtifactQuery makeQuery(String queryType)
			throws IllegalArgumentException;

	/**
	 * Query artifacts based on the given query Object
	 * 
	 * @param query -
	 *            ArtifactQuery the query to execute
	 */
	public Collection queryArtifact(IArtifactQuery query)
			throws IllegalArgumentException, TigerstripeException;

}
