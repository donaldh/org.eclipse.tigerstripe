/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.espace.resources.core;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

/**
 * This interface used to map object to corresponding storage.
 * It can be implemented by the user and registered with the
 * <code>org.eclipse.tigerstripe.annotation.core.router</code>
 * extension point.
 * 
 * @author Yuri Strot
 */
public interface EObjectRouter {
	
	/**
	 * Route object to some URI. If this object can not be routed, this
	 * method shall return null. This URI will be used to get resource with
	 * <code>ResourceSet.createResource(URI)</code> and
	 * <code>ResourceSet.getResource(URI, boolean)</code> methods. 
	 * 
	 * @param object object to route
	 * @return resource URI where this object routed or null, if this EMF object
	 * can not be routed with this <code>EObjectRouter</code>
	 */
	public URI route(EObject object);

}
