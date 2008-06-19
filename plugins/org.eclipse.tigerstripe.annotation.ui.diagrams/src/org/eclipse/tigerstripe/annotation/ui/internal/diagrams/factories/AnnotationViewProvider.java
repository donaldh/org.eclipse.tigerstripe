/**
 * 
 */
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams.factories;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.annotation.ui.diagrams.DiagramAnnotationType;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationViewProvider extends AbstractViewProvider {

	/** list of supported shape views. */
    static private final Map<String, Class<?>> map = new HashMap<String, Class<?>>();
	static {
		map.put(DiagramAnnotationType.ANN_TYPE, AnnotationViewFactory.class);
		map.put(DiagramAnnotationType.ANNOTATION_TYPE, AnnotationViewFactory.class);
		map.put(DiagramAnnotationType.CONNECTION_TYPE, ConnectionViewFactory.class);
	}

	/**
	 * Returns the shape view class to instantiate based on the passed params
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @return Class
	 */
	@SuppressWarnings("unchecked")
	protected Class getNodeViewClass(
		IAdaptable semanticAdapter,
		View containerView,
		String semanticHint) {
		if (semanticHint != null && semanticHint.length() > 0)
			return (Class)map.get(semanticHint);
		return (Class)map.get(getSemanticEClass(semanticAdapter));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider#getEdgeViewClass(org.eclipse.core.runtime.IAdaptable, org.eclipse.gmf.runtime.notation.View, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Class getEdgeViewClass(IAdaptable semanticAdapter,
			View containerView, String semanticHint) {
		if (semanticHint != null && semanticHint.length() > 0)
			return (Class)map.get(semanticHint);
		return (Class)map.get(getSemanticEClass(semanticAdapter));
	}

}
