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

import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;

public abstract class AbstractInstanceUpdateCommand extends BaseCommand {

	private Instance eArtifact;
	private IAbstractArtifact iArtifact;

	public AbstractInstanceUpdateCommand(Instance eArtifact,
			IAbstractArtifact iArtifact) {
		this.eArtifact = eArtifact;
		this.iArtifact = iArtifact;
	}

	@Override
	public boolean canExecute() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canUndo() {
		return false;
	}

	public void execute() {
		updateInstance(eArtifact, iArtifact);
	}

	public abstract void updateInstance(Instance eArtifact,
			IAbstractArtifact iArtifact);

}
