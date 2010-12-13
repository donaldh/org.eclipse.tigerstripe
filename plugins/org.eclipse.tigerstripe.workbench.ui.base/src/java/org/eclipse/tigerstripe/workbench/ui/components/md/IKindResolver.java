/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.components.md;

/**
 * Resolve the kind of object
 */
public interface IKindResolver {

	/**
	 * Does the right of the resolver to resolve this object
	 * 
	 * @param object
	 *            to resolve
	 */
	boolean canResolve(Object target);

	/**
	 * @return true if target corresponds to object source
	 */
	boolean equalsKind(Object target, Object source);

}
