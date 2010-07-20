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

import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjQuerySpecifics;

public class CompareQuery {

	public static ArrayList<Difference> compareQuerySpecifics(
			IQueryArtifact aArtifact, IQueryArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		// Query Specifics
		IOssjQuerySpecifics aSpecs = (IOssjQuerySpecifics) aArtifact
				.getIStandardSpecifics();
		IOssjQuerySpecifics bSpecs = (IOssjQuerySpecifics) bArtifact
				.getIStandardSpecifics();
		IType aType = aSpecs.getReturnedEntityIType();
		String aName = "";
		if (aType != null)
			aName = aType.getFullyQualifiedName();
		
		IType bType = bSpecs.getReturnedEntityIType();
		String bName = "";
		if (bType != null)
			bName = bType.getFullyQualifiedName();
		
		
		if (!aName.equals(bName)) {
			differences.add(new Difference(
					// "","Artifact QuerySpecifics",
					// aArtifact.getFullyQualifiedName(),
					// "ReturnedEntityType",aSpecs.getReturnedEntityIType().getFullyQualifiedName(),bSpecs.getReturnedEntityIType().getFullyQualifiedName()));
					"value", aArtifact.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(), "Query:Specifics",
					"ReturnedEntityType", aName, bName));
		}
		return differences;
	}

}
