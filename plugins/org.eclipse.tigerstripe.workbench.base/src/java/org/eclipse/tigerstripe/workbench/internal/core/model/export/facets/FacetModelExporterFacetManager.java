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

package org.eclipse.tigerstripe.workbench.internal.core.model.export.facets;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.core.model.export.IModelExporterFacetManager;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class FacetModelExporterFacetManager implements IModelExporterFacetManager {

	/*
	 * Source project for the export.
	 */
	private ITigerstripeModelProject sourceProject;

	/*
	 * Populate if there is an active facet when the export is invoked.
	 */
	private IFacetReference activeFacet;

	public FacetModelExporterFacetManager(ITigerstripeModelProject sourceProject) {

		super();
		this.sourceProject = sourceProject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.workbench.internal.contract.export.
	 * IModelExporterFacetManager
	 * #applyExportFacet(org.eclipse.core.resources.IFile)
	 */
	public void applyExportFacet(IFile facetFile) throws CoreException, TigerstripeException {

		// store active facet
		if (sourceProject.getActiveFacet() != null) {
			activeFacet = sourceProject.getActiveFacet();
		}

		// clean up a bit
		sourceProject.resetActiveFacet();

		// apply new facet
		InternalTigerstripeCore.createModelFacet(facetFile, new NullProgressMonitor());
		IFacetReference facetReference = sourceProject.makeFacetReference(facetFile.getProjectRelativePath().toOSString());
		sourceProject.setActiveFacet(facetReference, new NullProgressMonitor());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.workbench.internal.contract.export.
	 * IModelExporterFacetManager#restoreActiveFacet()
	 */
	public void restoreActiveFacet() throws CoreException, TigerstripeException {

		sourceProject.resetActiveFacet();
		if (activeFacet != null) {
			sourceProject.setActiveFacet(activeFacet, null);
		}
	}

}
