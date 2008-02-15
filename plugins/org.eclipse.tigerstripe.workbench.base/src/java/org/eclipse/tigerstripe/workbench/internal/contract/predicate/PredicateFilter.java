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

import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactFilter;
import org.eclipse.tigerstripe.workbench.internal.core.util.Predicate;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

public class PredicateFilter extends ArtifactFilter {

	private Predicate predicate;

	public PredicateFilter(Predicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public boolean select(IAbstractArtifact artifact) {
		return predicate.evaluate(artifact);
	}

}
