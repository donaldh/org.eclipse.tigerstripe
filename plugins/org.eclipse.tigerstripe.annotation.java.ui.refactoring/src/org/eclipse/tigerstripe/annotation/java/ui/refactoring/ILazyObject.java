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
 * This interface used to get lazy access to the java and resource objects.
 * For example, we may get access to the workspace resource by path as following:
 * <pre>
 * 	  ResourcesPlugin.getWorkspace().getRoot().findMember(path);
 * </pre>
 * 
 * This operation will be successful only when resource with the specified path exist. There is
 * why we can't get direct access to the IResource and need lazy access.
 * 
 * @author Yuri Strot
 */
public interface ILazyObject {
	
	/**
	 * Return lazy object
	 * 
	 * @return lazy object
	 */
	public Object getObject();

}
