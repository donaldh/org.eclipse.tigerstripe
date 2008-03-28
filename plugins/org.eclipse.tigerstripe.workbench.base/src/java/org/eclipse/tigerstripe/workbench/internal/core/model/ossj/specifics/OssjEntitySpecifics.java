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
package org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics;

import java.util.Properties;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.Tag;
import org.eclipse.tigerstripe.workbench.internal.core.model.tags.PropertiesConstants;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.OssjEntityMethodFlavor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IEntityMethodFlavorDetails;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjEntitySpecifics;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjFlavorDefaults;

public class OssjEntitySpecifics extends OssjArtifactSpecifics implements
		IOssjEntitySpecifics {

	// All entities have CRUD operations specifics
	Properties[] crudProperties; // using CREATE, GET, SET, DELETE as index

	private Properties interfaceKeyProperties = new Properties();

	private String primaryKey;

	private String extensibilityType = EXT_MULTI;

	public static String getFlavorDefaults(
			OssjEntityMethodFlavor[] supportedFlavors,
			OssjEntityMethodFlavor targetFlavor) {
		return getFlavorDefaults(supportedFlavors, targetFlavor, false);
	}

	/**
	 * Gets the flavor defaults
	 * 
	 * @param supportedFlavors
	 * @param targetFlavor
	 * @param shouldIncludeException,
	 *            if set to false, only the flag values are returned, not every
	 *            single Exception list.
	 * @return
	 */
	public static String getFlavorDefaults(
			OssjEntityMethodFlavor[] supportedFlavors,
			OssjEntityMethodFlavor targetFlavor, boolean shouldIncludeException) {
		int index = 0;
		String[] defaults = null;
		if (supportedFlavors == customMethodFlavors) {
			defaults = customMethodFlavorDefaults;
		} else if (supportedFlavors == createMethodFlavors) {
			defaults = createMethodFlavorDefaults;
		} else if (supportedFlavors == getMethodFlavors) {
			defaults = getMethodFlavorDefaults;
		} else if (supportedFlavors == setMethodFlavors) {
			defaults = setMethodFlavorDefaults;
		} else if (supportedFlavors == removeMethodFlavors) {
			defaults = removeMethodFlavorDefaults;
		}

		int i = 0;
		for (OssjEntityMethodFlavor flavor : supportedFlavors) {
			if (flavor == targetFlavor) {
				index = i;
			}
			i++;
		}

		if (shouldIncludeException)
			return defaults[index];
		else {
			String fullDefaults = defaults[index];
			return fullDefaults.substring(0, fullDefaults.indexOf(":"));
		}
	}

	public String getExtensibilityType() {
		return extensibilityType;
	}

	public boolean isSingleExtension() {
		return IOssjEntitySpecifics.EXT_SINGLE
				.equals(getExtensibilityType());
	}

	public void setExtensibilityType(String extensibilityType) {
		this.extensibilityType = extensibilityType;
	}

	public Properties getCRUDProperties(int crudID) {
		if (crudID <= IOssjEntitySpecifics.DELETE
				&& crudID >= IOssjEntitySpecifics.CREATE) {
			if (crudProperties[crudID] == null) {
				crudProperties[crudID] = getDefaultCrudProperties(crudID);
			}
			return crudProperties[crudID];
		}
		return null;
	}

	public void setCRUDProperties(int crudID, Properties crudProperties) {
		if (crudID <= IOssjEntitySpecifics.DELETE
				&& crudID >= IOssjEntitySpecifics.CREATE) {
			this.crudProperties[crudID] = crudProperties;
		}
		validateCRUDProperties(crudID);
	}

	/**
	 * Makes sure all the properties have a default value for the given CRUDID
	 * 
	 * @param crudID
	 */
	protected void validateCRUDProperties(int crudID) {

		OssjEntityMethodFlavor[] flavors = null;
		String[] flavorDefaults = null;

		switch (crudID) {
		case CREATE:
			flavors = createMethodFlavors;
			flavorDefaults = createMethodFlavorDefaults;
			break;
		case SET:
			flavors = setMethodFlavors;
			flavorDefaults = setMethodFlavorDefaults;
			break;
		case GET:
			flavors = getMethodFlavors;
			flavorDefaults = getMethodFlavorDefaults;
			break;
		case DELETE:
			flavors = removeMethodFlavors;
			flavorDefaults = removeMethodFlavorDefaults;
			break;
		}
		for (int f = 0; f < flavors.length; f++) {
			this.crudProperties[crudID].setProperty(flavors[f].getPojoLabel(),
					this.crudProperties[crudID].getProperty(flavors[f]
							.getPojoLabel(), flagOnly(flavorDefaults[f])));
		}

	}

	private String flagOnly(String flavorDefault) {
		if (flavorDefault.indexOf(":") == -1)
			return flavorDefault;
		else
			return flavorDefault.substring(0, flavorDefault.indexOf(":"));
	}

	public OssjEntitySpecifics(AbstractArtifact artifact) {
		super(artifact);
		crudProperties = new Properties[IOssjEntitySpecifics.DELETE + 1];
	}

	@Override
	public void build() {
		super.build();

		Tag tag = getArtifact().getFirstTagByName(
				ManagedEntityArtifact.MARKING_TAG);
		if (tag != null) {
			Properties props = tag.getProperties();
			setPrimaryKey(props.getProperty("primary-key", "String"));
		}

		// THIS IS A HACK TO OVERWRITE WHAT WAS EXTRACTED in SUPER.BUILD()
		// because the tag is different for an Entity
		Tag intfTag = getArtifact().getFirstTagByName(
				OssjTags.VALUE_TAG);
		if (intfTag != null) {
			setInterfaceProperties(intfTag.getProperties());
		}

		Tag keyIntfTag = getArtifact().getFirstTagByName(
				OssjTags.KEYINTERFACE_TAG);
		if (keyIntfTag != null) {
			interfaceKeyProperties = keyIntfTag.getProperties();
		}

		// Extract CRUD operations Options
		try {

			setCRUDProperties(IOssjEntitySpecifics.CREATE,
					PropertiesConstants.getPropertiesById(getArtifact()
							.getTags(), CREATE_PROP_ID));
			setCRUDProperties(IOssjEntitySpecifics.GET, PropertiesConstants
					.getPropertiesById(getArtifact().getTags(), GET_PROP_ID));
			setCRUDProperties(IOssjEntitySpecifics.SET, PropertiesConstants
					.getPropertiesById(getArtifact().getTags(), SET_PROP_ID));
			setCRUDProperties(IOssjEntitySpecifics.DELETE,
					PropertiesConstants.getPropertiesById(getArtifact()
							.getTags(), REMOVE_PROP_ID));
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}

		// Extract the base type
		// Extract Extensibility flag
		tag = getArtifact()
				.getFirstTagByName(ManagedEntityArtifact.MARKING_TAG);
		if (tag != null) {
			Properties props = tag.getProperties();
			String ext = props.getProperty("extensibilityType",
					IOssjEntitySpecifics.EXT_MULTI);

			setExtensibilityType(ext);
		}

	}

	public Properties getInterfaceKeyProperties() {
		return interfaceKeyProperties;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	protected Properties getDefaultCrudProperties(int crudID) {
		Properties result = new Properties();

		OssjEntityMethodFlavor[] flavors = null;
		String[] flavorDefaults = null;

		switch (crudID) {
		case CREATE:
			flavors = createMethodFlavors;
			flavorDefaults = createMethodFlavorDefaults;
			break;
		case SET:
			flavors = setMethodFlavors;
			flavorDefaults = setMethodFlavorDefaults;
			break;
		case GET:
			flavors = getMethodFlavors;
			flavorDefaults = getMethodFlavorDefaults;
			break;
		case DELETE:
			flavors = removeMethodFlavors;
			flavorDefaults = removeMethodFlavorDefaults;
			break;
		}

		int i = 0;
		for (OssjEntityMethodFlavor flavor : flavors) {
			String def = flavorDefaults[i++];
			result.setProperty(flavor.getPojoLabel(), flagOnly(def));
		}

		return result;
	}

	@Override
	public void applyDefaults() {
		super.applyDefaults();
		setPrimaryKey("String");
		setExtensibilityType(IOssjEntitySpecifics.EXT_MULTI);
		setCRUDProperties(IOssjEntitySpecifics.CREATE,
				getDefaultCrudProperties(IOssjEntitySpecifics.CREATE));
		setCRUDProperties(IOssjEntitySpecifics.GET,
				getDefaultCrudProperties(IOssjEntitySpecifics.GET));
		setCRUDProperties(IOssjEntitySpecifics.SET,
				getDefaultCrudProperties(IOssjEntitySpecifics.SET));
		setCRUDProperties(IOssjEntitySpecifics.DELETE,
				getDefaultCrudProperties(IOssjEntitySpecifics.DELETE));
	}

	public OssjEntityMethodFlavor[] getSupportedFlavors(int crudID) {
		switch (crudID) {
		case IOssjEntitySpecifics.CREATE:
			return IOssjFlavorDefaults.createMethodFlavors;
		case IOssjEntitySpecifics.DELETE:
			return IOssjFlavorDefaults.removeMethodFlavors;
		case IOssjEntitySpecifics.GET:
			return IOssjFlavorDefaults.getMethodFlavors;
		case IOssjEntitySpecifics.SET:
			return IOssjFlavorDefaults.setMethodFlavors;
		default:
			return null;
		}
	}

	public IEntityMethodFlavorDetails getCRUDFlavorDetails(int crudID,
			OssjEntityMethodFlavor flavor) {
		return new EntityMethodFlavorDetails(getArtifact(), getCRUDProperties(
				crudID).getProperty(flavor.getPojoLabel()));
	}

	public IEntityMethodFlavorDetails makeIEntityMethodFlavorDetails() {
		return new EntityMethodFlavorDetails(getArtifact());
	}

	public void setCRUDFlavorDetails(int crudID, OssjEntityMethodFlavor flavor,
			IEntityMethodFlavorDetails details) {
		getCRUDProperties(crudID).setProperty(flavor.getPojoLabel(),
				details.toString());
	}

}
