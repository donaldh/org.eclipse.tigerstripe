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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh;

import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IDependencyArtifact;
import org.eclipse.tigerstripe.api.external.model.IextType;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;

public class DependencyEndUpdateCommand extends
		AbstractQualifiedNamedElementUpdateCommand {

	public DependencyEndUpdateCommand(Dependency eDependency,
			IDependencyArtifact iDepArtifact) {
		super(eDependency, iDepArtifact);
	}

	@Override
	public void updateQualifiedNamedElement(QualifiedNamedElement element,
			IAbstractArtifact artifact) {
		if (!(element instanceof Dependency)
				|| !(artifact instanceof IDependencyArtifact))
			return;
		Dependency eDependency = (Dependency) element;
		IDependencyArtifact iDepArtifact = (IDependencyArtifact) artifact;
		IextType aEndType = iDepArtifact.getAEndType();
		IextType zEndType = iDepArtifact.getZEndType();

		if (aEndType != null) {
			String aEndTypeFQN = aEndType.getFullyQualifiedName();
			if (!aEndTypeFQN.equals(eDependency.getAEnd()
					.getFullyQualifiedName())) {
				// if we can find the corresponding type on the diagram fine
				// set it up, otherwise we need to remove the assoc
				Map map = (Map) eDependency.eContainer();
				MapHelper helper = new MapHelper(map);
				AbstractArtifact eArt = helper
						.findAbstractArtifactFor(aEndTypeFQN);
				eDependency.setAEnd(eArt);
				// if eArt == null the dependency will be removed downstream
			}
		}

		if (zEndType != null) {
			String zEndTypeFQN = zEndType.getFullyQualifiedName();
			if (!zEndTypeFQN.equals(eDependency.getZEnd()
					.getFullyQualifiedName())) {
				// if we can find the corresponding type on the diagram fine
				// set it up, otherwise we need to remove the assoc
				Map map = (Map) eDependency.eContainer();
				MapHelper helper = new MapHelper(map);
				AbstractArtifact eArt = helper
						.findAbstractArtifactFor(zEndTypeFQN);
				eDependency.setZEnd(eArt);
				// if eArt == null the dependency will be removed downstream
			}
		}

	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
	}

}
