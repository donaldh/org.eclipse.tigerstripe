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

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramUtils;

public class AssociationInstanceUpdateCommand extends
		AbstractInstanceUpdateCommand {

	private boolean isAEndAssoc = false;
	private boolean isZEndAssoc = false;
	private IArtifactManagerSession artifactMgrSession;

	public AssociationInstanceUpdateCommand(Instance eArtifact,
			IAbstractArtifact iArtifact,
			IArtifactManagerSession artifactMgrSession) {
		super(eArtifact, iArtifact);
		this.artifactMgrSession = artifactMgrSession;
	}

	@Override
	public void updateInstance(Instance instance, IAbstractArtifact artifact) {
		if (!(instance instanceof AssociationInstance))
			throw new IllegalArgumentException("input Instance argument "
					+ instance + "is not an AssociationInstance");
		else if (!(artifact instanceof IAssociationArtifact))
			throw new IllegalArgumentException(
					"input IAbstractArtifact argument " + artifact
							+ "is not an IAssociationArtifact");
		AssociationInstance eAssociation = (AssociationInstance) instance;
		IAssociationArtifact iArtifact = (IAssociationArtifact) artifact;

		if (iArtifact instanceof IAssociationClassArtifact) {
			isAEndAssoc = eAssociation.getArtifactName().endsWith("::aEnd");
			isZEndAssoc = eAssociation.getArtifactName().endsWith("::zEnd");
		} else {
			isAEndAssoc = true;
			isZEndAssoc = true;
		}
		IAssociationEnd aEnd = (IAssociationEnd) iArtifact.getAEnd();
		IAssociationEnd zEnd = (IAssociationEnd) iArtifact.getZEnd();
		if (isAEndAssoc && aEnd.getType() != null) {
			// search for matching type using the fqn of the eAssociation's
			// aEnd (or any of it's parent types...)
			String aEndType = aEnd.getType().getFullyQualifiedName();
			String fqn = eAssociation.getAEnd().getFullyQualifiedName();
			IAbstractArtifact eArt = null;
			try {
				eArt = artifactMgrSession.getArtifactByFullyQualifiedName(fqn);
			} catch (TigerstripeException e) {
				eAssociation.setAEnd(null);
				return;
			}
			boolean matchingTypeFound = false;
			IAbstractArtifact localArt = eArt;
			do {
				if (localArt.getFullyQualifiedName().equals(aEndType)) {
					matchingTypeFound = true;
					break;
				}
			} while ((localArt = localArt.getExtendedArtifact()) != null);
			if (!matchingTypeFound) {
				eAssociation.setAEnd(null);
				// if eArt == null the dependency will be removed downstream
				return;
			}
		}

		if (isZEndAssoc && zEnd.getType() != null) {
			// search for matching type using the fqn of the eAssociation's
			// zEnd (or any of it's parent types...)
			String zEndType = zEnd.getType().getFullyQualifiedName();
			String fqn = eAssociation.getZEnd().getFullyQualifiedName();
			IAbstractArtifact eArt = null;
			try {
				eArt = artifactMgrSession.getArtifactByFullyQualifiedName(fqn);
			} catch (TigerstripeException e) {
				eAssociation.setAEnd(null);
				return;
			}
			boolean matchingTypeFound = false;
			IAbstractArtifact localArt = eArt;
			do {
				if (localArt.getFullyQualifiedName().equals(zEndType)) {
					matchingTypeFound = true;
					break;
				}
			} while ((localArt = localArt.getExtendedArtifact()) != null);
			if (!matchingTypeFound) {
				eAssociation.setAEnd(null);
				// if eArt == null the dependency will be removed downstream
				return;
			}
		}

		if (isAEndAssoc && !aEnd.getName().equals(eAssociation.getAEndName())) {
			eAssociation.setAEndName(aEnd.getName());
		}
		if (isAEndAssoc
				&& aEnd.isNavigable() != eAssociation.isAEndIsNavigable()) {
			eAssociation.setAEndIsNavigable(aEnd.isNavigable());
		}
		if (isAEndAssoc && aEnd.isOrdered() != eAssociation.isAEndIsOrdered()) {
			eAssociation.setAEndIsOrdered(aEnd.isOrdered());
		}

		if (isZEndAssoc && !zEnd.getName().equals(eAssociation.getZEndName())) {
			eAssociation.setZEndName(zEnd.getName());
		}
		if (isZEndAssoc
				&& zEnd.isNavigable() != eAssociation.isZEndIsNavigable()) {
			eAssociation.setZEndIsNavigable(zEnd.isNavigable());
		}
		if (isZEndAssoc && zEnd.isOrdered() != eAssociation.isZEndIsOrdered()) {
			eAssociation.setZEndIsOrdered(zEnd.isOrdered());
		}

		InstanceDiagramUtils.updateAggregation(eAssociation, iArtifact,
				isAEndAssoc, isZEndAssoc);
		InstanceDiagramUtils.updateMultiplicity(eAssociation, iArtifact,
				isAEndAssoc, isZEndAssoc);
		InstanceDiagramUtils.updateChangeable(eAssociation, iArtifact,
				isAEndAssoc, isZEndAssoc);

	}

	public void redo() {
		// TODO Auto-generated method stub
	}

}
