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

/**
 * Tigerstripe Abstraction for UML Association.
 * 
 * @since 1.2
 */
public interface IextAssociationArtifact extends IArtifact, IRelationship {

	/**
	 * Get the 'A' end of this association.
	 * 
	 * @return The 'A' end of this association.
	 */
	public IextAssociationEnd getAEnd();

	/**
	 * Get the 'Z' end of this association.
	 * 
	 * @return The 'Z' end of this association.
	 */
	public IextAssociationEnd getZEnd();

	/**
	 * Returns an array containing all association ends
	 * 
	 * @return
	 */
	public IextAssociationEnd[] getAssociationEnds();
}
