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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.utils.ClassDiagramUtils;

public class PostCreationEnumArtifactUpdater extends
		PostCreationAbstractArtifactUpdater {

	public PostCreationEnumArtifactUpdater(IAbstractArtifact iArtifact,
			AbstractArtifact eArtifact, Map map,
			ITigerstripeModelProject diagramProject, boolean hideExtends) {
		super(iArtifact, eArtifact, map, diagramProject, hideExtends);
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
			for (ILiteral literal : getIArtifact().getLiterals()) {
				String labelType = literal.getType().getFullyQualifiedName();
				String labelName = literal.getName();
				String labelValue = literal.getValue();
				Literal lit = VisualeditorFactory.eINSTANCE.createLiteral();
				lit.setName(labelName);
				lit.setValue(labelValue);
				lit.setType(Misc.removeJavaLangString(labelType));
				lit.setVisibility(ClassDiagramUtils.toVisibility(literal
						.getVisibility()));
				for (IStereotypeInstance instance : literal
						.getStereotypeInstances()) {
					lit.getStereotypes().add(instance.getName());
				}
				lit.setLiteral(literal);
				enume.getLiterals().add(lit);
			}
		} finally {
			BaseETAdapter.setIgnoreNotify(false);
		}
	}

}
