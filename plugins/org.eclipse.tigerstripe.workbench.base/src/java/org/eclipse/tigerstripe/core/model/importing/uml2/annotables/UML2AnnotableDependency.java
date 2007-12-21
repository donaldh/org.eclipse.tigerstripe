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
import org.eclipse.tigerstripe.core.model.importing.AnnotableDependency;

public class UML2AnnotableDependency extends UML2AnnotableElement implements
		AnnotableDependency {

	private AnnotableDatatype aEnd;
	private AnnotableDatatype zEnd;

	public UML2AnnotableDependency(String name) {
		super(name);
	}

	public AnnotableDatatype getAEnd() {
		return aEnd;
	}

	public AnnotableDatatype getZEnd() {
		return zEnd;
	}

	public void setAEnd(AnnotableDatatype aEnd) {
		this.aEnd = aEnd;
	}

	public void setZEnd(AnnotableDatatype zEnd) {
		this.zEnd = zEnd;
	}

}
