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
package org.eclipse.tigerstripe.workbench.internal.tools.compare;

import java.util.ArrayList;

import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj.IOssjEnumSpecifics;
import org.eclipse.tigerstripe.workbench.model.artifacts.IEnumArtifact;

public class CompareEnumeration {

	public static ArrayList<Difference> compareEnumSpecifics(
			IEnumArtifact aArtifact, IEnumArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();

		IOssjEnumSpecifics aSpecs = (IOssjEnumSpecifics) aArtifact
				.getIStandardSpecifics();
		IOssjEnumSpecifics bSpecs = (IOssjEnumSpecifics) bArtifact
				.getIStandardSpecifics();
		if (!aSpecs.getBaseIType().getFullyQualifiedName().equals(
				bSpecs.getBaseIType().getFullyQualifiedName())) {
			differences.add(new Difference(
					// "","Artifact EnumSpecifics",
					// aArtifact.getFullyQualifiedName(),
					// "BaseType",aSpecs.getBaseIType().getFullyQualifiedName(),bSpecs.getBaseIType().getFullyQualifiedName()));
					"value", aArtifact.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(), "Enumeration:Specifics",
					"BaseType", aSpecs.getBaseIType().getFullyQualifiedName(),
					bSpecs.getBaseIType().getFullyQualifiedName()));
		}
		if (aSpecs.getExtensible() != bSpecs.getExtensible()) {
			differences.add(new Difference(
					// "","Artifact EnumSpecifics",
					// aArtifact.getFullyQualifiedName(),
					// "Extensible",aSpecs.getExtensible(),bSpecs.getExtensible()));
					"value", aArtifact.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(), "Enumeration:Specifics",
					"Extensible",
					((Boolean) aSpecs.getExtensible()).toString(),
					((Boolean) bSpecs.getExtensible()).toString()));
		}
		return differences;
	}

}
