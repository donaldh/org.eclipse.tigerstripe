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
package org.eclipse.tigerstripe.workbench.ui.eclipse.builder;

import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;

public class ArtifactAuditorFactory {

	private static ArtifactAuditorFactory instance;

	private HashMap<Class, IArtifactAuditor> auditorsMap = new HashMap<Class, IArtifactAuditor>();

	public static ArtifactAuditorFactory getInstance() {
		if (instance == null) {
			instance = new ArtifactAuditorFactory();
		}
		return instance;
	}

	private ArtifactAuditorFactory() {
	}

	public IArtifactAuditor newArtifactAuditor(IProject project,
			IAbstractArtifact artifact) throws TigerstripeException {

		IArtifactAuditor auditor = auditorsMap.get(artifact.getClass());

		if (auditor == null) {
			if (artifact instanceof IManagedEntityArtifact) {
				auditor = new ManagedEntityArtifactAuditor(project,
						(IManagedEntityArtifact) artifact);
				auditorsMap.put(artifact.getClass(), auditor);
			} else if (artifact instanceof IDatatypeArtifact) {
				auditor = new DatatypeArtifactAuditor(project, artifact);
				auditorsMap.put(artifact.getClass(), auditor);
			} else if (artifact instanceof IEnumArtifact) {
				auditor = new EnumerationArtifactAuditor(project, artifact);
				auditorsMap.put(artifact.getClass(), auditor);
			} else if (artifact instanceof IEventArtifact) {
				auditor = new EventArtifactAuditor(project, artifact);
				auditorsMap.put(artifact.getClass(), auditor);
			} else if (artifact instanceof IExceptionArtifact) {
				auditor = new ExceptionArtifactAuditor(project, artifact);
				auditorsMap.put(artifact.getClass(), auditor);
			} else if (artifact instanceof IQueryArtifact) {
				auditor = new QueryArtifactAuditor(project, artifact);
				auditorsMap.put(artifact.getClass(), auditor);
			} else if (artifact instanceof ISessionArtifact) {
				auditor = new SessionFacadeArtifactAuditor(project, artifact);
				auditorsMap.put(artifact.getClass(), auditor);
			} else if (artifact instanceof IUpdateProcedureArtifact) {
				auditor = new UpdateProcedureArtifactAuditor(project, artifact);
				auditorsMap.put(artifact.getClass(), auditor);
			} else if (artifact instanceof IAssociationClassArtifact) {
				auditor = new AssociationClassArtifactAuditor(project, artifact);
				auditorsMap.put(artifact.getClass(), auditor);
			} else if (artifact instanceof IAssociationArtifact) {
				auditor = new AssociationArtifactAuditor(project, artifact);
				auditorsMap.put(artifact.getClass(), auditor);
			} else if (artifact instanceof IDependencyArtifact) {
				auditor = new DependencyArtifactAuditor(project, artifact);
			} else
				throw new TigerstripeException(
						"Internal Error, can't find artifact auditor.");
			TigerstripeRuntime.logDebugMessage("Created Artifact Auditor for: "
					+ artifact.getClass().getCanonicalName());
		} else {
			auditor.setDetails(project, artifact);
		}
		return auditor;
	}
}
