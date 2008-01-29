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

import java.util.Collection;
import java.util.List;

import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact.IExposedUpdateProcedure;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;

public class PostCreationUpdateProcedureArtifactUpdater extends
		PostCreationAbstractArtifactUpdater {

	public PostCreationUpdateProcedureArtifactUpdater(
			IAbstractArtifact iArtifact, AbstractArtifact eArtifact, Map map,
			ITigerstripeProject diagramProject) {
		super(iArtifact, eArtifact, map, diagramProject);
	}

	@Override
	protected void internalUpdateOutgoingConnections()
			throws TigerstripeException {
		super.internalUpdateOutgoingConnections();
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
						Collection<IExposedUpdateProcedure> procs = iSession
								.getExposedUpdateProcedures();
						for (IExposedUpdateProcedure proc : procs) {
							if (proc.getFullyQualifiedName().equals(
									getEArtifact().getFullyQualifiedName())) {
								((SessionFacadeArtifact) eArt)
										.getExposedProcedures().add(
												getEArtifact());
							}
						}
					}
				}
			}
		}

	}
}
