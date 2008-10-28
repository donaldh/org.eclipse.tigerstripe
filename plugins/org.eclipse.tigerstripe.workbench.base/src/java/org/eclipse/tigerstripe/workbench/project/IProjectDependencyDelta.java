/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.project;

import org.eclipse.core.runtime.IPath;

/**
 * Details for a change in project dependencies
 * 
 * @author erdillon
 * 
 */
public interface IProjectDependencyDelta {

	public final static int PROJECT_REFERENCE_ADDED = 1;
	public final static int PROJECT_REFERENCE_REMOVED = 2;
	public final static int PROJECT_DEPENDENCY_ADDED = 3;
	public final static int PROJECT_DEPENDENCY_REMOVED = 4;

	public ITigerstripeModelProject getProject();

	/**
	 * The kind of change
	 * 
	 * @return
	 */
	public int getKind();

	/**
	 * The path to the project/dependency that was added/removed
	 * 
	 * @return
	 */
	public IPath getPath();
}
