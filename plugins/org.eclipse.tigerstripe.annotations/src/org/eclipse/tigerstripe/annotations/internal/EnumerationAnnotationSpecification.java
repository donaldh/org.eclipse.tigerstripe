/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations.internal;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.tigerstripe.annotations.IAnnotationForm;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.IEnumerationAnnotationSpecification;

public class EnumerationAnnotationSpecification extends
		BaseAnnotationSpecification implements IAnnotationSpecification,
		IEnumerationAnnotationSpecification {

	private Set<AnnotationSpecificationLiteral> literals = new HashSet<AnnotationSpecificationLiteral>();

	public void addLiteral(AnnotationSpecificationLiteral literal) {
		literals.add(literal);
	}

	public AnnotationSpecificationLiteral[] getLiterals() {
		return literals.toArray(new AnnotationSpecificationLiteral[literals
				.size()]);
	}

	@Override
	void populate(IConfigurationElement element, IAnnotationForm parentForm) {
		super.populate(element, parentForm);

		// parse the Literals
		AnnotationSpecificationLiteral literal;
		for (IConfigurationElement child : element.getChildren("literal")) {
			literal = new AnnotationSpecificationLiteral();
			literal.setValue(child.getAttribute("value"));
			literal.setIndex(Integer.parseInt(child.getAttribute("index")));
			addLiteral(literal);
		}
	}

}
