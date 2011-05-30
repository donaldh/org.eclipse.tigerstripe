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

import java.util.Collection;

import org.eclipse.core.runtime.Assert;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.NamedElementPropertiesHelper;

public class ArtifactPropertyChangeHandler {

	private final AbstractArtifact artifact;

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
			} else if (NamedElementPropertiesHelper.ARTIFACT_HIDE_IMPLEMENTS
					.equals(propertyKey)) {
				handleHideImplements(oldValue, newValue);
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	protected void handleHideExtends(String oldValue, String newValue)
			throws TigerstripeException {
		if ("true".equals(newValue)) {
			artifact.setExtends(null);
		} else if (artifact.getExtends() == null) {
			Map map = (Map) artifact.eContainer();
			MapHelper mapHelper = new MapHelper(map);
			IAbstractArtifact iArtifact = mapHelper.getIArtifactFor(artifact);
			IAbstractArtifact extendedIArtifact = iArtifact
					.getExtendedArtifact();
			if (extendedIArtifact != null) {
				AbstractArtifact extendedEArtifact = mapHelper
						.findAbstractArtifactFor(extendedIArtifact);
				if (extendedEArtifact != null) {
					artifact.setExtends(extendedEArtifact);
				}
			}
		}
	}

	protected void handleHideImplements(String oldValue, String newValue)
			throws TigerstripeException {
		artifact.getImplements().clear();
		if ("false".equals(newValue)) {
			Map map = (Map) artifact.eContainer();
			MapHelper mapHelper = new MapHelper(map);
			IAbstractArtifact iArtifact = mapHelper.getIArtifactFor(artifact);
			Collection<IAbstractArtifact> implementedArtifacts = iArtifact
					.getImplementedArtifacts();
			for (IAbstractArtifact implementedArtifact : implementedArtifacts) {
				AbstractArtifact implementedEArtifact = mapHelper
						.findAbstractArtifactFor(implementedArtifact);
				if (implementedEArtifact != null) {
					artifact.getImplements().add(implementedEArtifact);
				}
			}
		}
	}
}
