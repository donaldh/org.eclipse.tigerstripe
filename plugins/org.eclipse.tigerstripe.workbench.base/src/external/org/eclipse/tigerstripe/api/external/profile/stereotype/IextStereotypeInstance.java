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
package org.eclipse.tigerstripe.api.external.profile.stereotype;

import org.eclipse.tigerstripe.api.external.TigerstripeException;

/**
 * A Stereotype instance is an instance of a IStereotype defined in a profile as
 * stored in a Model component (e.g. artifact, method, etc...)
 * 
 * An IextStereotypeInstance is associated with the IextStereotype that
 * characterizes it. All values are stringified.
 * 
 * It is responsible for persisting the values into the artifact.
 * 
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IextStereotypeInstance {

	/**
	 * Returns the name of this instance.
	 * 
	 * <b>Note:</b> this is in fact the name of the characterizing IStereotype
	 * 
	 * @return the name of this instance
	 */
	public String getName();

	/**
	 * Returns the characterizing stereotype for this instance. The
	 * caharacterizingStereotype determines the defined attributes and their
	 * defualt values etc.
	 * 
	 * @return the characterizing stereotype
	 */
	public IextStereotype getCharacterizingIextStereotype();

	/**
	 * Returns the value of the given attribute.
	 * 
	 * @param attribute
	 * @throws TigerstripeException
	 *             if the given attribute is not a valid attribute for the
	 *             characterizing stereotype
	 * 
	 * @return
	 */
	public String getAttributeValue(IextStereotypeAttribute attribute)
			throws TigerstripeException;

	/**
	 * Returns the values for the given attribute in the case the attribute is
	 * an array attribute.
	 * 
	 * @param attribute
	 * @return
	 * @throws TigerstripeException
	 *             if attribute not array attribute
	 */
	public String[] getAttributeValues(IextStereotypeAttribute attribute)
			throws TigerstripeException;

	/**
	 * Returns the value for an attribute identified by its name.
	 * 
	 * @param attributeName
	 * @return
	 * @throws TigerstripeException
	 */
	public String getAttributeValue(String attributeName)
			throws TigerstripeException;

	/**
	 * Returns the value array for an attribute identified by its name.
	 * 
	 * @param attributeName
	 * @return
	 * @throws TigerstripeException
	 *             if attribute not array attribute
	 */
	public String[] getAttributeValues(String attributeName)
			throws TigerstripeException;
}
