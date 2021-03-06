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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.QueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.internal.core.model.export.ExportFacetManager;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;

public class FacetExporter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.workbench.internal.core.model.export.IModelExporter
	 * #export(boolean)
	 */
	public static void export(FacetExporterInput inputManager, IProgressMonitor monitor) throws TigerstripeException, CoreException {

		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		try {

			inputManager.validate();
			
			IArtifactQuery query = new QueryAllArtifacts();
			query.setIncludeDependencies(inputManager.isIncludeReferences());
			List<IAbstractArtifact> artifacts = (List<IAbstractArtifact>) inputManager.getSource().getArtifactManagerSession().queryArtifact(query);

			monitor.beginTask("Exporting", artifacts.size());

			monitor.subTask("Applying facet for export");
			ExportFacetManager facetManager = new ExportFacetManager(inputManager.getSource());
			facetManager.applyExportFacet(inputManager.getFacet());

			if (monitor.isCanceled()) {
				facetManager.restoreActiveFacet();
				return;
			}

			for (IAbstractArtifact artifact : artifacts) {

				if (artifact.isInActiveFacet()) {
					IAbstractArtifact cloned = ((IAbstractArtifactInternal) artifact)
							.makeWorkingCopy(new NullProgressMonitor());
					monitor.subTask(cloned.getName());
					inputManager.getDestination().getArtifactManagerSession().addArtifact(cloned);
					cloned.doSave(new NullProgressMonitor());
				}
				monitor.worked(1);

			}

			monitor.subTask("Applying original facet (if applicable)");
			facetManager.restoreActiveFacet();

			IProject project = (IProject) inputManager.getDestination().getAdapter(IProject.class);
			project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());

		} finally {

			monitor.done();
		}
	}

}
