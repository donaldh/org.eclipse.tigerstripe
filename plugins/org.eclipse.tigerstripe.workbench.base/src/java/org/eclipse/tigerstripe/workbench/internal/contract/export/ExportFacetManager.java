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

package org.eclipse.tigerstripe.workbench.internal.contract.export;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.contract.export.IExportFacetManager;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ExportFacetManager implements IExportFacetManager {

	/*
	 * Source project for the export.
	 */
	private ITigerstripeModelProject sourceProject;

	/*
	 * Populate if there is an active facet when the export is invoked.
	 */
	private IFacetReference activeFacet;

	public ExportFacetManager(ITigerstripeModelProject sourceProject) {

		super();
		this.sourceProject = sourceProject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.workbench.internal.contract.export.
	 * IExportFacetManager#applyExportFacet(org.eclipse.core.resources.IFile)
	 */
	public void applyExportFacet(IFile facetFile, IProgressMonitor monitor) throws CoreException, TigerstripeException {

		// store active facet	
		if(sourceProject.getActiveFacet() != null) {
			activeFacet = sourceProject.getActiveFacet();
		}
		
		// clean up a bit
		sourceProject.resetActiveFacet();
		
		// apply new facet
		InternalTigerstripeCore.createModelFacet(facetFile, monitor);
		IFacetReference facetReference = sourceProject.makeFacetReference(facetFile.getProjectRelativePath().toOSString());
		sourceProject.setActiveFacet(facetReference, monitor);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.workbench.internal.contract.export.
	 * IExportFacetManager#restoreActiveFacet()
	 */
	public void restoreActiveFacet(IProgressMonitor monitor) throws CoreException, TigerstripeException {
	
		sourceProject.resetActiveFacet();
		if(activeFacet != null) {
			sourceProject.setActiveFacet(activeFacet, monitor);
		}
	}

}
