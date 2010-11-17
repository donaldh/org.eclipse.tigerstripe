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

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class NoteFigure extends RectangleFigure {

	private TextFlow text;

	public NoteFigure(EditPart part) {
		GridLayout layout = new GridLayout();
		layout.marginHeight = 3;
		setLayoutManager(layout);
		setOpaque(true);
		setForegroundColor(ColorConstants.gray);
		setBackgroundColor(ColorConstants.lightGreen);

		Control control;
		Font font = (control = part.getViewer().getControl()) == null ? new Font(
				null, "Arial", 10, SWT.NONE) : control.getFont();

		text = new TextFlow("Notice");
		text.setFont(font);
		text.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		FlowPage fp = new FlowPage();
		fp.add(text);

		GridData constraint = new GridData();
		constraint.grabExcessHorizontalSpace = true;
		constraint.grabExcessVerticalSpace = true;
		constraint.verticalAlignment = SWT.TOP;
		constraint.horizontalAlignment = SWT.CENTER;
		add(fp, constraint);
		
//		setBorder(new NoteFigureBorder(new Insets(1)));
		updateTextLabelSize();
		setSize(getPreferredSize());
	}

	public void updateTextLabelSize() {
		
	}
	
	public void setText(String value) {
		text.setText(value);
		layout();
	}

	public String getText() {
		return text.getText();
	}

	public IFigure getEditComponent() {
		return text;
	}
	
	public class NoteFigureBorder extends AbstractBorder {
		private Insets margin;
		NoteFigureBorder(Insets insets) {
			margin = insets;
		}	
		
		/**
		 * Returns margin for this border
		 * @return margin as Insets
		 */
		public Insets getMargin() {
			return margin;
		}

		/**
		 * Sets the margin for this border 
		 * @param margin as Insets
		 */
		public void setMargin(Insets margin) {
			this.margin = margin;
		}


		/*
		 * @see org.eclipse.draw2d.Border#getInsets(org.eclipse.draw2d.IFigure)
		 */
		public Insets getInsets(IFigure figure) {
			NoteFigure noteFigure = (NoteFigure)figure;
			int width = noteFigure.getLineWidth();
			return new Insets(width + margin.top, width + margin.left, 
					width + margin.bottom, width + margin.right);
		}


		/* 
		 * @see org.eclipse.draw2d.Border#paint(org.eclipse.draw2d.IFigure, org.eclipse.draw2d.Graphics, org.eclipse.draw2d.geometry.Insets)
		 */
		public void paint(IFigure figure, Graphics g, Insets insets) {
			NoteFigure noteFigure = (NoteFigure)figure;
			Rectangle r = noteFigure.getBounds().getCopy();
			r.shrink(noteFigure.getLineWidth() / 2, noteFigure.getLineWidth() / 2);
			
			PointList p = noteFigure.getPointList(r);
			p.addPoint(r.x, r.y - noteFigure.getLineWidth() / 2);
			g.setLineWidth(noteFigure.getLineWidth());  
			g.setLineStyle(noteFigure.getLineStyle());  
			g.drawPolyline(p);
			
			PointList corner = new PointList();
			int clipWidth = 15, clipHeight = 15;
			corner.addPoint(r.x + r.width - clipWidth, r.y);
			corner.addPoint(r.x + r.width - clipWidth, r.y + clipHeight);
			corner.addPoint(r.x + r.width, r.y + clipHeight);
			g.drawPolyline(corner);			
		}
	}
	
	protected PointList getPointList(Rectangle r) {

		PointList p = new PointList();
		
		p.addPoint(r.x, r.y);
		p.addPoint(r.x + r.width - 1, r.y) ;
		p.addPoint(r.x + r.width - 1, r.y + r.height - 1);
		p.addPoint(r.x, r.y + r.height - 1);
		p.addPoint(r.x, r.y);

		return p;
	}

	
}
