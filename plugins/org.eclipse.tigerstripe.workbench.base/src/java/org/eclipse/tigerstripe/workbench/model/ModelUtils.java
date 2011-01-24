/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.model;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

public class ModelUtils {

	public static boolean equalsByFQN(IAbstractArtifact a1, IAbstractArtifact a2) {
		if (a1 == null || a2 == null) {
			return false;
		}

		return a1.getFullyQualifiedName().equals(a2.getFullyQualifiedName());
	}

}
