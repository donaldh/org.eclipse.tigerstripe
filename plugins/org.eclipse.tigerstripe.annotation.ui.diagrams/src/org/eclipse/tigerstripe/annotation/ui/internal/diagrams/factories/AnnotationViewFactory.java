/**
 * 
 */
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams.factories;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.TextShapeViewFactory;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.FillStyle;
import org.eclipse.gmf.runtime.notation.LineStyle;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.ui.diagrams.DiagramAnnotationType;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.AnnotationNode;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.ModelFactory;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationViewFactory
	extends TextShapeViewFactory {
	
	private String semanticHint;

	/**
	 * Method NoteView. creation constructor
	 * 
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @param index
	 * @param persisted
	 */
	public View createView(IAdaptable semanticAdapter, View containerView,
			String semanticHint, int index, boolean persisted, final PreferencesHint preferencesHint) {
		this.semanticHint = semanticHint;
		View view = super.createView(semanticAdapter, containerView, semanticHint,
			index, persisted, preferencesHint);
		if (view.getElement() instanceof Annotation && 
				view instanceof AnnotationNode) {
			AnnotationNode node = (AnnotationNode)view;
			Annotation annotation = (Annotation)view.getElement();
			node.setAnnotationId(annotation.getId());
		}
		return view;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.view.factories.BasicNodeViewFactory#createNode()
	 */
	@Override
	protected Node createNode() {
		if (semanticHint != null) {
			if (semanticHint.equals(DiagramAnnotationType.META_ANNOTATION_TYPE))
				return ModelFactory.eINSTANCE.createMetaAnnotationNode();
			if (semanticHint.equals(DiagramAnnotationType.META_VIEW_ANNOTATION_TYPE))
				return ModelFactory.eINSTANCE.createMetaViewAnnotations();
		}
		return ModelFactory.eINSTANCE.createAnnotationNode();
	}

	protected void initializeFromPreferences(View view) {
		super.initializeFromPreferences(view);
		
		FillStyle fillStyle = (FillStyle) view.getStyle(
				NotationPackage.Literals.FILL_STYLE);
		if (fillStyle != null) {
			fillStyle.setFillColor(FigureUtilities.RGBToInteger(
					new RGB(254, 243, 143)).intValue());
		}

		LineStyle lineStyle = (LineStyle) view.getStyle(
				NotationPackage.Literals.LINE_STYLE);
		if (lineStyle != null) {
			lineStyle.setLineColor(FigureUtilities.RGBToInteger(
					new RGB(193, 164, 101)).intValue());
		}
	}
}
