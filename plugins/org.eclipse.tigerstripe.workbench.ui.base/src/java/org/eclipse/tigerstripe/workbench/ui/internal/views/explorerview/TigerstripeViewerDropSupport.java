/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.internal.ui.packageview.FileTransferDropAdapter;
import org.eclipse.jdt.internal.ui.packageview.SelectionTransferDropAdapter;
import org.eclipse.jface.util.DelegatingDropAdapter;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.part.PluginTransfer;
import org.eclipse.ui.views.navigator.LocalSelectionTransfer;

public class TigerstripeViewerDropSupport {

	private final StructuredViewer fViewer;
	private final DelegatingDropAdapter fDelegatingDropAdapter;
	private final SelectionTransferDropAdapter fReorgDropListener;
	private boolean fStarted;

	public TigerstripeViewerDropSupport(StructuredViewer viewer) {
		fViewer = viewer;

		fDelegatingDropAdapter = new DelegatingDropAdapter();
		fDelegatingDropAdapter
				.addDropTargetListener(new LogicalExplorerNodeTransferDropAdapter(
						fViewer));
		fDelegatingDropAdapter
				.addDropTargetListener(new ArtifactComponentTransferDropAdapter(
						fViewer));
		fReorgDropListener = new SelectionTransferDropAdapter(fViewer);
		fDelegatingDropAdapter.addDropTargetListener(fReorgDropListener);
		fDelegatingDropAdapter
				.addDropTargetListener(new FileTransferDropAdapter(fViewer));
		fStarted = false;
	}

	public void addDropTargetListener(TransferDropTargetListener listener) {
		Assert.isLegal(!fStarted);

		fDelegatingDropAdapter.addDropTargetListener(listener);
	}

	public void start() {
		Assert.isLegal(!fStarted);

		int ops = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK
				| DND.DROP_DEFAULT;

		Transfer[] transfers = new Transfer[] {
				LocalSelectionTransfer.getInstance(),
				FileTransfer.getInstance(), PluginTransfer.getInstance() };

		fViewer.addDropSupport(ops, transfers, fDelegatingDropAdapter);

		fStarted = true;
	}

	public void setFeedbackEnabled(boolean enabled) {
		fReorgDropListener.setFeedbackEnabled(enabled);
	}

}
