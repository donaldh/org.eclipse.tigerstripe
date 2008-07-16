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
package org.eclipse.tigerstripe.annotation.ui.internal.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EProperty;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EPropertyProvider;

/**
 * @author Yuri Strot
 *
 */
public class PropertyRegistry {
	
	private static PropertyProviderContext[] providers;
	
	private static final String PROPERTY_PROVIDER_EXTPT = 
		AnnotationUIPlugin.PLUGIN_ID + ".propertyProvider";
	
	protected static PropertyProviderContext[] getProviders() {
		if (providers == null) {
			List<PropertyProviderContext> providers = new ArrayList<PropertyProviderContext>();
			IConfigurationElement[] configs = Platform.getExtensionRegistry(
				).getConfigurationElementsFor(PROPERTY_PROVIDER_EXTPT);
	        for (IConfigurationElement config : configs) {
	        	try {
	        		PropertyProviderContext context = new PropertyProviderContext(config);
	        		providers.add(context);
	            }
	            catch (Exception e) {
	                AnnotationUIPlugin.log(e);
	            }
	        }
	        Collections.sort(providers);
	        PropertyRegistry.providers = providers.toArray(new PropertyProviderContext[providers.size()]);
		}
		return providers;
	}
	
	public static EProperty getProperty(EObject object, EStructuralFeature feature) {
		for (PropertyProviderContext context : getProviders()) {
			EPropertyProvider provider = context.getProvider();
			EProperty property = provider.getProperty(object, feature);
			if (property != null)
				return property;
		}
		return null;
	}

}
