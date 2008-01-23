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
package org.eclipse.tigerstripe.workbench.ui.eclipse.search;

import org.eclipse.search.ui.text.Match;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.ILabel;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;

/**
 * An Artifact match for the Tigerstripe Search. The element is the artifact
 * itself
 * 
 * @author erdillon
 * @since 2.2.4
 */
public class ArtifactMatch extends Match {

	public final static int DECLARATION_MATCH = 0;
	public final static int OCCURENCE_MATCH = 1;
	public final static int REFERENCE_MATCH = 2;

	private int matchType;
	private IAbstractArtifact containingArtifact;

	public ArtifactMatch(IModelComponent component, int offset, int length,
			int matchType) {
		super(component, offset, length);

		containingArtifact = null;
		if (component instanceof IAbstractArtifact) {
			containingArtifact = (IAbstractArtifact) component;
		} else {
			if (component instanceof IField) {
				containingArtifact = (IAbstractArtifact) ((IField) component)
						.getContainingArtifact();
			} else if (component instanceof IMethod) {
				containingArtifact = (IAbstractArtifact) ((IMethod) component)
						.getContainingArtifact();
			} else if (component instanceof ILabel) {
				containingArtifact = (IAbstractArtifact) ((ILabel) component)
						.getContainingArtifact();
			} else if (component instanceof IAssociationEnd) {
				containingArtifact = (IAbstractArtifact) ((IAssociationEnd) component)
						.getContainingArtifact();
			}
		}

		if (containingArtifact == null)
			throw new IllegalArgumentException();

		this.matchType = matchType;
	}

	public int getMatchType() {
		return matchType;
	}

}
