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
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.contract.useCase.IUseCaseReference;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

public class UseCaseProcessingResult extends PluginRunStatus {

	private IUseCaseReference useCaseRef;

	public UseCaseProcessingResult(IUseCaseReference useCaseRef,
			ITigerstripeProject project, RunConfig config,
			IFacetReference facetRef) {
		super(null, project, config, facetRef);
		this.useCaseRef = useCaseRef;
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
			String projectType = "Project";
			if (getProject() instanceof ITigerstripeModuleProject) {
				projectType = "Module";
			}

			if (hasError && includeHTML)
				buf.append("<b>");
			buf.append("[" + projectType + ": "
					+ getProject().getProjectDetails().getName()
					+ ", Use Case: " + useCaseRef.getProjectRelativePath());

			if (getFacetReference() != null && getFacetReference().canResolve()) {
				String facetName = getFacetReference().resolve().getName();
				buf.append(", Facet: " + facetName);
			}

			buf.append("]");

			if (getContext() != null) {
				buf.append(" (" + getContext() + ")");
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
				for (IStatus error : getChildren()) {
					if (includeHTML)
						buf.append("<li><span color=\"red\">");
					buf.append(getSeverityString(error.getSeverity()) + ": "
							+ error.getMessage());
					if (includeHTML)
						buf.append("<br/>");
					else
						buf.append("\n");
					buf.append(error.getException().getLocalizedMessage());
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
