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
 * This class represents a Query Artifact.
 * 
 * 
 */
public interface IextQueryArtifact extends IArtifact {

	/**
	 * Returns the type of the objects that are to be returned by this query.
	 * 
	 * @return type of the returned objects
	 */
	public IextType getReturnedType();
}
