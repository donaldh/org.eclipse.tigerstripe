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
 * A refactor command holds a set of set of IModelChangeRequest that were
 * computed as a result to a set of RefactorRequests.
 * 
 * @author erdillon
 * 
 */
public interface IRefactorCommand {

	/**
	 * The requests that were used to
	 * 
	 * @return
	 */
	public RefactorRequest[] getRequests();

	/**
	 * Executes the command and performs the actual refactoring.
	 * 
	 * @param monitor
	 * @throws TigerstripeException
	 */
	public void execute(IProgressMonitor monitor) throws TigerstripeException;
}
