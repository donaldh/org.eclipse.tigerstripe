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
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElementOperationParameter;

@Deprecated
public class XMIAnnotableElementOperationParameter extends XMIAnnotableBase
		implements AnnotableElementOperationParameter {

	// private Parameter umlParameter;
	private AnnotableDatatype datatype;

	public XMIAnnotableElementOperationParameter(String name) {
		super(name);
	}

	public XMIAnnotableElementOperationParameter() {
		super("unknown");
	}

	@Override
	public Object getCorrespondingUmlElement() {
		return null;
		// return getUmlParameter();
	}

	// public Parameter getUmlParameter() {
	// return umlParameter;
	// }
	//
	// public void setUmlParameter(Parameter umlParameter) {
	// this.umlParameter = umlParameter;
	// }
	//
	public AnnotableDatatype getType() {
		return this.datatype;
	}

	public void setType(AnnotableDatatype datatype) {
		this.datatype = datatype;
	}

}
