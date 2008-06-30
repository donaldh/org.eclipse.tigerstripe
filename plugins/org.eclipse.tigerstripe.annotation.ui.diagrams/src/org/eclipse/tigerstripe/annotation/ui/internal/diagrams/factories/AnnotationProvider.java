/**
 * 
 */
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams.factories;

import org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.ui.diagrams.DiagramAnnotationType;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.AnnotationNode;
import org.eclipse.tigerstripe.annotation.ui.diagrams.parts.InvisibleEditPart;
import org.eclipse.tigerstripe.annotation.ui.diagrams.parts.UnknownAnnotationEditPart;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.parts.AnnotationConnectionEditPart;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationProvider extends AbstractEditPartProvider {
	
	private AnnotationEditPartManager manager = new AnnotationEditPartManager();
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider#getNodeEditPartClass(org.eclipse.gmf.runtime.notation.View)
	 */
	@Override
	protected Class<?> getNodeEditPartClass(View view) {
		String type = view.getType();
		if (DiagramAnnotationType.ANNOTATION_TYPE.equals(type)) {
			Annotation annotation = getTypedAnnotation(view);
			if (annotation != null)
				return manager.getAnnotationEditPartClass(annotation);
			return UnknownAnnotationEditPart.class;
		}
		else if (DiagramAnnotationType.META_ANNOTATION_TYPE.equals(type) ||
				DiagramAnnotationType.META_VIEW_ANNOTATION_TYPE.equals(type))
			return InvisibleEditPart.class;
	    return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider#getEdgeEditPartClass(org.eclipse.gmf.runtime.notation.View)
	 */
	@Override
	protected Class<?> getEdgeEditPartClass(View view) {
		String type = view.getType();
		if (DiagramAnnotationType.CONNECTION_TYPE.equals(type)) {
			return AnnotationConnectionEditPart.class;
		}
	    return null;
	}
	
	protected Annotation getTypedAnnotation(View view) {
		if (view instanceof AnnotationNode) {
			AnnotationNode node = (AnnotationNode)view;
			Annotation annotation = node.getAnnotation();
			if (annotation != null && AnnotationPlugin.getManager(
					).getType(annotation) != null) {
				return annotation;
			}
		}
		return null;
	}

}
