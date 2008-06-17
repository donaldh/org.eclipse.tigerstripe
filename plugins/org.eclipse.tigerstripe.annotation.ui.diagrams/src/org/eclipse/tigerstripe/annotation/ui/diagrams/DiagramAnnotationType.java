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
	 * the connection semantic hint
	 */
	public static String CONNECTION_TYPE = "AnnotationConnection"; //$NON-NLS-1$
	
	public static final IAnnotationType ANNOTATION =
		(IAnnotationType) getElementType("org.eclipse.tigerstripe.annotation.ui.diagram.presentation.annotation"); //$NON-NLS-1$

	public static final IAnnotationType CONNECTION =
		(IAnnotationType) getElementType("org.eclipse.tigerstripe.annotation.ui.diagram.presentation.connection"); //$NON-NLS-1$

}