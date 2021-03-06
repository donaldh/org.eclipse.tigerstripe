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
package org.eclipse.tigerstripe.workbench.generation;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginReport;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.RuleReport;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.Rule;
import org.eclipse.tigerstripe.workbench.plugins.IGlobalRule;
import org.eclipse.tigerstripe.workbench.plugins.IModelRule;
import org.eclipse.tigerstripe.workbench.plugins.IPluginReport;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * A Plugin run result that contains potential messages/exception regarding a
 * plugin run
 * 
 * @author Eric Dillon
 * 
 */
public class PluginRunStatus extends MultiStatus implements IStatus {

	private IPluginConfig pluginConfig;

	private ITigerstripeModelProject project;

	private IFacetReference facetRef;

	private String context;

	private String message = "";

	private IPluginReport report;

	public void setContext(String context) {
		this.context = context;
	}

	public PluginRunStatus(IPluginConfig pluginConfig,
			ITigerstripeModelProject project, RunConfig config,
			IFacetReference facetRef) {
		this(pluginConfig, project, config, facetRef, "");
	}
	
	public PluginRunStatus(IPluginConfig pluginConfig,
			ITigerstripeModelProject project, RunConfig config,
			IFacetReference facetRef, String message) {
		super(BasePlugin.getPluginId(), 222, "Plugin Run Status", null);
		this.pluginConfig = pluginConfig;
		this.project = project;
		this.facetRef = facetRef;
		this.message = message;
	}

	public PluginRunStatus(String message) {
		super(BasePlugin.getPluginId(), 222, "Plugin Run Status", null);
		this.message = message;
	}

	protected ITigerstripeModelProject getProject() {
		return project;
	}

	protected IFacetReference getFacetReference() {
		return facetRef;
	}

	protected String getContext() {
		return context;
	}

	public IPluginConfig getPluginConfig() {
		return this.pluginConfig;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public String toString() {
		return toString(false);
	}
	
	public String toString(boolean includeHTML) {
		return toString(includeHTML, true);
	}

	public String toString(boolean includeHTML, boolean includeChilds) {
		final StringBuilder res = new StringBuilder();

		boolean hasError = !isOK();
		final String newline = includeHTML ? "<br/>" : "\n";
		try {
			String projectType = "Project";
			if (project instanceof ITigerstripeModuleProject) {
				projectType = "Module";
			}

			if (hasError && includeHTML)
				res.append("<b>");
			res.append("[");
			res.append(getMessage());

			if (project != null) {
				res.append(projectType + ": " + project.getModelId()
						+ " version="
						+ project.getProjectDetails().getVersion());
			}
			if (pluginConfig != null) {
				res.append(", Plugin: "
						+ ((PluginConfig) pluginConfig).isResolvedTo()
						+ " version="
						+ ((PluginConfig) pluginConfig).getVersion());
			}

			if (facetRef != null && facetRef.canResolve()) {
				String facetName = facetRef.resolve().getName();
				res.append(", Facet: " + facetName);
			}

			res.append("]");

			if (context != null) {
				res.append(" (" + context + ")");
			}
			if (hasError && includeHTML)
				res.append("</b>");

			res.append(newline);

			if (!hasError) {
				if (analyzeWarnings(new AnalyzeHandler() {

					public void noRules(PluggablePluginReport ppr) {
						res.append("Warning: Plugin '")
								.append(ppr.getPluginConfig().getLabel())
								.append("' has no rules.").append(newline);
					}

					public void noMatchesArtifacts(RuleReport rr) {
						res.append(
								"Notice: None of the artifact(s) match the specified rule(s) '")
								.append(rr.getName()).append("'.")
								.append(newline);
					}

					public void noMatchesArtifacts() {
						res.append(
								"Warning: None of the artifact(s) match the generator's rule.")
								.append(newline);
					}

				})) {
					res.append("Generation Successful.").append(newline);
				}
			} else if (includeChilds) {
				for (IStatus status : getChildren()) {
					if (includeHTML)
						res.append("<li><span color=\"red\">");
					res.append(getSeverityString(status.getSeverity()) + ": "
							+ status.getMessage());
					res.append(newline);

					Throwable t = status.getException();
					if (t != null) {
						StringWriter writer = new StringWriter();
						t.printStackTrace(new PrintWriter(writer));
						if (t instanceof TigerstripeException) {
							TigerstripeException tsExc = (TigerstripeException) t;
							if (tsExc.getException() != null) {
								tsExc.getException().printStackTrace(
										new PrintWriter(writer));
								res.append(writer.toString());
							}
						}
					}
					res.append(newline);

					if (includeHTML)
						res.append("</span></li>");
				}
			}

		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
		return res.toString();
	}

	static interface AnalyzeHandler {

		AnalyzeHandler EMPTY = new AnalyzeHandler() {

			public void noRules(PluggablePluginReport ppr) {
			}

			public void noMatchesArtifacts(RuleReport rr) {
			}

			public void noMatchesArtifacts() {
			}

		};

		void noRules(PluggablePluginReport ppr);

		void noMatchesArtifacts(RuleReport rr);

		void noMatchesArtifacts();

	}

	private boolean analyzeWarnings(AnalyzeHandler handler) {
		boolean success = true;
		if (report instanceof PluggablePluginReport) {
			PluggablePluginReport ppr = (PluggablePluginReport) report;
			List<RuleReport> childReports = new ArrayList<RuleReport>(
					ppr.getChildReports());

			for (Iterator<RuleReport> it = childReports.iterator(); it
					.hasNext();) {
				RuleReport rr = it.next();
				if (rr == null) {
					it.remove();
				}
			}
			if (childReports.isEmpty()) {
				handler.noRules(ppr);
				success = false;
			} else {
				boolean nothingMatch = true;
				for (RuleReport rr : childReports) {
					Rule rule = rr.getRule();
					if (rule instanceof IGlobalRule || rule instanceof IModelRule) {
						nothingMatch = false;
						continue;
					}
					if (rr.getMatchedArtifacts().isEmpty()) {
						handler.noMatchesArtifacts(rr);
					} else {
						nothingMatch = false;
					}
				}
				if (nothingMatch) {
					handler.noMatchesArtifacts();
				}
				success = !nothingMatch;
			}
		}
		return success;
	}

	public boolean hasWarning() {
		return !analyzeWarnings(AnalyzeHandler.EMPTY);
	}

	protected String getSeverityString(int severity) {
		switch (severity) {
		case IStatus.ERROR:
			return "Error";
		case IStatus.WARNING:
			return "Warning";
		case IStatus.INFO:
			return "Info";
		default:
			return "Unknown";
		}
	}

	public void setReport(IPluginReport report) {
		this.report = report;
	}

	public IPluginReport getReport() {
		return report;
	}
}
