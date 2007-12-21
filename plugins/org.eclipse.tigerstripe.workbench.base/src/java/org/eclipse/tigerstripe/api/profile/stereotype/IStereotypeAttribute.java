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

import org.dom4j.Element;
import org.eclipse.tigerstripe.api.external.profile.stereotype.IextStereotypeAttribute;

/**
 * Top-level definition for user-defined attributes on a stereotype
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IStereotypeAttribute extends IextStereotypeAttribute {

	public void setName(String name);

	public void setDefaultValue(String defaultValue);

	public void setDescription(String description);

	public Element asElement();

	/**
	 * Sets whether this attribute is an array
	 * 
	 * @param isArray
	 */
	public void setArray(boolean isArray);

	public void parse(Element element);
}
