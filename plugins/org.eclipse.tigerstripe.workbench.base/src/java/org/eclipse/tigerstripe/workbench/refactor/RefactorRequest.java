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
package org.eclipse.tigerstripe.workbench.refactor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.tigerstripe.workbench.TigerstripeException;

public abstract class RefactorRequest {

	public abstract IStatus isValid();

	/**
	 * Returns the command to execute to perform this request
	 * 
	 * @return
	 */
	public IRefactorCommand getCommand(IProgressMonitor monitor)
			throws TigerstripeException {
		return IRefactorCommand.UNEXECUTABLE;
	}
}
