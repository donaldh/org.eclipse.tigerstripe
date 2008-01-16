/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial API and Implementation
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations.internal;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.IAnnotationForm;
import org.eclipse.tigerstripe.annotations.IAnnotationScheme;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.ISelector;
import org.eclipse.tigerstripe.annotations.IValidator;

public class AnnotationForm implements IAnnotationForm {

	private IAnnotationScheme scheme;
	private ISelector selector = ISelector.DEFAULT;
	private IValidator validator = IValidator.DEFAULT;
	private String ID;

	private Set<IAnnotationSpecification> specifications = new HashSet<IAnnotationSpecification>();

	public IAnnotationScheme getScheme() {
		return scheme;
	}

	public void setScheme(IAnnotationScheme scheme) {
		this.scheme = scheme;
	}

	public ISelector getSelector() {
		return selector;
	}

	public IAnnotationSpecification[] getSpecifications() {
		return specifications
				.toArray(new IAnnotationSpecification[specifications.size()]);
	}

	public IValidator getValidator() {
		return validator;
	}

	public String getID() {
		return ID;
	}

	public void parse(IConfigurationElement element)
			throws AnnotationCoreException {
		ID = element.getAttribute("ID");

		// Extract Selector
		try {
			Object obj = element.createExecutableExtension("selector");
			if (obj instanceof ISelector) {
				selector = (ISelector) obj;
				selector.setContext(this);
			}
		} catch (CoreException e) {
			// this means no definition of selector... ignore.
		}

		// Extract Validator
		try {
			Object obj = element.createExecutableExtension("validator");
			if (obj instanceof IValidator) {
				validator = (IValidator) obj;
				validator.setContext(this);
			}
		} catch (CoreException e) {
			// this means no definition of selector... ignore.
		}

		// Parse the AnnotationSpecs
		IConfigurationElement[] children = element.getChildren();
		for (IConfigurationElement child : children) {
			IAnnotationSpecification spec = AnnotationSpecificationFactory
					.parseAnnotationSpecification(child, this);
			if (spec != null)
				specifications.add(spec);
		}
	}
}
