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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.NamedElementPropertiesHelper;

public class HideArtifactExtendsToggleAction extends ArtifactToggleAction {

	@Override
	protected void computeEnabledAndChecked(IAction action) {
		// No need to enable if the artifact has no extends clause
		IAbstractArtifact[] iArtifacts = getCorrespondingIArtifacts();
		boolean allEnabled = iArtifacts.length != 0;
		if (allEnabled)
			super.computeEnabledAndChecked(action);

		for (IAbstractArtifact iArtifact : iArtifacts) {
			if (iArtifact != null) {
				IAbstractArtifact extendedIArt = iArtifact
						.getExtendedArtifact();
				if (extendedIArt == null) {
					// no extends defined, always disabled
					action.setEnabled(false);
					return;
				} else {
					// check that the extended artifact is on the diagram
					AbstractArtifact[] eArtifacts = getCorrespondingEArtifacts();
					for (AbstractArtifact eArtifact : eArtifacts) {
						if (eArtifact != null) {
							Map map = (Map) eArtifact.eContainer();
							MapHelper helper = new MapHelper(map);
							AbstractArtifact extendedEArt = helper
									.findAbstractArtifactFor(extendedIArt);
							if (extendedEArt != null) {
								continue;
							} else {
								action.setEnabled(false);
								return;
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected String getTargetProperty() {
		return NamedElementPropertiesHelper.ARTIFACT_HIDE_EXTENDS;
	}

}
