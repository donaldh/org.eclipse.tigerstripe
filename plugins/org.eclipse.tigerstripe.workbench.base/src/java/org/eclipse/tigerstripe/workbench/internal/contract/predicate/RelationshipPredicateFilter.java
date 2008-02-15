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
package org.eclipse.tigerstripe.workbench.internal.contract.predicate;

import org.eclipse.tigerstripe.workbench.internal.core.model.RelationshipFilter;
import org.eclipse.tigerstripe.workbench.internal.core.util.Predicate;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;

public class RelationshipPredicateFilter extends RelationshipFilter {

	private Predicate predicate;

	public RelationshipPredicateFilter(Predicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public boolean select(IRelationship relationship) {
		return predicate.evaluate(relationship);
	}

}
