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
package org.eclipse.tigerstripe.api.model.artifacts;

import java.util.List;

import org.eclipse.tigerstripe.api.model.IRelationship;
import org.eclipse.tigerstripe.api.model.IType;
import org.eclipse.tigerstripe.api.utils.TigerstripeError;

/**
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IDependencyArtifact extends IAbstractArtifact, IRelationship {

	public final static String DEFAULT_LABEL = "Dependency";

	public void setAEndType(IType aEndType);

	public void setZEndType(IType zEndType);

	public IType makeIType();

	public List<TigerstripeError> validate();

	/**
	 * The type of the AEnd for this Dependency.
	 * 
	 * @return AEnd type
	 */
	public IType getAEndType();

	/**
	 * The type of the ZEnd for this Dependency.
	 * 
	 * @return ZEnd type
	 */
	public IType getZEndType();

}
