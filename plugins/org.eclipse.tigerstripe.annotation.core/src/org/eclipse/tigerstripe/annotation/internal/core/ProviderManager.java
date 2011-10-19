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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.eclipse.tigerstripe.annotation.core.ProviderContext;

/**
 * This class store the provider managers
 * 
 * @author Valentin Yerastov
 */
public class ProviderManager {
	
	private final Map<String, ProviderContext> types;
	
	public ProviderManager(Map<String, ProviderContext> types) {
		this.types = Collections.unmodifiableMap(types);
	}
	
	public Collection<ProviderContext> getProviders() {
		return types.values();
	}
	
	public ProviderContext getProviderByType(String type) {
		return types.get(type);
	}
}
