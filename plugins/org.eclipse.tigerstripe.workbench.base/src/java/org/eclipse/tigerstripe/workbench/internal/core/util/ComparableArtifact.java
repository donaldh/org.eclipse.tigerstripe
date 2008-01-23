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
package org.eclipse.tigerstripe.workbench.internal.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;

/**
 * Because AbstractArtifact don't have an equals(..) method this allows to
 * conveniently wrap AbstractArtifacts so they can be compared based on their
 * FQN
 * 
 * @author Eric Dillon
 * @since 2.2
 */
public class ComparableArtifact {

	private IAbstractArtifact artifact;

	public ComparableArtifact(IAbstractArtifact artifact) {
		this.artifact = artifact;
	}

	public IAbstractArtifact getArtifact() {
		return this.artifact;
	}

	@Override
	public boolean equals(Object obj) {
		if (artifact == null)
			return false;

		IAbstractArtifact other = null;
		if (obj instanceof ComparableArtifact) {
			other = ((ComparableArtifact) obj).getArtifact();
		} else if (obj instanceof IAbstractArtifact) {
			other = (IAbstractArtifact) obj;
		}

		if (other != null)
			return other.getFullyQualifiedName().equals(
					getArtifact().getFullyQualifiedName());

		return false;
	}

	public final static Collection<ComparableArtifact> asComparableArtifacts(
			Collection<IAbstractArtifact> artifacts) {
		if (artifacts == null)
			return new ArrayList<ComparableArtifact>();
		ComparableArtifact[] result = asComparableArtifacts(artifacts
				.toArray(new IAbstractArtifact[artifacts.size()]));
		return Arrays.asList(result);
	}

	public final static ComparableArtifact[] asComparableArtifacts(
			IAbstractArtifact[] artifacts) {
		if (artifacts == null)
			return new ComparableArtifact[0];

		ComparableArtifact[] result = new ComparableArtifact[artifacts.length];
		for (int i = 0; i < artifacts.length; i++) {
			result[i] = new ComparableArtifact(artifacts[i]);
		}

		return result;
	}
}
