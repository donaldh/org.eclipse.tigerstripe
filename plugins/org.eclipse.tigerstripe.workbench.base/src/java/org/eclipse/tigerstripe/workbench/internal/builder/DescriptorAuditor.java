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
package org.eclipse.tigerstripe.workbench.internal.builder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class DescriptorAuditor {

	private IProject project;

	private IFile projectDescriptor;

	public DescriptorAuditor(IProject project) {
		this.project = project;
	}

	public void run(IProgressMonitor monitor) {

		ITigerstripeModelProject tsProject = (ITigerstripeModelProject) project
				.getAdapter(ITigerstripeModelProject.class);
		projectDescriptor = project
				.getFile(ITigerstripeConstants.PROJECT_DESCRIPTOR);
		if (projectDescriptor == null) {
			TigerstripeProjectAuditor.reportError("Project '"
					+ project.getName() + "' has no Tigerstripe descriptor ("
					+ ITigerstripeConstants.PROJECT_DESCRIPTOR + ")", project,
					222);
		}

		TigerstripeProjectAuditor.deleteAuditMarkers(projectDescriptor,
				IResource.DEPTH_ZERO);
		TigerstripeProjectAuditor.deleteAuditMarkers(project,
				IResource.DEPTH_ZERO);

		if (tsProject != null) {
			monitor.beginTask("Checking project descriptor", 70);
			checkProjectDetails(tsProject);
			monitor.worked(10);
			checkProjectDependencies(tsProject, monitor);
			monitor.worked(10);
			monitor.worked(20);
			alignProjectDependenciesWithEclipseClasspath(tsProject, project,
					monitor);
			checkPluginProperties(tsProject);
			monitor.worked(20);
			checkFacetReferences(tsProject);
			monitor.done();
		} else {
			TigerstripeProjectAuditor.reportError("Project '"
					+ project.getName() + "' is invalid", project, 222);
		}
	}

	private void checkProjectDependencies(ITigerstripeModelProject tsProject,
			IProgressMonitor monitor) {
		try {
			IDependency[] dependencies = tsProject.getDependencies();

			IDependency oldLegacyOSSJ = null;
			for (int i = 0; i < dependencies.length; i++) {

				if (!dependencies[i].isValid(monitor)) {
					TigerstripeProjectAuditor.reportError(
							"Missing dependency '" + dependencies[i].getPath()
									+ "'", project, 222);
				} else {
					// @see #299
					if (dependencies[i].getPath().startsWith(
							"distrib.core.model-1.")) {
						oldLegacyOSSJ = dependencies[i];
					}
				}
			}

			// @see #299
			if (oldLegacyOSSJ != null) {
				tsProject.removeDependency(oldLegacyOSSJ, monitor);
				// doSave();
				tsProject.commit(monitor);
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
	}

	private void checkFacetReferences(ITigerstripeModelProject tsProject) {
		try {
			for (IFacetReference ref : tsProject.getFacetReferences()) {
				if (!ref.canResolve()) {
					TigerstripeProjectAuditor.reportError("Facet '"
							+ ref.getProjectRelativePath()
							+ "' referenced in project '" + tsProject.getName()
							+ " cannot be found.", projectDescriptor, 222);
				} else if (ref.getGenerationDir() == null
						|| ref.getGenerationDir().trim().length() == 0) {
					TigerstripeProjectAuditor
							.reportWarning(
									"Facet '"
											+ ref.getProjectRelativePath()
											+ "' referenced in project '"
											+ tsProject.getName()
											+ " does not have a specific generation directory.",
									projectDescriptor, 222);
				}
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
	}

	private void checkPluginProperties(ITigerstripeModelProject tsProject) {
		try {
			IPluginConfig[] pluginConfigs = tsProject.getPluginConfigs();
			for (int i = 0; i < pluginConfigs.length; i++) {
				checkPropertiesOnPluginConfig(pluginConfigs[i]);
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
	}

	private void checkProjectDetails(ITigerstripeModelProject tsProject) {
		try {
			IProjectDetails details = tsProject.getProjectDetails();

			if (details.getVersion() == null
					|| details.getVersion().length() == 0) {
				TigerstripeProjectAuditor.reportWarning("Project "
						+ project.getName() + " has no 'Project Version'",
						projectDescriptor, 222);
			}

			if (details.getDescription() == null
					|| details.getDescription().length() == 0) {
				TigerstripeProjectAuditor.reportInfo("Project "
						+ project.getName() + " has no 'Project Description'",
						projectDescriptor, 222);
			}

			// validate default artifact package is legal
			String pack = details.getProperties().getProperty(
					IProjectDetails.DEFAULTARTIFACTPACKAGE_PROP);
			IStatus packStatus = JavaConventions.validatePackageName(pack,
					CompilerOptions.VERSION_1_4, CompilerOptions.VERSION_1_4);
			if (!packStatus.isOK()) {
				if (packStatus.getSeverity() == IStatus.ERROR) {
					TigerstripeProjectAuditor
							.reportError(
									"Invalid Default Artifact Package in project '"
											+ tsProject.getName() + "' ("
											+ pack + ").", projectDescriptor,
									222);
				}
			}

			// Checking Mandatory Properties
			for (int i = 0; i < IProjectDetails.MANDATORY_PROPERTIES.length; i++) {
				String prop = details.getProperties().getProperty(
						IProjectDetails.MANDATORY_PROPERTIES[i]);
				if (prop == null || "".equals(prop)) {
					TigerstripeProjectAuditor.reportError(
							"The '" + IProjectDetails.MANDATORY_PROPERTIES[i]
									+ "' is not set in project '"
									+ tsProject.getName(), projectDescriptor,
							222);
				}
			}

		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
	}

	private void checkPropertiesOnPluginConfig(IPluginConfig ref) {
		if (ref.isEnabled()) {
			String[] definedProps = ref.getDefinedProperties();

			for (int i = 0; i < definedProps.length; i++) {
				if (ref.getProperty(definedProps[i]) == null) {
					TigerstripeProjectAuditor.reportError("Property '"
							+ definedProps[i] + "' is undefined.",
							projectDescriptor, 222);
				}
			}
			// TODO : Check the Property TYPE is the same?
		}

	}

	private void alignProjectDependenciesWithEclipseClasspath(
			ITigerstripeModelProject project, IProject eclipseProject,
			IProgressMonitor monitor) {
		ClasspathUpdater.alignDependenciesWithEclipseClasspath(project,
				eclipseProject, monitor);
	}

}
