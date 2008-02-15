/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.modelManager;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo.PojoModelRepository;

public class ModelRepositoryFactory {

	public static ModelRepositoryFactory INSTANCE = new ModelRepositoryFactory();
	
	private ModelRepositoryFactory() {

	}

	public IModelRepository createRepository(URI repositoryURI, ModelManager manager)
			throws TigerstripeException {
		return new PojoModelRepository(repositoryURI, manager);
	}
}
