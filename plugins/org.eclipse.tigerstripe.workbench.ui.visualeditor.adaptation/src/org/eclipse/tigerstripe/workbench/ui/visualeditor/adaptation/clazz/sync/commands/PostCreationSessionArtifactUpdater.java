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

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IEmittedEvent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IExposedUpdateProcedure;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.INamedQuery;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NotificationArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;

public class PostCreationSessionArtifactUpdater extends
		PostCreationAbstractArtifactUpdater {

	public PostCreationSessionArtifactUpdater(IAbstractArtifact iArtifact,
			AbstractArtifact eArtifact, Map map,
			ITigerstripeModelProject diagramProject, boolean hideExtends) {
		super(iArtifact, eArtifact, map, diagramProject, hideExtends);
	}

	@Override
	protected void internalUpdateOutgoingConnections()
			throws TigerstripeException {
		super.internalUpdateOutgoingConnections();

		ISessionArtifact session = (ISessionArtifact) getIArtifact();

		// look thru the supported queries
		Collection<INamedQuery> queries = session.getNamedQueries();
		for (INamedQuery query : queries) {
			MapHelper helper = new MapHelper(getMap());
			NamedQueryArtifact eQuery = (NamedQueryArtifact) helper
					.findAbstractArtifactFor(query.getFullyQualifiedName());
			if (eQuery != null) {
				((SessionFacadeArtifact) getEArtifact()).getNamedQueries().add(
						eQuery);
			}
		}

		// look thru the exposed update procedures
		Collection<IExposedUpdateProcedure> procs = session
				.getExposedUpdateProcedures();
		for (IExposedUpdateProcedure proc : procs) {
			MapHelper helper = new MapHelper(getMap());
			UpdateProcedureArtifact eProc = (UpdateProcedureArtifact) helper
					.findAbstractArtifactFor(proc.getFullyQualifiedName());
			if (eProc != null) {
				((SessionFacadeArtifact) getEArtifact()).getExposedProcedures()
						.add(eProc);
			}
		}

		// look thru the emitted Notifications
		Collection<IEmittedEvent> events = session.getEmittedEvents();
		for (IEmittedEvent event : events) {
			MapHelper helper = new MapHelper(getMap());
			NotificationArtifact eEvent = (NotificationArtifact) helper
					.findAbstractArtifactFor(event.getFullyQualifiedName());
			if (eEvent != null) {
				((SessionFacadeArtifact) getEArtifact())
						.getEmittedNotifications().add(eEvent);
			}
		}

		// look thru the emitted Notifications
		Collection<IManagedEntityDetails> details = session
				.getManagedEntityDetails();
		for (IManagedEntityDetails detail : details) {
			MapHelper helper = new MapHelper(getMap());
			ManagedEntityArtifact eArt = (ManagedEntityArtifact) helper
					.findAbstractArtifactFor(detail.getFullyQualifiedName());
			if (eArt != null) {
				((SessionFacadeArtifact) getEArtifact()).getManagedEntities()
						.add(eArt);
			}
		}
	}

	@Override
	protected void internalUpdateIncomingConnections()
			throws TigerstripeException {
		super.internalUpdateIncomingConnections();
	}
}
