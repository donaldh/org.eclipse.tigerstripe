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
import org.eclipse.jdt.internal.ui.dnd.EditorInputTransferDragAdapter;
import org.eclipse.jdt.internal.ui.dnd.JdtViewerDragAdapter;
import org.eclipse.jdt.internal.ui.dnd.ResourceTransferDragAdapter;
import org.eclipse.jdt.internal.ui.packageview.FileTransferDragAdapter;
import org.eclipse.jdt.internal.ui.packageview.SelectionTransferDragAdapter;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.part.ResourceTransfer;
import org.eclipse.ui.views.navigator.LocalSelectionTransfer;

public class TigerstripeViewerDragSupport {

	private final StructuredViewer fViewer;
	private JdtViewerDragAdapter fDragAdapter;
	private boolean fStarted;

	public TigerstripeViewerDragSupport(StructuredViewer viewer) {
		fViewer = viewer;

		fDragAdapter = new JdtViewerDragAdapter(fViewer);
		fDragAdapter.addDragSourceListener(new SelectionTransferDragAdapter(
				fViewer));
		fDragAdapter.addDragSourceListener(new EditorInputTransferDragAdapter(
				viewer));
		fDragAdapter.addDragSourceListener(new ResourceTransferDragAdapter(
				fViewer));
		fDragAdapter
				.addDragSourceListener(new FileTransferDragAdapter(fViewer));

		fStarted = false;
	}

	public void start() {
		Assert.isLegal(!fStarted);

		int ops = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;

		Transfer[] transfers = new Transfer[] {
				LocalSelectionTransfer.getInstance(),
				ResourceTransfer.getInstance(), FileTransfer.getInstance() };

		fViewer.addDragSupport(ops, transfers, fDragAdapter);

		fStarted = true;
	}

}
