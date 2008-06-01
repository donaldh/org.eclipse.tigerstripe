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

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public interface IModelChangeDelta {

	public final int SET = 0;
	public final int ADD = 1;
	public final int REMOVE = 2;
	public final int UNKNOWN = -1;

	public final String ATTRIBUTE = "attribute";
	public final String METHOD = "method";
	public final String LITERAL = "literal";

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

}
