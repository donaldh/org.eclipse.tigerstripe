package org.eclipse.tigerstripe.annotations.samples.defaults;

import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.ISelector;

public class DefaultProjectFormSelector implements ISelector {

	public DefaultProjectFormSelector() {
	}

	public boolean isEnabled(IAnnotationSpecification spec, String URI)
			throws AnnotationCoreException {
		return false;
	}

	public boolean select(String URI) {
		if(URI.startsWith("project:/")) {
			return true;
		}
		return false;
	}

	public void setContext(Object context) {
	}

}
