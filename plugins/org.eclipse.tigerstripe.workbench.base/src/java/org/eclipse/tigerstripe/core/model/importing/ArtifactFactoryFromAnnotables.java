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
package org.eclipse.tigerstripe.core.model.importing;

import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IDatatypeArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEnumArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IManagedEntityArtifact;
import org.eclipse.tigerstripe.api.external.TigerstripeException;

public class ArtifactFactoryFromAnnotables {

	private static ArtifactFactoryFromAnnotables instance;

	private ArtifactFactoryFromAnnotables() {

	}

	public static ArtifactFactoryFromAnnotables getInstance() {
		if (instance == null) {
			instance = new ArtifactFactoryFromAnnotables();
		}
		return instance;
	}

	public IAbstractArtifact makeIArtifact(AnnotableElement element,
			IArtifactManagerSession session) throws TigerstripeException {

		IAbstractArtifact result = null;

		if (element == null)
			throw new TigerstripeException("No annotable to map");

		String annotationType = element.getAnnotationType();
		if (AnnotableElement.AS_ENTITY.equals(annotationType)) {
			result = session.makeArtifact(IManagedEntityArtifact.class
					.getCanonicalName());
		} else if (AnnotableElement.AS_DATATYPE.equals(annotationType)) {
			result = session.makeArtifact(IDatatypeArtifact.class
					.getCanonicalName());
		} else if (AnnotableElement.AS_ENUMERATION.equals(annotationType)) {
			result = session.makeArtifact(IEnumArtifact.class
					.getCanonicalName());
		}

		if (result == null)
			throw new TigerstripeException("Couldn't map "
					+ element.getFullyQualifiedName());

		applyAnnotations(element, result);
		return result;
	}

	protected void applyAnnotations(AnnotableElement elem,
			IAbstractArtifact artifact) throws TigerstripeException {

		applyAttributeAnnotations(elem, artifact);
		applyOperationAnnotations(elem, artifact);
		applyConstantAnnotations(elem, artifact);
	}

	protected void applyAttributeAnnotations(AnnotableElement elem,
			IAbstractArtifact artifact) {

		for (AnnotableElementAttribute attr : elem
				.getAnnotableElementAttributes()) {

		}
	}

	protected void applyOperationAnnotations(AnnotableElement elem,
			IAbstractArtifact artifact) {

	}

	protected void applyConstantAnnotations(AnnotableElement elem,
			IAbstractArtifact artifact) {

	}
}
