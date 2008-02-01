package org.eclipse.tigerstripe.annotations.samples.defaults;

import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.ISelector;

public class DefaultSchemeSelector implements ISelector {

	public DefaultSchemeSelector() {
	}

	public boolean isEnabled(IAnnotationSpecification spec, String URI)
			throws AnnotationCoreException {
		return false;
	}

	public boolean select(String URI) {
		return true;
	}

	public void setContext(Object context) {
	}

}
