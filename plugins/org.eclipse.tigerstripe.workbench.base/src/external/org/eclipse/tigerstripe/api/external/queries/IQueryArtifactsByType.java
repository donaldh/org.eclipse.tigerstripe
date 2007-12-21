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
package org.eclipse.tigerstripe.api.external.queries;

/**
 * This query returns all artifacts of the specified type for a Tigerstripe
 * project.
 * 
 * @author Eric Dillon
 * 
 */
public interface IQueryArtifactsByType extends IArtifactQuery {

	/**
	 * Used to identify the types of artifact that should be returned. The type
	 * argument should be the FQN of the class for the artifacts to be returned.
	 * Use getSupportedTypes() to obtain a list of valid Strings to be passed.
	 * 
	 * Only one artifact type can be specified for a given query instance.
	 * 
	 * @param FQN
	 *            of the artifact types that should be returned
	 */
	public void setArtifactType(String type);

	/**
	 * Returns the FQN of the current aryifact type for this query.
	 * 
	 * @return FQN of the artifact types that should be returned
	 */
	public String getArtifactType();

	/**
	 * Returns an array of the valid artifact type FQNs for the query. There
	 * will be one entry per artifact type in the model.
	 * 
	 * @return an array of valid FQNs
	 */
	public String[] getSupportedTypes();

}
