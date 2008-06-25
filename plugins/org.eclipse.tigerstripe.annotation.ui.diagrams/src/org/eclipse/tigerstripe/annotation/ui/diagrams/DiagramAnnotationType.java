/**
 * 
 */
package org.eclipse.tigerstripe.annotation.ui.diagrams;

import org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeEnumerator;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;

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
	public static String ANN_TYPE = "ann"; //$NON-NLS-1$
	
	/**
	 * the annotation semantic hint
	 */
	public static String ANNOTATION_TYPE = "Annotation"; //$NON-NLS-1$
	
	/**
	 * the annotation semantic hint
	 */
	public static String META_ANNOTATION_TYPE = "MetaAnnotation"; //$NON-NLS-1$
	
	/**
	 * the annotation semantic hint
	 */
	public static String META_VIEW_ANNOTATION_TYPE = "MetaViewAnnotation"; //$NON-NLS-1$
	/**
	 * the connection semantic hint
	 */
	public static String CONNECTION_TYPE = "AnnotationConnection"; //$NON-NLS-1$
	
	public static final IAnnotationType ANNOTATION =
		(IAnnotationType) getElementType("org.eclipse.tigerstripe.annotation.ui.diagram.presentation.annotation");

	public static final IAnnotationType CONNECTION =
		(IAnnotationType) getElementType("org.eclipse.tigerstripe.annotation.ui.diagram.presentation.connection");

	public static final IAnnotationType META_ANNOTATION =
		(IAnnotationType) getElementType("org.eclipse.tigerstripe.annotation.ui.diagram.presentation.meta");

	public static final IAnnotationType META_VIEW_ANNOTATION =
		(IAnnotationType) getElementType("org.eclipse.tigerstripe.annotation.ui.diagram.presentation.metaView");
	
	public static final IHintedType ANN =
		(IHintedType) getElementType("org.eclipse.tigerstripe.annotation.ui.diagram.presentation.ann");
	

}