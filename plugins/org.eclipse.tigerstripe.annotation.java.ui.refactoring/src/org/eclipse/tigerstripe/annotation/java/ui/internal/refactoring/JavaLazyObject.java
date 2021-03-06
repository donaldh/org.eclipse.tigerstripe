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
package org.eclipse.tigerstripe.annotation.java.ui.internal.refactoring;

import org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject;
import org.eclipse.tigerstripe.annotation.core.util.ObjectUtil;

/**
 * @author Yuri Strot
 *
 */
public class JavaLazyObject implements ILazyObject {
	
	private String project;
	private String path;
	
	public JavaLazyObject(String project, String path) {
		this.project = project;
		this.path = path;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.java.ui.refactoring.ILazyObject#getObject()
	 */
	public Object getObject() {
		return RefactoringUtil.getJavaElement(project, path);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		JavaLazyObject jzo = (JavaLazyObject)obj;
		return ObjectUtil.equals(project, jzo.project) &&
			ObjectUtil.equals(path, jzo.path);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return ObjectUtil.hashCode(project) >> 16 ^ ObjectUtil.hashCode(path);
	}

}
