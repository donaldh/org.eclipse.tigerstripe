/**
 * 
 */
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams.factories;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractShapeViewFactory;
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
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewLocationNode;

/**
 * @author Yuri Strot
 * 
 */
public class AnnotationViewFactory extends AbstractShapeViewFactory {

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
			String semanticHint, int index, boolean persisted,
			final PreferencesHint preferencesHint) {
		this.semanticHint = semanticHint;
		View view = super.createView(semanticAdapter, containerView,
				semanticHint, index, persisted, preferencesHint);
		EObject element = view.getElement();
		if (element instanceof Annotation && view instanceof AnnotationNode) {
			AnnotationNode node = (AnnotationNode) view;
			Annotation annotation = (Annotation) view.getElement();
			node.setAnnotationId(annotation.getId());
			view.setElement(null);
		}
		if (view instanceof ViewLocationNode && element instanceof View) {
			ViewLocationNode node = (ViewLocationNode) view;
			node.setView(element);
			view.setElement(null);
		}
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gmf.runtime.diagram.ui.view.factories.BasicNodeViewFactory
	 * #createNode()
	 */
	@Override
	protected Node createNode() {
		if (semanticHint != null) {
			if (semanticHint.equals(DiagramAnnotationType.META_ANNOTATION_TYPE))
				return ModelFactory.eINSTANCE.createMetaAnnotationNode();
			if (semanticHint
					.equals(DiagramAnnotationType.META_VIEW_ANNOTATION_TYPE))
				return ModelFactory.eINSTANCE.createMetaViewAnnotations();
			if (semanticHint
					.equals(DiagramAnnotationType.VIEW_LOCATION_NODE_TYPE))
				return ModelFactory.eINSTANCE.createViewLocationNode();
		}
		return ModelFactory.eINSTANCE.createAnnotationNode();
	}

	protected void initializeFromPreferences(View view) {
		super.initializeFromPreferences(view);

		FillStyle fillStyle = (FillStyle) view
				.getStyle(NotationPackage.Literals.FILL_STYLE);
		if (fillStyle != null) {
			fillStyle.setFillColor(FigureUtilities.RGBToInteger(
					new RGB(254, 243, 143)).intValue());
		}

		LineStyle lineStyle = (LineStyle) view
				.getStyle(NotationPackage.Literals.LINE_STYLE);
		if (lineStyle != null) {
			lineStyle.setLineColor(FigureUtilities.RGBToInteger(
					new RGB(193, 164, 101)).intValue());
		}
	}
}