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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.commands.IEObjectRefresher;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.commands.InstanceRefresher;

/**
 * A refresher factory that will provide a refresher for the given EObject
 * 
 * @author Eric Dillon
 * 
 */
public class InstanceDiagramRefresherFactory {

	private static InstanceDiagramRefresherFactory instance;

	private InstanceDiagramRefresherFactory() {
	}

	public static InstanceDiagramRefresherFactory getInstance() {
		if (instance == null) {
			instance = new InstanceDiagramRefresherFactory();
		}
		return instance;
	}

	public IEObjectRefresher getDefaultRefresher(
			TransactionalEditingDomain editingDomain, Instance element,
			IArtifactManagerSession artifactMgrSession)
			throws TigerstripeException {
		if (element instanceof ClassInstance)
			return new InstanceRefresher(editingDomain, artifactMgrSession);
		else if (element instanceof AssociationInstance)
			return new InstanceRefresher(editingDomain, artifactMgrSession);

		throw new TigerstripeException("Element "
				+ element.getFullyQualifiedName()
				+ " couldn't be refreshed, unknown IEObjectRefresher type ("
				+ element.getClass().getName());
	}
}
