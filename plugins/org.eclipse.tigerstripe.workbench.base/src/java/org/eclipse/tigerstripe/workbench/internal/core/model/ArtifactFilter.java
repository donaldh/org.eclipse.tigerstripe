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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

/**
 * Allows to filter returned artifacts when querying the Artifact Manager
 * 
 * @author Eric Dillon
 * 
 */
public abstract class ArtifactFilter {

	/**
	 * 
	 * @param artifact
	 * @return
	 */
	public abstract boolean select(IAbstractArtifact artifact);

	public static Collection<IAbstractArtifact> filter(
			Collection<IAbstractArtifact> artifactList, ArtifactFilter filter) {

		ArrayList<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();
		
		// Little acceleration for performance
		if (filter == null || filter instanceof ArtifactNoFilter){
			if (artifactList != null){
				return artifactList;
			} else {
				return result;
			}
		}
			

		for (Iterator iter = artifactList.iterator(); iter.hasNext();) {
			IAbstractArtifact artifact = (IAbstractArtifact) iter.next();
			if (filter.select(artifact)) {
				result.add(artifact);
			}
		}

		return result;
	}
}
