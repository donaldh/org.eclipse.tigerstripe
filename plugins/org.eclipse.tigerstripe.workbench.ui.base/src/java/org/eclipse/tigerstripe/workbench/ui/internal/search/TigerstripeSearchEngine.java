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
package org.eclipse.tigerstripe.workbench.ui.internal.search;

import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.QueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.search.TigerstripeSearchPage.SearchPatternData;

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

		for (ILiteral literal : artifact.getLiterals()) {
			collector.acceptModelComponent(literal);
		}

		monitor.worked(1);

		if (monitor.isCanceled())
			return Status.CANCEL_STATUS;

		return Status.OK_STATUS;
	}

	private IStatus internalProjectSearch(SearchPatternData searchData,
			TigerstripeSearchResultCollector collector, IProgressMonitor monitor) {
		try {
			ITigerstripeModelProject[] projects = searchData.getProjectsInScope();

			for (ITigerstripeModelProject project : projects) {
				IArtifactManagerSession session = project
						.getArtifactManagerSession();
				Collection<IAbstractArtifact> artifacts = session
						.queryArtifact(new QueryAllArtifacts());
				monitor.beginTask("Searching '"
						+ project.getProjectLabel() + "'",
						artifacts.size());
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
