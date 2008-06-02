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
package org.eclipse.tigerstripe.workbench.ui.internal.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This is intended to accumulate all the commands issued by the user of an
 * Editor with the option to "reduce" the commands
 * 
 * @author erdillon
 * 
 */
public class EditorUndoManager {

	private final static int NEVER = -1;

	private TigerstripeFormEditor editor;

	private List<UndoableEdit> edits;

	private int lastSavedIndex = NEVER;

	public EditorUndoManager(TigerstripeFormEditor editor) {
		this.editor = editor;
		edits = new ArrayList<UndoableEdit>();
	}

	public TigerstripeFormEditor getEditor() {
		return this.editor;
	}

	public void addEdit(UndoableEdit edit) {
		edits.add(edit);
	}

	/**
	 * To be called by the editor when it is saved. This allows to capture the
	 * index of the save.
	 */
	public void editorSaved() {
		edits.clear();
		lastSavedIndex = edits.size();
	}

	public Collection<UndoableEdit> getAllEdits() {
		return Collections.unmodifiableCollection(edits);
	}

	public Collection<UndoableEdit> getEditsSinceLastSave() {
		int index = lastSavedIndex;
		if (index == NEVER)
			index = 0;

		List<UndoableEdit> lastEdits = new ArrayList<UndoableEdit>();
		for (Iterator<UndoableEdit> iterator = edits.listIterator(index); iterator
				.hasNext();) {
			lastEdits.add(iterator.next());
		}
		return lastEdits;
	}
}
