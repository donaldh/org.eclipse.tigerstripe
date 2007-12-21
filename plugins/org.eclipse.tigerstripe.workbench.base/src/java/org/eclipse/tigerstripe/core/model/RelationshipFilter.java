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
package org.eclipse.tigerstripe.core.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.api.external.model.artifacts.IRelationship;

/**
 * Allows to filter returned relationships when querying the Artifact Manager
 * 
 * @author Eric Dillon
 * 
 */
public abstract class RelationshipFilter {

	/**
	 * 
	 * @param artifact
	 * @return
	 */
	public abstract boolean select(IRelationship relationship);

	public static List<IRelationship> filter(
			List<IRelationship> relationshipList, RelationshipFilter filter) {

		// Little acceleration for performance
		if (filter == null || filter instanceof RelationshipNoFilter)
			return relationshipList;

		ArrayList<IRelationship> result = new ArrayList<IRelationship>();

		for (IRelationship relationship : relationshipList) {
			if (filter.select(relationship)) {
				result.add(relationship);
			}
		}

		return result;
	}
}
