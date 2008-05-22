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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.repository.MetamodelCorePlugin;

/**
 * Factory for Model Repositories.
 * 
 * Repository types can be registered thru extension point
 * 
 * Note: This is a singleton.
 * 
 * @author erdillon
 * 
 */
public class ModelRepositoryFactory {

	public final static String MODEL_REPOSITORY_EPT = "modelRepositoryProvider";

	public final static ModelRepositoryFactory INSTANCE = new ModelRepositoryFactory();

	private List<IModelRepositoryProvider> providers = new ArrayList<IModelRepositoryProvider>();

	private ModelRepositoryFactory() {
		initialize();
	}

	public IModelRepository createRepository(URI uri) throws ModelCoreException {
		for (IModelRepositoryProvider provider : providers) {
			if (provider.understandsURI(uri)) {
				return provider.create(uri);
			}
		}

		throw new ModelCoreException();
	}

	/**
	 * Read all repository types from extension point
	 * 
	 */
	private void initialize() {

		IConfigurationElement[] elements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(MetamodelCorePlugin.PLUGIN_ID,
						MODEL_REPOSITORY_EPT);
		for (IConfigurationElement element : elements) {
			try {
				IModelRepositoryProvider provider = (IModelRepositoryProvider) element
						.createExecutableExtension("class");
				providers.add(provider);
			} catch (CoreException e) {
				MetamodelCorePlugin.log(e);
			}
		}
	}
}
