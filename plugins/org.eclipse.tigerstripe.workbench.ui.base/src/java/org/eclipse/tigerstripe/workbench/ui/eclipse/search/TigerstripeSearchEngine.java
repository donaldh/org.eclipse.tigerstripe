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
package org.eclipse.tigerstripe.workbench.ui.eclipse.search;

import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.QueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.ILabel;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.search.TigerstripeSearchPage.SearchPatternData;

public class TigerstripeSearchEngine {

	private static TigerstripeSearchEngine instance;

	public static TigerstripeSearchEngine getInstance() {
		if (instance == null) {
			instance = new TigerstripeSearchEngine();
		}
		return instance;
	}

	private TigerstripeSearchEngine() {

	}

	public IStatus search(SearchPatternData searchData,
			TigerstripeSearchResultCollector collector, IProgressMonitor monitor) {

		if (searchData.getContainerScope() == ISearchPageContainer.SELECTED_PROJECTS_SCOPE
				|| searchData.getContainerScope() == ISearchPageContainer.WORKSPACE_SCOPE)
			return internalProjectSearch(searchData, collector, monitor);
		else if (searchData.getContainerScope() == ISearchPageContainer.SELECTION_SCOPE)
			return internalArtifactsSearch(searchData, collector, monitor);

		return Status.CANCEL_STATUS;
	}

	private IStatus internalArtifactsSearch(SearchPatternData searchData,
			TigerstripeSearchResultCollector collector, IProgressMonitor monitor) {
		for (IAbstractArtifact artifact : searchData.getArtifactsInScope()) {
			IStatus s = searchArtifact(artifact, collector, monitor);
			if (s == Status.CANCEL_STATUS)
				return s;
		}

		monitor.done();
		return Status.OK_STATUS;
	}

	private IStatus searchArtifact(IAbstractArtifact artifact,
			TigerstripeSearchResultCollector collector, IProgressMonitor monitor) {
		monitor.subTask(artifact.getName());
		collector.acceptModelComponent(artifact);

		if (artifact instanceof IAssociationArtifact) {
			IAssociationArtifact assoc = (IAssociationArtifact) artifact;
			collector.acceptModelComponent((IAssociationEnd) assoc.getAEnd());
			collector.acceptModelComponent((IAssociationEnd) assoc.getZEnd());
		}

		for (IField field : artifact.getFields()) {
			collector.acceptModelComponent(field);
		}

		for (IMethod method : artifact.getMethods()) {
			collector.acceptModelComponent(method);
		}

		for (ILabel label : artifact.getLabels()) {
			collector.acceptModelComponent(label);
		}

		monitor.worked(1);

		if (monitor.isCanceled())
			return Status.CANCEL_STATUS;

		return Status.OK_STATUS;
	}

	private IStatus internalProjectSearch(SearchPatternData searchData,
			TigerstripeSearchResultCollector collector, IProgressMonitor monitor) {
		try {
			ITigerstripeProject[] projects = searchData.getProjectsInScope();

			for (ITigerstripeProject project : projects) {
				IArtifactManagerSession session = project
						.getArtifactManagerSession();
				Collection<IAbstractArtifact> artifacts = session
						.queryArtifact(new QueryAllArtifacts());
				monitor.beginTask("Searching '" + project.getProjectLabel()
						+ "'", artifacts.size());
				for (IAbstractArtifact artifact : artifacts) {
					IStatus s = searchArtifact(artifact, collector, monitor);
					if (s == Status.CANCEL_STATUS)
						return s;
				}
			}

			monitor.done();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return Status.OK_STATUS;
	}
}
