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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.commands;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.ILabel;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;

public class PostCreationEnumArtifactUpdater extends
		PostCreationAbstractArtifactUpdater {

	public PostCreationEnumArtifactUpdater(IAbstractArtifact iArtifact,
			AbstractArtifact eArtifact, Map map,
			ITigerstripeProject diagramProject) {
		super(iArtifact, eArtifact, map, diagramProject);
	}

	@Override
	public void updateConnections() throws TigerstripeException {
		super.updateConnections();

		Enumeration enume = (Enumeration) getEArtifact();

		try {
			BaseETAdapter.setIgnoreNotify(true);
			IEnumArtifact iEnum = (IEnumArtifact) getIArtifact();
			enume.setBaseType(iEnum.getBaseTypeStr());

			// Create Literals
			for (ILabel label : getIArtifact().getLabels()) {
				String labelType = label.getType().getFullyQualifiedName();
				String labelName = label.getName();
				String labelValue = label.getValue();
				Literal lit = VisualeditorFactory.eINSTANCE.createLiteral();
				lit.setName(labelName);
				lit.setValue(labelValue);
				lit.setType(Misc.removeJavaLangString(labelType));
				for (IStereotypeInstance instance : label
						.getStereotypeInstances()) {
					lit.getStereotypes().add(instance.getName());
				}
				enume.getLiterals().add(lit);
			}
		} finally {
			BaseETAdapter.setIgnoreNotify(false);
		}
	}

}
