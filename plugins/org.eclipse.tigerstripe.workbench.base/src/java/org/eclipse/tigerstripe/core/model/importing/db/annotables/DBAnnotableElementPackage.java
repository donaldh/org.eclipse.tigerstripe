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
package org.eclipse.tigerstripe.core.model.importing.db.annotables;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.core.model.importing.AnnotableElement;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElementPackage;

public class DBAnnotableElementPackage extends DBAnnotableElement implements
		AnnotableElementPackage {

	private DBAnnotableElementPackage parentPackage;

	private List<AnnotableElement> annotableElements = new ArrayList<AnnotableElement>();

	private List<AnnotableElementPackage> annotablePackageElements = new ArrayList<AnnotableElementPackage>();

	public DBAnnotableElementPackage(String name) {
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

	public DBAnnotableElementPackage getParentPackage() {
		return parentPackage;
	}

	public void setParentPackage(DBAnnotableElementPackage parentPackage) {
		this.parentPackage = parentPackage;
	}

	@Override
	public String getFullyQualifiedName() {

		if (this.parentPackage == null)
			return getName();
		return this.parentPackage.getFullyQualifiedName() + "." + getName();
	}

}
