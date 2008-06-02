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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.internal.tools.ModelChangeReducer;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.EditorUndoManager;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.UndoableEdit;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.undo.ModelUndoableEdit;

public class ArtifactEditorUndoManager extends EditorUndoManager {

	private Collection<IModelChangeDelta> reducedDeltas = new ArrayList<IModelChangeDelta>();

	public ArtifactEditorUndoManager(TigerstripeFormEditor editor) {
		super(editor);
	}

	@Override
	public void editorSaved() {

		postDeltas();

		super.editorSaved();
	}

	protected void postDeltas() {
		reducedDeltas.clear();
		Collection<IModelChangeDelta> deltas = new ArrayList<IModelChangeDelta>();

		Collection<UndoableEdit> edits = getEditsSinceLastSave();
		for (UndoableEdit edit : edits) {
			deltas.add((ModelUndoableEdit) edit);
		}

		reducedDeltas = ModelChangeReducer.reduceDeltas(deltas);

		TigerstripeWorkspaceNotifier.INSTANCE.signalModelChange(reducedDeltas
				.toArray(new IModelChangeDelta[reducedDeltas.size()]));
	}

	public IModelChangeDelta[] getDeltaSinceLastSave() {
		return reducedDeltas
				.toArray(new IModelChangeDelta[reducedDeltas.size()]);
	}
}
