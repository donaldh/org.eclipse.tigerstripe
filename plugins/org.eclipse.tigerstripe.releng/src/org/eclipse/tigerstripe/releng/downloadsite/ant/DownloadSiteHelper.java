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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.tigerstripe.releng.downloadsite.schema.Build;
import org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSite;

/**
 * Helper class with convenience methods to manipulate Build elements
 * 
 * @author erdillon
 * 
 */
public class DownloadSiteHelper {

	public static String saveAs(Build build, String filename)
			throws IOException {
		Resource buildRes = new XMLResourceImpl(URI.createFileURI(filename));

		buildRes.getContents().add(build);
		buildRes.save(new HashMap<Object, Object>());

		return filename;
	}

	public static String save(Build build) throws IOException {
		Resource res = build.eResource();
		if (res == null) {
			throw new IOException(
					"Build doesn't belong to a file yet. Please use saveAs(..).");
		}

		res.save(new HashMap<Object, Object>());
		return res.getURI().toFileString();
	}

	/**
	 * Reads the given downloads.xml into a DownloadSite element. The file is
	 * expected to contain a single top-level DownloadSite.
	 * 
	 * @param buildsfile
	 * @return
	 * @throws IOException
	 */
	public static DownloadSite readDownloadSite(File downloadsfile)
			throws IOException {
		DownloadSite result = null;
		// Load existing builds.xml descriptor
		Resource buildsRes = new XMLResourceImpl(URI
				.createFileURI(downloadsfile.getAbsolutePath()));
		buildsRes.load(new HashMap<Object, Object>());
		EList<EObject> content = buildsRes.getContents();
		if (content.size() == 1 && content.get(0) instanceof DownloadSite) {
			result = (DownloadSite) content.get(0);
		} else {
			throw new IOException("Invalid downloads.xml file.");
		}
		return result;
	}

	/**
	 * Reads the given buildfile into a Build element. The file is expected to
	 * contain only 1 single Build description.
	 * 
	 * @param buildfile
	 * @return
	 * @throws IOException
	 */
	public static Build readBuildfile(File buildfile) throws IOException {
		Build result = null;
		Resource newBuildRes = new XMLResourceImpl(URI.createFileURI(buildfile
				.getAbsolutePath()));
		newBuildRes.load(new HashMap<Object, Object>());
		EList<EObject> content = newBuildRes.getContents();
		if (content.size() == 1 && content.get(0) instanceof Build) {
			result = (Build) content.get(0);
		} else {
			throw new IOException("Invalid new build.xml file.");
		}
		return result;
	}
}
