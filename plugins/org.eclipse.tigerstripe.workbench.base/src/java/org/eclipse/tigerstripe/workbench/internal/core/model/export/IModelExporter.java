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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;


public interface IModelExporter {

	/**
	 * Exports a complete Tigerstripe model to a destination artifact defined by
	 * the implementation.
	 * 
	 * @param includeReferences
	 * @param monitor
	 * 
	 * @throws TigerstripeException
	 * @throws CoreException 
	 */
	public abstract void export(boolean includeReferences, IProgressMonitor monitor) throws TigerstripeException, CoreException;
}
