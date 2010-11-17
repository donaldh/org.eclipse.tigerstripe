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
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Control;

public class NoteFigure extends RoundedRectangle {

	private Label text;

	public NoteFigure(EditPart part) {
		GridLayout layout = new GridLayout();
		setLayoutManager(layout);
		setOpaque(true);
		setForegroundColor(ColorConstants.gray);
		setBackgroundColor(ColorConstants.lightGreen);

		Control control;
		Font font = (control = part.getViewer().getControl()) == null ? new Font(
				null, "Arial", 10, SWT.NONE) : control.getFont();

		text = new Label("Notice");
		text.setFont(font);
		text.setForegroundColor(ColorConstants.black);

		add(text, new GridData(GridData.FILL_HORIZONTAL));

		setSize(getPreferredSize());
	}

	public void setText(String value) {
		text.setText(value);
	}

	public String getText() {
		return text.getText();
	}

	public IFigure getEditComponent() {
		return text;
	}
}
