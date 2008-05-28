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
package org.eclipse.tigerstripe.annotation.core.test.provider;

import org.eclipse.core.runtime.IPath;

/**
 * @author Yuri Strot
 *
 */
public class ResourcePath implements IResourcePath {
	
	private IPath path;
	
	public ResourcePath(IPath path) {
		this.path = path;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.test.provider.IResourcePath#getPath()
	 */
	public IPath getPath() {
		return path;
	}

}
