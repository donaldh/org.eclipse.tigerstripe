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
package org.eclipse.tigerstripe.annotation.core;

import java.util.HashMap;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;

/**
 * @author Yuri Strot
 *
 */
public class ResourceSetFactory {
	
	private static AdapterFactoryEditingDomain editDomain;
	
	public static ResourceSet getResourceSet() {
		return getEditDomain().getResourceSet();
	}
	
	public static AdapterFactoryEditingDomain getEditDomain() {
		if (editDomain == null) {
			ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

    		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
    		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
    		
    		BasicCommandStack commandStack = new BasicCommandStack();
    		
    		editDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack, new HashMap<Resource, Boolean>());
		}
		return editDomain;
	}

}
