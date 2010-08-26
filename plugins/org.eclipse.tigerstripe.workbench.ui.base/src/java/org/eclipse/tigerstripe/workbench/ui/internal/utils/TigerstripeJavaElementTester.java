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
package org.eclipse.tigerstripe.workbench.ui.internal.utils;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;

public class TigerstripeJavaElementTester extends PropertyTester {

	private static final String ON_BUILD_PATH = "onBuildPath";
	private static final String IN_ARHIVE = "inArchive";

	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {

		if (!(receiver instanceof IJavaElement)) {
			return false;
		}
		
		IJavaElement javaElement = (IJavaElement) receiver;
		if (ON_BUILD_PATH.equals(property)) {
			return javaElement.getJavaProject().isOnClasspath(javaElement);
		} else if (IN_ARHIVE.equals(property)) {
			IPackageFragmentRoot root = JavaModelUtil
					.getPackageFragmentRoot(javaElement);
			return (root != null) && root.isArchive();
		} else {
			throw new IllegalArgumentException(String.format(
					"Property '%s' not supported", property));
		}
	}
}
