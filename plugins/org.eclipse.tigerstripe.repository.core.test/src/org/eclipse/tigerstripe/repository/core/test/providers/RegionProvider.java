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
package org.eclipse.tigerstripe.repository.core.test.providers;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.repository.manager.IModelRepository;
import org.eclipse.tigerstripe.repository.manager.IModelRepositoryProvider;
import org.eclipse.tigerstripe.repository.manager.ModelCoreException;

public class RegionProvider implements IModelRepositoryProvider {

	public final static String REPO_EXT = "region";

	public IModelRepository create(URI uri) throws ModelCoreException {
		return new DefaultRepository(uri);
	}

	public <T extends IModelRepository> Class<T>[] getSupportedRepositories() {
		return null;
	}

	public boolean understandsURI(URI uri) {
		return REPO_EXT.equals(uri.fileExtension());
	}

}
