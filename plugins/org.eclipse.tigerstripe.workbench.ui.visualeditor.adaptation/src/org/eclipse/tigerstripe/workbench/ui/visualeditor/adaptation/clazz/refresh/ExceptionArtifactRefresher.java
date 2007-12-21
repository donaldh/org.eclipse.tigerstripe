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

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;

/**
 * Builds a command to refresh a Datatyppe in a diagram
 * 
 * @author Eric Dillon
 * 
 */
public class ExceptionArtifactRefresher extends AbstractArtifactRefresher
		implements IEObjectRefresher {

	public ExceptionArtifactRefresher(TransactionalEditingDomain editingDomain) {
		super(editingDomain);
	}

	@Override
	public Command refresh(QualifiedNamedElement element,
			IAbstractArtifact iArtifact) throws DiagramRefreshException {
		return createRefreshCommand(element, iArtifact);
	}

}
