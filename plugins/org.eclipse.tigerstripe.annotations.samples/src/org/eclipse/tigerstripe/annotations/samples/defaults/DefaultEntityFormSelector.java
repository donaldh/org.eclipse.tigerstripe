package org.eclipse.tigerstripe.annotations.samples.defaults;

import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.ISelector;

public class DefaultEntityFormSelector implements ISelector {

	public DefaultEntityFormSelector() {
	}

	public boolean isEnabled(IAnnotationSpecification spec, String URI)
			throws AnnotationCoreException {
		return false;
	}

	public boolean select(String URI) {

		if (URI.startsWith("tigerstripe:")) {
			return true;
		}
		return false;
	}

	public void setContext(Object context) {
	}

}
