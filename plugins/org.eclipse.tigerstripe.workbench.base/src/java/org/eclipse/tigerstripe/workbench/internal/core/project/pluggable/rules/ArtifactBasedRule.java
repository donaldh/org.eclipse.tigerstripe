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

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.MigrationHelper;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetPredicate;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.api.impl.QueryArtifactsByType;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactMetadataSession;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.PluginVelocityLog;
import org.eclipse.tigerstripe.workbench.internal.contract.predicate.FacetPredicate;
import org.eclipse.tigerstripe.workbench.internal.contract.predicate.PredicateFilter;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactFilter;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactNoFilter;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.Expander;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.IPluginRuleExecutor;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactBasedTemplateRule;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactFilter;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactModel;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.queries.IQueryArtifactsByType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Implementation of an Artifact-based rule, ie. a rule that run as a result of
 * a loop over a set of identified Artifact types.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class ArtifactBasedRule extends TemplateBasedRule implements
		IArtifactBasedTemplateRule {

	private final static String REPORTTEMPLATE = "IArtifactBasedTemplateRunRule.vm";

	private boolean includeDependencies = false;

	public final static String LABEL = "Artifact Model Run Rule";

	private String modelClass = "";

	private String modelClassName = "model";

	private String filterClass = "";

	private String artifactType = "";

	public String getArtifactType() {
		return artifactType;
	}

	public void setArtifactType(String artifactType) {
		markDirty();
		this.artifactType = artifactType;
	}

	public String getModelClass() {
		return modelClass;
	}

	public void setModelClass(String modelClass) {
		markDirty();
		this.modelClass = modelClass;
	}

	public String getModelClassName() {
		return modelClassName;
	}

	public void setModelClassName(String modelClassName) {
		markDirty();
		this.modelClassName = modelClassName;
	}

	public String getArtifactFilterClass() {
		return this.filterClass;
	}

	public void setArtifactFilterClass(String filterClass) {
		markDirty();
		this.filterClass = filterClass;
	}

	@Override
	public String getLabel() {
		return LABEL;
	}

	public String getType() {
		return IArtifactBasedTemplateRule.class.getCanonicalName();
	}

	@Override
	public void buildBodyFromNode(Node node) {
		Element elm = (Element) node;
		NodeList bodies = elm.getElementsByTagName("body");
		if (bodies.getLength() != 0) {
			Element body = (Element) bodies.item(0);
			setTemplate(body.getAttribute("template"));
			setOutputFile(body.getAttribute("outputFile"));
			setModelClass(body.getAttribute("modelClass"));
			setModelClassName(body.getAttribute("modelClassName"));
			setArtifactFilterClass(body.getAttribute("artifactFilterClass"));
			setArtifactType(MigrationHelper.pluginMigrateArtifactType(body
					.getAttribute("artifactType")));
			setSuppressEmptyFilesStr(body.getAttribute("suppressFiles"));
			setOverwriteFilesStr(body.getAttribute("overwriteFiles"));
			setIncludeDependenciesStr(body.getAttribute("includeDependencies"));
			NodeList libraries = body.getElementsByTagName("library");
			for (int i = 0; i < libraries.getLength(); i++) {
				addMacroLibrary(((Element) libraries.item(i))
						.getAttribute("name"));
			}

		}
	}

	@Override
	public Node getBodyAsNode(Document document) {
		Element elm = document.createElement("body");
		elm.setAttribute("template", getTemplate());
		elm.setAttribute("outputFile", getOutputFile());
		elm.setAttribute("modelClass", getModelClass());
		elm.setAttribute("modelClassName", getModelClassName());
		elm.setAttribute("artifactFilterClass", getArtifactFilterClass());
		elm.setAttribute("artifactType", getArtifactType());
		elm.setAttribute("suppressFiles", isSuppressEmptyFilesStr());
		elm.setAttribute("overwriteFiles", isOverwriteFilesStr());
		elm.setAttribute("includeDependencies", isIncludeDependenciesStr());
		if (hasMacroLibrary()) {
			for (int i = 0; i < getMacroLibraries().length; i++) {
				Element lib = (document.createElement("library"));
				lib.setAttribute("name", getMacroLibraries()[i]);
				elm.appendChild(lib);
			}
		}
		return elm;
	}

	@Override
	protected String getReportTemplatePath() {
		return PluggablePlugin.TEMPLATE_PREFIX + "/" + REPORTTEMPLATE;
	}

	@Override
	protected void initializeReport(PluggablePluginConfig pluginConfig) {
		super.initializeReport(pluginConfig);
		getReport().setIncludeDependencies(isIncludeDependencies());
	}

	public void trigger(PluggablePluginConfig pluginConfig,
			IPluginRuleExecutor exec) throws TigerstripeException {
		IAbstractArtifact currentArtifact = null;
		// TigerstripeRuntime.logInfoMessage("triggering " + getName());
		Writer writer = null;
		try {
			initializeReport(pluginConfig);
			String logPath = exec.getPlugin().getLogPath(); 
			VelocityEngine engine = setClasspathLoaderForVelocity(pluginConfig,exec);
			Template template = engine.getTemplate(getTemplate());
			Expander expander = new Expander(pluginConfig);
			// TODO add referenced user-java objects into the context
			// VelocityContext defaultContext = getDefaultContext(pluginConfig,
			// exec);
			// VelocityContext localContext = exec.getPlugin()
			// .getLocalVelocityContext(defaultContext, this);

			// We are going to loop over all artifacts of this type
			String artifactType = getArtifactType();

			// There must be a better way? Just to get a label
			IArtifactMetadataSession metaSession = InternalTigerstripeCore
					.getDefaultArtifactMetadataSession();
			String[] baseSupportedArtifacts = metaSession
					.getSupportedArtifactTypes();
			String[] supportedArtifacts = new String[baseSupportedArtifacts.length + 1];
			for (int i = 0; i < baseSupportedArtifacts.length; i++) {
				supportedArtifacts[i] = baseSupportedArtifacts[i];
			}
			supportedArtifacts[baseSupportedArtifacts.length] = ArtifactBasedRule.ANY_ARTIFACT_LABEL;

			int index = -1;
			for (int i = 0; i < supportedArtifacts.length; i++) {
				if (supportedArtifacts[i].equals(artifactType)) {
					index = i;
				}
			}

			// Bug 224655
			if (index == -1) {
				// This means the plugin was deployed with an incorrect artifact
				// type
				// or the current profile disabled the corresponding artifact
				if (artifactType == null || artifactType.length() == 0) {
					throw new TigerstripeException("Rule '" + getName()
							+ "' is incomplete: no artifact type.");
				} else {
					throw new TigerstripeException("Rule '" + getName()
							+ "' can't be run because '" + artifactType
							+ "' is disabled in active profile.");
				}
			}

			String[] baseSupportedArtifactLabels = metaSession
					.getSupportedArtifactTypeLabels();
			String artifactLabel;
			if (index < baseSupportedArtifactLabels.length) {
				artifactLabel = baseSupportedArtifactLabels[index];
			} else if (index == baseSupportedArtifactLabels.length) {
				artifactLabel = ArtifactBasedRule.ANY_ARTIFACT_LABEL;
			} else {
				artifactLabel = "Something else";
			}

			getReport().setArtifactType(artifactLabel);
			// Phew - got it!

			// IProjectSession session = API.getDefaultProjectSession();
			IAbstractTigerstripeProject aProject = pluginConfig
					.getProjectHandle();
			// session
			// .makeTigerstripeProject(pluginConfig.getProject().getBaseDir()
			// .toURI(), ITigerstripeProject.class
			// .getCanonicalName());

			if (aProject != null
					&& aProject instanceof ITigerstripeModelProject) {
				ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;

				IArtifactManagerSession mgrSession = project
						.getArtifactManagerSession();
				Collection<IAbstractArtifact> resultSet;

				// Compute a filter to use for the list of artifact in the case
				// where dependencies/referenced projects need to be looped over
				ArtifactFilter fFilter = new ArtifactNoFilter();
				if (isIncludeDependencies()) {
					IFacetReference ref = pluginConfig.getProjectHandle()
							.getActiveFacet();
					if (ref != null) {
						IFacetPredicate allPredicate = new FacetPredicate(ref,
								pluginConfig.getProjectHandle());
						allPredicate.resolve(exec.getConfig().getMonitor());
						fFilter = new PredicateFilter(allPredicate);
					}
				}

				ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) mgrSession;
				ArtifactManager artifactMgr = session.getArtifactManager();

				// If this is "Any Artifact" then get all artifacts,
				// else use the type to select the subset
				if (artifactType.equals(ANY_ARTIFACT_LABEL)) {
					if (!isIncludeDependencies()) {
						IQueryAllArtifacts query = (IQueryAllArtifacts) mgrSession
								.makeQuery(IQueryAllArtifacts.class
										.getCanonicalName());
						query.setIncludeDependencies(false);
						resultSet = mgrSession.queryArtifact(query);
					} else {
						// In this case we need to apply the filter
						resultSet = ArtifactFilter.filter(artifactMgr
								.getAllArtifacts(true, true, exec.getConfig()
										.getMonitor()), fFilter);
					}
				} else {
					if (!isIncludeDependencies()) {
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
								.getArtifactsByModel(model, true, true, exec
										.getConfig().getMonitor()), fFilter);
					}
				}

				// look for filter object
				IArtifactFilter filter = null;
				if (getArtifactFilterClass() != null
						&& getArtifactFilterClass().length() != 0) {
					Object filterObj = exec.getPlugin().getInstance(
							getArtifactFilterClass());
					if (filterObj instanceof IArtifactFilter) {
						filter = (IArtifactFilter) filterObj;
					} else {
						TigerstripeRuntime
								.logInfoMessage("Error: "
										+ getArtifactFilterClass()
										+ " doesn't implement IArtifactFilter, ignoring.");
					}
				}

				VelocityContext defaultContext = getDefaultContext(
						pluginConfig, exec);

				for (IAbstractArtifact artifact : resultSet) {

					VelocityContext localContext = exec.getPlugin()
							.getLocalVelocityContext(defaultContext, this);

					currentArtifact = artifact;
					if (filter != null && !filter.select(artifact)) {
						continue;
					}

					IArtifactModel model = null;
					if (getModelClass() != null
							&& getModelClass().length() != 0) {
						Object modelObj = exec.getPlugin().getInstance(
								getModelClass());
						if (modelObj instanceof IArtifactModel) {
							model = (IArtifactModel) modelObj;
							model.setIArtifact(artifact);
							model.setPluginConfig(pluginConfig);

							localContext.put(getModelClassName(), model);
							expander
									.setCurrentModel(model, getModelClassName());
						} else {
							TigerstripeRuntime
									.logInfoMessage("Error: "
											+ getModelClass()
											+ " doesn't implement IArtifactModel, ignoring.");
						}
					}

					localContext.put("artifact", artifact);
					localContext.put("templateName", template.getName());
					localContext.put("pluginConfig", pluginConfig);

					// Logging stuff
					localContext.put("pluginLog", new PluginVelocityLog(
							template.getName()));

					expander.setCurrentArtifact(artifact);
					String targetFile = expander.expandVar(getOutputFile());
					File outputFileF = getOutputFile(pluginConfig, targetFile,
							exec.getConfig());

					// Only create the flag if we are allowed to overwrite Or
					// the file doesn't exist
					if (isOverwriteFiles() || !outputFileF.exists()) {

						writer = getDefaultWriter(pluginConfig, targetFile,
								exec.getConfig());
						template.merge(localContext, writer);
						writer.close();

						Collection<String> artifacts = getReport()
								.getMatchedArtifacts();
						artifacts.add(artifact.getFullyQualifiedName());

						Long fred = outputFileF.length();
						if (fred.intValue() == 0 && isSuppressEmptyFiles()) {
							outputFileF.delete();
							Collection<String> files = getReport()
									.getSuppressedFiles();
							files.add(targetFile);
						} else {
							Collection<String> files = getReport()
									.getGeneratedFiles();
							files.add(targetFile);
						}
					} else {
						Collection<String> files = getReport()
								.getPreservedFiles();
						files.add(targetFile);
					}
				}
			}

		} catch (TigerstripeException e) {
			TigerstripeException newException;
			if (currentArtifact != null) {
				newException = new TigerstripeException(e.getMessage()
						+ ", current artifact: "
						+ currentArtifact.getFullyQualifiedName(), e);
			} else {
				newException = e;
			}
			throw newException;
		} catch (Exception e) {
			if (currentArtifact != null)
				throw new TigerstripeException(
						"Unexpected error while merging '" + getTemplate()
								+ "' template: " + e.getMessage()
								+ ", current artifact: "
								+ currentArtifact.getFullyQualifiedName(), e);
			else
				throw new TigerstripeException(
						"Unexpected error while merging '" + getTemplate()
								+ "' template: " + e.getMessage(), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	public boolean isIncludeDependencies() {
		return this.includeDependencies;
	}

	public String isIncludeDependenciesStr() {
		return Boolean.toString(includeDependencies);
	}

	public void setIncludeDependencies(boolean includeDependencies) {
		markDirty();
		this.includeDependencies = includeDependencies;
	}

	public void setIncludeDependenciesStr(String includeDependencies) {
		markDirty();
		this.includeDependencies = Boolean.parseBoolean(includeDependencies);
	}

}
