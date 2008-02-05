/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.ModelManager;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IUpdateProcedureArtifact;

/**
 * An Artifact Factory independent of the underlying persistence mechanism
 * 
 * @author erdillon
 * 
 */
public class ArtifactFactory {

	public static ArtifactFactory INSTANCE = new ArtifactFactory();

	private ArtifactFactory() {

	}

	@SuppressWarnings("unchecked")
	protected Collection<Class> getSupportedArtifactClasses() {
		Class[] potentials = { IManagedEntityArtifact.class,
				IDatatypeArtifact.class, IEventArtifact.class,
				IQueryArtifact.class, IExceptionArtifact.class,
				ISessionArtifact.class, IEnumArtifact.class,
				IUpdateProcedureArtifact.class, IAssociationArtifact.class,
				IAssociationClassArtifact.class, IPrimitiveTypeArtifact.class,
				IDependencyArtifact.class };

		ArrayList<Class> result = new ArrayList<Class>();

		CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);

		for (Class pot : potentials) {
			if (prop.getDetailsForType(pot.getName()).isEnabled()) {
				result.add(pot);
			}
		}

		return Collections.unmodifiableCollection(result);
	}

	@SuppressWarnings("unchecked")
	public Collection<Class> getSupportedArtifactQueryTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public Collection<Class> getSupportedArtifactTypes() {
		return getSupportedArtifactClasses();
	}

	@SuppressWarnings("unchecked")
	public AbstractArtifact makeArtifact(Class artifactType,
			ModelManager manager) throws TigerstripeException {
		AbstractArtifact result = internalMakeArtifact(artifactType, manager);
		result.setWorkingCopy(true);

		return result;
	}

	@SuppressWarnings("unchecked")
	public AbstractArtifact internalMakeArtifact(Class artifactType,
			ModelManager manager) throws TigerstripeException {
		if (IManagedEntityArtifact.class == artifactType)
			return new ManagedEntityArtifact(manager.getArtifactManager());
		else if (IDatatypeArtifact.class == artifactType)
			return new DatatypeArtifact(manager.getArtifactManager());
		else if (IExceptionArtifact.class == artifactType)
			return new ExceptionArtifact(manager.getArtifactManager());
		else if (IQueryArtifact.class == artifactType)
			return new QueryArtifact(manager.getArtifactManager());
		else if (ISessionArtifact.class == artifactType)
			return new SessionFacadeArtifact(manager.getArtifactManager());
		else if (IEnumArtifact.class == artifactType)
			return new EnumArtifact(manager.getArtifactManager());
		else if (IEventArtifact.class == artifactType)
			return new EventArtifact(manager.getArtifactManager());
		else if (IUpdateProcedureArtifact.class == artifactType)
			return new UpdateProcedureArtifact(manager.getArtifactManager());
		else if (IAssociationClassArtifact.class == artifactType)
			return new AssociationClassArtifact(manager.getArtifactManager());
		else if (IAssociationArtifact.class == artifactType)
			return new AssociationArtifact(manager.getArtifactManager());
		else if (IPrimitiveTypeArtifact.class == artifactType)
			return new PrimitiveTypeArtifact(manager.getArtifactManager());
		else if (IDependencyArtifact.class == artifactType)
			return new DependencyArtifact(manager.getArtifactManager());
		else
			throw new TigerstripeException("Unknown artifact type: "
					+ artifactType.getCanonicalName());

	}

	public AbstractArtifact makeWorkingCopy(IAbstractArtifact artifact) {
		//FIXME
		return null;
	}
}
