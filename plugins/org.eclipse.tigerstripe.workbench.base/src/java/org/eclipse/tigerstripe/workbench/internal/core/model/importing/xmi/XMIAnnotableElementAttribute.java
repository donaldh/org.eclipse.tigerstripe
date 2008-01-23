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
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElementAttribute;

@Deprecated
public class XMIAnnotableElementAttribute extends XMIAnnotableBase implements
		AnnotableElementAttribute {

	private AnnotableDatatype type;
	// private Attribute attribute;
	// private UmlAssociation umlAssociation;
	private int visibility;
	private int dimensions;

	public XMIAnnotableElementAttribute(String name) {
		super(name);
	}

	public XMIAnnotableElementAttribute() {
		super("unknown");
	}

	public int getDimensions() {
		return dimensions;
	}

	public void setDimensions(int dimensions) {
		this.dimensions = dimensions;
	}

	@Override
	public Object getCorrespondingUmlElement() {
		return null;
		// return getAttribute();
	}

	// public Attribute getAttribute() {
	// return attribute;
	// }

	// public void setAttribute(Attribute attribute) {
	// this.attribute = attribute;
	// }

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

	// public UmlAssociation getUmlAssociation() {
	// return umlAssociation;
	// }

	// public void setUmlAssociation(UmlAssociation umlAssociation) {
	// this.umlAssociation = umlAssociation;
	// }

}
