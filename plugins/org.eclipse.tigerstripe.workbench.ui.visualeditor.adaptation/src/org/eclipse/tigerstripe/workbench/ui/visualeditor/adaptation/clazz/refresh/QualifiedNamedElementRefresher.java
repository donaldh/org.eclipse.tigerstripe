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

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;

public class QualifiedNamedElementRefresher extends BaseEObjectRefresher {

	public QualifiedNamedElementRefresher(
			TransactionalEditingDomain editingDomain) {
		super(editingDomain);
	}

	protected CompoundCommand createRefreshCommand(
			QualifiedNamedElement element, IAbstractArtifact iArtifact) {
		CompoundCommand cCommand = new CompoundCommand();

		StereotypeUpdateCommand stereotypeUpdateCommand = new StereotypeUpdateCommand(
				element, iArtifact);
		stereotypeUpdateCommand.setInitialRefresh(isInitialRefresh());
		cCommand.append(stereotypeUpdateCommand);

		return cCommand;
	}
}
