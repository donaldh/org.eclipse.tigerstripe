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
package org.eclipse.tigerstripe.core.model.importing.uml2.annotables;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.core.model.importing.BaseAnnotableElement;

public class UML2AnnotableBase extends BaseAnnotableElement {

	private EObject correspondingEObject;

	public UML2AnnotableBase(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void setEObject(EObject eObject) {
		this.correspondingEObject = eObject;
	}

	public EObject getEObject() {
		return this.correspondingEObject;
	}
}
