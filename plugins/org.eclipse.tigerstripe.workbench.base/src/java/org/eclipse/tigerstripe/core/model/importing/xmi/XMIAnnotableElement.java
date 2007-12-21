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
package org.eclipse.tigerstripe.core.model.importing.xmi;

import org.eclipse.tigerstripe.core.model.importing.AnnotableElement;

@Deprecated
public class XMIAnnotableElement extends XMIAnnotableBase implements
		AnnotableElement {

	public XMIAnnotableElement(String fqn) {
		super(fqn);
	}

	public XMIAnnotableElement() {
		super(null);
	}

	// private UmlClass umlClass;
	// private Enumeration umlEnumeration;
	// public UmlClass getUmlClass() {
	// return umlClass;
	// }

	// public void setUmlClass(UmlClass umlClass) {
	// this.umlClass = umlClass;
	// }
	//
	// public Enumeration getUmlEnumeration() {
	// return umlEnumeration;
	// }
	//
	// public void setUmlEnumeration(Enumeration umlEnumeration) {
	// this.umlEnumeration = umlEnumeration;
	// }

	@Override
	public Object getCorrespondingUmlElement() {
		return null;
		// if ( getUmlClass() != null ) {
		// return getUmlClass();
		// } else {
		// return getUmlEnumeration();
		// }
	}

}
