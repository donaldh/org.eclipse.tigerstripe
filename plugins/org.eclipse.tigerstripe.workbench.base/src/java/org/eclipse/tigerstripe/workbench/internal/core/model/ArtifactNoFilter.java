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

import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;

/**
 * A filter that filters... NOTHING.
 * 
 * @author Eric Dillon
 * 
 */
public class ArtifactNoFilter extends ArtifactFilter {

	public ArtifactNoFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean select(IAbstractArtifact artifact) {
		return true;
	}

}
