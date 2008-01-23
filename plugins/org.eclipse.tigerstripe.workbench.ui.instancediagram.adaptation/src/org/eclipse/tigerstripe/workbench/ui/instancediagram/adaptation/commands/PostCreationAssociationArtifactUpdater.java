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

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.IAssociationEnd;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramUtils;

public class PostCreationAssociationArtifactUpdater extends
		BasePostCreationElementUpdater {

	protected AssociationInstance association;

	public PostCreationAssociationArtifactUpdater(IAbstractArtifact iArtifact,
			AssociationInstance association, InstanceMap map,
			ITigerstripeProject diagramProject) {
		super(iArtifact, map, diagramProject);
		this.association = association;
	}

	@Override
	public void updateConnections() throws TigerstripeException {

		// try {
		// BaseETAdapter.setIgnoreNotify(true);

		IAssociationArtifact iAssoc = (IAssociationArtifact) getIArtifact();
		IAssociationEnd aEnd = iAssoc.getAEnd();
		association.setAEndIsNavigable(aEnd.isNavigable());
		association.setAEndIsOrdered(aEnd.isOrdered());
		association.setAEndName(aEnd.getName());
		IAssociationEnd zEnd = iAssoc.getZEnd();
		association.setZEndIsNavigable(zEnd.isNavigable());
		association.setZEndIsOrdered(zEnd.isOrdered());
		association.setZEndName(zEnd.getName());
		InstanceDiagramUtils.updateAggregation(association, iAssoc, true, true);
		InstanceDiagramUtils
				.updateMultiplicity(association, iAssoc, true, true);
		InstanceDiagramUtils.updateChangeable(association, iAssoc, true, true);

		// } finally {
		// BaseETAdapter.setIgnoreNotify(false);
		// }

	}

}
