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
package org.eclipse.tigerstripe.workbench.model.deprecated_;

import java.util.Collection;


/**
 * Internal Interface for IAssociationArtifact
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IAssociationArtifact extends IModelComponent,
		IAbstractArtifact , IRelationship{

	public void setAEnd(IAssociationEnd aEnd);

	public void setZEnd(IAssociationEnd zEnd);

	public IAssociationEnd makeAssociationEnd();

	/**
	 * Get the 'A' end of this association.
	 * 
	 * @return The 'A' end of this association.
	 */
	public IAssociationEnd getAEnd();

	/**
	 * Returns a collection containing all association ends
	 * 
	 * @return
	 */
	public Collection<IAssociationEnd> getAssociationEnds();

	/**
	 * Get the 'Z' end of this association.
	 * 
	 * @return The 'Z' end of this association.
	 */
	public IAssociationEnd getZEnd();
}
