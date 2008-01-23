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
package org.eclipse.tigerstripe.workbench.internal.api.model;

import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;

/**
 * Listener for Active Facet changes in a project
 * 
 * @author Eric Dillon
 * 
 */
public interface IActiveFacetChangeListener {

	public void facetChanged(IFacetReference oldFacet, IFacetReference newFacet);

}
