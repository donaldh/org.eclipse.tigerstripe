/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jim Strawn (Cisco Systems, Inc.) - initial implementation
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.internal.core.model.export;

import org.eclipse.tigerstripe.workbench.TigerstripeException;

// Do we want to move this to a more generic location (do we think we may want multiple
// exports, maybe roll in module exports here?? maybe define these in extension point, and 
// roll all this into one Export Wizard?

public interface IModelExporter {

	/**
	 * Exports a complete Tigerstripe model to a destination artifact defined by
	 * the implementation.
	 * 
	 * @param includeReferences
	 * @throws TigerstripeException
	 */
	public abstract void export(boolean includeReferences) throws TigerstripeException;
}
