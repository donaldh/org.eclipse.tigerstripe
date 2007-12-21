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
package org.eclipse.tigerstripe.core.model.importing.uml2.mapper;

import org.eclipse.uml2.uml.NamedElement;

public abstract class UML2BaseMapper {

	protected NamedElement namedElement;
	protected UML2MappingUtils utils;

	public UML2BaseMapper(NamedElement namedElement, UML2MappingUtils utils) {
		this.namedElement = namedElement;
		this.utils = utils;
	}

}
