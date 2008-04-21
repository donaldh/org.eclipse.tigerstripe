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
package org.eclipse.tigerstripe.annotation.ui.core;

import java.util.Map;

import org.eclipse.emf.common.util.URI;

/**
 * Interface for listening to refactoring changes.
 * 
 * @author Yuri Strot
 */
public interface IRefactoringListener {
	
	/**
	 * Notify that annotable components changed and some
	 * annotations can lost they places
	 */
	public void containerUpdated();
	
	/**
	 * Notify that some annotable objects renamed,
	 * moved or changed in another way. Method provide old and
	 * new URI values
	 * 
	 * @param changes - map of the changed URI
	 */
	public void refactoringPerformed(Map<URI, URI> changes);

}
