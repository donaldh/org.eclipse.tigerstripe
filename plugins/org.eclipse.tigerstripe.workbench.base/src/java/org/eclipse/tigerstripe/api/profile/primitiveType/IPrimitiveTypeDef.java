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
package org.eclipse.tigerstripe.api.profile.primitiveType;

import org.dom4j.Element;
import org.eclipse.tigerstripe.api.TigerstripeException;

/**
 * A primitive type definition as it appears in a profile definition
 * 
 * Once the profile is loaded, these are turned into IPrimitiveTypeArtifact that
 * are put into context for all projects
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IPrimitiveTypeDef {

	public void setName(String name);

	public String getName();

	public void setPackageName(String name);

	public String getPackageName();

	public String getDescription();

	public void setDescription(String description);

	public boolean isReserved();

	/**
	 * used when testing names of primitive types to see if they match the
	 * "typical" naming conventions for data types (isRecommendedName returns
	 * true if the type name "looks like" a typical classname; isValidName
	 * returns true if the type name will result in a parse-able POJO if it is
	 * used as a data type)
	 * 
	 * @return
	 */
	public boolean isRecommendedName();

	public boolean isValidName();

	/**
	 * Returns an XML representation of this stereotype
	 * 
	 * @return
	 */
	public Element asElement();

	/**
	 * Returns an XML representation of this primitive type The default element
	 * contains package information.
	 * 
	 * @return
	 */
	public Element asDefaultElement();

	/**
	 * Parses the details of this from the given XML element
	 * 
	 * @param element
	 */
	public void parse(Element element) throws TigerstripeException;

}
