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

import java.util.List;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.diagram.ui.figures.DiagramColorConstants;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.FontStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.ui.util.DisplayAnnotationUtil;

/**
 * @author Yuri Strot
 *
 */
public class StructuredAnnotationEditPart extends AnnotationEditPart {

	/**
	 * @param view
	 */
	public StructuredAnnotationEditPart(View view) {
		super(view);
	}

	/**
	 * Creates a note figure.
	 */
	protected NodeFigure createNodeFigure() {
		Annotation annotation = getAnnotation();
		IMapMode mm = getMapMode();
		Insets insets = new Insets(mm.DPtoLP(5), mm.DPtoLP(5), mm.DPtoLP(5), mm.DPtoLP(14));
		Label[] top = new Label[] { createLabel() };
		Label[] bottom = new Label[getFeaturesCount(annotation)];
		for (int i = 0; i < bottom.length; i++) {
			bottom[i] = createLabel();
		}
		updateNameLabel(annotation, top[0]);
		updateFeatures(annotation, bottom);
		
		StructuredAnnotationFigure fig = new StructuredAnnotationFigure(top, bottom,
				mm.DPtoLP(100), mm.DPtoLP(56), insets, mm.DPtoLP(10));
		Dimension dimension = fig.getLayoutManager().getPreferredSize(fig, -1, -1);
		int plus = 12;
		dimension.height += plus;
		dimension.width += plus;
		fig.setDefaultSize((Dimension)mm.DPtoLP(dimension));
		return fig;
	}
	
	protected Label createLabel() { 
		Label label = new Label();
		
		FontStyle fontStyle = (FontStyle) getNotationView().getStyle(
				NotationPackage.Literals.FONT_STYLE);
		if (fontStyle != null) {
			Font font = new Font(Display.getCurrent(), 
					fontStyle.getFontName(), fontStyle.getFontHeight(), SWT.NORMAL);
			label.setFont(font);
		}
		else {
			label.setFont(Display.getCurrent().getSystemFont());
		}
		
		label.setForegroundColor(DiagramColorConstants.black);
		return label;
	}
	
	protected void updateNameLabel(Annotation annotation, Label label) {
		label.setIcon(DisplayAnnotationUtil.getImage(annotation));
		label.setText(DisplayAnnotationUtil.getText(annotation));
	}
	
	protected void updateFeatures(Annotation annotation, Label[] labels) {
		List<EStructuralFeature> list = getFeatures(annotation);
		int count = 0;
		for (EStructuralFeature feature : list) {
			Object value = getFeatureValue(annotation, feature);
			String text = value == null ? "" : value.toString();
			labels[count++].setText(feature.getName() + ": " + text);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		StructuredAnnotationFigure figure = (StructuredAnnotationFigure)getFigure();
		Annotation annotation = getAnnotation();
		updateNameLabel(annotation, figure.getTopLabels()[0]);
		updateFeatures(annotation, figure.getBottomLabels());
		//figure.setDefaultSize(figure.getLayoutManager().getPreferredSize(figure, -1, -1));
//		Annotation annotation = getAnnotation();
//		AnnotationFigure figure = (AnnotationFigure)getFigure();
//		List<EStructuralFeature> list = getFeatures(annotation);
//		int count = 0;
//		for (EStructuralFeature feature : list) {
//			Object value = getFeatureValue(annotation, feature);
//			String text = value == null ? "" : value.toString();
//			figure.setText(feature.getName() + ": " + text, count++);
//		}
		
		super.refreshVisuals();
	}
	
	protected int getFeaturesCount(Annotation annotation) {
		return getFeatures(annotation).size();
	}
	
	protected List<EStructuralFeature> getFeatures(Annotation annotation) {
		return annotation.getContent().eClass().getEStructuralFeatures();
	}
	
	protected Object getFeatureValue(Annotation annotation, EStructuralFeature feature) {
		return annotation.getContent().eGet(feature);
	}

}
