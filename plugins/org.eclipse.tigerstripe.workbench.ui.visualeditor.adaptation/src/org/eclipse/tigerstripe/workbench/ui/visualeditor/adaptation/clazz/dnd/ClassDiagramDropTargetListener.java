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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramDropTargetListener;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.AbstractArtifactAdapter;

public class ClassDiagramDropTargetListener extends DiagramDropTargetListener {

	private ITigerstripeModelProject tsProject = null;

	@Override
	protected void handleDragEnter() {
		// TODO Auto-generated method stub
		super.handleDragEnter();
	}

	public ClassDiagramDropTargetListener(EditPartViewer viewer,
			ITigerstripeModelProject tsProject) {
		super(viewer, LocalSelectionTransfer.getTransfer());
		this.tsProject = tsProject;
	}

	@Override
	/*
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
		TransferData[] data = getCurrentEvent().dataTypes;
		return internalGetObjectsBeingDropped(data);
	}

	protected List<IAbstractArtifact> internalGetObjectsBeingDropped(
			TransferData[] data) {
		List<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();
		for (int i = 0; i < data.length; i++) {
			if (LocalSelectionTransfer.getTransfer().isSupportedType(data[i])) {
				ISelection selection = LocalSelectionTransfer.getTransfer()
						.getSelection();
				if (selection != null
						&& selection instanceof IStructuredSelection) {
					IStructuredSelection sel = (IStructuredSelection) selection;
					for (Iterator iter = sel.iterator(); iter.hasNext();) {
						Object item = iter.next();
						IAbstractArtifact artifact = AbstractArtifactAdapter
								.adapt(item);
						if (artifact != null) {
							result.add(artifact);
						}
					}
				}
				break;
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

		event.detail = DND.DROP_COPY;

		boolean result = true;
		TransferData[] data = event.dataTypes;
		List<IAbstractArtifact> arts = internalGetObjectsBeingDropped(data);
		for (IAbstractArtifact art : arts) {
			try {
				if (tsProject.getArtifactManagerSession()
						.getArtifactByFullyQualifiedName(
								art.getFullyQualifiedName()) == null) {
					event.detail = DND.DROP_NONE;
					return false;
				}
			} catch (TigerstripeException e) {
				return false;
			}
		}
		return result;
	}

}
