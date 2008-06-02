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

import org.eclipse.emf.common.util.URI;

/**
 * Default implementation of the <code>IRefactoringSupport</code>
 * 
 * @author Yuri Strot
 */
public class RefactoringSupport implements IRefactoringSupport {
	
	private IRefactoringHelper helper;
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IRefactoringSupport#setRefactoringHelper(org.eclipse.tigerstripe.annotation.core.IRefactoringHelper)
	 */
	public void initRefactoringHelper(IRefactoringHelper helper) {
		this.helper = helper;
	}
	
	protected void fireContainerUpdated() {
		helper.fireContainerUpdated();
	}
	
	protected void fireRefactoringPerformed(Map<URI, URI> changes) {
		helper.fireRefactoringPerformed(changes);
	}

}
