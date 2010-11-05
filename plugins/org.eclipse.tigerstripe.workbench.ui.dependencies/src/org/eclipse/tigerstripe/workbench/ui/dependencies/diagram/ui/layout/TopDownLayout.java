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
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.layout;

import static org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.Utils.toDimension;
import static org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.Utils.toPoint;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape;

public class TopDownLayout extends BaseLayout {

	@Override
	protected Point getLocation(GraphicalEditPart node) {
		return toPoint(shape(node).getLocation());
	}

	@Override
	protected Dimension getSize(GraphicalEditPart node) {
		return toDimension(shape(node).getSize());
	}

	private Shape shape(GraphicalEditPart node) {
		return (Shape) node.getModel();
	}

	@Override
	protected Rectangle translateToGraph(Rectangle r) {
		Rectangle rDP = r.getCopy();
		return rDP;
	}

	@Override
	protected Rectangle translateFromGraph(Rectangle rect) {
		Rectangle rLP = rect.getCopy();
		return rLP;
	}

}
