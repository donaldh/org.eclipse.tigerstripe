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

import java.util.Collection;


/**
 * Interface to be implemented by all components of the model that can be
 * annotated with Stereotypes.
 * 
 * @author Eric Dillon
 * 
 */
public interface IStereotypeCapable {

	/**
	 * Returns an array of stereotype instances. These are the stereotypes (also
	 * referred to as annotations) that are applied to this model component. If
	 * there are no stereotypes applied to this component, the method returns
	 * an empty array.
	 * 
	 * @return array of IStereotypeInstance.
	 */
	public Collection<IStereotypeInstance> getStereotypeInstances();
	/**
	 * Returns a StereotypeInstance with the name supplied.
	 * If there is no instance with that name, null is returned.
	 * @param name
	 * @return IStereotypeInstance
	 */
	public IStereotypeInstance getStereotypeInstanceByName(String name);
	
	/**
	 * Checks for the presence of the named stereotype.
	 *  
	 * @param name
	 * @return 
	 */
	public boolean hasStereotypeInstance(String name);

	public void addStereotypeInstance(IStereotypeInstance instance);

	public void removeStereotypeInstance(IStereotypeInstance instance);

	public void removeStereotypeInstances(Collection<IStereotypeInstance> instances);

	/**
	 * 	Returns a String with the Stereotype lavel a-la-UML
	 * 
	 * <<steo, st2>>
	 * 
	 * or "" if no stereotype instance
	 * 
	 * @return
	 */
	public String getStereotypeString();
	
}
