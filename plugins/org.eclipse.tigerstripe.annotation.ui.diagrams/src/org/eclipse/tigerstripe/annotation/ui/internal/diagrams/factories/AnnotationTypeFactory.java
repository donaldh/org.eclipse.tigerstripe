/**
 * 
 */
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams.factories;

import org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeFactory;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationType;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.SpecializationType;
import org.eclipse.tigerstripe.annotation.ui.diagrams.IAnnotationType;

/**
 * Factory for annotation element types, which are specializations types that have
 * a semantic hint parameter (like notations).
 * 
 * @author Yuri Strot
 */
public class AnnotationTypeFactory
	extends AbstractElementTypeFactory {

	/**
	 * The hinted type kind. This string is specified in the XML 'kind'
	 * attribute of any element type that is a hinted type.
	 */
	public static final String HINTED_TYPE_KIND = "org.eclipse.tigerstripe.annotation.ui.diagrams.IAnnotationType"; //$NON-NLS-1$

	/**
	 * The semantic hint parameter name.
	 */
	private static final String SEMANTIC_HINT_PARAM_NAME = "semanticHint"; //$NON-NLS-1$

	/**
	 * The notation type class.
	 */
	private static final class NotationType
		extends SpecializationType
		implements IAnnotationType {

		/**
		 * The semantic hint.
		 */
		private final String semanticHint;

		/**
		 * Constructs a new notation type.
		 * 
		 * @param descriptor
		 *            the specialization type descriptor
		 * @param semanticHint
		 *            the semantic hint
		 */
		public NotationType(ISpecializationTypeDescriptor descriptor,
				String semanticHint) {

			super(descriptor);
			this.semanticHint = semanticHint;
		}

		/**
		 * Gets the semantic hint.
		 */
		public String getSemanticHint() {
			return semanticHint;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeFactory#createSpecializationType(org.eclipse.gmf.runtime.emf.type.core.internal.impl.SpecializationTypeDescriptor)
	 */
	public ISpecializationType createSpecializationType(
			ISpecializationTypeDescriptor descriptor) {

		String semanticHint = descriptor
			.getParamValue(SEMANTIC_HINT_PARAM_NAME);

		return new NotationType(descriptor, semanticHint);
	}

}