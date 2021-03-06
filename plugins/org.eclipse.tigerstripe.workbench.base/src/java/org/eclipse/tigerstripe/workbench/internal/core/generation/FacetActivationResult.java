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
package org.eclipse.tigerstripe.workbench.internal.core.generation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class FacetActivationResult extends PluginRunStatus {

	public FacetActivationResult(ITigerstripeModelProject project, M1RunConfig config,
			IFacetReference facetRef) {
		super(null, project, config, facetRef, "Facet contains inconsistencies");
	}

	@Override
	public String toString() {
		return toString(false);
	}

	@Override
	public String toString(boolean includeHTML) {
		StringBuffer buf = new StringBuffer();

		boolean hasError = !isOK();
		try {
			String projectType = "Inconsistent Facet:";

			if (hasError && includeHTML)
				buf.append("<b>");
			buf.append("[" + projectType + ": "
					+ getFacetReference().resolve().getName());
			buf.append("]");

			if (hasError && includeHTML)
				buf.append("</b>");
			if (includeHTML)
				buf.append("<br/>");
			else
				buf.append("\n");

			for (IStatus error : getChildren()) {
				if (includeHTML)
					buf.append("<li><span color=\"red\">");
				if (!includeHTML)
					buf.append(" - ");
				buf.append(getSeverityString(error.getSeverity()) + ": "
						+ error.getMessage());
				if (includeHTML)
					buf.append("<br/>");
				else
					buf.append("\n");
				if (includeHTML)
					buf.append("</span></li>");
			}

			buf.append("Generated content may be inconsistent");
			if (includeHTML)
				buf.append("<br/>");
			else
				buf.append("\n");

		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
		return buf.toString();
	}

}
