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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.tigerstripe.annotation.core.ProviderContext;

/**
 * @author Yuri Strot
 *
 */
public class ProviderManager {
	
	private Map<String, ProviderContext> ids;
	private Map<String, ProviderContext> types;
	
	public ProviderManager() {
		ids = new HashMap<String, ProviderContext>();
		types = new HashMap<String, ProviderContext>();
	}
	
	public void addProvider(ProviderContext context) {
		ids.put(context.getId(), context);
		types.put(context.getType(), context);
	}
	
	public ProviderContext[] getProviders() {
		return ids.values().toArray(new ProviderContext[ids.size()]);
	}
	
	public ProviderContext getProviderById(String id) {
		return ids.get(id);
	}
	
	public ProviderContext getProviderByType(String type) {
		return types.get(type);
	}
	
	public String[] getAllTypes() {
		return types.keySet().toArray(new String[types.size()]);
	}

}
