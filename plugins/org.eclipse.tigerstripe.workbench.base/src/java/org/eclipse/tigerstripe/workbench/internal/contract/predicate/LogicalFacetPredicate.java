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

import java.util.Arrays;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetPredicate;
import org.eclipse.tigerstripe.workbench.internal.core.util.LogicalPredicate;
import org.eclipse.tigerstripe.workbench.internal.core.util.Predicate;

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

	public IStatus getInconsistencies() {
		MultiStatus result = new MultiStatus(BasePlugin.getPluginId(), 222,
				"Facet Inconsistencies", null);
		int s = size();
		for (int i = 0; i < s; i++) {
			Predicate pred = get(i);
			if (pred instanceof IFacetPredicate) {
				IFacetPredicate fPred = (IFacetPredicate) pred;
				result.add(fPred.getInconsistencies());
			}
		}
		return result;
	}

	public boolean isConsistent() {
		return getInconsistencies().isOK();
	}

	public void resolve(IProgressMonitor monitor) {

	}

}
