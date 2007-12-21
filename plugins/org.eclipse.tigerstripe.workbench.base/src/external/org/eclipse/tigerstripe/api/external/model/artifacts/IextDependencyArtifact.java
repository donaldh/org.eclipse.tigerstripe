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
package org.eclipse.tigerstripe.api.external.model.artifacts;

import org.eclipse.tigerstripe.api.external.model.IextType;

/**
 * Artifact representing a UML Dependency
 * 
 * 
 */
public interface IextDependencyArtifact extends IArtifact, IRelationship {

	/**
	 * The type of the AEnd for this Dependency.
	 * 
	 * @return AEnd type
	 */
	public IextType getAEndType();

	/**
	 * The type of the ZEnd for this Dependency.
	 * 
	 * @return ZEnd type
	 */
	public IextType getZEndType();
}
