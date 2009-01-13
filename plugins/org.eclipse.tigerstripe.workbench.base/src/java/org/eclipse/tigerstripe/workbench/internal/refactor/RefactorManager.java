/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.refactor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;

/**
 * The refactor manager is the main entry point for all Tigerstripe Refactoring
 * Operations.
 * 
 * @author erdillon
 * 
 */
public class RefactorManager {

	public static RefactorManager INSTANCE = new RefactorManager();

	private RefactorManager() {
		// this is a singleton
	}

	public IRefactorCommand getRefactorCommand(ModelRefactorRequest[] requests,
			IProgressMonitor monitor) throws TigerstripeException {
		throw new UnsupportedOperationException();
	}

	public IRefactorCommand getRefactorCommand(ProjectRefactorRequest[] requests,
			IProgressMonitor monitor) throws TigerstripeException {
		throw new UnsupportedOperationException();
	}

	public IRefactorCommand getRefactorCommand(DiagramRefactorRequest[] requests,
			IProgressMonitor monitor) throws TigerstripeException {
		throw new UnsupportedOperationException();
	}
}
