package org.eclipse.tigerstripe.annotations.samples.defaults;

import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.ISelector;

public class DefaultFormSelector implements ISelector {

	public DefaultFormSelector() {
	}

	public boolean isEnabled(IAnnotationSpecification spec, String URI)
			throws AnnotationCoreException {
		return false;
	}

	public boolean select(String URI) {
		// these have selector defined already (need to discuss this)
		if(!URI.startsWith("tigerstripe:/")) {
			return true;
		}
		return false;
	}

	public void setContext(Object context) {
	}

}
