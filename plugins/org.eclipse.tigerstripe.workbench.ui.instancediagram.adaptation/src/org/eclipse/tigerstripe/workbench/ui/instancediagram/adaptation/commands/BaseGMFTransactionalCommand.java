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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.commands;

import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;

public class BaseGMFTransactionalCommand extends AbstractTransactionalCommand {

	public BaseGMFTransactionalCommand(TransactionalEditingDomain domain,
			String label, List affectedFiles) {
		super(domain, label, affectedFiles);
		// TODO Auto-generated constructor stub
	}

	public BaseGMFTransactionalCommand(TransactionalEditingDomain domain,
			String label, Map options, List affectedFiles) {
		super(domain, label, options, affectedFiles);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
			IAdaptable info) throws ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

}
