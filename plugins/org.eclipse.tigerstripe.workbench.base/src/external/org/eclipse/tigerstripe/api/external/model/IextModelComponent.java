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
package org.eclipse.tigerstripe.api.external.model;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.profile.stereotype.IextStereotypeCapable;

/**
 * This interface is a set of common definitions for components of a Tigerstripe
 * Model.
 * 
 */
public interface IextModelComponent extends IextStereotypeCapable {

	/**
	 * Static integer value for public visibility.
	 */
	public final static int VISIBILITY_PUBLIC = 0;

	/**
	 * Static integer value for protected visibility.
	 */
	public final static int VISIBILITY_PROTECTED = 1;

	/**
	 * Static integer value for private visibility.
	 */
	public final static int VISIBILITY_PRIVATE = 2;

	/**
	 * Static integer value for private visibility.
	 */
	public final static int VISIBILITY_PACKAGE = 3;

	/**
	 * Returns the name associated with this component.
	 * 
	 * @return String - the name of the component
	 */
	public String getName();

	/**
	 * Returns the comment (or plain-english description) associated with this
	 * model component.
	 * 
	 * @return String - the comment
	 */
	public String getComment();

	/**
	 * Returns an integer value indicating the visibility of this component.
	 * Possible values are defined in the static fields of this class.
	 * 
	 * @return int - representing the visbility
	 */
	public int getVisibility();

	/**
	 * Returns true if this component is included in the current active facet.
	 * 
	 * For IFields, IMethods and ILabels this looks at Annotation-based
	 * exclusion only.
	 * 
	 * If no facet is active, always returns true.
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public boolean isInActiveFacet() throws TigerstripeException;
}
