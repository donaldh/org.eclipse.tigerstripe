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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.ui.views.framelist.TreeFrame;
import org.eclipse.ui.views.framelist.TreeViewerFrameSource;

public class TigerstripeFrameSource extends TreeViewerFrameSource {
	private TigerstripeExplorerPart tsExplorer;

	public TigerstripeFrameSource(TigerstripeExplorerPart explorer) {
		super(explorer.getTreeViewer());
		tsExplorer = explorer;
	}

	@Override
	protected TreeFrame createFrame(Object input) {
		TreeFrame frame = super.createFrame(input);
		frame.setName(tsExplorer.getFrameName(input));
		frame.setToolTipText(tsExplorer.getToolTipText(input));
		return frame;
	}

	/**
	 * Also updates the title of the packages explorer
	 */
	@Override
	protected void frameChanged(TreeFrame frame) {
		super.frameChanged(frame);
		tsExplorer.updateTitle();
	}

}
