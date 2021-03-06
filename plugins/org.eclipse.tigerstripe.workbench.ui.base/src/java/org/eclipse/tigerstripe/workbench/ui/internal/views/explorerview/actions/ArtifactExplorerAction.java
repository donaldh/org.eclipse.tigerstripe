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

import org.eclipse.jdt.ui.actions.SelectionDispatchAction;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TigerstripeExplorerPart;
import org.eclipse.ui.IWorkbenchSite;

public class ArtifactExplorerAction extends SelectionDispatchAction {

	private TigerstripeExplorerPart view;

	public ArtifactExplorerAction(IWorkbenchSite site,
			TigerstripeExplorerPart view) {
		super(site);
		this.view = view;
	}

	protected TigerstripeExplorerPart getView() {
		return this.view;
	}
}
