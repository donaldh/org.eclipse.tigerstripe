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
 * An refactoring support class provide a way to notify framework about URI changes.
 * IRefactoringSupport instance can be retrieved from Annotation Manager
 * 
 * @see IAnnotationManager
 * @author Yuri Strot
 */
public interface IRefactoringSupport {
	
	/**
	 * Notify framework about object with specified URI has been deleted
	 * 
	 * @param uri URI of the deleted object
	 */
	public void deleted(URI uri);
	
	/**
	 * Notify framework about object's URI change
	 * 
	 * @param oldUri old object URI
	 * @param newUri new object URI
	 */
	public void changed(URI oldUri, URI newUri);

}
