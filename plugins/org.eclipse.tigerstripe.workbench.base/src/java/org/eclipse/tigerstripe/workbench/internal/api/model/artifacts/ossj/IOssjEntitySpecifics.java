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
package org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj;

import java.util.Properties;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.OssjEntityMethodFlavor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IEntityMethodFlavorDetails;

public interface IOssjEntitySpecifics extends IOssjArtifactSpecifics,
	 IOssjFlavorDefaults {

	public final static String EXT_SINGLE = "single";
	public final static int GET = 1;
	public final static String GET_PROP_ID = "ossj.entity.get";
	public final static String REMOVE_PROP_ID = "ossj.entity.remove";
	public final static int SET = 2;
	public final static String SET_PROP_ID = "ossj.entity.set";
	public final static int CREATE = 0;
	public final static String CREATE_PROP_ID = "ossj.entity.create";
	public final static int DELETE = 3;
	public final static String EXT_MULTI = "multi";
	public final static String EXT_NONE = "none";
	public final static String[] EXT_OPTIONS = { EXT_MULTI, EXT_SINGLE,
			EXT_NONE };
	

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


	/**
	 * Return the type of the extensibility. Valid vlaues are defined in this
	 * interface, and are listed in EXT_OPTIONS.
	 * 
	 * 
	 * @return
	 */
	public String getExtensibilityType();

	/**
	 * Returns the interface properties for the KEY associated with this entity.
	 * 
	 * @return
	 */
	public Properties getInterfaceKeyProperties();

	/**
	 * Returns the interface properties for the KEY associated with this entity.
	 * 
	 * @return
	 */
	public String getPrimaryKey();

	/**
	 * Returns aa array of the supported Flavors for the specified crud Method.
	 * The flavors correspond to the optins such as bestEffortsByKeys and so on.
	 * 
	 * @param crudID,
	 *            one of the CREATE, GET, SET, DELETE
	 * @return
	 */
	public OssjEntityMethodFlavor[] getSupportedFlavors(int crudID);

	/**
	 * Whether methods are session factory methods.
	 * 
	 * @return
	 */
	public boolean isSessionFactoryMethods();

}
