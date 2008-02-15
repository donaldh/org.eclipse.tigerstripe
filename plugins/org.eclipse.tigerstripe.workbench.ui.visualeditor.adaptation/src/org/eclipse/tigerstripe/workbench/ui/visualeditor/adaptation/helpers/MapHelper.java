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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers;

import java.util.List;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;

/**
 * Provides a set of convenience methods on a Map
 * 
 * @author Eric Dillon
 * 
 */
public class MapHelper {

	private Map map;

	public MapHelper(Map map) {
		this.map = map;
	}

	public IAbstractArtifact getIArtifactFor(AbstractArtifact eArtifact)
			throws TigerstripeException {
		ITigerstripeModelProject tsProject = map
				.getCorrespondingITigerstripeProject();
		IArtifactManagerSession session = tsProject.getArtifactManagerSession();
		return session.getArtifactByFullyQualifiedName(eArtifact
				.getFullyQualifiedName());
	}

	public Association findAssociationFor(String name, AbstractArtifact aEnd,
			AbstractArtifact zEnd) {
		List<Association> associations = map.getAssociations();
		for (Association association : associations) {
			if (association.getName().equals(name)
					&& association.getAEnd() == aEnd
					&& association.getZEnd() == zEnd)
				return association;
		}
		return null;
	}

	public QualifiedNamedElement findElementFor(String fqn) {
		QualifiedNamedElement element = null;

		List<Association> assocs = map.getAssociations();
		for (Association a : assocs) {
			if (a.getFullyQualifiedName() != null
					&& a.getFullyQualifiedName().equals(fqn)) {
				element = a;
				return element;
			}
		}

		List<Dependency> deps = map.getDependencies();
		for (Dependency dep : deps) {
			if (dep.getFullyQualifiedName() != null
					&& dep.getFullyQualifiedName().equals(fqn))
				return dep;
		}

		List<AbstractArtifact> arts = map.getArtifacts();
		for (AbstractArtifact art : arts) {
			if (art.getFullyQualifiedName() != null
					&& art.getFullyQualifiedName().equals(fqn))
				return art;
		}

		return element;
	}

	public AbstractArtifact findAbstractArtifactFor(String fqn) {
		AbstractArtifact eArtifact = null;

		List<AbstractArtifact> artifacts = map.getArtifacts();
		for (AbstractArtifact artifact : artifacts) {
			if (artifact.getFullyQualifiedName() != null
					&& artifact.getFullyQualifiedName().equals(fqn)) {
				eArtifact = artifact;
				break;
			}
		}

		return eArtifact;
	}

	public AbstractArtifact findAbstractArtifactFor(IAbstractArtifact iArtifact) {

		if (iArtifact != null)
			return findAbstractArtifactFor(iArtifact.getFullyQualifiedName());

		return null;
	}
}
