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
package org.eclipse.tigerstripe.api.impl.updater.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationClassArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IManagedEntityArtifact;
import org.eclipse.tigerstripe.api.artifacts.updater.request.IArtifactRemoveFeatureRequest;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

public class ArtifactRemoveFeatureRequest extends BaseArtifactElementRequest
		implements IArtifactRemoveFeatureRequest {

	private String featureId;

	private String featureValue;

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		IArtifact art = mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());

		if (IMPLEMENTS_FEATURE.equals(featureId))
			return art instanceof IManagedEntityArtifact
					|| art instanceof IAssociationClassArtifact;
		return false;
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		IArtifact art = mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());
		if (art instanceof IManagedEntityArtifact
				|| art instanceof IAssociationClassArtifact) {
			IAbstractArtifact me = (IAbstractArtifact) art;
			IAbstractArtifact[] arts = me.getImplementedArtifacts();
			List<IAbstractArtifact> list = new ArrayList<IAbstractArtifact>();
			list.addAll(Arrays.asList(arts));
			IAbstractArtifact target = mgrSession
					.getArtifactByFullyQualifiedName(featureValue);
			if (target != null) {
				list.remove(target);
				me.setImplementedArtifacts(list
						.toArray(new IAbstractArtifact[list.size()]));
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
