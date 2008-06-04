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
 *
 * @author Yuri Strot
 */
public class RefactoringChange {
	
	public static final int URI_CHANGED = 1;
	public static final int URI_DELETED = 2;
	
	private URI oldUri;
	private URI newUri;
	private int kind;
	
	public RefactoringChange(URI oldUri, URI newUri) {
		this.oldUri = oldUri;
		this.newUri = newUri;
		kind = URI_CHANGED;
	}
	
	public RefactoringChange(URI uri) {
		this.oldUri = uri;
		kind = URI_DELETED;
	}
	
	public URI getOldUri() {
		return oldUri;
	}
	
	public URI getNewUri() {
		return newUri;
	}
	
	/**
	 * @return the kind
	 */
	public int getKind() {
		return kind;
	}

}
