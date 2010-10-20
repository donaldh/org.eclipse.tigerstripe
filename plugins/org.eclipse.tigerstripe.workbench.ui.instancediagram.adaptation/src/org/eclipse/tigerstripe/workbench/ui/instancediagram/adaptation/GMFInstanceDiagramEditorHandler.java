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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.editor.FileDiagramEditor;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.dnd.InstanceDiagramDropTargetListener;

/**
 * Base class to handle synchronization and features between Tigerstripe core
 * and GMF-based editor.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class GMFInstanceDiagramEditorHandler {

	private FileDiagramEditor editor;

	private InstanceDiagramSynchronizer synchronizer;

	public GMFInstanceDiagramEditorHandler(FileDiagramEditor editor) {
		this.editor = editor;
		this.synchronizer = new InstanceDiagramSynchronizer(editor);
		initializeInMap();
	}

	public void initialize() {
		synchronizer.startSynchronizing();
		editor.getDiagramGraphicalViewer().addDropTargetListener(
				new InstanceDiagramDropTargetListener(editor
						.getDiagramGraphicalViewer()));
	}

	public void initializeInMap() {
		synchronizer.initializeTSProjectInMap();
	}

	public void dispose() {
		synchronizer.stopSynchronizing();
	}

}
