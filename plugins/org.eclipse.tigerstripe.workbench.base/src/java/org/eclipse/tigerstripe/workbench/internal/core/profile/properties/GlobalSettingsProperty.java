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
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.internal.api.profile.IWorkbenchProfileProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IGlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;

/**
 * Contains all details of the OSS/J Legacy settings for a workbench profile
 * 
 * 
 * @author Eric Dillon
 * @since 1.2
 * @see IWorkbenchPropertyLabels#OSSJ_LEGACY_SETTINGS
 */
public class GlobalSettingsProperty extends MultiPropertiesProfileProperty
		implements IWorkbenchProfileProperty, IGlobalSettingsProperty {

	private final static String GLOBALSETTINGS = "globalSettings";

	// all props propKey, prop Label displayed on GUI, default Legacy OSS/J
	// value, SectionTitle
	private static String[][] properties = {
			{ IMPLEMENTSRELATIONSHIP, "'Implements' Relationship", "true",
					"Meta-model" },
			{
					ENABLE_SESSIONFACADE_ONINSTDIAG,
					"Enable "
							+ ArtifactMetadataFactory.INSTANCE.getMetadata(
									ISessionArtifactImpl.class.getName())
									.getLabel(), "true", "Instance Diagrams" }, };

	@Override
	protected String getPropertyLabel() {
		return GLOBALSETTINGS;
	}

	public GlobalSettingsProperty() {
		super();
	}

	@Override
	public String[][] getProperties() {
		return properties;
	}

}
