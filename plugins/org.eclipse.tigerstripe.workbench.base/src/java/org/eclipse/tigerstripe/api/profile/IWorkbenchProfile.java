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
package org.eclipse.tigerstripe.api.profile;

import java.io.Reader;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.IModelComponent;
import org.eclipse.tigerstripe.api.profile.primitiveType.IPrimitiveTypeDef;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeCapable;

/**
 * Top-level interface for a Workbench Profile
 * 
 * A Workbench profile defines the configuration of a Workbench runtime. It
 * defines a set of elements that will condition how workbench behaves. For
 * example, - which artifacts are available and usable - Are built-in plugins
 * visible (until they disappear as built-in plugins) - Which stereotypes are
 * defined - which primitive type are defined, etc...
 * 
 * Profiles are versioned and can be installed on any Workbench install. They
 * are saved in .wbp files which are in fact XML files that have been encrypted.
 * 
 * Profiles can be defined by the Workbench Profile Editor/Wizard provided the
 * license includes the proper role (@see IWorkbenchRole for more details)
 * 
 * Upon start, Workbench looks for a default.wbp file in the install directory.
 * If not found, a built-in profile is used instead and a warning message is
 * issued. The default.wbp profile can be replaced by any valid Workbench
 * profile. A re-start of Eclipse is required for it to be taken into account.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IWorkbenchProfile {

	// the file extension for IWorkbenchProfile files
	public final static String FILE_EXTENSION = "wbp";

	// The default filename looked for as the default profile
	public final static String DEFAULT_PROFILE_FILE = "default."
			+ FILE_EXTENSION;

	// The factory defaults filename
	public final static String FACTORY_PROFILE_FILE = "factory."
			+ FILE_EXTENSION;

	// ==================================================================
	// All XML Elements
	public final static String XML_COMPATIBILITY_LEVEL_ATTR = "compatibilityLevel";
	public final static String XML_ROOT_ELEMENT = "profile";
	public final static String XML_NAME = "name";
	public final static String XML_VERSION = "version";
	public final static String XML_DESCRIPTION = "description";

	public void setName(String name);

	public String getName();

	public void setVersion(String version);

	public String getVersion();

	public void setDescription(String description);

	public String getDescription();

	public IStereotype[] getStereotypes();

	public void setStereotypes(IStereotype[] stereotypes)
			throws TigerstripeException;

	public void addStereotype(IStereotype stereotype)
			throws TigerstripeException;

	public void removeStereotype(IStereotype stereotype)
			throws TigerstripeException;

	public void removeStereotypes(IStereotype[] stereotype)
			throws TigerstripeException;

	public IPrimitiveTypeDef[] getPrimitiveTypeDefs(boolean includeReservedTypes);

	public void setPrimitiveTypeDefs(IPrimitiveTypeDef[] primitiveTypeDefs)
			throws TigerstripeException;

	public void addPrimitiveTypeDef(IPrimitiveTypeDef stereotype)
			throws TigerstripeException;

	public void removePrimitiveTypeDef(IPrimitiveTypeDef stereotype)
			throws TigerstripeException;

	public void removePrimitiveTypeDefs(IPrimitiveTypeDef[] stereotype)
			throws TigerstripeException;

	public void setDefaultPrimitiveType(IPrimitiveTypeDef stereotype)
			throws TigerstripeException;

	public IPrimitiveTypeDef getDefaultPrimitiveType();

	public String getDefaultPrimitiveTypeString();

	/**
	 * Returns the XML content corresponding to this IWorkbenchProfile
	 * 
	 * It is ready to be encrypted and saved.
	 * 
	 * @return
	 */
	public String asText();

	/**
	 * Parses the content into this IWorkbenchProfile.
	 * 
	 * The content of this is overwritten with the content acquired from the
	 * reader.
	 * 
	 * @param reader
	 */
	public void parse(Reader reader) throws TigerstripeException;

	/**
	 * Returns the stereotype for the given name. If no stereotype exists in
	 * that profile for the given name, null is returned.
	 * 
	 * @param
	 * @return
	 */
	public IStereotype getStereotypeByName(String name);

	/**
	 * Returns an array of stereotype for a given scope
	 * 
	 * @param scope
	 * @return
	 * @deprected use {@link #getAvailableStereotypeForCapable(IModelComponent)}
	 *            instead
	 */
	public IStereotype[] getAvailableStereotypeForComponent(
			IModelComponent component);

	/**
	 * Returns an array of stereotype for a given scope
	 * 
	 * @param scope
	 * @return
	 */
	public IStereotype[] getAvailableStereotypeForCapable(
			IStereotypeCapable component);

	/**
	 * Returns the value of the property identified by its name
	 * 
	 * @param propertyName
	 * @return the property value or null if the property cannot be found
	 */
	public IWorkbenchProfileProperty getProperty(String propertyName);

	/**
	 * Sets property propertyName to the given value
	 * 
	 * @param propertyName
	 * @param property
	 */
	public void setProperty(String propertyName,
			IWorkbenchProfileProperty property);

}
