/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.editor;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.layout.LayoutUtils;

public class LayoutAction extends Action {

	private EditPart container;

	public LayoutAction(EditPart container) {
		super("Layout");
		this.container = container;
	}

	@Override
	public void run() {
		LayoutUtils.layout(container, true);
	}

}
