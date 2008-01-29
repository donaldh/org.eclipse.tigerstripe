package org.eclipse.tigerstripe.annotations.ui.internal;

import java.util.Comparator;

import org.eclipse.tigerstripe.annotations.IAnnotationScheme;

public class AnnotationSchemeComparator implements
		Comparator<IAnnotationScheme> {

	public int compare(IAnnotationScheme scheme1, IAnnotationScheme scheme2) {

		return scheme1.getNamespaceUserLabel().compareTo(
				scheme2.getNamespaceUserLabel());

	}
}
