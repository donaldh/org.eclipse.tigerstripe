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
package org.eclipse.tigerstripe.annotation.core.storage.internal;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.annotation.core.IProviderTarget;

/**
 * @author Yuri Strot
 *
 */
public class ProviderTarget implements IProviderTarget {
	
	private final String className;
	private final String description;
	
	public ProviderTarget(String className, String description) {
		this.className = className;
		this.description = description;
	}
	
	public Object adapt(Object object) {
		Object adapted = Platform.getAdapterManager().loadAdapter(object, className);
		if (adapted != null)
			return adapted;
		try {
			Class<?> clazz = Class.forName(className, true, object.getClass().getClassLoader());
			adapted = Platform.getAdapterManager().getAdapter(object, clazz);
			if (adapted == null && object instanceof IAdaptable) {
				adapted = ((IAdaptable) object).getAdapter(clazz);
			}
			return adapted;
		} catch (ClassNotFoundException e) {
		}
		return null;
	}
	
	public String getClassName() {
		return className;
	}
	
	public String getDescription() {
		return description;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ProviderTarget) {
			ProviderTarget target = (ProviderTarget)obj;
			return target.getClassName().equals(getClassName());
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getClassName().hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClassName();
	}

}
