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
package org.eclipse.tigerstripe.workbench.profile.stereotype;

import org.eclipse.tigerstripe.workbench.TigerstripeException;


/**
 * A Stereotype instance is an instance of a IStereotype defined in a profile as
 * stored in a Model component (e.g. artifact, method, etc...)
 * 
 * A IStereotypeInstance is associated with the IStereotype that characterizes
 * it. All values are stringified.
 * 
 * It is responsible for persisting the values into the artifact.
 * 
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IStereotypeInstance {

	/**
	 * Returns the name of this instance.
	 * 
	 * <b>Note:</b> this is in fact the name of the characterizing IStereotype
	 * 
	 * @return
	 */
	public String getName();


	/**
	 * Sets the value for the given attribute.
	 * 
	 * @param attribute
	 * @param value
	 * @throws TigerstripeException
	 *             if the attribute is not a valid attribute for the
	 *             characterizing Stereotype or if the value is invalid.
	 */
	public void setAttributeValue(IStereotypeAttribute attribute, String value)
			throws TigerstripeException;

	/**
	 * Sets the values for an array attribute
	 * 
	 * @param attribute
	 * @param values
	 * @throws TigerstripeException
	 *             if attribute not array attribute
	 */
	public void setAttributeValues(IStereotypeAttribute attribute,
			String[] values) throws TigerstripeException;

	public Object clone() throws CloneNotSupportedException;

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
	public String getAttributeValue(IStereotypeAttribute attribute)
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
	 * Returns the values for the given attribute in the case the attribute is
	 * an array attribute.
	 * 
	 * @param attribute
	 * @return
	 * @throws TigerstripeException
	 *             if attribute not array attribute
	 */
	public String[] getAttributeValues(IStereotypeAttribute attribute)
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

	/**
	 * Returns the characterizing stereotype for this instance. The
	 * caharacterizingStereotype determines the defined attributes and their
	 * default values etc.
	 * 
	 * @return the characterizing stereotype
	 */
	public IStereotype getCharacterizingIStereotype();


	public final static IStereotypeInstance[] EMPTY_ARRAY = new IStereotypeInstance[0];
}
