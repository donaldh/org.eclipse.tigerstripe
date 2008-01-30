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

	private Set<String> literals = new HashSet<String>();

	public void addLiteral(String literal) {
		literals.add(literal);
	}

	public String[] getLiterals() {
		return literals.toArray(new String[literals.size()]);
	}

	@Override
	void populate(IConfigurationElement element, IAnnotationForm parentForm) {
		super.populate(element, parentForm);

		// parse the Literals
		for (IConfigurationElement child : element.getChildren("literal")) {
			String lit = child.getAttribute("name");
			addLiteral(lit);
		}
	}

}
