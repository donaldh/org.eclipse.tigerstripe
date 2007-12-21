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

import java.util.Properties;

import org.eclipse.tigerstripe.api.external.model.IextMethod.OssjEntityMethodFlavor;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextSessionArtifact.IextEntityMethodFlavorDetails;

/**
 * Ossj standard specific details for Managed Entity Artifacts.
 * 
 * @author Eric Dillon
 * 
 */
public interface IextOssjEntitySpecifics extends IextOssjArtifactSpecifics {

	public final static String EXT_MULTI = "multi";

	public final static String EXT_SINGLE = "single";

	public final static String EXT_NONE = "none";

	public final static String[] EXT_OPTIONS = { EXT_MULTI, EXT_SINGLE,
			EXT_NONE };

	public final static String CREATE_PROP_ID = "ossj.entity.create";

	public final static String GET_PROP_ID = "ossj.entity.get";

	public final static String SET_PROP_ID = "ossj.entity.set";

	public final static String REMOVE_PROP_ID = "ossj.entity.remove";

	public final static int CREATE = 0;

	public final static int GET = 1;

	public final static int SET = 2;

	public final static int DELETE = 3;

	/**
	 * Returns the flavor details for the corresponding CRUD operation on this
	 * entity
	 * 
	 * @param crudID,
	 *            one of the CREATE, GET, SET, DELETE
	 * @param flavor
	 * @return
	 */
	public IextEntityMethodFlavorDetails getCRUDFlavorDetails(int crudID,
			OssjEntityMethodFlavor flavor);

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
	 * Returns the interface properties for the KEY associated with this entity.
	 * 
	 * @return
	 */
	public String getPrimaryKey();

	/**
	 * Whether methods are session factory methods.
	 * 
	 * @return
	 */
	public boolean isSessionFactoryMethods();

	/**
	 * Returns the interface properties for the KEY associated with this entity.
	 * 
	 * @return
	 */
	public Properties getInterfaceKeyProperties();

	/**
	 * Return the type of the extensibility. Valid vlaues are defined in this
	 * interface, and are listed in EXT_OPTIONS.
	 * 
	 * 
	 * @return
	 */
	public String getExtensibilityType();

}
