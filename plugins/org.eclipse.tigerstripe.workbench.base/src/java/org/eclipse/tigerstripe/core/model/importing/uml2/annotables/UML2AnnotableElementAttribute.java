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
import org.eclipse.tigerstripe.core.model.importing.AnnotableElementAttribute;

public class UML2AnnotableElementAttribute extends UML2AnnotableBase implements
		AnnotableElementAttribute {

	private AnnotableDatatype type;
	private int visibility;

	public UML2AnnotableElementAttribute(String name) {
		super(name);
	}

	public UML2AnnotableElementAttribute() {
		super("unknown");
	}

	public int getDimensions() {
		return getType().getMultiplicity();
	}

	public void setDimensions(int dimensions) {
		// ignore this is saved in the type itself
	}

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
