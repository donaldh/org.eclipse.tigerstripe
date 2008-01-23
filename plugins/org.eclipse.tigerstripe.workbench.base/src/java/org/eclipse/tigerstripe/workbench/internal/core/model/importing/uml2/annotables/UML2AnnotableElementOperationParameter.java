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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.annotables;

import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableDatatype;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElementOperationParameter;

public class UML2AnnotableElementOperationParameter extends UML2AnnotableBase
		implements AnnotableElementOperationParameter {

	private AnnotableDatatype type;

	public UML2AnnotableElementOperationParameter(String name) {
		super(name);
	}

	public AnnotableDatatype getType() {
		return type;
	}

	public void setType(AnnotableDatatype type) {
		this.type = type;
	}
}
