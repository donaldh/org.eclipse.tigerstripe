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
package org.eclipse.tigerstripe.annotation.ui.util;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * This class need for make easier operations with running async exec
 * 
 * @author Yuri Strot
 */
public class AsyncExecUtil {
	
	/**
	 * This method using all needed dispose checking for you
	 * 
	 * @param control, which Display will be used
	 * @param runnable, running operation
	 */
	public static void run(final Control control, final Runnable runnable) {
		if (control == null || control.isDisposed()) return;
		Display display = control.getDisplay();
		if (display != null && !display.isDisposed()) {
			display.asyncExec(new Runnable() {
			
				public void run() {
					if (!control.isDisposed())
						runnable.run();
				}
			
			});
		}
	}

}
