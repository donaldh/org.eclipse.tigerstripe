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
package org.eclipse.tigerstripe.workbench.optional.UML2Export;

import java.net.URI;
import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.IextField;
import org.eclipse.tigerstripe.api.external.model.IextMethod;
import org.eclipse.tigerstripe.api.external.model.IextType;
import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;
import org.eclipse.tigerstripe.api.external.queries.IArtifactQuery;
import org.eclipse.tigerstripe.api.external.queries.IQueryAllArtifacts;
import org.eclipse.tigerstripe.api.project.IProjectSession;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

public class Updatedemo {

	private IArtifactManagerSession mgrSession;

	private ITigerstripeProject tsProject;

	/** constructor */
	public Updatedemo() {
	}

	public void testFieldTypes(String tSProjectName)
			throws TigerstripeException {

		try {
			IResource tsContainer = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(new Path(tSProjectName));

			URI projectURI = tsContainer.getLocationURI();
			IProjectSession session = API.getDefaultProjectSession();
			tsProject = (ITigerstripeProject) session.makeTigerstripeProject(
					projectURI, ITigerstripeProject.class.getName());
			this.mgrSession = tsProject.getArtifactManagerSession();
			this.mgrSession.refresh(true, new TigerstripeNullProgressMonitor());
		} catch (Exception e) {
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
			return;
		}

		IArtifactQuery myQuery = mgrSession.makeQuery(IQueryAllArtifacts.class
				.getName());
		myQuery.setIncludeDependencies(false);
		Collection<IArtifact> projectArtifacts = mgrSession
				.queryArtifact(myQuery);
		for (IArtifact artifact : projectArtifacts) {
			TigerstripeRuntime.logInfoMessage("Processing "
					+ artifact.getFullyQualifiedName());
		}

		// Re-pass to add Attributes etc
		for (IArtifact artifact : projectArtifacts) {

			TigerstripeRuntime.logInfoMessage("Artifact Attributes for "
					+ artifact.getFullyQualifiedName());

			IextField[] fields = artifact.getIextFields();
			for (int i = 0; i < fields.length; i++) {
				IextField field = fields[i];
				IextType type = field.getIextType();
				TigerstripeRuntime.logInfoMessage(field.getName() + " "
						+ type.getName());
			}
			IextMethod[] methods = artifact.getIextMethods();
			for (int i = 0; i < methods.length; i++) {
				for (int j = 0; j < methods[i].getIextArguments().length; j++) {
					IextType type = methods[i].getIextArguments()[j].getIType();
					TigerstripeRuntime.logInfoMessage(methods[i]
							.getIextArguments()[j].getName()
							+ " " + type.getName());
				}

			}

		}

	}

}
