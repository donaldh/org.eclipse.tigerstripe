/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions;

import org.eclipse.jdt.internal.ui.workingsets.ViewActionGroup;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.util.Assert;

public class ViewAction extends Action {

	private final ViewActionGroup fActionGroup;
	private final int fMode;

	public ViewAction(ViewActionGroup group, int mode) {
		super("", AS_RADIO_BUTTON); //$NON-NLS-1$
		Assert.isNotNull(group);
		fActionGroup = group;
		fMode = mode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		if (isChecked())
			fActionGroup.setMode(fMode);
	}
}
