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
package org.eclipse.tigerstripe.annotation.core.util;

/**
 * @author Yuri Strot
 *
 */
public class ObjectUtil {
	
	public static boolean equals(Object obj1, Object obj2) {
		if (obj1 == null)
			return obj2 == null;
		if (obj2 == null) return false;
		return obj1.equals(obj2);
	}
	
	public static int hashCode(Object obj) {
		return obj == null ? 0 : obj.hashCode();
	}

}
