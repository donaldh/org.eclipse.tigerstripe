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

import org.eclipse.tigerstripe.core.model.importing.AnnotableDatatype;

@Deprecated
public class XMIAnnotableDatatype extends XMIAnnotableBase implements
		AnnotableDatatype {

	public XMIAnnotableDatatype(String name) {
		super(name);
	}

	public XMIAnnotableDatatype() {
		super("unknown");
	}

	// private DataType umlDatatype;
	private XMIAnnotableElementPackage annPackage;

	@Override
	public Object getCorrespondingUmlElement() {
		// return getUmlDatatype();
		return null;
	}

	// public DataType getUmlDatatype() {
	// return umlDatatype;
	// }

	// public void setUmlDatatype(DataType umlDatatype) {
	// this.umlDatatype = umlDatatype;
	// }

	@Override
	public String getFullyQualifiedName() {
		String prefix = "";
		if (annPackage != null) {
			prefix = annPackage.getFullyQualifiedName() + ".";
		}
		return prefix + getName();
	}
}
