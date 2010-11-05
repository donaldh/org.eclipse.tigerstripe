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

import static java.lang.String.format;

import java.util.Map.Entry;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencySubject;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.parts.SubjectEditPart;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.Utils;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject;

public class SubjectFigure extends RoundedRectangle {

	private final IDependencySubject externalModel;

	private int titleHeight;
	private Image image;

	public SubjectFigure(EditPart part) {
		Shape shape = ((SubjectEditPart) part).getShape();
		externalModel = Utils.findExternalModel((Subject) shape,
				part.getViewer());
		GridLayout layout = new GridLayout();
		setLayoutManager(layout);
		setOpaque(true);

		Font font = part.getViewer().getControl().getFont();

		Image image = externalModel.getType().getImage();
		Label title = new Label(externalModel.getName(), image);
		title.setFont(font);
		title.setForegroundColor(ColorConstants.black);

		titleHeight = title.getPreferredSize().height;
		titleHeight += layout.marginHeight + 2;

		add(title, new GridData(GridData.FILL_HORIZONTAL));
		for (Entry<String, String> entry : externalModel.getProperties()
				.entrySet()) {
			Label label = new Label(format(" %s: %s  ", entry.getKey(),
					entry.getValue()));
			label.setLabelAlignment(PositionConstants.LEFT);
			label.setFont(font);
			label.setForegroundColor(ColorConstants.black);
			add(label, new GridData(GridData.FILL_HORIZONTAL));
		}

		String description = externalModel.getDescription();
		if (description != null && description.length() > 0) {
			setToolTip(new Label(description));
		}
		setSize(getPreferredSize());
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		super.outlineShape(graphics);

		float lineInset = Math.max(1.0f, getLineWidthFloat()) / 2.0f;
		int inset1 = (int) Math.floor(lineInset);
		int inset2 = (int) Math.ceil(lineInset);
		Rectangle r = getBounds();
		int level = r.y + inset1 + titleHeight;
		graphics.drawLine(r.x + inset1, level, r.right() - inset1 + inset2,
				level);
	}

	@Override
	public void removeNotify() {
		super.removeNotify();
		if (image != null) {
			image.dispose();
			image = null;
		}
	}

	@Override
	public String toString() {
		return String
				.format("Figure for '%s' subject", externalModel.getName());
	}

}
