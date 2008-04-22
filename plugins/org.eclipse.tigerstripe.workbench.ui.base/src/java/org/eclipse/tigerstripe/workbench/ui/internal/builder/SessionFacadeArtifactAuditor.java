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
package org.eclipse.tigerstripe.workbench.ui.internal.builder;

import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.metamodel.impl.IEventArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IExceptionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IManagedEntityArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IQueryArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.EntityMethodFlavorDetails;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.EntityOveride;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.OssjEntityMethodFlavor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IEmittedEvent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.INamedQuery;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjMethod;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

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
			OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
					.getWorkbenchProfileSession().getActiveProfile()
					.getProperty(IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);

			IArtifactManagerSession session = artifact.getTigerstripeProject()
					.getArtifactManagerSession();
			Collection<IManagedEntityDetails> details = artifact
					.getManagedEntityDetails();
			if (details.size() == 0
					&& prop
							.getPropertyValue(IOssjLegacySettigsProperty.USEMANAGEDENTITIES_ONSESSION)) {
				TigerstripeProjectAuditor.reportWarning(ArtifactMetadataFactory.INSTANCE.getMetadata(
						ISessionArtifactImpl.class.getName()).getLabel() + " '"
						+ artifact.getName()
						+ "' has no declared "
						+ ArtifactMetadataFactory.INSTANCE.getMetadata(
								IManagedEntityArtifactImpl.class.getName())
								.getLabel() + ".", getIResource(), 222);
			}

			for (IManagedEntityDetails detail : details) {

				IAbstractArtifact mArt = session
						.getArtifactByFullyQualifiedName(detail
								.getFullyQualifiedName());
				if (mArt == null) {
					TigerstripeProjectAuditor.reportError(
							ArtifactMetadataFactory.INSTANCE.getMetadata(
									IManagedEntityArtifactImpl.class.getName())
									.getLabel()
									+ " '"
									+ detail.getFullyQualifiedName()
									+ "' referenced in '"
									+ artifact.getName()
									+ "' cannot be resolved.", getIResource(),
							222);
				} else {
					EntityOveride overide = ((ManagedEntityDetails) detail)
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
													ArtifactMetadataFactory.INSTANCE
															.getMetadata(
																	IExceptionArtifactImpl.class
																			.getName())
															.getLabel()
															+ "'"
															+ fqn
															+ "' defined in override of '"
															+ detail.getName()
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
			OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
					.getWorkbenchProfileSession().getActiveProfile()
					.getProperty(IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
			IArtifactManagerSession session = artifact.getTigerstripeProject()
					.getArtifactManagerSession();
			Collection<INamedQuery> queries = artifact.getNamedQueries();

			if (queries.size() == 0
					&& prop
							.getPropertyValue(IOssjLegacySettigsProperty.USENAMEDQUERIES_ONSESSION)) {
				TigerstripeProjectAuditor.reportWarning(ArtifactMetadataFactory.INSTANCE.getMetadata(
						ISessionArtifactImpl.class.getName()).getLabel() + " '"
						+ artifact.getName() + "' has no declared " + ArtifactMetadataFactory.INSTANCE.getMetadata(
								IQueryArtifactImpl.class.getName()).getLabel(),
						getIResource(), 222);
			}

			for (INamedQuery query : queries) {
				IAbstractArtifact mArt = session
						.getArtifactByFullyQualifiedName(query
								.getFullyQualifiedName());
				if (mArt == null) {
					TigerstripeProjectAuditor.reportError(ArtifactMetadataFactory.INSTANCE.getMetadata(
							IQueryArtifactImpl.class.getName()).getLabel() + " '"
							+ query.getFullyQualifiedName()
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
			OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
					.getWorkbenchProfileSession().getActiveProfile()
					.getProperty(IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
			IArtifactManagerSession session = artifact.getTigerstripeProject()
					.getArtifactManagerSession();
			Collection<IEmittedEvent> eevents = artifact.getEmittedEvents();

			if (eevents.size() == 0
					&& prop
							.getPropertyValue(IOssjLegacySettigsProperty.USEEMITTEDNOTIFICATIONS_ONSESSION)) {
				TigerstripeProjectAuditor.reportWarning(ArtifactMetadataFactory.INSTANCE.getMetadata(
						ISessionArtifactImpl.class.getName()).getLabel() + " '"
						+ artifact.getName()
						+ "' has no declared Emitted " + ArtifactMetadataFactory.INSTANCE.getMetadata(
								IEventArtifactImpl.class.getName()).getLabel(),
						getIResource(), 222);
			}

			for (IEmittedEvent eevent : eevents) {
				IAbstractArtifact mArt = session
						.getArtifactByFullyQualifiedName(eevent
								.getFullyQualifiedName());
				if (mArt == null) {
					TigerstripeProjectAuditor.reportError(ArtifactMetadataFactory.INSTANCE.getMetadata(
							IEventArtifactImpl.class.getName()).getLabel() + " '"
							+ eevent.getFullyQualifiedName()
							+ "' referenced in '" + artifact.getName()
							+ "' cannot be resolved.", getIResource(), 222);
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}
}
