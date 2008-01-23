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
package org.eclipse.tigerstripe.workbench.internal.core.util;

import java.util.ArrayList;

public class LogicalPredicate extends ArrayList<Predicate> implements Predicate {

	private static final long serialVersionUID = 3027654695285880664L;
	public static final String OR = "OR";
	public static final String AND = "AND";
	private String expressionType;

	public LogicalPredicate(String expressionType) {
		if (expressionType != LogicalPredicate.AND
				&& expressionType != LogicalPredicate.OR)
			throw new IllegalArgumentException("unsupported relationshipType '"
					+ expressionType + "' in constructor; must be either '"
					+ LogicalPredicate.AND + "' or '" + LogicalPredicate.OR
					+ "'");
		this.expressionType = expressionType;
	}

	public boolean evaluate(Object obj) {
		// if no predicate has been added to the list, return true (always)
		if (this.size() == 0)
			return true;

		for (Predicate predicate : this) {
			boolean test = predicate.evaluate(obj);
			// if it's an AND expression and any element is false,
			// then the entire expression is false
			if (expressionType == LogicalPredicate.AND && !test)
				return false;
			else if (expressionType == LogicalPredicate.OR && test)
				return true;
		}
		// if get to here and it's an AND expression and nothing failed,
		// so return true
		if (expressionType == LogicalPredicate.AND)
			return true;
		// else it's an OR expression and nothing matched, so return false
		return false;
	}

}
