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
package org.eclipse.tigerstripe.annotation.internal.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.tigerstripe.annotation.core.ProviderContext;

/**
 * @author Yuri Strot
 *
 */
public class ProviderManager {
	
	private Map<String, ProviderContext> types;
	
	public ProviderManager() {
		types = new ConcurrentHashMap<String, ProviderContext>();
	}
	
	public void addProvider(ProviderContext context) {
		types.put(context.getTarget().getClassName(), context);
	}
	
	public ProviderContext[] getProviders() {
		return types.values().toArray(new ProviderContext[types.size()]);
	}
	
	public ProviderContext getProviderByType(String type) {
		return types.get(type);
	}

}
