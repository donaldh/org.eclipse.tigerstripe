/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.diagrams.parts;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Yuri Strot
 *
 */
public class ListLabel extends RoundedRectangle {
	
	private Label[] top;
	private Label[] bottom;
	private Label[] shortLabel;
	
	private EmptyFigure topPart;
	private boolean shortMode;
	
	public static Label[] EMPTY = new Label[0];
	
	public ListLabel(Label[] top, Label[] bottom, Label shortLabel) {
		this(top, bottom, shortLabel, null);
	}
	
	public ListLabel(Label[] top, Label[] bottom, Label shortLabel, String tooltipString) {
		this.top = top;
		this.bottom = bottom;
		this.shortLabel = new Label[] { shortLabel };
		setCornerDimensions(new Dimension(16, 16));
		if (tooltipString != null) {
			setToolTip(new Label(tooltipString));
		}
		setMode(false);
		
	}
	
	public void setMode(boolean shortMode) {
		this.shortMode = shortMode;
		if (shortMode)
			initialize(shortLabel, EMPTY);
		else
			initialize(top, bottom);
	}
	
	public boolean isShortMode() {
		return shortMode;
	}
	
	private void initialize(Label[] top, Label[] bottom) {
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		setLayoutManager(layout);
		removeAll();
		
		topPart = new EmptyFigure();
		addChildren(topPart, top, false);
		add(topPart);
		layout.setConstraint(topPart, new GridData(GridData.FILL_HORIZONTAL));
		
		if (bottom.length > 0) {
			EmptyFigure bottomPart = new EmptyFigure();
			addChildren(bottomPart, bottom, true);
			add(bottomPart);
			layout.setConstraint(bottomPart, new GridData(GridData.FILL_BOTH));
		}
	}
	
	protected void outlineShape(Graphics graphics) {
		super.outlineShape(graphics);
		
		int borderHeight = topPart.getSize().height;
		Rectangle bounds = getBounds();
		if (bounds.height > borderHeight && bottom.length > 0 && shortMode == false) {
			graphics.drawLine(bounds.x, bounds.y + borderHeight, 
					bounds.x + bounds.width, bounds.y + borderHeight);
		}
	}
	
	private void addChildren(IFigure figure, Label[] it, boolean left) {
		GridLayout layout = new GridLayout(left ? 2 : 1, false);
		figure.setLayoutManager(layout);
		for (int i = 0; i < it.length; i++) {
			if (left) {
				EmptyFigure space = new EmptyFigure();
				//space.setSize(15, -1);
				space.setPreferredSize(15, -1);
				figure.add(space);
				layout.setConstraint(space, new GridData(GridData.FILL_HORIZONTAL));
			}
			figure.add(it[i]);
		}
	}

}
