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

import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableDatatype;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElementConstant;

@Deprecated
public class XMIAnnotableElementConstant extends XMIAnnotableBase implements
		AnnotableElementConstant {

	private AnnotableDatatype type;
	// private EnumerationLiteral literal;
	private int visibility;
	private String value;

	public XMIAnnotableElementConstant(String name) {
		super(name);
	}

	public XMIAnnotableElementConstant() {
		super("unknown");
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public Object getCorrespondingUmlElement() {
		return null;
		// return getLiteral();
	}

	// public EnumerationLiteral getLiteral() {
	// return literal;
	// }
	//
	// public void setLiteral(EnumerationLiteral literal) {
	// this.literal = literal;
	// }
	//
	public AnnotableDatatype getType() {
		return type;
	}

	public void setType(AnnotableDatatype type) {
		this.type = type;
	}

	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}
}
