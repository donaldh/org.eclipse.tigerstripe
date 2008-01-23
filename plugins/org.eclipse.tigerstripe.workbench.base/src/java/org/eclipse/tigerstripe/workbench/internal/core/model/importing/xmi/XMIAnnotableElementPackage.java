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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.xmi;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElement;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElementPackage;

@Deprecated
public class XMIAnnotableElementPackage extends XMIAnnotableBase implements
		AnnotableElementPackage {

	// private UmlPackage umlPackage;

	private XMIAnnotableElementPackage parentPackage;

	private List annotableElements = new ArrayList();

	private List annotablePackageElements = new ArrayList();

	public XMIAnnotableElementPackage(String name) {
		super(name);
	}

	public XMIAnnotableElementPackage() {
		super("unknown");
	}

	@Override
	public Object getCorrespondingUmlElement() {
		return null;
		// return getUmlPackage();
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

	// public UmlPackage getUmlPackage() {
	// return umlPackage;
	// }
	//
	// public void setUmlPackage(UmlPackage umlPackage) {
	// this.umlPackage = umlPackage;
	// }
	//
	public XMIAnnotableElementPackage getParentPackage() {
		return parentPackage;
	}

	public void setParentPackage(XMIAnnotableElementPackage parentPackage) {
		this.parentPackage = parentPackage;
	}

	@Override
	public String getFullyQualifiedName() {

		if (this.parentPackage == null)
			return getName();
		return this.parentPackage.getFullyQualifiedName() + "." + getName();
	}

}
