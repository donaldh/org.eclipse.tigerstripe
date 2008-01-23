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
package org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request;

/**
 * Artifact Link creation request
 * 
 * @author Eric Dillon
 * 
 */
public interface IArtifactLinkCreateRequest extends IArtifactCreateRequest {

	public void setAEndType(String aEndType);

	public void setAEndName(String aEndName);

	public void setAEndMultiplicity(String aEndMultiplicity);

	public void setAEndNavigability(boolean isNavigable);

	public void setZEndType(String zEndType);

	public void setZEndName(String zEndName);

	public void setZEndMultiplicity(String zEndMultiplicity);

	public void setZEndNavigability(boolean isNavigable);

}
