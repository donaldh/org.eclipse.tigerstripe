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

import org.eclipse.tigerstripe.api.external.profile.stereotype.IextStereotypeCapable;

/**
 * Interface to be implemented by all components of the model that can be
 * annotated with Stereotypes.
 * 
 * @author Eric Dillon
 * 
 */
public interface IStereotypeCapable extends IextStereotypeCapable {

	/**
	 * Returns all the stereotype instances for this
	 * 
	 * @return
	 */
	public IStereotypeInstance[] getStereotypeInstances();

	public void addStereotypeInstance(IStereotypeInstance instance);

	public void removeStereotypeInstance(IStereotypeInstance instance);

	public void removeStereotypeInstances(IStereotypeInstance[] instances);

}
