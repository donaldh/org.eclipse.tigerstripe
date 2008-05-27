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
package org.eclipse.tigerstripe.workbench.internal.builder;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjEntitySpecifics;

public class ManagedEntityArtifactAuditor extends AbstractArtifactAuditor
		implements IArtifactAuditor {

	public ManagedEntityArtifactAuditor(IProject project,
			IManagedEntityArtifact artifact) {
		super(project, artifact);
	}

	@Override
	public void run(IProgressMonitor monitor) {
		super.run(monitor);

		IManagedEntityArtifact artifact = (IManagedEntityArtifact) getArtifact();
		IOssjEntitySpecifics specifics = (IOssjEntitySpecifics) artifact
				.getIStandardSpecifics();

		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
		.getWorkbenchProfileSession().getActiveProfile().getProperty(
				IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
		

		if (prop
				.getPropertyValue(IOssjLegacySettigsProperty.DISPLAY_OSSJSPECIFICS)) {
			// Check on the primary key - ONLY if using OSS/J Specifics
			String fqn = specifics.getPrimaryKey();
			if (fqn == null || fqn.length() == 0) {
				TigerstripeProjectAuditor.reportError(
						"No primary key type defined.", getIResource(), 222);
			} else {
				IJavaProject jProject = getJProject();
				if (jProject != null) {
					try {

						// ED java.lang.String hidding
						if ("String".equals(fqn)) {
							fqn = "java.lang.String";
						}

						IType type = jProject.findType(fqn);
						if (type == null) {
							TigerstripeProjectAuditor.reportError(
									"Primary key type is unknown", getIResource(),
									222);
						}
					} catch (JavaModelException e) {
						BasePlugin.log(e);
					}
				}
			}
		}
	}

}
