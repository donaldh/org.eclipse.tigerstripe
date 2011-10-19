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
package org.eclipse.tigerstripe.annotation.core.refactoring;

import org.eclipse.core.runtime.ListenerList;

/**
 * Composite refactoring listener
 * 
 * @author Yuri Strot
 */
public class CompositeRefactorListener implements IRefactoringListener {
	
	private ListenerList listeners = new ListenerList();
	
	public CompositeRefactorListener() {
	}
	
	public void addListener(IRefactoringListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(IRefactoringListener listener) {
		listeners.remove(listener);
	}
	
	public void refactoringPerformed(RefactoringChange change) {
		Object[] objects = listeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			IRefactoringListener listener = (IRefactoringListener)objects[i];
			listener.refactoringPerformed(change);
        }
	}
	
}