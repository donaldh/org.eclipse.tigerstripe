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
package org.eclipse.tigerstripe.workbench.internal.core.profile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.eclipse.core.resources.IResource;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.project.ArtifactRepository;
import org.eclipse.tigerstripe.workbench.internal.core.project.ProjectDetails;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;

/**
 * A Phantom Tigerstripe project is a special type of project that is
 * instantiated internally once (in the install dir) to host the Artifacts
 * defined in the active project.
 * 
 * This project is recreated each time a new profile is deployed thru the
 * PhantomTigerstripeProjectManager
 * 
 * The idea behind the PhantomTigerstripeProject is to have a convenient
 * container that can be used to store anything that should be accessible to any
 * TS project.
 * 
 * The Artifact Mgr attached to this project is automatically referenced in each
 * artifact mgr from real project behind the scenes.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class PhantomTigerstripeProject extends TigerstripeProject {

	PhantomTigerstripeProject(File baseDir) {
		super(baseDir);

		// Create minimalistic project details
		ProjectDetails details = new ProjectDetails(this);
		setProjectDetails(details);
		getProjectDetails().setDescription(
				"Tigerstripe Internal Phantom Project");
		getProjectDetails().setName("Phantom Project");
		getProjectDetails().setModelId("Phantom Project");
		String t =TigerstripeRuntime
			.getProperty(TigerstripeRuntime.TIGERSTRIPE_FEATURE_VERSION);
		if (t==null)
			t= "0.0.0";
		getProjectDetails().setVersion(t);
		getProjectDetails().setProjectOutputDirectory("target"); // this
		// should
		// never be
		// used :-)

		// Create necessary repository
		ArtifactRepository repo = new ArtifactRepository(baseDir);
		String[] includes = { "src/**/*.java" };
		repo.setIncludes(Arrays.asList(includes));

		getArtifactRepositories().add(repo);
	}

	/**
	 * Creates an empty phantom project on disk
	 * 
	 * @return
	 */
	public boolean createEmpty() {
		// At this point, we're assuming that the directory to host the
		// phantom project exists already.
		// All that's left to do is create a default project descriptor file
		String descriptor = getBaseDir().getAbsolutePath() + File.separator
				+ ITigerstripeConstants.PROJECT_DESCRIPTOR;

		try {
			FileWriter writer = new FileWriter(descriptor);
			write(writer);
			reload(true); // not sure this is necessary
		} catch (IOException e) {
			TigerstripeRuntime.logErrorMessage("IOException detected", e);
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
		return true;
	}

	@Override
	public void descriptorChanged(IResource changedDescriptor) {
		//Ignore this - it shouldn't really happen!
	}
	
	
}
