/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules;

import java.util.Collection;

import org.apache.velocity.VelocityContext;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactFilter;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactNoFilter;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.Expander;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.util.VelocityContextUtil;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.plugins.IPluginRuleExecutor;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public abstract class M1LevelRule extends BaseTemplatePPluginRule {

	private VelocityContext defaultVContext;

	/**
	 * Returns the default velocity context.
	 * 
	 * @return VelocityContext - the default context
	 */
	@Override
	protected VelocityContext getDefaultContext(
			PluggablePluginConfig pluginConfig, IPluginRuleExecutor exec)
			throws TigerstripeException {
		if (this.defaultVContext == null) {
			this.defaultVContext = new VelocityContext();
			VelocityContextUtil util = new VelocityContextUtil();
			this.defaultVContext.put("util", util);
		}

		// TODO allow to reference a filter from Use-defined java object
		ArtifactFilter filter = new ArtifactNoFilter();

		ITigerstripeModelProject handle = pluginConfig.getProjectHandle();
		// (ITigerstripeProject) API
		// .getDefaultProjectSession().makeTigerstripeProject(
		// pluginConfig.getProject().getBaseDir().toURI(), null);
		ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) handle
				.getArtifactManagerSession();
		ArtifactManager artifactMgr = session.getArtifactManager();

		// Let's put what we'll need in the context and get going
		Collection<IAbstractArtifact> entities = ArtifactFilter.filter(
				artifactMgr.getArtifactsByModel(ManagedEntityArtifact.MODEL,
						false, new NullProgressMonitor()), filter);

		Collection<IAbstractArtifact> datatypes = ArtifactFilter.filter(
				artifactMgr.getArtifactsByModel(DatatypeArtifact.MODEL, false,
						new NullProgressMonitor()), filter);

		Collection<IAbstractArtifact> events = ArtifactFilter.filter(
				artifactMgr.getArtifactsByModel(EventArtifact.MODEL, false,
						new NullProgressMonitor()), filter);

		Collection<IAbstractArtifact> enums = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(EnumArtifact.MODEL, false,
						new NullProgressMonitor()), filter);

		Collection<IAbstractArtifact> exceptions = ArtifactFilter.filter(
				artifactMgr.getArtifactsByModel(ExceptionArtifact.MODEL, false,
						new NullProgressMonitor()), filter);

		Collection<IAbstractArtifact> queries = ArtifactFilter.filter(
				artifactMgr.getArtifactsByModel(QueryArtifact.MODEL, false,
						new NullProgressMonitor()), filter);

		Collection<IAbstractArtifact> sessions = ArtifactFilter.filter(
				artifactMgr.getArtifactsByModel(SessionFacadeArtifact.MODEL,
						false, new NullProgressMonitor()), filter);

		Collection<IAbstractArtifact> updateProcedures = ArtifactFilter.filter(
				artifactMgr.getArtifactsByModel(UpdateProcedureArtifact.MODEL,
						false, new NullProgressMonitor()), filter);

		Collection<IAbstractArtifact> associations = ArtifactFilter.filter(
				artifactMgr.getArtifactsByModel(AssociationArtifact.MODEL,
						false, new NullProgressMonitor()), filter);

		Collection<IAbstractArtifact> dependencies = ArtifactFilter.filter(
				artifactMgr.getArtifactsByModel(DependencyArtifact.MODEL,
						false, new NullProgressMonitor()), filter);

		Collection<IAbstractArtifact> associationClasses = ArtifactFilter
				.filter(artifactMgr.getArtifactsByModel(
						AssociationClassArtifact.MODEL, false,
						new NullProgressMonitor()), filter);

		// Bug 928: removed ArtifactFilter that was used to filter all lists
		// below
		// since the facet is now propagated to all dependencies/referenced
		// projects
		Collection<IAbstractArtifact> allEntities = artifactMgr
				.getArtifactsByModel(ManagedEntityArtifact.MODEL, true, false,
						new NullProgressMonitor());

		Collection<IAbstractArtifact> allDatatypes = artifactMgr
				.getArtifactsByModel(DatatypeArtifact.MODEL, true, false,
						new NullProgressMonitor());

		Collection<IAbstractArtifact> allEvents = artifactMgr
				.getArtifactsByModel(EventArtifact.MODEL, true, false,
						new NullProgressMonitor());

		Collection<IAbstractArtifact> allEnums = artifactMgr
				.getArtifactsByModel(EnumArtifact.MODEL, true, false,
						new NullProgressMonitor());

		Collection<IAbstractArtifact> allExceptions = artifactMgr
				.getArtifactsByModel(ExceptionArtifact.MODEL, true, false,
						new NullProgressMonitor());

		Collection<IAbstractArtifact> allQueries = artifactMgr
				.getArtifactsByModel(QueryArtifact.MODEL, true, false,
						new NullProgressMonitor());

		Collection<IAbstractArtifact> allSessions = artifactMgr
				.getArtifactsByModel(SessionFacadeArtifact.MODEL, true, false,
						new NullProgressMonitor());

		Collection<IAbstractArtifact> allUpdateProcedures = artifactMgr
				.getArtifactsByModel(UpdateProcedureArtifact.MODEL, true,
						false, new NullProgressMonitor());

		Collection<IAbstractArtifact> allAssociations = artifactMgr
				.getArtifactsByModel(AssociationArtifact.MODEL, true, false,
						new NullProgressMonitor());

		Collection<IAbstractArtifact> allDependencies = artifactMgr
				.getArtifactsByModel(DependencyArtifact.MODEL, true, false,
						new NullProgressMonitor());

		Collection<IAbstractArtifact> allAssociationClasses = artifactMgr
				.getArtifactsByModel(AssociationClassArtifact.MODEL, true,
						false, new NullProgressMonitor());

		Collection<IAbstractArtifact> artifacts = artifactMgr.getAllArtifacts(
				false, new NullProgressMonitor());
		Collection<IAbstractArtifact> allArtifacts = artifactMgr
				.getAllArtifacts(true, false, new NullProgressMonitor());

		defaultVContext.put("artifacts", artifacts);
		defaultVContext.put("allArtifacts", allArtifacts);

		defaultVContext.put("entities", entities);
		defaultVContext.put("datatypes", datatypes);
		defaultVContext.put("events", events);
		defaultVContext.put("enumerations", enums);
		defaultVContext.put("exceptions", exceptions);
		defaultVContext.put("queries", queries);
		defaultVContext.put("updateProcedures", updateProcedures);
		defaultVContext.put("associations", associations);
		defaultVContext.put("associationClasses", associationClasses);
		defaultVContext.put("dependencies", dependencies);
		defaultVContext.put("sessions", sessions);

		defaultVContext.put("allEntities", allEntities);
		defaultVContext.put("allDatatypes", allDatatypes);
		defaultVContext.put("allEvents", allEvents);
		defaultVContext.put("allEnumerations", allEnums);
		defaultVContext.put("allExceptions", allExceptions);
		defaultVContext.put("allQueries", allQueries);
		defaultVContext.put("allUpdateProcedures", allUpdateProcedures);
		defaultVContext.put("allAssociations", allAssociations);
		defaultVContext.put("allAssociationClasses", allAssociationClasses);
		defaultVContext.put("allDependencies", allDependencies);
		defaultVContext.put("allSessions", allSessions);

		defaultVContext.put("pluginConfig", pluginConfig);
		defaultVContext.put("runtime", TigerstripeRuntime.getInstance());

		// This should eventually get removed as TigerstripeProject is not in
		// the API
		defaultVContext.put("tsProject", pluginConfig.getProject());
		defaultVContext.put("exp", new Expander(pluginConfig));
		defaultVContext.put("manager", artifactMgr);

		defaultVContext.put("tsProjectHandle", handle);
		defaultVContext.put("managerSession", session);
		defaultVContext
				.put("pluginDir", getContainingDescriptor().getBaseDir());

		return this.defaultVContext;
	}

}
