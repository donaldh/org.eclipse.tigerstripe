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


/**
 * @author Yuri Strot
 *
 */
public interface IProviderTarget {
	
	/**
	 * @param object
	 * @return
	 */
	public Object adapt(Object object);
	
	/**
	 * @return the className
	 */
	public String getClassName();
	
	/**
	 * @return the description
	 */
	public String getDescription();

}
