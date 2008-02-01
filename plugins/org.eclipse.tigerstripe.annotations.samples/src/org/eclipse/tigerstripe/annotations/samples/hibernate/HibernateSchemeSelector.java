package org.eclipse.tigerstripe.annotations.samples.hibernate;

import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.ISelector;

public class HibernateSchemeSelector implements ISelector {

	public HibernateSchemeSelector() {
	}

	public boolean isEnabled(IAnnotationSpecification spec, String URI)
			throws AnnotationCoreException {
		return false;
	}

	public boolean select(String URI) {
		if(URI.endsWith(".java")) {
			return true;
		}
		return false;
	}

	public void setContext(Object context) {
	}

}
