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
import org.eclipse.tigerstripe.releng.downloadsite.schema.BuildType;
import org.eclipse.tigerstripe.releng.downloadsite.schema.Detail;
import org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSite;
import org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSiteFactory;

/**
 * This task adds a Release Notes detail to the given new build based on the
 * given downloads.xml
 * 
 * Basically, looking for a previous build in the same stream with the same
 * build type to compute the "between" period to look for in Bugzilla (from the
 * previous build of same type/stream) to newBuild timestamp.
 * 
 * 
 * @author erdillon
 * 
 */
public class UpdateNotesDetails extends BaseTask {

	private String downloadsFile = null;
	private String newBuildFile = null;
	private String php = null;
	private int retainNumber = 5;

	/**
	 * A setter for the buildsFile attribute
	 * 
	 * @param buildsFile
	 */
	public void setDownloadsFile(String downloadsFile) {
		this.downloadsFile = downloadsFile;
	}

	public void setNewBuildFile(String newBuildFile) {
		this.newBuildFile = newBuildFile;
	}

	public void setPhp(String php) {
		this.php = php;
	}

	public void setRetain(String retainNumber) {
		this.retainNumber = Integer.parseInt(retainNumber);
		if (this.retainNumber < 1)
			this.retainNumber = 1;
	}

	@Override
	public void execute() throws BuildException {
		super.execute();

		System.out.println("Writing to " + downloadsFile);
		DownloadSite builds = null;
		Build newBuild = null;

		// Load existing builds.xml descriptor
		Resource buildsRes = new XMLResourceImpl(URI
				.createFileURI(downloadsFile));
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

		// Load build to be updated
		try {
			newBuild = DownloadSiteHelper.readBuildfile(new File(newBuildFile));
		} catch (IOException e) {
			throw new BuildException(e);
		}

		Detail notesDetail = computeReleaseNotes(newBuild, builds);
		newBuild.getDetail().add(notesDetail);

		try {
			DownloadSiteHelper.save(newBuild);
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}

	/**
	 * Computes the release notes details for the given build based on the given
	 * site.
	 * 
	 * Basically looking for any previous build of the same type and same stream
	 * to get the "Since date" and use the build tstamp as the "until date"
	 * 
	 * @param newBuild
	 * @param site
	 * @return
	 */
	protected Detail computeReleaseNotes(Build newBuild, DownloadSite site) {
		Detail result = DownloadSiteFactory.eINSTANCE.createDetail();
		result.setName("Release Notes");
		result.setSummary("Summary of fixed bugzillas.");

		String stream = newBuild.getStream();
		BuildType type = newBuild.getType();
		String toTStamp = newBuild.getTstamp();
		String fromTStamp = "200801010000";

		Build previousBuild = null;
		for (Build build : site.getBuild()) {
			if (build.getType() == type && build.getStream().equals(stream)) {
				if (previousBuild == null) {
					previousBuild = build;
				} else if (Long.parseLong(build.getTstamp()) > Long
						.parseLong(previousBuild.getTstamp())) {
					previousBuild = build;
				}
			}
		}

		if (previousBuild != null) {
			fromTStamp = previousBuild.getTstamp();
		}

		result.setLink(php + "?from=" + fromTStamp + "?to=" + toTStamp + "?build=" + newBuild.getName());
		return result;
	}
}
