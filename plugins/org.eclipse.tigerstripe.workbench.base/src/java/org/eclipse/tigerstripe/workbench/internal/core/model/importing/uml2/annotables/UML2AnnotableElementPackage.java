/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.annotables;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElement;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElementPackage;

public class UML2AnnotableElementPackage extends UML2AnnotableElement implements
		AnnotableElementPackage {

	private UML2AnnotableElementPackage parentPackage;

	private List<AnnotableElement> annotableElements = new ArrayList<AnnotableElement>();

	private List<AnnotableElementPackage> annotablePackageElements = new ArrayList<AnnotableElementPackage>();

	public UML2AnnotableElementPackage(String name) {
		super(name);
	}

	public List getAnnotableElements() {
		return annotableElements;
	}

	public List getAnnotableElementPackages() {
		return annotablePackageElements;
	}

	public void addAnnotableElementPackage(AnnotableElementPackage pack) {
		this.annotablePackageElements.add(pack);
	}

	public void addAnnotableElement(AnnotableElement elem) {
		this.annotableElements.add(elem);
	}

	public UML2AnnotableElementPackage getParentPackage() {
		return parentPackage;
	}

	public void setParentPackage(UML2AnnotableElementPackage parentPackage) {
		this.parentPackage = parentPackage;
	}

	@Override
	public String getFullyQualifiedName() {

		if (this.parentPackage == null)
			return getName();
		return this.parentPackage.getFullyQualifiedName() + "." + getName();
	}

}
