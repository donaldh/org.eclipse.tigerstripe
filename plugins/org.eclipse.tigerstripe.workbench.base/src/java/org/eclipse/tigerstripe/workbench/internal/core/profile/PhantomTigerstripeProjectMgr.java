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
import java.net.URI;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProjectFactory;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;

/**
 * Singleton class that provide access to the PhantomTigerstripeProject in an
 * install.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class PhantomTigerstripeProjectMgr {

	private PhantomTigerstripeProject phantomProject;

	public PhantomTigerstripeProjectMgr() {
	}

	public void reset() {
		if (phantomProject != null) {
			phantomProject.dispose();
			phantomProject = null;
		}
	}
	
	public synchronized PhantomTigerstripeProject getPhantomProject()
			throws TigerstripeException {
		if (phantomProject == null) {
			createPhantomProject();
		}
		return phantomProject;
	}


	/**
	 * Creates and populate the phantom project based on the current active
	 * profile
	 * @throws TigerstripeException 
	 * 
	 */
	protected void createPhantomProject() throws TigerstripeException {

		URI phantomURI = getPhantomURI();
		// first remove any existing stuff
		File phantomDir = new File(phantomURI);
		if (phantomDir.exists()) {
			Util.deleteDir(phantomDir);
		}
		phantomDir.mkdirs();

		phantomProject = new PhantomTigerstripeProject(phantomDir);
		phantomProject.createEmpty(); // creates an empty Project Structure on
		// disk
		
		// set up the phantom project
		TigerstripeProjectFactory.INSTANCE.getPhantomProject();
	}	

	/**
	 * Returns the URI corresponding to the Phantom Project dir. If this dir
	 * doesn't exist (first run after install) it is created on the fly
	 * 
	 * @return
	 */
	public URI getPhantomURI() {

		String installationRoot = TigerstripeRuntime
				.getTigerstripeRuntimeRoot();
		String phantomPath = installationRoot + File.separator + "phantom";

		File phantomDir = new File(phantomPath);
		return phantomDir.toURI();
	}
}
