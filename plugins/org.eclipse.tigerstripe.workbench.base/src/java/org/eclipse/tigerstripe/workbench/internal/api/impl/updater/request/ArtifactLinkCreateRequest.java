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

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactLinkCreateRequest;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IDependencyArtifact;

public class ArtifactLinkCreateRequest extends ArtifactCreateRequest implements
		IArtifactLinkCreateRequest {

	private String aEndType;
	private String aEndName;
	private boolean aEndNavigability = false;
	private String zEndType;
	private String zEndName;
	private boolean zEndNavigability = false;

	public void setAEndType(String aEndType) {
		this.aEndType = aEndType;
	}

	public void setZEndType(String zEndType) {
		this.zEndType = zEndType;
	}

	public void setAEndNavigability(boolean isNavigable) {
		aEndNavigability = isNavigable;
	}

	public void setZEndNavigability(boolean isNavigable) {
		zEndNavigability = isNavigable;
	}

	public void setAEndMultiplicity(String aEndMultiplicity) {
	}

	public void setAEndName(String aEndName) {
		this.aEndName = aEndName;
	}

	public void setZEndMultiplicity(String zEndMultiplicity) {
	}

	public void setZEndName(String zEndName) {
		this.zEndName = zEndName;
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		IAbstractArtifact artifact = mgrSession.makeArtifact(getArtifactType());
		artifact.setFullyQualifiedName(getFullyQualifiedName());

		if (artifact instanceof IAssociationArtifact) {
			IAssociationArtifact assoc = (IAssociationArtifact) artifact;
			IAssociationEnd aEnd = assoc.makeAssociationEnd();
			IType aType = aEnd.makeType();
			aType.setFullyQualifiedName(aEndType);
			aEnd.setType(aType);
			aEnd.setName(aEndName);
			aEnd.setNavigable(aEndNavigability);
			aEnd.setMultiplicity(IModelComponent.EMultiplicity.ONE); // FIXME this should be
			// translated from
			// Diagram
			assoc.setAEnd(aEnd);

			IAssociationEnd zEnd = assoc.makeAssociationEnd();
			IType zType = zEnd.makeType();
			zType.setFullyQualifiedName(zEndType);
			zEnd.setType(zType);
			zEnd.setName(zEndName);
			zEnd.setNavigable(zEndNavigability);
			zEnd.setMultiplicity(IModelComponent.EMultiplicity.ONE); // FIXME this should be
			// translated from
			// Diagram
			assoc.setZEnd(zEnd);
		} else if (artifact instanceof IDependencyArtifact) {
			IDependencyArtifact dep = (IDependencyArtifact) artifact;
			IType aType = dep.makeType();
			aType.setFullyQualifiedName(aEndType);
			dep.setAEndType(aType);
			IType zType = dep.makeType();
			zType.setFullyQualifiedName(zEndType);
			dep.setZEndType(zType);
		} else if (artifact instanceof IAssociationClassArtifact) {
			IAssociationClassArtifact assocClass = (IAssociationClassArtifact) artifact;
			IAssociationEnd aEnd = assocClass.makeAssociationEnd();
			IType aType = aEnd.makeType();
			aType.setFullyQualifiedName(aEndType);
			aEnd.setType(aType);
			aEnd.setName(aEndName);
			aEnd.setNavigable(aEndNavigability);
			aEnd.setMultiplicity(IModelComponent.EMultiplicity.ONE); // FIXME this should be
			// translated from
			// Diagram
			assocClass.setAEnd(aEnd);

			IAssociationEnd zEnd = assocClass.makeAssociationEnd();
			IType zType = zEnd.makeType();
			zType.setFullyQualifiedName(zEndType);
			zEnd.setType(zType);
			zEnd.setName(zEndName);
			zEnd.setMultiplicity(IModelComponent.EMultiplicity.ONE); // FIXME this should be
			// translated from
			// Diagram
			zEnd.setNavigable(zEndNavigability);
			assocClass.setZEnd(zEnd);
		}

		applyDefaults(artifact, mgrSession);
		mgrSession.addArtifact(artifact);
		artifact.doSave(new NullProgressMonitor());
	}

}
