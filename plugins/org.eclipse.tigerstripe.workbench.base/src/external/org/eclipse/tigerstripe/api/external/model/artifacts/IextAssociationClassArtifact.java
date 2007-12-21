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
package org.eclipse.tigerstripe.api.external.model.artifacts;

import org.eclipse.tigerstripe.api.external.model.IextField;

/**
 * Tigerstripe Abstraction for UML Association
 * 
 * @since 1.2
 */
public interface IextAssociationClassArtifact extends IextAssociationArtifact {

	/**
	 * Returns the fields defined for this association Class.
	 * 
	 * @return IextField[] - an array of all the fields for this artifact
	 */
	public IextField[] getIextFields();

}
