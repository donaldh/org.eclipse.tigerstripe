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
package org.eclipse.tigerstripe.workbench.internal.api.examples;

import java.io.File;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj.IOssjEntitySpecifics;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj.IStandardSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.model.artifacts.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * Example showing out to create an artifact an persist it through the
 * Tigerstripe API
 * 
 * @author Eric Dillon
 * 
 */
public class CreateArtifact {

	public CreateArtifact() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void body() {
		try {
			IProjectDetails details = TigerstripeCore.makeProjectDetails();
			details.setName("MyTSProject");
			ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
					.createProject(details, null, ITigerstripeModelProject.class,
							null, new NullProgressMonitor());

			IArtifactManagerSession artifactMgrSession = project
					.getArtifactManagerSession();

			// the list of artifacts
			// String[] artifactTypes =
			// artifactMgrSession.getSupportedArtifacts();

			IManagedEntityArtifact artifact = (IManagedEntityArtifact) artifactMgrSession
					.makeArtifact(IManagedEntityArtifact.class.getName());

			// We're assuming here that the src/com dirs exist...
			File targetFile = new File(
					"C:/JWorkspace/runtime-EclipseApplication/MyTSProject/src/com/Entity.java");

			artifact.setFullyQualifiedName("com.Entity");
			// FileWriter writer = new FileWriter(targetFile);
			// artifact.write(writer);

			artifactMgrSession.addArtifact(artifact);
			artifact.doSave(new NullProgressMonitor());
			// done!

			// Now to set up some of the OSS/J specifics...
			IStandardSpecifics specifics = artifact.getIStandardSpecifics();

			// At this point you've got to know what specifics to expect.
			// Here, we got the specifics from an IManagedEntityArtifact,
			// so the specifics will be
			IOssjEntitySpecifics entitySpecs = (IOssjEntitySpecifics) specifics;

			// THen it is possible to set specifics
			entitySpecs.setPrimaryKey("String");

		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
			// } catch ( IOException e ) {
			// TigerstripeRuntime.logErrorMessage("IOException detected", e);
		}
	}
}
