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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EPropertyProvider;

/**
 * @author Yuri Strot
 *
 */
public class PropertyProviderContext implements Comparable<PropertyProviderContext> {
	
	private static final String ATTR_CLASS = "class";
	private static final String ATTR_PRIORITY = "priority";
	
	private EPropertyProvider provider;
	private int priority;
	
	public PropertyProviderContext(IConfigurationElement element) throws CoreException {
		provider = (EPropertyProvider)element.createExecutableExtension(ATTR_CLASS);
		priority = Integer.parseInt(element.getAttribute(ATTR_PRIORITY));
	}
	
	/**
	 * 
	 */
	public PropertyProviderContext(EPropertyProvider provider, int priority) {
		this.provider = provider;
		this.priority = priority;
	}
	
	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}
	
	/**
	 * @return the provider
	 */
	public EPropertyProvider getProvider() {
		return provider;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(PropertyProviderContext context) {
		return context.priority - priority;
	}

}
