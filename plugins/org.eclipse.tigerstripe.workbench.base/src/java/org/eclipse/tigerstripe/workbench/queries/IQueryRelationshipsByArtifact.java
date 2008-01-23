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
package org.eclipse.tigerstripe.workbench.queries;

/**
 * This query returns all IRelationships corresponding to either or both ends
 * set.
 * 
 * @author Eric Dillon
 * 
 */
public interface IQueryRelationshipsByArtifact extends IArtifactQuery {

	/**
	 * Set the originating artifact. When this is set, any relationships that
	 * originate from the artifact with the FQN passed, will be included in the
	 * results of the query. This equates to any relationship whose "A" end type
	 * mathes the FQN passed.
	 * 
	 * @param fullyQualifiedName
	 *            of the "originating" artifact
	 */
	public void setOriginatingFrom(String fullyQualifiedName);

	/**
	 * Set the terminating artifact. When this is set, any relationships that
	 * terminating in the artifact with the FQN passed, will be included in the
	 * results of the query. This equates to any relationship whose "Z" end type
	 * mathes the FQN passed.
	 * 
	 * @param fullyQualifiedName
	 *            of the "terminating" artifact
	 */
	public void setTerminatingIn(String fullyQualifiedName);

	/**
	 * Determines whether artifacts in dependant projects are included in the
	 * query results. When true is passed, the query will return a results set
	 * including any matching artifacts in and dependant modules, or referenced
	 * projects. When false, only the artifacts in the local project are
	 * considered.
	 * 
	 * @param includeProjectDependencies
	 */
	public void setIncludeProjectDependencies(boolean includeProjectDependencies);
}
