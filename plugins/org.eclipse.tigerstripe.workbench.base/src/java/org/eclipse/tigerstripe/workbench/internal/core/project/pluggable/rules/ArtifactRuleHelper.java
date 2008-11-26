/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules;

import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetPredicate;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.api.impl.QueryArtifactsByType;
import org.eclipse.tigerstripe.workbench.internal.contract.predicate.FacetPredicate;
import org.eclipse.tigerstripe.workbench.internal.contract.predicate.PredicateFilter;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactFilter;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactNoFilter;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.IPluginRuleExecutor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactFilter;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactRule;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.queries.IQueryArtifactsByType;

public class ArtifactRuleHelper {

	public static Collection<IAbstractArtifact> getResultSet(String artifactType, IPluginConfig pluginConfig,
			boolean includeDependencies, IProgressMonitor monitor) throws TigerstripeException{
		// Build the result set
		Collection<IAbstractArtifact> resultSet = null;
		try {
			IAbstractTigerstripeProject aProject = pluginConfig
			.getProjectHandle();
			if (aProject != null
					&& aProject instanceof ITigerstripeModelProject) {
				ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;

				IArtifactManagerSession mgrSession = project.getArtifactManagerSession();
				ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) mgrSession;
				ArtifactManager artifactMgr = session.getArtifactManager();

				// Compute a filter to use for the list of artifact in the case
				// where dependencies/referenced projects need to be looped over
				ArtifactFilter fFilter = new ArtifactNoFilter();
				if (includeDependencies) {
					IFacetReference ref = pluginConfig.getProjectHandle()
					.getActiveFacet();
					if (ref != null) {
						IFacetPredicate allPredicate = new FacetPredicate(ref,
								pluginConfig.getProjectHandle());
						allPredicate.resolve(monitor);
						fFilter = new PredicateFilter(allPredicate);
					}
				}

				// If this is "Any Artifact" then get all artifacts,
				// else use the type to select the subset
				if (artifactType.equals(IArtifactRule.ANY_ARTIFACT_LABEL)) {
					if (includeDependencies) {
						IQueryAllArtifacts query = (IQueryAllArtifacts) mgrSession
						.makeQuery(IQueryAllArtifacts.class
								.getCanonicalName());
						query.setIncludeDependencies(false);
						resultSet = mgrSession.queryArtifact(query);
					} else {
						// In this case we need to apply the filter
						resultSet = ArtifactFilter.filter(artifactMgr
								.getAllArtifacts(true, true, monitor), fFilter);
					}
				} else {
					if (includeDependencies) {
						IQueryArtifactsByType query = (IQueryArtifactsByType) mgrSession
						.makeQuery(IQueryArtifactsByType.class
								.getCanonicalName());
						query.setIncludeDependencies(false);
						query.setArtifactType(artifactType);
						resultSet = mgrSession.queryArtifact(query);
					} else {
						AbstractArtifact model = QueryArtifactsByType
						.getArtifactClassForType(artifactMgr,
								artifactType);
						resultSet = ArtifactFilter.filter(artifactMgr
								.getArtifactsByModel(model, true, true, monitor), fFilter);
					}
				}
			}
		} catch (TigerstripeException t){
			throw new TigerstripeException("Failed to build result Set. ",t);
		}
		return resultSet;
	}

	public static IArtifactFilter getArtifactFilter(String filterClass, IPluginRuleExecutor exec)
		throws TigerstripeException {
		// look for filter object
		IArtifactFilter filter = null;
		if (filterClass != null
				&& filterClass.length() != 0) {
			Object filterObj = exec.getPlugin().getInstance(
					filterClass);
			if (filterObj instanceof IArtifactFilter) {
				filter = (IArtifactFilter) filterObj;
			} else {
				TigerstripeRuntime
					.logInfoMessage("Error: "
						+ filterClass
						+ " doesn't implement IArtifactFilter, ignoring.");
			}
		}
		return filter;
	}
	

}
