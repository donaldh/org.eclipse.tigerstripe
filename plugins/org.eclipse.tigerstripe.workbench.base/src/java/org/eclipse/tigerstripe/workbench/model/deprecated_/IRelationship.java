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
package org.eclipse.tigerstripe.workbench.model.deprecated_;

import java.util.Collection;

/**
 * An IRelationshipArtifact is an abstraction of an explicit relationship
 * between multiple artifacts
 * 
 * 
 * @author Eric Dillon
 * 
 */
public interface IRelationship extends IModelComponent {

	public Collection<IRelationshipEnd> getRelationshipEnds();

	/**
	 * Returns the first end
	 * 
	 * @return
	 */
	public IRelationshipEnd getRelationshipAEnd();

	/**
	 * Returns the last end
	 * 
	 * @return
	 */
	public IRelationshipEnd getRelationshipZEnd();

	/**
	 * An end in a IRelationshipArtifact
	 * 
	 * @author Eric Dillon
	 * 
	 */
	public interface IRelationshipEnd {

		public String getName();

		/**
		 * The type of the artifact attached to this end.
		 * 
		 * @return
		 */
		public IType getType();

		/**
		 * Returns the containing IRelationshipEnd
		 * 
		 */
		public IRelationship getContainingRelationship();

		/**
		 * Returns the "other" end for the containing relationship if the
		 * containing relationship is a 2-way relationship (ie. contains 2
		 * IRelationshipEnds). Undetermined otherwise.
		 */
		public IRelationshipEnd getOtherEnd();

		/**
		 * Returns a String representing the "name" of the end that matches the
		 * typeName (a FQN) that is passed in as an input argument (or an empty
		 * string if no match is found)
		 */
		public String getNameForType(String typeName);

	}

}
