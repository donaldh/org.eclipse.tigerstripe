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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.repository.MetamodelCorePlugin;

/**
 * The key provider is a singleton used to determine the unique key to be used
 * within a {@link IModelRepository} for a stored {@link EObject}.
 * 
 * The key is determined by evaluating the relevant IKeyProvider as registered
 * by the clients.
 * 
 * @author erdillon
 * 
 */
public class KeyService {

	/**
	 * The extension point used to register KeyProvider
	 */
	public final static String PROVIDER_EPT = "keyProvider";

	public final static KeyService INSTANCE = new KeyService();

	private List<IKeyProvider> providers = new ArrayList<IKeyProvider>();

	private KeyService() {
		initialize();
	}

	public Object getKey(EObject obj) throws ModelCoreException {
		for (IKeyProvider provider : providers) {
			if (provider.selects(obj))
				return provider.getKey(obj);
		}
		throw new ModelCoreException("Can't determine key for: " + obj);
	}

	private void initialize() {
		IConfigurationElement[] elements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(MetamodelCorePlugin.PLUGIN_ID,
						PROVIDER_EPT);
		for (IConfigurationElement element : elements) {
			try {
				IKeyProvider provider = (IKeyProvider) element
						.createExecutableExtension("class");
				providers.add(provider);
			} catch (CoreException e) {
				MetamodelCorePlugin.log(e);
			}
		}
	}
}
