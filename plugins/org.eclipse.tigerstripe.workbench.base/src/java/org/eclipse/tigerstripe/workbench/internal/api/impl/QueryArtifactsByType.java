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
package org.eclipse.tigerstripe.workbench.internal.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExecutionContext;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.PackageArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.PrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.queries.IQueryArtifactsByType;

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
			IPrimitiveTypeArtifact.class.getName(),
			IPackageArtifact.class.getName() };

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
			PrimitiveTypeArtifact.class.getName(),
			PackageArtifact.class.getName() };

	private String artifactType;

	public String[] getSupportedTypes() {
		return supportedArtifactTypes;
	}

	public QueryArtifactsByType() {
		super();
	}

	@Override
	public Collection<IAbstractArtifact> run(IArtifactManagerSession managerSession) {

		ArtifactManagerSessionImpl impl = (ArtifactManagerSessionImpl) managerSession;
		ArtifactManager mgr = impl.getArtifactManager();

		Collection<IAbstractArtifact> registered = mgr.getRegisteredArtifacts();

		for (Iterator<IAbstractArtifact> iter = registered.iterator(); iter.hasNext();) {
			IAbstractArtifactInternal model = (IAbstractArtifactInternal) iter
					.next();
			if (model.getClass().getName()
					.equals(mappedImplementationType(artifactType))) {

				ExecutionContext context = getExecutionContext();

				if (context == null) {
					return mgr.getArtifactsByModel(model,
							includeDependencies(), getProgressMonitor());
				} else {
					return mgr.getArtifactsByModel(model,
							includeDependencies(), context);
				}

			}
		}
		return new ArrayList<IAbstractArtifact>();
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

	public final static IAbstractArtifactInternal getArtifactClassForType(
			ArtifactManager mgr, String artifactType)
			throws TigerstripeException {
		Collection<IAbstractArtifact> registered = mgr.getRegisteredArtifacts();

		for (Iterator<IAbstractArtifact> iter = registered.iterator(); iter.hasNext();) {
			IAbstractArtifactInternal model = (IAbstractArtifactInternal) iter
					.next();
			if (model.getClass().getName()
					.equals(mappedImplementationType(artifactType)))
				return model;
		}

		throw new TigerstripeException("Unknown type: " + artifactType);
	}
}
