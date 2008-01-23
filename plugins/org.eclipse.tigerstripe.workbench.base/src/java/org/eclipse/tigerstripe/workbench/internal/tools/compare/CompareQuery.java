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

import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj.IOssjQuerySpecifics;
import org.eclipse.tigerstripe.workbench.model.artifacts.IQueryArtifact;

public class CompareQuery {

	public static ArrayList<Difference> compareQuerySpecifics(
			IQueryArtifact aArtifact, IQueryArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		// Query Specifics
		IOssjQuerySpecifics aSpecs = (IOssjQuerySpecifics) aArtifact
				.getIStandardSpecifics();
		IOssjQuerySpecifics bSpecs = (IOssjQuerySpecifics) bArtifact
				.getIStandardSpecifics();
		if (!aSpecs.getReturnedEntityIType().getFullyQualifiedName().equals(
				bSpecs.getReturnedEntityIType().getFullyQualifiedName())) {
			differences.add(new Difference(
					// "","Artifact QuerySpecifics",
					// aArtifact.getFullyQualifiedName(),
					// "ReturnedEntityType",aSpecs.getReturnedEntityIType().getFullyQualifiedName(),bSpecs.getReturnedEntityIType().getFullyQualifiedName()));
					"value", aArtifact.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(), "Query:Specifics",
					"ReturnedEntityType", aSpecs.getReturnedEntityIType()
							.getFullyQualifiedName(), bSpecs
							.getReturnedEntityIType().getFullyQualifiedName()));
		}
		return differences;
	}

}
