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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.generation.M1RunConfig;
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

	public void setContext(String context) {
		this.context = context;
	}

	public PluginRunStatus(IPluginConfig pluginConfig,
			ITigerstripeModelProject project, M1RunConfig config,
			IFacetReference facetRef) {
		super(BasePlugin.getPluginId(), 222, "Plugin Run Status", null);
		this.pluginConfig = pluginConfig;
		this.project = project;
		this.facetRef = facetRef;
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
	public String toString() {
		return toString(false);
	}

	public String toString(boolean includeHTML) {
		super.toString();
		StringBuffer buf = new StringBuffer();

		boolean hasError = !isOK();
		try {
			String projectType = "Project";
			if (project instanceof ITigerstripeModuleProject) {
				projectType = "Module";
			}

			if (hasError && includeHTML)
				buf.append("<b>");
		    buf.append("[" );
		    buf.append(this.message);
		    
			if (project != null) { 
			    buf.append(projectType + ": " + project.getProjectLabel() );
			}
			if (pluginConfig != null){
					buf.append(", Plugin: "
					+ pluginConfig.getPluginId());
			}

			if (facetRef != null && facetRef.canResolve()) {
				String facetName = facetRef.resolve().getName();
				buf.append(", Facet: " + facetName);
			}

			buf.append("]");

			if (context != null) {
				buf.append(" (" + context + ")");
			}
			if (hasError && includeHTML)
				buf.append("</b>");
			if (includeHTML)
				buf.append("<br/>");
			else
				buf.append("\n");

			if (!hasError) {
				buf.append("Generation Successful.");
				if (includeHTML)
					buf.append("<br/>");
				else
					buf.append("\n");
			} else {
				for (IStatus status : getChildren()) {
					if (includeHTML)
						buf.append("<li><span color=\"red\">");
					buf.append(getSeverityString(status.getSeverity()) + ": "
							+ status.getMessage());
					if (includeHTML)
						buf.append("<br/>");
					else
						buf.append("\n");

					if (status.getException() instanceof TigerstripeException) {
						TigerstripeException tsExc = (TigerstripeException) status
								.getException();
						if (tsExc.getException() != null) {
							StringWriter writer = new StringWriter();
							tsExc.getException().printStackTrace(
									new PrintWriter(writer));
							buf.append(writer.toString());
						}
					} else if (status.getException() != null)
						buf.append(status.getException()
								.getLocalizedMessage());
					if (includeHTML)
						buf.append("<br/>");
					else
						buf.append("\n");

					if (includeHTML)
						buf.append("</span></li>");
				}
			}

		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
		return buf.toString();
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
}
