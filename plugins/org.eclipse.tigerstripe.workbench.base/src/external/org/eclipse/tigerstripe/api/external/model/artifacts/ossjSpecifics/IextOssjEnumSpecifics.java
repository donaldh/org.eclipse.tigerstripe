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
package org.eclipse.tigerstripe.api.external.model.artifacts.ossjSpecifics;

import org.eclipse.tigerstripe.api.artifacts.model.IType;

/**
 * OSSJ Standard specific details for an Enumeration Artifact
 * 
 * @author Eric Dillon
 * 
 */
public interface IextOssjEnumSpecifics extends IextOssjArtifactSpecifics {

	/**
	 * Return the basetype for this Enumeration
	 * 
	 * @return
	 */
	public IType getBaseIType();

	/**
	 * Whether this enumeration can be extended.
	 * 
	 * @return
	 */
	public boolean getExtensible();

}
