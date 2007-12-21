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
package org.eclipse.tigerstripe.api.artifacts.model.ossj;

import java.util.Properties;

import org.eclipse.tigerstripe.api.artifacts.model.ossj.ISessionArtifact.IEntityMethodFlavorDetails;
import org.eclipse.tigerstripe.api.external.model.IextMethod.OssjEntityMethodFlavor;
import org.eclipse.tigerstripe.api.external.model.artifacts.ossjSpecifics.IextOssjEntitySpecifics;

public interface IOssjEntitySpecifics extends IOssjArtifactSpecifics,
		IextOssjEntitySpecifics, IOssjFlavorDefaults {

	/**
	 * Returns the properties for the specified CRUD operations
	 * 
	 * @deprecated DO NOT USE - use getCRUDFlavorDetails instead
	 * @param crudID,
	 *            one of the CREATE, GET, SET, DELETE
	 * @return
	 */
	@Deprecated
	public Properties getCRUDProperties(int crudID);

	/**
	 * @deprecated DO NOT USE - use setCRUDFlavorDetails
	 * @param crudID
	 * @param crudProperties
	 */
	@Deprecated
	public void setCRUDProperties(int crudID, Properties crudProperties);

	/**
	 * Returns the flavor details for the corresponding CRUD operation on this
	 * entity
	 * 
	 * @param crudID,
	 *            one of the CREATE, GET, SET, DELETE
	 * @param flavor
	 * @return
	 */
	public IEntityMethodFlavorDetails getCRUDFlavorDetails(int crudID,
			OssjEntityMethodFlavor flavor);

	public OssjEntityMethodFlavor[] getSupportedFlavors(int crudID);

	/**
	 * Factory method for IEntityMethodFlavorDetails
	 * 
	 * @return
	 */
	public IEntityMethodFlavorDetails makeIEntityMethodFlavorDetails();

	/**
	 * Sets the flavor details for the corresponding CRUD Operation
	 * 
	 * @param crudID,
	 *            one of the CREATE, GET, SET, DELETE
	 * @param flavor
	 * @param details
	 */
	public void setCRUDFlavorDetails(int crudID, OssjEntityMethodFlavor flavor,
			IEntityMethodFlavorDetails details);

	public void setPrimaryKey(String primaryKey);

	/**
	 * Valid values are EXT_MULTI, EXT_SINGLE, EXT_NONE
	 * 
	 * @param extensibilityType
	 */
	public void setExtensibilityType(String extensibilityType);

	public void setSessionFactoryMethods(boolean sessionBased);

}
