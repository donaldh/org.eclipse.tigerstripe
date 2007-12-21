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
package org.eclipse.tigerstripe.api.profile.stereotype;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.profile.stereotype.IextStereotypeInstance;

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
public interface IStereotypeInstance extends IextStereotypeInstance {

	/**
	 * Returns the name of this instance.
	 * 
	 * <b>Note:</b> this is in fact the name of the characterizing IStereotype
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Returns the characterizing stereotype for this instance
	 * 
	 * @return
	 */
	public IStereotype getCharacterizingIStereotype();

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

	public final static IStereotypeInstance[] EMPTY_ARRAY = new IStereotypeInstance[0];
}
