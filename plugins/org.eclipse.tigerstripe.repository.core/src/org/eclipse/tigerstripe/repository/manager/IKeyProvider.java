/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.repository.manager;

import org.eclipse.emf.ecore.EObject;

/**
 * An {@link IKeyProvider} is used to determine the Key to be used in a
 * ModelRepository for an {@link EObject}
 * 
 * They can be registered thru
 * 
 * @author erdillon
 * 
 */
public interface IKeyProvider {

	/**
	 * Returns true if this provider can be used against the given obj
	 * 
	 * @param obj
	 */
	public boolean selects(EObject obj);

	/**
	 * returns the key for the given {@link EObject} based on this provider
	 * 
	 * @param obj
	 * @return
	 * @throws ModelCoreException
	 */
	public Object getKey(EObject obj) throws ModelCoreException;
}
