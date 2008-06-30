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
import java.util.List;

import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactMetadataSession;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.PackageArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.PrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
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

/**
 * A singleton class that holds all the metadata about known artifact types
 * 
 * Each artifact manager delegates the artifact registration to this.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class ArtifactMetadataSessionImpl implements IArtifactMetadataSession {

	private static ArtifactMetadataSessionImpl instance;

	public static ArtifactMetadataSessionImpl getInstance() {
		if (instance == null) {
			instance = new ArtifactMetadataSessionImpl();
			instance.registerTmpDefault();
		}
		return instance;
	}

	private List<IAbstractArtifact> registeredArtifactTypes;

	private String[] registeredArtifactTypesCache;

	private String[] registeredArtifactLabelsCache;

	private ArtifactMetadataSessionImpl() {
		registeredArtifactTypes = new ArrayList<IAbstractArtifact>();
		buildRegisteredArtifactCache();
	}

	public String[] getSupportedArtifactTypeLabels() {
		return registeredArtifactLabelsCache;
	}

	public synchronized void registerArtifactType(
			IAbstractArtifact artifactModel) throws TigerstripeException {
		if (!registeredArtifactTypes.contains(artifactModel)) {
			registeredArtifactTypes.add(artifactModel);
			buildRegisteredArtifactCache();
			return;
		}
		throw new TigerstripeException("Duplicate Artifact Type definition:'"
				+ artifactModel.getLabel() + "'");
	}

	public synchronized void unRegisterArtifactType(
			IAbstractArtifact artifactModel) throws TigerstripeException {
		if (registeredArtifactTypes.contains(artifactModel)) {
			registeredArtifactTypes.remove(artifactModel);
			buildRegisteredArtifactCache();
			return;
		}
		throw new TigerstripeException("Unknown Artifact Type definition:'"
				+ artifactModel.getLabel() + "'");
	}

	protected void buildRegisteredArtifactCache() {
		List<String> result = new ArrayList<String>();
		List<String> resultType = new ArrayList<String>();

		for (IAbstractArtifact artifact : registeredArtifactTypes) {
			result.add(artifact.getLabel());
			resultType.add(artifact.getArtifactType());
		}
		registeredArtifactLabelsCache = result
				.toArray(new String[result.size()]);
		registeredArtifactTypesCache = resultType.toArray(new String[resultType
				.size()]);
	}

	public String[] getSupportedArtifactTypes() {
		if (registeredArtifactTypesCache == null) {
			buildRegisteredArtifactCache();
		}
		return registeredArtifactTypesCache;
	}

	/**
	 * Temporary method that artificially registers all known artifact types
	 * This should eventually be configuration-driven rather than hard coded.
	 * Also, it is currently duplicated in some shape or form in the
	 * ArtifactManager and ArtifactManagerSessionImpl... to be fixed.
	 * 
	 */
	private void registerTmpDefault() {

		CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);
		try {
			if (prop.getDetailsForType(IManagedEntityArtifact.class.getName())
					.isEnabled()) {
				registerArtifactType(ManagedEntityArtifact.MODEL);
			}

			if (prop.getDetailsForType(IDatatypeArtifact.class.getName())
					.isEnabled()) {
				registerArtifactType(DatatypeArtifact.MODEL);
			}

			if (prop.getDetailsForType(ISessionArtifact.class.getName())
					.isEnabled()) {
				registerArtifactType(SessionFacadeArtifact.MODEL);
			}

			if (prop.getDetailsForType(IEventArtifact.class.getName())
					.isEnabled()) {
				registerArtifactType(EventArtifact.MODEL);
			}

			if (prop.getDetailsForType(IEnumArtifact.class.getName())
					.isEnabled()) {
				registerArtifactType(EnumArtifact.MODEL);
			}

			if (prop.getDetailsForType(IQueryArtifact.class.getName())
					.isEnabled()) {
				registerArtifactType(QueryArtifact.MODEL);
			}

			if (prop.getDetailsForType(IExceptionArtifact.class.getName())
					.isEnabled()) {
				registerArtifactType(ExceptionArtifact.MODEL);
			}

			if (prop
					.getDetailsForType(IUpdateProcedureArtifact.class.getName())
					.isEnabled()) {
				registerArtifactType(UpdateProcedureArtifact.MODEL);
			}

			if (prop.getDetailsForType(IDependencyArtifact.class.getName())
					.isEnabled()) {
				registerArtifactType(DependencyArtifact.MODEL);
			}

			if (prop.getDetailsForType(IAssociationArtifact.class.getName())
					.isEnabled()) {
				registerArtifactType(AssociationArtifact.MODEL);
			}

			if (prop.getDetailsForType(
					IAssociationClassArtifact.class.getName()).isEnabled()) {
				registerArtifactType(AssociationClassArtifact.MODEL);
			}

			if (prop.getDetailsForType(
					IPackageArtifact.class.getName()).isEnabled()) {
				registerArtifactType(PackageArtifact.MODEL);
			}
			
			if (prop.getDetailsForType(IPrimitiveTypeArtifact.class.getName())
					.isEnabled()) {
				registerArtifactType(PrimitiveTypeArtifact.MODEL);
			}
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
	}
}
