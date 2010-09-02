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
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BaseContainerObject;
import org.eclipse.tigerstripe.workbench.internal.IContainedObject;
import org.eclipse.tigerstripe.workbench.internal.IContainerObject;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.PackageArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.DiagramGenerator;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.Expander;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.IPluginRuleExecutor;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.RuleReport;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.GeneratorProjectDescriptor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract class Rule extends BaseContainerObject implements
		IContainedObject, IContainerObject, IRule {

	private boolean enabled = true;

	private String name = "";

	private String description = "";

	private RuleReport report;

	// =====================================================================
	// IContainedObject
	private boolean isLocalDirty = false;
	private IContainerObject container = null;

	public void setContainer(IContainerObject container) {
		isLocalDirty = false;
		this.container = container;
	}

	public void clearDirty() {
		isLocalDirty = false;
	}

	public boolean isDirty() {
		return isLocalDirty;
	}

	protected abstract String getReportTemplatePath();

	protected void initializeReport(PluggablePluginConfig pluginConfig) {
		this.report = new RuleReport(pluginConfig);
		this.report.setTemplate(getReportTemplatePath());
		this.report.setName(getName());
		this.report.setType(getLabel());
		this.report.setEnabled(isEnabled());
	}

	/**
	 * Marks this object as dirty and notify the container if any
	 * 
	 */
	protected void markDirty() {
		isLocalDirty = true;
		if (container != null) {
			container.notifyDirty(this);
		}
	}

	public IContainerObject getContainer() {
		return container;
	}

	// =====================================================================
	// =====================================================================

	public String getName() {
		return name;
	}

	public void setName(String name) {
		markDirty();
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		markDirty();
		this.description = description;
	}

	public abstract String getLabel();

	public abstract Node getBodyAsNode(Document document);

	public abstract void buildBodyFromNode(Node node);

	public GeneratorProjectDescriptor getContainingDescriptor() {
		return (GeneratorProjectDescriptor) getContainer();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		markDirty();
		this.enabled = enabled;
	}

	public String isEnabledStr() {
		return Boolean.toString(enabled);
	}

	public void setEnabledStr(String enabledStr) {
		setEnabled(Boolean.parseBoolean(enabledStr));
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof IRule) {
			IRule other = (IRule) arg0;
			if (other.getName() != null)
				return other.getName().equals(getName());
		}
		return false;
	}

	public abstract void trigger(PluggablePluginConfig pluginConfig,
			IPluginRuleExecutor exec) throws TigerstripeException;

	public RuleReport getReport() {
		return report;
	}

	public Map<String, Object> getGlobalContext(PluggablePluginConfig pluginConfig )
	throws TigerstripeException {

		Map<String, Object> context = new HashMap<String, Object>();
		ITigerstripeModelProject handle = pluginConfig.getProjectHandle();

		ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) handle
		.getArtifactManagerSession();
		ArtifactManager artifactMgr = session.getArtifactManager();

		// The collections of things in this project
		Collection<IAbstractArtifact> artifacts = artifactMgr.getAllArtifacts(
				false, new NullProgressMonitor());
		Collection<IAbstractArtifact> allArtifacts = artifactMgr
		.getAllArtifacts(true, false, new NullProgressMonitor());
		context.put(ARTIFACTS, artifacts);
		context.put(ALLARTIFACTS, allArtifacts);


		if (artifactMgr.getRegisteredArtifacts().contains(ManagedEntityArtifact.MODEL)){
			Collection<IAbstractArtifact> entities = 
				artifactMgr.getArtifactsByModel(ManagedEntityArtifact.MODEL,
						false, new NullProgressMonitor());
			Collection<IAbstractArtifact> allEntities = artifactMgr
			.getArtifactsByModel(ManagedEntityArtifact.MODEL, true, false,
					new NullProgressMonitor());
			context.put(ENTITIES, entities);
			context.put(ALLENTITIES, allEntities);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(DatatypeArtifact.MODEL)){
			Collection<IAbstractArtifact> datatypes = 
				artifactMgr.getArtifactsByModel(DatatypeArtifact.MODEL, false,
						new NullProgressMonitor());
			Collection<IAbstractArtifact> allDatatypes = artifactMgr
			.getArtifactsByModel(DatatypeArtifact.MODEL, true, false,
					new NullProgressMonitor());
			context.put(DATATYPES, datatypes);
			context.put(ALLDATATYPES, allDatatypes);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(EventArtifact.MODEL)){
			Collection<IAbstractArtifact> events = 
				artifactMgr.getArtifactsByModel(EventArtifact.MODEL, false,
						new NullProgressMonitor());
			Collection<IAbstractArtifact> allEvents = artifactMgr
			.getArtifactsByModel(EventArtifact.MODEL, true, false,
					new NullProgressMonitor());
			context.put(EVENTS, events);
			context.put(ALLEVENTS, allEvents);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(EnumArtifact.MODEL)){
			Collection<IAbstractArtifact> enums = 
				artifactMgr.getArtifactsByModel(EnumArtifact.MODEL, false,
						new NullProgressMonitor());
			Collection<IAbstractArtifact> allEnums = artifactMgr
			.getArtifactsByModel(EnumArtifact.MODEL, true, false,
					new NullProgressMonitor());
			context.put(ENUMERATIONS, enums);
			context.put(ALLENUMERATIONS, allEnums);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(ExceptionArtifact.MODEL)){
			Collection<IAbstractArtifact> exceptions = 
				artifactMgr.getArtifactsByModel(ExceptionArtifact.MODEL, false,
						new NullProgressMonitor());
			Collection<IAbstractArtifact> allExceptions = artifactMgr
			.getArtifactsByModel(ExceptionArtifact.MODEL, true, false,
					new NullProgressMonitor());
			context.put(EXCEPTIONS, exceptions);
			context.put(ALLEXCEPTIONS, allExceptions);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(QueryArtifact.MODEL)){
			Collection<IAbstractArtifact> queries = 
				artifactMgr.getArtifactsByModel(QueryArtifact.MODEL, false,
						new NullProgressMonitor());
			Collection<IAbstractArtifact> allQueries = artifactMgr
			.getArtifactsByModel(QueryArtifact.MODEL, true, false,
					new NullProgressMonitor());
			context.put(QUERIES, queries);
			context.put(ALLQUERIES, allQueries);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(SessionFacadeArtifact.MODEL)){
			Collection<IAbstractArtifact> sessions = 
				artifactMgr.getArtifactsByModel(SessionFacadeArtifact.MODEL,
						false, new NullProgressMonitor());
			Collection<IAbstractArtifact> allSessions = artifactMgr
			.getArtifactsByModel(SessionFacadeArtifact.MODEL, true, false,
					new NullProgressMonitor());
			context.put(SESSIONS, sessions);
			context.put(ALLSESSIONS, allSessions);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(UpdateProcedureArtifact.MODEL)){
			Collection<IAbstractArtifact> updateProcedures = 
				artifactMgr.getArtifactsByModel(UpdateProcedureArtifact.MODEL,
						false, new NullProgressMonitor());
			Collection<IAbstractArtifact> allUpdateProcedures = artifactMgr
			.getArtifactsByModel(UpdateProcedureArtifact.MODEL, true,
					false, new NullProgressMonitor());
			context.put(UPDATEPROCEDURES, updateProcedures);
			context.put(ALLUPDATEPROCEDURES, allUpdateProcedures);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(AssociationArtifact.MODEL)){
			Collection<IAbstractArtifact> associations = 
				artifactMgr.getArtifactsByModel(AssociationArtifact.MODEL,
						false, new NullProgressMonitor());
			Collection<IAbstractArtifact> allAssociations = artifactMgr
			.getArtifactsByModel(AssociationArtifact.MODEL, true, false,
					new NullProgressMonitor());
			context.put(ASSOCIATIONS, associations);
			context.put(ALLASSOCIATIONS, allAssociations);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(DependencyArtifact.MODEL)){
			Collection<IAbstractArtifact> dependencies = 
				artifactMgr.getArtifactsByModel(DependencyArtifact.MODEL,
						false, new NullProgressMonitor());
			Collection<IAbstractArtifact> allDependencies = artifactMgr
			.getArtifactsByModel(DependencyArtifact.MODEL, true, false,
					new NullProgressMonitor());
			context.put(DEPENDENCIES, dependencies);
			context.put(ALLDEPENDENCIES, allDependencies);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(AssociationClassArtifact.MODEL)){
			Collection<IAbstractArtifact> associationClasses = 
				artifactMgr.getArtifactsByModel(
						AssociationClassArtifact.MODEL, false,
						new NullProgressMonitor());
			Collection<IAbstractArtifact> allAssociationClasses = artifactMgr
			.getArtifactsByModel(AssociationClassArtifact.MODEL, true,
					false, new NullProgressMonitor());
			context.put(ASSOCIATIONCLASSES, associationClasses);
			context.put(ALLASSOCIATIONCLASSES, allAssociationClasses);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(PackageArtifact.MODEL)){
			Collection<IAbstractArtifact> packages = 
				artifactMgr.getArtifactsByModel(PackageArtifact.MODEL, false,
						new NullProgressMonitor());
			Collection<IAbstractArtifact> allPackages = artifactMgr
			.getArtifactsByModel(PackageArtifact.MODEL, true, false,
					new NullProgressMonitor());
			context.put(PACKAGES, packages);
			context.put(ALLPACKAGES, allPackages);
		}

		context.put(PLUGINCONFIG, pluginConfig);
		context.put(RUNTIME, TigerstripeRuntime.getInstance());

		// This should eventually get removed as TigerstripeProject is not in
		// the API
		context.put(TSPROJECT, pluginConfig.getProject());
		context.put(TSPROJECTHANDLE, handle);
		context.put(MANAGERSESSION, session);
		context.put(EXP, new Expander(pluginConfig));
		context.put(MANAGER, artifactMgr);


		context.put(PLUGINDIR, getContainingDescriptor().getBaseDir());

		context.put(DIAGRAMGENERATOR, new DiagramGenerator(handle));

		if (session.getActiveFacet() != null
				&& session.getActiveFacet().canResolve())
			context.put(ANNOTATIONCONTEXT, session.getActiveFacet()
					.resolve().getAnnotationContext());

		return context;
	}
	
}
