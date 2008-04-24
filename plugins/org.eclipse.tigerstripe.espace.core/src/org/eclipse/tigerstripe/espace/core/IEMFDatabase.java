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
package org.eclipse.tigerstripe.espace.core;

import org.eclipse.emf.ecore.EObject;

/**
 * This interface represent persistence service which used for storing/retrieving
 * annotation objects (but it's able to persist any EMF object as well). Upon initialization service 
 * install available content-based routers which used to route annotations to resources.
 * Also service provide default routing scenario. Currently it store all objects
 * in the single file in the workspace metadata.
 * 
 * @see EObjectRouter
 * @author Yuri Strot
 */
public interface IEMFDatabase {
	
	/**
	 * Save EMF object to the specific resource
	 * 
	 * @param object EMF object
	 */
	public void write(EObject object);
	
	/**
	 * Remove EMF object from the specific resource
	 * 
	 * @param object
	 */
	public void remove(EObject object);
	
	/**
	 * Read all previously saved EMF objects
	 * 
	 * @return previously saved EMF objects or empty array if none
	 */
	public EObject[] read();

}
