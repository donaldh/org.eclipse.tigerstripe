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
package org.eclipse.tigerstripe.annotation.ui.example.editpartProvider;

import org.eclipse.draw2d.Label;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderedNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.ui.diagrams.parts.AnnotationEditPart;
import org.eclipse.tigerstripe.annotation.ui.example.entityNote.EntityNote;
import org.eclipse.tigerstripe.annotation.ui.util.DisplayAnnotationUtil;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationImageEditPart extends AnnotationEditPart {

	/**
	 * @param view
	 */
	public AnnotationImageEditPart(View view) {
		super(view);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart#createNodeFigure()
	 */
	@Override
	protected NodeFigure createNodeFigure() {
		BorderedNodeFigure figure = new BorderedNodeFigure(new Label());
		return figure;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		BorderedNodeFigure figure = (BorderedNodeFigure)getFigure();
		Label label = (Label)figure.getMainFigure();
		Annotation annotation = getAnnotation();
		EntityNote note = (EntityNote)annotation.getContent();
		label.setText(note.getDescription());
		label.setIcon(DisplayAnnotationUtil.getImage(annotation));
		super.refreshVisuals();
	}

}
