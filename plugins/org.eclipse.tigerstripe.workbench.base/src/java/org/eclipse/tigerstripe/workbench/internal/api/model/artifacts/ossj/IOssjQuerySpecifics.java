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
package org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;

public interface IOssjQuerySpecifics extends IOssjArtifactSpecifics {

	public void setReturnedEntityIType(IType type);

	public void setSingleExtensionType(boolean single);

	public void setSessionFactoryMethods(boolean sessionBased);

	/**
	 * The returned type for this Query Artifact
	 * 
	 * @return
	 */
	public IType getReturnedEntityIType();

	/**
	 * Whether methods are session factory methods.
	 * 
	 * @return
	 */
	public boolean isSessionFactoryMethods();

	/**
	 * Whether this datatype can only be extended with a single stream of
	 * datatypes.
	 * 
	 * @return
	 */
	public boolean isSingleExtensionType();

}
