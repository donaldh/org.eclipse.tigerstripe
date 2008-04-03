/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.releng.ant;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.eclipse.ant.core.AntCorePlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.tigerstripe.releng.ant.builds.schema.Build;
import org.eclipse.tigerstripe.releng.ant.builds.schema.Builds;

/**
 * This task allows to update a Builds.xml descriptor
 * 
 * Ultimately, a build is uniquely identified a {buildType, tStamp} pair.
 * 
 * <tigerstripe.updateBuilds buildsFile="build.xml"> </tigerstripe.updateBuilds>
 * 
 * @author erdillon
 * 
 */
public class AddBuild extends Task {

	private String buildsFile = null;
	private String newBuildFile = null;

	/**
	 * A setter for the buildsFile attribute
	 * 
	 * @param buildsFile
	 */
	public void setBuildsFile(String buildsFile) {
		this.buildsFile = buildsFile;
	}

	public void setNewBuildFile(String newBuildFile) {
		this.newBuildFile = newBuildFile;
	}

	/**
	 * Returns the AntRun progress monitor or a NullProgress monitor if it is
	 * not available.
	 * 
	 * @return
	 */
	protected IProgressMonitor getProgressMonitor() {
		IProgressMonitor monitor = (IProgressMonitor) getProject()
				.getReferences().get(AntCorePlugin.ECLIPSE_PROGRESS_MONITOR);
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		return monitor;
	}

	@Override
	public void execute() throws BuildException {
		IProgressMonitor monitor = getProgressMonitor();
		monitor.beginTask("Updating builds.xml", 10);
		File baseDir = getProject().getBaseDir();

		Builds builds = null;
		Build newBuild = null;

		// Load existing builds.xml descriptor
		Resource buildsRes = new XMLResourceImpl(URI.createFileURI(baseDir
				.getAbsolutePath()
				+ File.separator + buildsFile));
		try {
			buildsRes.load(new HashMap<Object, Object>());
			EList<EObject> content = buildsRes.getContents();
			if (content.size() == 1 && content.get(0) instanceof Builds) {
				builds = (Builds) content.get(0);
			} else {
				throw new BuildException("Invalid builds.xml file.");
			}
		} catch (IOException e) {
			throw new BuildException(e);
		}

		// Load build to be added
		// Load existing builds.xml descriptor
		Resource newBuildRes = new XMLResourceImpl(URI.createFileURI(baseDir
				.getAbsolutePath()
				+ File.separator + newBuildFile));
		try {
			newBuildRes.load(new HashMap<Object, Object>());
			EList<EObject> content = newBuildRes.getContents();
			if (content.size() == 1 && content.get(0) instanceof Build) {
				newBuild = (Build) content.get(0);
			} else {
				throw new BuildException("Invalid new builds.xml file.");
			}
		} catch (IOException e) {
			throw new BuildException(e);
		}

		builds.getBuild().add(newBuild);

		try {
			buildsRes.save(new HashMap<Object, Object>());
		} catch (IOException e) {
			throw new BuildException(e);
		}
		monitor.done();
	}

}
