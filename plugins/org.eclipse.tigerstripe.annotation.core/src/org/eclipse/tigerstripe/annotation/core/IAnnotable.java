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

import org.eclipse.emf.common.util.URI;

/**
 * This interface uses to mark annotable objects. All annotable objects should be adapted to this type 
 * (for example using <code>IAdapterFactory</code>)
 * 
 * @see org.eclipse.core.runtime.IAdaptable
 * @author Yuri Strot
 */
public interface IAnnotable {
	
	/**
	 * Return annotable object URI 
	 * 
	 * @return annotable object URI
	 */
	public URI getUri();

}
