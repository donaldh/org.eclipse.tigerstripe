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
package org.eclipse.tigerstripe.eclipse.wizards.generate;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.API;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.TigerstripeLicenseException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginRef;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.ossj.ws.OssjWsdlPlugin;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;

/**
 * This class performs a set of last minute tests right before a plugin is
 * triggered for generation and provides potential user feedback
 * 
 * @author Eric Dillon
 * 
 */
public class GenerateAuditor {

	/**
	 * Performs last minute check on the given pluginRef. returns false if the
	 * corresponding plugin shouldn't be run
	 * 
	 * @param shell
	 * @param ref
	 * @return
	 */
	public static Status checkRefBeforeTrigger(Shell shell, PluginRef ref) {
		return checkForWSDL(shell, ref);
	}

	private static Status checkForWSDL(Shell shell, PluginRef ref) {

		// If no Session Artifact popup and block WSDL Generation.
		if (OssjWsdlPlugin.PLUGIN_ID.equals(ref.getPluginId())) {
			try {
				ITigerstripeProject handle = (ITigerstripeProject) API
						.getDefaultProjectSession().makeTigerstripeProject(
								ref.getProject().getBaseDir().toURI(), null);
				ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) handle
						.getArtifactManagerSession();

				ArtifactManager artifactMgr = session.getArtifactManager();

				Collection sessions = artifactMgr.getArtifactsByModel(
						SessionFacadeArtifact.MODEL, false,
						new TigerstripeNullProgressMonitor());
				if (sessions.isEmpty()) {
					Status status = new Status(
							IStatus.WARNING,
							TigerstripePluginConstants.PLUGIN_ID,
							222,
							"No Session Facade Artifact defined in this project: no Web Service(WSDL) was generated.",
							null);
					return status;
				}
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			} catch (TigerstripeLicenseException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeLicenseException detected", e);
			}
			return null;
		} else
			return null;
	}
}
