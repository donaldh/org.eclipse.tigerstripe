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
package org.eclipse.tigerstripe.workbench.model.deprecated_.ossj;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;

public interface IOssjEnumSpecifics extends IOssjArtifactSpecifics {

	public void setBaseIType(IType type);

	public void setExtensible(boolean extensible);

	/**
	 * Return the basetype for this Enumeration
	 * 
	 * @return
	 */
	public IType getBaseIType();

	/**
	 * Whether this enumeration can be extended.
	 * 
	 * @return
	 */
	public boolean getExtensible();
}
