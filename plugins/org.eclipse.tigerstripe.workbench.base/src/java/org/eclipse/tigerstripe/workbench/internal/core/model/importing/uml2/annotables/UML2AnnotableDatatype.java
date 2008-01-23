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

public class UML2AnnotableDatatype extends UML2AnnotableBase implements
		AnnotableDatatype {

	private int multiplicity;

	public UML2AnnotableDatatype(String name) {
		super(name);
	}

	public UML2AnnotableDatatype() {
		super("unknown");
	}

	public void setMultiplicity(int multiplicity) {
		this.multiplicity = multiplicity;
	}

	@Override
	public int getMultiplicity() {
		return this.multiplicity;
	}

}
