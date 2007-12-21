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
package org.eclipse.tigerstripe.api.external.plugins;

import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;

/**
 * 
 * This interface must be implemented by Artifact filters when used to limit the
 * scope of an ArtifactBased rule
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IArtifactFilter {

	/**
	 * Determines if the given artifact passes this filter.
	 * 
	 * @param artifact -
	 *            Artifact to compare against the filter
	 * @return true iff the object is accepted by the filter.
	 */
	public boolean select(IArtifact artifact);

}
