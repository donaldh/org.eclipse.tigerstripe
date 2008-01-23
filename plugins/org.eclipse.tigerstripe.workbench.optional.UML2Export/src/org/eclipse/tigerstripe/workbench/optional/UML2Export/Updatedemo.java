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
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.model.IField;
import org.eclipse.tigerstripe.api.model.IMethod;
import org.eclipse.tigerstripe.api.model.IType;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.project.IProjectSession;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.api.queries.IArtifactQuery;
import org.eclipse.tigerstripe.api.queries.IQueryAllArtifacts;
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
		Collection<IAbstractArtifact> projectArtifacts = mgrSession
				.queryArtifact(myQuery);
		for (IAbstractArtifact artifact : projectArtifacts) {
			TigerstripeRuntime.logInfoMessage("Processing "
					+ artifact.getFullyQualifiedName());
		}

		// Re-pass to add Attributes etc
		for (IAbstractArtifact artifact : projectArtifacts) {

			TigerstripeRuntime.logInfoMessage("Artifact Attributes for "
					+ artifact.getFullyQualifiedName());

			IField[] fields = artifact.getIFields();
			for (int i = 0; i < fields.length; i++) {
				IField field = fields[i];
				IType type = field.getIType();
				TigerstripeRuntime.logInfoMessage(field.getName() + " "
						+ type.getName());
			}
			IMethod[] methods = artifact.getIMethods();
			for (int i = 0; i < methods.length; i++) {
				for (int j = 0; j < methods[i].getIArguments().length; j++) {
					IType type = methods[i].getIArguments()[j].getIType();
					TigerstripeRuntime.logInfoMessage(methods[i]
							.getIArguments()[j].getName()
							+ " " + type.getName());
				}

			}

		}

	}

}
