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
package org.eclipse.tigerstripe.annotation.java.ui.refactoring;

/**
 * Refactoring changes listener
 * 
 * @author Yuri Strot
 */
public interface IRefactoringChangesListener {
	
	public static final int ABOUT_TO_CHANGE = 1;
	
	public static final int CHANGED = 2;
	
	public void deleted(ILazyObject object);
	
	public void changed(ILazyObject objObject, ILazyObject newObject, int kind);

}
