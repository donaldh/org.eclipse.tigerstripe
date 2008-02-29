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

	/**
	 * Get the 'A' end of this association.
	 * 
	 * 
	 * @return The 'A' end of this association.
	 */
	public IAssociationEnd getAEnd();
	
	/**
	 * Set the 'A' end of the association.
	 * 
	 * @param aEnd
	 */
	public void setAEnd(IAssociationEnd aEnd);

	/**
	 * Get the 'Z' end of this association.
	 * 
	 * @return The 'Z' end of this association.
	 */
	public IAssociationEnd getZEnd();
	
	/**
	 * Set the 'Z' end of the association.
	 * 
	 * @param zEnd
	 */
	public void setZEnd(IAssociationEnd zEnd);

	/**
	 * Returns a collection containing all association ends.
	 * 
	 * @return - an unmodifiable collection of Association Ends 
	 */
	public Collection<IAssociationEnd> getAssociationEnds();

	/**
	 * Make a new blank association end.
	 * 
	 * This does not add the field to the Artifact.
	 * 
	 * @return a new IAssociationEnd.
	 */
	public IAssociationEnd makeAssociationEnd();


}
