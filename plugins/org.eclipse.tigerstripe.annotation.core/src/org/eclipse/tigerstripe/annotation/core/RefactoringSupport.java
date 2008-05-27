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

import java.util.Map;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.emf.common.util.URI;

/**
 * Default implementation of the <code>IRefactoringSupport</code>
 * 
 * @author Yuri Strot
 */
public class RefactoringSupport implements IRefactoringSupport {
	
	private ListenerList refactorListeners = new ListenerList();
	
	protected void fireContainerUpdated() {
		Object[] objects = refactorListeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			IRefactoringListener listener = (IRefactoringListener)objects[i];
			listener.containerUpdated();
        }
	}
	
	protected void fireRefactoringPerformed(Map<URI, URI> changes) {
		Object[] objects = refactorListeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			IRefactoringListener listener = (IRefactoringListener)objects[i];
			listener.refactoringPerformed(changes);;
        }
	}

	public void addRefactoringListener(IRefactoringListener listener) {
		refactorListeners.add(listener);
    }

	public void removeRefactoringListener(IRefactoringListener listener) {
		refactorListeners.remove(listener);
    }

}
