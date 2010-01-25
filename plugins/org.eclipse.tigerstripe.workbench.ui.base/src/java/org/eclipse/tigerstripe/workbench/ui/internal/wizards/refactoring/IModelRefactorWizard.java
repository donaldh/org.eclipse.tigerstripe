/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jim Strawn (Cisco Systems, Inc.) - initial implementation
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring;

import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Implementors represent Tigerstripe refactoring wizards.
 * 
 * @see org.eclipse.jface.wizard.IWizard
 */
public interface IModelRefactorWizard {

	/**
	 * Initializes this refactoring wizard using the passed object selection.
	 * <p>
	 * This method is called after the no argument constructor and before other
	 * methods are called.
	 * </p>
	 * 
	 * @param selection
	 *            the current object selection
	 */
	void init(IStructuredSelection selection);

}
