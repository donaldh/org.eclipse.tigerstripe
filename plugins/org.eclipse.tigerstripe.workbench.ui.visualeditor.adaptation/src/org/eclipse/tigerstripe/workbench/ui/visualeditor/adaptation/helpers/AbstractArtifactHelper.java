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

import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;

public class AbstractArtifactHelper {

	private AbstractArtifact artifact;

	public AbstractArtifactHelper(AbstractArtifact artifact) {
		this.artifact = artifact;
	}

	public Reference findReferenceByName(String name) {
		List<Reference> refs = artifact.getReferences();
		for (Reference ref : refs) {
			if (ref.getName() != null && ref.getName().equals(name))
				return ref;
		}

		return null;
	}
}
