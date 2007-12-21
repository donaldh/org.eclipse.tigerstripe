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
package org.eclipse.tigerstripe.api.external.model;

import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;

/**
 * This class represents a Label for an IArtifact.
 * 
 * An IextLabel is the model representation of an constant of a Tigerstripe
 * Artifact. For Enumeration artifacts the IextLabels represent the possible
 * enumerated values.
 * 
 */
public interface IextLabel extends IextModelComponent {

	/**
	 * Returns the value of the label. The return will be a String irrespetive
	 * of the "type" of the Label.
	 * 
	 * @return String - the value of the label
	 */
	public String getValue();

	/**
	 * Returns the type of this label.
	 * 
	 * @return IextType - the type of this label
	 */
	public IextType getIextType();

	/**
	 * Returns the IArtifact that is the "container" for the Label.
	 * 
	 * @return the containing artifact.
	 */
	public IArtifact getContainingArtifact();

}
