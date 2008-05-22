/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.repository.internal;



/**
 * This interface specifies all the useful metadata for an artifact.
 * 
 * This can be access on any IAbstractArtifact through the getMetadata() method.
 * 
 * Note that a default implementation is provided for each core Tigerstripe
 * Artifact, but this can be customized through the
 * org.eclipse.tigerstripe.metadata.artifactMetadata extension point.
 * 
 * @author erdillon
 * @since 0.2.3
 */
public interface IArtifactMetadata extends IModelComponentMetadata {

	/**
	 * Returns true if the specified artifact type supports fields.
	 * 
	 * @return
	 */
	public boolean hasFields();

	/**
	 * Returns true if the specified artifact type supports fields.
	 * 
	 * @return
	 */
	public boolean hasMethods();

	/**
	 * Returns true if the specified artifact type supports fields.
	 * 
	 * @return
	 */
	public boolean hasLiterals();

}
