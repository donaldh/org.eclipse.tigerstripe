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

import org.eclipse.tigerstripe.core.model.importing.AnnotableDatatype;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElementConstant;

public class UML2AnnotableElementConstant extends UML2AnnotableBase implements
		AnnotableElementConstant {

	private AnnotableDatatype type;
	private String value;
	private int visibility;

	public UML2AnnotableElementConstant(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public AnnotableDatatype getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public int getVisibility() {
		return visibility;
	}

	public void setType(AnnotableDatatype datatype) {
		this.type = datatype;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

}
