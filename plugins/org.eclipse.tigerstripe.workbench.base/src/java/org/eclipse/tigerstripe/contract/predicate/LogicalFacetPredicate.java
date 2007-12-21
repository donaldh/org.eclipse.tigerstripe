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
package org.eclipse.tigerstripe.contract.predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.tigerstripe.api.contract.segment.IFacetPredicate;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.api.utils.TigerstripeError;
import org.eclipse.tigerstripe.core.util.LogicalPredicate;
import org.eclipse.tigerstripe.core.util.Predicate;

public class LogicalFacetPredicate extends LogicalPredicate implements
		IFacetPredicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2228505957432230254L;

	public LogicalFacetPredicate(String expressionType) {
		super(expressionType);
	}

	public LogicalFacetPredicate(IFacetPredicate[] predicates,
			String expressionType) {
		super(expressionType);
		addAll(Arrays.asList(predicates));
	}

	public TigerstripeError[] getInconsistencies() {
		List<TigerstripeError> result = new ArrayList<TigerstripeError>();
		int s = size();
		for (int i = 0; i < s; i++) {
			Predicate pred = get(i);
			if (pred instanceof IFacetPredicate) {
				IFacetPredicate fPred = (IFacetPredicate) pred;
				result.addAll(Arrays.asList(fPred.getInconsistencies()));
			}
		}
		return result.toArray(new TigerstripeError[result.size()]);
	}

	public boolean isConsistent() {
		return getInconsistencies().length == 0;
	}

	public void resolve(ITigerstripeProgressMonitor monitor) {

	}

}
