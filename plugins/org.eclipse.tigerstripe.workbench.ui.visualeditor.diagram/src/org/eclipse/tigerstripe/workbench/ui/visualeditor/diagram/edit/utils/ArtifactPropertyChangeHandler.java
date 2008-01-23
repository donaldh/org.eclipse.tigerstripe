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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.utils;

import org.eclipse.core.runtime.Assert;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.NamedElementPropertiesHelper;

public class ArtifactPropertyChangeHandler {

	private AbstractArtifact artifact;

	public ArtifactPropertyChangeHandler(AbstractArtifact artifact) {
		Assert.isNotNull(artifact);
		this.artifact = artifact;
	}

	public void handleArtifactPropertyChange(String propertyKey,
			String oldValue, String newValue) {
		try {
			if (NamedElementPropertiesHelper.ARTIFACT_HIDE_EXTENDS
					.equals(propertyKey)) {
				handleHideExtends(oldValue, newValue);
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	protected void handleHideExtends(String oldValue, String newValue)
			throws TigerstripeException {
		if ("true".equals(newValue)) {
			artifact.setExtends(null);
		} else {
			Map map = (Map) artifact.eContainer();
			MapHelper mapHelper = new MapHelper(map);
			IAbstractArtifact iArtifact = mapHelper.getIArtifactFor(artifact);
			IAbstractArtifact extendedIArtifact = iArtifact
					.getExtendedIArtifact();
			if (extendedIArtifact != null) {
				AbstractArtifact extendedEArtifact = mapHelper
						.findAbstractArtifactFor(extendedIArtifact);
				if (extendedEArtifact != null) {
					artifact.setExtends(extendedEArtifact);
				}
			}
		}
	}
}
