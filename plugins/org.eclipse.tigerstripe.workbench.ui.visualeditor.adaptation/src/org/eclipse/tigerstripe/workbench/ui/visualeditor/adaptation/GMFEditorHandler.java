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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.editor.FileDiagramEditor;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd.ClassDiagramDropTargetListener;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizer;

/**
 * Base class to handle synchronization and features between Tigerstripe core
 * and GMF-based editor.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class GMFEditorHandler {

	private FileDiagramEditor editor;

	private ClassDiagramSynchronizer synchronizer;

	public GMFEditorHandler(FileDiagramEditor editor) {
		this.editor = editor;
		this.synchronizer = new ClassDiagramSynchronizer(editor);
		initializeInMap();
	}

	public void initialize() {
		synchronizer.startSynchronizing();
		Map map = (Map) editor.getDiagram().getElement();
		ITigerstripeModelProject tsProject = map
				.getCorrespondingITigerstripeProject();
		editor.getDiagramGraphicalViewer().addDropTargetListener(
				new ClassDiagramDropTargetListener(editor
						.getDiagramGraphicalViewer(), tsProject));
	}

	public void initializeInMap() {
		synchronizer.initializeTSProjectInMap();
	}

	public void dispose() {
		synchronizer.stopSynchronizing();
	}

}
