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
 * An refactoring support class provide a way to notify framework about changes.
 * Refactoring support should be registered with
 * the <code>org.eclipse.tigerstripe.annotation.core.refactoringSupport</code>
 * extension point.
 * 
 * @author Yuri Strot
 */
public interface IRefactoringSupport {
	
	public void deleted(URI uri);
	
	public void changed(URI newUri, URI oldUri);

}
