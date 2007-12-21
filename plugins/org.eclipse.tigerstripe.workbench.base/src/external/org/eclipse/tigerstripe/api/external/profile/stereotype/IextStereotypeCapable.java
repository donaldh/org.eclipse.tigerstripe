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

/**
 * This interface is implemented by all elements in the model that can be
 * annotated with Stereotypes
 * 
 * @author Eric Dillon
 * 
 */
public interface IextStereotypeCapable {

	/**
	 * Returns an array of stereotype instances. These are the stereotypes (also
	 * referred to as annotations) that are applied to this model component. If
	 * there are no stereotypes applied to this componenet, the method returns
	 * an empty array.
	 * 
	 * @return array of IextStereotypeInstance.
	 */
	public IextStereotypeInstance[] getStereotypeInstances();

}
