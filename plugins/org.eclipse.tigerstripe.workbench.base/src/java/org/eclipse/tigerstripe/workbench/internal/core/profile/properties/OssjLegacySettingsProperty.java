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
package org.eclipse.tigerstripe.workbench.internal.core.profile.properties;

import org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.internal.api.profile.IWorkbenchProfileProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;

/**
 * Contains all details of the OSS/J Legacy settings for a workbench profile
 * 
 * 
 * @author Eric Dillon
 * @since 1.2
 * @see IWorkbenchPropertyLabels#OSSJ_LEGACY_SETTINGS
 */
public class OssjLegacySettingsProperty extends MultiPropertiesProfileProperty
		implements IWorkbenchProfileProperty, IOssjLegacySettigsProperty {

	private final static String OSSJLEGACYSETTINGS = "ossjLegacySettings";

	// all props propKey, prop Label displayed on GUI, default Legacy OSS/J
	// value, SectionTitle
	private static String[][] properties = {
			{ USEATTRIBUTES_ASREFERENCE,
					"Artifact Attributes can be used as references", "true",
					"References" },
			{ DISPLAY_OSSJSPECIFICS, "Display OSS/J Specifics for Artifacts",
					"true", "" },
			{
					USEREFBY_MODIFIERS,
					"References can be by \"Key\", \"Value\" or by \"KeyResult\"",
					"true", "" },
			{ ENABLE_INSTANCEMETHOD, "Enable OSS/J Instance Methods", "true",
					"" },
			{ ENABLE_ISOPTIONAL, "Enable OSS/J 'isOptional' modifier", "true",
					"" },
			{
					USEMANAGEDENTITIES_ONSESSION,
					"Managed Entities can be explicitly referenced on a "
							+ ArtifactMetadataFactory.INSTANCE.getMetadata(
									ISessionArtifactImpl.class.getName())
									.getLabel(),
					"true",
					ArtifactMetadataFactory.INSTANCE.getMetadata(
							ISessionArtifactImpl.class.getName()).getLabel()
							+ " Properties" },
			{
					USEEMITTEDNOTIFICATIONS_ONSESSION,
					"Emitted Notifications can be explicitly referenced on a "
							+ ArtifactMetadataFactory.INSTANCE.getMetadata(
									ISessionArtifactImpl.class.getName())
									.getLabel(), "true", "" },
			{
					USEEXPOSEDPROCEDURES_ONSESSION,
					"Update Procedures can be explicitly referenced on a "
							+ ArtifactMetadataFactory.INSTANCE.getMetadata(
									ISessionArtifactImpl.class.getName())
									.getLabel(), "true", "" },
			{
					USENAMEDQUERIES_ONSESSION,
					"Named Queries can be explicitly referenced on a "
							+ ArtifactMetadataFactory.INSTANCE.getMetadata(
									ISessionArtifactImpl.class.getName())
									.getLabel(), "true", "" },

	/*
	 * { ENABLE_JVT_PLUGIN, "Enable the legacy OSS/J Plugin for JVT Profile
	 * generation", "true", "OSS/J Legacy Plugins" }, { ENABLE_XML_PLUGIN,
	 * "Enable the legacy OSS/J Plugin for JMS/XML Profile generation", "true", "" }, {
	 * ENABLE_WSDL_PLUGIN, "Enable the legacy OSS/J Plugin for WSDL Profile
	 * generation", "true", "" },
	 */};

	@Override
	protected String getPropertyLabel() {
		return OSSJLEGACYSETTINGS;
	}

	public OssjLegacySettingsProperty() {
		super();
	}

	@Override
	public String[][] getProperties() {
		return properties;
	}

}
