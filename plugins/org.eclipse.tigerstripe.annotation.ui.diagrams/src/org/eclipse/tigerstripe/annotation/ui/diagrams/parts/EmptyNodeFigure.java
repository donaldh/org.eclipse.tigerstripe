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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;

/**
 * @author Yuri Strot
 *
 */
public class EmptyNodeFigure extends NodeFigure {
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#isShowing()
	 */
	@Override
	public boolean isShowing() {
		return super.isShowing();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		return super.getPreferredSize(wHint, hHint);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#getMinimumSize(int, int)
	 */
	@Override
	public Dimension getMinimumSize(int wHint, int hHint) {
		if (wHint < 0)
			wHint = 0;
		if (hHint < 0)
			hHint = 0;
		return new Dimension(wHint, hHint);
	}

}
