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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
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

	private List<IAbstractArtifact> exportedArtifacts = new ArrayList<IAbstractArtifact>();

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
	public void export(boolean includeDependencies, IProgressMonitor monitor) throws TigerstripeException, CoreException {

		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		try {

			validateAttributes();

			IArtifactQuery query = new QueryAllArtifacts();
			query.setIncludeDependencies(includeDependencies);
			List<IAbstractArtifact> artifacts = (List<IAbstractArtifact>) sourceProject.getArtifactManagerSession().queryArtifact(query);

			monitor.beginTask("Exporting", artifacts.size());

			monitor.subTask("Applying facet for export");
			IModelExporterFacetManager facetManager = new FacetModelExporterFacetManager(sourceProject);
			facetManager.applyExportFacet(facetFile);

			if (monitor.isCanceled()) {
				facetManager.restoreActiveFacet();
				return;
			}

			for (IAbstractArtifact artifact : artifacts) {

				if (artifact.isInActiveFacet()) {

					IAbstractArtifact cloned = ((AbstractArtifact) artifact).makeWorkingCopy(new NullProgressMonitor());
					monitor.subTask(cloned.getName());
					destinationProject.getArtifactManagerSession().addArtifact(cloned);
					exportedArtifacts.add(cloned);
					cloned.doSave(monitor);
				}

			}

			monitor.subTask("Applying original facet (if applicable)");
			facetManager.restoreActiveFacet();

			IProject project = (IProject) destinationProject.getAdapter(IProject.class);
			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);

		} finally {

			monitor.done();
		}

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
