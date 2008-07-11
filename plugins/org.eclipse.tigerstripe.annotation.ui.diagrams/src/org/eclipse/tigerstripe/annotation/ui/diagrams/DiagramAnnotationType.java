/**
 * 
 */
package org.eclipse.tigerstripe.annotation.ui.diagrams;

import org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeEnumerator;

/**
 * Element types and semantic hints for annotation elements.
 * 
 * @author Yuri Strot
 */
public class DiagramAnnotationType
	extends AbstractElementTypeEnumerator {
	
	/**
	 * the annotation semantic hint
	 */
	public static String ANNOTATION_TYPE = "Annotation"; //$NON-NLS-1$
	
	/**
	 * the annotation meta information semantic hint
	 */
	public static String META_ANNOTATION_TYPE = "MetaAnnotation"; //$NON-NLS-1$
	
	/**
	 * the annotation view meta information semantic hint
	 */
	public static String META_VIEW_ANNOTATION_TYPE = "MetaViewAnnotation"; //$NON-NLS-1$
	
	/**
	 * the annotation view meta information semantic hint
	 */
	public static String VIEW_LOCATION_NODE_TYPE = "ViewLocationNode"; //$NON-NLS-1$
	
	/**
	 * the annotation connection semantic hint
	 */
	public static String CONNECTION_TYPE = "AnnotationConnection"; //$NON-NLS-1$

	public static final IAnnotationType CONNECTION =
		(IAnnotationType) getElementType("org.eclipse.tigerstripe.annotation.ui.diagram.presentation.connection");
	

}