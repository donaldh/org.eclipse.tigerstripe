/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jim Strawn (Cisco Systems, Inc.) - initial implementation
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.internal.core.model.export;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.tigerstripe.workbench.TigerstripeException;

public interface IModelExporterFacetManager {

	/**
	 * Applies the facet to be used for the export operation. This method will
	 * store the currently active facet, if required.
	 * 
	 * @param facetFile
	 * @throws CoreException, TigerstripeException
	 */
	public abstract void applyExportFacet(IFile facetFile) throws CoreException, TigerstripeException;

	/**
	 * Re-applies the facet that was stored when the export operation was
	 * invoked. In the case that there is no stored active facet this method will
	 * clear the "export" facet.
	 * 
	 * @throws CoreException, TigerstripeException
	 */
	public abstract void restoreActiveFacet() throws CoreException, TigerstripeException;

}