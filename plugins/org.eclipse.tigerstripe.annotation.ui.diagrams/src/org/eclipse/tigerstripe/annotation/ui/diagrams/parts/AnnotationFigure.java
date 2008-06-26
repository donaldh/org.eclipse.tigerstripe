/**
 * 
 */
package org.eclipse.tigerstripe.annotation.ui.diagrams.parts;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationFigure extends DefaultSizeNodeFigure {

	private boolean withDanglingCorner = true;
	private int lineWidth = 1;  

	/**
	 * the clip height constant in device coordinates
	 */
	static public final int CLIP_HEIGHT_DP = 12;
	
	/**
	 * the margin constant in device coordinates
	 */
	static public final int MARGIN_DP = 5;
	
	/**
	 * the clip margin constant in device coordinates
	 */
	static public final int CLIP_MARGIN_DP = 14;
		
	/**
	 * Constructor
	 * 
	 * @param width <code>int</code> value that is the default width in logical units
	 * @param height <code>int</code> value that is the default height in logical units
	 * @param insets <code>Insets</code> that is the empty margin inside the note figure in logical units
	 */
	public AnnotationFigure(int width, int height, Insets insets) {
		super(width, height);
		setBorder(
			new MarginBorder(insets.top, insets.left, insets.bottom, insets.right));

		ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
		layout.setMinorAlignment(ConstrainedToolbarLayout.ALIGN_TOPLEFT);
		layout.setSpacing(insets.top);
		setLayoutManager(layout);
	}
	
	private int getClipHeight() {
		return MapModeUtil.getMapMode(this).DPtoLP(12);
	}
	
	private int getClipWidth() {
		return getClipHeight() + MapModeUtil.getMapMode(this).DPtoLP(1);
	}
	
	/**
	 * Method getPointList.
	 * @param r
	 * @return PointList
	 */
	protected PointList getPointList(Rectangle r) {

		PointList p = new PointList();
		
		p.addPoint(r.x, r.y);
		p.addPoint(r.x + r.width - 1, r.y) ;
		p.addPoint(r.x + r.width - 1, r.y + r.height - getClipHeight()) ;
		p.addPoint(r.x + r.width - getClipWidth(), r.y + r.height - 1) ;
		p.addPoint(r.x, r.y + r.height - 1);
		p.addPoint(r.x, r.y);

		return p;
	}

	protected void paintBorder(Graphics g) {
		Rectangle r = getBounds();
		
		PointList p = getPointList(r);
		g.setLineWidth(lineWidth);  
		g.drawPolyline(p);

		if (withDanglingCorner) {
			PointList corner = new PointList();
			corner.addPoint(r.x + r.width - getClipWidth(), r.y + r.height);
			corner.addPoint(r.x + r.width - getClipWidth(), r.y + r.height - getClipHeight());
			corner.addPoint(r.x + r.width, r.y + r.height - getClipHeight());
			g.drawPolyline(corner);			
		}
	}


	protected void paintFigure(Graphics g) {
		super.paintFigure(g);
		Rectangle r = getBounds();
		PointList p = getPointList(r);
		g.fillPolygon(p);
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		return super.getPreferredSize(wHint, hHint).getUnioned(new Dimension(
								MapModeUtil.getMapMode(this).DPtoLP(100), 
								MapModeUtil.getMapMode(this).DPtoLP(50)));
	}

}
