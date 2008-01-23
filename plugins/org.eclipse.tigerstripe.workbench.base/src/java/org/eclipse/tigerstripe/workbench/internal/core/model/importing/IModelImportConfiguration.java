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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing;

import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * This interface defines the configuration details for an import.
 * 
 * @author Eric Dillon
 * @since 1.1
 */
public interface IModelImportConfiguration {

	public IModelImportConfiguration make(ITigerstripeProject referenceProject);

	/**
	 * Returns the project to use as a reference (Checkpoint) for the import
	 * 
	 * @return
	 */
	public ITigerstripeProject getReferenceProject();

}
