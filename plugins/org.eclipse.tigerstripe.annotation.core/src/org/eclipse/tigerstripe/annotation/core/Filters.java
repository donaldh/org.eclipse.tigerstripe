package org.eclipse.tigerstripe.annotation.core;

import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Set of the predefined singleton filters.
 */
public class Filters {

	/**
	 * Filter by URI of the annotation
	 */
	public static Filter uri(final org.eclipse.emf.common.util.URI uri) {
		return new Filter() {

			public boolean apply(Annotation ann) {
				return uri.equals(ann.getUri());
			}
		};
	}

	/**
	 * Filter by equality of given feature of the annotation
	 */
	public static Filter eq(final EStructuralFeature feature, final Object value) {
		return new Filter() {

			public boolean apply(Annotation ann) {
				Object gotten = ann.eGet(feature);
				if (value == null) {
					return gotten == null; 
				} else {
					return value.equals(gotten);
				}
			}
		};
	}

	/**
	 * Find all annotations with feature that start with the given value
	 */
	public static Filter startWith(final EStructuralFeature feature, final Object value) {
		return new Filter() {

			public boolean apply(Annotation ann) {
				Object gotten = ann.eGet(feature);
				if (gotten == null) {
					return value == null; 
				} else {
					return gotten.toString().startsWith(value.toString());
				}
			}
		};
	}
}
