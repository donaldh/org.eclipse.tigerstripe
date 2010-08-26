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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ComponentUtils {

	public static void setEnabledAll(Composite parent, boolean value) {
		for (Control child : parent.getChildren()) {
			child.setEnabled(value);
			if (child instanceof Composite) {
				setEnabledAll((Composite) child, value);
			} 
		}
	}

}
