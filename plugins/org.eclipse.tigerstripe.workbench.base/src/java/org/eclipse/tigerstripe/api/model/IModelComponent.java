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
package org.eclipse.tigerstripe.api.model;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeInstance;

/**
 * A component for a Tigerstripe Model
 * 
 * 
 * @author Eric Dillon
 */
public interface IModelComponent  extends IStereotypeCapable {

	/**
	 * Static integer value for private visibility.
	 */
	public final static int VISIBILITY_PACKAGE = 3;
	/**
	 * Static integer value for private visibility.
	 */
	public final static int VISIBILITY_PRIVATE = 2;
	/**
	 * Static integer value for protected visibility.
	 */
	public final static int VISIBILITY_PROTECTED = 1;
	/**
	 * Static integer value for public visibility.
	 */
	public final static int VISIBILITY_PUBLIC = 0;

	/**
	 * Sets the name associated with this component
	 * 
	 * @return
	 */
	public void setName(String name);

	public void setComment(String comment);

	public void setVisibility(int visibility);

	public void addStereotypeInstance(IStereotypeInstance instance);

	public void removeStereotypeInstance(IStereotypeInstance instance);

	public void removeStereotypeInstances(IStereotypeInstance[] instances);

	/**
	 * Returns the comment (or plain-english description) associated with this
	 * model component.
	 * 
	 * @return String - the comment
	 */
	public String getComment();

	/**
	 * Returns the name associated with this component.
	 * 
	 * @return String - the name of the component
	 */
	public String getName();

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
