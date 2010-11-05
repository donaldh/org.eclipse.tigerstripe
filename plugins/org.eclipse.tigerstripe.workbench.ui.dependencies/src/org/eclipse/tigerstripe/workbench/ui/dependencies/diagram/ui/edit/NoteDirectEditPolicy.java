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
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.figures.NoteFigure;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.parts.NoteEditPart;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Note;

public class NoteDirectEditPolicy extends DirectEditPolicy {

	@Override
	protected Command getDirectEditCommand(DirectEditRequest edit) {
		String text = (String) edit.getCellEditor().getValue();
		NoteEditPart notePart = (NoteEditPart) getHost();
		NoteTextCommand command = new NoteTextCommand(
				(Note) notePart.getModel(), text);
		return command;
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
		String value = (String) request.getCellEditor().getValue();
		((NoteFigure) getHostFigure()).setText(value);
		getHostFigure().getUpdateManager().performUpdate();

	}

}
