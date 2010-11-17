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
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.edit.FigureCellEditorLocator;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.edit.NoteDirectEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.edit.NoteTextEditManager;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.figures.NoteFigure;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Note;

public class NoteEditPart extends ShapeEditPart {

	@Override
	protected IFigure doCreateFigure() {
		NoteFigure noteFigure = new NoteFigure(this);
		noteFigure.setText(getNote().getText());
		return noteFigure;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new NoteDirectEditPolicy());
	}

	public Note getNote() {
		return (Note) getModel();
	}

	@Override
	public void performRequest(Request req) {
		if (RequestConstants.REQ_DIRECT_EDIT.equals(req.getType())) {
			NoteTextEditManager manager = new NoteTextEditManager(this,
					new FigureCellEditorLocator(
							((NoteFigure) getFigure()).getEditComponent()));
			manager.show();
		}
	}
}
