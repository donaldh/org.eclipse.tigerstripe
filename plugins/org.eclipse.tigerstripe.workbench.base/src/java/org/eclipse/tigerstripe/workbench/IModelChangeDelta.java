/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench;

import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public interface IModelChangeDelta {

	public final int SET = 0;
	public final int ADD = 1;
	public final int REMOVE = 2;
	public final int COPY = 3;
	public final int MOVE = 4;
	public final int UNKNOWN = -1;

	public final String ATTRIBUTE = "attribute";
	public final String METHOD = "method";
	public final String LITERAL = "literal";
	public final String RELATIONSHIP_END = "relationship_end";

	/**
	 * Returns the type of the model change
	 * 
	 * @return one of {@value #SET}, {@value #ADD}, {@value #REMOVE},
	 *         {@value #UNKNOWN}
	 */
	public int getType();

	/**
	 * The Model component URI on which the delta has occured
	 * 
	 * @author erdillon
	 * 
	 */
	public URI getAffectedModelComponentURI();

	/**
	 * On a {@value #SET} returns the new value of the feature, on an
	 * {@value #ADD} returns the add value of the feature, on a {@value #REMOVE}
	 * returns null
	 * 
	 * @return
	 */
	public Object getNewValue();

	/**
	 * On a {@value #SET} returns the old value of the feature, on an
	 * {@value #ADD} returns null, on a {@value #REMOVE} returns the value that
	 * was deleted
	 * 
	 * @return
	 */
	public Object getOldValue();

	/**
	 * Returns the feature being affected for the model component
	 * 
	 * @return
	 */
	public String getFeature();

	/**
	 * Returns all the affected projects affected by the model change
	 * 
	 * Note that if a model change occurs in a project referenced by other
	 * Tigerstripe Model project, all referencing projects will be included
	 * here.
	 * 
	 * @return
	 */
	public ITigerstripeModelProject[] getAffectedProjects()
			throws TigerstripeException;

	/**
	 * Returns the project where this delta originated.
	 * 
	 * @return
	 */
	public ITigerstripeModelProject getProject();

	/**
	 * Return the resource where this delta is taking place
	 * 
	 * @return
	 */
	public IResource getAffectedResource();

	/**
	 * Apply the given delta.
	 * 
	 * This method is used in the case of refactors.
	 * 
	 * @param toCleanUp
	 *            - upon return, this collection will contain a list of
	 *            artifacts to remove.
	 * @param toSave
	 *            - if not null, this will contain the list of artifacts to save
	 *            to commit the delta. If null, each delta is followed by a
	 *            save.
	 * @throws TigerstripeException
	 */
	public void apply(Collection<Object> toCleanUp,
			Collection<IAbstractArtifact> toSave) throws TigerstripeException;
}
