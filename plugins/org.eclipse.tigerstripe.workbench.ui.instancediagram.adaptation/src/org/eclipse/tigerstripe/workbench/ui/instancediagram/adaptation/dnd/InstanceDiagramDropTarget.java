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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.dnd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramDropTargetListener;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.editor.FileDiagramEditor;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.tigerstripe.workbench.eclipse.utils.AbstractArtifactAdapter;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

public class InstanceDiagramDropTarget extends DiagramDropTargetListener {

	public InstanceDiagramDropTarget(EditPartViewer viewer,
			FileDiagramEditor editor) {
		super(viewer, LocalSelectionTransfer.getTransfer());
	}

	@Override
	/**
	 * This method figures out the list of artifacts being dropped and returns
	 * them
	 */
	protected List getObjectsBeingDropped() {

		// This is what we need to populate
		List<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();
		result = internalGetObjectsBeingDropped();

		return result;
	}

	/**
	 * Based on the selected elements being dragged, we build a corresponding
	 * list of Abstract artifacts
	 * 
	 * @return
	 */
	protected List<IAbstractArtifact> internalGetObjectsBeingDropped() {

		List<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();

		TransferData[] data = getCurrentEvent().dataTypes;

		for (int i = 0; i < data.length; i++) {
			if (LocalSelectionTransfer.getTransfer().isSupportedType(data[i])) {
				Object obj = LocalSelectionTransfer.getTransfer().nativeToJava(
						data[i]);
				if (obj instanceof IStructuredSelection) {
					IStructuredSelection sel = (IStructuredSelection) obj;
					for (Iterator iter = sel.iterator(); iter.hasNext();) {
						Object item = iter.next();
						IAbstractArtifact artifact = AbstractArtifactAdapter
								.adapt(item);
						if (artifact != null) {
							result.add(artifact);
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * Checks whether the Drag-n-drop operation should be enabled
	 * 
	 * To be enabled, the object being dragged needs to represent an
	 * IAbstractArtifact
	 */
	@Override
	public boolean isEnabled(DropTargetEvent event) {
		boolean result = false;
		TransferData[] data = event.dataTypes;

		for (int i = 0; i < data.length; i++) {
			if (LocalSelectionTransfer.getTransfer().isSupportedType(data[i])) {
				Object obj = LocalSelectionTransfer.getTransfer().nativeToJava(
						data[i]);
				if (obj instanceof IStructuredSelection) {
					IStructuredSelection sel = (IStructuredSelection) obj;
					result = InstanceDiagramDragDropEnablePolicy
							.getInstance()
							.isEnabled(sel,
									getViewer().getRootEditPart().getContents());
				}
			}
		}
		return result;
	}
}
