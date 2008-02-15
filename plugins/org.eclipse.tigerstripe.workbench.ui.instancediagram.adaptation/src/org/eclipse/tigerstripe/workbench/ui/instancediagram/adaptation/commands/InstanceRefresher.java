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

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.InstanceDiagramRefreshException;

public class InstanceRefresher extends BaseEObjectRefresher implements
		IEObjectRefresher {

	private IArtifactManagerSession artifactMgrSession;

	public InstanceRefresher(TransactionalEditingDomain editingDomain,
			IArtifactManagerSession artifactMgrSession) {
		super(editingDomain);
		this.artifactMgrSession = artifactMgrSession;
	}

	protected CompoundCommand createRefreshCommand(Instance element,
			IAbstractArtifact iArtifact) {
		CompoundCommand cCommand = new CompoundCommand();

		if (element instanceof ClassInstance) {
			// Attrs
			ClassInstanceUpdateCommand attrCmd = new ClassInstanceUpdateCommand(
					element, iArtifact);
			attrCmd.setInitialRefresh(isInitialRefresh());
			cCommand.append(attrCmd);
		}

		if (element instanceof AssociationInstance) {
			AssociationInstanceUpdateCommand endCmd = new AssociationInstanceUpdateCommand(
					element, iArtifact, artifactMgrSession);
			endCmd.setInitialRefresh(isInitialRefresh());
			cCommand.append(endCmd);
		}
		return cCommand;
	}

	public Command refresh(Instance element, IAbstractArtifact iArtifact)
			throws InstanceDiagramRefreshException {
		return createRefreshCommand(element, iArtifact);
	}

}
