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

import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.internal.refactor.BaseRefactorCommand;
import org.eclipse.tigerstripe.workbench.internal.refactor.diagrams.DiagramChangeDelta;

/**
 * A refactor command holds a set of set of changes that were computed as a
 * result to a set of RefactorRequests.
 * 
 * @author erdillon
 * 
 */
public interface IRefactorCommand {

	public static IRefactorCommand UNEXECUTABLE = new BaseRefactorCommand(
			new RefactorRequest[0]);

	public Collection<ModelChangeDelta> getDeltas();
	
	public Collection<DiagramChangeDelta> getDiagramDeltas();

	/**
	 * All the request to be serviced by this command. Note that this will
	 * contain the initial request first, and all the derived requests
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
