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
package org.eclipse.tigerstripe.workbench.ui.eclipse.builder;

import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.tigerstripe.api.IPluginReference;
import org.eclipse.tigerstripe.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.api.project.IDependency;
import org.eclipse.tigerstripe.api.project.IProjectDetails;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.plugin.PackageToSchemaMapper;
import org.eclipse.tigerstripe.core.plugin.XmlPluginRef;
import org.eclipse.tigerstripe.core.plugin.PackageToSchemaMapper.PckXSDMapping;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.utils.TigerstripeProgressMonitor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.TSExplorerUtils;

public class DescriptorAuditor {

	private IProgressMonitor monitor = new NullProgressMonitor();

	private IProject project;

	private IFile projectDescriptor;

	public DescriptorAuditor(IProject project) {
		this.project = project;
	}

	public void run(IProgressMonitor monitor) {

		this.monitor = monitor;

		ITigerstripeProject tsProject = (ITigerstripeProject) TSExplorerUtils
				.getProjectHandleFor(project);
		projectDescriptor = project
				.getFile(ITigerstripeConstants.PROJECT_DESCRIPTOR);
		if (projectDescriptor == null) {
			TigerstripeProjectAuditor.reportError("Project '"
					+ project.getName() + "' has no Tigerstripe descriptor ("
					+ ITigerstripeConstants.PROJECT_DESCRIPTOR + ")", project,
					222);
		}

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

	private void checkProjectDependencies(ITigerstripeProject tsProject,
			IProgressMonitor monitor) {
		try {
			IDependency[] dependencies = tsProject.getDependencies();

			IDependency oldLegacyOSSJ = null;
			for (int i = 0; i < dependencies.length; i++) {

				if (!dependencies[i].isValid(new TigerstripeProgressMonitor(
						monitor))) {
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
				tsProject.removeDependency(oldLegacyOSSJ,
						new TigerstripeProgressMonitor(monitor));
				tsProject.doSave();
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private void checkFacetReferences(ITigerstripeProject tsProject) {
		try {
			for (IFacetReference ref : tsProject.getFacetReferences()) {
				if (!ref.canResolve()) {
					TigerstripeProjectAuditor.reportError(
							"Facet '" + ref.getProjectRelativePath()
									+ "' referenced in project '"
									+ tsProject.getProjectLabel()
									+ " cannot be found.", projectDescriptor,
							222);
				} else if (ref.getGenerationDir() == null
						|| ref.getGenerationDir().trim().length() == 0) {
					TigerstripeProjectAuditor
							.reportWarning(
									"Facet '"
											+ ref.getProjectRelativePath()
											+ "' referenced in project '"
											+ tsProject.getProjectLabel()
											+ " does not have a specific generation directory.",
									projectDescriptor, 222);
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private void checkPluginProperties(ITigerstripeProject tsProject) {
		try {
			IPluginReference[] pluginRefs = tsProject.getPluginReferences();
			for (int i = 0; i < pluginRefs.length; i++) {
				checkPropertiesOnPluginRef(pluginRefs[i]);
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private void checkProjectDetails(ITigerstripeProject tsProject) {
		try {
			IProjectDetails details = tsProject.getProjectDetails();

			if (details.getName() == null || details.getName().length() == 0) {
				TigerstripeProjectAuditor.reportWarning("Project "
						+ project.getName() + " has no 'Project Name'",
						projectDescriptor, 222);
			}

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
			IStatus packStatus = JavaConventions.validatePackageName(pack);
			if (!packStatus.isOK()) {
				if (packStatus.getSeverity() == IStatus.ERROR) {
					TigerstripeProjectAuditor.reportError(
							"Invalid Default Artifact Package in project '"
									+ details.getName() + "' (" + pack + ").",
							projectDescriptor, 222);
				}
			}

			// Checking Mandatory Properties
			for (int i = 0; i < IProjectDetails.MANDATORY_PROPERTIES.length; i++) {
				String prop = details.getProperties().getProperty(
						IProjectDetails.MANDATORY_PROPERTIES[i]);
				if (prop == null || "".equals(prop)) {
					TigerstripeProjectAuditor.reportError("The '"
							+ IProjectDetails.MANDATORY_PROPERTIES[i]
							+ "' is not set in project '" + details.getName(),
							projectDescriptor, 222);
				}
			}

		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private void checkPropertiesOnPluginRef(IPluginReference ref) {
		if (ref.isEnabled()) {
			Properties presentProps = ref.getProperties();

			String[] definedProps = ref.getDefinedProperties();
			for (int i = 0; i < definedProps.length; i++) {
				if (presentProps.getProperty(definedProps[i]) == null
						|| presentProps.getProperty(definedProps[i]).length() == 0) {
					TigerstripeProjectAuditor.reportError("Property '"
							+ definedProps[i] + "' is undefined.",
							projectDescriptor, 222);
				}
			}
		}

		// FIXME: this should not be implemented here
		// Check XmlPlugin Mapping properties
		if (ref instanceof XmlPluginRef) {
			XmlPluginRef xmlRef = (XmlPluginRef) ref;
			PackageToSchemaMapper mapper = xmlRef.getMapper();

			if (mapper.useDefaultMapping()) {
				String str = mapper.getDefaultSchemaName();
				if (str == null || str.trim().length() == 0) {
					TigerstripeProjectAuditor.reportError(
							"XML Schema name undefined for project '"
									+ project.getName() + "'.",
							projectDescriptor, 222);
				}

				str = mapper.getDefaultSchemaLocation();
				if (str == null || str.trim().length() == 0) {
					TigerstripeProjectAuditor.reportError(
							"XML Schema location undefined for project '"
									+ project.getName() + "'.",
							projectDescriptor, 222);
				}

				str = mapper.getTargetNamespace();
				if (str == null || str.trim().length() == 0) {
					TigerstripeProjectAuditor.reportError(
							"XML Schema namespace undefined for project '"
									+ project.getName() + "'.",
							projectDescriptor, 222);
				}

			} else {
				PckXSDMapping[] mappings = mapper.getMappings();
				if (mappings == null || mappings.length == 0) {
					TigerstripeProjectAuditor.reportError(
							"No Package-XSD mapping defined for project '"
									+ project.getName() + "'.",
							projectDescriptor, 222);
				}
			}
		}

	}

	private void alignProjectDependenciesWithEclipseClasspath(
			ITigerstripeProject project, IProject eclipseProject,
			IProgressMonitor monitor) {
		ClasspathUpdater.alignDependenciesWithEclipseClasspath(project,
				eclipseProject, monitor);
	}

}
