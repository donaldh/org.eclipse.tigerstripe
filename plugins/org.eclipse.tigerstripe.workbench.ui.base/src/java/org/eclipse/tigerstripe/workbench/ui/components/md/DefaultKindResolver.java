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
 * Default implementation of the resolver kind
 */
public class DefaultKindResolver implements IKindResolver {

	public Object toKind(Object object) {
		return object.getClass();
	}

	public boolean canResolve(Object target) {
		return true;
	}

	public boolean equalsKind(Object target, Object source) {
		if (source instanceof Class<?>) {
			return ((Class<?>) source).isInstance(target);
		} else {
			return false;
		}
	}

}
