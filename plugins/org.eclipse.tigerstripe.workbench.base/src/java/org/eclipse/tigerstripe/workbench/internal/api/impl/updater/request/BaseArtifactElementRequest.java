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

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.BaseModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * Base class for any request related to an artifact element (i.e where the
 * actual artifact needs to be identified)
 * 
 * @author Eric Dillon
 * 
 */
public abstract class BaseArtifactElementRequest extends BaseModelChangeRequest {

	private String artifactFQN;

	public String getArtifactFQN() {
		return this.artifactFQN;
	}

	public void setArtifactFQN(String artifactFQN) {
		this.artifactFQN = artifactFQN;
	}

	
	
	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		if (!super.canExecute(mgrSession)) {
			return false;
		}
		try {
			IAbstractArtifact art = mgrSession
					.getArtifactByFullyQualifiedName(getArtifactFQN());
			if (art != null){
				ITigerstripeModelProject project = art.getTigerstripeProject();
				if (project != null){
					// We are NOT in a module so can be updated
					return true;
				}
			}
			return false;
		} catch (TigerstripeException t) {
			return false;
		}
	}

	protected ModelChangeDelta makeDelta(int type) {
		ModelChangeDelta delta = new ModelChangeDelta(type);

		try {
			AbstractArtifact comp = (AbstractArtifact) getMgrSession()
					.getArtifactByFullyQualifiedName(getArtifactFQN());
			delta
					.setAffectedModelComponentURI((URI) comp
							.getAdapter(URI.class));
			delta.setSource(this);
			delta.setProject(comp.getProject());

		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return ModelChangeDelta.UNKNOWNDELTA;
		}

		return delta;
	}
}
