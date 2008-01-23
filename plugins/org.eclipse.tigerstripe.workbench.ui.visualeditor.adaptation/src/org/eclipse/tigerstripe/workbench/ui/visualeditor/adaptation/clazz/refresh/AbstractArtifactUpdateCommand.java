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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh;

import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;

public abstract class AbstractArtifactUpdateCommand extends BaseCommand {

	private AbstractArtifact eArtifact;
	private IAbstractArtifact iArtifact;

	protected AbstractArtifactUpdateCommand(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		this.eArtifact = eArtifact;
		this.iArtifact = iArtifact;
	}

	protected AbstractArtifact getEArtifact() {
		return eArtifact;
	}

	protected IAbstractArtifact getIArtifact() {
		return iArtifact;
	}

	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	public boolean canUndo() {
		return false;
	}

	public void execute() {
		updateEArtifact(eArtifact, iArtifact);
	}

	public abstract void updateEArtifact(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact);

}
