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
package org.eclipse.tigerstripe.workbench.internal.api.impl;

import java.io.File;
import java.io.StringReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.impl.pluggable.M0GeneratorProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.impl.pluggable.TigerstripePluginProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.project.IPhantomTigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.project.Dependency;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.GeneratorProjectDescriptor;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM0GeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;

public class ProjectSessionImpl {

	private Map projectMappedByURIs = new HashMap();

	private TigerstripePhantomProjectHandle phantomHandle = null;

	public ProjectSessionImpl() {
		super();
	}

	public String[] getSupportedTigerstripeProjects() {
		return new String[] { TigerstripeOssjProjectHandle.class.getName(),
				ITigerstripeM1GeneratorProject.class.getName(),
				ITigerstripeM0GeneratorProject.class.getName() };
	}

	public IAbstractTigerstripeProject makeTigerstripeProject(URI projectURI)
			throws TigerstripeException {
		return makeTigerstripeProject(projectURI, null);
	}

	/**
	 * 
	 * @param projectURI
	 *            - The URI of the project container
	 * @param projectType
	 *            - the project type.
	 * 
	 * @throws UnsupportedOperationException
	 *             if the projectType is not supported
	 * @throws org.eclipse.tigerstripe.workbench.TigerstripeException
	 *             if any other error occured.
	 */
	public IAbstractTigerstripeProject makeTigerstripeProject(URI projectURI,
			String projectType) throws UnsupportedOperationException,
			TigerstripeException {

		if (projectMappedByURIs.containsKey(projectURI)) {

			AbstractTigerstripeProjectHandle result = null;
			// Check that the URI points to an existing resource
			File uriFile = new File(projectURI);
			if (!uriFile.exists()) {
				// The underlying project doesn't exist
				// we invalidate the project in the cache and create a new
				// handle.
				// The handle will be validated and marked as invalid later.
				projectMappedByURIs.remove(projectURI);
				result = new TigerstripeOssjProjectHandle(projectURI);
				projectMappedByURIs.put(projectURI, result);
			} else {
				result = (AbstractTigerstripeProjectHandle) projectMappedByURIs
						.get(projectURI);
				// Check that the handle is not stale
				// TODO: backing out this change for now as it caused 530 and
				// 531
				// what should be done is tstamp on the tigerstripe.xml not the
				// project
				// dir.
				// long tstamp = uriFile.lastModified();
				// if ( result instanceof ITigerstripeProject &&
				// result.handleTStamp() != tstamp) {
				// // not checking for Plugin project for now.
				// projectMappedByURIs.remove(projectURI);
				// result = new TigerstripeOssjProjectHandle(projectURI);
				// projectMappedByURIs.put(projectURI, result);
				// }
			}

			return (IAbstractTigerstripeProject) projectMappedByURIs
					.get(projectURI);
		} else if (projectType == null) {
			IAbstractTigerstripeProject result = null;
			projectType = findProjectType(projectURI);
			if (projectType.equals(ITigerstripeM1GeneratorProject.class
					.getName())) {
				// TODO select the right type
				result = new TigerstripePluginProjectHandle(projectURI);
			} else if (projectType.equals(ITigerstripeM0GeneratorProject.class
					.getName())) {
				result = new M0GeneratorProjectHandle(projectURI);
			} else {
				result = new TigerstripeOssjProjectHandle(projectURI);
			}
			projectMappedByURIs.put(projectURI, result);
			return result;
		} else {
			IAbstractTigerstripeProject result = null;
			if (ITigerstripeM1GeneratorProject.class.getName().equals(
					projectType)) {
				result = new TigerstripePluginProjectHandle(projectURI);
			} else {
				result = new TigerstripeOssjProjectHandle(projectURI);
			}
			projectMappedByURIs.put(projectURI, result);
			return result;
		}
	}

	public IDependency makeIDependency(String absolutePath)
			throws TigerstripeException {
		return new Dependency(absolutePath);
	}

	/**
	 * Tries and locate the project descriptor for this project
	 * 
	 * @return
	 */
	protected String findProjectType(URI projectContainerURI) {
		File projectContainer = new File(projectContainerURI);
		if (projectContainer != null && projectContainer.exists()
				&& projectContainer.isDirectory()) {
			String[] files = projectContainer.list();

			for (String file : files) {
				if (file.endsWith(ITigerstripeConstants.PROJECT_DESCRIPTOR))
					return TigerstripeOssjProjectHandle.class.getName();
				else if (file.endsWith(ITigerstripeConstants.PLUGIN_DESCRIPTOR))
					return ITigerstripeM1GeneratorProject.class.getName();
				else if (file
						.endsWith(ITigerstripeConstants.M0_GENERATOR_DESCRIPTOR))
					return ITigerstripeM0GeneratorProject.class.getName();
			}
		}
		return "";
	}

	public IPhantomTigerstripeProject getPhantomProject()
			throws TigerstripeException {
		if (phantomHandle == null) {
			phantomHandle = new TigerstripePhantomProjectHandle();
		}

		return phantomHandle;
	}

	public void removeFromCache(IAbstractTigerstripeProject project) {
		if (project != null && project.getName() != null) {
			for (Iterator<IAbstractTigerstripeProject> iter = projectMappedByURIs
					.values().iterator(); iter.hasNext();) {
				IAbstractTigerstripeProject proj = iter.next();
				if (project.getName().equals(proj.getName())) {
					iter.remove();
				}
			}
		}
	}

	public void refreshCacheFor(URI uri,
			IAbstractTigerstripeProject workingCopy, IProgressMonitor monitor)
			throws TigerstripeException {
		if (projectMappedByURIs.containsKey(uri)) {
			AbstractTigerstripeProjectHandle result = (AbstractTigerstripeProjectHandle) projectMappedByURIs
					.get(uri);
			if (result instanceof TigerstripeProjectHandle
					&& workingCopy instanceof TigerstripeProjectHandle) {
				// 
				TigerstripeProjectHandle handle = (TigerstripeProjectHandle) result;
				TigerstripeProjectHandle workingHandle = (TigerstripeProjectHandle) workingCopy;
				TigerstripeProject project = handle.getTSProject();

				boolean hasDifferentDepOrRef = !project.hasSameDependencies(
						workingHandle.getTSProject(), false)
						|| !project.hasSameReferences(workingHandle
								.getTSProject(), false);

				StringReader reader = new StringReader(workingHandle
						.getTSProject().asText());
				project.parse(reader);

				// Bug #637
				// Only when a change in dependencies or references, refresh and
				// provide user feedback
				if (hasDifferentDepOrRef) {
					handle.getArtifactManagerSession()
							.refreshAll(true, monitor);
					handle.getArtifactManagerSession().updateCaches(monitor);// this
					// should be
					// part of
					// REFRESHALL
				}
			} else if (result instanceof ITigerstripeM1GeneratorProject
					&& workingCopy instanceof ITigerstripeM1GeneratorProject) {
				TigerstripePluginProjectHandle handle = (TigerstripePluginProjectHandle) result;
				TigerstripePluginProjectHandle workingHandle = (TigerstripePluginProjectHandle) workingCopy;
				GeneratorProjectDescriptor project = handle.getDescriptor();

				StringReader reader = new StringReader(workingHandle
						.getDescriptor().asText());
				project.parse(reader);
			}
		}
	}
}
