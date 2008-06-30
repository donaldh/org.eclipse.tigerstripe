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
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.DiagramColorConstants;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author Yuri Strot
 *
 */
public class UnknownAnnotationEditPart extends ShapeNodeEditPart {
	
	public UnknownAnnotationEditPart(View view) {
		super(view);
	}

	/**
	 * Rectangle figure with error text.
	 */
	static class DefaultNodeFigure
		extends DefaultSizeNodeFigure {

		public DefaultNodeFigure(int width, int height) {
			super(width, height);
		}

		protected void paintFigure(Graphics g) {
			Rectangle r = Rectangle.SINGLETON;
			r.setBounds(getBounds());

			g.setBackgroundColor(DiagramColorConstants.diagramLightRed);
			g.fillRectangle(r);

			g.setForegroundColor(DiagramColorConstants.black);
			r.width--;
			r.height--;
			g.drawRectangle(r);

			String txt = "Unknown Annotation";

			IMapMode mm = MapModeUtil.getMapMode(this);
			Dimension td = FigureUtilities.getTextExtents(txt, g.getFont());
			mm.DPtoLP(td);
			Point p = FigureUtilities.getLocation(
				PositionConstants.NORTH_SOUTH, td, r);
			g.drawString(txt, p);
			setPreferredSize(td.expand(mm.DPtoLP(10), mm.DPtoLP(10)));
		}
	}


	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart#createNodeFigure()
	 */
	protected NodeFigure createNodeFigure() {
		return new DefaultNodeFigure(getMapMode().DPtoLP(40), getMapMode().DPtoLP(40));
	}

}
