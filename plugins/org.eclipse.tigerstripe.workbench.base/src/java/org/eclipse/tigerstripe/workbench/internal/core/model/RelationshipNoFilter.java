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
package org.eclipse.tigerstripe.workbench.internal.core.model;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;

/**
 * A filter that filters... NOTHING.
 * 
 * @author Eric Dillon
 * 
 */
public class RelationshipNoFilter extends RelationshipFilter {

	public static RelationshipNoFilter INSTANCE = new RelationshipNoFilter();

	public RelationshipNoFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean select(IRelationship artifact) {
		return true;
	}

}
