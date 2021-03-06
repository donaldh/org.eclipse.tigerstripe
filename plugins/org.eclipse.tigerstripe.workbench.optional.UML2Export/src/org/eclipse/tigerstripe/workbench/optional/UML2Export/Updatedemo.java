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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;

public class Updatedemo {

	private IArtifactManagerSession mgrSession;

	private ITigerstripeModelProject tsProject;

	/** constructor */
	public Updatedemo() {
	}

	public void testFieldTypes(String tSProjectName)
			throws TigerstripeException {

		try {
			IResource tsContainer = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(new Path(tSProjectName));

			URI projectURI = tsContainer.getLocationURI();
			tsProject = (ITigerstripeModelProject) TigerstripeCore
					.findProject(projectURI);
			this.mgrSession = tsProject.getArtifactManagerSession();
			this.mgrSession.refresh(true, new NullProgressMonitor());
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

			for (IField field : artifact.getFields()) {
				IType type = field.getType();
				TigerstripeRuntime.logInfoMessage(field.getName() + " "
						+ type.getName());
			}
			/*
			 * for (IMethod method : artifact.getMethods()) { for (int j = 0; j <
			 * method.getIArguments().length; j++) { IType type =
			 * method.getIArguments()[j].getType();
			 * TigerstripeRuntime.logInfoMessage(method
			 * .getIArguments()[j].getName() + " " + type.getName()); } }
			 */

		}

	}

}
