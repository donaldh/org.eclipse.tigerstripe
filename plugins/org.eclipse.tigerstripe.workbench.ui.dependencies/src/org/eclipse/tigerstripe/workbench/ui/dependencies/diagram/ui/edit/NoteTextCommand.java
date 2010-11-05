/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.edit;

import org.eclipse.gef.commands.Command;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Note;

public class NoteTextCommand extends Command {

	private String newText, oldText;
	private Note note;

	public NoteTextCommand(Note note, String value) {
		this.note = note;
		if (value != null) {
			newText = value;
		} else {
			newText = ""; //$NON-NLS-1$
		}
	}

	@Override
	public void execute() {
		oldText = note.getText();
		note.setText(newText);
	}

	@Override
	public void undo() {
		note.setText(oldText);
	}

}
