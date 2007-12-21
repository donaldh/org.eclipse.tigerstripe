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
package org.eclipse.tigerstripe.api.impl.updater.request;

import org.eclipse.tigerstripe.api.impl.updater.BaseModelChangeRequest;

/**
 * Base class for any request related to an artifact element (i.e where the
 * actual artifact needs to be identified)
 * 
 * @author Eric Dillon
 * 
 */
public abstract class BaseArtifactElementRequest extends BaseModelChangeRequest {

	private String artifactFQN;

	public String getArtifactFQN() {
		return this.artifactFQN;
	}

	public void setArtifactFQN(String artifactFQN) {
		this.artifactFQN = artifactFQN;
	}

}
