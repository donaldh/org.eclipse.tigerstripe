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
package org.eclipse.tigerstripe.api.model.artifacts;


public interface IManagedEntityArtifact extends IAbstractArtifact {

	public final static String DEFAULT_LABEL = "Entity";

	/**
	 * This class represents a Managed Entity Primary Key.
	 * 
	 * 
	 */
	public interface IPrimaryKey {
		
		public void setFullyQualifiedName(String fullyQualifiedName);
		
		/**
		 * Returns the fully qualified name of the key.
		 * 
		 * @return String - fully qualified name of this key
		 */
		public String getFullyQualifiedName();
	
		/**
		 * Returns the package of the key.
		 * 
		 * @return String - package of this key
		 */
		public String getPackage();
	
		/**
		 * Returns the name of the key (ie without the package).
		 * 
		 * @return String - name of this key.
		 */
		public String getName();
	}

	/**
	 * Returns the PrimaryKey object for this Managed Entity.
	 * 
	 * @return IextPrimaryKey - The primaryKey for this Managed Entity
	 */
	public IPrimaryKey getPrimaryKey();

}
