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
package org.eclipse.tigerstripe.core.generation;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.api.IPluginReference;
import org.eclipse.tigerstripe.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.api.utils.TigerstripeError;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;

/**
 * A Plugin run result that contains potential messages/exception regarding a
 * plugin run
 * 
 * @author Eric Dillon
 * 
 */
public class PluginRunResult {

	private List<TigerstripeError> errors = new ArrayList<TigerstripeError>();

	private IPluginReference pluginRef;

	private ITigerstripeProject project;

	private IFacetReference facetRef;

	private String context;

	public void setContext(String context) {
		this.context = context;
	}

	public PluginRunResult(IPluginReference pluginRef,
			ITigerstripeProject project, RunConfig config,
			IFacetReference facetRef) {
		this.pluginRef = pluginRef;
		this.project = project;
		this.facetRef = facetRef;
	}

	protected ITigerstripeProject getProject() {
		return project;
	}

	protected IFacetReference getFacetReference() {
		return facetRef;
	}

	protected String getContext() {
		return context;
	}

	public void addError(TigerstripeError error) {
		errors.add(error);
	}

	public TigerstripeError[] getErrors() {
		return errors.toArray(new TigerstripeError[errors.size()]);
	}

	public IPluginReference getPluginRef() {
		return this.pluginRef;
	}

	public boolean errorExists() {
		return TigerstripeError.errorExists(errors);
	}

	public boolean warningExists() {
		return TigerstripeError.warningExists(errors);
	}

	@Override
	public String toString() {
		return toString(false);
	}

	public String toString(boolean includeHTML) {
		StringBuffer buf = new StringBuffer();

		boolean hasError = getErrors().length != 0;
		try {
			String projectType = "Project";
			if (project instanceof ITigerstripeModuleProject) {
				projectType = "Module";
			}

			if (hasError && includeHTML)
				buf.append("<b>");
			buf.append("[" + projectType + ": "
					+ project.getProjectDetails().getName() + ", Plugin: "
					+ pluginRef.getPluginId());

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
				for (TigerstripeError error : getErrors()) {
					if (includeHTML)
						buf.append("<li><span color=\"red\">");
					buf.append(error.getErrorLevel().toString() + ": "
							+ error.getErrorMessage());
					if (includeHTML)
						buf.append("<br/>");
					else
						buf.append("\n");

					if (error.getCorrespondingException() instanceof TigerstripeException) {
						TigerstripeException tsExc = (TigerstripeException) error
								.getCorrespondingException();
						if (tsExc.getException() != null) {
							StringWriter writer = new StringWriter();
							tsExc.getException().printStackTrace(
									new PrintWriter(writer));
							buf.append(writer.toString());
						}
					} else if (error.getCorrespondingException() != null)
						buf.append(error.getCorrespondingException()
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
}
