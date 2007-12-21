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

import org.eclipse.tigerstripe.core.model.importing.AnnotableAssociation;
import org.eclipse.tigerstripe.core.model.importing.AnnotableAssociationEnd;

public class UML2AnnotableAssociation extends UML2AnnotableElement implements
		AnnotableAssociation {

	private AnnotableAssociationEnd aEnd;
	private AnnotableAssociationEnd zEnd;

	public UML2AnnotableAssociation(String name) {
		super(name);
	}

	public AnnotableAssociationEnd getAEnd() {
		return aEnd;
	}

	public AnnotableAssociationEnd getZEnd() {
		return zEnd;
	}

	public void setAEnd(AnnotableAssociationEnd aEnd) {
		this.aEnd = aEnd;
	}

	public void setZEnd(AnnotableAssociationEnd zEnd) {
		this.zEnd = zEnd;
	}

}
