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
package org.eclipse.tigerstripe.api.artifacts.model.ossj;

import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextManagedEntityArtifact;

public interface IManagedEntityArtifact extends IAbstractArtifact,
		IextManagedEntityArtifact {

	public final static String DEFAULT_LABEL = "Entity";

	public interface IPrimaryKey extends IextPrimaryKey {

		public void setFullyQualifiedName(String fullyQualifiedName);

	}

}
