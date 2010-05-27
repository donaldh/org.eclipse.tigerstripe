/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/

package org.eclipse.tigerstripe.annotation.core.refactoring;

import org.eclipse.emf.common.util.URI;

/**
 * An refactoring delegate class apply URI changes.
 * <p>
 * This interface should not be implemented by clients.
 * 
 * @see IRefactoringChangesListener
 * @author Yuri Strot
 */
public interface IRefactoringDelegate {

	/**
	 * Notify framework about object with specified URI has been deleted
	 * 
	 * @param uri
	 *            URI of the deleted object
	 */
	void deleted(URI uri, boolean affectChildren);

	/**
	 * Notify framework about object's URI change
	 * 
	 * @param oldUri
	 *            old object URI
	 * @param newUri
	 *            new object URI
	 */
	void changed(URI oldUri, URI newUri, boolean affectChildren);

	/**
	 * Notify framework about object with fromUri was copied to object with
	 * toUri
	 * 
	 * @param fromUri
	 * @param toUri
	 */
	void copied(URI fromUri, URI toUri, boolean affectChildren);
}