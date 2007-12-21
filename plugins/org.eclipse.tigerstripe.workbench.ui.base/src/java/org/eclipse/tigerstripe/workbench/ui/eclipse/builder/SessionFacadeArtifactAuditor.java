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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IOssjMethod;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.ISessionArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.ISessionArtifact.IEmittedEvent;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.ISessionArtifact.IManagedEntityDetails;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.ISessionArtifact.INamedQuery;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.IextMethod.OssjEntityMethodFlavor;
import org.eclipse.tigerstripe.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.core.model.ManagedEntityDetails;
import org.eclipse.tigerstripe.core.model.ossj.specifics.EntityMethodFlavorDetails;
import org.eclipse.tigerstripe.core.model.ossj.specifics.EntityOveride;
import org.eclipse.tigerstripe.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;

public class SessionFacadeArtifactAuditor extends AbstractArtifactAuditor
		implements IArtifactAuditor {

	public SessionFacadeArtifactAuditor(IProject project,
			IAbstractArtifact artifact) {
		super(project, artifact);
	}

	@Override
	public void run(IProgressMonitor monitor) {
		super.run(monitor);

		checkManagedEntities(monitor);
		checkNamedQueries(monitor);
		checkEmittedEvents(monitor);
	}

	/**
	 * Checks that all managed entities can be resolved in the workspace
	 * 
	 * @param monitor
	 */
	private void checkManagedEntities(IProgressMonitor monitor) {
		ISessionArtifact artifact = (ISessionArtifact) getArtifact();

		try {
			OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) API
					.getIWorkbenchProfileSession().getActiveProfile()
					.getProperty(IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);

			IArtifactManagerSession session = artifact.getIProject()
					.getArtifactManagerSession();
			IManagedEntityDetails[] details = artifact
					.getIManagedEntityDetails();
			if (details.length == 0
					&& prop
							.getPropertyValue(IOssjLegacySettigsProperty.USEMANAGEDENTITIES_ONSESSION)) {
				TigerstripeProjectAuditor.reportWarning("Session Facade '"
						+ artifact.getName()
						+ "' has no declared managed entity.", getIResource(),
						222);
			}

			for (int i = 0; i < details.length; i++) {
				IAbstractArtifact mArt = session
						.getArtifactByFullyQualifiedName(details[i]
								.getFullyQualifiedName());
				if (mArt == null) {
					TigerstripeProjectAuditor.reportError("Entity '"
							+ details[i].getFullyQualifiedName()
							+ "' referenced in '" + artifact.getName()
							+ "' cannot be resolved.", getIResource(), 222);
				} else {
					EntityOveride overide = ((ManagedEntityDetails) details[i])
							.getOveride();
					// Need to check here for the Exceptions as they may be
					// coming from
					// an overide
					IOssjMethod[] methods = overide.getMethods();
					for (IOssjMethod method : methods) {
						for (OssjEntityMethodFlavor flavor : method
								.getSupportedFlavors()) {
							EntityMethodFlavorDetails oDetails = method
									.getFlavorDetails(flavor);
							for (String fqn : oDetails.getExceptions()) {
								IAbstractArtifact mAExc = session
										.getArtifactByFullyQualifiedName(fqn);
								if (mAExc == null) {
									TigerstripeProjectAuditor
											.reportError(
													"Exception '"
															+ fqn
															+ "' defined in override of '"
															+ details[i]
																	.getName()
															+ ":"
															+ method.getName()
															+ "("
															+ flavor.name()
															+ ")' cannot be resolved.",
													getIResource(), 222);
								}
							}
						}
					}
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private void checkNamedQueries(IProgressMonitor monitor) {
		ISessionArtifact artifact = (ISessionArtifact) getArtifact();

		try {
			OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) API
					.getIWorkbenchProfileSession().getActiveProfile()
					.getProperty(IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
			IArtifactManagerSession session = artifact.getIProject()
					.getArtifactManagerSession();
			INamedQuery[] queries = artifact.getINamedQueries();

			if (queries.length == 0
					&& prop
							.getPropertyValue(IOssjLegacySettigsProperty.USENAMEDQUERIES_ONSESSION)) {
				TigerstripeProjectAuditor.reportWarning("Session Facade '"
						+ artifact.getName() + "' has no declared Named Query",
						getIResource(), 222);
			}

			for (int i = 0; i < queries.length; i++) {
				IAbstractArtifact mArt = session
						.getArtifactByFullyQualifiedName(queries[i]
								.getFullyQualifiedName());
				if (mArt == null) {
					TigerstripeProjectAuditor.reportError("Query '"
							+ queries[i].getFullyQualifiedName()
							+ "' referenced in '" + artifact.getName()
							+ "' cannot be resolved.", getIResource(), 222);
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private void checkEmittedEvents(IProgressMonitor monitor) {
		ISessionArtifact artifact = (ISessionArtifact) getArtifact();

		try {
			OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) API
					.getIWorkbenchProfileSession().getActiveProfile()
					.getProperty(IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
			IArtifactManagerSession session = artifact.getIProject()
					.getArtifactManagerSession();
			IEmittedEvent[] eevents = artifact.getIEmittedEvents();

			if (eevents.length == 0
					&& prop
							.getPropertyValue(IOssjLegacySettigsProperty.USEEMITTEDNOTIFICATIONS_ONSESSION)) {
				TigerstripeProjectAuditor.reportWarning("Session Facade '"
						+ artifact.getName()
						+ "' has no declared Emitted Notification",
						getIResource(), 222);
			}

			for (int i = 0; i < eevents.length; i++) {
				IAbstractArtifact mArt = session
						.getArtifactByFullyQualifiedName(eevents[i]
								.getFullyQualifiedName());
				if (mArt == null) {
					TigerstripeProjectAuditor.reportError("Notification '"
							+ eevents[i].getFullyQualifiedName()
							+ "' referenced in '" + artifact.getName()
							+ "' cannot be resolved.", getIResource(), 222);
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}
}
