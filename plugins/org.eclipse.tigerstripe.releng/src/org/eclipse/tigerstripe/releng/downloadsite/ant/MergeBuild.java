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
package org.eclipse.tigerstripe.releng.downloadsite.ant;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.tools.ant.BuildException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.tigerstripe.releng.downloadsite.schema.Build;
import org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSite;

/**
 * This task allows to merge a new build into a downloads.xml descriptor
 * 
 * Ultimately, a build is uniquely identified a {stream, buildType, tStamp} pair.
 * 
 * <tigerstripe.mergeBuilds downloadsFile="build.xml">
 * </tigerstripe.updateBuilds>
 * 
 * @author erdillon
 * 
 */
public class MergeBuild extends BaseTask {

	private String buidslFile = null;
	private String newBuildFile = null;

	/**
	 * A setter for the buildsFile attribute
	 * 
	 * @param buildsFile
	 */
	public void setBuildsFile(String buildsFile) {
		this.buidslFile = buildsFile;
	}

	public void setNewBuildFile(String newBuildFile) {
		this.newBuildFile = newBuildFile;
	}

	@Override
	public void execute() throws BuildException {
		super.execute();

		System.out.println("Writing to " + buidslFile);
		DownloadSite builds = null;
		Build newBuild = null;

		// Load existing builds.xml descriptor
		Resource buildsRes = new XMLResourceImpl(URI.createFileURI(buidslFile));
		try {
			buildsRes.load(new HashMap<Object, Object>());
			EList<EObject> content = buildsRes.getContents();
			if (content.size() == 1 && content.get(0) instanceof DownloadSite) {
				builds = (DownloadSite) content.get(0);
			} else {
				throw new BuildException("Invalid downloads.xml file.");
			}
		} catch (IOException e) {
			throw new BuildException(e);
		}

		// Load build to be added
		try {
			newBuild = DownloadSiteHelper.readBuildfile(new File(newBuildFile));
		} catch (IOException e) {
			throw new BuildException(e);
		}

		builds.getBuild().add(newBuild);

		try {
			buildsRes.save(new HashMap<Object, Object>());
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}

}
