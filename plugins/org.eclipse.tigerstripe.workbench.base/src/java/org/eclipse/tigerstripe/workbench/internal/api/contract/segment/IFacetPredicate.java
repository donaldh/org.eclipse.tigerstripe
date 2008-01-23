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
package org.eclipse.tigerstripe.workbench.internal.api.contract.segment;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.api.utils.TigerstripeError;
import org.eclipse.tigerstripe.workbench.internal.core.util.Predicate;

/**
 * Generic FacetPredicate
 * 
 * @author Eric Dillon
 * 
 */
public interface IFacetPredicate extends Predicate {

	public void resolve(ITigerstripeProgressMonitor monitor)
			throws TigerstripeException;

	/**
	 * Returns true if the submodel corresponding to the facet is determined to
	 * be consistent with the original model. (equivalent to
	 * getInconsistencies().length == 0)
	 * 
	 * Will return true if predicate is not resolved.
	 * 
	 * @return
	 */
	public boolean isConsistent();

	/**
	 * Returns an array of inconsistencies as found during facet resolution.
	 * 
	 * Returns empty array is predicate is not resolved.
	 * 
	 * @return
	 */
	public TigerstripeError[] getInconsistencies();
}
