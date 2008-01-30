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
package org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact.IEmittedEvent;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact.IExposedUpdateProcedure;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact.IManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact.INamedQuery;

public class ArtifactAddFeatureRequest extends BaseArtifactElementRequest
		implements IArtifactAddFeatureRequest {

	private String featureId;

	private String featureValue;

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		try{
		IAbstractArtifact art = mgrSession
				.getArtifactByFullyQualifiedName(getArtifactFQN());

		if (EXPOSED_PROCEDURES.equals(featureId)
				|| MANAGED_ENTITIES.equals(featureId)
				|| EMITTED_NOTIFICATIONS.equals(featureId)
				|| NAMED_QUERIES.equals(featureId))
			return art instanceof ISessionArtifact;
		else if (IMPLEMENTS_FEATURE.equals(featureId))
			return art instanceof IManagedEntityArtifact
					|| art instanceof IAssociationClassArtifact;
		return false;
	}
	catch (TigerstripeException t){
		return false;
	}
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		IAbstractArtifact art = mgrSession
				.getArtifactByFullyQualifiedName(getArtifactFQN());
		if (art instanceof ISessionArtifact) {
			ISessionArtifact session = (ISessionArtifact) art;
			if (EXPOSED_PROCEDURES.equals(featureId)) {
				IExposedUpdateProcedure proc = session
						.makeExposedUpdateProcedure();
				proc.setFullyQualifiedName(featureValue);
				session.addExposedUpdateProcedure(proc);
				session.doSave(new TigerstripeNullProgressMonitor()); // FIXME
			} else if (MANAGED_ENTITIES.equals(featureId)) {
				IManagedEntityDetails details = session
						.makeManagedEntityDetails();
				details.setFullyQualifiedName(featureValue);
				session.addManagedEntityDetails(details);
				session.doSave(new TigerstripeNullProgressMonitor());
			} else if (EMITTED_NOTIFICATIONS.equals(featureId)) {
				IEmittedEvent event = session.makeEmittedEvent();
				event.setFullyQualifiedName(featureValue);
				session.addEmittedEvent(event);
				session.doSave(new TigerstripeNullProgressMonitor());
			} else if (NAMED_QUERIES.equals(featureId)) {
				INamedQuery query = session.makeNamedQuery();
				query.setFullyQualifiedName(featureValue);
				session.addNamedQuery(query);
				session.doSave(new TigerstripeNullProgressMonitor());
			}
		} else if (art instanceof IManagedEntityArtifact) {
			IManagedEntityArtifact me = (IManagedEntityArtifact) art;
			Collection<IAbstractArtifact> arts = me.getImplementedArtifacts();
			List<IAbstractArtifact> list = new ArrayList<IAbstractArtifact>();
			list.addAll(arts);
			IAbstractArtifact target = mgrSession
					.getArtifactByFullyQualifiedName(featureValue);
			if (target != null) {
				list.add(target);
				me.setImplementedArtifacts(list);
				me.doSave(new TigerstripeNullProgressMonitor());
			}
		}
	}

	public void setFeatureId(String featureID) {
		this.featureId = featureID;
	}

	public void setFeatureValue(String value) {
		this.featureValue = value;
	}

}
