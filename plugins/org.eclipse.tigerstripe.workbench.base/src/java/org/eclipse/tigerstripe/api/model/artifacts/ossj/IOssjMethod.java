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
package org.eclipse.tigerstripe.api.model.artifacts.ossj;

import org.eclipse.tigerstripe.api.model.IMethod;
import org.eclipse.tigerstripe.core.model.ossj.specifics.EntityMethodFlavorDetails;

/**
 * The OSS/J specifics for a method
 * 
 * @author Eric Dillon
 * 
 */
public interface IOssjMethod extends IMethod {

	public void setInstanceMethod(boolean isInstanceMethod);

	public boolean isInstanceMethod();

	/**
	 * When a method is defined on an entity, it may be exposed thru multiple
	 * flavors.
	 * 
	 * @return an array of the supported flavors identifiers
	 */
	public OssjEntityMethodFlavor[] getSupportedFlavors();

	public void setSupportedFlavors(OssjEntityMethodFlavor[] supportedFlavors);

	public EntityMethodFlavorDetails getFlavorDetails(
			OssjEntityMethodFlavor flavor);

	public void setFlavorDetails(OssjEntityMethodFlavor flavor,
			EntityMethodFlavorDetails details);

	/**
	 * Returns a clone of this
	 * 
	 * @return
	 */
	public IOssjMethod cloneToIOssjMethod();

	public void setDefaultFlavors();
}
