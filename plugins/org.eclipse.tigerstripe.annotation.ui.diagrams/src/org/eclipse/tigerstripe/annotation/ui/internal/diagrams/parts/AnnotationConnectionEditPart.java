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
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams.parts;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Graphics;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationConnectionEditPart extends ConnectionNodeEditPart {

	public AnnotationConnectionEditPart(View view) {
		super(view);
	}

	/**
	 * @see ConnectionNodeEditPart#createConnectionFigure()
	 */
	protected Connection createConnectionFigure() {
		PolylineConnectionEx conn = new PolylineConnectionEx();
		conn.setLineStyle(Graphics.LINE_DOT);
		return conn;
	}	
}
