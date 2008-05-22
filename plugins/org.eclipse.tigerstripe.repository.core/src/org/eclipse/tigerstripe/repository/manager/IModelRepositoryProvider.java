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

import org.eclipse.emf.common.util.URI;

/**
 * Generic interface to be implemented by clients willing to provide a new kind
 * of IModelRepository
 * 
 * @author erdillon
 * 
 */
public interface IModelRepositoryProvider {

	/**
	 * Core method to actually create the IModelRepository
	 * 
	 * @param uri
	 * @return
	 * @throws ModelCoreException
	 */
	public IModelRepository create(URI uri) throws ModelCoreException;

	/**
	 * This method is used by the framework to determine if this provider
	 * understands the provided URI.
	 * 
	 * @param uri
	 * @return
	 */
	public boolean understandsURI(URI uri);

	public <T extends IModelRepository> Class<T>[] getSupportedRepositories();
}
