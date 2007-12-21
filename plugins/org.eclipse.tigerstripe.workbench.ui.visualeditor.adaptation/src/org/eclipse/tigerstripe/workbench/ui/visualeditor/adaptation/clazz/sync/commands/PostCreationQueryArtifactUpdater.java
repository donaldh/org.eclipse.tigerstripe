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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.commands;

import java.util.List;

import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IType;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IOssjQuerySpecifics;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IQueryArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.ISessionArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.ISessionArtifact.INamedQuery;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;

public class PostCreationQueryArtifactUpdater extends
		PostCreationAbstractArtifactUpdater {

	public PostCreationQueryArtifactUpdater(IAbstractArtifact iArtifact,
			AbstractArtifact eArtifact, Map map,
			ITigerstripeProject diagramProject) {
		super(iArtifact, eArtifact, map, diagramProject);
	}

	@Override
	protected void internalUpdateOutgoingConnections()
			throws TigerstripeException {
		super.internalUpdateOutgoingConnections();

		IQueryArtifact query = (IQueryArtifact) getIArtifact();
		IOssjQuerySpecifics specifics = (IOssjQuerySpecifics) query
				.getIStandardSpecifics();

		if (specifics.getReturnedEntityIType() != null) {
			IType returnedType = specifics.getReturnedEntityIType();
			Map map = (Map) getEArtifact().eContainer();
			MapHelper helper = new MapHelper(map);
			AbstractArtifact eReturnedArtifact = helper
					.findAbstractArtifactFor(returnedType
							.getFullyQualifiedName());
			if (eReturnedArtifact != null) {
				((NamedQueryArtifact) getEArtifact())
						.setReturnedType(eReturnedArtifact);
			}
		}
	}

	@Override
	protected void internalUpdateIncomingConnections()
			throws TigerstripeException {
		super.internalUpdateIncomingConnections();

		// Handle all sessions that support this query
		IArtifactManagerSession session = getDiagramProject()
				.getArtifactManagerSession();
		List<AbstractArtifact> eArtifacts = getMap().getArtifacts();
		for (AbstractArtifact eArt : eArtifacts) {
			if (eArt instanceof SessionFacadeArtifact) {
				String fqn = eArt.getFullyQualifiedName();
				if (fqn != null && fqn.length() != 0) {
					IAbstractArtifact iArt = session
							.getArtifactByFullyQualifiedName(fqn);
					if (iArt instanceof ISessionArtifact) {
						ISessionArtifact iSession = (ISessionArtifact) iArt;
						INamedQuery[] queries = iSession.getINamedQueries();
						for (INamedQuery query : queries) {
							if (query.getFullyQualifiedName().equals(
									getEArtifact().getFullyQualifiedName())) {
								((SessionFacadeArtifact) eArt)
										.getNamedQueries().add(getEArtifact());
							}
						}
					}
				}
			}
		}

	}
}
