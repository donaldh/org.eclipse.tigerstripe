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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.jdt.core.IJavaProject;

public class TigerstripeProjectPropertyTester extends
		org.eclipse.core.expressions.PropertyTester {
	private static final String OPEN = "open";

	public TigerstripeProjectPropertyTester() {
	}

	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if ((receiver instanceof IJavaProject) && property.equals(OPEN))
			return ((IJavaProject) receiver).getProject().isOpen() == toBoolean(expectedValue);
		return false;
	}

	private boolean toBoolean(Object expectedValue) {
		if (expectedValue instanceof Boolean) {
			return ((Boolean) expectedValue).booleanValue();
		}
		return true;
	}
}
