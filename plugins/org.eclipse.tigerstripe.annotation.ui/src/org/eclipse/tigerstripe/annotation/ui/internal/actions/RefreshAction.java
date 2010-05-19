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
package org.eclipse.tigerstripe.annotation.ui.internal.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.tigerstripe.annotation.ui.Images;

/**
 * @author Yuri Strot
 * 
 */
public class RefreshAction extends Action {

	private Viewer viewer;

	public RefreshAction(Viewer viewer) {
		super("Refresh", Images.getDescriptor(Images.REFRESH));
		this.viewer = viewer;
	}

	@Override
	public void run() {
		viewer.refresh();
	}

}
