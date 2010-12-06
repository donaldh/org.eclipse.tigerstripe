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
package org.eclipse.tigerstripe.workbench.queries;

import org.eclipse.tigerstripe.workbench.internal.core.model.ExecutionContext;

/**
 * Base Query type for all Artifact Queries
 * 
 * @author Eric Dillon
 * @since 0.3
 */
public interface IArtifactQuery {

	/**
	 * Check the state of the includeDependencies attribute.
	 * 
	 * @return the value of the includeDependencies attribute
	 */
	public boolean includeDependencies();

	/**
	 * Determines whether artifacts in dependant projects are included in the
	 * query results. When true is passed, the query will return a results set
	 * including any matching artifacts in and dependant modules, or referenced
	 * projects. When false, only the artifacts in the local project are
	 * considered.
	 * 
	 * @param includeDependencies
	 */
	public void setIncludeDependencies(boolean includeDependencies);

	public void setExecutionContext(ExecutionContext executionContext);

	public ExecutionContext getExecutionContext();

}
