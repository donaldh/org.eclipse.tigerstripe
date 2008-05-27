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
 * An refactoring support class provide a way to listen refactoring changes.
 * Refactoring support should be registered with
 * the <code>org.eclipse.tigerstripe.annotation.ui.workbenchAnnotationProvider</code>
 * extension point.
 * 
 * @author Yuri Strot
 */
public interface IRefactoringSupport {

	/**
	 * Add refactoring listener
	 * 
	 * @param listener
	 */
	public void addRefactoringListener(IRefactoringListener listener);
	
	/**
	 * Remove refactoring listener
	 * 
	 * @param listener
	 */
	public void removeRefactoringListener(IRefactoringListener listener);

}
