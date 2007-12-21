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
package org.eclipse.tigerstripe.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationClassArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IDependencyArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IDatatypeArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEnumArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEventArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IExceptionArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IManagedEntityArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IQueryArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.ISessionArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.queries.IQueryArtifactsByType;
import org.eclipse.tigerstripe.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.core.model.ArtifactManager;
import org.eclipse.tigerstripe.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.core.model.EnumArtifact;
import org.eclipse.tigerstripe.core.model.EventArtifact;
import org.eclipse.tigerstripe.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.core.model.PrimitiveTypeArtifact;
import org.eclipse.tigerstripe.core.model.QueryArtifact;
import org.eclipse.tigerstripe.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.core.model.UpdateProcedureArtifact;

public class QueryArtifactsByType extends ArtifactQueryBase implements
		IQueryArtifactsByType {

	// #195 We need to map the internal representations with the API ones...
	private static String[] supportedArtifactTypes = {
			IManagedEntityArtifact.class.getName(),
			IDatatypeArtifact.class.getName(), IEnumArtifact.class.getName(),
			IEventArtifact.class.getName(), IExceptionArtifact.class.getName(),
			IQueryArtifact.class.getName(),
			IUpdateProcedureArtifact.class.getName(),
			ISessionArtifact.class.getName(),
			IAssociationClassArtifact.class.getName(),
			IAssociationArtifact.class.getName(),
			IDependencyArtifact.class.getName(),
			IPrimitiveTypeArtifact.class.getName() };

	private static String[] implArtifactTypes = {
			ManagedEntityArtifact.class.getName(),
			DatatypeArtifact.class.getName(), EnumArtifact.class.getName(),
			EventArtifact.class.getName(), ExceptionArtifact.class.getName(),
			QueryArtifact.class.getName(),
			UpdateProcedureArtifact.class.getName(),
			SessionFacadeArtifact.class.getName(),
			AssociationClassArtifact.class.getName(),
			AssociationArtifact.class.getName(),
			DependencyArtifact.class.getName(),
			PrimitiveTypeArtifact.class.getName() };

	private String artifactType;

	public String[] getSupportedTypes() {
		return supportedArtifactTypes;
	}

	public QueryArtifactsByType() {
		super();
	}

	@Override
	public Collection run(IArtifactManagerSession managerSession) {

		ArtifactManagerSessionImpl impl = (ArtifactManagerSessionImpl) managerSession;
		ArtifactManager mgr = impl.getArtifactManager();

		Collection registered = mgr.getRegisteredArtifacts();

		for (Iterator iter = registered.iterator(); iter.hasNext();) {
			AbstractArtifact model = (AbstractArtifact) iter.next();
			if (model.getClass().getName().equals(
					mappedImplementationType(artifactType)))
				return mgr.getArtifactsByModel(model, includeDependencies(),
						getProgressMonitor());
		}

		// TODO Auto-generated method stub
		return new ArrayList();
	}

	public void setArtifactType(String type) {
		this.artifactType = type;
	}

	public String getArtifactType() {
		return this.artifactType;
	}

	private static String mappedImplementationType(String apiType) {

		for (int index = 0; index < supportedArtifactTypes.length; index++) {
			if (supportedArtifactTypes[index].equals(apiType))
				return implArtifactTypes[index];
		}
		return apiType;
	}

	public final static AbstractArtifact getArtifactClassForType(
			ArtifactManager mgr, String artifactType)
			throws TigerstripeException {
		Collection registered = mgr.getRegisteredArtifacts();

		for (Iterator iter = registered.iterator(); iter.hasNext();) {
			AbstractArtifact model = (AbstractArtifact) iter.next();
			if (model.getClass().getName().equals(
					mappedImplementationType(artifactType)))
				return model;
		}

		throw new TigerstripeException("Unknown type: " + artifactType);
	}
}
