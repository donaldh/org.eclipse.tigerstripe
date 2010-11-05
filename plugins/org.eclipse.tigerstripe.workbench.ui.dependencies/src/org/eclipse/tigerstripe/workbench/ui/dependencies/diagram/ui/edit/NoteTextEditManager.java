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

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.figures.NoteFigure;

public class NoteTextEditManager extends DirectEditManager {

	public NoteTextEditManager(GraphicalEditPart source,
			CellEditorLocator locator) {
		super(source, TextCellEditor.class, locator);
	}

	@Override
	protected void initCellEditor() {
		NoteFigure noteFigure = (NoteFigure) getEditPart().getFigure();
		getCellEditor().setValue(noteFigure.getText());
	}

}
