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

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.QueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.export.IModelExporter;
import org.eclipse.tigerstripe.workbench.internal.core.model.export.IModelExporterFacetManager;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;

public class FacetModelExporter implements IModelExporter {

	private IFile facetFile;

	private ITigerstripeModelProject sourceProject;

	private ITigerstripeModelProject destinationProject;

	public FacetModelExporter(ITigerstripeModelProject sourceProject, ITigerstripeModelProject destinationProject, IFile facetFile) {

		super();

		this.sourceProject = sourceProject;
		this.destinationProject = destinationProject;
		this.facetFile = facetFile;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.workbench.internal.core.model.export.IModelExporter
	 * #export(boolean)
	 */
	@SuppressWarnings("deprecation")
	public void export(boolean includeDependencies) throws TigerstripeException, CoreException {

		validateAttributes();

		IModelExporterFacetManager facetManager = new FacetModelExporterFacetManager(sourceProject);
		facetManager.applyExportFacet(facetFile);

		// query artifacts from source project
		IArtifactQuery query = new QueryAllArtifacts();
		query.setIncludeDependencies(includeDependencies);
		List<IAbstractArtifact> artifacts = (List<IAbstractArtifact>) sourceProject.getArtifactManagerSession().queryArtifact(query);
		for (IAbstractArtifact artifact : artifacts) {

			if (artifact.isInActiveFacet()) {

				IAbstractArtifact cloned = ((AbstractArtifact) artifact).makeWorkingCopy(new NullProgressMonitor());
				destinationProject.getArtifactManagerSession().addArtifact(cloned);
				cloned.doSave(new NullProgressMonitor());
			}
		}

		facetManager.restoreActiveFacet();

	}

	private void validateAttributes() throws TigerstripeException {

		if (facetFile == null || !facetFile.exists()) {
			String facet;
			if (facetFile == null) {
				facet = "*null*";
			} else {
				facet = facetFile.getFullPath().toOSString();
			}

			throw new TigerstripeException("The facet file " + facet + " does not exist");
		}

		if (sourceProject == null || !sourceProject.exists()) {
			String proj;
			if (sourceProject == null) {
				proj = "source";
			} else {
				proj = sourceProject.getFullPath().toOSString();
			}

			throw new TigerstripeException("The " + proj + " project does not exist");
		}

		if (destinationProject == null || !destinationProject.exists()) {
			String proj;
			if (destinationProject == null) {
				proj = "destination";
			} else {
				proj = destinationProject.getFullPath().toOSString();
			}

			throw new TigerstripeException("The " + proj + " project does not exist");
		}
	}

}
